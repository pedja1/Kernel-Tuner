package rs.pedjaapps.KernelTuner.utility;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;

/**
 * Created by pedja on 9/28/13.
 */
public class FSHelper
{
    public static final int CPU_COUNT_METHOD_STATIC = 1;
    public static final int CPU_COUNT_METHOD_DYNAMIC = 2;

	public static String readCpu(String path)
	{
		try
		{
			return FileUtils.readFileToString(new File(path));
		}
		catch(Exception e)
		{
			return "offline";
		}
	}

	public static List<Integer> getAllCpuFreqs(boolean forceRead)
	{
		if (forceRead || MainApp.getInstance().getCpuFreqs().size() == 0)
		{
			List<Integer> freqs = new ArrayList<Integer>();
			try
			{
				String tmp = FileUtils.readFileToString(new File(Constants.PATH_CPU_PRE + 0 + Constants.PATH_CPU_ALL_FREQS));
				String[] tmp2 = tmp.trim().split(" ");

				for (String s : tmp2)
				{
					int freq = Integer.parseInt(s);
					freqs.add(freq);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Log.e(Constants.LOG_TAG, "FSHelper : getAllCpuFreq() : " + e.getMessage());
			}
		    return freqs;
		}
		else
		{
			return MainApp.getInstance().getCpuFreqs();
		}

	}

    public static  boolean isCpuAvailable()
    {
        boolean i = false;
        if (new File(Constants.CPU0_FREQS).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isOOMAvailable()
    {
        boolean i = false;
        if (new File(Constants.OOM).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isThermalAvailable()
    {
        boolean i = false;
        if (new File(Constants.THERMALD).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isSwapAvailable()
    {
        boolean i = false;
        if (new File(Constants.SWAPS).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isCpuAvailable(int cpu)
	{
		return new File(Constants.PATH_CPU_PRE + cpu + Constants.PATH_CPU_ONLINE).exists();
	}

	/*public static  boolean cpu1Exists()
	 {
	 return new File(Constants.cpu1online).exists();
	 }

	 public static  boolean cpu2Exists()
	 {
	 return new File(Constants.cpu2online).exists();
	 }

	 public static  boolean cpu3Exists()
	 {
	 return new File(Constants.cpu3online).exists();
	 }*/

    public static  boolean isGpuAvailable()
    {
        boolean i = false;
        if (new File(Constants.GPU_3D).exists() || new File(Constants.GPU_SGX540).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isColorDepthAvailable()
    {
        boolean i = false;
        if (new File(Constants.CDEPTH).exists() || new File(Constants.CDEPTH_SGX).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isVoltageAvailable()
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

    public static  boolean isOtgAvailable()
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

    public static  boolean isSweepToWakeAvailable()
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

    public static  boolean isTimesInStateAvailable()
    {
        boolean i = false;
        if (new File(Constants.TIMES_IN_STATE_CPU0).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isMpdecisionAvailable()
    {
        boolean i = false;
        if (new File(Constants.MPDECISION).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isSoftButtonsBacklightAvailable()
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

    public static  boolean isSDReadAheadAvailable()
    {
        boolean i = false;
        if (new File(Constants.SD_CACHE).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isVsyncAvailable()
    {
        boolean i = false;
        if (new File(Constants.VSYNC).exists())
        {
            i = true;
        }
        return i;

    }

    public static  boolean isFastchargeAvailable()
    {
        boolean i = false;
        if (new File(Constants.FCHARGE).exists())
        {
            i = true;
        }
        return i;

    }

	public static final int getCpuCount(int method)
	{
		int count = 0;
		if (method == CPU_COUNT_METHOD_DYNAMIC)
		{
			String[] cpuFolders = new File(Constants.PATH_CPU_ROOT).list();

			for (int i = 0; i < cpuFolders.length; i++)
			{
				if (cpuFolders[i].matches("^.*\\d$"))
				{
					count++;
				}
			}
		}
		else if (method == CPU_COUNT_METHOD_STATIC)
		{
			//assuming max num of cpus is 4
			for (int i = 0; i < 4; i++)
			{
				if (isCpuAvailable(i))
				{
					count++;
				}
			}
		}

		return count;
	}


}
