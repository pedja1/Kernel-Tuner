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
package rs.pedjaapps.kerneltuner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.FMAdapter;
import rs.pedjaapps.kerneltuner.model.FMEntry;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.Tools;
import siir.es.adbWireless.Utils;

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
	 * Regexp pattern to parse the result from ls.
	 */
	private static Pattern sLsPattern = Pattern.compile("^([bcdlsp-][-r][-w][-xsS][-r][-w][-xsS][-r][-w][-xstST])\\s+(\\S+)\\s+(\\S+)\\s+([\\d\\s,]*)\\s+(\\d{4}-\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.*)$"); //$NON-NLS-1$

    /**
     * Regex pattern to parse result from LIST_FILES_CMD below
     * */
    private static Pattern fileListPattern = Pattern.compile("kt:\\s+(\\d{3})\\s+(\\d+)\\s+(\\d+)\\s+(\\w+)\\s+.*'(.*)'.*\\s+.*'(.*)'.*");//$NON-NLS-1$

    private static final String LIST_FILES_CMD = "CURR_DIR='{path}'; IFS=$'\\n'; for f in $(ls -a); do if [ -d $CURR_DIR$f ]; then if [ -h $CURR_DIR$f ]; then echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" dl '\"$f\"' '`readlink -f \"$f\"`'\"; else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" d '\"$f\"' ''\"; fi elif [ -f $CURR_DIR$f ]; then if [ -h $CURR_DIR$f ]; then echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" fl '\"$f\"' '`readlink -f \"$f\"`'\"; else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" f '\"$f\"' ''\"; fi else echo \"kt: \"`stat -c '%a %s %Y' $CURR_DIR$f`\" - '\"$f\"' ''\"; fi done";

	String path;
	FMAdapter fAdapter;
	ListView fListView;
	HashMap<String, Parcelable> listScrollStates = new HashMap<>();
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

		setContentView(R.layout.fm);
		fListView = (ListView) findViewById(R.id.list);

		path = "/";//Environment.getExternalStorageDirectory().toString();

        fListView.setDrawingCacheEnabled(true);
		fAdapter = new FMAdapter(this, R.layout.fm_row);

		fListView.setAdapter(fAdapter);

		ls(path);

		getActionBar().setTitle(path);
		fListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
										long arg3)
				{
					Parcelable state = fListView.onSaveInstanceState();
					listScrollStates.put(path, state);
					path = fAdapter.getItem(pos).getPath();
					ls(path);
					getActionBar().setTitle(path);
				}
			});
		if(savedInstanceState != null)
		{
			Parcelable listState = savedInstanceState.getParcelable("list_position");
			if(listState != null)fListView.post(new RestoreListStateRunnable(listState));
		}
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// Serialize the current dropdown position.
		Parcelable state = fListView.onSaveInstanceState();
		outState.putParcelable("list_position", state);
		//listScrollStates.put(path, state);
	}

	private void ls(final String path)
	{
		setProgressBarVisibility(true);
		final List<FMEntry> e = new ArrayList<FMEntry>();

		new RootUtils().exec(new RootUtils.CommandCallbackImpl()
		{
			public void onComplete(RootUtils.Status status, String output)
			{
				if(status == RootUtils.Status.success)
				{
					String[] files = output.split("\n");
					for (String line : files)
					{
						Matcher m = fileListPattern.matcher(line);
						if (!m.matches()) continue;
						System.out.println("list files line: " + line);
						
						FMEntry entry = new FMEntry();
						// get the name
						entry.setName(m.group(5));
                        System.out.println("list file nane: " + entry.getName());
						entry.setPath(path + (path.endsWith("/") ? "" : "/") + entry.getName());
						// get the rest of the groups
						entry.setPermissions(Tools.parseInt(m.group(1), 0));
						entry.setSize(m.group(2));
						entry.setSizeHr(Tools.byteToHumanReadableSize(Tools.parseLong(entry.getSize(), 0)));
						entry.setDate(new Date(Tools.parseLong(m.group(3), 0) * 1000));
						entry.setDateHr(Tools.msToDate(entry.getDate().getTime()));
						entry.setLink(m.group(6));
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
					Collections.sort(e, new SortByName());
					Collections.sort(e, new SortFolderFirst());
					fAdapter.clear();
					fAdapter.addAll(e);
					fAdapter.notifyDataSetChanged();
					setProgressBarVisibility(false);
				}
				else
				{
					//TODO error
					System.out.println("ls error: " + status);
				}
			}
		}, LIST_FILES_CMD.replace("{path}", path));
	}

	static class SortFolderFirst implements Comparator<FMEntry>
	{
		@Override
		public int compare(FMEntry p1, FMEntry p2)
		{
	        if (p1.getType() < p2.getType()) return 1;
	        if (p1.getType() > p2.getType()) return -1;
	        return 0;
	    }   

	}

	static class SortByName implements Comparator<FMEntry>
	{
		@Override
		public int compare(FMEntry s1, FMEntry s2)
		{
		    String sub1 = s1.getName();
		    String sub2 = s2.getName();
		    return sub1.compareToIgnoreCase(sub2);
		} 

	}



	@Override
	public void onBackPressed()
	{
		if ("/".equals(path))
		{
			super.onBackPressed();
		}
		else
		{
			path = path.substring(0, path.lastIndexOf("/"));

			ls(path);
			
			getActionBar().setTitle(path);
			Parcelable state = listScrollStates.get(path);
			if(state != null)fListView.post(new RestoreListStateRunnable(state));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//menu.add(0, 0, 0, getResources().getString(R.string.select)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		//menu.add(0, 1, 1, getResources().getString(R.string.cancel)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
		{
	        case 0:
	        	Intent i = new Intent();
	        	i.putExtra("path", path);
	        	setResult(RESULT_OK, i);
	        	finish();
	        	return true;
	        case 1:
	        	finish();
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
}
