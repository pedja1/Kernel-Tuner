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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.FMAdapter;
import rs.pedjaapps.kerneltuner.model.FMEntry;
import rs.pedjaapps.kerneltuner.root.RootUtils;
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
	 * Regexp pattern to parse the result from ls.
	 */
	private static Pattern sLsPattern = Pattern.compile("^([bcdlsp-][-r][-w][-xsS][-r][-w][-xsS][-r][-w][-xstST])\\s+(\\S+)\\s+(\\S+)\\s+([\\d\\s,]*)\\s+(\\d{4}-\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.*)$"); //$NON-NLS-1$
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
						Matcher m = sLsPattern.matcher(line);
						if (m.matches() == false) continue;
						System.out.println("ls line: " + line);
						
						FMEntry entry = new FMEntry();
						// get the name
						entry.setName(m.group(7));
						entry.setPath(path + (path.endsWith("/") ? "" : "/") + entry.getName());
						// get the rest of the groups
						String permissions = m.group(1);
						entry.setOwner(m.group(2));
						entry.setGroup(m.group(3));
						entry.setSize(m.group(4));
						entry.setSizeHr(Tools.byteToHumanReadableSize(Tools.parseLong(entry.getSize() ,0)));
						entry.setDate(m.group(5));
						entry.setTime(m.group(6));
						String info = null;
						// and the type
						int objectType = TYPE_OTHER;
						switch (permissions.charAt(0)) 
						{
							case '-':
								objectType = TYPE_FILE;
								break;
							case 'b':
								objectType = TYPE_BLOCK;
								break;
							case 'c':
								objectType = TYPE_CHARACTER;
								break;
							case 'd':
								objectType = TYPE_DIRECTORY;
								entry.setFolder(1);
								break;
							case 'l':
								objectType = TYPE_LINK;
								break;
							case 's':
								objectType = TYPE_SOCKET;
								break;
							case 'p':
								objectType = TYPE_FIFO;
								break;
						}
						// now check what we may be linking to
						if (objectType == TYPE_LINK) 
						{
							String name = entry.getName();
							String[] segments = name.split("\\s->\\s"); //$NON-NLS-1$
							// we should have 2 segments
							if (segments.length == 2) 
							{
								// update the entry name to not contain the link
								name = segments[0];
								// and the link name
								info = segments[1];
								// now get the path to the link
								String[] pathSegments = info.split(FILE_SEPARATOR);
								if (pathSegments.length == 1)
								{
									// the link is to something in the same directory,
									// unless the link is ..
									if ("..".equals(pathSegments[0])) 
									{ //$NON-NLS-1$
										// set the type and we're done.
										objectType = TYPE_DIRECTORY_LINK;
									    entry.setFolder(1);
										entry.setPath(info);
									} 
									else 
									{
										// either we found the object already
										// or we'll find it later.
									}
								}
							}
							// add an arrow in front to specify it's a link.
							info = "-> " + info; //$NON-NLS-1$;
							entry.setInfo(info);
						}
						
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
		}, "ls -al " + path);
	}

	static class SortFolderFirst implements Comparator<FMEntry>
	{
		@Override
		public int compare(FMEntry p1, FMEntry p2)
		{
	        if (p1.getFolder() < p2.getFolder()) return 1;
	        if (p1.getFolder() > p2.getFolder()) return -1;
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
