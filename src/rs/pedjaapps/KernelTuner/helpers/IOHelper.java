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
package rs.pedjaapps.KernelTuner.helpers;

import java.io.*;

import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.entry.TimesEntry;
import rs.pedjaapps.KernelTuner.ui.OOM;

public class IOHelper
{
	
	public static final boolean freqsExists()
	{
		boolean i = false;
		if (new File(Constants.CPU0_FREQS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean oomExists()
	{
		boolean i = false;
		if (new File(Constants.OOM).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean thermaldExists()
	{
		boolean i = false;
		if (new File(Constants.THERMALD).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean swapsExists()
	{
		boolean i = false;
		if (new File(Constants.SWAPS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean cpu0Online()
	{
		boolean i = false;
		if (new File(Constants.cpu0online).exists())
		{
			i = true;
		}
		return i;

	}

	public static final boolean cpu1Online()
	{
		boolean i = false;
		if (new File(Constants.cpu1online).exists())
		{
			i = true;
		}
		return i;

	}

	public static final boolean cpu2Online()
	{
		boolean i = false;
		if (new File(Constants.cpu2online).exists())
		{
			i = true;
		}
		return i;

	}

	public static final boolean cpu3Online()
	{
		boolean i = false;
		if (new File(Constants.cpu3online).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean gpuExists()
	{
		boolean i = false;
		if (new File(Constants.GPU_3D).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean cdExists()
	{
		boolean i = false;
		if (new File(Constants.CDEPTH).exists())
		{
			i = true;
		}
		return i;

	}

	public static final boolean voltageExists()
	{
		boolean i = false;
		if (new File(Constants.VOLTAGE_PATH).exists())
		{
			i = true;
		}
		else if (new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean otgExists()
	{
		boolean i = false;
		if (new File(Constants.OTG).exists())
		{
			i = true;
		}
		else if (new File(Constants.OTG_2).exists())
		{
			i = true;
		}
		return i;

	}

	public static final boolean s2wExists()
	{
		boolean i = false;
		if (new File(Constants.S2W).exists())
		{
			i = true;
		}
		else if (new File(Constants.S2W_ALT).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean TISExists()
	{
		boolean i = false;
		if (new File(Constants.TIMES_IN_STATE_CPU0).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean mpdecisionExists()
	{
		boolean i = false;
		if (new File(Constants.MPDECISION).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean buttonsExists()
	{
		boolean i = false;
		if (new File(Constants.BUTTONS_LIGHT).exists())
		{
			i = true;
		}
		else if (new File(Constants.BUTTONS_LIGHT_2).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean sdcacheExists()
	{
		boolean i = false;
		if (new File(Constants.SD_CACHE).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean vsyncExists()
	{
		boolean i = false;
		if (new File(Constants.VSYNC).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static final boolean fchargeExists()
	{
		boolean i = false;
		if (new File(Constants.FCHARGE).exists())
		{
			i = true;
		}
		return i;

	}

	public static final List<FreqsEntry> frequencies()
	{
		
		List<FreqsEntry> entries = new ArrayList<FreqsEntry>();
		List<String> frequencies = new ArrayList<String>();
		try
		{

			File myFile = new File(Constants.CPU0_FREQS);
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
			fIn.close();
		}
		catch (Exception e)
		{
			try
			{

	 			FileInputStream fstream = new FileInputStream(Constants.TIMES_IN_STATE_CPU0);

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
	 			fstream.close();
	 			br.close();
			}
			catch (Exception ee)
			{
				entries.add(new FreqsEntry("", 0));
 				
			}
		}
		return entries;

	}
	
	public static final List<String> oom()
	{
		try
		{
			return Arrays.asList(FileUtils.readFileToString(new File(Constants.OOM)).split(","));
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}

	}
	
	public static final String leds()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.BUTTONS_LIGHT_2)).trim();	
		}
		catch (Exception e)
		{
			try
			{
				return FileUtils.readFileToString(new File(Constants.BUTTONS_LIGHT)).trim();
			}
			catch (Exception ee)
			{
				return "";
			}
		}
		
	}

	public static final List<String> governors()
	{
		try
		{
			return Arrays.asList(FileUtils.readFileToString(new File(Constants.CPU0_GOVS)).split("\\s"));
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}

	}

	public static final String cpu0MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU0_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
		
	}
	
	public static final String cpuMin()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU_MIN)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}
	
	public static final String cpuMax()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU_MAX)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu0MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU0_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu1MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU1_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu1MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU1_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu2MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU2_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu2MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU2_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu3MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU3_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU3_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu0CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU0_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu1CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU1_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu2CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU2_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU3_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu0CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU0_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu1CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU1_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu2CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU2_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU3_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}


	public static  final List<TimesEntry> getTis()
	{
		List<TimesEntry> times = new ArrayList<TimesEntry>();

		try
		{

			FileInputStream fstream = new FileInputStream(Constants.TIMES_IN_STATE_CPU0);

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
			fstream.close();
			br.close();
		}
		catch (Exception e)
		{
			Log.e("Error: " , e.getMessage());
		}
		
		return times;

	}

	

	public static final List<VoltageList> voltages()
	{
		List<VoltageList> voltages = new ArrayList<VoltageList>();
		if(voltages.isEmpty()==false){
			voltages.clear();
		}
		try
		{

			FileInputStream fstream = new FileInputStream(Constants.VOLTAGE_PATH);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{

				voltages.add(new VoltageList(strLine.substring(0,strLine.length() - 10).trim(),
											 strLine.substring(0,strLine.length() - 13).trim()+"MHz", 
											 Integer.parseInt(strLine.substring(9,strLine.length() - 0).trim())));
			}

			in.close();
			fstream.close();
			br.close();
		}
		catch (Exception e)
		{
			try
			{

				FileInputStream fstream = new FileInputStream(Constants.VOLTAGE_PATH_TEGRA_3);

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null)
				{	
					String[] delims = strLine.split(" ");
					voltages.add(new VoltageList(delims[0],
							 					 delims[0].substring(0,delims[0].length() - 4).trim()+"MHz", 
							 					 Integer.parseInt(delims[1])));

				}

				in.close();
				fstream.close();
				br.close();
			}
			catch (Exception ex)
			{

			}
		}
		System.out.println(voltages);
		return voltages;

	}

	

	public static final String uptime()
	{
		String uptime;

		int time =(int) SystemClock.elapsedRealtime();

		String s = ((int)((time / 1000) % 60))+"";
		String m = ((int)((time / (1000 * 60)) % 60))+"";
		String h = ((int)((time / (1000 * 3600)) % 24))+"";
		String d = ((int)(time / (1000 * 60 * 60 * 24)))+"";
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
	public static final String deepSleep()
	{
		String deepSleep;

		int time =(int) (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());

		String s = ((int)((time / 1000) % 60))+"";
	    String m = ((int)((time / (1000 * 60)) % 60))+"";
		String h = ((int)((time / (1000 * 3600)) % 24))+"";
		String d = ((int)(time / (1000 * 60 * 60 * 24)))+"";
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

	public static final String cpuTemp()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU_TEMP)).trim();
		}
		catch (Exception e2)
		{
			return "0";
		}
	}

	public static final String cpuInfo()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.CPU_INFO)).trim();
		}
		catch (Exception e2)
		{
			return "";
		}
	}

	public static final List<String> availableGovs()
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

	public static final List<String> govSettings()
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


	public static final List<String> schedulers()
	{

		List<String> schedulers = new ArrayList<String>();

		try
		{

			File myFile = new File(Constants.SCHEDULER);
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
			fIn.close();
			
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
	
	public static final String mpup()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_THR_UP)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpdown(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_THR_DOWN)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String gpu3d(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.GPU_3D)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String gpu2d(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.GPU_2D)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final int fcharge(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.FCHARGE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final int vsync(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.VSYNC)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final String cDepth(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.CDEPTH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String scheduler(){
		String scheduler = "";
		try
		{

			File myFile = new File(Constants.SCHEDULER);
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
			fIn.close();

		}
		catch (Exception e)
		{
			
		}
		return scheduler;
	}
	
	public static final int sdCache(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.SD_CACHE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final int s2w()
	{
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.S2W)).trim());
		}
		catch (Exception e)
		{
			try
			{
				return Integer.parseInt(FileUtils.readFileToString(new File(Constants.S2W_ALT)).trim());
			}
			catch(Exception e2)
			{
				return 0;
			}
		}
	}
	
	public static final String readOTG(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.OTG)).trim();
		}
		catch (Exception e)
		{
			try
			{
				return FileUtils.readFileToString(new File(Constants.OTG_2)).trim();
			}
			catch(Exception e2)
			{
				return "";
			}
		}
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
	
	public static final class VoltageList
	{

		private final String freq;
		private final String freqName;
		private final int voltage;
		


		public VoltageList(final String freq, final String freqName,
				final int voltage)
		{
			this.freq = freq;
			this.freqName = freqName;
			this.voltage = voltage;
		}


		public String getFreq()
		{
			return freq;
		}
		
		public String getFreqName()
		{
			return freqName;
		}


		public int getVoltage(){
			return voltage;
		}

	}
	
	public static final String kernel(){
		try
		{
			return FileUtils.readFileToString(new File(Constants.KERNEL)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	static class MyComparator implements Comparator<FreqsEntry>{
		  public int compare(FreqsEntry ob1, FreqsEntry ob2){
		   return ob1.getFreq() - ob2.getFreq() ;
		  }
		}
	public static int batteryLevel()
	{
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.BATTERY_LEVEL)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static double batteryTemp()
	{
		try
		{
			return Double.parseDouble(FileUtils.readFileToString(new File(Constants.BATTERY_TEMP)).trim());
		}
		catch (Exception e)
		{
			return 0.0;
		}
	}
	
	public static String batteryDrain()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.BATTERY_DRAIN)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static int batteryVoltage()
	{
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.BATTERY_VOLTAGE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static String batteryTechnology()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.BATTERY_TECH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	public static String batteryHealth()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.BATTERY_HEALTH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	public static String batteryCapacity()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.BATTERY_CAPACITY)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	/**
	 * @return 0 if not charging or not found, 
	 * @return 1 if charging from USB
	 * @return 2 if charging from AC
	 * */
	public static int batteryChargingSource()
	{
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(Constants.BATTERY_CHARGING_SOURCE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	public static boolean isTempEnabled(){
		try
		{
			if(FileUtils.readFileToString(new File(Constants.CPU_TEMP_ENABLED)).equals("enabled")){
				return true;
			}
			else{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static final String mpDelay()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_DELAY)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpPause()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_PAUSE)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpTimeUp()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_TIME_UP)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpTimeDown()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_TIME_DOWN)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpIdleFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_IDLE_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpScroffFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String mpScroffSingleCore()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_SINGLE)).trim();
		}
		catch (Exception e)
		{
			return "err";
		}
	}
	
	public static final String thermalLowLow()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_LOW)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String thermalLowHigh()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_HIGH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String thermalMidLow()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MID_LOW)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String thermalMidHigh()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MID_HIGH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String thermalMaxLow()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_LOW)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static final String thermalMaxHigh()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_HIGH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static final String thermalLowFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static final String thermalMidFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MID_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	public static final String thermalMaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	static int load;
	public static int cpuLoad() {
		// Do something long
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				float fLoad = 0;
					try {
						RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
						String load = reader.readLine();

						String[] toks = load.split(" ");

						long idle1 = Long.parseLong(toks[5]);
						long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						try {
							Thread.sleep(360);
						} catch (Exception e) {}

						reader.seek(0);
						load = reader.readLine();
						reader.close();

						toks = load.split(" ");

						long idle2 = Long.parseLong(toks[5]);
						long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
					load = (int) (fLoad*100);
					
					
				
			}
		};
		new Thread(runnable).start();
		return load;
	}
	
}
