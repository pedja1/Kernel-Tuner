package rs.pedjaapps.KernelTuner;




import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.*;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.webkit.WebView;



public class Preferences extends PreferenceActivity
{

ListPreference bootPrefList;
EditTextPreference widgetPref;
ListPreference tempPrefList;
ListPreference localePrefList;
ListPreference notifPrefList;
CheckBoxPreference notifBox;
PreferenceScreen notifScreen;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{        
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);        
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
        
        localePrefList = (ListPreference) findPreference("loc");
        localePrefList.setDefaultValue(localePrefList.getEntryValues()[0]);
        String locale = localePrefList.getValue();
        if (locale == null) {
        	localePrefList.setValue((String)localePrefList.getEntryValues()[0]);
        	locale = localePrefList.getValue();
        }
        localePrefList.setSummary(localePrefList.getEntries()[localePrefList.findIndexOfValue(locale)]);


        localePrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	localePrefList.setSummary(localePrefList.getEntries()[localePrefList.findIndexOfValue(newValue.toString())]);
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
       
	}
	
	private boolean isNotificationServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (NotificationService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	

}