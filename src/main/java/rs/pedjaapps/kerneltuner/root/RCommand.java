package rs.pedjaapps.kerneltuner.root;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
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
								 "start mpdecision",
								 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "echo 0 > /sys/devices/system/cpu/cpu" + coreNum + "/online",
								 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/online");
        }

        else
        {
            new RootUtils().exec(callback,
								 "echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
								 "stop mpdecision",
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
        if (Constants.MPDECISION_BINARY.exists()) commands.add("stop mpdecision");
        for (int i = 0; i < 4; i++)
        {
            if (new File("/sys/devices/system/cpu/cpu" + i + "/online").exists())
            {
                commands.add("chmod 777 /sys/devices/system/cpu/cpu" + i + "/online");
                commands.add("echo " + (on_off ? 1 : 0) + " > /sys/devices/system/cpu/cpu" + i + "/online");
                if (on_off) commands.add("chmod 444 /sys/devices/system/cpu/cpu" + i + "/online");
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
        PrefsManager.setCpuMaxFreq(coreNum, freq);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
							 "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
							 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq",
							 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_max_freq");
    }

    public static void setMinFreq(int coreNum, int freq, RootUtils.CommandCallback callback)
    {
        PrefsManager.setCpuMinFreq(coreNum, freq);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
							 "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
							 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq",
							 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_min_freq");
    }

    public static void setGovernor(int coreNum, String governor, RootUtils.CommandCallback callback)
    {
        PrefsManager.setCpuGovernor(coreNum, governor);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
							 "echo " + governor + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
							 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor",
							 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/scaling_governor");
    }

    public static void setMaxScroffFreq(int coreNum, int freq, RootUtils.CommandCallback callback)
    {
        PrefsManager.setCpuScroffFreq(coreNum, freq);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
							 "echo " + freq + " > /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
							 "chmod 444 /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq",
							 "chown system /sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/screen_off_max_freq");
    }

    public static void setGpuMaxFreq(int freq, RootUtils.CommandCallback callback)
    {
        PrefsManager.setGpu3d(freq);
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.GPU_3D_2,
							 "echo " + freq + " > " + Constants.GPU_3D_2,
							 "chmod 444 " + Constants.GPU_3D_2,
							 "chown system " + Constants.GPU_3D_2);
    }

    public static void setGpuGov(String gov, RootUtils.CommandCallback callback)
    {
        PrefsManager.setGpuGovernor(gov);
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.GPU_3D_2_GOV,
							 "echo " + gov + " > " + Constants.GPU_3D_2_GOV,
							 "chmod 444 " + Constants.GPU_3D_2_GOV,
							 "chown system " + Constants.GPU_3D_2_GOV);
    }

    public static void setScheduler(String scheduler, RootUtils.CommandCallback callback)
    {
        PrefsManager.setScheduler(scheduler);
        new RootUtils().exec(callback,
							 "chmod 777 " + "/sys/block/mmcblk0/queue/scheduler",
							 "echo " + scheduler + " > /sys/block/mmcblk0/queue/scheduler");
    }

    public static void setReadAhead(String cache, RootUtils.CommandCallback callback)
    {
        PrefsManager.setReadAhead(cache);
        new RootUtils().exec(callback,
							 "chmod 777 " + "/sys/devices/virtual/bdi/179:0/read_ahead_kb",
							 "chmod 777 " + "/sys/devices/virtual/bdi/179:32/read_ahead_kb",
							 "echo " + cache + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb",
							 "echo " + cache + " > /sys/devices/virtual/bdi/179:32/read_ahead_kb");
    }

    public static void setS2w(int value, String path, RootUtils.CommandCallback callback)
    {
        PrefsManager.setS2W(value);
        new RootUtils().exec(callback,
							 "chmod 777 " + path,
							 "echo " + value + " > " + path);
    }

    public static void setDt2w(int value, RootUtils.CommandCallback callback)
    {
        PrefsManager.setDT2W(value);
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.DT2W,
							 "echo " + value + " > " + Constants.DT2W);
    }

    public static void setFastcharge(int value, RootUtils.CommandCallback callback)
    {
        PrefsManager.setFastcharge(value);
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.FCHARGE,
							 "echo " + value + " > " + Constants.FCHARGE);
    }

    public static void setVsync(int value, RootUtils.CommandCallback callback)
    {
        PrefsManager.setVsync(value);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable",
							 "chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode",
							 "chmod 777 /sys/kernel/debug/msm_fb/0/backbuff",
							 "echo " + value + " > /sys/kernel/debug/msm_fb/0/vsync_enable",
							 "echo " + (value == 1 ? 1 : 0) + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode",
							 "echo " + (value == 1 ? 3 : 4) + " > /sys/kernel/debug/msm_fb/0/backbuff");
    }

    public static void setOtg(int value, RootUtils.CommandCallback callback)
    {
        PrefsManager.setOtg(value);
        new RootUtils().exec(callback,
							 "chmod 777 /sys/kernel/debug/msm_otg/mode",
							 "chmod 777 /sys/kernel/debug/otg/mode",
							 "echo " + (value == 1 ? "host" : "peripheral") + " > /sys/kernel/debug/otg/mode",
							 "echo " + (value == 1 ? "host" : "peripheral") + " > /sys/kernel/debug/msm_otg/mode");
    }

    public static void killPid(int pid, RootUtils.CommandCallback callback)
    {
        new RootUtils().exec(callback,
							 "kill " + pid);
    }

    public static String readFileContent(File file) throws IOException
    {
		if (!file.exists())throw new FileNotFoundException(file.getAbsolutePath());
		if (file.canRead())
		{
            return FileUtils.readFileToString(file);
		}
		else
		{
			RootUtils ru = new RootUtils();
			return ru.execAndWait("cat " + file.getAbsolutePath());
		}
    }

    public static String[] readFileContentAsLineArray(File path) throws IOException
    {
        return readFileContent(path).trim().split("\n");
    }

    public static List<String> readFileContentAsList(File path) throws IOException
    {
        return Arrays.asList(readFileContentAsLineArray(path));
    }

    public static void setTcpCongestion(String tcp, RootUtils.CommandCallback callback)
    {
        PrefsManager.setTcpCongestion(tcp);
        new RootUtils().exec(callback,
							 "chmod 777 " + Constants.TCP_CONGESTION,
							 "echo " + tcp + " > " + Constants.TCP_CONGESTION);
    }


    public static void toggleMpDecision(RootUtils.CommandCallback callback, boolean on_off)
    {
        List<String> commands = new ArrayList<>();
        if (Constants.MPDECISION.exists())
            commands.add("echo " + (on_off ? 1 : 0) + " > " + Constants.MPDECISION);
        if (Constants.MPDECISION_BINARY.exists())
            commands.add(on_off ? "start" : "stop" + " mpdecision");
        new RootUtils().exec(callback, commands.toArray(new String[commands.size()]));
    }

    public static void setEntropyWriteThreshold(int value)
    {
        new RootUtils().exec((RootUtils.CommandCallback)null, "echo " + value + " > " + Constants.ENTROPY_WRITE_THRESHOLD);
        PrefsManager.setEntropyWrite(value);
    }

    public static void setEntropyReadThreshold(int value)
    {
        new RootUtils().exec((RootUtils.CommandCallback)null, "echo " + value + " > " + Constants.ENTROPY_READ_THRESHOLD);
        PrefsManager.setEntropyRead(value);
    }

}
