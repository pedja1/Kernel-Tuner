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

import android.annotation.*;
import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.graphics.*;
import android.graphics.Paint.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.helpers.*;
import rs.pedjaapps.KernelTuner.receiver.*;

import android.graphics.Bitmap.Config;
import java.lang.Process;


public class WidgetUpdateServiceBig extends Service
{
	
	private List<CPUInfo.FreqsEntry> freqEntries = CPUInfo.frequencies();
	private List<String> freqNames = new ArrayList<String>();
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
	private String charge;
	private String battperc;
	private String batttemp;
	private String battvol;
	private String batttech;
	private String battcurrent;
	private String batthealth;
	private String battcap;
	private List<String> frequencies = new ArrayList<String>();
	private int cf = 0;
	private String cpu1curr;
	private int cf2 = 0;
	private int timeint = 30;
	private boolean enableTmp(){
		boolean b;
		try
		{

			File myFile = new File("/sys/devices/virtual/thermal/thermal_zone1/mode");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			myReader.close();
			fIn.close();
			if(aBuffer.trim().equals("enabled")){
				b = false;
			}
			else{
				b=true;
			}
			
			

		}
		catch (Exception e)
		{
			b=true;
		}
		return b;
	}


	
	

	private void mountdebugfs()
	{
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n").getBytes());
           
            stdin.flush();

            stdin.close();
            BufferedReader brCleanUp =
                    new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                Log.d("[KernelTuner Widget Output]", line);
            }
            brCleanUp.close();
            brCleanUp =
                    new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
            	Log.e("[KernelTuner Widget Error]", line);
            }
            brCleanUp.close();

        } catch (IOException ex) {
        }
	}


	private void enableTemp()
	{
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write(("chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode\n").getBytes());
            stdin.write(("chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode\n").getBytes());
            stdin.write(("echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode\n").getBytes());
            stdin.write(("echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode\n").getBytes());
	           
            stdin.flush();

            stdin.close();
            BufferedReader brCleanUp =
                    new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                Log.d("[KernelTuner EnableTempMonitor Output]", line);
            }
            brCleanUp.close();
            brCleanUp =
                    new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
            	Log.e("[KernelTuner EnableTempMonitor Error]", line);
            }
            brCleanUp.close();

        } catch (IOException ex) {
        }

	}


	private void info()
	{


		
		cpu0gov = CPUInfo.cpu0CurGov();
		cpu1gov = CPUInfo.cpu1CurGov();
		led = CPUInfo.cbb();
		
		fastcharge = ""+CPUInfo.fcharge();
		vsync = ""+CPUInfo.vsync();
		cdepth = CPUInfo.cDepth();
		for(CPUInfo.FreqsEntry f: freqEntries){
			frequencies.add(""+f.getFreq());
		}
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}
		cpu0curr = CPUInfo.cpu0CurFreq();
		cpu1curr = CPUInfo.cpu1CurFreq();
		upTime = CPUInfo.uptime();
		deepSleep =CPUInfo.deepSleep();

		try
		{

			File myFile = new File("/proc/version");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			kernel = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			kernel = "Kernel version file not found";

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

		//System.out.println(cpu0min);

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/batt_temp");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			batttemp = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			batttemp = "0";
		}

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/batt_vol");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			battvol = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			battvol = "0";
		}

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/technology");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			batttech = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			batttech = "err";
		}	

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/batt_current");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			battcurrent = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			battcurrent = "err";
		}	

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/health");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			batthealth = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			batthealth = "err";
		}	

		try
		{

			File myFile = new File("/sys/class/power_supply/battery/full_bat");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			battcap = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			battcap = "err";
		}	

		
		

		int freqslength = frequencies.size();
		int index = frequencies.indexOf(cpu0curr);
		int index2 = frequencies.indexOf(cpu1curr);

		cf = index * 100 / freqslength + 4;
		cf2 = index2 * 100 / freqslength + 4;
		
	}

	@SuppressLint("ParserError")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//Log.i(LOG, "Called");
		// Create some random data

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
																		 .getApplicationContext());

		int[] allWidgetIds = intent
			.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		

		File file = new File("/sys/kernel/debug");
		
		File[] contents = file.listFiles();
		// the directory file is not really a directory..
		if (contents == null) {

		}
		// Folder is empty
		else if (contents.length == 0) {
			mountdebugfs();
		}
		// Folder contains files
		else {

		}
		if(enableTmp()==true){
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
			// canvas2.drawArc(new RectF(5, 5, 90, 90), 90, angle, true, p);
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

			//System.out.println(cpu0min);
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
			if (battperciconint <= 15 && battperciconint != 0 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_low);
				remoteViews.setTextColor(R.id.textView14, Color.RED);
			}
			else if (battperciconint > 30 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_full);
				remoteViews.setTextColor(R.id.textView14, Color.GREEN);

			}
			else if (battperciconint < 30 && battperciconint > 15 && charge.equals("0"))
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_half);
				remoteViews.setTextColor(R.id.textView14, Color.YELLOW);
			}
			else if (charge.equals("1"))
			{
				remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_charge_usb);
				remoteViews.setTextColor(R.id.textView14, Color.CYAN);
			}
			else if (charge.equals("2"))
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
			String cpuTemp = CPUInfo.cpuTemp();
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
			if (battcurrent.substring(0, 1).equals("-"))
			{
				remoteViews.setTextColor(R.id.textView23, Color.RED);
			}
			else
			{
				remoteViews.setTextViewText(R.id.textView23, "+" + battcurrent + "mA");
				remoteViews.setTextColor(R.id.textView23, Color.GREEN);
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
				timeint = Integer.parseInt(timer.trim());
			}
			catch (Exception e)
			{
				timeint = 30;
			}
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
																	 PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_layout2, pendingIntent);
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, timeint*60);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20 * 1000, pendingIntent);

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
