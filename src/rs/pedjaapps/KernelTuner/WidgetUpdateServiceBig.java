package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.widget.RemoteViews;


public class WidgetUpdateServiceBig extends Service
{
	
	private List<CPUInfo.FreqsEntry> freqEntries = CPUInfo.frequencies();
	private List<String> freqNames = new ArrayList<String>();
	public String led;
	public String cpu0curr;
	public String gov;
	public String cpu0gov;
	public String cpu1gov;
	public String cpu0max = "        ";
	public String cpu1max = "        ";
	public String cpu0min = "     7   ";
	public String cpu1min = "        ";
	public String gpu2d = "        ";
	public String gpu3d = "        ";
	public int countcpu0;
	public int countcpu1;
	public String vsync = " ";
	public String fastcharge = " ";
	public String out;
	public String cdepth = " ";
	public String kernel = "     ";
	public String deepSleep;
	public String upTime;
	public String charge;
	public String battperc;
	public String batttemp;
	public String battvol;
	public String batttech;
	public String battcurrent;
	public String batthealth;
	public String battcap;
	public List<String> frequencies = new ArrayList<String>();
	public int angle;
	public int cf = 0;
	public String cpu1curr;
	public int cf2 = 0;
	public int timeint = 30;
	//public boolean enableTmp = true;
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


	
	

	public void mountdebugfs()
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


	public void enableTemp()
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


	public void info()
	{


		cpu0min = CPUInfo.cpu0MinFreq();
		cpu0max = CPUInfo.cpu0MaxFreq();
		cpu1min = CPUInfo.cpu1MinFreq();
		cpu1max = CPUInfo.cpu1MaxFreq();
		cpu0gov = CPUInfo.cpu0CurGov();
		cpu1gov = CPUInfo.cpu1CurGov();
		led = CPUInfo.cbb();
		gpu2d = CPUInfo.gpu2d();
		gpu3d = CPUInfo.gpu3d();
		fastcharge = String.valueOf(CPUInfo.fcharge());
		vsync = String.valueOf(CPUInfo.vsync());
		cdepth = CPUInfo.cDepth();
		for(CPUInfo.FreqsEntry f: freqEntries){
			frequencies.add(String.valueOf(f.getFreq()));
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
		//System.out.println(angle);
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

		/*ComponentName thisWidget = new ComponentName(getApplicationContext(),
													 AppWidgetBig.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);*/
		//Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
		// Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));


		File file = new File("/sys/kernel/debug");
		/*if(file.list().length>0){
			
		}
		else{
			
			mountdebugfs();
		}*/
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
				cpuTemp = String.valueOf((int)(Double.parseDouble(cpuTemp) * 1.8) + 32);
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
				cpuTemp = String.valueOf((int)(Double.parseDouble(cpuTemp) + 273.15));
				
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
				remoteViews.setTextViewText(R.id.textView20, String.valueOf((int)battempint) + "°F");
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
				remoteViews.setTextViewText(R.id.textView20, String.valueOf((int)battempint) + "°C");
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
				remoteViews.setTextViewText(R.id.textView20, String.valueOf((int)battempint) + "°K");
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
