package rs.pedjaapps.KernelTuner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FMEntry;
import rs.pedjaapps.KernelTuner.helpers.FMAdapter;

public class FMActivity extends SherlockActivity
{
	List<FMEntry> e;
	String path;
	FMAdapter fAdapter;
	GridView fListView;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm);
		fListView = (GridView) findViewById(R.id.list);
		
		path = Environment.getExternalStorageDirectory().toString();

        fListView.setDrawingCacheEnabled(true);
		fAdapter = new FMAdapter(this, R.layout.fm_row);

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
						fAdapter.add(entry);
					}
					fAdapter.notifyDataSetChanged();
					getSupportActionBar().setTitle(path);
				}

			});

		/*fListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
											   int position, long id)
				{
				
					return true;
				}



			});*/
    }
	
	private List<FMEntry> ls(File path){
		e = new ArrayList<FMEntry>();
		
		File[] files = path.listFiles();
		for(File f : files){
			if(f.isDirectory()){
		      	e.add(new FMEntry(f.getName(),
		            	date(f.lastModified()),
		            	size(f.length()),1,f.getAbsolutePath().toString()));
			}
			/*else{
				e.add(new FMEntry(f.getName(),
								  date(f.lastModified()),
								  size(f.length()),0,f.getAbsolutePath().toString()));
			}*/
			//System.out.println(f.getName());
		}
		Collections.sort(e, new SortByName());
		Collections.sort(e, new SortFolderFirst());
	//	
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
	
	private String size(long size){
		String hrSize = "";
		long b = size;
		double k = size/1024.0;
		double m = size/1048576.0;
		double g = size/1073741824.0;
		double t = size/1099511627776.0;
		
		DecimalFormat dec = new DecimalFormat("0.00");
	
		if (t>1)
		{
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
			hrSize = dec.format(k).concat("KB");
		}
		else if (b>1)
		{
			hrSize = dec.format(b).concat("B");
		}
		return hrSize;
		
	}
	
	public String date(long ms){
		SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		return f.format(ms);
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
