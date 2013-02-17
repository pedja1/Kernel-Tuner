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
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.receiver.*;

public class WidgetUpdateService extends Service
{
	
	private String curentfreq;
	private String gov;
	private String battperc;
	private String charge;
	private int timeint;

	private void read()
	{
		System.out.println("widget service");
		try
		{

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			curentfreq = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			curentfreq = "offline";

		}
		try
		{

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			gov = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			gov = "offline";
		}

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/capacity");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			battperc = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			battperc = "0";
		}

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/charging_source");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			charge = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			charge = "0";
		}


	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//Log.i(LOG, "Called");


		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
																		 .getApplicationContext());

		int[] allWidgetIds = intent
			.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		read();

		for (int widgetId : allWidgetIds)
		{
			// Create some random data
			//int number = (new Random().nextInt(100));

			RemoteViews remoteViews = new RemoteViews(this
													  .getApplicationContext().getPackageName(),
													  R.layout.widget_2x1);
			//Log.w("Widget", String.valueOf(number));
			remoteViews.setTextViewText(R.id.textView5, curentfreq.substring(0, curentfreq.length() - 4) + "Mhz");
			int fr = Integer.parseInt(curentfreq.substring(0, curentfreq.length() - 4));
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
			if (battperciconint <= 15 && battperciconint != 0 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_low);
				remoteViews.setTextColor(R.id.textView1, Color.RED);
			}
			else if (battperciconint > 30 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_full);
				remoteViews.setTextColor(R.id.textView1, Color.GREEN);

			}
			else if (battperciconint < 30 && battperciconint > 15 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_half);
				remoteViews.setTextColor(R.id.textView1, Color.YELLOW);
			}
			else if (charge.equals("1"))
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_usb);
				remoteViews.setTextColor(R.id.textView1, Color.CYAN);
			}
			else if (charge.equals("2"))
			{
				remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_ac);
				remoteViews.setTextColor(R.id.textView1, Color.CYAN);
			}
			remoteViews.setProgressBar(R.id.progressBar1, 100, battperciconint, false);
			remoteViews.setTextViewText(R.id.textView1, battperc + "%");
			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(),
											AppWidget.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
								 allWidgetIds);
			final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


			String timer = sharedPrefs.getString("widget_time", "");
			//System.out.println(timer);

		 	try
			{
				timeint = Integer.parseInt(timer.trim());
			}
			catch (Exception e)
			{
				timeint = 30;
			}

			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
																	 PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, timeint*60);
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
