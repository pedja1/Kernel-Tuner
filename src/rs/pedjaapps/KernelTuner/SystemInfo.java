package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.Intent;
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
	
	
	private Integer cpu0max;
	private Integer cpu1max;
	private Integer cpu0min;
	private Integer cpu1min;
	private Integer cpu2max;
	private Integer cpu3max;
	private Integer cpu2min;
	private Integer cpu3min;
	private Integer gpu2d;
	private Integer gpu3d;
	private String vsync;
	private String fastcharge;
	private String cdepth ;
	private String kernel;
	private String schedulers;
	private String scheduler;
	private String sdcache;;
	private String curentgovernorcpu0;
	private String curentgovernorcpu1;
	private String curentgovernorcpu2;
	private String curentgovernorcpu3;
	private String led;
	private String mpdec;
	private String s2w;
	private String cpu_info;
	


	private static String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	private static String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	private static String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	private static String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	private static String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	private static String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	private static String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	private static String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	private static String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	private static String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	private static String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	private static String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";


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

				cpu0min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu0max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
			
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

				cpu1min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu1max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu2min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu2max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu3min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				cpu3max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
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

				gpu3d = Integer.parseInt(aBuffer.trim());
				myReader.close();



			}
			catch (Exception e)
			{
				
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

				gpu2d = Integer.parseInt(aBuffer.trim());

				myReader.close();

			}
			catch (Exception e)
			{
				
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
			if (cpu0min!=null)
			{
				cpu0mintxt.setText(String.valueOf(cpu0min/1000)+"MHz");
				cpu0mintxt.setVisibility(View.VISIBLE);
				cpu0mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu0mintxt.setVisibility(View.GONE);
				cpu0mintxte.setVisibility(View.GONE);
			}
			
			TextView cpu0maxtxt = (TextView)findViewById(R.id.textView12);
			TextView cpu0maxtxte = (TextView)findViewById(R.id.textView3);
			if (cpu0max!=null)
			{
				cpu0maxtxt.setText(String.valueOf(cpu0max/1000)+"MHz");
				cpu0maxtxt.setVisibility(View.VISIBLE);
				cpu0maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu0maxtxt.setVisibility(View.GONE);
				cpu0maxtxte.setVisibility(View.GONE);
			}
			
			TextView cpu1mintxt = (TextView)findViewById(R.id.textView13);
			TextView cpu1mintxte = (TextView)findViewById(R.id.textView4);
			if (cpu1min!=null)
			{
				cpu1mintxt.setText(String.valueOf(cpu1min/1000)+"MHz");
				cpu1mintxt.setVisibility(View.VISIBLE);
				cpu1mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu1mintxt.setVisibility(View.GONE);
				cpu1mintxte.setVisibility(View.GONE);
			}
			
			TextView cpu1maxtxt = (TextView)findViewById(R.id.textView14);
			TextView cpu1maxtxte = (TextView)findViewById(R.id.textView5);
			if (cpu1max!=null)
			{
				cpu1maxtxt.setText(String.valueOf(cpu1max/1000)+"MHz");
				cpu1maxtxt.setVisibility(View.VISIBLE);
				cpu1maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu1maxtxt.setVisibility(View.GONE);
				cpu1maxtxte.setVisibility(View.GONE);
			}
			
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
			
			TextView cpu2mintxt = (TextView)findViewById(R.id.cpu2min);
			TextView cpu2mintxte = (TextView)findViewById(R.id.textView256);
			if (cpu2min!=null)
			{
				cpu2mintxt.setText(String.valueOf(cpu2min/1000)+"MHz");
				cpu2mintxt.setVisibility(View.VISIBLE);
				cpu2mintxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu2mintxt.setVisibility(View.GONE);
				cpu2mintxte.setVisibility(View.GONE);
			}
			
			TextView cpu2maxtxt = (TextView)findViewById(R.id.cpu2max);
			TextView cpu2maxtxte = (TextView)findViewById(R.id.textView300);
			if (cpu2max!=null)
			{
				cpu2maxtxt.setText(String.valueOf(cpu2max/1000)+"MHz");
				cpu2maxtxt.setVisibility(View.VISIBLE);
				cpu2maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu2maxtxt.setVisibility(View.GONE);
				cpu2maxtxte.setVisibility(View.GONE);
			}
			
			TextView cpu3mintxt = (TextView)findViewById(R.id.cpu3min);
			TextView cpu3mintxte = (TextView)findViewById(R.id.textView46);
			if (cpu3min!=null)
			{
				cpu3mintxt.setText(String.valueOf(cpu3min/1000)+"MHz");
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
			if (cpu3max!=null)
			{
				cpu3maxtxt.setText(String.valueOf(cpu3max/1000)+"MHz");
				cpu3maxtxt.setVisibility(View.VISIBLE);
				cpu3maxtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				cpu3maxtxt.setVisibility(View.GONE);
				cpu3maxtxte.setVisibility(View.GONE);
			}
			
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
			
			TextView ledlight = (TextView)findViewById(R.id.textView16);
			TextView ledlighte = (TextView)findViewById(R.id.textView7);

			try
			{

				ledlight.setText(Integer.parseInt(led) * 100 / 60 + "%");
				ledlight.setVisibility(View.VISIBLE);
				ledlighte.setVisibility(View.VISIBLE);

			}
			catch (Exception e)
			{
				
				ledlight.setVisibility(View.GONE);
				ledlighte.setVisibility(View.GONE);
				
			}
			System.out.println("13");
			TextView gpu2dtxt = (TextView)findViewById(R.id.textView17);
			TextView gpu2dtxte = (TextView)findViewById(R.id.textView8);
			if (gpu2d!=null)
			{
				gpu2dtxt.setText(String.valueOf(gpu2d/1000000)+"MHz");
				gpu2dtxt.setVisibility(View.VISIBLE);
				gpu2dtxte.setVisibility(View.VISIBLE);
			}
			else
			{
				gpu2dtxt.setVisibility(View.GONE);
				gpu2dtxte.setVisibility(View.GONE);
			}
			
			TextView gpu3dtxt = (TextView)findViewById(R.id.textView18);
			TextView gpu3dtxte = (TextView)findViewById(R.id.textView9);
			if (gpu3d!=null)
			{
				gpu3dtxt.setText(String.valueOf(gpu3d/1000000)+"MHz");
				gpu3dtxt.setVisibility(View.VISIBLE);
				gpu3dtxte.setVisibility(View.VISIBLE);
			}
			else
			{ 
				gpu3dtxt.setVisibility(View.GONE);
				gpu3dtxte.setVisibility(View.GONE);
			}
			
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
			
			TextView kinfo = (TextView)findViewById(R.id.textView26);
			kinfo.setText(kernel);
			
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
