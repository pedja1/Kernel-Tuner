package rs.pedjaapps.KernelTuner.receiver;


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

public class AppWidgetSummary extends AppWidgetProvider {

	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	
	RemoteViews remoteViews;
	Context context;
	SharedPreferences pref;
	String uptime;
	String sleep;
	String min;
	String max;
	String governor;
	String temp;
	String gpu2d;
	String gpu3d;
	int fc;
	int vsync;
	String light;
	String scheduler;
	int s2w;
	int cache;
	Integer battperc;
	Double batttemp;
	String battcurrent;
	int load;
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
		Intent active = new Intent(context, AppWidgetSummary.class);
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
		getInfo();
		remoteViews.setTextViewText(R.id.cpu_min, min);
		remoteViews.setTextViewText(R.id.cpu_max, max);
		remoteViews.setTextViewText(R.id.cpu_uptime, uptime);
		remoteViews.setTextViewText(R.id.cpu_sleep, sleep);
		remoteViews.setTextViewText(R.id.cpu_gov, governor);
		remoteViews.setTextViewText(R.id.cpu_temp, temp);
		remoteViews.setTextViewText(R.id.gpu2d, gpu2d);
		remoteViews.setTextViewText(R.id.gpu3d, gpu3d);
		remoteViews.setTextViewText(R.id.misc_bl, light);
		if(vsync==0){
		remoteViews.setTextViewText(R.id.misc_vs, "OFF");
		remoteViews.setTextColor(R.id.misc_vs, Color.RED);
		}
		else{
			remoteViews.setTextViewText(R.id.misc_vs, "ON");
			remoteViews.setTextColor(R.id.misc_vs, Color.GREEN);
		}
		if(fc==0){
			remoteViews.setTextViewText(R.id.misc_fc, "OFF");
			remoteViews.setTextColor(R.id.misc_fc, Color.RED);
		}
		else{
			remoteViews.setTextViewText(R.id.misc_fc, "ON");
			remoteViews.setTextColor(R.id.misc_fc, Color.GREEN);
		}
		remoteViews.setTextViewText(R.id.misc_scheduler, scheduler);
		if(s2w==0){
			remoteViews.setTextViewText(R.id.misc_s2w, "OFF");
			remoteViews.setTextColor(R.id.misc_s2w, Color.RED);
		}
		else{
			remoteViews.setTextViewText(R.id.misc_s2w, "ON");
			remoteViews.setTextColor(R.id.misc_s2w, Color.GREEN);
		}
		remoteViews.setTextViewText(R.id.misc_cache, cache+"KB");
		if (battperc != null) {
			remoteViews.setTextViewText(R.id.textView1, "Level: " + battperc + "%");
			remoteViews.setProgressBar(R.id.progressBar1, 100, battperc, false);
		} else {
			remoteViews.setTextViewText(R.id.textView1, "Unknown");
		}
		if (batttemp != null) {
			remoteViews.setTextViewText(R.id.textView3, tempConverter(pref.getString("temp", "celsius"), batttemp));
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
		remoteViews.setTextViewText(R.id.cpu_load_percent, load+"%");
		remoteViews.setProgressBar(R.id.cpu_load_progress, 100, load, false);
		ComponentName thiswidget = new ComponentName(context, AppWidgetSummary.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_summary);
		 if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
	    	setView(context);
	        System.out.println("summary widget refresh");
	    } 
	    
	    else {
	        super.onReceive(context, intent);
	    }
	    
	}
	
	public void getInfo(){
		uptime = CPUInfo.uptime();
		sleep = CPUInfo.deepSleep();
		min = CPUInfo.cpuMin();
		max = CPUInfo.cpuMax();
		min = min.substring(0, min.length()-3)+"Mhz";
		max = max.substring(0, max.length()-3)+"Mhz";
		governor = CPUInfo.cpu0CurGov();
		temp = tempConverter(pref.getString("temp", "celsius"), Double.parseDouble(CPUInfo.cpuTemp()));
		gpu2d = CPUInfo.gpu2d();
		gpu3d = CPUInfo.gpu3d();
		fc = CPUInfo.fcharge();
		vsync = CPUInfo.vsync();
		light = ((Integer.parseInt(CPUInfo.cbb())*100)/60)+"";
		scheduler = CPUInfo.scheduler();
		s2w = CPUInfo.s2w();
		cache = CPUInfo.sdCache();
		battperc =CPUInfo.batteryLevel();
		batttemp = CPUInfo.batteryTemp();
		battcurrent = CPUInfo.batteryDrain();
		load = CPUInfo.cpuLoad();
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
