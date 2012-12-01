package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SystemInfo extends SherlockActivity
{
	
	SharedPreferences sharedPrefs;
	public String cpu0freqs;
	public String cpu1freqs;
	public String cpu2freqs;
	public String cpu3freqs;
	public String cpu0max = "        ";
	public String cpu1max = "        ";
	public String cpu0min = "        ";
	public String cpu1min = "        ";
	public String cpu2max = "        ";
	public String cpu3max = "        ";
	public String cpu2min = "        ";
	public String cpu3min = "        ";
	public String gpu2d = "        ";
	public String gpu3d = "       ";
	public int countcpu0;
	public int countcpu1;
	public String vsync = " ";
	public String fastcharge = " ";
	public String out;
	public String cdepth ;
	public String kernel = "     ";
	public String remoteversion;
	public String schedulers;
	public String scheduler;
	public String sdcache;
	public String curentidlefreq;
	public String delay;
	public String pause;
	public String thrupload;
	public String thrupms;
	public String thrdownload;
	public String thrdownms;
	public boolean cpu1check;
	public String sdcacheinfo;
	public String ioschedulerinfo;
	List<String> list;
	public String ldt;
	public String freqcpu2;
	public String freqcpu3;

	public String p1low;
	public String p1high;
	public String p2low;
	public String p2high;
	public String p3low;
	public String p3high;
	public String p1freq;
	public String p2freq;
	public String p3freq;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
	public String curentgovernorcpu2;
	public String curentgovernorcpu3;
	public String led;
	public String mpdec;
	public String s2w;
	String cpu_info;
	public static String cpu0online = "/sys/devices/system/cpu/cpu0/online"; 
	public static String cpu1online = "/sys/devices/system/cpu/cpu1/online"; 
	public static String cpu2online = "/sys/devices/system/cpu/cpu2/online"; 
	public static String cpu3online = "/sys/devices/system/cpu/cpu3/online"; 


	public static String CPU0_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	public static String CPU1_FREQS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_frequencies";
	public static String CPU2_FREQS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_frequencies";
	public static String CPU3_FREQS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_frequencies";

	public static String CPU0_CURR_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	public static String CPU1_CURR_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
	public static String CPU2_CURR_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
	public static String CPU3_CURR_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";

	public static String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	public static String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	public static String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	public static String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	public static String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	public static String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	public static String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	public static String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	public static String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	public static String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";

	public static String CPU0_GOVS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governor";
	public static String CPU1_GOVS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_governor";
	public static String CPU2_GOVS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_governor";
	public static String CPU3_GOVS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_governor";

	private class info extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			try
			{

				File myFile = new File("/proc/cpuinfo");
				FileInputStream fIn = new FileInputStream(myFile);	
				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu_info = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu_info = "err";
			}

			try
			{

				File myFile = new File(CPU0_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);	
				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu0min = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu0min = "err";
			}


			try
			{

				File myFile = new File(CPU0_MAX_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);	
				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu0max = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu0max = "err";
			}

			try
			{

				File myFile = new File(CPU1_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu1min = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu1min = "err";
			}

			try
			{

				File myFile = new File(CPU1_MAX_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);		
				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu1max = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu1max = "err";
			}

			try
			{

				File myFile = new File(CPU0_CURR_GOV);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu0 = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				curentgovernorcpu0 = "err";
			}

			try
			{

    			File myFile = new File(CPU1_CURR_GOV);
    			FileInputStream fIn = new FileInputStream(myFile);

    			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null)
				{
    				aBuffer += aDataRow + "\n";
    			}

    			curentgovernorcpu1 = aBuffer.trim();
    			myReader.close();

    		}
			catch (Exception e)
			{
				curentgovernorcpu1 = "err";
    		}

			try
			{

				File myFile = new File(CPU2_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);	
				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu2min = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu2min = "err";
			}

			try
			{

				File myFile = new File(CPU2_MAX_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);	
				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu2max = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu2max = "err";
			}

			try
			{

				File myFile = new File(CPU3_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu3min = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu3min = "err";
			}

			try
			{

				File myFile = new File(CPU3_MAX_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);		
				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cpu3max = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				cpu3max = "err";
			}

			try
			{

    			File myFile = new File(CPU2_CURR_GOV);
    			FileInputStream fIn = new FileInputStream(myFile);

    			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null)
				{
    				aBuffer += aDataRow + "\n";
    			}

    			curentgovernorcpu2 = aBuffer.trim();
    			myReader.close();

    		}
			catch (Exception e)
			{
    			curentgovernorcpu2 = "err";
    		}

			try
			{

     			File myFile = new File(CPU3_CURR_GOV);
     			FileInputStream fIn = new FileInputStream(myFile);

     			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
     			String aDataRow = "";
     			String aBuffer = "";
     			while ((aDataRow = myReader.readLine()) != null)
				{
     				aBuffer += aDataRow + "\n";
     			}

     			curentgovernorcpu3 = aBuffer.trim();
     			myReader.close();

     		}
			catch (Exception e)
			{
				curentgovernorcpu3 = "err";
     		}

			try
			{
        		String aBuffer = "";
				File myFile = new File("/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				led = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				led = "err";
			}

			try
			{

				File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				gpu3d = aBuffer.trim();
				myReader.close();



			}
			catch (Exception e)
			{
				gpu3d = "err";

			}

			try
			{

				File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				gpu2d = aBuffer.trim();

				myReader.close();

			}
			catch (Exception e)
			{
				gpu2d = "err";
			}

			try
			{
        		String aBuffer = "";
				File myFile = new File("/sys/kernel/fast_charge/force_fast_charge");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				fastcharge = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				fastcharge = "err";
			}

			try
			{
        		String aBuffer = "";
				File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				vsync = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				vsync = "err";
			}



			try
			{

				File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				cdepth = aBuffer.trim();
				myReader.close();
				//Log.d("done",cdepth);

			}
			catch (IOException e)
			{
				cdepth = "err";
				;
			}

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

				File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				schedulers = aBuffer;
				myReader.close();

				scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
				scheduler.trim();
				schedulers = schedulers.replace("[", "");
				schedulers = schedulers.replace("]", "");

			}
			catch (Exception e)
			{
				schedulers = "err";
				scheduler = "err";
			}

			try
			{

				File myFile = new File("/sys/devices/virtual/bdi/179:0/read_ahead_kb");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				sdcache = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				sdcache = "err";

			}

			try
			{

				File myFile = new File("/sys/kernel/msm_mpdecision/conf/enabled");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				mpdec = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				mpdec = "err";

			}



			try
			{

				File myFile = new File("/sys/kernel/notification_leds/off_timer_multiplier");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				ldt = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{

				ldt = "266";
			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p1freq = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p2freq = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p3freq = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_freq");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p3freq = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_high");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p1high = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{


			}
			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_low");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p2low = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_high");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p2high = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_low");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p3low = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_high");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				p3high = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{


			}

			try
			{

				File myFile = new File(
					"/sys/android_touch/sweep2wake");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(new InputStreamReader(
																 fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				s2w = aBuffer.trim();

				myReader.close();

			}
			catch (Exception e)
			{

				try
				{

					File myFile = new File(
						"/sys/android_touch/sweep2wake/s2w_switch");
					FileInputStream fIn = new FileInputStream(myFile);

					BufferedReader myReader = new BufferedReader(new InputStreamReader(
																	 fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null)
					{
						aBuffer += aDataRow + "\n";
					}

					s2w = aBuffer.trim();

					myReader.close();

				}
				catch (Exception e2)
				{

					s2w = "err";
				}
			}





			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			// Pass the result data back to the main activity
			System.out.println("0");
			TextView cpu0mintxt = (TextView)findViewById(R.id.textView11);
			TextView cpu0mintxte = (TextView)findViewById(R.id.textView2);
			if (!cpu0min.equals("err"))
			{
				cpu0mintxt.setText(cpu0min.substring(0, cpu0min.length() - 3) + "Mhz");
				cpu0mintxt.setVisibility(View.VISIBLE);
				cpu0mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu0mintxt.setVisibility(View.GONE);
				cpu0mintxte.setVisibility(View.GONE);
			}
			System.out.println("1");
			TextView cpu0maxtxt = (TextView)findViewById(R.id.textView12);
			TextView cpu0maxtxte = (TextView)findViewById(R.id.textView3);
			if (!cpu0max.equals("err"))
			{
				cpu0maxtxt.setText(cpu0max.substring(0, cpu0max.length() - 3) + "Mhz");
				cpu0maxtxt.setVisibility(View.VISIBLE);
				cpu0maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu0maxtxt.setVisibility(View.GONE);
				cpu0maxtxte.setVisibility(View.GONE);
			}
			System.out.println("2");
			TextView cpu1mintxt = (TextView)findViewById(R.id.textView13);
			TextView cpu1mintxte = (TextView)findViewById(R.id.textView4);
			if (!cpu1min.equals("err"))
			{
				cpu1mintxt.setText(cpu1min.substring(0, cpu1min.length() - 3) + "Mhz");
				cpu1mintxt.setVisibility(View.VISIBLE);
				cpu1mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu1mintxt.setVisibility(View.GONE);
				cpu1mintxte.setVisibility(View.GONE);
			}
			System.out.println("3");
			TextView cpu1maxtxt = (TextView)findViewById(R.id.textView14);
			TextView cpu1maxtxte = (TextView)findViewById(R.id.textView5);
			if (!cpu1max.equals("err"))
			{
				cpu1maxtxt.setText(cpu1max.substring(0, cpu1max.length() - 3) + "Mhz");
				cpu1maxtxt.setVisibility(View.VISIBLE);
				cpu1maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu1maxtxt.setVisibility(View.GONE);
				cpu1maxtxte.setVisibility(View.GONE);
			}
			System.out.println("4");
			TextView cpu0gov = (TextView)findViewById(R.id.textView15);
			TextView cpu0gove = (TextView)findViewById(R.id.textView20);

			if (!curentgovernorcpu0.equals("err"))
			{
				cpu0gov.setText(curentgovernorcpu0);
				cpu0gov.setVisibility(View.VISIBLE);
				cpu0gove.setVisibility(View.VISIBLE);
        	}
        	else
			{
        		cpu0gov.setVisibility(View.GONE);
        		cpu0gove.setVisibility(View.GONE);
        	}
			System.out.println("5");
			TextView cpu1gov = (TextView)findViewById(R.id.textView23);
			TextView cpu1gove = (TextView)findViewById(R.id.textView21);
			if (!curentgovernorcpu1.equals("err"))
			{
				cpu1gov.setText(curentgovernorcpu1);
				cpu1gov.setVisibility(View.VISIBLE);
				cpu1gove.setVisibility(View.VISIBLE);
        	}
        	else
			{
        		cpu1gov.setVisibility(View.GONE);
        		cpu1gove.setVisibility(View.GONE);
        	}
			System.out.println("6");
			TextView cpu2mintxt = (TextView)findViewById(R.id.cpu2min);
			TextView cpu2mintxte = (TextView)findViewById(R.id.textView256);
			if (!cpu2min.equals("err"))
			{
				cpu2mintxt.setText(cpu2min.substring(0, cpu2min.length() - 3) + "Mhz");
				cpu2mintxt.setVisibility(View.VISIBLE);
				cpu2mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu2mintxt.setVisibility(View.GONE);
				cpu2mintxte.setVisibility(View.GONE);
			}
			System.out.println("7");
			TextView cpu2maxtxt = (TextView)findViewById(R.id.cpu2max);
			TextView cpu2maxtxte = (TextView)findViewById(R.id.textView300);
			if (!cpu2max.equals("err"))
			{
				cpu2maxtxt.setText(cpu2max.substring(0, cpu2max.length() - 3) + "Mhz");
				cpu2maxtxt.setVisibility(View.VISIBLE);
				cpu2maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu2maxtxt.setVisibility(View.GONE);
				cpu2maxtxte.setVisibility(View.GONE);
			}
			System.out.println("8");
			TextView cpu3mintxt = (TextView)findViewById(R.id.cpu3min);
			TextView cpu3mintxte = (TextView)findViewById(R.id.textView46);
			if (!cpu3min.equals("err"))
			{
				cpu3mintxt.setText(cpu3min.substring(0, cpu3min.length() - 3) + "Mhz");
				cpu3mintxt.setVisibility(View.VISIBLE);
				cpu3mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu3mintxt.setVisibility(View.GONE);
				cpu3mintxte.setVisibility(View.GONE);
			}
			System.out.println("9");
			TextView cpu3maxtxt = (TextView)findViewById(R.id.cpu3max);
			TextView cpu3maxtxte = (TextView)findViewById(R.id.textView56);
			if (!cpu3max.equals("err"))
			{
				cpu3maxtxt.setText(cpu3max.substring(0, cpu3max.length() - 3) + "Mhz");
				cpu3maxtxt.setVisibility(View.VISIBLE);
				cpu3maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu3maxtxt.setVisibility(View.GONE);
				cpu3maxtxte.setVisibility(View.GONE);
			}
			System.out.println("10");
			TextView cpu2gov = (TextView)findViewById(R.id.cpu2gov);
			TextView cpu2gove = (TextView)findViewById(R.id.textView201);

			if (!curentgovernorcpu2.equals("err"))
			{
				cpu2gov.setText(curentgovernorcpu2);
				cpu2gov.setVisibility(View.VISIBLE);
				cpu2gove.setVisibility(View.VISIBLE);
        	}
        	else
			{
        		cpu2gov.setVisibility(View.GONE);
        		cpu2gove.setVisibility(View.GONE);
        	}
			System.out.println("11");
			TextView cpu3gov = (TextView)findViewById(R.id.cpu3gov);
			TextView cpu3gove = (TextView)findViewById(R.id.textView213);
			if (!curentgovernorcpu3.equals("err"))
			{
				cpu3gov.setText(curentgovernorcpu3);
				cpu3gov.setVisibility(View.VISIBLE);
				cpu3gove.setVisibility(View.VISIBLE);
        	}
        	else
			{
        		cpu3gov.setVisibility(View.GONE);
        		cpu3gove.setVisibility(View.GONE);
        	}
			System.out.println("12");
			TextView ledlight = (TextView)findViewById(R.id.textView16);
			TextView ledlighte = (TextView)findViewById(R.id.textView7);

			try
			{

				ledlight.setText(Integer.parseInt(led) * 100 / 60 + "%");
				//ledlight.setTextColor(Color.RED);
				ledlight.setVisibility(View.VISIBLE);
				ledlighte.setVisibility(View.VISIBLE);

			}
			catch (Exception e)
			{
				//	else if(led.equals("UNSUPPORTED")){
				ledlight.setVisibility(View.GONE);
				ledlighte.setVisibility(View.GONE);
				//	}
			}
			System.out.println("13");
			TextView gpu2dtxt = (TextView)findViewById(R.id.textView17);
			TextView gpu2dtxte = (TextView)findViewById(R.id.textView8);
			if (!gpu2d.equals("err"))
			{
				gpu2dtxt.setText(gpu2d.substring(0, gpu2d.length() - 6) + "Mhz");
				gpu2dtxt.setVisibility(View.VISIBLE);
				gpu2dtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				gpu2dtxt.setVisibility(View.GONE);
				gpu2dtxte.setVisibility(View.GONE);
			}
			System.out.println("14");
			TextView gpu3dtxt = (TextView)findViewById(R.id.textView18);
			TextView gpu3dtxte = (TextView)findViewById(R.id.textView9);
			if (!gpu3d.equals("err"))
			{
				gpu3dtxt.setText(gpu3d.substring(0, gpu3d.length() - 6) + "Mhz");
				gpu3dtxt.setVisibility(View.VISIBLE);
				gpu3dtxte.setVisibility(View.VISIBLE);
			}
			else
			{ 
				gpu3dtxt.setVisibility(View.GONE);
				gpu3dtxte.setVisibility(View.GONE);
			}
			System.out.println("15");
			TextView fastchargetxt = (TextView)findViewById(R.id.textView22);
			TextView fastchargetxte = (TextView)findViewById(R.id.textView6);
			if (fastcharge.equals("1"))
			{
				fastchargetxt.setText("ON");
				fastchargetxt.setTextColor(Color.GREEN);
				fastchargetxt.setVisibility(View.VISIBLE);
				fastchargetxte.setVisibility(View.VISIBLE);
			}
			else if (fastcharge.equals("0"))
			{
				fastchargetxt.setText("OFF");
				fastchargetxt.setTextColor(Color.RED);
				fastchargetxt.setVisibility(View.VISIBLE);
				fastchargetxte.setVisibility(View.VISIBLE);
			}
			else
			{
				fastchargetxt.setVisibility(View.GONE);
				fastchargetxte.setVisibility(View.GONE);
			}
			System.out.println("16");
			TextView vsynctxt = (TextView)findViewById(R.id.textView19);
			TextView vsynctxte = (TextView)findViewById(R.id.textView10);
			if (vsync.equals("1"))
			{
				vsynctxt.setText("ON");
				vsynctxt.setTextColor(Color.GREEN);
				vsynctxt.setVisibility(View.VISIBLE);
				vsynctxte.setVisibility(View.VISIBLE);
			}
			else if (vsync.equals("0"))
			{
				vsynctxt.setText("OFF");
				vsynctxt.setTextColor(Color.RED);
				vsynctxt.setVisibility(View.VISIBLE);
				vsynctxte.setVisibility(View.VISIBLE);
			}
			else
			{
				vsynctxt.setVisibility(View.GONE);
				vsynctxte.setVisibility(View.GONE);
			}
			System.out.println("17");
			TextView cdepthtxt = (TextView)findViewById(R.id.textView25);
			TextView cdepthtxte = (TextView)findViewById(R.id.textView24);
			if (cdepth.equals("16"))
			{
				cdepthtxt.setText("16");
				cdepthtxt.setTextColor(Color.RED);
				cdepthtxt.setVisibility(View.VISIBLE);
				cdepthtxte.setVisibility(View.VISIBLE);
			}
			else if (cdepth.equals("24"))
			{
				cdepthtxt.setText("24");
				cdepthtxt.setTextColor(Color.YELLOW);
				cdepthtxt.setVisibility(View.VISIBLE);
				cdepthtxte.setVisibility(View.VISIBLE);
			}
			else if (cdepth.equals("32"))
			{
				cdepthtxt.setText("32");
				cdepthtxt.setTextColor(Color.GREEN);
				cdepthtxt.setVisibility(View.VISIBLE);
				cdepthtxte.setVisibility(View.VISIBLE);
			}
			else// if(cdepth==null) {
			{	cdepthtxt.setVisibility(View.GONE);
				cdepthtxte.setVisibility(View.GONE);
			}
			System.out.println("18");
			TextView kinfo = (TextView)findViewById(R.id.textView26);
			kinfo.setText(kernel);
			System.out.println("19");
			TextView sdcachetxt = (TextView)findViewById(R.id.textView34);
			TextView sdcachetxte = (TextView)findViewById(R.id.textView33);
			TextView ioschedulertxt = (TextView)findViewById(R.id.textView32);
			TextView ioschedulertxte = (TextView)findViewById(R.id.textView29);
			if (sdcache.equals("err"))
			{
				sdcachetxt.setVisibility(View.GONE);
				sdcachetxte.setVisibility(View.GONE);
			}
			else
			{
				sdcachetxt.setText(sdcache);
				sdcachetxt.setVisibility(View.VISIBLE);
				sdcachetxte.setVisibility(View.VISIBLE);
			}
			System.out.println("20");
			if (scheduler.equals("err"))
			{
				ioschedulertxt.setVisibility(View.GONE);
				ioschedulertxte.setVisibility(View.GONE);
			}
			else
			{
				ioschedulertxt.setText(scheduler);
				ioschedulertxt.setVisibility(View.VISIBLE);
	    		ioschedulertxte.setVisibility(View.VISIBLE);
			}
			System.out.println("21");
			TextView s2wtxt = (TextView)findViewById(R.id.textView36);
			TextView s2wtxte = (TextView)findViewById(R.id.textView35);
			if (s2w.equals("1"))
			{
				s2wtxt.setText("ON with no backlight");

				s2wtxt.setVisibility(View.VISIBLE);
				s2wtxte.setVisibility(View.VISIBLE);
			}
			else if (s2w.equals("2"))
			{
				s2wtxt.setText("ON with backlight");

				s2wtxt.setVisibility(View.VISIBLE);
				s2wtxte.setVisibility(View.VISIBLE);
			}
			else if (s2w.equals("0"))
			{
				s2wtxt.setText("OFF");
				s2wtxt.setTextColor(Color.RED);
				s2wtxt.setVisibility(View.VISIBLE);
				s2wtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				s2wtxt.setVisibility(View.GONE);
				s2wtxte.setVisibility(View.GONE);
			}
			System.out.println("22");
			TextView mpdectxt = (TextView)findViewById(R.id.mpdecValue);
			TextView mpdectxte = (TextView)findViewById(R.id.mpdecText);
			if (mpdec.equals("0"))
			{
				mpdectxt.setText("OFF");
				mpdectxt.setVisibility(View.VISIBLE);
				mpdectxte.setVisibility(View.VISIBLE);
			}
			else if (mpdec.equals("1"))
			{
				mpdectxt.setText("ON");
				mpdectxt.setVisibility(View.VISIBLE);
				mpdectxte.setVisibility(View.VISIBLE);
			}
			else
			{
				mpdectxt.setVisibility(View.GONE);
				mpdectxte.setVisibility(View.GONE);
			}

			TextView cpuinfo = (TextView)findViewById(R.id.cpu_i);
			cpuinfo.setText(cpu_info);
			TextView board = (TextView)findViewById(R.id.board);
			TextView device = (TextView)findViewById(R.id.deviceTxt);
			TextView display = (TextView)findViewById(R.id.display);
			TextView bootloader = (TextView)findViewById(R.id.bootloader);
			TextView brand = (TextView)findViewById(R.id.brand);
			TextView hardware = (TextView)findViewById(R.id.hardware);
			TextView manufacturer = (TextView)findViewById(R.id.manufacturer);
			TextView model = (TextView)findViewById(R.id.model);
			TextView product = (TextView)findViewById(R.id.product);
			TextView radio = (TextView)findViewById(R.id.radio);
			board.setText(android.os.Build.BOARD);
			device.setText(android.os.Build.DEVICE);
			display.setText(android.os.Build.DISPLAY);
			bootloader.setText(android.os.Build.BOOTLOADER);
			brand.setText(android.os.Build.BRAND);
			hardware.setText(android.os.Build.HARDWARE);
			manufacturer.setText(android.os.Build.MANUFACTURER);
			model.setText(android.os.Build.MODEL);
			product.setText(android.os.Build.PRODUCT);
			if (android.os.Build.VERSION.SDK_INT > 10)
			{
				if (android.os.Build.getRadioVersion() != null)
				{
					radio.setText(android.os.Build.getRadioVersion());
				}
			}
		}


	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		new info().execute();
		RelativeLayout cpu = (RelativeLayout)findViewById(R.id.cpu);
		final RelativeLayout cpuInfo = (RelativeLayout)findViewById(R.id.cpu_info);
		final ImageView cpuImg = (ImageView)findViewById(R.id.cpu_img);

		RelativeLayout other = (RelativeLayout)findViewById(R.id.other);
		final RelativeLayout otherInfo = (RelativeLayout)findViewById(R.id.other_info);
		final ImageView otherImg = (ImageView)findViewById(R.id.other_img);

		RelativeLayout kernel = (RelativeLayout)findViewById(R.id.kernel);
		final RelativeLayout kernelInfo = (RelativeLayout)findViewById(R.id.kernel_info);
		final ImageView kernelImg = (ImageView)findViewById(R.id.kernel_img);

		RelativeLayout device = (RelativeLayout)findViewById(R.id.device);
		final RelativeLayout deviceInfo = (RelativeLayout)findViewById(R.id.device_info);
		final ImageView deviceImg = (ImageView)findViewById(R.id.device_img);


		cpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					if (cpuInfo.getVisibility() == View.VISIBLE)
					{
						cpuInfo.setVisibility(View.GONE);
						cpuImg.setImageResource(R.drawable.arrow_right);
					}
					else if (cpuInfo.getVisibility() == View.GONE)
					{
						cpuInfo.setVisibility(View.VISIBLE);
						cpuImg.setImageResource(R.drawable.arrow_down);
					}
				}

			});

		device.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					if (deviceInfo.getVisibility() == View.VISIBLE)
					{
						deviceInfo.setVisibility(View.GONE);
						deviceImg.setImageResource(R.drawable.arrow_right);
					}
					else if (deviceInfo.getVisibility() == View.GONE)
					{
						deviceInfo.setVisibility(View.VISIBLE);
						deviceImg.setImageResource(R.drawable.arrow_down);
					}
				}

			});

		kernel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					if (kernelInfo.getVisibility() == View.VISIBLE)
					{
						kernelInfo.setVisibility(View.GONE);
						kernelImg.setImageResource(R.drawable.arrow_right);
					}
					else if (kernelInfo.getVisibility() == View.GONE)
					{
						kernelInfo.setVisibility(View.VISIBLE);
						kernelImg.setImageResource(R.drawable.arrow_down);
					}
				}

			});

		other.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					if (otherInfo.getVisibility() == View.VISIBLE)
					{
						otherInfo.setVisibility(View.GONE);
						otherImg.setImageResource(R.drawable.arrow_right);
					}
					else if (otherInfo.getVisibility() == View.GONE)
					{
						otherInfo.setVisibility(View.VISIBLE);
						otherImg.setImageResource(R.drawable.arrow_down);
					}
				}

			});
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
}
