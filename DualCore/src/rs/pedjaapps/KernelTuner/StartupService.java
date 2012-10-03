package rs.pedjaapps.KernelTuner;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class StartupService extends Service
{
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	List<String> frequencies = new ArrayList<String>();
	List<String> freqlist = new ArrayList<String>();
	String freqs;
	@Override
	public void onCreate() {
		super.onCreate();
		readFreqs();
		boot();
		/*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		boolean tempMonitor = sharedPrefs.getBoolean("tempmon", false);
    	if(tempMonitor==true){
    		Intent intent = new Intent(this, TemperatureMonitorService.class);
    		startService(intent);
    		//SharedPreferences.Editor editor = preferences.edit();
    		  //  editor.putBoolean("temp_service_started", true);
    	}*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void boot(){
	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	  String applyonboot = sharedPrefs.getString("boot2", "");
	  if (applyonboot.equals("boot")){
		  apply();
		  
	  }
	}
	
	public void apply(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean cputoggle = sharedPrefs.getBoolean("cputoggle", false);
		

		Process localProcess;
 		try {
				localProcess = Runtime.getRuntime().exec("su");
			
 		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
      localDataOutputStream.writeBytes("mount -t debugfs debugfs /sys/kernel/debug\n");
       localDataOutputStream.writeBytes("exit\n");
        localDataOutputStream.flush();
        localDataOutputStream.close();
        localProcess.waitFor();
        localProcess.destroy();
 		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		  if (cputoggle==true){

	     		try {
					localProcess = Runtime.getRuntime().exec("su");
				
	     		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
	            localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
	            localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
	            localDataOutputStream.writeBytes("echo echo 1 > /sys/devices/system/cpu/cpu1/online\n");
	            localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
	            localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
	            
	            localDataOutputStream.writeBytes("exit\n");
	            localDataOutputStream.flush();
	            localDataOutputStream.close();
	            localProcess.waitFor();
	            localProcess.destroy();
	     		} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		  }
		  String gpu3d = sharedPrefs.getString("gpu3d", "");
		  String gpu2d = sharedPrefs.getString("gpu2d", "");
		 

		  try {
				localProcess = Runtime.getRuntime().exec("su");
			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
         localDataOutputStream.writeBytes("echo " + gpu3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
         localDataOutputStream.writeBytes("exit\n");
         localDataOutputStream.flush();
         localDataOutputStream.close();
         localProcess.waitFor();
         localProcess.destroy();
  		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  
		  try {
				localProcess = Runtime.getRuntime().exec("su");
			
     		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
            localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
            localDataOutputStream.writeBytes("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n");
            localDataOutputStream.writeBytes("echo " + gpu2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk\n");
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localDataOutputStream.close();
            localProcess.waitFor();
            localProcess.destroy();
     		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  
		  String led = sharedPrefs.getString("led", "");
		  
		  
		  

			  try {
					localProcess = Runtime.getRuntime().exec("su");
				
	 		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
	       localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
	       localDataOutputStream.writeBytes("echo " + led + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
	       localDataOutputStream.writeBytes("exit\n");
	        localDataOutputStream.flush();
	        localDataOutputStream.close();
	        localProcess.waitFor();
	        localProcess.destroy();
	 		} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
		  }
		 
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
		  
		  try {
				localProcess = Runtime.getRuntime().exec("su");
			
    		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu0gov + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("echo \"" + cpu0min + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu0max + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu1gov + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("echo \"" + cpu1min + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu1max + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
      
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu2gov + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("echo \"" + cpu2min + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu2max + "\" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu3gov + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
           localDataOutputStream.writeBytes("echo \"" + cpu3min + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n");
           localDataOutputStream.writeBytes("echo \"" + cpu3max + "\" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n");
      
           
           localDataOutputStream.writeBytes("exit\n");
           localDataOutputStream.flush();
           localDataOutputStream.close();
           localProcess.waitFor();
           localProcess.destroy();
    		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	
       String fastcharge = sharedPrefs.getString("fastcharge", "");
		  String vsync = sharedPrefs.getString("vsync", "");
		  String hw = sharedPrefs.getString("hw", "");
		  String backbuf = sharedPrefs.getString("backbuf", "");
		  String mpscroff = sharedPrefs.getString("mpdecisionscroff", "");
		  String cdepth = sharedPrefs.getString("cdepth", "");
		  String freqselected = sharedPrefs.getString("idlefreq", "");
		  String io = sharedPrefs.getString("io", "");
		  String sdcache = sharedPrefs.getString("sdcache", "");
		  
		  String onoff = sharedPrefs.getString("onoff", "");
		  String delaynew = sharedPrefs.getString("delaynew", "");
		  String pausenew = sharedPrefs.getString("pausenew", "");
		  String thruploadnew = sharedPrefs.getString("thruploadnew", "");
		  String thrupmsnew = sharedPrefs.getString("thrupmsnew", "");
		  String thrdownloadnew = sharedPrefs.getString("thrdownloadnew", "");
		  String thrdownmsnew = sharedPrefs.getString("thrdownmsnew", "");
		  String maxfreqselected = sharedPrefs.getString("maxfreqselected", "");
		  String govselected = sharedPrefs.getString("govselected", "");
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
		  String s2wStart = sharedPrefs.getString("s2wStart","");
		  String s2wEnd = sharedPrefs.getString("s2wEnd","");
		  
		  

		  try {
 				localProcess = Runtime.getRuntime().exec("su");
 			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
        localDataOutputStream.writeBytes("echo " + vsync + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
        localDataOutputStream.writeBytes("echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
        localDataOutputStream.writeBytes("echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
        localDataOutputStream.writeBytes("echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n");
        localDataOutputStream.writeBytes("echo " + fastcharge + " > /sys/kernel/fast_charge/force_fast_charge\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
        localDataOutputStream.writeBytes("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n");
			  localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb\n");
        localDataOutputStream.writeBytes("echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb\n");
		localDataOutputStream.writeBytes("echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk0/queue/scheduler\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/scheduler\n");
        localDataOutputStream.writeBytes("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n");
        localDataOutputStream.writeBytes("echo " + io + " > /sys/block/mmcblk1/queue/scheduler\n");
        
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");

        

        localDataOutputStream.writeBytes("echo " + delaynew.trim() + " > /sys/kernel/msm_mpdecision/conf/delay\n");
        localDataOutputStream.writeBytes("echo " + pausenew.trim() + " > /sys/kernel/msm_mpdecision/conf/pause\n");
        localDataOutputStream.writeBytes("echo " + thruploadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
        localDataOutputStream.writeBytes("echo " + thrdownloadnew.trim() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
        localDataOutputStream.writeBytes("echo " + thrupmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
        localDataOutputStream.writeBytes("echo " + thrdownmsnew.trim() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");
        //localDataOutputStream.writeBytes("echo " + freqselected + " > /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n");
        localDataOutputStream.writeBytes( "echo " + "\""+ldt + "\""+" > /sys/kernel/notification_leds/off_timer_multiplier\n");
        localDataOutputStream.writeBytes( "echo " + "\""+s2w + "\""+" > /sys/android_touch/sweep2wake\n");
        localDataOutputStream.writeBytes( "echo " + "\""+s2w + "\""+" > /sys/android_touch/sweep2wake/s2w_switch\n");
        
        localDataOutputStream.writeBytes("echo " + p1freq + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
        localDataOutputStream.writeBytes("echo " + p2freq + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
        localDataOutputStream.writeBytes("echo " + p3freq + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
        localDataOutputStream.writeBytes("echo " + p1low + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
        localDataOutputStream.writeBytes("echo " + p1high + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
        localDataOutputStream.writeBytes("echo " + p2low + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
        localDataOutputStream.writeBytes("echo " + p2high + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
        localDataOutputStream.writeBytes("echo " + p3low + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n");
        localDataOutputStream.writeBytes("echo " + p3high + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n");
        
        localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake_startbutton\n");
		localDataOutputStream.writeBytes("echo "+ s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n");
		localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake_endbutton\n");
		localDataOutputStream.writeBytes("echo "+ s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n");
		
		for(String s : freqlist){
			String temp = sharedPrefs.getString("uv"+s, "");
		  
		    if(!temp.equals("")){
		    	localDataOutputStream.writeBytes("echo " + "\""+temp+"\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
		    }
		}
       
        localDataOutputStream.writeBytes("exit\n");
         localDataOutputStream.flush();
         localDataOutputStream.close();
         localProcess.waitFor();
         localProcess.destroy();
  		} catch (IOException e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			} catch (InterruptedException e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
	}
	
	public void readFreqs()
	{
		 

		try {
			
			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			
			freqs = aBuffer;
			myReader.close();
			freqlist = Arrays.asList(freqs.split("\\s"));
			
		} catch (Exception e) {
			try{
				// Open the file that is the first 
	 			// command line parameter
	 			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
	 			// Get the object of DataInputStream
	 			DataInputStream in = new DataInputStream(fstream);
	 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 			String strLine;
	 			//Read File Line By Line
	 			
	 			while ((strLine = br.readLine()) != null)   {
	 				
	 				String[] delims = strLine.split(" ");
	 				String freq = delims[0];
	 				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

	 				frequencies.add(freq);

	 			}
	 			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	 			boolean ov = sharedPrefs.getBoolean("override", false);
	 			if(ov==true){
	 			Collections.reverse(frequencies);
	 			}
	 			String[] strarray = frequencies.toArray(new String[0]);
	 			frequencies.clear();
	 			System.out.println(frequencies);
	 			StringBuilder builder = new StringBuilder();
	 			for(String s : strarray) {
	 			    builder.append(s);
	 			    builder.append(" ");
	 			}
	 			freqs = builder.toString();
	 			
	 			freqlist = Arrays.asList(freqs.split("\\s"));
	 			
	 			

	 			
	 			in.close();
			}
			catch(Exception ee){
			/**/
			}
		}
		
	}

}
