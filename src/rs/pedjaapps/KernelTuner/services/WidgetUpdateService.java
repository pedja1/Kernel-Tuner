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
package rs.pedjaapps.KernelTuner.services;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.widget.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.receiver.*;

public class WidgetUpdateService extends Service
{
	
	private String curentfreq;
	private String gov;
	private String battperc;
	private int charge;
	private double timeint;
	private int bgRes = 0;
	AlarmManager alarmManager;
	PendingIntent pendingIntent;
	
	private void read()
	{
		System.out.println("widget service");
			curentfreq = IOHelper.cpu0CurFreq();
			gov = IOHelper.cpu0CurGov();
			battperc = ""+IOHelper.batteryLevel();
			charge = IOHelper.batteryChargingSource();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		int[] allWidgetIds = intent
			.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		read();

		for (int widgetId : allWidgetIds)
		{
			
			RemoteViews remoteViews = new RemoteViews(this
													  .getApplicationContext().getPackageName(),
													  R.layout.widget_2x1);
			remoteViews.setTextViewText(R.id.textView5, curentfreq.substring(0, curentfreq.length() - 4) + "Mhz");
			int fr;
			try{
			fr = Integer.parseInt(curentfreq.substring(0, curentfreq.length() - 4));
			}
			catch(Exception e){
				fr = 0;
			}
			if (fr <= 918)
			{
				remoteViews.setTextColor(R.id.textView5, Color.YELLOW);
			}
			else if (fr <= 1512 && fr > 918)
			{
				remoteViews.setTextColor(R.id.textView5, Color.GREEN);
			}
			else if (fr > 1512)
			{
				remoteViews.setTextColor(R.id.textView5, Color.RED);
			}
			remoteViews.setTextViewText(R.id.textView6, gov);
			int battperciconint = Integer.parseInt(battperc.substring(0, battperc.length()).trim());
			if (battperciconint <= 15 && battperciconint != 0 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_low);
				remoteViews.setTextColor(R.id.textView1, Color.RED);
			}
			else if (battperciconint > 30 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_full);
				remoteViews.setTextColor(R.id.textView1, Color.GREEN);

			}
			else if (battperciconint < 30 && battperciconint > 15 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_half);
				remoteViews.setTextColor(R.id.textView1, Color.YELLOW);
			}
			else if (charge==1)
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_usb);
				remoteViews.setTextColor(R.id.textView1, Color.CYAN);
			}
			else if (charge==2)
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_ac);
				remoteViews.setTextColor(R.id.textView1, Color.CYAN);
			}
			remoteViews.setProgressBar(R.id.progressBar1, 100, battperciconint, false);
			remoteViews.setTextViewText(R.id.textView1, battperc + "%");
			Intent clickIntent = new Intent(this.getApplicationContext(),
											AppWidget.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
								 allWidgetIds);
			final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


			String timer = sharedPrefs.getString("widget_time", "");

		 	try
			{
				timeint = Double.parseDouble(timer.trim());
			}
			catch (Exception e)
			{
				timeint = 30;
			}

		 	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
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
			if(bgRes!=0){
			    remoteViews.setInt(R.id.widget_layout, "setBackgroundResource", bgRes);
			    }
			pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
																	 PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

			alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, (int)timeint*60);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendar.getTimeInMillis(), pendingIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
		stopSelf();

		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
} 
