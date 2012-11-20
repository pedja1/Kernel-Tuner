package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;


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
		 List<String> voltageFreqs = CPUInfo.voltageFreqs();
			
		Process localProcess;
		try
		{
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			
			if (CPUInfo.cpu1Online() == true)
					{
						localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
					}
					if (CPUInfo.cpu2Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
					}
					if (CPUInfo.cpu3Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
					}

					localDataOutputStream.writeBytes("mount -t debugfs debugfs /sys/kernel/debug\n");
			
			//cpu0 min
			if( cpu0min != null && !cpu0min.equals("Unchanged") && !cpu0min.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu0min + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu0min", cpu0min);
				editor.commit();
			}
			
			//cpu0 max
			if( cpu0max != null && !cpu0max.equals("Unchanged") && !cpu0max.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu0max + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu0max", cpu0max);
				editor.commit();
			}
			
			//cpu1min
			if( cpu1min != null && !cpu1min.equals("Unchanged") && !cpu1min.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu1min + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu1min", cpu1min);
				editor.commit();
			}
			//cpu1max
			if( cpu1max != null && !cpu1max.equals("Unchanged") && !cpu1max.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu1max + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu1max", cpu1max);
				editor.commit();
			}

			//cpu2min
			if( cpu2min != null && !cpu2min.equals("Unchanged") && !cpu2min.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu2min + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu2min", cpu2min);
				editor.commit();
			}
			
			//cpu2max
			if( cpu2max != null && !cpu2max.equals("Unchanged") && !cpu2max.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu2max + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu2max", cpu2max);
				editor.commit();
			}
			
			//cpu3min
			if( cpu3min != null && !cpu3min.equals("Unchanged") && !cpu3min.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu3min + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu3min", cpu3min);
				editor.commit();
			}
			
			//cpu3max
			if( cpu3max != null && !cpu3max.equals("Unchanged") && !cpu3max.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo " + cpu3max + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu3max", cpu3max);
				editor.commit();
			}
			
			//cpu0governor
			if( cpu0gov != null && !cpu0gov.equals("Unchanged") && !cpu0gov.equals("") ){
				localDataOutputStream.writeBytes("echo " + cpu0gov + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu0gov", cpu0gov);
				editor.commit();
			}
			
			//cpu1governor
			if( cpu1gov != null && !cpu1gov.equals("Unchanged") && !cpu1gov.equals("") ){
				localDataOutputStream.writeBytes("echo " + cpu1gov + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu1gov", cpu1gov);
				editor.commit();
			}
			
			//cpu2governor
			if( cpu2gov != null && !cpu2gov.equals("Unchanged") && !cpu2gov.equals("") ){
				localDataOutputStream.writeBytes("echo " + cpu2gov + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu2gov", cpu2gov);
				editor.commit();
			}
			
			
			//cpu3governor
			if( cpu3gov != null && !cpu3gov.equals("Unchanged") && !cpu3gov.equals("") ){
				localDataOutputStream.writeBytes("echo " + cpu3gov + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cpu3gov", cpu3gov);
				editor.commit();
			}
			
			//voltage
			if( voltage != null && !voltage.equals("Unchanged") && !voltage.equals("") ){
				Voltage volt = db.getVoltageByName(profile.getVoltage());
				String[] values = volt.getValue().split("\\s");
				
			
			localDataOutputStream
				.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");

			for (int i = 0; i < voltageFreqs.size(); i++)
			{
				//int volt = voltages.get(i) + 12500;
				
					localDataOutputStream
						.writeBytes("echo "
									+ voltageFreqs.get(i)
									+ " "
									+ values[i]
									+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
					
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + values[i]);
					editor.commit();
			}
			}
			
			//mpdecision down
			if( mpdown != null && !mpdown.equals("") ){

				localDataOutputStream.writeBytes("echo " + mpdown + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("thrdownloadnew", mpdown);
				editor.commit();
			}
			
			//mpdecision up
			if( mpup != null && !mpup.equals("") ){
				localDataOutputStream.writeBytes("echo " + mpup + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("thruploadnew", mpup);
				editor.commit();
			}
			
			//gpu2d
			if( gpu2d != null && !gpu2d.equals("Unchanged") && !gpu2d.equals("") ){
				
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");   
				SharedPreferences.Editor editor = preferences.edit();
		  	    editor.putString("gpu2d", gpu2d);
		  	    editor.commit();
			}
			
			//gpu3d
			if( gpu3d != null && !gpu3d.equals("Unchanged") && !gpu3d.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				SharedPreferences.Editor editor = preferences.edit();
		  	    editor.putString("gpu3d", gpu3d);
		  	    editor.commit();
			}
			
			//buttons
			if( buttons != null && !buttons.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
			localDataOutputStream.writeBytes("echo "+ buttons + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("led", buttons);
	  	    editor.commit();
			}
			
			//vsync
			if( vsync==0 ){
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
				localDataOutputStream.writeBytes("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("echo " + 4 + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("vsync", "0");
				editor.putString("hw", "0");
				editor.putString("backbuf", "0");
				editor.commit();
			}
			else if(vsync==1){
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
				localDataOutputStream.writeBytes("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("echo " + 3 + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("vsync", "1");
				editor.putString("hw", "1");
				editor.putString("backbuf", "3");
				editor.commit();
			}
			
			//fcharge
			localDataOutputStream.writeBytes("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n");
		localDataOutputStream.writeBytes("echo " + fcharge+ " > /sys/kernel/fast_charge/force_fast_charge\n");
		SharedPreferences.Editor editor = preferences.edit();
  	    editor.putString("fastcharge", String.valueOf(fcharge));
  	    editor.commit();
			
			//cdepth
			if( cdepth != null && !cdepth.equals("Unchanged") && !cdepth.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
			localDataOutputStream.writeBytes("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n");
			
	  	    editor.putString("cdepth", cdepth);
	  	    editor.commit();
			}
			
			//io
			if( io != null && !io.equals("Unchanged") && !io.equals("") ){
				localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk0/queue/scheduler\n");
			localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/scheduler\n");
			localDataOutputStream.writeBytes("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n");
			localDataOutputStream.writeBytes("echo " + io+ " > /sys/block/mmcblk1/queue/scheduler\n");
			
	  	    editor.putString("io", io);
	  	    editor.commit();
			}
			
			//sd
			if( sdcache != null && sdcache!=0){
				localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n");
			localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n");
			localDataOutputStream.writeBytes("chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
			localDataOutputStream.writeBytes("echo " + sdcache+ " > /sys/block/mmcblk1/queue/read_ahead_kb\n");
			localDataOutputStream.writeBytes("echo " + sdcache+ " > /sys/block/mmcblk0/queue/read_ahead_kb\n");
			localDataOutputStream.writeBytes("echo " + sdcache+ " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
			editor.putString("sdcache", String.valueOf(sdcache));
	  	    editor.commit();
			}
			
			//s2w
			if( s2w!=null ){
				localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake\n");
				localDataOutputStream.writeBytes("echo " + s2w + " > /sys/android_touch/sweep2wake\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake/s2w_switch\n");
				localDataOutputStream.writeBytes("echo " + s2w + " > /sys/android_touch/sweep2wake/s2w_switch\n");
				editor.putString("s2w", String.valueOf(s2w));
		  	    editor.commit();
			}
			
			if (CPUInfo.cpu1Online() == true)
			{
				localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
			}
			if (CPUInfo.cpu2Online() == true)
			{
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu2/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
			}
			if (CPUInfo.cpu3Online() == true)
			{
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu3/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
			}

			localDataOutputStream.writeBytes("exit\n");
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
		}

		

		return "";
	}
	@Override
	protected void onPostExecute(String result){
		
		Toast.makeText(context, context.getResources().getString(R.string.profile)+"\""+ profile.getName()+"\""+" " + context.getResources().getString(R.string.applied)  , Toast.LENGTH_LONG).show();
		
	}
}	

