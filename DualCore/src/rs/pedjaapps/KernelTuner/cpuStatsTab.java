package rs.pedjaapps.KernelTuner;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import rs.pedjaapps.KernelTuner.R;


import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
public class cpuStatsTab extends TabActivity {
 
	TabSpec tabSpecCpu1;
	TabSpec tabSpecCpu0;
	TabSpec tabSpecCpu2;
	TabSpec tabSpecCpu3;
	SharedPreferences sharedPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = sharedPrefs.getString("theme", "system");
		if (theme.equals("system")) {
			setTheme(android.R.style.Theme_DeviceDefault);
		} else if (theme.equals("holo")) {
			setTheme(android.R.style.Theme_Holo);
		} else if (theme.equals("holo_light")) {
			setTheme(android.R.style.Theme_Holo_Light);
		} else if (theme.equals("dark")) {
			setTheme(android.R.style.Theme_Black);
		} else if (theme.equals("light")) {
			setTheme(android.R.style.Theme_Light);
		} else if (theme.equals("holo_no_ab")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar);
		} else if (theme.equals("holo_wp")) {
			setTheme(android.R.style.Theme_Holo_Wallpaper);
		} else if (theme.equals("holo_fs")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		} else if (theme.equals("holo_light_dark_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		} else if (theme.equals("holo_light_no_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		} else if (theme.equals("holo_light_fs")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		}
		setContentView(R.layout.tabl_layout_cpu_stats);
 
		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
		File file1 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
		try{
		
		InputStream fIn2 = new FileInputStream(file1);
		Intent intentCpu0 = new Intent().setClass(this, cpuTimes.class);
		tabSpecCpu0 = tabHost
				  .newTabSpec("CPU0")
				  .setIndicator("CPU0")
				  .setContent(intentCpu0);
		}
		catch(FileNotFoundException e){
			Intent intentNotSupported = new Intent(this, notFoundActivity.class);
			tabSpecCpu0 = tabHost
					  .newTabSpec("CPU0")
					  .setIndicator("CPU0")
					  .setContent(intentNotSupported);
		}
	
		File file = new File("/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state");
		try{
		
		InputStream fIn = new FileInputStream(file);
		Intent intentCpu1 = new Intent().setClass(this, cpuTimesCpu1.class);
		tabSpecCpu1 = tabHost
				  .newTabSpec("CPU1")
				  .setIndicator("CPU1")
				  .setContent(intentCpu1);
		}
		catch(FileNotFoundException e){
			Intent intentNotSupported = new Intent(this, notFoundActivity.class);
			tabSpecCpu1 = tabHost
					  .newTabSpec("CPU1")
					  .setIndicator("CPU1")
					  .setContent(intentNotSupported);
		}
		
		
		File file2 = new File("/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state");
		try{
		
		InputStream fIn = new FileInputStream(file);
		Intent intentCpu2 = new Intent().setClass(this, cpuTimesCpu2.class);
		tabSpecCpu2 = tabHost
				  .newTabSpec("CPU2")
				  .setIndicator("CPU2")
				  .setContent(intentCpu2);
		}
		catch(FileNotFoundException e){
			Intent intentNotSupported = new Intent(this, notFoundActivity.class);
			tabSpecCpu2 = tabHost
					  .newTabSpec("CPU2")
					  .setIndicator("CPU2")
					  .setContent(intentNotSupported);
		}
 
		File file3 = new File("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
		try{
		
		InputStream fIn = new FileInputStream(file);
		Intent intentCpu3 = new Intent().setClass(this, cpuTimesCpu3.class);
		tabSpecCpu3 = tabHost
				  .newTabSpec("CPU3")
				  .setIndicator("CPU3")
				  .setContent(intentCpu3);
		}
		catch(FileNotFoundException e){
			Intent intentNotSupported = new Intent(this, notFoundActivity.class);
			tabSpecCpu3 = tabHost
					  .newTabSpec("CPU3")
					  .setIndicator("CPU3")
					  .setContent(intentNotSupported);
		}
		
 
		// add all tabs 
		
			
		
		//tabHost.addTab(tabSpecCpu0);
		if(new File("/sys/devices/system/cpu/cpu0/online").exists()){
			tabHost.addTab(tabSpecCpu0);
		}
		if(new File("/sys/devices/system/cpu/cpu1/online").exists()){
			tabHost.addTab(tabSpecCpu1);
		}
		if(new File("/sys/devices/system/cpu/cpu2/online").exists()){
			tabHost.addTab(tabSpecCpu2);
		}
		if(new File("/sys/devices/system/cpu/cpu3/online").exists()){
			tabHost.addTab(tabSpecCpu3);
		}
		
		
 
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(2);
		
	}
	
}
