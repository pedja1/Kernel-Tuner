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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.DatabaseHandler;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.model.Profile;

//import rs.pedjaapps.KernelTuner.model.Voltage;


public class ProfileApplier extends AsyncTask<String, Void, String>
{

	Context context;
	DatabaseHandler db;
	Profile profile;

	public ProfileApplier(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		db = new DatabaseHandler(context);
	}

	

	SharedPreferences preferences;
	
	@Override
	protected String doInBackground(String... args)
	{

		profile = db.getProfileByName(args[0]);
		String cpu0min = profile.getCpu0min();
		String cpu0max = profile.getCpu0max();
		String cpu1min = profile.getCpu1min();
		String cpu1max = profile.getCpu1max();
		String cpu2min = profile.getCpu2min();
		String cpu2max = profile.getCpu2max();
		String cpu3min = profile.getCpu3min();
		String cpu3max = profile.getCpu3max();
		
		 String cpu0gov = profile.getCpu0gov();
		 String cpu1gov = profile.getCpu1gov();
		 String cpu2gov = profile.getCpu2gov();
		 String cpu3gov = profile.getCpu3gov();
		 
		 String mpup = profile.getMtu();
		 String mpdown = profile.getMtd();
		 
		 String gpu2d = profile.getGpu2d();
		 String gpu3d = profile.getGpu3d();
		 
		 String voltage = profile.getVoltage();

		 String buttons = profile.getButtonsLight();
		 int vsync = profile.getVsync();
		 int fcharge = profile.getFcharge();
		 String cdepth = profile.getCdepth();
		 String io = profile.getIoScheduler();
		 Integer sdcache = profile.getSdcache();

		 Integer s2w = profile.getSweep2wake();
		 /*List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
			
			List<String> voltageFreqs =  new ArrayList<String>();
			
			for(IOHelper.VoltageList v: voltageList){
				voltageFreqs.add((v.getFreq()));
			}*/
		
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            if (IOHelper.cpu1Exists() == true)
				{
					stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
				}
				if (IOHelper.cpu2Exists() == true)
				{
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu2/online\n").getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/online\n").getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
				}
				if (IOHelper.cpu3Exists() == true)
				{
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu3/online\n").getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/online\n").getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
				}

				stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n").getBytes());

		//cpu0 min
		if( cpu0min != null && !cpu0min.equals("Unchanged") && !cpu0min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu0min + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0min", cpu0min);
			editor.commit();
		}
		
		//cpu0 max
		if( cpu0max != null && !cpu0max.equals("Unchanged") && !cpu0max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu0max + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0max", cpu0max);
			editor.commit();
		}
		
		//cpu1min
		if( cpu1min != null && !cpu1min.equals("Unchanged") && !cpu1min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu1min + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1min", cpu1min);
			editor.commit();
		}
		//cpu1max
		if( cpu1max != null && !cpu1max.equals("Unchanged") && !cpu1max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu1max + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1max", cpu1max);
			editor.commit();
		}

		//cpu2min
		if( cpu2min != null && !cpu2min.equals("Unchanged") && !cpu2min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu2min + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu2min", cpu2min);
			editor.commit();
		}
		
		//cpu2max
		if( cpu2max != null && !cpu2max.equals("Unchanged") && !cpu2max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu2max + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu2max", cpu2max);
			editor.commit();
		}
		
		//cpu3min
		if( cpu3min != null && !cpu3min.equals("Unchanged") && !cpu3min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu3min + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu3min", cpu3min);
			editor.commit();
		}
		
		//cpu3max
		if( cpu3max != null && !cpu3max.equals("Unchanged") && !cpu3max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu3max + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu3max", cpu3max);
			editor.commit();
		}
		
		//cpu0governor
		if( cpu0gov != null && !cpu0gov.equals("Unchanged") && !cpu0gov.equals("") ){
			stdin.write(("echo " + cpu0gov + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0gov", cpu0gov);
			editor.commit();
		}
		
		//cpu1governor
		if( cpu1gov != null && !cpu1gov.equals("Unchanged") && !cpu1gov.equals("") ){
			stdin.write(("echo " + cpu1gov + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1gov", cpu1gov);
			editor.commit();
		}
		
		//cpu2governor
		if( cpu2gov != null && !cpu2gov.equals("Unchanged") && !cpu2gov.equals("") ){
			stdin.write(("echo " + cpu2gov + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu2gov", cpu2gov);
			editor.commit();
		}
		
		
		//cpu3governor
		if( cpu3gov != null && !cpu3gov.equals("Unchanged") && !cpu3gov.equals("") ){
			stdin.write(("echo " + cpu3gov + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu3gov", cpu3gov);
			editor.commit();
		}
		
		//voltage
		/*if( voltage != null && !voltage.equals("Unchanged") && !voltage.equals("") ){
			Voltage volt = db.getVoltageByName(profile.getVoltage());
			String[] values = volt.getValue().split("\\s");
			
		
		stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n").getBytes());
		int voltageFreqsSize = voltageFreqs.size();
		for (int i = 0; i < voltageFreqsSize; i++)
		{
			//int volt = voltages.get(i) + 12500;
			
				stdin.write(("echo "
								+ voltageFreqs.get(i)
								+ " "
								+ values[i]
								+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n").getBytes());
				
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + values[i]);
				editor.commit();
		}
		}*/
		
		//mpdecision down
		if( mpdown != null && !mpdown.equals("") ){

			stdin.write(("echo " + mpdown + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("thrdownloadnew", mpdown);
			editor.commit();
		}
		
		//mpdecision up
		if( mpup != null && !mpup.equals("") ){
			stdin.write(("echo " + mpup + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("thruploadnew", mpup);
			editor.commit();
		}
		
		//gpu2d
		if( gpu2d != null && !gpu2d.equals("Unchanged") && !gpu2d.equals("") ){
			
			stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
			stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
			stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
			stdin.write(("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());   
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("gpu2d", gpu2d);
	  	    editor.commit();
		}
		
		//gpu3d
		if( gpu3d != null && !gpu3d.equals("Unchanged") && !gpu3d.equals("") ){
			stdin.write(("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
			stdin.write(("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("gpu3d", gpu3d);
	  	    editor.commit();
		}
		
		//buttons
		if( buttons != null && !buttons.equals("") ){
			stdin.write(("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
		stdin.write(("echo "+ buttons + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
		SharedPreferences.Editor editor = preferences.edit();
  	    editor.putString("led", buttons);
  	    editor.commit();
		}
		
		//vsync
		if( vsync==0 ){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			stdin.write(("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("echo " + 4 + " > /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vsync", "0");
			editor.putString("hw", "0");
			editor.putString("backbuf", "0");
			editor.commit();
		}
		else if(vsync==1){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			stdin.write(("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("echo " + 3 + " > /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vsync", "1");
			editor.putString("hw", "1");
			editor.putString("backbuf", "3");
			editor.commit();
		}
		
		//fcharge
		stdin.write(("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
	stdin.write(("echo " + fcharge+ " > /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
	SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("fastcharge", String.valueOf(fcharge));
	    editor.commit();
		
		//cdepth
		if( cdepth != null && !cdepth.equals("Unchanged") && !cdepth.equals("") ){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
		stdin.write(("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
		
  	    editor.putString("cdepth", cdepth);
  	    editor.commit();
		}
		
		//io
		if( io != null && !io.equals("Unchanged") && !io.equals("") ){
			stdin.write(("chmod 777 /sys/block/mmcblk0/queue/scheduler\n").getBytes());
		stdin.write(("chmod 777 /sys/block/mmcblk1/queue/scheduler\n").getBytes());
		stdin.write(("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n").getBytes());
		stdin.write(("echo " + io+ " > /sys/block/mmcblk1/queue/scheduler\n").getBytes());
		
  	    editor.putString("io", io);
  	    editor.commit();
		}
		
		//sd
		if( sdcache != null && sdcache!=0){
			stdin.write(("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
		stdin.write(("chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n").getBytes());
		stdin.write(("chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/block/mmcblk0/queue/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n").getBytes());
		editor.putString("sdcache", String.valueOf(sdcache));
  	    editor.commit();
		}
		
		//s2w
		if( s2w!=null ){
			stdin.write(("chmod 777 /sys/android_touch/sweep2wake\n").getBytes());
			stdin.write(("echo " + s2w + " > /sys/android_touch/sweep2wake\n").getBytes());
			stdin.write(("chmod 777 /sys/android_touch/sweep2wake/s2w_switch\n").getBytes());
			stdin.write(("echo " + s2w + " > /sys/android_touch/sweep2wake/s2w_switch\n").getBytes());
			if(s2w!=3){
			//editor.putString("s2w", String.valueOf(s2w));
			}
	  	    editor.commit();
		}
		
		if (IOHelper.cpu1Exists() == true)
		{
			stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
		}
		if (IOHelper.cpu2Exists() == true)
		{
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
		}
		if (IOHelper.cpu3Exists() == true)
		{
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
		}
	            stdin.write("exit\n".getBytes());
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
				process.waitFor();
				process.destroy();

	        } catch (Exception ex) {
	        }

		return "";
	}
	@Override
	protected void onPostExecute(String result){
		if(preferences.getBoolean("profile_notifications",true)){
		Toast.makeText(context, context.getResources().getString(R.string.profile)+"\""+ profile.getName()+"\""+" " + context.getResources().getString(R.string.applied)  , Toast.LENGTH_LONG).show();
		}
	}
}	

