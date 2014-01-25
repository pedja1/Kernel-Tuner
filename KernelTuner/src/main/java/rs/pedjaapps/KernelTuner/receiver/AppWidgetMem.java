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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

import rs.pedjaapps.KernelTuner.R;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.MemoryInfo;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import rs.pedjaapps.KernelTuner.utility.Tools;

public class AppWidgetMem extends AppWidgetProvider {

	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	
	RemoteViews remoteViews;
	Context context;
	ActivityManager activityManager;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		   this.context = context;
		int bgRes = R.drawable.lcd_background_grey;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
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
		Intent active = new Intent(context, AppWidgetMem.class);
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
		int freeRAM = getFreeRAM();
		int totalRAM = getTotalRAM();
		int usedRAM = getTotalRAM() - getFreeRAM();
		long freeInternal = Tools.getAvailableSpaceInBytesOnInternalStorage();
		long usedInternal = Tools.getUsedSpaceInBytesOnInternalStorage();
		long totalInternal = Tools.getTotalSpaceInBytesOnInternalStorage();
		long freeExternal = Tools.getAvailableSpaceInBytesOnExternalStorage();
		long usedExternal = Tools.getUsedSpaceInBytesOnExternalStorage();
		long totalExternal = Tools.getTotalSpaceInBytesOnExternalStorage();
		remoteViews.setTextViewText(R.id.textView7, freeRAM+"MB/" + totalRAM + "MB");
		remoteViews.setProgressBar(R.id.progressBar2, 100, usedRAM * 100 / totalRAM, false);

		remoteViews.setTextViewText(R.id.textView10, Tools.byteToHumanReadableSize(freeInternal)+"/" + Tools.byteToHumanReadableSize(totalInternal));
		remoteViews.setProgressBar(R.id.progressBar3, 100, (int) (usedInternal * 100 / totalInternal), false);
		boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			remoteViews.setTextViewText(R.id.textView13, Tools.byteToHumanReadableSize(freeExternal)+"/"
					+ Tools.byteToHumanReadableSize(totalExternal));
			remoteViews.setProgressBar(R.id.progressBar4, 100, (int) (usedExternal * 100 / totalExternal), false);
		} else {
			remoteViews.setTextViewText(R.id.textView12, "External Storage not present");
			remoteViews.setViewVisibility(R.id.textView13, View.GONE);
			remoteViews.setViewVisibility(R.id.progressBar4, View.GONE);
		}
		ComponentName thiswidget = new ComponentName(context, AppWidgetMem.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
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
		activityManager.getMemoryInfo(mi);
		Integer mem = (int) (mi.availMem / 1048576L);
		return mem;

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		activityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_memory);
	    if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
	    	setView(context);
	        System.out.println("mem widget refresh");
	    } 
	    else {
	        super.onReceive(context, intent);
	    }
	    
	}
}
