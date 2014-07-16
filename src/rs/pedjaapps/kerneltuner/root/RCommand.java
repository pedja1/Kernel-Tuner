package rs.pedjaapps.kerneltuner.root;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.kerneltuner.Constants;

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
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "echo 0 > /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/online");
		}

		else
		{
			new RootUtils().exec(callback,
								 "echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
								 "chmod 666 /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "echo 1 > /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_cur_freq",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor");
		}
	}

    public static void toggleAllCpu(RootUtils.CommandCallback callback, boolean on_off)
    {
        List<String> commands = new ArrayList<>();
        commands.add("echo " + (on_off ? 1 : 0) + " > /sys/kernel/msm_mpdecision/conf/enabled");
        for(int i = 0; i < 4; i++)
        {
            if(new File("/sys/devices/system/cpu/cpu" + i + "/online").exists())
            {
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/online");
                commands.add("echo " + (on_off ? 1 : 0) + " > /sys/devices/system/cpu/cpu" + i + "/online");
                if(on_off)commands.add("chmod 444 /sys/devices/system/cpu/cpu" + i + "/online");
                commands.add("chown system /sys/devices/system/cpu/cpu" + i + "/online");
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_max_freq");
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_min_freq");
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_governor");
            }
        }
        new RootUtils().exec(callback, commands.toArray(new String[commands.size()]));
    }

    public static void setMaxFreq(int coreNum, int freq, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
                "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
                "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
                "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
                "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq");
    }

    public static void setMinFreq(int coreNum, int freq, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
                "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
                "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
                "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
                "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq");
    }

    public static void setGovernor(int coreNum, String governor, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
                "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
                "echo " + governor + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
                "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
                "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor");
    }

    public static void setMaxScroffFreq(int coreNum, int freq, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
                "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
                "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
                "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
                "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq");
    }
	
	public static void setGpuMaxFreq(int freq, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.GPU_3D_2,
							 "echo " + freq + " > " + Constants.GPU_3D_2,
							 "chmod 444 " + Constants.GPU_3D_2,
							 "chown system " + Constants.GPU_3D_2);
    }

	public static void setGpuGov(String gov, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.GPU_3D_2_GOV,
							 "echo " + gov + " > " + Constants.GPU_3D_2_GOV,
							 "chmod 444 " + Constants.GPU_3D_2_GOV,
							 "chown system " + Constants.GPU_3D_2_GOV);
    }
	
}
