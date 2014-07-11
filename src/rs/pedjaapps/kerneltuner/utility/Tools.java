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
package rs.pedjaapps.kerneltuner.utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.model.SysCtlDatabaseEntry;
import rs.pedjaapps.kerneltuner.helpers.DatabaseHandler;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;

public class Tools
{

    public static String byteToHumanReadableSize(long size)
	{
		String hrSize = "";
		long b = size;
		double k = size / 1024.0;
		double m = size / 1048576.0;
		double g = size / 1073741824.0;
		double t = size / 1099511627776.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1)
		{
			hrSize = dec.format(t).concat("TB");
		}
		else if (g > 1)
		{
			hrSize = dec.format(g).concat("GB");
		}
		else if (m > 1)
		{
			hrSize = dec.format(m).concat("MB");
		}
		else if (k > 1)
		{
			hrSize = dec.format(k).concat("KB");
		}
		else if (b > 1)
		{
			hrSize = dec.format(b).concat("B");
		}
		return hrSize;

	}

	public static String kByteToHumanReadableSize(int size)
	{
		String hrSize = "";
		int k = size;
		double m = size / 1024.0;
		double g = size / 1048576.0;
		double t = size / 1073741824.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1)
		{
			hrSize = dec.format(t).concat("TB");
		}
		else if (g > 1)
		{
			hrSize = dec.format(g).concat("GB");
		}
		else if (m > 1)
		{
			hrSize = dec.format(m).concat("MB");
		}
		else if (k > 1)
		{
			hrSize = dec.format(k).concat("KB");
		}

		return hrSize;

	}

	public static String msToDate(long ms)
	{
		SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		return f.format(ms);
	}
	public static String msToDateSimple(long ms)
	{
		SimpleDateFormat f = new SimpleDateFormat("ddMMyyHHmmss");
		return f.format(ms);
	}

	public static String mbToPages(int progress)
	{
		String prog = (progress * 1024 / 4) + "";
		return prog;
	}

	public static int pagesToMb(int pages)
	{
		return pages / 1024 * 4;

	}

	public static long getAvailableSpaceInBytesOnInternalStorage()
	{
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		availableSpace = (long) stat.getAvailableBlocks()
			* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnInternalStorage()
	{
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
			* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnInternalStorage()
	{
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getAvailableSpaceInBytesOnExternalStorage()
	{
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		availableSpace = (long) stat.getAvailableBlocks()
			* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnExternalStorage()
	{
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
			* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnExternalStorage()
	{
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}
	/**
	 * cTemp = temperature in celsius tempPreff = string from shared
	 * preferences with value fahrenheit, celsius or kelvin
	 */
	public static String tempConverter(String tempPref, double cTemp)
	{
		String tempNew = "";
		
		if (tempPref.equals("fahrenheit"))
		{
			tempNew = ((cTemp * 1.8) + 32) + "°F";

		}
		else if (tempPref.equals("celsius"))
		{
			tempNew = cTemp + "°C";

		}
		else if (tempPref.equals("kelvin"))
		{

			tempNew = (cTemp + 273.15) + "°C";

		}
		return tempNew;
	}

	public static String msToHumanReadableTime(long time)
	{

		String timeString;
		String s = "" + ((int)((time / 1000) % 60));
		String m = "" + ((int)((time / (1000 * 60)) % 60));
		String h = "" + ((int)((time / (1000 * 3600)) % 24));
		String d = "" + ((int)(time / (1000 * 60 * 60 * 24)));
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
		timeString = builder.toString();
		return timeString;
	}

	public static String msToHumanReadableTime2(long time)
	{

		String timeString;
		String s = "" + ((int)((time / 100) % 60));
		String m = "" + ((int)((time / (100 * 60)) % 60));
		String h = "" + ((int)((time / (100 * 3600)) % 24));
		String d = "" + ((int)(time / (100 * 60 * 60 * 24)));
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
		timeString = builder.toString();
		return timeString;
	}
	
	public static String getAbi(){
		String abi = android.os.Build.CPU_ABI;
		if(abi.contains("armeabi")){
			return "arm";
		}
		else if(abi.contains("x86")){
			return "x86";
		}
		else if(abi.contains("mips")){
			return "mips";
		}
		else{
			return "arm";
		}
	}
	
	public static String getProcessStatus(String status){
		if(status.equals("S")){
			return "Sleeping";
		} else if(status.equals("D")){
			return "Uninterruptible";
		} else if(status.equals("R")){
			return "Running";
		} else if(status.equals("T")){
			return "Stopped";
		} else if(status.equals("X")){
			return "Terminated";
		} else if(status.equals("Z")){
			return "Zombie";
		} else{
			return "";
		}
		
	}
	
	public static void exportInitdScripts(Context c, List<String> voltages) {

		DatabaseHandler db = new DatabaseHandler(c);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
		String gpu3d = sharedPrefs.getString("gpu3d", "");
		String gpu2d = sharedPrefs.getString("gpu2d", "");

		String hw = sharedPrefs.getString("hw", "");
		String cdepth = sharedPrefs.getString("cdepth", "");
		String cpu1min = sharedPrefs.getString("cpu1min", "");
		String cpu1max = sharedPrefs.getString("cpu1max", "");
		String cpu0max = sharedPrefs.getString("cpu0max", "");
		String cpu0min = sharedPrefs.getString("cpu0min", "");
		String cpu3min = sharedPrefs.getString("cpu3min", "");
		String cpu3max = sharedPrefs.getString("cpu3max", "");
		String cpu2max = sharedPrefs.getString("cpu2max", "");
		String cpu2min = sharedPrefs.getString("cpu2min", "");

		String fastcharge = sharedPrefs.getString("fastcharge", "");
		String mpdecisionscroff = sharedPrefs.getString("mpdecisionscroff", "");
		String backbuff = sharedPrefs.getString("backbuf", "");
		String vsync = sharedPrefs.getString("vsync", "");
		String led = sharedPrefs.getString("led", "");
		String cpu0gov = sharedPrefs.getString("cpu0gov", "");
		String cpu1gov = sharedPrefs.getString("cpu1gov", "");
		String cpu2gov = sharedPrefs.getString("cpu2gov", "");
		String cpu3gov = sharedPrefs.getString("cpu3gov", "");
		String io = sharedPrefs.getString("io", "");
		String sdcache = sharedPrefs.getString("sdcache", "");

		String delaynew = sharedPrefs.getString("delaynew", "");
		String pausenew = sharedPrefs.getString("pausenew", "");
		String thruploadnew = sharedPrefs.getString("thruploadnew", "");
		String thrupmsnew = sharedPrefs.getString("thrupmsnew", "");
		String thrdownloadnew = sharedPrefs.getString("thrdownloadnew", "");
		String thrdownmsnew = sharedPrefs.getString("thrdownmsnew", "");
		String ldt = sharedPrefs.getString("ldt", "");
		String s2w = sharedPrefs.getString("s2w", "");
		String s2wStart = sharedPrefs.getString("s2wStart", "");
		String s2wEnd = sharedPrefs.getString("s2wEnd", "");

		String p1freq = sharedPrefs.getString("p1freq", "");
		String p2freq = sharedPrefs.getString("p2freq", "");
		String p3freq = sharedPrefs.getString("p3freq", "");
		String p1low = sharedPrefs.getString("p1low", "");
		String p1high = sharedPrefs.getString("p1high", "");
		String p2low = sharedPrefs.getString("p2low", "");
		String p2high = sharedPrefs.getString("p2high", "");
		String p3low = sharedPrefs.getString("p3low", "");
		String p3high = sharedPrefs.getString("p3high", "");
		boolean swap = sharedPrefs.getBoolean("swap", false);
		String swapLocation = sharedPrefs.getString("swap_location", "");
		String swappiness = sharedPrefs.getString("swappiness", "");
		String oom = sharedPrefs.getString("oom", "");
		String otg = sharedPrefs.getString("otg", "");

		String idle_freq = sharedPrefs.getString("idle_freq", "");
		String scroff = sharedPrefs.getString("scroff", "");
		String scroff_single = sharedPrefs.getString("scroff_single", "");
		String voltage_ = sharedPrefs.getString("voltage_", "");
		String[] thr = new String[6];
		String[] tim = new String[6];
		thr[0] = sharedPrefs.getString("thr0", "");
		thr[1] = sharedPrefs.getString("thr2", "");
		thr[2] = sharedPrefs.getString("thr3", "");
		thr[3] = sharedPrefs.getString("thr4", "");
		thr[4] = sharedPrefs.getString("thr5", "");
		thr[5] = sharedPrefs.getString("thr7", "");
		tim[0] = sharedPrefs.getString("tim0", "");
		tim[1] = sharedPrefs.getString("tim2", "");
		tim[2] = sharedPrefs.getString("tim3", "");
		tim[3] = sharedPrefs.getString("tim4", "");
		tim[4] = sharedPrefs.getString("tim5", "");
		tim[5] = sharedPrefs.getString("tim7", "");
		String maxCpus = sharedPrefs.getString("max_cpus", "");
		String minCpus = sharedPrefs.getString("min_cpus", "");
		StringBuilder gpubuilder = new StringBuilder();

		gpubuilder.append("#!/system/bin/sh");
		gpubuilder.append("\n");
		if (!gpu3d.equals("")) {
			gpubuilder
					.append("echo ")
					.append("\"")
					.append(gpu3d)
					.append("\"")
					.append(" > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk")
					.append("\n");
			
		}
		if (!gpu2d.equals("")) {
			gpubuilder
					.append("echo ")
					.append("\"")
					.append(gpu2d)
					.append("\"")
					.append(" > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
			gpubuilder.append("\n");
			gpubuilder
					.append("echo ").append(
							 "\"").append(
							 gpu2d).append(
							 "\"").append(
							 " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
			gpubuilder.append("\n");

		}

		String gpu = gpubuilder.toString();

		StringBuilder cpubuilder = new StringBuilder();

		cpubuilder.append("#!/system/bin/sh");
		cpubuilder.append("\n");
		/**
		 * cpu0
		 * */
		if (!cpu0gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n");
		}
		if (!cpu0max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0max).append(
							 "\"").append(
							" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
		}
		if (!cpu0min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0min).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n");
		}
		/**
		 * cpu1
		 * */
		if (!cpu1gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu1gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n");
		}
		if (!cpu1max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu1max).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n");
		}
		if (!cpu1min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n").append(
							"echo ").append(
							"\"").append(
							 cpu1min).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n").append(
							"chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n");
		}

		/**
		 * cpu2
		 * */
		if (!cpu2gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu2gov).append(
						 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n");
		}
		if (!cpu2max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							cpu2max).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n");
		}
		if (!cpu2min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu2min).append(
						 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n\n");
		}
		/**
		 * cpu3
		 * */

		if (!cpu3gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n");
		}
		if (!cpu3max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3max).append(
							"\"").append(
							 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n");
		}
		if (!cpu3min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3min).append(
							 "\"").append(
						 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n\n");
		}
		List<String> govSettings = IOHelper.govSettings();
		List<String> availableGovs = IOHelper.availableGovs();

		for (String s : availableGovs) {
			for (String st : govSettings) {
				String temp = sharedPrefs.getString(s + "_" + st, "");

				if (!temp.equals("")) {
					cpubuilder
							.append("chmod 777 /sys/devices/system/cpu/cpufreq/")
							.append(s).append( "/").append(st).append("\n");
					cpubuilder.append("echo ").append("\"").append(temp).append("\"").append(
							 " > /sys/devices/system/cpu/cpufreq/").append(s).append("/").append(st).append("\n");

				}
			}
		}
		String cpu = cpubuilder.toString();

		StringBuilder miscbuilder = new StringBuilder();

		miscbuilder.append("#!/system/bin/sh \n\n#mount debug filesystem\nmount -t debugfs debugfs /sys/kernel/debug \n\n");
		if (!vsync.equals("")) {
			miscbuilder.append("#vsync\nchmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable \nchmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode \nchmod 777 /sys/kernel/debug/msm_fb/0/backbuff \necho " + "\"" + vsync + "\""
					+ " > /sys/kernel/debug/msm_fb/0/vsync_enable \n" + "echo "
					+ "\"" + hw + "\""
					+ " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n"
					+ "echo " + "\"" + backbuff + "\""
					+ " > /sys/kernel/debug/msm_fb/0/backbuff \n\n");
		}
		if (!led.equals("")) {
			miscbuilder
					.append("#capacitive buttons backlight\n"
							+ "chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n"
							+ "echo "
							+ "\""
							+ led
							+ "\""
							+ " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n\n");
		}
		if (!fastcharge.equals("")) {
			miscbuilder.append("#fastcharge\n"
					+ "chmod 777 /sys/kernel/fast_charge/force_fast_charge \n"
					+ "echo " + "\"" + fastcharge + "\""
					+ " > /sys/kernel/fast_charge/force_fast_charge \n\n");
		}
		if (!cdepth.equals("")) {
			miscbuilder.append("#color depth\n"
					+ "chmod 777 /sys/kernel/debug/msm_fb/0/bpp \n" + "echo "
					+ "\"" + cdepth + "\""
					+ " > /sys/kernel/debug/msm_fb/0/bpp \n\n");
		}

		if (!mpdecisionscroff.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core \n"
							+ "echo "
							+ "\""
							+ mpdecisionscroff
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/scroff_single_core \n");
		}
		if (!delaynew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/delay \n"
							+ "echo "
							+ "\""
							+ delaynew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/delay \n");
		}
		if (!pausenew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/pause \n"
							+ "echo "
							+ "\""
							+ pausenew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/pause \n");
		}
		if (!thruploadnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n"
							+ "echo "
							+ "\""
							+ thruploadnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n");

		}
		if (!thrdownloadnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n"
							+ "echo "
							+ "\""
							+ thrdownloadnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n");

		}
		if (!thrupmsnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n"
							+ "echo "
							+ "\""
							+ thrupmsnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up \n");
		}
		if (!thrdownmsnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n"
							+ "echo "
							+ "\""
							+ thrdownmsnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down \n\n");
		}
		if(!thr[0].equals("")){
			for(int i = 0; i < 8; i++){
				miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+i+"\n");
			}
			miscbuilder.append("echo " + thr[0] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+0+"\n");
			miscbuilder.append("echo " + thr[1] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+2+"\n");
			miscbuilder.append("echo " + thr[2] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+3+"\n");
			miscbuilder.append("echo " + thr[3] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+4+"\n");
			miscbuilder.append("echo " + thr[4] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+5+"\n");
			miscbuilder.append("echo " + thr[5] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+7+"\n");
			
		}
		if(!tim[0].equals("")){
			for(int i = 0; i < 8; i++){
				miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_"+i+"\n");
			}
			miscbuilder.append("echo " + tim[0] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+0+"\n");
			miscbuilder.append("echo " + tim[1] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+2+"\n");
			miscbuilder.append("echo " + tim[3] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+3+"\n");
			miscbuilder.append("echo " + tim[3] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+4+"\n");
			miscbuilder.append("echo " + tim[4] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+5+"\n");
			miscbuilder.append("echo " + tim[5] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+7+"\n");
			
		}
		if(!maxCpus.equals("")){
			miscbuilder.append("echo " + maxCpus + " > /sys/kernel/msm_mpdecision/conf/max_cpus\n");
		}
		if(!minCpus.equals("")){
			miscbuilder.append("echo " + minCpus + " > /sys/kernel/msm_mpdecision/conf/min_cpus\n");
		}
		if (!sdcache.equals("")) {
			miscbuilder
					.append("#sd card cache size\n"
							+ "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n"
							+ "chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n"
							+ "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/block/mmcblk1/queue/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/block/mmcblk0/queue/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n\n");
		}
		if (!io.equals("")) {
			miscbuilder.append("#IO scheduler\n"
					+ "chmod 777 /sys/block/mmcblk0/queue/scheduler \n"
					+ "chmod 777 /sys/block/mmcblk1/queue/scheduler \n"
					+ "echo " + "\"" + io + "\""
					+ " > /sys/block/mmcblk0/queue/scheduler \n" + "echo "
					+ "\"" + io + "\""
					+ " > /sys/block/mmcblk1/queue/scheduler \n\n");
		}
		if (!ldt.equals("")) {
			miscbuilder
					.append("#Notification LED Timeout\n"
							+ "chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n"
							+ "echo "
							+ "\""
							+ ldt
							+ "\""
							+ " > /sys/kernel/notification_leds/off_timer_multiplier\n\n");
		}
		if (!s2w.equals("")) {
			miscbuilder.append("#Sweep2Wake\n"
					+ "chmod 777 /sys/android_touch/sweep2wake\n" + "echo "
					+ "\"" + s2w + "\""
					+ " > /sys/android_touch/sweep2wake\n\n");
		}
		if (!s2wStart.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/android_touch/sweep2wake_startbutton\n"
							+ "echo "
							+ s2wStart
							+ " > /sys/android_touch/sweep2wake_startbutton\n"
							+ "chmod 777 /sys/android_touch/sweep2wake_endbutton\n"
							+ "echo "
							+ s2wEnd
							+ " > /sys/android_touch/sweep2wake_endbutton\n\n");
		}

		if (!p1freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n"
							+ "echo "
							+ "\""
							+ p1freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
		}
		if (!p2freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n"
							+ "echo "
							+ "\""
							+ p2freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
		}
		if (!p3freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n"
							+ "echo "
							+ "\""
							+ p3freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
		}
		if (!p1low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n"
							+ "echo "
							+ "\""
							+ p1low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
		}
		if (!p1high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n"
							+ "echo "
							+ "\""
							+ p1high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
		}
		if (!p2low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n"
							+ "echo "
							+ "\""
							+ p2low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
		}
		if (!p2high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n"
							+ "echo "
							+ "\""
							+ p2high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
		}
		if (!p3low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_low\n"
							+ "echo "
							+ "\""
							+ p3low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_high_low\n");
		}

		if (!p3high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_high\n"
							+ "echo "
							+ "\""
							+ p3high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_high_high\n");
		}

		if (!idle_freq.equals("")) {
			miscbuilder.append("echo " + idle_freq
					+ " > /sys/kernel/msm_mpdecision/conf/idle_freq\n");
		}
		if (!scroff.equals("")) {
			miscbuilder.append("echo " + scroff
					+ " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n");
		}
		if (!scroff_single.equals("")) {
			miscbuilder
					.append("echo "
							+ scroff_single
							+ " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n\n");
		}
		if (swap == true) {
			miscbuilder.append("echo " + swappiness
					+ " > /proc/sys/vm/swappiness\n" + "swapon "
					+ swapLocation.trim() + "\n\n");

		} else if (swap == false) {
			miscbuilder.append("swapoff " + swapLocation.trim() + "\n\n");

		}
		if (!oom.equals("")) {
			miscbuilder.append("echo " + oom
					+ " > /sys/module/lowmemorykiller/parameters/minfree\n");

		}
		if (!otg.equals("")) {
			miscbuilder.append("echo " + otg
					+ " > /sys/kernel/debug/msm_otg/mode\n");
			miscbuilder.append("echo " + otg
					+ " > /sys/kernel/debug/otg/mode\n");
		}
		miscbuilder.append("#Umount debug filesystem\n"
				+ "umount /sys/kernel/debug \n");
		String misc = miscbuilder.toString();

		StringBuilder voltagebuilder = new StringBuilder();
		voltagebuilder.append("#!/system/bin/sh \n");
		if (new File(Constants.VOLTAGE_PATH).exists()){
		for (String s : voltages) {
			String temp = sharedPrefs.getString("voltage_" + s, "");
			if (!temp.equals("")) {
				voltagebuilder
						.append("echo "
								+ "\""
								+ temp
								+ "\""
								+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
			}
		}
		}
		else if (new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()){
			if(!voltage_.equals("")){
			voltagebuilder.append("echo " + voltage_ + " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");
			}
		}
		String voltage = voltagebuilder.toString();
		StringBuilder sysBuilder = new StringBuilder();
		sysBuilder.append("#!/system/bin/sh \n");
			List<SysCtlDatabaseEntry> sysEntries = db.getAllSysCtlEntries();
			for(SysCtlDatabaseEntry e : sysEntries){
				sysBuilder.append(c.getFilesDir().getPath() + "/sysctl-"+Tools.getAbi()+" -w " + e.getKey().trim() + "=" + e.getValue().trim()+"\n");
			}
		
		String sys = sysBuilder.toString();
		try {

			FileOutputStream fOut = c.openFileOutput("99ktsysctl",
												   Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(sys);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = c.openFileOutput("99ktcputweaks",
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(cpu);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = c.openFileOutput("99ktgputweaks",
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(gpu);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = c.openFileOutput("99ktmisctweaks",
					Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(misc);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}

		try {

			FileOutputStream fOut = c.openFileOutput("99ktvoltage", Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(voltage);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		new Initd(c).execute(new String[] { "apply" });
	}

    public static int parseInt(String value, int def)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception e)
        {
            return def;
        }
    }
}

