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

import android.app.*;
import android.app.ActivityManager.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.preference.Preference.*;
import com.actionbarsherlock.app.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.services.*;

import com.actionbarsherlock.app.ActionBar;



public class Preferences extends SherlockPreferenceActivity
{

	private ListPreference bootPrefList;
	private EditTextPreference widgetPref;
	private ListPreference tempPrefList;
	private ListPreference notifPrefList;
	private CheckBoxPreference notifBox;
	private PreferenceScreen notifScreen;
	private CheckBoxPreference htcOneOverride;
	private ListPreference tisList;
	private CheckBoxPreference resetApp;
	private ListPreference themePrefList;
	private ListPreference cpuList;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{        
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String them = sharedPrefs.getString("theme", "light");
		
			if(them.equals("light")){
				setTheme(R.style.IndicatorLight);
			}
			else if(them.equals("dark")){
				setTheme(R.style.IndicatorDark);
				
			}
			else if(them.equals("light_dark_action_bar")){
				setTheme(R.style.IndicatorLightDark);
				
			}
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences); 
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		themePrefList = (ListPreference) findPreference("theme");
        themePrefList.setDefaultValue(themePrefList.getEntryValues()[0]);
        String theme = themePrefList.getValue();
        if (theme == null) {
            themePrefList.setValue((String)themePrefList.getEntryValues()[0]);
            theme = themePrefList.getValue();
        }
        themePrefList.setSummary(themePrefList.getEntries()[themePrefList.findIndexOfValue(theme)]);


        themePrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                themePrefList.setSummary(themePrefList.getEntries()[themePrefList.findIndexOfValue(newValue.toString())]);
                return true;
            }
        }); 
		
		bootPrefList = (ListPreference) findPreference("boot");
        bootPrefList.setDefaultValue(bootPrefList.getEntryValues()[0]);
        String boot = bootPrefList.getValue();
        if (boot == null) {
            bootPrefList.setValue((String)bootPrefList.getEntryValues()[0]);
            boot = bootPrefList.getValue();
        }
        bootPrefList.setSummary(bootPrefList.getEntries()[bootPrefList.findIndexOfValue(boot)]);


        bootPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                bootPrefList.setSummary(bootPrefList.getEntries()[bootPrefList.findIndexOfValue(newValue.toString())]);
                return true;
            }
        }); 
        
        widgetPref = (EditTextPreference) findPreference("widget_time");
        widgetPref.setDefaultValue(widgetPref.getText());
        String widget = widgetPref.getText();
        if (widget == null) {
        	widgetPref.setText((String)widgetPref.getText());
            widget = widgetPref.getText();
        }
        widgetPref.setSummary(widget+"min");


        widgetPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	widgetPref.setSummary(newValue.toString()+"min");
                return true;
            }
        }); 
        
        tempPrefList = (ListPreference) findPreference("temp");
        tempPrefList.setDefaultValue(tempPrefList.getEntryValues()[0]);
        String temp = tempPrefList.getValue();
        if (temp == null) {
        	tempPrefList.setValue((String)tempPrefList.getEntryValues()[0]);
        	temp = tempPrefList.getValue();
        }
        tempPrefList.setSummary(tempPrefList.getEntries()[tempPrefList.findIndexOfValue(temp)]);


        tempPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	tempPrefList.setSummary(tempPrefList.getEntries()[tempPrefList.findIndexOfValue(newValue.toString())]);
                return true;
            }
        }); 
        
       
        
        notifPrefList = (ListPreference) findPreference("notif");
        notifPrefList.setDefaultValue(notifPrefList.getEntryValues()[0]);
        String notif = notifPrefList.getValue();
        if (notif == null) {
        	notifPrefList.setValue((String)notifPrefList.getEntryValues()[0]);
        	notif = notifPrefList.getValue();
        }
        notifPrefList.setSummary(notifPrefList.getEntries()[notifPrefList.findIndexOfValue(notif)]);


        notifPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	notifPrefList.setSummary(notifPrefList.getEntries()[notifPrefList.findIndexOfValue(newValue.toString())]);
            	if(isNotificationServiceRunning()){
            	stopService(new Intent(Preferences.this, NotificationService.class));
            	startService(new Intent(Preferences.this, NotificationService.class));
            	}
                return true;
            }
        }); 
		
        notifBox = (CheckBoxPreference) findPreference("notificationService");
        notifBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	if(notifBox.isChecked()){
            		stopService(new Intent(Preferences.this, NotificationService.class));
                	
            	}
            	else if(notifBox.isChecked()==false){
            	startService(new Intent(Preferences.this, NotificationService.class));
            	}
            	
                return true;
            }
        }); 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
        	 getActionBar().setSubtitle("Application Preferences");
        }
        notifScreen = (PreferenceScreen)findPreference("notificationScreen");
        notifScreen.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
	                    Preferences.this);

					builder.setMessage(getResources().getString(R.string.notificatio_preferences_warning));

					builder.setIcon(R.drawable.ic_menu_recent_history);

					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							
							}
						});
					
					
					AlertDialog alert = builder.create();

					alert.show();
				return false;
			}
        	
        });
        htcOneOverride = (CheckBoxPreference) findPreference("htc_one_workaround");
        htcOneOverride.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(htcOneOverride.isChecked()){
				AlertDialog.Builder builder = new AlertDialog.Builder(
	                    Preferences.this);

					builder.setMessage(getResources().getString(R.string.htc_override_preferences_warning));

					builder.setIcon(R.drawable.ic_menu_info_details);

					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							
							}
						});
					
					
					AlertDialog alert = builder.create();

					alert.show();
				}
				return false;
			}
        	
        });
        
        resetApp = (CheckBoxPreference) findPreference("reset");
        resetApp.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(resetApp.isChecked()){
				AlertDialog.Builder builder = new AlertDialog.Builder(
	                    Preferences.this);

					builder.setMessage("This will not work if init.d is selected for restoring settings after reboot.\n\n init.d scripts are executed before this application can start");

					builder.setIcon(R.drawable.ic_menu_info_details);

					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							
							}
						});
					
					
					AlertDialog alert = builder.create();

					alert.show();
				}
				return false;
			}
        	
        });
		
		tisList = (ListPreference) findPreference("tis_open_as");
		tisList.setDefaultValue(notifPrefList.getEntryValues()[0]);
        String tis = tisList.getValue();
        if (tis == null) {
        	tisList.setValue((String)tisList.getEntryValues()[0]);
        	tis = tisList.getValue();
        }
        tisList.setSummary(tisList.getEntries()[tisList.findIndexOfValue(tis)]);

        tisList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					tisList.setSummary(tisList.getEntries()[tisList.findIndexOfValue(newValue.toString())]);

					return true;
				}
			}); 
        
        cpuList = (ListPreference) findPreference("show_cpu_as");
		cpuList.setDefaultValue(cpuList.getEntryValues()[0]);
        String cpu = cpuList.getValue();
        if (cpu == null) {
        	cpuList.setValue((String)cpuList.getEntryValues()[0]);
        	cpu = cpuList.getValue();
        }
        cpuList.setSummary(cpuList.getEntries()[cpuList.findIndexOfValue(cpu)]);

        cpuList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					cpuList.setSummary(cpuList.getEntries()[cpuList.findIndexOfValue(newValue.toString())]);

					return true;
				}
			}); 
       
	}
	
	private boolean isNotificationServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (NotificationService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
	
}
