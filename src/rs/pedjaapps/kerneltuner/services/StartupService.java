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
package rs.pedjaapps.kerneltuner.services;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.helpers.DatabaseHandler;
import rs.pedjaapps.kerneltuner.model.SysCtl;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;

public class StartupService extends IntentService
{
	public StartupService()
	{
		super("startup_service");
	}

	@Override
	protected void onHandleIntent(Intent p1)
	{
		// TODO: Implement this method
	}
	
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    SharedPreferences sharedPrefs;

    @Override
    public void onCreate()
    {
        Log.d("rs.pedjaapps.KernelTuner", "StartupService created");
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("rs.pedjaapps.KernelTuner", "StartupService started");
        DatabaseHandler db = new DatabaseHandler(this);
        List<Voltage> voltageList = IOHelper.voltages();

        List<String> aditionalCommands = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            String gov = PrefsManager.getCpuGovernor(i);
            int min = PrefsManager.getCpuMinFreq(i);
            int max = PrefsManager.getCpuMaxFreq(i);
            int maxScroff = PrefsManager.getCpuScroffFreq(i);
            if(gov != null)
            {
                RCommand.setGovernor(i, gov, null);
            }
            if(min != -1)
            {
                RCommand.setMinFreq(i, min, null);
            }
            if(max != -1)
            {
                RCommand.setMaxFreq(i, max, null);
            }
            if(maxScroff != -1)
            {
                RCommand.setMaxScroffFreq(i, maxScroff, null);
            }
        }
        int gpu2d = PrefsManager.getGpu2d();
        int gpu3d = PrefsManager.getGpu3d();
        String gpuGov = PrefsManager.getGpuGovernor();
        if(gpu2d != -1)
        {
            aditionalCommands.add("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
            aditionalCommands.add("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
            aditionalCommands.add("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
            aditionalCommands.add("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk");
            aditionalCommands.add("chmod 777 " + Constants.GPU_SGX540);
            aditionalCommands.add("echo " + gpu2d + " > "+ Constants.GPU_SGX540);
        }
        if(gpu3d != -1)
        {
            aditionalCommands.add("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
            aditionalCommands.add("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
            RCommand.setGpuMaxFreq(gpu3d, null);
        }
        if(gpuGov != null)
        {
            RCommand.setGpuGov(gpuGov, null);
        }

        for (int i = 0; i < 8; i++)
        {
            aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_" + i);
            aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_" + i);
        }

        aditionalCommands.add("mount -t debugfs debugfs /sys/kernel/debug");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/enabled");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_freq");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/idle_freq");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/delay");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_mpdecision/conf/pause");
        aditionalCommands.add("echo " + PrefsManager.getMpdecEnabled() + " > /sys/kernel/msm_mpdecision/conf/enabled");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(0) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_0");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(2) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_2");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(3) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_3");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(4) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_4");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(5) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_5");
        aditionalCommands.add("echo " + PrefsManager.getMpdecThreshold(7) + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_7");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(0) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_0");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(2) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_2");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(3) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_3");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(4) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_4");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(5) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_5");
        aditionalCommands.add("echo " + PrefsManager.getMpdecTime(7) + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_7");
        aditionalCommands.add("echo " + PrefsManager.getMpdecMaxCpus() + " > /sys/kernel/msm_mpdecision/conf/max_cpus");
        aditionalCommands.add("echo " + PrefsManager.getMpdecMinCpus() + " > /sys/kernel/msm_mpdecision/conf/min_cpus");
        aditionalCommands.add("echo " + PrefsManager.getMpdecIdleFreq() + " > /sys/kernel/msm_mpdecision/conf/idle_freq");
        aditionalCommands.add("echo " + PrefsManager.getMpdecSoF() + " > /sys/kernel/msm_mpdecision/conf/scroff_freq");
        aditionalCommands.add("echo " + PrefsManager.getMpdecSoSc() + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core");
        aditionalCommands.add("echo " + PrefsManager.getMpdecDelay() + " > /sys/kernel/msm_mpdecision/conf/delay");
        aditionalCommands.add("echo  " + PrefsManager.getMpdecPause() + " > /sys/kernel/msm_mpdecision/conf/pause");

        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_low");
        aditionalCommands.add("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_high");
        aditionalCommands.add("echo " + PrefsManager.getThremalFreq(1) + " > /sys/kernel/msm_thermal/conf/allowed_low_freq");
        aditionalCommands.add("echo " + PrefsManager.getThremalFreq(2) + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq");
        aditionalCommands.add("echo " + PrefsManager.getThremalFreq(3) + " > /sys/kernel/msm_thermal/conf/allowed_max_freq");
        aditionalCommands.add("echo " + PrefsManager.getThremalLow(1) + " > /sys/kernel/msm_thermal/conf/allowed_low_low");
        aditionalCommands.add("echo " + PrefsManager.getThremalHigh(1) + " > /sys/kernel/msm_thermal/conf/allowed_low_high");
        aditionalCommands.add("echo " + PrefsManager.getThremalLow(2) + " > /sys/kernel/msm_thermal/conf/allowed_mid_low");
        aditionalCommands.add("echo " + PrefsManager.getThremalHigh(2) + " > /sys/kernel/msm_thermal/conf/allowed_mid_high");
        aditionalCommands.add("echo " + PrefsManager.getThremalLow(3) + " > /sys/kernel/msm_thermal/conf/allowed_max_low");
        aditionalCommands.add("echo " + PrefsManager.getThremalHigh(3) + " > /sys/kernel/msm_thermal/conf/allowed_max_high");
		
        RCommand.setScheduler(PrefsManager.getScheduer(), null);
        RCommand.setReadAhead(PrefsManager.getReadAhead(), null);
        int s2w = PrefsManager.getS2W();
        RCommand.setS2w(s2w,  Constants.S2W, null);
        RCommand.setS2w(s2w,  Constants.S2W_ALT, null);
        RCommand.setDt2w(PrefsManager.getDT2W(), null);
        RCommand.setFastcharge(PrefsManager.getFastcharge(), null);
        RCommand.setVsync(PrefsManager.getVsync(), null);
        RCommand.setOtg(PrefsManager.getOtg(), null);
        RCommand.setTcpCongestion(PrefsManager.getTcpCongestion(), null);
        RCommand.setEntropyReadThreshold(PrefsManager.getEntropyRead());
        RCommand.setEntropyWriteThreshold(PrefsManager.getEntropyWrite());

        Voltage voltage = db.getVoltageByName("boot");
        if(voltage != null)
        {
            if(new File(Constants.VOLTAGE_PATH).exists())
            {
                aditionalCommands.add("chmod 777 " + Constants.VOLTAGE_PATH);
                String[] freqs = voltage.getDbFreqs().trim().split(",");
                String[] voltages = voltage.getDbValues().trim().split(",");
                if(freqs.length == voltages.length) for(int i = 0; i < freqs.length; i++)
                {
                    aditionalCommands.add("echo '" + freqs[i] + " " + voltages[i] + "' > " + Constants.VOLTAGE_PATH);
                }
            }
            if(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())
            {
                aditionalCommands.add("chmod 777 " + Constants.VOLTAGE_PATH_TEGRA_3);
                String voltages = voltage.getDbValues().trim();
                aditionalCommands.add("echo '" + voltages + "' > " + Constants.VOLTAGE_PATH_TEGRA_3);
            }
        }

        List<String> govSettings = IOHelper.govSettings();
        List<String> availableGovs = IOHelper.availableGovs();

        for (String s : availableGovs)
        {
            for (String st : govSettings)
            {
                String temp = sharedPrefs.getString(s + "_" + st, "");

                if (!temp.equals(""))
                {
                    aditionalCommands.add("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st);
                    aditionalCommands.add("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st);

                }
            }
        }

        String oom = sharedPrefs.getString("oom", null);
        if (oom != null)
        {
            aditionalCommands.add("echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree");
        }

        List<SysCtl> sysEntries = db.getAllSysCtlEntries();
        for (SysCtl e : sysEntries)
        {
            aditionalCommands.add(getFilesDir().getPath() + "/busybox sysctl -w " + e.getKey().trim() + "=" + e.getValue().trim());
        }
        new RootUtils().exec(aditionalCommands.toArray(new String[aditionalCommands.size()]));
        new RootUtils().closeAllShells();
		stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        Log.d("rs.pedjaapps.KernelTuner", "StartupService destroyed");
        super.onDestroy();
    }
}
