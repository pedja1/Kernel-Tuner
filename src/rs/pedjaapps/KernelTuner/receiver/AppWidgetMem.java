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
import java.text.DecimalFormat;
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
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

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
		long freeInternal = getAvailableSpaceInBytesOnInternalStorage();
		long usedInternal = getUsedSpaceInBytesOnInternalStorage();
		long totalInternal = getTotalSpaceInBytesOnInternalStorage();
		long freeExternal = getAvailableSpaceInBytesOnExternalStorage();
		long usedExternal = getUsedSpaceInBytesOnExternalStorage();
		long totalExternal = getTotalSpaceInBytesOnExternalStorage();
		remoteViews.setTextViewText(R.id.textView7, freeRAM+"MB/" + totalRAM + "MB");
		remoteViews.setProgressBar(R.id.progressBar2, 100, usedRAM * 100 / totalRAM, false);

		remoteViews.setTextViewText(R.id.textView10, humanReadableSize(freeInternal)+"/" + humanReadableSize(totalInternal));
		remoteViews.setProgressBar(R.id.progressBar3, 100, (int) (usedInternal * 100 / totalInternal), false);
		boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			remoteViews.setTextViewText(R.id.textView13, humanReadableSize(freeExternal)+"/"
					+ humanReadableSize(totalExternal));
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

	public static long getAvailableSpaceInBytesOnInternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
				* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getAvailableSpaceInBytesOnExternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
				* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public String humanReadableSize(long size) {
		String hrSize = "";

		long b = size;
		double k = size / 1024.0;
		double m = size / 1048576.0;
		double g = size / 1073741824.0;
		double t = size / 1099511627776.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1) {

			hrSize = dec.format(t).concat("TB");
		} else if (g > 1) {

			hrSize = dec.format(g).concat("GB");
		} else if (m > 1) {

			hrSize = dec.format(m).concat("MB");
		} else if (k > 1) {

			hrSize = dec.format(k).concat("KB");

		} else if (b > 1) {
			hrSize = dec.format(b).concat("B");
		}

		return hrSize;

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
