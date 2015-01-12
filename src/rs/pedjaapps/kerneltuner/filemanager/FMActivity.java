/*
 * This file is part of the Kernel Tuner.
 *
 * Copyright Predrag Čokulov <predragcokulov@gmail.com>
 *
 * Kernel Tuner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Tuner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
 */
package rs.pedjaapps.kerneltuner.filemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.ui.AbsActivity;
import rs.pedjaapps.kerneltuner.utility.Tools;

public class FMActivity extends AbsActivity
{
	/** Entry type: File */
	public static final int TYPE_FILE = 0;
	/** Entry type: Directory */
	public static final int TYPE_DIRECTORY = 1;
	/** Entry type: Directory Link */
	public static final int TYPE_DIRECTORY_LINK = 2;
	/** Entry type: Block */
	public static final int TYPE_BLOCK = 3;
	/** Entry type: Character */
	public static final int TYPE_CHARACTER = 4;
	/** Entry type: Link */
	public static final int TYPE_LINK = 5;
	/** Entry type: Socket */
	public static final int TYPE_SOCKET = 6;
	/** Entry type: FIFO */
	public static final int TYPE_FIFO = 7;
	/** Entry type: Other */
	public static final int TYPE_OTHER = 8;
	/** Device side file separator. */
	public static final String FILE_SEPARATOR = "/"; //$NON-NLS-1$

    /**
     * Regex pattern to parse result from LIST_FILES_CMD below
     * */
    private static Pattern fileListPattern = Pattern.compile("kt:\\s+(\\d{3})\\s+(\\d+)\\s+(\\d+)\\s+(\\w+)\\s+.*'(.*)'.*\\s+.*'(.*)'.*");//$NON-NLS-1$

    private static final String LIST_FILES_CMD = "CURR_DIR='{path}'; IFS=$'\\n'; for f in $(ls -a $CURR_DIR); do if [ -d $CURR_DIR$f ]; then if [ -h $CURR_DIR$f ]; then echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" dl '\"$f\"' '`readlink -f \"$f\"`'\"; else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" d '\"$f\"' ''\"; fi elif [ -f $CURR_DIR$f ]; then if [ -h $CURR_DIR$f ]; then echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" fl '\"$f\"' '`readlink -f \"$f\"`'\"; else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" f '\"$f\"' ''\"; fi else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" - '\"$f\"' ''\"; fi done";

    private static final String CURR_DIR = "curr_dir";
    private static final String BACKSTACK = "backstack";

    enum SortBy
    {
        name, size, time
    }

	String path;
	FMAdapter fAdapter;
	ListView fListView;
	HashMap<String, Parcelable> listScrollStates = new HashMap<>();
    LinkedList<String> backstack = new LinkedList<>();
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		//supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

		setContentView(R.layout.fm);
		fListView = (ListView) findViewById(R.id.list);

		path = savedInstanceState != null ? savedInstanceState.getString(CURR_DIR) : FILE_SEPARATOR;//Environment.getExternalStorageDirectory().toString();

        fListView.setDrawingCacheEnabled(true);
		fAdapter = new FMAdapter(this, R.layout.fm_row);

		fListView.setAdapter(fAdapter);

		ls(path, false);

		getSupportActionBar().setSubtitle(path);
		fListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
										long arg3)
				{
                    FMEntry entry = fAdapter.getItem(pos);
                    if(entry.getType() == TYPE_DIRECTORY || entry.getType() == TYPE_DIRECTORY_LINK)
                    {
                        Parcelable state = fListView.onSaveInstanceState();
                        listScrollStates.put(path, state);
                        backstack.add(path);
                        path = entry.getType() == TYPE_DIRECTORY_LINK ? entry.getLink() : entry.getPath();
                        validatePath();
                        ls(path, false);
                    }
                    else if(entry.getType() == TYPE_FILE || entry.getType() == TYPE_LINK)
                    {
                        //TODO
                    }
				}
			});
		if(savedInstanceState != null)
		{
            backstack = (LinkedList<String>) savedInstanceState.getSerializable(BACKSTACK);
			Parcelable listState = savedInstanceState.getParcelable("list_position");
			if(listState != null)fListView.post(new RestoreListStateRunnable(listState));
		}
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        APKImageLoader.getInstance(this).cleanup();
        System.gc();
    }

    private void validatePath()
    {
        if(path != null)
        {
            if(path.endsWith(FILE_SEPARATOR))return;
            path = path + FILE_SEPARATOR;
        }
    }

    @Override
	public void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current dropdown position.
		Parcelable state = fListView.onSaveInstanceState();
		outState.putParcelable("list_position", state);
        outState.putString(CURR_DIR, path);
        outState.putSerializable(BACKSTACK, backstack);
	}

	private void ls(final String path, final boolean back)
	{
        //setSupportProgressBarIndeterminateVisibility(true);

		new RootUtils().exec(new RootUtils.CommandCallbackImpl()
		{
			public void onComplete(RootUtils.Status status, String output)
			{
				if(status == RootUtils.Status.success)
				{
					new ATParseOutput(back).execute(output);
				}
				else
				{
					//TODO error
					System.out.println("ls error: " + status);
				}
			}
		}, LIST_FILES_CMD.replace("{path}", path));
	}

	static class FileListComparator implements Comparator<FMEntry>
	{
        SortBy sortBy;
        int order;//1 = ascending, -1 = descending

        public FileListComparator(SortBy sortBy, int order)
        {
            this.sortBy = sortBy;
            this.order = order;
        }

        @Override
		public int compare(FMEntry file1, FMEntry file2)
		{
            if(file1.isFolder() && !file2.isFolder())
            {
                return -1;
            }
            else if(file2.isFolder() && !file1.isFolder())
            {
                return 1;
            }

            switch (sortBy)
            {
                case name:
                    return order * file1.getName().compareToIgnoreCase(file2.getName());

                case time:
                    return order * file1.getDate().compareTo(file2.getDate());

                case size:
                    return order * Long.valueOf(file1.getSize()).compareTo(file2.getSize());

                default:
                    break;
            }
            return 0;
		}

	}



	@Override
	public void onBackPressed()
	{
        String bsPath = backstack.pollLast();
		if (bsPath == null/* || "/".equals(path)*/)
		{
			super.onBackPressed();
		}
		else
		{

			path = bsPath;//path.substring(0, path.lastIndexOf("/"));

			ls(path, true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add(0, 0, 0, getResources().getString(R.string.add));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_action_add);
		item = menu.add(0, 1, 1, getResources().getString(R.string.search));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_action_search);
        item = menu.add(0, 2, 2, getResources().getString(R.string.sort));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_action_sort);
        item = menu.add(0, 3, 3, getResources().getString(R.string.refresh));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_action_refresh);
        item = menu.add(0, 4, 4, getResources().getString(R.string.home));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_action_home);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
		{
	        case 0:
                newDialog();
	        	return true;
	        case 1:
	        	return true;
            case 3:
                ls(path, false);
                return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private class RestoreListStateRunnable implements Runnable
	{
        Parcelable state;

		public RestoreListStateRunnable(Parcelable state)
		{
			this.state = state;
		}
		
		@Override
		public void run()
		{
			fListView.onRestoreInstanceState(state);
			System.out.println("restoring list state");
		}
	}

    private class ATParseOutput extends AsyncTask<String, Void, List<FMEntry>>
    {
        boolean back;

        public ATParseOutput(boolean back)
        {
            this.back = back;
        }

        @Override
        protected List<FMEntry> doInBackground(String... strings)
        {
            final List<FMEntry> e = new ArrayList<>();
            String[] files = strings[0].split("\n");
            for (String line : files)
            {
                Matcher m = fileListPattern.matcher(line);
                if (!m.matches()) continue;
                System.out.println("list files line: " + line);

                FMEntry entry = new FMEntry();
                // get the name
                entry.setName(m.group(5));
                System.out.println("list file nane: " + entry.getName());
                entry.setPath(path + (path.endsWith(FILE_SEPARATOR) ? "" : FILE_SEPARATOR) + entry.getName());
                // get the rest of the groups
                entry.setPermissions(Tools.parseInt(m.group(1), 0));
                entry.setSize(Tools.parseLong(m.group(2), 0));
                entry.setSizeHr(Tools.byteToHumanReadableSize(entry.getSize()));
                entry.setDate(new Date(Tools.parseLong(m.group(3), 0) * 1000));
                entry.setDateHr(Tools.msToDate(entry.getDate().getTime()));
                entry.setLink(m.group(6));
                entry.setMimeType(FMUtils.getFileType(entry.getPath()));
                // and the type
                int objectType = TYPE_OTHER;
                switch (m.group(4))
                {
                    case "d":
                        objectType = TYPE_DIRECTORY;
                        break;
                    case "dl":
                        objectType = TYPE_DIRECTORY_LINK;
                        break;
                    case "f":
                        objectType = TYPE_FILE;
                        break;
                    case "fl":
                        objectType = TYPE_LINK;
                        break;
                }
                entry.setType(objectType);
                e.add(entry);

            }
            Collections.sort(e, new FileListComparator(SortBy.name, 1));

            return e;
        }

        @Override
        protected void onPostExecute(List<FMEntry> e)
        {
            fAdapter.clear();
            fAdapter.addAll(e);
            fAdapter.notifyDataSetChanged();
            if(back)
            {
                Parcelable state = listScrollStates.get(path);
                if(state != null)fListView.post(new RestoreListStateRunnable(state));
            }
			else
			{
				fListView.post(new Runnable()
				{
					@Override
					public void run()
					{
						fListView.setSelection(0);
					}
				});
			}
            //setSupportProgressBarIndeterminateVisibility(false);
            getSupportActionBar().setSubtitle(path);
        }
    }

    private void newDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fm_new_title);
        String[] items = {getString(R.string.file), getString(R.string.folder)};
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });
        builder.show();
    }
}
