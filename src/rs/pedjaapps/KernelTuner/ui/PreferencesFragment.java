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
import android.view.MenuItem;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.services.*;

public class PreferencesFragment extends PreferenceFragment
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
	private ListPreference cpuList;
	private ListPreference widgetPrefList;
	private ListPreference unsupportedPrefList;
	private CheckBoxPreference mainCpuPref;
	private CheckBoxPreference mainTempPref;
	private CheckBoxPreference mainTogglesPref;
	private CheckBoxPreference mainButtonsPref;
	private CheckBoxPreference mainStylePref;
	private EditTextPreference refreshPref;
	private ListPreference levelPrefList;
	private ListPreference formatPrefList;
	private ListPreference bufferPrefList;
	private ListPreference textsizePrefList;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{        
		super.onCreate(savedInstanceState);
		
	    ActionBar ab = getActivity().getActionBar();
		String settings = getArguments().getString("settings");
        if ("application".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_application);
			app();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_genereal));
        } else if ("widget".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_widget);
			widget();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_widget));
        } else if ("notification".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_notification);
			notif();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_notification));
        } else if ("ui".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_ui);
			ui();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_ui));
        } else if ("logcat".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_logcat);
			logcat();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_logcat));
        } else if ("main".equals(settings)) {
            addPreferencesFromResource(R.xml.settings_main);
			main();
			ab.setSubtitle(getResources().getString(R.string.preferences_subtitle_main));
        }
	}
	
	private boolean isNotificationServiceRunning() {
	    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (NotificationService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void main(){
		mainCpuPref = (CheckBoxPreference)findPreference("main_cpu");
		mainTempPref = (CheckBoxPreference)findPreference("main_temp");
		mainTogglesPref = (CheckBoxPreference)findPreference("main_toggles");
		mainButtonsPref = (CheckBoxPreference)findPreference("main_buttons");
		mainStylePref = (CheckBoxPreference)findPreference("main_style");
		
		mainStylePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

				public boolean onPreferenceChange(Preference preferenxe, Object newValue)
				{
					if(newValue.toString().equals("true")){
						mainCpuPref.setEnabled(false);
						mainTogglesPref.setEnabled(false);
						mainButtonsPref.setEnabled(false);
						mainTempPref.setEnabled(false);
					}
					else{
						mainCpuPref.setEnabled(true);
						mainTogglesPref.setEnabled(true);
						mainButtonsPref.setEnabled(true);
						mainTempPref.setEnabled(true);
					}
					return true;
				}
			});
		if(mainStylePref.isChecked()){
			mainCpuPref.setEnabled(false);
			mainTogglesPref.setEnabled(false);
			mainButtonsPref.setEnabled(false);
			mainTempPref.setEnabled(false);
		}
		else{
			mainCpuPref.setEnabled(true);
			mainTogglesPref.setEnabled(true);
			mainButtonsPref.setEnabled(true);
			mainTempPref.setEnabled(true);
		}
		unsupportedPrefList = (ListPreference) findPreference("unsupported_items_display");
        unsupportedPrefList.setDefaultValue(unsupportedPrefList.getEntryValues()[0]);
        String unsupported = unsupportedPrefList.getValue();
        if (unsupported == null) {
            unsupportedPrefList.setValue((String)unsupportedPrefList.getEntryValues()[0]);
            unsupported = unsupportedPrefList.getValue();
        }
        unsupportedPrefList.setSummary(unsupportedPrefList.getEntries()[unsupportedPrefList.findIndexOfValue(unsupported)]);


        unsupportedPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					unsupportedPrefList.setSummary(unsupportedPrefList.getEntries()[unsupportedPrefList.findIndexOfValue(newValue.toString())]);
					return true;
				}
			}); 
	}
	
	private void widget(){
			widgetPrefList = (ListPreference) findPreference("widget_bg");
        widgetPrefList.setDefaultValue(widgetPrefList.getEntryValues()[0]);
        String widgetBg = widgetPrefList.getValue();
        if (widgetBg == null) {
            widgetPrefList.setValue((String)widgetPrefList.getEntryValues()[0]);
            widgetBg = widgetPrefList.getValue();
        }
        widgetPrefList.setSummary(widgetPrefList.getEntries()[widgetPrefList.findIndexOfValue(widgetBg)]);


        widgetPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                widgetPrefList.setSummary(widgetPrefList.getEntries()[widgetPrefList.findIndexOfValue(newValue.toString())]);
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
	
	}
	
	private void logcat(){
			levelPrefList = (ListPreference) findPreference("level");
        levelPrefList.setDefaultValue(levelPrefList.getEntryValues()[0]);
        String level = levelPrefList.getValue();
        if (level == null) {
        	levelPrefList.setValue((String)levelPrefList.getEntryValues()[0]);
        	level = levelPrefList.getValue();
        }
        levelPrefList.setSummary(levelPrefList.getEntries()[levelPrefList.findIndexOfValue(level)]);


        levelPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					levelPrefList.setSummary(levelPrefList.getEntries()[levelPrefList.findIndexOfValue(newValue.toString())]);
					return true;
				}
			}); 
			
		bufferPrefList = (ListPreference) findPreference("buffer");
        bufferPrefList.setDefaultValue(bufferPrefList.getEntryValues()[0]);
        String buffer = bufferPrefList.getValue();
        if (buffer == null) {
        	bufferPrefList.setValue((String)bufferPrefList.getEntryValues()[0]);
        	buffer = bufferPrefList.getValue();
        }
        bufferPrefList.setSummary(bufferPrefList.getEntries()[bufferPrefList.findIndexOfValue(buffer)]);


        bufferPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					bufferPrefList.setSummary(bufferPrefList.getEntries()[bufferPrefList.findIndexOfValue(newValue.toString())]);
					return true;
				}
			}); 
			
		formatPrefList = (ListPreference) findPreference("format");
        formatPrefList.setDefaultValue(formatPrefList.getEntryValues()[0]);
        String format = formatPrefList.getValue();
        if (format == null) {
        	formatPrefList.setValue((String)formatPrefList.getEntryValues()[0]);
        	format = formatPrefList.getValue();
        }
        formatPrefList.setSummary(formatPrefList.getEntries()[formatPrefList.findIndexOfValue(format)]);


        formatPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					formatPrefList.setSummary(formatPrefList.getEntries()[formatPrefList.findIndexOfValue(newValue.toString())]);
					return true;
				}
			}); 
			
		textsizePrefList = (ListPreference) findPreference("textsize");
        textsizePrefList.setDefaultValue(textsizePrefList.getEntryValues()[0]);
        String textsize = textsizePrefList.getValue();
        if (textsize == null) {
        	textsizePrefList.setValue((String)textsizePrefList.getEntryValues()[0]);
        	textsize = textsizePrefList.getValue();
        }
        textsizePrefList.setSummary(textsizePrefList.getEntries()[textsizePrefList.findIndexOfValue(textsize)]);


        textsizePrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					textsizePrefList.setSummary(textsizePrefList.getEntries()[textsizePrefList.findIndexOfValue(newValue.toString())]);
					return true;
				}
			}); 
	}
	private void notif(){
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
						getActivity().stopService(new Intent(PreferencesFragment.this.getActivity(), NotificationService.class));
						getActivity().startService(new Intent(PreferencesFragment.this.getActivity(), NotificationService.class));
					}
					return true;
				}
			}); 

        notifBox = (CheckBoxPreference) findPreference("notificationService");
        notifBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					if(notifBox.isChecked()){
						getActivity().stopService(new Intent(PreferencesFragment.this.getActivity(), NotificationService.class));

					}
					else if(notifBox.isChecked()==false){
						getActivity().startService(new Intent(PreferencesFragment.this.getActivity(), NotificationService.class));
					}

					return true;
				}
			}); 

        notifScreen = (PreferenceScreen)findPreference("notificationScreen");
        notifScreen.setOnPreferenceClickListener(new OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference arg0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
	                    PreferencesFragment.this.getActivity());

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
	}
	
	private void ui(){
	
		/*tisList = (ListPreference) findPreference("tis_open_as");
		tisList.setDefaultValue(tisList.getEntryValues()[0]);
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
			});*/

        /*cpuList = (ListPreference) findPreference("show_cpu_as");
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
			}); */
	}
	
	private void app(){
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


		refreshPref = (EditTextPreference) findPreference("refresh");
        refreshPref.setDefaultValue(refreshPref.getText());
        String refresh = refreshPref.getText();
        if (refresh == null) {
        	refreshPref.setText(refreshPref.getText().toString());
            refresh = refreshPref.getText();
        }
        refreshPref.setSummary(refresh+"ms");


        refreshPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					refreshPref.setSummary(newValue.toString()+"ms");
					return true;
				}
			}); 



        htcOneOverride = (CheckBoxPreference) findPreference("htc_one_workaround");
        htcOneOverride.setOnPreferenceClickListener(new OnPreferenceClickListener(){

				@Override
				public boolean onPreferenceClick(Preference arg0) {
					if(htcOneOverride.isChecked()){
						AlertDialog.Builder builder = new AlertDialog.Builder(
							PreferencesFragment.this.getActivity());

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
							PreferencesFragment.this.getActivity());

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
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this.getActivity(), KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
	
}
