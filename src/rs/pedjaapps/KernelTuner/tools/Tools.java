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
package rs.pedjaapps.KernelTuner.tools;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import android.os.StatFs;
import android.os.Environment;
import rs.pedjaapps.KernelTuner.R;

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

	public static String tempConverter(String tempPref, double cTemp)
	{
		String tempNew = "";
		/**
		 * cTemp = temperature in celsius tempPreff = string from shared
		 * preferences with value fahrenheit, celsius or kelvin
		 */
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

	public static int getPreferedTheme(String theme)
	{
		if (theme.equals("light")) 
		{
			return R.style.Theme_Sherlock_Light;
		} 
		else if (theme.equals("dark")) 
		{
			return R.style.Theme_Sherlock;
		} 
		else if (theme.equals("light_dark_action_bar")) 
		{
			return R.style.Theme_Sherlock_Light_DarkActionBar;
		}
		else if (theme.equals("miui_light")) 
		{
			return R.style.Theme_Miui_Light;
		} 
		else if (theme.equals("miui_dark")) 
		{
			return R.style.Theme_Miui_Dark;
		} 
		else if (theme.equals("sense5")) 
		{
			return R.style.Theme_Sense5;
		}
		else if (theme.equals("sense5_light")) 
		{
			return R.style.Theme_Light_Sense5;
		}
		else
		{
			return R.style.Theme_Sherlock_Light_DarkActionBar;
		}
	}

	public static int getPreferedThemeTranslucent(String theme)
	{
		if (theme.equals("light")) 
		{
			return R.style.Theme_Translucent_NoTitleBar_Light;
		} 
		else if (theme.equals("dark")) 
		{
			return R.style.Theme_Translucent_NoTitleBar;
		} 
		else if (theme.equals("miui_light")) 
		{
			return R.style.Theme_Translucent_NoTitleBar_Miui_Light;
		} 
		else if (theme.equals("miui_dark")) 
		{
			return R.style.Theme_Translucent_NoTitleBar_Miui;
		} 
		else if (theme.equals("sense5")) 
		{
			return R.style.Theme_Translucent_NoTitleBar_Sense5;
		}
		else if (theme.equals("sense5_light")) 
		{
			return R.style.Theme_Translucent_NoTitleBar_Sense5_Light;
		}
		else
		{
			return R.style.Theme_Translucent_NoTitleBar_Light;
		}
	}

	public static int getPreferedThemeSwitchCompat(String theme)
	{
		if (theme.equals("light")) 
		{
			return R.style.SwitchCompatAndSherlockLight;
		} 
		else if (theme.equals("dark")) 
		{
			return R.style.SwitchCompatAndSherlock;
		} 
		else if (theme.equals("light_dark_action_bar")) 
		{
			return R.style.SwitchCompatAndSherlockLightDark;
		}
		else if (theme.equals("miui_light")) 
		{
			return R.style.Theme_Miui_Light;
		} 
		else if (theme.equals("miui_dark")) 
		{
			return R.style.Theme_Miui_Dark;
		} 
		else if (theme.equals("sense5")) 
		{
			return R.style.Theme_Sense5;
		}
		else if (theme.equals("sense5_light")) 
		{
			return R.style.Theme_Light_Sense5;
		}
		else
		{
			return R.style.SwitchCompatAndSherlockLight;
		}
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
}
