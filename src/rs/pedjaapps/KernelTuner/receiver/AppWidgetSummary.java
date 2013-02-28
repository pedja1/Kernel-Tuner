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
package rs.pedjaapps.KernelTuner.receiver;


import java.util.Calendar;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
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
import rs.pedjaapps.KernelTuner.tools.Tools;

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
		if(gpu3d.length()>6){
			
			remoteViews.setTextViewText(R.id.gpu_3d, gpu3d.substring(0, gpu3d.length()-6)+"Mhz");
		}
		if(gpu2d.length()>6){
			
			remoteViews.setTextViewText(R.id.gpu_2d, gpu2d.substring(0, gpu2d.length()-6)+"Mhz");
		}
		
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
			remoteViews.setTextViewText(R.id.textView3, Tools.tempConverter(pref.getString("temp", "celsius"), batttemp));
		} else {
			remoteViews.setTextViewText(R.id.textView3, "Unknown");
		}
		if (battcurrent.length()<0) {
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
		uptime = IOHelper.uptime();
		sleep = IOHelper.deepSleep();
		min = IOHelper.cpuMin();
		max = IOHelper.cpuMax();
		min = min.substring(0, min.length()-3)+"Mhz";
		max = max.substring(0, max.length()-3)+"Mhz";
		governor = IOHelper.cpu0CurGov();
		temp = Tools.tempConverter(pref.getString("temp", "celsius"), Double.parseDouble(IOHelper.cpuTemp()));
		gpu2d = IOHelper.gpu2d();
		gpu3d = IOHelper.gpu3d();
		fc = IOHelper.fcharge();
		vsync = IOHelper.vsync();
		if(IOHelper.leds().length()>0){
		light = ((Integer.parseInt(IOHelper.leds())*100)/60)+"";
		}
		scheduler = IOHelper.scheduler();
		s2w = IOHelper.s2w();
		cache = IOHelper.sdCache();
		battperc =IOHelper.batteryLevel();
		batttemp = IOHelper.batteryTemp()/10.0;
		battcurrent = IOHelper.batteryDrain();
		load = IOHelper.cpuLoad();
	}
	
}
