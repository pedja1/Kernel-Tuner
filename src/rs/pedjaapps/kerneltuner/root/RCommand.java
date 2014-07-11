package rs.pedjaapps.kerneltuner.root;

import com.crashlytics.android.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.io.*;

public class RCommand
{
	public static void toggleCpu(int coreNum, RootUtils.CommandCallback callback)
	{

		File file = new File("/sys/devices/system/cpu/cpu" + coreNum
							 + "/cpufreq/scaling_governor");
		if (file.exists())
		{
			new RootUtils().exec(callback,
								 "echo 1 > /sys/kernel/msm_mpdecision/conf/enabled",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "echo 0 > /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "chown system /sys/devices/system/cpu/cpu" + coreNum
								 + "/online");
		}

		else
		{
			new RootUtils().exec(callback,
								 "echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
								 "chmod 666 /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "echo 1 > /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "chown system /sys/devices/system/cpu/cpu" + coreNum
								 + "/online",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum
								 + "/cpufreq/scaling_max_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum
								 + "/cpufreq/scaling_min_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum
								 + "/cpufreq/scaling_cur_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum
								 + "/cpufreq/scaling_governor");
		}
	}
}
