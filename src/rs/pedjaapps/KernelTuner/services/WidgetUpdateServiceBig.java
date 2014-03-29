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

import java.io.File;
import java.util.Calendar;
import java.util.List;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.Frequency;
import rs.pedjaapps.KernelTuner.entry.FrequencyCollection;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.receiver.AppWidgetBig;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;


public class WidgetUpdateServiceBig extends Service
{
	
	private String led;
	private String cpu0curr;
	private String cpu0gov;
	private String cpu1gov;
	private String vsync = " ";
	private String fastcharge = " ";
	
	private String cdepth = " ";
	private String kernel = "     ";
	private String deepSleep;
	private String upTime;
	private int charge;
	private String battperc;
	private String batttemp;
	private String battvol;
	private String batttech;
	private String battcurrent;
	private String batthealth;
	private String battcap;
	private List<String> frequencies = FrequencyCollection.getInstance().getFrequencyStrings();
	private int cf = 0;
	private String cpu1curr;
	private int cf2 = 0;
	private double timeint = 30;
	private int bgRes = 0;
	private int cpuTempPath = IOHelper.getCpuTempPath();
	
	private void mountdebugfs()
	{
	
           CommandCapture command = new CommandCapture(0, "mount -t debugfs debugfs /sys/kernel/debug");
		try{
			RootTools.getShell(true).add(command).waitForFinish();
		}
		catch(Exception e){

		}
		
	}


	private void enableTemp()
	{
	     CommandCapture command = new CommandCapture(0, 
            "chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode\n",
            "chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode\n",
            "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode\n",
            "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode\n");
		try{
			RootTools.getShell(true).add(command).waitForFinish();
		}
		catch(Exception e){

		}
	           

	}
	private void info()
	{
		System.out.println("widget service big");
		cpu0gov = IOHelper.cpu0CurGov();
		cpu1gov = IOHelper.cpu1CurGov();
		led = IOHelper.leds();
		fastcharge = ""+IOHelper.fcharge();
		vsync = ""+IOHelper.vsync();
		cdepth = IOHelper.cDepth();
		cpu0curr = IOHelper.cpu0CurFreq();
		cpu1curr = IOHelper.cpu1CurFreq();
		upTime = IOHelper.uptime();
		deepSleep =IOHelper.deepSleep();
		kernel = IOHelper.kernel();
		battperc = ""+IOHelper.batteryLevel();
		charge = IOHelper.batteryChargingSource();
		batttemp =""+IOHelper.batteryTemp();
		battvol =""+IOHelper.batteryVoltage();
		batttech = IOHelper.batteryTechnology();
		battcurrent = IOHelper.batteryDrain();
		batthealth = IOHelper.batteryHealth();
		battcap = IOHelper.batteryCapacity();
		int freqslength = frequencies.size();
		int index = frequencies.indexOf(cpu0curr);
		int index2 = frequencies.indexOf(cpu1curr);
		cf = index * 100 / freqslength + 4;
		cf2 = index2 * 100 / freqslength + 4;
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if(frequencies == null || frequencies.size() == 0)
		{
			FrequencyCollection.getInstance().getAllFrequencies();
			frequencies = FrequencyCollection.getInstance().getFrequencyStrings();
		}
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
																		 .getApplicationContext());

		int[] allWidgetIds = intent
			.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		File file = new File("/sys/kernel/debug");
		File[] contents = file.listFiles();
		if (contents == null) {
		}
		else if (contents.length == 0) {
			mountdebugfs();
		}
		else {
		}
		if(IOHelper.isTempEnabled()==false){
		enableTemp();
		}
		info();
		for (int widgetId : allWidgetIds)
		{

			RemoteViews remoteViews = new RemoteViews(this
													  .getApplicationContext().getPackageName(),
													  R.layout.widget_4x4);

			Paint p = new Paint(); 
			p.setAntiAlias(true);
			p.setStyle(Style.STROKE);
			p.setStyle(Paint.Style.FILL);
			p.setStrokeWidth(8);
			p.setColor(0xFFFF0000);

			Bitmap bitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);

			p.setColor(Color.BLUE);

			for (int i = 5, a = 8; a < 100 && i < 100; i = i + 4, a = a + 4)
			{

				Rect rect = new Rect(10, i, 50, a);
				RectF rectF = new RectF(rect);
				canvas.drawRoundRect(rectF, 1, 1, p);

				rect = new Rect(51, i, 91, a);
				rectF = new RectF(rect);
				canvas.drawRoundRect(rectF, 1, 1, p);

			}

			for (int i = 97, a = 100; a > 100 - cf && i > 100 - cf; i = i - 4, a = a - 4)
			{
				p.setColor(Color.GREEN);
				Rect rect = new Rect(10, i, 50, a);
				RectF rectF = new RectF(rect);
				canvas.drawRoundRect(rectF, 1, 1, p);

				rect = new Rect(51, i, 91, a);
				rectF = new RectF(rect);
				canvas.drawRoundRect(rectF, 1, 1, p);
			}

			Bitmap bitmap2 = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
			Canvas canvas2 = new Canvas(bitmap2);
			if (!cpu1curr.equals("offline"))
			{
				for (int i = 5, a = 8; a < 100 && i < 100; i = i + 4, a = a + 4)
				{
					p.setColor(Color.BLUE);
					Rect rect = new Rect(10, i, 50, a);
					RectF rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

					rect = new Rect(51, i, 91, a);
					rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

				}

				for (int i = 97, a = 100; a > 100 - cf2 && i > 100 - cf2; i = i - 4, a = a - 4)
				{

					p.setColor(Color.GREEN);
					Rect rect = new Rect(10, i, 50, a);
					RectF rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

					rect = new Rect(51, i, 91, a);
					rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

				}
			}
			else
			{
				for (int i = 5, a = 8; a < 100 && i < 100; i = i + 4, a = a + 4)
				{
					p.setColor(Color.BLUE);
					Rect rect = new Rect(10, i, 50, a);
					RectF rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

					rect = new Rect(51, i, 91, a);
					rectF = new RectF(rect);
					canvas2.drawRoundRect(rectF, 1, 1, p);

				}
			}

			remoteViews.setImageViewBitmap(R.id.imageView7, bitmap);
			remoteViews.setImageViewBitmap(R.id.ImageView01, bitmap2);
			if (!cpu0curr.equals("offline"))
			{
				remoteViews.setTextViewText(R.id.freq0, cpu0curr.substring(0, cpu0curr.length() - 3) + "Mhz");
			}
			else
			{
				remoteViews.setTextViewText(R.id.freq0, cpu0curr);
				remoteViews.setTextColor(R.id.freq0, Color.RED);
			}

			if (!cpu1curr.equals("offline"))
			{
				remoteViews.setTextViewText(R.id.freq1, cpu1curr.substring(0, cpu1curr.length() - 3) + "Mhz");
				remoteViews.setTextColor(R.id.freq1, Color.WHITE);
			}
			else
			{
				remoteViews.setTextViewText(R.id.freq1, cpu1curr);
				remoteViews.setTextColor(R.id.freq1, Color.RED);
			}


			remoteViews.setTextViewText(R.id.textView9,	cpu0gov);

			remoteViews.setTextViewText(R.id.TextView02, cpu1gov);
			remoteViews.setTextColor(R.id.TextView02, Color.WHITE);
			if (cpu1gov.equals("offline"))
			{
				remoteViews.setTextColor(R.id.TextView02, Color.RED);
			}
			try
			{
				if (Integer.parseInt(led) < 2)
				{
					remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.red);
				}
				else if (Integer.parseInt(led) < 11 && Integer.parseInt(led) >= 2)
				{
					remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.yellow);

				}
				else if (Integer.parseInt(led) >= 11)
				{
					remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.green);
				}
				else
				{
					remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.err);
				}}
			catch (Exception e)
			{

				remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.err);

			}
			if (fastcharge.equals("1"))
			{
				remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.green);
			}
			else if (fastcharge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.red);
			}
			else
			{
				remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.err);
			}

			if (vsync.equals("1"))
			{
				remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.green);
			}
			else if (vsync.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.red);
			}
			else
			{
				remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.err);
			}

			if (cdepth.equals("16"))
			{
				remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.red);
			}
			else if (cdepth.equals("24"))
			{
				remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.yellow);
			}
			else if (cdepth.equals("32"))
			{
				remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.green);
			}
			else
			{
				remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.err);
			}

			remoteViews.setTextViewText(R.id.textView1k, kernel.trim());
			remoteViews.setTextViewText(R.id.textView11, upTime);
			remoteViews.setTextViewText(R.id.textView13, deepSleep);

			int battperciconint = 0;
			try
			{
				battperciconint = Integer.parseInt(battperc.substring(0, battperc.length()).trim());
			}
			catch (Exception e)
			{
				battperciconint = 0;
			}
			if (battperciconint <= 15 && battperciconint != 0 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_low);
				remoteViews.setTextColor(R.id.textView14, Color.RED);
			}
			else if (battperciconint > 30 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_full);
				remoteViews.setTextColor(R.id.textView14, Color.GREEN);

			}
			else if (battperciconint < 30 && battperciconint > 15 && charge==0)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_half);
				remoteViews.setTextColor(R.id.textView14, Color.YELLOW);
			}
			else if (charge==1)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_charge_usb);
				remoteViews.setTextColor(R.id.textView14, Color.CYAN);
			}
			else if (charge==2)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_charge_ac);
				remoteViews.setTextColor(R.id.textView14, Color.CYAN);
			}
			else if (battperciconint == 0)
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.err);
			}
			remoteViews.setProgressBar(R.id.progressBar1, 100, battperciconint, false);
			remoteViews.setTextViewText(R.id.textView14, battperc + "%");

			double battempint = Double.parseDouble(batttemp)/10;
			String cpuTemp = IOHelper.cpuTemp(cpuTempPath);
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			String tempPref = sharedPrefs.getString("temp", "celsius");
			if (tempPref.equals("fahrenheit"))
			{
				cpuTemp = ""+((int)(Double.parseDouble(cpuTemp) * 1.8) + 32);
				remoteViews.setTextViewText(R.id.textView27, cpuTemp + "°F");
				
		  		int temp = Integer.parseInt(cpuTemp);

		  		if (temp < 113)
				{
		  			remoteViews.setTextColor(R.id.textView27, Color.GREEN);
				}
				else if (temp >= 113 && temp < 138)
				{
					remoteViews.setTextColor(R.id.textView27, Color.YELLOW);
				}

				else if (temp >= 138)
				{
					remoteViews.setTextColor(R.id.textView27, Color.RED);

				}
			}

			else if (tempPref.equals("celsius"))
			{
				remoteViews.setTextViewText(R.id.textView27, cpuTemp + "°C");
				int temp = Integer.parseInt(cpuTemp);
				if (temp < 45)
				{
					remoteViews.setTextColor(R.id.textView27, Color.GREEN);
				}
				else if (temp >= 45 && temp <= 59)
				{
					remoteViews.setTextColor(R.id.textView27, Color.YELLOW);
				}

				else if (temp > 59)
				{
					remoteViews.setTextColor(R.id.textView27, Color.RED);

				}
			}
			/**
			If kelvin is selected in settings convert cpu temp to kelvin
			*/
			else if (tempPref.equals("kelvin"))
			{
				cpuTemp = ""+((int)(Double.parseDouble(cpuTemp) + 273.15));
				
				remoteViews.setTextViewText(R.id.textView27, cpuTemp + "°K");
				
				int temp = Integer.parseInt(cpuTemp);
				if (temp < 318)
				{
					remoteViews.setTextColor(R.id.textView27, Color.GREEN);
				}
				else if (temp >= 318 && temp <= 332)
				{
					remoteViews.setTextColor(R.id.textView27, Color.YELLOW);
				}

				else if (temp > 332)
				{
					remoteViews.setTextColor(R.id.textView27, Color.RED);;

				}
			}
			if (tempPref.equals("fahrenheit"))
			{
				battempint = (battempint * 1.8) + 32;
				remoteViews.setTextViewText(R.id.textView20, ((int)battempint) + "°F");
				if (battempint <= 104)
				{
					remoteViews.setTextColor(R.id.textView20, Color.GREEN);
				}
				else if (battempint > 104 && battempint < 131)
				{
					remoteViews.setTextColor(R.id.textView20, Color.YELLOW);
				}
				
				else if (battempint >= 140)
				{
					remoteViews.setTextColor(R.id.textView20, Color.RED);
				}
				
			}
			else if (tempPref.equals("celsius"))
			{
				remoteViews.setTextViewText(R.id.textView20, ((int)battempint) + "°C");
				if (battempint <= 45)
				{
					remoteViews.setTextColor(R.id.textView20, Color.GREEN);
				}
				else if (battempint > 45 && battempint < 55)
				{
					remoteViews.setTextColor(R.id.textView20, Color.YELLOW);
				}
				
				else if (battempint >= 55)
				{
					remoteViews.setTextColor(R.id.textView20, Color.RED);
				}
				
			}
			else if (tempPref.equals("kelvin"))
			{
				battempint = battempint + 273.15;
				remoteViews.setTextViewText(R.id.textView20, ((int)battempint) + "°K");
				if (battempint <= 318.15)
				{
					remoteViews.setTextColor(R.id.textView20, Color.GREEN);
				}
				else if (battempint > 318.15 && battempint < 328.15)
				{
					remoteViews.setTextColor(R.id.textView20, Color.YELLOW);
				}
				
				else if (battempint >= 328.15)
				{
					remoteViews.setTextColor(R.id.textView20, Color.RED);
				}
				
			}
			remoteViews.setTextViewText(R.id.textView21, battvol.trim() + "mV");
			remoteViews.setTextViewText(R.id.textView22, batttech);
			remoteViews.setTextViewText(R.id.textView23, battcurrent + "mA");
			if(battcurrent.length()>0){
			if (battcurrent.substring(0, 1).equals("-"))
			{
				remoteViews.setTextColor(R.id.textView23, Color.RED);
			}
			else
			{
				remoteViews.setTextViewText(R.id.textView23, "+" + battcurrent + "mA");
				remoteViews.setTextColor(R.id.textView23, Color.GREEN);
			}
			}
			remoteViews.setTextViewText(R.id.textView24, batthealth);
			remoteViews.setTextViewText(R.id.textView26, battcap + "mAh");
			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(),
											AppWidgetBig.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
								 allWidgetIds);



			String timer = sharedPrefs.getString("widget_time", "30");
			

			try
			{
				timeint = Double.parseDouble(timer.trim());
			}
			catch (Exception e)
			{
				timeint = 30;
			}
			String widgetBgPref = sharedPrefs.getString("widget_bg","grey");
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
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
																	 PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
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
