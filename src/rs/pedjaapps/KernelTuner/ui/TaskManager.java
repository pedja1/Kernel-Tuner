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

import android.widget.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.TMEntry;
import rs.pedjaapps.KernelTuner.helpers.TMAdapter;
import rs.pedjaapps.KernelTuner.tools.RootExecuter;

import com.actionbarsherlock.app.SherlockActivity;

public class TaskManager extends SherlockActivity
{
	
	ListView tmListView;
	TMAdapter tmAdapter;
	static Drawable dr;
	List<TMEntry> entries;
	ProgressDialog pd;
	PackageManager pm;
	String set;
	CheckBox system, user, other;
	SharedPreferences preferences;
	
	/**
	 * Foreground Application = 10040
	 * Secondary Server  	  = 10041
	 * Content Providers      = 10043
	 * Empty Application      = 10079
	 * Visible Application    = 10025
	 * Hidden Application     =
	 * */
	/*
	private static final int BACKGROUND = RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
	private static final int EMPTY = RunningAppProcessInfo.IMPORTANCE_EMPTY;
	private static final int FOREGROUND = RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
	private static final int PERCEPTIBLE = RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE;
	private static final int SERVICE = RunningAppProcessInfo.IMPORTANCE_SERVICE;
	private static final int VISIBLE = RunningAppProcessInfo.IMPORTANCE_VISIBLE;
	*/
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//System.out.println(PERCEPTIBLE+","+SERVICE+","+FOREGROUND+","+BACKGROUND+","+","+EMPTY+","+VISIBLE);
		 preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = preferences.getString("theme", "light");

		if (theme.equals("light")) 
		{
			setTheme(R.style.Theme_Sherlock_Light);
		} 
		else if (theme.equals("dark")) 
		{
			setTheme(R.style.Theme_Sherlock);
		} 
		else if (theme.equals("light_dark_action_bar")) 
		{
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		}
		else if (theme.equals("miui_light")) 
		{
			setTheme(R.style.Theme_Miui_Light);
		} 
		else if (theme.equals("miui_dark")) 
		{
			setTheme(R.style.Theme_Miui_Dark);
		} 
		else if (theme.equals("sense5")) 
		{
			setTheme(R.style.Theme_Sense5);
		}
		else if (theme.equals("sense5_light")) 
		{
			setTheme(R.style.Theme_Light_Sense5);
		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.task_manager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		pm = getPackageManager();
		/**
		 * Load ads if enabled in settings*/
		final boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{
			AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());
		}
		
		system = (CheckBox)findViewById(R.id.system);
		user = (CheckBox)findViewById(R.id.user);
		other = (CheckBox)findViewById(R.id.other);
	
		system.setChecked(preferences.getBoolean("tm_system", false));
		user.setChecked(preferences.getBoolean("tm_user", true));
		other.setChecked(preferences.getBoolean("tm_other", false));
	
		system.setOnCheckedChangeListener(new Listener());
		user.setOnCheckedChangeListener(new Listener());
		other.setOnCheckedChangeListener(new Listener());
	
		
		tmListView = (ListView) findViewById(R.id.list);
		tmAdapter = new TMAdapter(this, R.layout.tm_row);
		tmListView.setAdapter(tmAdapter);
		tmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int pos,
					long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskManager.this);

				builder.setTitle("Change Process Priority");
				Integer value = null;
				try {
					value = Integer.parseInt(FileUtils.readFileToString(new File("/proc/"+tmAdapter.getItem(pos).getPid()+"/oom_adj")).trim());
				} catch (Exception e) {
					Log.e("", e.getMessage());
				}
				LayoutInflater inflater = (LayoutInflater) TaskManager.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.tm_priority_layout, null);
				final TextView nice  = (TextView)view.findViewById(R.id.nice);
				SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
				LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
				
				if(value!=null){
					seekBar.setProgress(32-(15-value));
					Log.e("", 32-(15-value)+"");
					nice.setText(value+"");
					
					builder.setPositiveButton("Set", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							RootExecuter.exec(new String[]{"echo " + nice.getText().toString().trim() + " > /proc/"+tmAdapter.getItem(pos).getPid()+"/oom_adj"});		
						}
						
					});
					seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

						@Override
						public void onProgressChanged(SeekBar arg0, int progress,
								boolean fromUser) {
						
							nice.setText(progress-(32-15)+"");
							
						}

						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
							
						}

						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
							set = nice.getText().toString().trim();
							System.out.println(set);
						}
						
					});
					
				}
				else{
					ll.setVisibility(View.GONE);
					nice.setText("Something went wrong");
					
				}
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						
					}
					
				});
				
				

				builder.setView(view);
				AlertDialog alert = builder.create();
				alert.show();
				
			}
		});
		try{
			String line;
			Process p = Runtime.getRuntime().exec("which ps");
			InputStream inputStream = p.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			line = bufferedReader.readLine();
			
				if(line != null && line.startsWith("/")){
					new GetRunningApps().execute();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error!");

					builder.setMessage("ps executable not found!\nYour ROM isnt supported");

					builder.setIcon(R.drawable.tm);
					
					builder.setNegativeButton("Exit", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								finish();
							}

						});
					AlertDialog alert = builder.create();
					alert.setCancelable(false);
					alert.setCanceledOnTouchOutside(false);
					alert.show();
				}
			
		}
		catch(Exception e){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Error!");

			builder.setMessage("Something went wrong\n"+e.getMessage());

			builder.setIcon(R.drawable.tm);
			
			builder.setNegativeButton("Exit", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						finish();
					}

				});
			AlertDialog alert = builder.create();
			alert.setCancelable(false);
			alert.setCanceledOnTouchOutside(false);
			alert.show();
		}
		

	
		
		
	}
	
	private class Listener implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			new GetRunningApps().execute();
		}

	}
	
	private class GetRunningApps extends AsyncTask<String, TMEntry, Void> {
		String line;
		@Override
		protected Void doInBackground(String... args) {
			entries = new ArrayList<TMEntry>();
			Process proc = null;
			try
			{
				proc = Runtime.getRuntime().exec("ps\n");
				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				int i = 0;
				while ( ( line = bufferedReader.readLine() ) != null )
				{
					line = line.trim().replaceAll(" {1,}", " ");
					String[] temp = line.split("\\s");
					List<String> tmp = Arrays.asList(temp);
					if(i>0){
						if(!tmp.get(4).equals("0")){
					TMEntry tmpEntry = new TMEntry(getApplicationName(tmp.get(8)), Integer.parseInt(tmp.get(1)), getApplicationIcon(tmp.get(8)), Integer.parseInt(tmp.get(4)), appType(tmp.get(8)));
					entries.add(tmpEntry);
					publishProgress(tmpEntry);
					}
					}
					else{
						i++;
					}
					
				}
			}
			catch (Exception e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(TMEntry... values)
		{
			if(values[0].getType()==2){
				if(other.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			else if(values[0].getType()==1){
				if(user.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			else if(values[0].getType()==0){
				if(system.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res) {
			setProgressBarIndeterminateVisibility(false);
			
			Collections.sort(entries, new SortByMb());
			tmAdapter.clear();
			for(TMEntry e : entries){
				if(e.getType()==2){
					if(other.isChecked())
					{
						tmAdapter.add(e);
					}
				}
				else if(e.getType()==1){
					if(user.isChecked())
					{
						tmAdapter.add(e);
					}
				}
				else if(e.getType()==0){
					if(system.isChecked())
					{
						tmAdapter.add(e);
					}
				}
			}
			tmAdapter.notifyDataSetChanged();

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("tm_system", system.isChecked())
				.putBoolean("tm_user", user.isChecked())
				.putBoolean("tm_other",other.isChecked())
				.apply();
		}
		@Override
		protected void onPreExecute(){
			setProgressBarIndeterminateVisibility(true);
		}

	}
	
	public Drawable getApplicationIcon(String packageName){
		try
		{
			return pm.getApplicationIcon(packageName);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return getResources().getDrawable(R.drawable.apk);
		}
	}
	public String getApplicationName(String packageName){
		try
		{
			return (String)pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return packageName;
		}
	}
	
	/**
	* @return 0 if system, 1 if user, 2 if unknown
	*/
	public int appType(String packageName) {
		try{
		ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
		int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
	    if((ai.flags & mask) == 0){
			return 1;
		}
		else{
     	    return 0;
		}
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return 2;
		}
	}
	
	class SortByMb implements Comparator<TMEntry>
	{
		  @Override
		  public int compare(TMEntry ob1, TMEntry ob2)
		  {
		   return ob2.getRss() - ob1.getRss() ;
		  }
	}
		
	static class SortByName implements Comparator<TMEntry>
	{
		@Override
		public int compare(TMEntry s1, TMEntry s2)
		{
		    String sub1 = s1.getName();
		    String sub2 = s2.getName();
		    return sub2.compareTo(sub1);
		} 
	}
	
	class SortByType implements Comparator<TMEntry>
	{
		@Override
		public int compare(TMEntry ob1, TMEntry ob2)
		{
			return ob1.getType() - ob2.getType() ;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, KernelTuner.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

				return true;

		}
		return super.onOptionsItemSelected(item);
	}
}
