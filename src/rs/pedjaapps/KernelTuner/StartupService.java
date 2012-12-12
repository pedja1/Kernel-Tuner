package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class StartupService extends Service
{
	@Override
	public IBinder onBind(Intent intent)
	{
		
		return null;
	}

	
	

	SharedPreferences sharedPrefs;
	@Override
	public void onCreate()
	{
		Log.d("rs.pedjaapps.KernelTuner","StartupService created");
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate();

	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       	Log.d("rs.pedjaapps.KernelTuner","StartupService started");
	   new Apply().execute();
        return START_STICKY;
    }

	@Override
	public void onDestroy()
	{
		Log.d("rs.pedjaapps.KernelTuner","StartupService destroyed");
		super.onDestroy();
	}

	private class Apply extends AsyncTask<String, Void, String>
	{

		
		@Override
		protected String doInBackground(String... args)
		{
			List<CPUInfo.VoltageList> voltageList = CPUInfo.voltages();
			
			List<String> voltageFreqs =  new ArrayList<String>();
			
			for(CPUInfo.VoltageList v: voltageList){
				voltageFreqs.add((v.getFreq()));
			}
			
			String gpu3d = sharedPrefs.getString("gpu3d", "");
			String gpu2d = sharedPrefs.getString("gpu2d", "");
			String led = sharedPrefs.getString("led", "");
			String cpu0gov = sharedPrefs.getString("cpu0gov", "");
			String cpu0max = sharedPrefs.getString("cpu0max", "");
			String cpu0min = sharedPrefs.getString("cpu0min", "");
			String cpu1gov = sharedPrefs.getString("cpu1gov", "");
			String cpu1max = sharedPrefs.getString("cpu1max", "");
			String cpu1min = sharedPrefs.getString("cpu1min", "");
			String cpu2gov = sharedPrefs.getString("cpu2gov", "");
			String cpu2max = sharedPrefs.getString("cpu2max", "");
			String cpu2min = sharedPrefs.getString("cpu2min", "");
			String cpu3gov = sharedPrefs.getString("cpu3gov", "");
			String cpu3max = sharedPrefs.getString("cpu3max", "");
			String cpu3min = sharedPrefs.getString("cpu3min", "");
			String fastcharge = sharedPrefs.getString("fastcharge", "");
			String vsync = sharedPrefs.getString("vsync", "");
			String hw = sharedPrefs.getString("hw", "");
			String backbuf = sharedPrefs.getString("backbuf", "");
			
			String cdepth = sharedPrefs.getString("cdepth", "");
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
			String p1freq = sharedPrefs.getString("p1freq", "");
			String p2freq = sharedPrefs.getString("p2freq", "");
			String p3freq = sharedPrefs.getString("p3freq", "");
			String p1low = sharedPrefs.getString("p1low", "");
			String p1high = sharedPrefs.getString("p1high", "");
			String p2low = sharedPrefs.getString("p2low", "");
			String p2high = sharedPrefs.getString("p2high", "");
			String p3low = sharedPrefs.getString("p3low", "");
			String p3high = sharedPrefs.getString("p3high", "");
			String s2wStart = sharedPrefs.getString("s2wStart", "");
			String s2wEnd = sharedPrefs.getString("s2wEnd", "");

			boolean swap = sharedPrefs.getBoolean("swap", false);
			String swapLocation = sharedPrefs.getString("swap_location", "");
			String swappiness = sharedPrefs.getString("swappiness", "");
			String oom = sharedPrefs.getString("oom", "");
			String otg = sharedPrefs.getString("otg", "");
			
			String idle_freq = sharedPrefs.getString("idle_freq", "");
			String scroff = sharedPrefs.getString("scroff", "");
			String scroff_single = sharedPrefs.getString("scroff_single", "");


			/*try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				stdin.write(("echo \"" + cpu0gov + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				stdin.write(("echo \"" + cpu0min + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				stdin.write(("echo \"" + cpu0max + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				
			    stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				stdin.write(("echo \"" + cpu1gov + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				stdin.write(("echo \"" + cpu1min + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				stdin.write(("echo \"" + cpu1max + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
				
				stdin.write(("echo \"" + cpu2gov + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
				stdin.write(("echo \"" + cpu2min + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
				stdin.write(("echo \"" + cpu2max + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
				
		    	stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
				stdin.write(("echo \"" + cpu3gov + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
				stdin.write(("echo \"" + cpu3min + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
				stdin.write(("echo \"" + cpu3max + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
				
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
				if(!vsync.equals("")){
				stdin.write(("echo " + vsync + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				stdin.write(("echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				stdin.write(("echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
				}
				stdin.write(("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n");
				stdin.write(("echo " + fastcharge + " > /sys/kernel/fast_charge/force_fast_charge\n");
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
				if(!cdepth.equals("")){
				stdin.write(("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n");
				}
				if(!sdcache.equals("")){
				stdin.write(("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n");
				stdin.write(("chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb\n");
				stdin.write(("echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb\n");
				stdin.write(("echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb\n");
				}
				stdin.write(("chmod 777 /sys/block/mmcblk0/queue/scheduler\n");
				stdin.write(("chmod 777 /sys/block/mmcblk1/queue/scheduler\n");
				stdin.write(("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n");
				stdin.write(("echo " + io + " > /sys/block/mmcblk1/queue/scheduler\n");

				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");



				stdin.write(("echo " + delaynew.trim() + " > /sys/kernel/msm_mpdecision/conf/delay\n");
				stdin.write(("echo " + pausenew.trim() + " > /sys/kernel/msm_mpdecision/conf/pause\n");
				stdin.write(("echo " + thruploadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				stdin.write(("echo " + thrdownloadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				stdin.write(("echo " + thrupmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				stdin.write(("echo " + thrdownmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");
				stdin.write(("echo " + "\"" + ldt + "\"" + " > /sys/kernel/notification_leds/off_timer_multiplier\n");
				stdin.write(("echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake\n");
				stdin.write(("echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake/s2w_switch\n");

				stdin.write(("echo " + p1freq + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
				stdin.write(("echo " + p2freq + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
				stdin.write(("echo " + p3freq + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
				stdin.write(("echo " + p1low + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
				stdin.write(("echo " + p1high + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
				stdin.write(("echo " + p2low + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
				stdin.write(("echo " + p2high + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
				stdin.write(("echo " + p3low + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n");
				stdin.write(("echo " + p3high + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n");

				stdin.write(("chmod 777 /sys/android_touch/sweep2wake_startbutton\n");
				stdin.write(("echo " + s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n");
				stdin.write(("chmod 777 /sys/android_touch/sweep2wake_endbutton\n");
				stdin.write(("echo " + s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n");

				stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n");
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				stdin.write(("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
				stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n");
				stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk\n");
				if(!led.equals("")){
				stdin.write(("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
				stdin.write(("echo " + led + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
				}

				for (String s : voltageFreqs)
				{
					String temp = sharedPrefs.getString("voltage_" + s, "");

					if (!temp.equals(""))
					{
						stdin.write(("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
					}
				}

				List<String> govSettings = CPUInfo.govSettings();
				List<String> availableGovs = CPUInfo.availableGovs();

				for (String s : availableGovs)
				{
					for (String st : govSettings)
					{
						String temp = sharedPrefs.getString(s + "_" + st, "");

						if (!temp.equals(""))
						{
							stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
							stdin.write(("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
							System.out.println(temp);
						}
					}
				}
				if (swap == true)
				{
					stdin.write(("echo " + swappiness + " > /proc/sys/vm/swappiness\n");
					stdin.write(("swapon " + swapLocation.trim() + "\n");
				}
				else if (swap == false)
				{
					stdin.write(("swapoff " + swapLocation.trim() + "\n");

				}
				if(!oom.equals("")){
				stdin.write(("echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree\n");
				}
				if(!otg.equals("")){
					stdin.write(("echo " + otg + " > /sys/kernel/debug/msm_otg/mode\n");
					stdin.write(("echo " + otg + " > /sys/kernel/debug/otg/mode\n");
						
				}
				if(!idle_freq.equals("")){
				stdin.write(("echo " + idle_freq + " > /sys/kernel/msm_mpdecision/conf/idle_freq\n");
				}
				if(!scroff.equals("")){
				stdin.write(("echo " + scroff + " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n");
				}
				if(!scroff_single.equals("")){
				stdin.write(("echo " + scroff_single + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n");
				}
				stdin.write(("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
			}
			catch (IOException e1)
			{
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}*/
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("echo \"" + cpu0gov + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("echo \"" + cpu0min + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("echo \"" + cpu0max + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
				
			    stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("echo \"" + cpu1gov + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("echo \"" + cpu1min + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("echo \"" + cpu1max + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
				
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
				
				stdin.write(("echo \"" + cpu2gov + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("echo \"" + cpu2min + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("echo \"" + cpu2max + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
				
		    	stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("echo \"" + cpu3gov + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("echo \"" + cpu3min + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("echo \"" + cpu3max + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
				stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
				
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
				if(!vsync.equals("")){
				stdin.write(("echo " + vsync + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
				stdin.write(("echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
				stdin.write(("echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
				}
				stdin.write(("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
				stdin.write(("echo " + fastcharge + " > /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
				if(!cdepth.equals("")){
				stdin.write(("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
				}
				if(!sdcache.equals("")){
				stdin.write(("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
				stdin.write(("chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb\n").getBytes());
				stdin.write(("echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
				stdin.write(("echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb\n").getBytes());
				}
				stdin.write(("chmod 777 /sys/block/mmcblk0/queue/scheduler\n").getBytes());
				stdin.write(("chmod 777 /sys/block/mmcblk1/queue/scheduler\n").getBytes());
				stdin.write(("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n").getBytes());
				stdin.write(("echo " + io + " > /sys/block/mmcblk1/queue/scheduler\n").getBytes());

				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n").getBytes());



				stdin.write(("echo " + delaynew.trim() + " > /sys/kernel/msm_mpdecision/conf/delay\n").getBytes());
				stdin.write(("echo " + pausenew.trim() + " > /sys/kernel/msm_mpdecision/conf/pause\n").getBytes());
				stdin.write(("echo " + thruploadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n").getBytes());
				stdin.write(("echo " + thrdownloadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n").getBytes());
				stdin.write(("echo " + thrupmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n").getBytes());
				stdin.write(("echo " + thrdownmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n").getBytes());
				stdin.write(("echo " + "\"" + ldt + "\"" + " > /sys/kernel/notification_leds/off_timer_multiplier\n").getBytes());
				stdin.write(("echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake\n").getBytes());
				stdin.write(("echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake/s2w_switch\n").getBytes());

				stdin.write(("echo " + p1freq + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n").getBytes());
				stdin.write(("echo " + p2freq + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n").getBytes());
				stdin.write(("echo " + p3freq + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n").getBytes());
				stdin.write(("echo " + p1low + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n").getBytes());
				stdin.write(("echo " + p1high + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n").getBytes());
				stdin.write(("echo " + p2low + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n").getBytes());
				stdin.write(("echo " + p2high + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n").getBytes());
				stdin.write(("echo " + p3low + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n").getBytes());
				stdin.write(("echo " + p3high + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n").getBytes());

				stdin.write(("chmod 777 /sys/android_touch/sweep2wake_startbutton\n").getBytes());
				stdin.write(("echo " + s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n").getBytes());
				stdin.write(("chmod 777 /sys/android_touch/sweep2wake_endbutton\n").getBytes());
				stdin.write(("echo " + s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n").getBytes());

				stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
				stdin.write(("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
				stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
				stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n").getBytes());
				stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk\n").getBytes());
				if(!led.equals("")){
				stdin.write(("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
				stdin.write(("echo " + led + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
				}

				for (String s : voltageFreqs)
				{
					String temp = sharedPrefs.getString("voltage_" + s, "");

					if (!temp.equals(""))
					{
						stdin.write(("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n").getBytes());
					}
				}

				List<String> govSettings = CPUInfo.govSettings();
				List<String> availableGovs = CPUInfo.availableGovs();

				for (String s : availableGovs)
				{
					for (String st : govSettings)
					{
						String temp = sharedPrefs.getString(s + "_" + st, "");

						if (!temp.equals(""))
						{
							stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n").getBytes());
							stdin.write(("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n").getBytes());
							System.out.println(temp);
						}
					}
				}
				if (swap == true)
				{
					stdin.write(("echo " + swappiness + " > /proc/sys/vm/swappiness\n").getBytes());
					stdin.write(("swapon " + swapLocation.trim() + "\n").getBytes());
				}
				else if (swap == false)
				{
					stdin.write(("swapoff " + swapLocation.trim() + "\n").getBytes());

				}
				if(!oom.equals("")){
				stdin.write(("echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree\n").getBytes());
				}
				if(!otg.equals("")){
					stdin.write(("echo " + otg + " > /sys/kernel/debug/msm_otg/mode\n").getBytes());
					stdin.write(("echo " + otg + " > /sys/kernel/debug/otg/mode\n").getBytes());
						
				}
				if(!idle_freq.equals("")){
				stdin.write(("echo " + idle_freq + " > /sys/kernel/msm_mpdecision/conf/idle_freq\n").getBytes());
				}
				if(!scroff.equals("")){
				stdin.write(("echo " + scroff + " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n").getBytes());
				}
				if(!scroff_single.equals("")){
				stdin.write(("echo " + scroff_single + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n").getBytes());
				}
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner ChangeGovernor Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner ChangeGovernor Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }
		
			return "";
		}
		
		@Override
		protected void onPostExecute(String result){
			
			stopService(new Intent(StartupService.this,StartupService.class));
			
		}
	}	
	

}
