package rs.pedjaapps.KernelTuner.receiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.CPUInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class AppWidgetBattery extends AppWidgetProvider {

	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	private Integer battperc;
	private Double batttemp;
	private String battcurrent = "";
	RemoteViews remoteViews;
	Context context;
	SharedPreferences pref;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		   this.context = context;
		int bgRes = R.drawable.lcd_background_grey;
		String widgetBgPref = pref.getString("widget_bg","grey");
		if(widgetBgPref.equals("grey")){
			bgRes = R.drawable.lcd_background_grey;
		}
		else if(widgetBgPref.equals("dark")){
			bgRes = R.drawable.appwidget_dark_bg;
		}
		else if(widgetBgPref.equals("transparent")){
			bgRes = 0;
		}
		Intent active = new Intent(context, AppWidgetBattery.class);
	    active.setAction(ACTION_WIDGET_REFRESH);
	    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	    remoteViews.setOnClickPendingIntent(R.id.widget_layout, actionPendingIntent);

	    if(bgRes!=0){
	    remoteViews.setInt(R.id.widget_layout, "setBackgroundResource", bgRes);
	    }
	    
		
	    setView(context);
	    String timer = pref.getString("widget_time", "");
	    double time;
	 	try
		{
			time = Double.parseDouble(timer.trim());
		}
		catch (Exception e)
		{
			time = 30;
		}
	    
	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.add(Calendar.SECOND, (int)time*60);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendar.getTimeInMillis(), actionPendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	private void setView(Context context){
		battInfo();
		String tempPref = pref.getString("temp", "celsius");
		if (battperc != null) {
			remoteViews.setTextViewText(R.id.textView1, "Level: " + battperc + "%");
			remoteViews.setProgressBar(R.id.progressBar1, 100, battperc, false);
		} else {
			remoteViews.setTextViewText(R.id.textView1, "Unknown");
		}
		if (batttemp != null) {
			remoteViews.setTextViewText(R.id.textView3, tempConverter(tempPref, batttemp));
		} else {
			remoteViews.setTextViewText(R.id.textView3, "Unknown");
		}
		if (!battcurrent.equals("err")) {
			remoteViews.setTextViewText(R.id.textView5, battcurrent + "mAh");
			if (battcurrent.substring(0, 1).equals("-"))
			{
				remoteViews.setTextColor(R.id.textView5, Color.RED);
			}
			else
			{
				remoteViews.setTextViewText(R.id.textView5, "+"+battcurrent + "mAh");
				remoteViews.setTextColor(R.id.textView5, Color.GREEN);
			}
		} else {
			remoteViews.setTextViewText(R.id.textView5, "Unknown");
		}
		ComponentName thiswidget = new ComponentName(context, AppWidgetBattery.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_battery);
		 if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
	    	setView(context);
	        System.out.println("battery widget refresh");
	    } 
	    
	    else {
	        super.onReceive(context, intent);
	    }
	    
	}
public void battInfo(){	
	
		battperc =CPUInfo.batteryLevel();
		batttemp = CPUInfo.batteryTemp();
		battcurrent = CPUInfo.batteryDrain();
		
}
public static String tempConverter(String tempPref, double cTemp) {
	String tempNew = "";
	/**
	 * cTemp = temperature in celsius tempPreff = string from shared
	 * preferences with value fahrenheit, celsius or kelvin
	 */
	if (tempPref.equals("fahrenheit")) {
		tempNew = ((cTemp * 1.8) + 32) + "°F";

	} else if (tempPref.equals("celsius")) {
		tempNew = cTemp + "°C";

	} else if (tempPref.equals("kelvin")) {

		tempNew = (cTemp + 273.15) + "°C";

	}
	return tempNew;
}
}
