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
package rs.pedjaapps.KernelTuner.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FMEntry;
import rs.pedjaapps.KernelTuner.helpers.FMAdapter;
import android.content.Context;
import rs.pedjaapps.KernelTuner.tools.Tools;
import javax.crypto.NullCipher;

public class FMActivity extends SherlockActivity
{
	List<FMEntry> e;
	String path;
	FMAdapter fAdapter;
	GridView fListView;
	Context c;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("theme", "light");
		setTheme(Tools.getPreferedTheme(theme));
		fListView = (GridView) findViewById(R.id.list);
		
		path = Environment.getExternalStorageDirectory().toString();

        fListView.setDrawingCacheEnabled(true);
		fAdapter = new FMAdapter(c, R.layout.fm_row);

		fListView.setAdapter(fAdapter);

		for (FMEntry entry : ls(new File(path)))
		{
			fAdapter.add(entry);
		}

		getSupportActionBar().setTitle(path);
		fListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){



				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
										long arg3)
				{
					path = e.get(pos).getPath();
					fAdapter.clear();
					for (FMEntry entry : ls(new File(path)))
					{
						if(entry!=null){
						fAdapter.add(entry);
						}
					}
					fAdapter.notifyDataSetChanged();
					getSupportActionBar().setTitle(path);
				}

			});

    }
	
	private List<FMEntry> ls(File path){
		e = new ArrayList<FMEntry>();
		
		File[] files = path.listFiles();
		for(File f : files){
			if(f.isDirectory()){
		      	e.add(new FMEntry(f.getName(),
		            	Tools.msToDate(f.lastModified()),
		            	Tools.byteToHumanReadableSize(f.length()),1,f.getAbsolutePath().toString()));
			}
		}
		Collections.sort(e, new SortByName());
		Collections.sort(e, new SortFolderFirst());

		return e;
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
	public void onBackPressed() {
		if(path.equals(Environment.getExternalStorageDirectory().toString())){
		finish();
		}
		else{
			path = path.substring(0, path.lastIndexOf("/"));
			
			fAdapter.clear();
			for (FMEntry entry : ls(new File(path)))
			{
				fAdapter.add(entry);
			}
			fAdapter.notifyDataSetChanged();
			getSupportActionBar().setTitle(path);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,0,0,"Select").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0,1,1,"Cancel").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
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
}
