package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rs.pedjaapps.KernelTuner.SDScannerActivity.MyComparator;

import android.os.SystemClock;
import android.util.Log;

public class CPUInfo
{

	public static String cpu0online = "/sys/devices/system/cpu/cpu0/online"; 
	public static String cpu1online = "/sys/devices/system/cpu/cpu1/online"; 
	public static String cpu2online = "/sys/devices/system/cpu/cpu2/online"; 
	public static String cpu3online = "/sys/devices/system/cpu/cpu3/online"; 


	public static String CPU0_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	public static String SWAPS = "/proc/swaps";
	
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

	public static String CPU0_GOVS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
	public static String CPU1_GOVS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_governors";
	public static String CPU2_GOVS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_governors";
	public static String CPU3_GOVS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_governors";
	public static String TIMES_IN_STATE_CPU0 = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU1 = "/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU2 = "/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU3 = "/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state";

	public static String VOLTAGE_PATH = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
	public static String VOLTAGE_PATH_TEGRA_3 = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	public static String GPU = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk";
	public static String CDEPTH = "/sys/kernel/debug/msm_fb/0/bpp";
	public static String S2W = "/sys/android_touch/sweep2wake";
	public static String S2W_ALT = "/sys/android_touch/sweep2wake/s2w_switch";
	public static String MPDECISION = "/sys/kernel/msm_mpdecision/conf/enabled";
	public static String BUTTONS_LIGHT = "/sys/devices/platform/leds-pm8058/leds/button-backlight/currents";
	public static String BUTTONS_LIGHT_2 = "/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents";
	public static String SD_CACHE = "/sys/devices/virtual/bdi/179:0/read_ahead_kb";
	public static String VSYNC = "/sys/kernel/debug/msm_fb/0/vsync_enable";
	public static String FCHARGE = "/sys/kernel/fast_charge/force_fast_charge";
	public static String OOM = "/sys/module/lowmemorykiller/parameters/minfree";
	public static String THERMALD = "/sys/kernel/msm_thermal/conf/allowed_low_freq";
	public static String SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
	public static String OTG = "/sys/kernel/debug/msm_otg/mode";
	public static String OTG_2= "/sys/kernel/debug/otg/mode";
	public static boolean freqsExists()
	{
		boolean i = false;
		if (new File(CPU0_FREQS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean oomExists()
	{
		boolean i = false;
		if (new File(OOM).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean thermaldExists()
	{
		boolean i = false;
		if (new File(THERMALD).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean swapsExists()
	{
		boolean i = false;
		if (new File(SWAPS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean cpu0Online()
	{
		boolean i = false;
		if (new File(cpu0online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu1Online()
	{
		boolean i = false;
		if (new File(cpu1online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu2Online()
	{
		boolean i = false;
		if (new File(cpu2online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu3Online()
	{
		boolean i = false;
		if (new File(cpu3online).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean gpuExists()
	{
		boolean i = false;
		if (new File(GPU).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean cdExists()
	{
		boolean i = false;
		if (new File(CDEPTH).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean voltageExists()
	{
		boolean i = false;
		if (new File(VOLTAGE_PATH).exists())
		{
			i = true;
		}
		else if (new File(VOLTAGE_PATH_TEGRA_3).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean otgExists()
	{
		boolean i = false;
		if (new File(OTG).exists())
		{
			i = true;
		}
		else if (new File(OTG_2).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean s2wExists()
	{
		boolean i = false;
		if (new File(S2W).exists())
		{
			i = true;
		}
		else if (new File(S2W_ALT).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean TISExists()
	{
		boolean i = false;
		if (new File(TIMES_IN_STATE_CPU0).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean mpdecisionExists()
	{
		boolean i = false;
		if (new File(MPDECISION).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean buttonsExists()
	{
		boolean i = false;
		if (new File(BUTTONS_LIGHT).exists())
		{
			i = true;
		}
		else if (new File(BUTTONS_LIGHT_2).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean sdcacheExists()
	{
		boolean i = false;
		if (new File(SD_CACHE).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean vsyncExists()
	{
		boolean i = false;
		if (new File(VSYNC).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean fchargeExists()
	{
		boolean i = false;
		if (new File(FCHARGE).exists())
		{
			i = true;
		}
		return i;

	}

	public static List<FreqsEntry> frequencies()
	{
		
		List<FreqsEntry> entries = new ArrayList<FreqsEntry>();
		List<String> frequencies = new ArrayList<String>();
		try
		{

			File myFile = new File(CPU0_FREQS);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			frequencies = Arrays.asList(aBuffer.split("\\s"));
			for(String s: frequencies){
				entries.add(new FreqsEntry(s.trim().substring(0, s.trim().length()-3)+"MHz", Integer.parseInt(s.trim())));
 				
			}
			myReader.close();

		}
		catch (Exception e)
		{
			try
			{

	 			FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);

	 			DataInputStream in = new DataInputStream(fstream);
	 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 			String strLine;


	 			while ((strLine = br.readLine()) != null)
				{

	 				String[] delims = strLine.split(" ");
	 				String freq = delims[0];
	 				//frequencies.add(freq);
	 				entries.add(new FreqsEntry(freq.trim().substring(0, freq.trim().length()-3)+"MHz", Integer.parseInt(freq.trim())));
	 				

	 			}

	 			
	 				Collections.sort(entries,new MyComparator());
	 			


	 			in.close();
			}
			catch (Exception ee)
			{
				entries.add(new FreqsEntry("", 0));
 				
			}
		}
		return entries;

	}
	
	public static List<String> oom()
	{
		List<String> oom = new ArrayList<String>();


		try
		{

			File myFile = new File(OOM);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{

				
				aBuffer += aDataRow + "\n";
			}
			oom = Arrays.asList(aBuffer.split(","));
			myReader.close();

		}
		catch (Exception e)
		{
		
		
		}
		return oom;

	}
	
	public static String leds()
	{
		String leds = "";


		try
		{

			File myFile = new File(BUTTONS_LIGHT);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			leds = aBuffer.trim();

			myReader.close();

		}
		catch (Exception e)
		{
			try
			{

				File myFile = new File(BUTTONS_LIGHT_2);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}
				leds = aBuffer.trim();

				myReader.close();
			}
			catch (Exception ee)
			{

			}
		}
		return leds;

	}

	public static List<String> governors()
	{
		List<String> governors = new ArrayList<String>();


		try
		{

			File myFile = new File(CPU0_GOVS);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			governors = Arrays.asList(aBuffer.split("\\s"));

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return governors;

	}

	public static String cpu0MinFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU0_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu0MaxFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU0_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu1MinFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU1_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu1MaxFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU1_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu2MinFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU2_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu2MaxFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU2_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu3MinFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU3_MIN_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu3MaxFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU3_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{
		}
		return aBuffer.trim();

	}

	public static String cpu0CurFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU0_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu1CurFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU1_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu2CurFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU2_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu3CurFreq()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU3_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu0CurGov()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU0_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu1CurGov()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU1_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu2CurGov()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU2_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}

	public static String cpu3CurGov()
	{
		String aBuffer = "offline";
		try
		{

			File myFile = new File(CPU3_CURR_GOV);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			myReader.close();

		}
		catch (Exception e)
		{

		}
		return aBuffer.trim();

	}


	public static  List<TimesEntry> getTis()
	{
		List<TimesEntry> times = new ArrayList<TimesEntry>();

		try
		{

			FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{	
				String[] delims = strLine.split(" ");
				times.add(new TimesEntry(Integer.parseInt(delims[0]), Long.parseLong(delims[1])));
				System.out.println(strLine);
			}

			in.close();
		}
		catch (Exception e)
		{
			Log.e("Error: " , e.getMessage());
		}
		
		return times;

	}

	

	public static List<Integer> voltages()
	{
		List<Integer> voltages = new ArrayList<Integer>();

		try
		{

			FileInputStream fstream = new FileInputStream(VOLTAGE_PATH);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{

				voltages.add(Integer.parseInt(strLine.substring(9,
																strLine.length() - 0).trim()));
			}

			in.close();
		}
		catch (Exception e)
		{
			try
			{

				FileInputStream fstream = new FileInputStream(VOLTAGE_PATH_TEGRA_3);

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null)
				{	
					String[] delims = strLine.split(" ");
					voltages.add(Integer.parseInt(delims[1]));
				}

				in.close();
			}
			catch (Exception ex)
			{

			}
		}
		System.out.println(voltages);
		return voltages;

	}

	public static List<String> voltageFreqs()
	{
		List<String> voltageFreqs = new ArrayList<String>();

		try
		{

			FileInputStream fstream = new FileInputStream(VOLTAGE_PATH);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{

				voltageFreqs.add(strLine.substring(0,
												   strLine.length() - 10).trim());
			}

			in.close();
		}
		catch (Exception e)
		{

			try
			{

				FileInputStream fstream = new FileInputStream(VOLTAGE_PATH_TEGRA_3);

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null)
				{	
					String[] delims = strLine.split(" ");
					voltageFreqs.add(delims[0].substring(0, delims[0].length() - 4));
				}

				in.close();
			}
			catch (Exception ex)
			{

			}

		}
		System.out.println(voltageFreqs);
		return voltageFreqs;

	}


	public static List<Integer> allVoltages()
	{
		List<Integer> allVoltages = new ArrayList<Integer>();

		for (int i = 700000; i < 1412500; i += 12500)
		{
			allVoltages.add(i);
		}
		return allVoltages;

	}

	public static List<Integer> allVoltagesTegra3()
	{
		List<Integer> allVoltages = new ArrayList<Integer>();

		for (int i = 700000; i < 1412500; i += 12500)
		{
			allVoltages.add(i / 1000);
		}
		return allVoltages;

	}

	public static String uptime()
	{
		String uptime;

		int time =(int) SystemClock.elapsedRealtime();


		String s = String.valueOf((int)((time / 1000) % 60));
		String m = String.valueOf((int)((time / (1000 * 60)) % 60));
		String h = String.valueOf((int)((time / (1000 * 3600)) % 24));
		String d = String.valueOf((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		uptime = builder.toString();

		return uptime;


	}
	public static String deepSleep()
	{
		String deepSleep;

		int time =(int) (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());

		String s = String.valueOf((int)((time / 1000) % 60));
	    String m = String.valueOf((int)((time / (1000 * 60)) % 60));
		String h = String.valueOf((int)((time / (1000 * 3600)) % 24));
		String d = String.valueOf((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		deepSleep = builder.toString();

		return deepSleep;
	}

	public static String cpuTemp()
	{
		String cpuTemp = "0";
		try
		{

			File myFile = new File(
				"/sys/class/thermal/thermal_zone1/temp");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			cpuTemp = aBuffer.trim();

			myReader.close();

		}
		catch (Exception e2)
		{
			cpuTemp = "0";
		}

		return cpuTemp;
	}

	public static String cpuInfo()
	{
		String cpuInfo = "";
		try
		{

			File myFile = new File("/proc/cpuinfo");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			cpuInfo = aBuffer.trim();

			myReader.close();

		}
		catch (Exception e2)
		{
		}
		return cpuInfo;
	}

	public static List<String> availableGovs()
	{
		File govs = new File("/sys/devices/system/cpu/cpufreq/");
		List<String> availableGovs = new ArrayList<String>();


		if (govs.exists())
		{
			File[] files = govs.listFiles();

			for (File file : files)
			{
				availableGovs.add(file.getName());


			}
		}

		availableGovs.removeAll(Arrays.asList("vdd_table"));
		return availableGovs;

	}

	public static List<String> govSettings()
	{

		List<String> govSettings = new ArrayList<String>();

		for (String s : availableGovs())
		{
			File gov = new File("/sys/devices/system/cpu/cpufreq/" + s + "/");

			if (gov.exists())
			{
				File[] files = gov.listFiles();
				if(files!=null){
				for (File file : files)
				{

					govSettings.add(file.getName());

				}
				}
			}}
		return govSettings;
		}

	public static String voltDebug(){
		String voltage = "";
		String div = "####################";
		List<String> paths = new ArrayList<String>();
		/*0*/paths.add("/sys/devices/system/cpu/cpufreq/frequency_voltage_table");
		/*1*/paths.add("/sys/devices/system/cpu/cpu0/cpufreq/frequency_voltage_table");
		/*2*/paths.add("/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_freq_voltage_table");
		/*3*/paths.add("/sys/devices/system/cpu/cpu0/cpufreq/vdd_levels_havs");
		/*4*/paths.add("/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");
		/*5*/paths.add("/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
		/*6*/paths.add("/sys/devices/system/cpu/cpu0/cpufreq/vdd_levels");
		/*7*/paths.add("/sys/class/misc/customvoltage/UV_mV_table");
		/*8*/paths.add("/sys/class/misc/customvoltage/vdd_levels");
		/*9*/paths.add("/sys/devices/system/cpu/cpufreq/UV_mV_table");
		
		StringBuilder builder = new StringBuilder();
		try
		{
			File myFile = new File(paths.get(0));
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			builder.append(div+"\n\n");
			builder.append("VOLTAGE INFORMATION\n\n");
			builder.append(paths.get(0)+"\n\n");
			builder.append(aBuffer+"\n\n");
			builder.append(div+"\n\n");
			
			voltage = builder.toString();
			myReader.close();

		}
		catch (Exception e)
		{

			try
			{
				File myFile = new File(paths.get(1));
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				builder.append(div+"\n\n");
				builder.append("VOLTAGE INFORMATION\n\n");
				builder.append(paths.get(1)+"\n\n");
				builder.append(aBuffer+"\n\n");
				builder.append(div+"\n\n");
				
				voltage = builder.toString();
				myReader.close();

			}
			catch (Exception e1)
			{
				try
				{
					File myFile = new File(paths.get(2));
					FileInputStream fIn = new FileInputStream(myFile);
					BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null)
					{
						aBuffer += aDataRow + "\n";
					}

					builder.append(div+"\n\n");
					builder.append("VOLTAGE INFORMATION\n\n");
					builder.append(paths.get(2)+"\n\n");
					builder.append(aBuffer+"\n\n");
					builder.append(div+"\n\n");
					
					voltage = builder.toString();
					myReader.close();

				}
				catch (Exception e2)
				{
					try
					{
						File myFile = new File(paths.get(3));
						FileInputStream fIn = new FileInputStream(myFile);
						BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
						String aDataRow = "";
						String aBuffer = "";
						while ((aDataRow = myReader.readLine()) != null)
						{
							aBuffer += aDataRow + "\n";
						}

						builder.append(div+"\n\n");
						builder.append("VOLTAGE INFORMATION\n\n");
						builder.append(paths.get(3)+"\n\n");
						builder.append(aBuffer+"\n\n");
						builder.append(div+"\n\n");
						
						voltage = builder.toString();
						myReader.close();

					}
					catch (Exception e3)
					{
						try
						{
							File myFile = new File(paths.get(4));
							FileInputStream fIn = new FileInputStream(myFile);
							BufferedReader myReader = new BufferedReader(
								new InputStreamReader(fIn));
							String aDataRow = "";
							String aBuffer = "";
							while ((aDataRow = myReader.readLine()) != null)
							{
								aBuffer += aDataRow + "\n";
							}

							builder.append(div+"\n\n");
							builder.append("VOLTAGE INFORMATION\n\n");
							builder.append(paths.get(4)+"\n\n");
							builder.append(aBuffer+"\n\n");
							builder.append(div+"\n\n");
							
							voltage = builder.toString();
							myReader.close();

						}
						catch (Exception e4)
						{
							try
							{
								File myFile = new File(paths.get(5));
								FileInputStream fIn = new FileInputStream(myFile);
								BufferedReader myReader = new BufferedReader(
									new InputStreamReader(fIn));
								String aDataRow = "";
								String aBuffer = "";
								while ((aDataRow = myReader.readLine()) != null)
								{
									aBuffer += aDataRow + "\n";
								}

								builder.append(div+"\n\n");
								builder.append("VOLTAGE INFORMATION\n\n");
								builder.append(paths.get(5)+"\n\n");
								builder.append(aBuffer+"\n\n");
								builder.append(div+"\n\n");
								
								voltage = builder.toString();
								myReader.close();

							}
							catch (Exception e5)
							{
								try
								{
									File myFile = new File(paths.get(6));
									FileInputStream fIn = new FileInputStream(myFile);
									BufferedReader myReader = new BufferedReader(
										new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null)
									{
										aBuffer += aDataRow + "\n";
									}

									builder.append(div+"\n\n");
									builder.append("VOLTAGE INFORMATION\n\n");
									builder.append(paths.get(6)+"\n\n");
									builder.append(aBuffer+"\n\n");
									builder.append(div+"\n\n");
									
									voltage = builder.toString();
									myReader.close();

								}
								catch (Exception e6)
								{
									try
									{
										File myFile = new File(paths.get(7));
										FileInputStream fIn = new FileInputStream(myFile);
										BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
										String aDataRow = "";
										String aBuffer = "";
										while ((aDataRow = myReader.readLine()) != null)
										{
											aBuffer += aDataRow + "\n";
										}

										builder.append(div+"\n\n");
										builder.append("VOLTAGE INFORMATION\n\n");
										builder.append(paths.get(7)+"\n\n");
										builder.append(aBuffer+"\n\n");
										builder.append(div+"\n\n");
										
										voltage = builder.toString();
										myReader.close();

									}
									catch (Exception e7)
									{
										try
										{
											File myFile = new File(paths.get(8));
											FileInputStream fIn = new FileInputStream(myFile);
											BufferedReader myReader = new BufferedReader(
												new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader.readLine()) != null)
											{
												aBuffer += aDataRow + "\n";
											}

											builder.append(div+"\n\n");
											builder.append("VOLTAGE INFORMATION\n\n");
											builder.append(paths.get(8)+"\n\n");
											builder.append(aBuffer+"\n\n");
											builder.append(div+"\n\n");
											
											voltage = builder.toString();
											myReader.close();

										}
										catch (Exception e8)
										{
											try
											{
												File myFile = new File(paths.get(9));
												FileInputStream fIn = new FileInputStream(myFile);
												BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader.readLine()) != null)
												{
													aBuffer += aDataRow + "\n";
												}

												builder.append(div+"\n\n");
												builder.append("VOLTAGE INFORMATION\n\n");
												builder.append(paths.get(9)+"\n\n");
												builder.append(aBuffer+"\n\n");
												builder.append(div+"\n\n");
												
												voltage = builder.toString();
												myReader.close();

											}
											catch (Exception e9)
											{
												builder.append(div+"\n\n");
												builder.append("VOLTAGE INFORMATION\n\n");
												builder.append("no voltage information found\n\n");
												builder.append(div+"\n\n");
												
												voltage = builder.toString();
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		return voltage;
	}
	
	public static String tisDebug(){
		String tis = "";
		String div = "####################";
		List<String> paths = new ArrayList<String>();
		paths.add("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
		
		StringBuilder builder = new StringBuilder();
		try
		{
			File myFile = new File(paths.get(0));
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			builder.append(div+"\n\n");
			builder.append("TIMES IN STATE INFORMATION\n\n");
			builder.append(paths.get(0)+"\n\n");
			builder.append(aBuffer+"\n\n");
			builder.append(div+"\n\n");
			
			tis = builder.toString();
			myReader.close();

		}
		catch (Exception e)
		{
			builder.append(div+"\n\n");
			builder.append("TIMES IN STATE INFORMATION\n\n");
			builder.append("no times in state information found\n\n");
			builder.append(div+"\n\n");
			
			tis = builder.toString();
		
		}
	return tis;
		}
	
	public static String frequenciesDebug(){
		String freq = "";
		String div = "####################";
		List<String> paths = new ArrayList<String>();
		paths.add("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
		
		StringBuilder builder = new StringBuilder();
		try
		{
			File myFile = new File(paths.get(0));
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			builder.append(div+"\n\n");
			builder.append("FREQUENCY TABLE INFORMATION\n\n");
			builder.append(paths.get(0)+"\n\n");
			builder.append(aBuffer+"\n\n");
			builder.append(div+"\n\n");
			
			freq = builder.toString();
			myReader.close();

		}
		catch (Exception e)
		{
			builder.append(div+"\n\n");
			builder.append("FREQUENCY TABLE INFORMATION\n\n");
			builder.append("no 	frequency information found\n\n");
			builder.append(div+"\n\n");
			
			freq = builder.toString();
		
		}
	return freq;
		}
	
	public static String deviceInfoDebug(){
		String freq = "";
		String div = "####################";
		List<String> paths = new ArrayList<String>();
		paths.add("/proc/version");
		
		StringBuilder builder = new StringBuilder();
		builder.append(div+"\n\n");
		builder.append("DEVICE INFORMATION\n\n");
		builder.append("Device: "+android.os.Build.DEVICE+"\n");
		builder.append("Model: "+android.os.Build.MODEL+"\n");
		builder.append("Free Memory: "+String.valueOf(Runtime.getRuntime().freeMemory())+"\n");
		builder.append("Total Memory: "+String.valueOf(Runtime.getRuntime().totalMemory())+"\n");
		if(cpu3Online()){
			builder.append("Total Number of Cores: 4\n");
		}
		else if(cpu1Online() && cpu3Online()==false){
			builder.append("Total Number of Cores: 2\n");
		}
		else if(cpu1Online()==false && cpu3Online()==false ){
			builder.append("Total Number of Cores: 1\n");
		}
		else{
			builder.append("Unable to detect number of cpu cores\n");
		}
		builder.append("Number of Active Cores: "+String.valueOf(Runtime.getRuntime().availableProcessors())+"\n\n\n");
		try
		{
			File myFile = new File(paths.get(0));
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			
			builder.append("Kernel Information:\n\n");
			builder.append(aBuffer+"\n\n");
			builder.append(div+"\n\n");
			
			
			myReader.close();

		}
		catch (Exception e)
		{
			builder.append("Kernel Information\n");
			builder.append("Unable to find kernel information\n\n");
			builder.append(div+"\n\n");
			
			
		
		}
		freq = builder.toString();
	return freq;
		}
	
	public static String logcat(){
		String logcat = "";
		try{
		 Process process = Runtime.getRuntime().exec("logcat -d rs.pedjaapps.KernelTuner:V *:S");
	      BufferedReader bufferedReader = new BufferedReader(
	      new InputStreamReader(process.getInputStream()));
	                       
	      StringBuilder log=new StringBuilder();
	      String line;
	      while ((line = bufferedReader.readLine()) != null) {
	        log.append(line);
	      }
	      logcat = log.toString();
	    } catch (IOException e) {
	    }
		return logcat;
	}
	
	

	public static List<String> schedulers()
	{

		List<String> schedulers = new ArrayList<String>();

		try
		{

			File myFile = new File(SCHEDULER);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			String schedulersTemp = aBuffer;
			myReader.close();
			
			
			schedulersTemp = schedulersTemp.replace("[", "");
			schedulersTemp = schedulersTemp.replace("]", "");
			String[] temp = schedulersTemp.split("\\s");
			for(String s : temp){
			schedulers.add(s);
			}
		}
		catch (Exception e)
		{

		}
		
		return schedulers;
		}
	
	public static String mpup(){
		String mpup = "";
		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_up");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			mpup = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			
		}
		return mpup;
	}
	
	public static String mpdown(){
		String mpdown = "";
		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_down");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			mpdown = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			
		}
		return mpdown;
	}
	
	public static String gpu3d(){
		String gpu3d = "";
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
			

		}
		return gpu3d;
	}
	
	public static String gpu2d(){
		String gpu2d = "";
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
			

		}
		return gpu2d;
	}
	
	public static String cbb(){
		String cbb = "";
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

			cbb = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{
			
		}
		return cbb;
	}
	
	public static int fcharge(){
		int fcharge = 0;
		try
		{
    		String aBuffer = "";
			File myFile = new File(FCHARGE);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			fcharge = Integer.parseInt(aBuffer.trim());
			myReader.close();

		}
		catch (Exception e)
		{
			
		}
		return fcharge;
	}
	
	public static int vsync(){
		int vsync = 0;
		try
		{
    		String aBuffer = "";
			File myFile = new File(VSYNC);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			vsync = Integer.parseInt(aBuffer.trim());
			myReader.close();

		}
		catch (Exception e)
		{
			
		}
		return vsync;
	}
	
	public static String cDepth(){
		String cdepth = "";
		try
		{

			File myFile = new File(CDEPTH);
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
			

		}
		catch (IOException e)
		{
			
		}
		return cdepth;
	}
	
	public static String scheduler(){
		String scheduler = "";
		try
		{

			File myFile = new File(SCHEDULER);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			String schedulers = aBuffer;
			myReader.close();

			scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
			scheduler.trim();
			

		}
		catch (Exception e)
		{
			
		}
		return scheduler;
	}
	
	public static int sdCache(){
		int sd = 0;
		try
		{

			File myFile = new File(SD_CACHE);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			sd = Integer.parseInt(aBuffer.trim());
			myReader.close();

		}
		catch (Exception e)
		{
			

		}
		return sd;
	}
	
	public static int s2w(){
		int s2w = 0;
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

			s2w = Integer.parseInt(aBuffer.trim());

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

				s2w = Integer.parseInt(aBuffer.trim());

				myReader.close();

			}
			catch (Exception e2)
			{

			}
		}
		return s2w;
	}
	
	public static String readOTG(){
		String otg = "";
		try
		{

			File myFile = new File(OTG);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			otg = aBuffer.trim();

			myReader.close();

		}
		catch (Exception e)
		{

			try
			{

				File myFile = new File(OTG_2);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(new InputStreamReader(
																 fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				otg = aBuffer.trim();

				myReader.close();

			}
			catch (Exception e2)
			{

			}
		}
		return otg;
	}
	
	public static final class FreqsEntry
	{

		private final String freqName;
		private final int freq;
		


		public FreqsEntry(final String freqName, 
				final int freq)
		{
			this.freqName = freqName;
			this.freq = freq;
		}


		public String getFreqName()
		{
			return freqName;
		}


		public int getFreq(){
			return freq;
		}

	}

	static class MyComparator implements Comparator<FreqsEntry>{
		  public int compare(FreqsEntry ob1, FreqsEntry ob2){
		   return ob1.getFreq() - ob2.getFreq() ;
		  }
		}
	
}
