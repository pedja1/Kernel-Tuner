package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.os.*;
import java.util.*;
import android.graphics.*;
import android.content.*;
import android.preference.*;
import android.app.*;
import java.io.*;
import java.lang.Process;

import rs.pedjaapps.KernelTuner.R;

public class WidgetUpdateServiceBig extends Service {

  private static final String LOG = "";
public String led;
public String curentfreq;
public String gov;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
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
	public String temp;
	public String tmp;
	public String charge;
	public String battperc;
	public String batttemp;
	public String battvol;
	public String batttech;
	public String battcurrent;
	public String batthealth;
	public String battcap;
	public String cpu0freqs;
	public int angle;
	public int cf = 0;
	public String curentfreqcpu1;
	public int cf2 = 0;
	public int timeint = 1800;


	public void uptime(){
		long uptime = SystemClock.elapsedRealtime();
		int hr =  (int) ((uptime / 1000) / 3600);
		int   mn =  (int) (((uptime / 1000) / 60) % 60);
		int sc =  (int) ((uptime / 1000) % 60);
		String minut = String.valueOf(mn);
		String sekund = String.valueOf(sc);
		String sati = String.valueOf(hr);
		
		tmp= sati+"h:"+minut+"m:"+sekund+"s";
	
	}
	public void deepsleep(){
		temp = String.valueOf(SystemClock.elapsedRealtime()-SystemClock.uptimeMillis()); 
		int time = Integer.parseInt(temp);
		int hr =  ((time / 1000) / 3600);
		int mn =  (((time / 1000) / 60) % 60);
		int sc =  ((time / 1000) % 60);
		String minut = String.valueOf(mn);
		String sekund = String.valueOf(sc);
		String sati = String.valueOf(hr);

		temp = sati+"h:"+minut+"m:"+sekund+"s";
	}
	
	public void mountdebugfs(){
		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("mount -t debugfs debugfs /sys/kernel/debug\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	public void info(){
		
		
		
		System.out.println(cpu0min);
			try {

				File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu0min = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				cpu0min="offline";
			}

			try {

				File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu0max = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				cpu0max="offline";
			}

			try {

				File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu1min = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				cpu1min = "offline";
			}

			try {

				File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu1max = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				cpu1max = "offline";
			}

			try {

				File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu0 = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				curentgovernorcpu0="offline";
			}

			try {

    			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor");
    			FileInputStream fIn = new FileInputStream(myFile);

    			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null) {
    				aBuffer += aDataRow + "\n";
    			}

    			curentgovernorcpu1 = aBuffer.trim();
    			myReader.close();

    		} catch (Exception e) {
    			curentgovernorcpu1 = "offline";
    		}

			try {
        		String aBuffer = "";
				File myFile = new File("/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				led = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				led="UNSUPPORTED";
			}

			try {

				File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu3d = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				gpu3d = "UNUSPPORTED";

			}

			try {

				File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu2d = aBuffer.trim();

				myReader.close();

			} catch (Exception e) {
				gpu2d = "UNSUPPORTED";
			}

			try {
        		String aBuffer = "";
				File myFile = new File("/sys/kernel/fast_charge/force_fast_charge");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				fastcharge = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				fastcharge = "UNSUPPORTED";
			}

			try {
        		String aBuffer = "";
				File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				vsync = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				vsync ="UNSUPPORTED";
			}

			try {

				File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cdepth = aBuffer.trim();
				myReader.close();
			} catch (Exception e) {
				cdepth = "UNSUPPORTED";

			}

			try {

				File myFile = new File("/proc/version");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				kernel = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				kernel = "Kernel version file not found";

			}

		
		try {

			File myFile = new File("/sys/class/power_supply/battery/capacity");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			battperc = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			battperc="0";
		}

		try {

			File myFile = new File("/sys/class/power_supply/battery/charging_source");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			charge = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			charge="0";
		}

		System.out.println(cpu0min);
	
	try {

		File myFile = new File("/sys/class/power_supply/battery/batt_temp");
		FileInputStream fIn = new FileInputStream(myFile);

		BufferedReader myReader = new BufferedReader(
			new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		batttemp = aBuffer.trim();
		myReader.close();

	} catch (Exception e) {
		batttemp="0";
	}

		try {

			File myFile = new File("/sys/class/power_supply/battery/batt_vol");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			battvol = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			battvol="0";
		}
		
		try {

			File myFile = new File("/sys/class/power_supply/battery/technology");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			batttech = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			batttech="err";
		}	
	
		try {

			File myFile = new File("/sys/class/power_supply/battery/batt_current");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			battcurrent = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			battcurrent="err";
		}	
	
		try {

			File myFile = new File("/sys/class/power_supply/battery/health");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			batthealth = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			batthealth="err";
		}	
	
		try {

			File myFile = new File("/sys/class/power_supply/battery/full_bat");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			battcap = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			battcap="err";
		}	
	
		try {

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			cpu0freqs = aBuffer;
			myReader.close();

		} catch (Exception e) {
			cpu0freqs="offline";
		}
		try {

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			curentfreq = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			curentfreq = "offline";

		}
	
		try {

			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			curentfreqcpu1 = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {
			curentfreqcpu1 = "offline";

		}

	String[] freqarray = cpu0freqs.split(" ");
	int freqslength = freqarray.length;
	List<String> wordList = Arrays.asList(freqarray); 
	int index = wordList.indexOf(curentfreq);
	int index2 = wordList.indexOf(curentfreqcpu1);
	
	cf = index*100/freqslength+4;
	cf2 = index2*100/freqslength+4;
	System.out.println(angle);
}

  @SuppressLint("ParserError")
@Override
  public void onStart(Intent intent, int startId) {
    Log.i(LOG, "Called");
    // Create some random data

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    ComponentName thisWidget = new ComponentName(getApplicationContext(),
        AppWidgetBig.class);
    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
    Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
    Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
	  
		  
	  File file = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
	  try{

		  InputStream fIn = new FileInputStream(file);


	  }
	  catch(FileNotFoundException e){
		  mountdebugfs();
	  }
    info();
	
	uptime();
	deepsleep();
    
    for (int widgetId : allWidgetIds) {

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

		for (int i = 5, a = 8; a<100 && i<100; i=i+4, a=a+4	){
			
			Rect rect = new Rect(10,i,50,a);
			RectF rectF = new RectF(rect);
			canvas.drawRoundRect( rectF, 1,1, p);
			
			 rect = new Rect(51,i,91,a);
			rectF = new RectF(rect);
			canvas.drawRoundRect( rectF, 1,1, p);
		
		}
		
		for (int i = 97, a = 100; a>100-cf && i>100-cf; i=i-4, a=a-4	){

			p.setColor(Color.GREEN);
			Rect rect = new Rect(10,i,50,a);
			RectF rectF = new RectF(rect);
			canvas.drawRoundRect( rectF, 1,1, p);

			rect = new Rect(51,i,91,a);
			rectF = new RectF(rect);
			canvas.drawRoundRect( rectF, 1,1, p);

		}

	    Bitmap bitmap2 = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
	   Canvas canvas2 = new Canvas(bitmap2);
	   // canvas2.drawArc(new RectF(5, 5, 90, 90), 90, angle, true, p);
	    if(!curentfreqcpu1.equals("offline")){
			for (int i = 5, a = 8; a<100 && i<100; i=i+4, a=a+4	){
				p.setColor(Color.BLUE);
				Rect rect = new Rect(10,i,50,a);
				RectF rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

				rect = new Rect(51,i,91,a);
				rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

			}

			for (int i = 97, a = 100; a>100-cf2 && i>100-cf2; i=i-4, a=a-4	){

				p.setColor(Color.GREEN);
				Rect rect = new Rect(10,i,50,a);
				RectF rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

				rect = new Rect(51,i,91,a);
				rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

			}
		}
	    else{
			for (int i = 5, a = 8; a<100 && i<100; i=i+4, a=a+4	){
				p.setColor(Color.BLUE);
				Rect rect = new Rect(10,i,50,a);
				RectF rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

				rect = new Rect(51,i,91,a);
				rectF = new RectF(rect);
				canvas2.drawRoundRect( rectF, 1,1, p);

			}
		}
	
	    remoteViews.setImageViewBitmap(R.id.imageView7, bitmap);
	    remoteViews.setImageViewBitmap(R.id.ImageView01, bitmap2);

		System.out.println(cpu0min);
		if(!curentfreq.equals("offline")){
			remoteViews.setTextViewText(R.id.freq0, curentfreq.substring(0, curentfreq.length()-3)+"Mhz");
		}
		else{
		remoteViews.setTextViewText(R.id.freq0, curentfreq);
		remoteViews.setTextColor(R.id.freq0, Color.RED);
		}
		
		if(!curentfreqcpu1.equals("offline")){
			remoteViews.setTextViewText(R.id.freq1, curentfreqcpu1.substring(0, curentfreqcpu1.length()-3)+"Mhz");
			remoteViews.setTextColor(R.id.freq1, Color.WHITE);
		}
		else{
			remoteViews.setTextViewText(R.id.freq1,curentfreqcpu1);
			remoteViews.setTextColor(R.id.freq1, Color.RED);
		}
		
		
		remoteViews.setTextViewText(R.id.textView9,	curentgovernorcpu0);
		
		remoteViews.setTextViewText(R.id.TextView02,curentgovernorcpu1);
		remoteViews.setTextColor(R.id.TextView02, Color.WHITE);
		if (curentgovernorcpu1.equals("offline")){
			remoteViews.setTextColor(R.id.TextView02, Color.RED);
		}
		try{
		if (Integer.parseInt(led)<2){
			remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.red);
		}
		else if (Integer.parseInt(led)<11 && Integer.parseInt(led)>=2){
			remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.yellow);
			
		}
		else if (Integer.parseInt(led)>=11){
			remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.green);
		}
		else {
			remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.err);
		}}
		catch(Exception e){
		
			remoteViews.setImageViewResource(R.id.imageView5,	R.drawable.err);
		
	}
		if (fastcharge.equals("1")){
			remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.green);
		}
		else if (fastcharge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.red);
		}
		else {
			remoteViews.setImageViewResource(R.id.imageView6,	R.drawable.err);
		}
		
		if (vsync.equals("1")){
			remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.green);
		}
		else if (vsync.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.red);
		}
		else {
			remoteViews.setImageViewResource(R.id.imageView4,	R.drawable.err);
		}
	
		if (cdepth.equals("16")){
			remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.red);
		}
		else if (cdepth.equals("24")){
			remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.yellow);
		}
		else if (cdepth.equals("32")){
			remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.green);
		}
		else {
			remoteViews.setImageViewResource(R.id.imageView3,	R.drawable.err);
		}

          remoteViews.setTextViewText(R.id.textView1k, kernel.trim());
	       remoteViews.setTextViewText(R.id.textView11, tmp);
		remoteViews.setTextViewText(R.id.textView13, temp);
		
		int battperciconint = 0;
		try{
				battperciconint = Integer.parseInt(battperc.substring(0, battperc.length()).trim());
				}
				catch(Exception e){
					battperciconint=0;
				}
		if (battperciconint <= 15 && battperciconint!=0 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_low);
			remoteViews.setTextColor(R.id.textView14, Color.RED);
		}
		else if(battperciconint > 30 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_full);
			remoteViews.setTextColor(R.id.textView14, Color.GREEN);

		}
		else if(battperciconint < 30 && battperciconint > 15 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_half);
			remoteViews.setTextColor(R.id.textView14, Color.YELLOW);
		}
		else if(charge.equals("1")){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_charge_usb);
			remoteViews.setTextColor(R.id.textView14, Color.CYAN);
		}
		else if(charge.equals("2")){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.battery_charge_ac);
			remoteViews.setTextColor(R.id.textView14, Color.CYAN);
		}
		else if(battperciconint==0){
			remoteViews.setImageViewResource(R.id.imageView10, R.drawable.err);
		}
		remoteViews.setProgressBar(R.id.progressBar1, 100, battperciconint, false );
		remoteViews.setTextViewText(R.id.textView14, battperc+"%");
		
		double battempint = Double.parseDouble(batttemp);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean tempPref = sharedPrefs.getBoolean("temp", false);
		if (tempPref==true){
			battempint = (battempint*0.18)+32;
			batttemp = String.valueOf((int)battempint);
			remoteViews.setTextViewText(R.id.textView20, batttemp+"°F");
			if(battempint>932){
				remoteViews.setTextColor(R.id.textView20, Color.RED);
			}
		}
		else if(tempPref==false){
		batttemp = String.valueOf(battempint/10);
		remoteViews.setTextViewText(R.id.textView20, batttemp+"°C");
		if(battempint>500){
			remoteViews.setTextColor(R.id.textView20, Color.RED);
		}
		}
		remoteViews.setTextViewText(R.id.textView21, battvol.trim()+"mV");
		remoteViews.setTextViewText(R.id.textView22, batttech);
		remoteViews.setTextViewText(R.id.textView23, battcurrent+"mA");
		if(battcurrent.substring(0, 1).equals("-"))
		{
			remoteViews.setTextColor(R.id.textView23, Color.RED);
		}
		else{
			remoteViews.setTextViewText(R.id.textView23, "+"+battcurrent+"mA");
			remoteViews.setTextColor(R.id.textView23, Color.GREEN);
		}
		remoteViews.setTextViewText(R.id.textView24, batthealth);
		remoteViews.setTextViewText(R.id.textView26, battcap+"mAh");
      // Register an onClickListener
      Intent clickIntent = new Intent(this.getApplicationContext(),
          AppWidgetBig.class);

      clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
          allWidgetIds);
		  
		 
 	 
	  String timer = sharedPrefs.getString("widget_time", "1800");
	  System.out.println(timer);
	  
		try{
				timeint = Integer.parseInt(timer.trim());
		}
		catch(Exception e){
			timeint = 1800;
		}
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.widget_layout2, pendingIntent);
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, timeint);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20*1000, pendingIntent);
	 
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    } 
    stopSelf();

    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  
} 
