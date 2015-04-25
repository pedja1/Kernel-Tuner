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

import android.app.ActivityManager.*;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.TMEntry;
import rs.pedjaapps.KernelTuner.fragments.TMDetailFragment;
import rs.pedjaapps.KernelTuner.fragments.TMListFragment;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.app.Activity;
import android.app.ActivityManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TaskManager extends Activity implements TMListFragment.Callbacks
{
	
	
	public static CheckBox system, user, other;
	SharedPreferences preferences;
	ProgressBar loading;
	PackageManager pm;
	String arch;
	
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
	private boolean mTwoPane;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_tm_list);

		getActionBar().setTitle(getResources().getString(R.string.title_task_manager));
		getActionBar().setSubtitle(null);
		getActionBar().setIcon(R.drawable.tm);
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.ram_layout, null);

		((TextView)customNav.findViewById(R.id.free)).setText(getResources().getString(R.string.mem_free)+ getFreeRAM()+"MB");
		((TextView)customNav.findViewById(R.id.total)).setText(getResources().getString(R.string.mem_free)+ getTotalRAM()+"MB");

        //Attach to the action bar
        getActionBar().setCustomView(customNav);
        getActionBar().setDisplayShowCustomEnabled(true);
		
		if (findViewById(R.id.process_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((TMListFragment) getFragmentManager()
					.findFragmentById(R.id.process_list))
					.setActivateOnItemClick(true);
		}
		pm = getPackageManager();
		
		final boolean ads = preferences.getBoolean("ads", true);
        final AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5750ECFACEA6FCE685DE7A97D8C59A5F")
                .addTestDevice("05FBCDCAC44495595ACE7DC1AEC5C208")
                .addTestDevice("40AA974617D79A7A6C155B1A2F57D595")
                .build();
        if(ads)adView.loadAd(adRequest);
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
	
			new GetRunningApps().execute();
			arch = Tools.getAbi();
	}
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(TMDetailFragment.ARG_ITEM_ID, id);
			TMDetailFragment fragment = new TMDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.process_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, TaskManagerDetailActivity.class);
			detailIntent.putExtra(TMDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	private class Listener implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			 new GetRunningApps().execute();
		}

	}

	public class GetRunningApps extends AsyncTask<String, /*TMEntry*/Void, Void> {
		String line;
		@Override
		protected Void doInBackground(String... args) {
			TMListFragment.entries = new ArrayList<TMEntry>();
			Process proc = null;
			try
			{
				proc = Runtime.getRuntime().exec(getFilesDir().getPath()+"/ps-"+arch+"\n");
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
							TMListFragment.entries.add(tmpEntry);
							//publishProgress(tmpEntry);
						}
					}
					else{
						i++;
					}

				}
				proc.waitFor();
				proc.destroy();
			}
			catch (Exception e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			//	setProgressBarIndeterminateVisibility(false);

			Collections.sort(TMListFragment.entries, new SortByMb());

			for(TMEntry e : TMListFragment.entries){
				if(e.getType()==2){
					if(other.isChecked())
					{
						TMListFragment.tmAdapter.add(e);
					}
				}
				else if(e.getType()==1){
					if(user.isChecked())
					{
						TMListFragment.tmAdapter.add(e);
					}
				}
				else if(e.getType()==0){
					if(system.isChecked())
					{
						TMListFragment.tmAdapter.add(e);
					}
				}
				setProgressBarIndeterminateVisibility(false);
			}
			TMListFragment.tmAdapter.notifyDataSetChanged();

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("tm_system", TaskManager.system.isChecked())
				.putBoolean("tm_user", TaskManager.user.isChecked())
				.putBoolean("tm_other",TaskManager.other.isChecked())
				.apply();
		}
		@Override
		protected void onPreExecute(){
			setProgressBarIndeterminateVisibility(true);
			TMListFragment.tmAdapter.clear();
			
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
	public boolean onCreateOptionsMenu(Menu menu) {

		new MenuInflater(this).inflate(R.menu.tm_custom_menu, menu);
		RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.layout_item)
                .getActionView();

        View inflatedView = getLayoutInflater().inflate(R.layout.tm_cb_view,
                null);

        relativeLayout.addView(inflatedView);
		loading = (ProgressBar)relativeLayout.findViewById(R.id.loading);
		system = (CheckBox)relativeLayout.findViewById(R.id.system);
		user = (CheckBox)relativeLayout.findViewById(R.id.user);
		other = (CheckBox)relativeLayout.findViewById(R.id.other);

		system.setChecked(preferences.getBoolean("tm_system", false));
		user.setChecked(preferences.getBoolean("tm_user", true));
		other.setChecked(preferences.getBoolean("tm_other", false));

		system.setOnCheckedChangeListener(new Listener());
		user.setOnCheckedChangeListener(new Listener());
		other.setOnCheckedChangeListener(new Listener());
		return super.onCreateOptionsMenu(menu);
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
	
	public static Integer getTotalRAM() {
		RandomAccessFile reader = null;
		String load = null;
		Integer mem = null;
		try {
			reader = new RandomAccessFile("/proc/meminfo", "r");
			load = reader.readLine();
			mem = Integer.parseInt(load.substring(load.indexOf(":") + 1,
												  load.lastIndexOf(" ")).trim()) / 1024;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return mem;
	}

	public Integer getFreeRAM() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		Integer mem = (int) (mi.availMem / 1048576L);
		return mem;

	}
}
