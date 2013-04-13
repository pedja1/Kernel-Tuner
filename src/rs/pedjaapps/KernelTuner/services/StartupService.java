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
package rs.pedjaapps.KernelTuner.services;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.entry.SysCtlDatabaseEntry;
import rs.pedjaapps.KernelTuner.helpers.DatabaseHandler;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.Constants;
import java.io.File;

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
			DatabaseHandler db = new DatabaseHandler(StartupService.this);
			List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
			
			List<String> voltageFreqs =  new ArrayList<String>();
			
			for(IOHelper.VoltageList v: voltageList){
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
			String voltage_ = sharedPrefs.getString("voltage_","");
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
		
		      CommandCapture command = new CommandCapture(0, 
	            "chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor",
				"chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq",
				"chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq",
				"echo \"" + cpu0gov + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor",
				"echo \"" + cpu0min + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq",
			    "echo \"" + cpu0max + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq",
				"chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor",
				"chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq",
				"chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq",
				
			    "chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor",
				"chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq",
				"chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq",
				"echo \"" + cpu1gov + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor",
				"echo \"" + cpu1min + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq",
				"echo \"" + cpu1max + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq",
				"chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor",
				"chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq",
				"chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq",
				
				"chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor",
				"chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq",
				"chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq",
				
				"echo \"" + cpu2gov + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor",
				"echo \"" + cpu2min + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq",
				"echo \"" + cpu2max + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq",
				"chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor",
				"chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq",
				"chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq",
				
		    	"chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor",
				"chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq",
				"chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq",
				"echo \"" + cpu3gov + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor",
				"echo \"" + cpu3min + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq",
				"echo \"" + cpu3max + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq",
				"chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor",
				"chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq",
				"chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq",
				
				"chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable",
				"chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode",
				"chmod 777 /sys/kernel/debug/msm_fb/0/backbuff",
			"chmod 777 /sys/block/mmcblk0/queue/scheduler",
			"chmod 777 /sys/block/mmcblk1/queue/scheduler",
			"echo " + io + " > /sys/block/mmcblk0/queue/scheduler",
			"echo " + io + " > /sys/block/mmcblk1/queue/scheduler",

			"chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/dealy",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/pause",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down",
			"chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down",



			"echo " + delaynew.trim() + " > /sys/kernel/msm_mpdecision/conf/delay",
			"echo " + pausenew.trim() + " > /sys/kernel/msm_mpdecision/conf/pause",
			"echo " + thruploadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up",
			"echo " + thrdownloadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down",
			"echo " + thrupmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up",
			"echo " + thrdownmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down",
			"echo " + "\"" + ldt + "\"" + " > /sys/kernel/notification_leds/off_timer_multiplier",
			"echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake",
			"echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake/s2w_switch",

			"echo " + p1freq + " > /sys/kernel/msm_thermal/conf/allowed_low_freq",
			"echo " + p2freq + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq",
			"echo " + p3freq + " > /sys/kernel/msm_thermal/conf/allowed_max_freq",
			"echo " + p1low + " > /sys/kernel/msm_thermal/conf/allowed_low_low",
			"echo " + p1high + " > /sys/kernel/msm_thermal/conf/allowed_low_high",
			"echo " + p2low + " > /sys/kernel/msm_thermal/conf/allowed_mid_low",
			"echo " + p2high + " > /sys/kernel/msm_thermal/conf/allowed_mid_high",
			"echo " + p3low + " > /sys/kernel/msm_thermal/conf/allowed_max_low",
			"echo " + p3high + " > /sys/kernel/msm_thermal/conf/allowed_max_high",

			"chmod 777 /sys/android_touch/sweep2wake_startbutton",
			"echo " + s2wStart + " > /sys/android_touch/sweep2wake_startbutton",
			"chmod 777 /sys/android_touch/sweep2wake_endbutton",
			"echo " + s2wEnd + " > /sys/android_touch/sweep2wake_endbutton",

			"mount -t debugfs debugfs /sys/kernel/debug",
			"chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk",
			"echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk",
			"chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk",
			"chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk",
			"echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk",
			"echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk",
			"echo " + thr[0] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+0,
			"echo " + thr[1] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+2,
			"echo " + thr[2] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+3,
			"echo " + thr[3] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+4,
			"echo " + thr[4] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+5,
			"echo " + thr[5] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+7,
			"echo " + tim[0] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+0,
			"echo " + tim[1] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+2,
			"echo " + tim[2] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+3,
			"echo " + tim[3] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+4,
			"echo " + tim[4] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+5,
			"echo " + tim[5] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+7,
			"echo " + maxCpus + " > /sys/kernel/msm_mpdecision/conf/max_cpus",
			"echo " + minCpus + " > /sys/kernel/msm_mpdecision/conf/min_cpus"
			
				);
				try{
				RootTools.getShell(true).add(command).waitForFinish();
				}
				catch(Exception e){
					
				}
				if(!vsync.equals("")){
				CommandCapture command4 = new CommandCapture(0, 
				"echo " + vsync + " > /sys/kernel/debug/msm_fb/0/vsync_enable",
				"echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode",
				"echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff");
					try{
						RootTools.getShell(true).add(command4).waitForFinish();
					}
					catch(Exception e){

					}
				}
				if(!fastcharge.equals("")){
					CommandCapture command1 = new CommandCapture(0, 
				"chmod 777 /sys/kernel/fast_charge/force_fast_charge",
				"echo " + fastcharge + " > /sys/kernel/fast_charge/force_fast_charge",
				"chmod 777 /sys/kernel/debug/msm_fb/0/bpp");
					try{
						RootTools.getShell(true).add(command1).waitForFinish();
					}
					catch(Exception e){

					}
				}
				if(!cdepth.equals("")){
					CommandCapture command2 = new CommandCapture(0, 
				"echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp");
					try{
						RootTools.getShell(true).add(command2).waitForFinish();
					}
					catch(Exception e){

					}
				}
				if(!sdcache.equals("")){
					CommandCapture command3 = new CommandCapture(0, 
				"chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb",
				"chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb",
				"echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb",
				"echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb");
					try{
						RootTools.getShell(true).add(command3).waitForFinish();
					}
					catch(Exception e){

					}
				}
			if(!led.equals("")){
				CommandCapture command5 = new CommandCapture(0, 
				"chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents",
				"echo " + led + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
				try{
					RootTools.getShell(true).add(command5).waitForFinish();
				}
				catch(Exception e){

				}
				}

				if(new File(Constants.VOLTAGE_PATH).exists()){
				for (String s : voltageFreqs)
				{
					String temp = sharedPrefs.getString("voltage_" + s, "");

					if (!temp.equals(""))
					{
						CommandCapture command6 = new CommandCapture(0, "echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
						try{
							RootTools.getShell(true).add(command6).waitForFinish();
						}
						catch(Exception e){

						}
					}
				}
				}
			else if (new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()){
				if(!voltage_.equals("")){
					CommandCapture command1 = new CommandCapture(0, "echo " + voltage_ + " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");
					try{
						RootTools.getShell(true).add(command1).waitForFinish();
					}
					catch(Exception e){

					}
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
							CommandCapture command7 = new CommandCapture(0, 
							"chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st,
							"echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st);
							try{
								RootTools.getShell(true).add(command7).waitForFinish();
							}
							catch(Exception e){

							}
							
						}
					}
				}
				if (swap == true)
				{
					CommandCapture command8 = new CommandCapture(0, 
					"echo " + swappiness + " > /proc/sys/vm/swappiness",
					"swapon " + swapLocation.trim());
					try{
						RootTools.getShell(true).add(command8).waitForFinish();
					}
					catch(Exception e){

					}
				}
				else if (swap == false)
				{
					CommandCapture command9 = new CommandCapture(0, 
					"swapoff " + swapLocation.trim());
					try{
						RootTools.getShell(true).add(command9).waitForFinish();
					}
					catch(Exception e){

					}

				}
				if(!oom.equals("")){
					CommandCapture command10 = new CommandCapture(0, 
				      "echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree");
					try{
						RootTools.getShell(true).add(command10).waitForFinish();
					}
					catch(Exception e){

					}
				}
				if(!otg.equals("")){
					CommandCapture command11 = new CommandCapture(0, 
					"echo " + otg + " > /sys/kernel/debug/msm_otg/mode",
					"echo " + otg + " > /sys/kernel/debug/otg/mode");
					try{
						RootTools.getShell(true).add(command11).waitForFinish();
					}
					catch(Exception e){

					}
						
				}
				if(!idle_freq.equals("")){
					CommandCapture command12 = new CommandCapture(0, 
				"echo " + idle_freq + " > /sys/kernel/msm_mpdecision/conf/idle_freq");
					try{
						RootTools.getShell(true).add(command12).waitForFinish();
					}
					catch(Exception e){

					}
				
				}
				if(!scroff.equals("")){
					CommandCapture command13 = new CommandCapture(0,
				"echo " + scroff + " > /sys/kernel/msm_mpdecision/conf/scroff_freq");
					try{
						RootTools.getShell(true).add(command13).waitForFinish();
					}
					catch(Exception e){

					}
				}
				if(!scroff_single.equals("")){
					CommandCapture command14 = new CommandCapture(0, 
				"echo " + scroff_single + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core");
					try{
						RootTools.getShell(true).add(command14).waitForFinish();
					}
					catch(Exception e){

					}
				}
				for(int i = 0; i < 8; i++){
					CommandCapture command15 = new CommandCapture(0, 
					"chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+i,
					"chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_"+i);
					try{
						RootTools.getShell(true).add(command15).waitForFinish();
					}
					catch(Exception e){

					}
				}
				
				List<SysCtlDatabaseEntry> sysEntries = db.getAllSysCtlEntries();
				for(SysCtlDatabaseEntry e : sysEntries){
					CommandCapture command16 = new CommandCapture(0, 
					getFilesDir().getPath() + "/busybox sysctl -w " + e.getKey().trim() + "=" + e.getValue().trim());
					try{
						RootTools.getShell(true).add(command16).waitForFinish();
					}
					catch(Exception ex){

					}
				}
				
		
			return "";
		}
		
		@Override
		protected void onPostExecute(String result){
			
			try{
				RootTools.closeAllShells();
			}
			catch(Exception e){

			}
			stopService(new Intent(StartupService.this,StartupService.class));
			
		}
	}	
	

}
