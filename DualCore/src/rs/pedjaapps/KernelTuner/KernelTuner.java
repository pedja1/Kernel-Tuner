package rs.pedjaapps.KernelTuner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Button;

import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;


import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.ads.*;

import rs.pedjaapps.KernelTuner.R; 
import android.widget.*;
import android.content.pm.*;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;


//EndImports

public class KernelTuner extends Activity {

	private TextView batteryLevel;
	private TextView batteryTemp;
	private TextView cputemptxt;
	  private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent intent) {
	      // TODO Auto-generated method stub
	    	
	    	
	      int level = intent.getIntExtra("level", 0);
	      Double temperature = (double) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10;
	      batteryTemp.setText(String.valueOf(temperature) + "°C");
	      if(temperature<40){
	    	  batteryTemp.setTextColor(Color.GREEN);
	    	  battTempWarningStop();
	      }
	      else if(temperature>45 && temperature<55){
	    	  batteryTemp.setTextColor(Color.YELLOW);
	    	  battTempWarningStop();
	      }
	      else if(temperature>55 && temperature < 60 ){
	    	  batteryTemp.setTextColor(Color.RED);
	    	  battTempWarningStop();
	      }
	      else if(temperature>60){
	    	  Log.e("Battery warning","start animation");
	    	  batteryTemp.setTextColor(Color.RED);
	    	  battTempWarning();

	      }
	      batteryLevel.setText(String.valueOf(level) + "%");
	      if(level<15 && level>5){
	    	  batteryLevel.setTextColor(Color.RED);
	    	  battLevelWarningStop();
	      }
	      else if(level>15 && level<=30){
	    	  batteryLevel.setTextColor(Color.MAGENTA);
	    	  battLevelWarningStop();
	      }
	      else if(level>30 ){
	    	  batteryLevel.setTextColor(Color.GREEN);
	    	  battLevelWarningStop();
	      }
	      else if(level<5){
	    	  batteryLevel.setTextColor(Color.RED);
	    	  battLevelWarning();
	    	  
	      }
	    }
	  };
	
boolean thread = true;
private Button button2;
public String iscVa = "offline";
public String iscVa2 = "offline";
public String governors;
public String governorscpu1;
public String curentgovernorcpu0;
public String curentgovernorcpu1;
public String curentgovernorcpu2;
public String curentgovernorcpu3;
private TextView cpu1;
public String led;
SeekBar mSeekBar;
TextView progresstext;
public String cpu0freqs;
public String cpu1freqs;
public String cpu2freqs;
public String cpu3freqs;
public String cpu0max = "        ";
public String cpu1max = "        ";
public String cpu0min = "        ";
public String cpu1min = "        ";
public String cpu2max = "        ";
public String cpu3max = "        ";
public String cpu2min = "        ";
public String cpu3min = "        ";
public String gpu2d = "        ";
public String gpu3d = "       ";
public int countcpu0;
public int countcpu1;
public String vsync = " ";
public String fastcharge = " ";
public String out;
public String cdepth ;
public String kernel = "     ";
public String remoteversion;
public String schedulers;
public String scheduler;
public String sdcache;
public String curentidlefreq;
public String delay;
public String pause;
public String thrupload;
public String thrupms;
public String thrdownload;
public String thrdownms;
public boolean cpu1check;
public String sdcacheinfo;
public String ioschedulerinfo;
List<String> list;
public String ldt;
public String freqcpu2;
public String freqcpu3;

public String p1low;
public String p1high;
public String p2low;
public String p2high;
public String p3low;
public String p3high;
public String p1freq;
public String p2freq;
public String p3freq;

public String cputemp;

List<String> frequencies3 = new ArrayList<String>();
List<String> frequencies4 = new ArrayList<String>();
List<String> frequencies5 = new ArrayList<String>();
	public String[] delims;

String[] freqs;
List<String> vdd = new ArrayList<String>();

List<String> frequencies = new ArrayList<String>();
List<String> frequencies2 = new ArrayList<String>();
List<Integer> perint = new ArrayList<Integer>();
String[] freqarray;
String[] freqarray2;
String[] vddarray;

public String govs;
public String currentscrofffreq;
public String currentscroffgov;
public String govselected;
public String maxfreqselected;
public String onoff;
public String scroff_profile;
public String mpdecisionidle;
public String version;
public String changelog;
//public String[] cpu0freqslist;
public List<String> cpu0freqslist;
public List<String> cpu1freqslist;
public List<String> cpu2freqslist;
public List<String> cpu3freqslist;
//private ToggleButton cpu1toggle;
public SharedPreferences preferences;
private ProgressDialog pd = null;
private Object data = null;
public String s2w;


//set on boot


///public String[] MyStringAray;// = {"1","2",};
Handler mHandler = new Handler();


//EndOfGlobalVariables

public static boolean isDownloadManagerAvailable(Context context) {
	try {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
																				   PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	} catch (Exception e) {
		return false;
	}
}

private class updateCheck extends AsyncTask<String, Void, Object> {


	protected Object doInBackground(String... args) {
		Log.i("DualCore", "Check for new version");

		
		try {
			// Create a URL for the desired page
			URL url = new URL("http://dualcore.netii.net/fm/DualCore/version");

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
			remoteversion = in.readLine();
			in.close();
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			remoteversion=null;
		}
	
		try {
			// Create a URL for the desired page
			URL url = new URL("http://dualcore.netii.net/fm/DualCore/changelog");

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = in.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}

  			changelog = aBuffer;
		//	changelog = in.readLine();
			in.close();

		} catch (MalformedURLException e) {
		} catch (IOException e) {
			changelog=null;
		}
		
   	

		return "";
    }
	
	
    protected void onPreExecute() {
        super.onPreExecute();
      //  DualCore.this.pd = ProgressDialog.show(DualCore.this, "Working..", "Checking for updates...", true, false );
	//	pd.setCancelable(true);
	
		pd = ProgressDialog.show(
			KernelTuner.this,
			"Working...",
			"Checking for updates...",
			true,
			true,
			new DialogInterface.OnCancelListener(){
			
				public void onCancel(DialogInterface dialog) {
					updateCheck.this.cancel(true);
				//	finish();
				}
			}
        );
    }


    protected void onPostExecute(Object result) {
	
        KernelTuner.this.data = result;
        KernelTuner.this.pd.dismiss();
        
		try {
		PackageInfo	pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		version = pInfo.versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        if (remoteversion!= null && !remoteversion.equals(version)){
        	AlertDialog.Builder builder = new AlertDialog.Builder(
                    KernelTuner.this);

    builder.setTitle("New Version Available");

    builder.setMessage("New version of the application is available!\n\n" +
    		"\"Kernel Tuner-"+ remoteversion+"\"\n\n"+
			"Changelog:\n"+ changelog + "\n\n"+
    		"Would you like to download?");

    builder.setIcon(R.drawable.icon);

    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	download();
            }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
      
        }
});

    AlertDialog alert = builder.create();
			
			alert.show();
		
		
		}
		else if(remoteversion==null){
			Toast.makeText(getApplicationContext(), "Problem connecting to server", Toast.LENGTH_LONG).show();
		}
        else{
        	Toast.makeText(getApplicationContext(), "Your version is up-to-date", Toast.LENGTH_LONG).show();
        }


		

    }

	}

private class mountDebugFs extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
 
     }

	}


private class cpu1Toggle extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
         File file = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor");
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
         localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
         localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
         localDataOutputStream.writeBytes("exit\n");
         localDataOutputStream.flush();
         localDataOutputStream.close();
         localProcess.waitFor();
         localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
     	SharedPreferences.Editor editor = preferences.edit();
	    editor.putBoolean("cputoggle", false);
	    editor.commit();

     	}

     	catch(FileNotFoundException e){
     		//enable cpu1
     			
  
     		Process localProcess;
     		try {
				localProcess = Runtime.getRuntime().exec("su");
			
     		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
            localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
            localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
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
     		SharedPreferences.Editor editor = preferences.edit();
    	    editor.putBoolean("cputoggle", true);
    	    editor.commit();
     	}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
    
             KernelTuner.this.pd.dismiss();
         
     }

	}

private class cpu2Toggle extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
         File file = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor");
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/online\n");
         localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu2/online\n");
         localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
         localDataOutputStream.writeBytes("exit\n");
         localDataOutputStream.flush();
         localDataOutputStream.close();
         localProcess.waitFor();
         localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
     	SharedPreferences.Editor editor = preferences.edit();
	    editor.putBoolean("cpu2toggle", false);
	    editor.commit();

     	}

     	catch(FileNotFoundException e){
     		//enable cpu1
     			
  
     		Process localProcess;
     		try {
				localProcess = Runtime.getRuntime().exec("su");
			
     		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
            localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu2/online\n");
            localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu2/online\n");
            localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu2/online\n");
            localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
            
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
     		SharedPreferences.Editor editor = preferences.edit();
    	    editor.putBoolean("cpu2toggle", true);
    	    editor.commit();
     	}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
    
             KernelTuner.this.pd.dismiss();
         
     }

	}

private class cpu3Toggle extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
         File file = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor");
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/online\n");
         localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu3/online\n");
         localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
         localDataOutputStream.writeBytes("exit\n");
         localDataOutputStream.flush();
         localDataOutputStream.close();
         localProcess.waitFor();
         localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
     	SharedPreferences.Editor editor = preferences.edit();
	    editor.putBoolean("cpu3toggle", false);
	    editor.commit();

     	}

     	catch(FileNotFoundException e){
     		//enable cpu1
     			
  
     		Process localProcess;
     		try {
				localProcess = Runtime.getRuntime().exec("su");
			
     		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
            localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu3/online\n");
            localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu3/online\n");
            localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu3/online\n");
            localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
            
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
     		SharedPreferences.Editor editor = preferences.edit();
    	    editor.putBoolean("cpu3toggle", true);
    	    editor.commit();
     	}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
    
             KernelTuner.this.pd.dismiss();
         
     }

	}
private class initdApplyCpuGpuMisc extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
		
		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("busybox mount -o remount,rw /system\n");
			localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99dccputweaks /system/etc/init.d\n");
			localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99dccputweaks\n");
			localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99dcgputweaks /system/etc/init.d\n");
			localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99dcgputweaks\n");
			localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99dcmisctweaks /system/etc/init.d\n");
			localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99dcmisctweaks\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
        
     }

	}

private class initdApplyCpu1Off extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
	
		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99dccpu1disable /system/etc/init.d\n");
			localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99dccpu1disable\n");
		
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
  
     }

	}

private class initdApplyCpu1forced extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");

		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99dccpu1online /system/etc/init.d\n");
			localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99dccpu1online\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;          
         
     }

	}

private class rmInitd extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");

		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
		
			localDataOutputStream.writeBytes("rm /system/etc/init.d/99dccputweaks\n");
		
			localDataOutputStream.writeBytes("rm /system/etc/init.d/99dcgputweaks\n");
			localDataOutputStream.writeBytes("rm /system/etc/init.d/99dcmisctweaks\n");
			localDataOutputStream.writeBytes("rm /system/etc/init.d/99dccpu1online\n");
			localDataOutputStream.writeBytes("rm /system/etc/init.d/99dccpu1disable\n");
			
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         KernelTuner.this.data = result;
        
     }

	}


private class info extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         try {
 			
 			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq");
 			FileInputStream fIn = new FileInputStream(myFile);	
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}
 			
 			cpu0min = aBuffer.trim();
 			myReader.close();
 			
 		} catch (Exception e) {
 			cpu0min="err";
 		}
         
         try {
  			
  			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
  			FileInputStream fIn = new FileInputStream(myFile);	
  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}
  			
  			cpu0max = aBuffer.trim();
  			myReader.close();
  			
  		} catch (Exception e) {
  			cpu0max="err";
  		}
         
         try {
  			
  			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq");
  			FileInputStream fIn = new FileInputStream(myFile);
		
  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}
  			
  			cpu1min = aBuffer.trim();
  			myReader.close();
  			
  		} catch (Exception e) {
  			cpu1min ="err";
  		}
         
         try {
  			
  			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq");
  			FileInputStream fIn = new FileInputStream(myFile);		
  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}
  			
  			cpu1max = aBuffer.trim();
  			myReader.close();
  			
  		} catch (Exception e) {
  			cpu1max = "err";
  		}
         
         try {
   			
   			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
   			FileInputStream fIn = new FileInputStream(myFile);

   			BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
   			String aDataRow = "";
   			String aBuffer = "";
   			while ((aDataRow = myReader.readLine()) != null) {
   				aBuffer += aDataRow + "\n";
   			}
   			
   			curentgovernorcpu0 = aBuffer.trim();
   			myReader.close();
   			
   		} catch (Exception e) {
   			curentgovernorcpu0="err";
   		}
         
         try {
    			
    			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor");
    			FileInputStream fIn = new FileInputStream(myFile);
	
    			BufferedReader myReader = new BufferedReader(
    					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null) {
    				aBuffer += aDataRow + "\n";
    			}
    			
    			curentgovernorcpu1 = aBuffer.trim();
    			myReader.close();
    			
    		} catch (Exception e) {
			curentgovernorcpu1 = "err";
    		}

         try {
  			
  			File myFile = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq");
  			FileInputStream fIn = new FileInputStream(myFile);	
  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}
  			
  			cpu2min = aBuffer.trim();
  			myReader.close();
  			
  		} catch (Exception e) {
  			cpu2min="err";
  		}
          
          try {
   			
   			File myFile = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq");
   			FileInputStream fIn = new FileInputStream(myFile);	
   			BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
   			String aDataRow = "";
   			String aBuffer = "";
   			while ((aDataRow = myReader.readLine()) != null) {
   				aBuffer += aDataRow + "\n";
   			}
   			
   			cpu2max = aBuffer.trim();
   			myReader.close();
   			
   		} catch (Exception e) {
   			cpu2max="err";
   		}
          
          try {
   			
   			File myFile = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq");
   			FileInputStream fIn = new FileInputStream(myFile);
 		
   			BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
   			String aDataRow = "";
   			String aBuffer = "";
   			while ((aDataRow = myReader.readLine()) != null) {
   				aBuffer += aDataRow + "\n";
   			}
   			
   			cpu3min = aBuffer.trim();
   			myReader.close();
   			
   		} catch (Exception e) {
   			cpu3min ="err";
   		}
          
          try {
   			
   			File myFile = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq");
   			FileInputStream fIn = new FileInputStream(myFile);		
   			BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
   			String aDataRow = "";
   			String aBuffer = "";
   			while ((aDataRow = myReader.readLine()) != null) {
   				aBuffer += aDataRow + "\n";
   			}
   			
   			cpu3max = aBuffer.trim();
   			myReader.close();
   			
   		} catch (Exception e) {
   			cpu3max = "err";
   		}
          
          try {
    			
    			File myFile = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor");
    			FileInputStream fIn = new FileInputStream(myFile);

    			BufferedReader myReader = new BufferedReader(
    					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null) {
    				aBuffer += aDataRow + "\n";
    			}
    			
    			curentgovernorcpu2 = aBuffer.trim();
    			myReader.close();
    			
    		} catch (Exception e) {
    			curentgovernorcpu2="err";
    		}
          
          try {
     			
     			File myFile = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor");
     			FileInputStream fIn = new FileInputStream(myFile);
 	
     			BufferedReader myReader = new BufferedReader(
     					new InputStreamReader(fIn));
     			String aDataRow = "";
     			String aBuffer = "";
     			while ((aDataRow = myReader.readLine()) != null) {
     				aBuffer += aDataRow + "\n";
     			}
     			
     			curentgovernorcpu3 = aBuffer.trim();
     			myReader.close();
     			
     		} catch (Exception e) {
 			curentgovernorcpu3 = "err";
     		}
         
         try {
        		String aBuffer = "";
        	 			File myFile = new File("/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
        	 			FileInputStream fIn = new FileInputStream(myFile);
        	 			BufferedReader myReader = new BufferedReader(
        	 					new InputStreamReader(fIn));
        	 			String aDataRow = "";
        	 			while ((aDataRow = myReader.readLine()) != null) {
        	 				aBuffer += aDataRow + "\n";
        	 			}

        	 			led = aBuffer.trim();
        	 			myReader.close();
        	 	
        	 		} catch (Exception e) {
        	 			led="err";
        	 		}

         try {
  			
  			File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
  			FileInputStream fIn = new FileInputStream(myFile);

  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}

  			gpu3d = aBuffer.trim();
  			myReader.close();
  			

  			
  		} catch (Exception e) {
  			gpu3d = "err";
  			
  		}
         
         try {
   			
   			File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
   			FileInputStream fIn = new FileInputStream(myFile);
   			BufferedReader myReader = new BufferedReader(
   					new InputStreamReader(fIn));
   			String aDataRow = "";
   			String aBuffer = "";
   			while ((aDataRow = myReader.readLine()) != null) {
   				aBuffer += aDataRow + "\n";
   			}

   			gpu2d = aBuffer.trim();
   			
   			myReader.close();
 	
   		} catch (Exception e) {
			gpu2d = "err";
   		}
   		
         try {
        		String aBuffer = "";
        	 			File myFile = new File("/sys/kernel/fast_charge/force_fast_charge");
        	 			FileInputStream fIn = new FileInputStream(myFile);
        	 			BufferedReader myReader = new BufferedReader(
        	 					new InputStreamReader(fIn));
        	 			String aDataRow = "";
        	 			while ((aDataRow = myReader.readLine()) != null) {
        	 				aBuffer += aDataRow + "\n";
        	 			}

        	 			fastcharge = aBuffer.trim();
        	 			myReader.close();
        	 	
        	 		} catch (Exception e) {
						fastcharge = "err";
        	 		}
         
         try {
        		String aBuffer = "";
        	 			File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
        	 			FileInputStream fIn = new FileInputStream(myFile);
        	 			BufferedReader myReader = new BufferedReader(
        	 					new InputStreamReader(fIn));
        	 			String aDataRow = "";
        	 			while ((aDataRow = myReader.readLine()) != null) {
        	 				aBuffer += aDataRow + "\n";
        	 			}

        	 			vsync = aBuffer.trim();
        	 			myReader.close();
        	 	
        	 		} catch (Exception e) {
						vsync ="err";
        	 		}


         
         try {
 			
 			File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
 			FileInputStream fIn = new FileInputStream(myFile);

 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			cdepth = aBuffer.trim();
 			myReader.close();
 			Log.d("done",cdepth);

 		} catch (IOException e) {
 			cdepth = "err";
			System.out.println(cdepth);
 			Log.d("failed",cdepth);
 		}
         
         try {
  			
  			File myFile = new File("/proc/version");
  			FileInputStream fIn = new FileInputStream(myFile);	
  			BufferedReader myReader = new BufferedReader(
  					new InputStreamReader(fIn));
  			String aDataRow = "";
  			String aBuffer = "";
  			while ((aDataRow = myReader.readLine()) != null) {
  				aBuffer += aDataRow + "\n";
  			}

  			kernel = aBuffer.trim();
  			myReader.close();
			
  		} catch (Exception e) {
  			kernel = "Kernel version file not found";
  			
  		}
         
         try {
 			
 			File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
 			FileInputStream fIn = new FileInputStream(myFile);
 		
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			schedulers = aBuffer;
 			myReader.close();
 			/*String[] schedaray = schedulers.split(" ");
 			int schedlength = schedaray.length;
 			List<String> wordList = Arrays.asList(schedaray); 
 			int index = wordList.indexOf(curentfreq);
 			int index2 = wordList.indexOf(curentfreqcpu1);
 				scheduler = */
 			//String between = schedulers.split("]|[")[1];
 			scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
 			scheduler.trim();
 			schedulers = schedulers.replace("[", "");
 			schedulers = schedulers.replace("]", "");

 		} catch (Exception e) {
 			schedulers = "err";
 			scheduler="err";
 		}
         
         try {
 			
 			File myFile = new File("/sys/devices/virtual/bdi/179:0/read_ahead_kb");
 			FileInputStream fIn = new FileInputStream(myFile);
 		
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			sdcache = aBuffer.trim();
 			myReader.close();
 					
 		} catch (Exception e) {
 			sdcache = "err";
 			
 		}
         
         try {
 			
 			File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_idle_freq");
 			FileInputStream fIn = new FileInputStream(myFile);
 		
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			curentidlefreq = aBuffer.trim();
 			myReader.close();
 						
 		} catch (Exception e) {
 			curentidlefreq = "err";
 			
 		}
         
         try {
        		String aBuffer = "";
        	 			File myFile = new File("/sys/kernel/msm_mpdecision/conf/do_scroff_single_core");
        	 			FileInputStream fIn = new FileInputStream(myFile);
        	 			BufferedReader myReader = new BufferedReader(
        	 					new InputStreamReader(fIn));
        	 			String aDataRow = "";
        	 			while ((aDataRow = myReader.readLine()) != null) {
        	 				aBuffer += aDataRow + "\n";
        	 			}

        	 			mpdecisionidle = aBuffer.trim();
        	 			myReader.close();
        	 	
        	 		} catch (Exception e) {
						mpdecisionidle="err";
        	 		}

         
         try {

 			File myFile = new File("/sys/kernel/msm_mpdecision/conf/delay");
 			FileInputStream fIn = new FileInputStream(myFile);

 			BufferedReader myReader = new BufferedReader(
 				new InputStreamReader(fIn));
 			String aDataRow = "";
 			String aBuffer = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			delay = aBuffer;
 			myReader.close();

 		} catch (Exception e) {
 			delay="err";
 			
 		}
 		
 		
 	   try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/pause");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   pause = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {
 		   pause ="err" ;
 		   

 	   }
 	   
 	      try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_up");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   thrupload = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {
 		   thrupload="err";
 		  
 	   }
 	   
 	     try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_up");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   thrupms = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {
 		   thrupms="err";
 		   
 	   }
 	   
 	    try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_down");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   thrdownms = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {
 		   thrdownms="err";
 		  
 	   }
 	   
 	   try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_down");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   thrdownload = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {
 		   thrdownload="err";
 		   
 	   }
 	   
 	   try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_scroff_gov");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   currentscroffgov = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {

 		   currentscroffgov="err";
 	   }
 	   
 	   try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_scroff_freq");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   currentscrofffreq = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {

 		   currentscrofffreq="err";
 	   }
 	   
 	   try {

 		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/scroff_profile");
 		   FileInputStream fIn = new FileInputStream(myFile);

 		   BufferedReader myReader = new BufferedReader(
 			   new InputStreamReader(fIn));
 		   String aDataRow = "";
 		   String aBuffer = "";
 		   while ((aDataRow = myReader.readLine()) != null) {
 			   aBuffer += aDataRow + "\n";
 		   }

 		   scroff_profile = aBuffer;
 		   myReader.close();

 	   } catch (Exception e) {

 		   scroff_profile="err";
 	   }
	   
 	  try {
			
			File myFile = new File("/sys/kernel/notification_leds/off_timer_multiplier");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			ldt = aBuffer.trim();
			myReader.close();
					
		} catch (Exception e) {
			
			ldt="266";
		}
 	  
 	 try {
			
			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_freq");
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			p1freq = aBuffer.trim();
			myReader.close();
						
		} catch (Exception e) {
			
			
		}
 	 
 	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p2freq = aBuffer.trim();
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
 	
 	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p3freq = aBuffer.trim();
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
 	
 	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p3freq = aBuffer.trim();
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
 	
	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_high");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p1high = aBuffer;
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_low");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p2low = aBuffer;
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
	
	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_high");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p2high = aBuffer;
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
	
	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_low");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p3low = aBuffer;
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
	
	try {
		
		File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_high");
		FileInputStream fIn = new FileInputStream(myFile);
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		p3high = aBuffer;
		myReader.close();
					
	} catch (Exception e) {
		
		
	}
	
	try {

		File myFile = new File(
				"/sys/android_touch/sweep2wake");
		FileInputStream fIn = new FileInputStream(myFile);

		BufferedReader myReader = new BufferedReader(new InputStreamReader(
				fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		s2w = aBuffer.trim();
		
		myReader.close();

	} catch (Exception e) {

		try {

			File myFile = new File(
					"/sys/android_touch/sweep2wake/s2w_switch");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			s2w = aBuffer.trim();
			
			myReader.close();

		} catch (Exception e2) {

			s2w="err";
		}
	}
	
	
	
	

return "";
    }

    protected void onPostExecute(Object result) {
        // Pass the result data back to the main activity
    	TextView cpu0mintxt = (TextView)findViewById(R.id.textView11);
    	TextView cpu0mintxte = (TextView)findViewById(R.id.textView2);
    	if(!cpu0min.equals("err")){
    	cpu0mintxt.setText(cpu0min.substring(0, cpu0min.length()-3)+"Mhz");
    	cpu0mintxt.setVisibility(View.VISIBLE);
		cpu0mintxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu0mintxt.setVisibility(View.GONE);
    		cpu0mintxte.setVisibility(View.GONE);
    	}
    	TextView cpu0maxtxt = (TextView)findViewById(R.id.textView12);
    	TextView cpu0maxtxte = (TextView)findViewById(R.id.textView3);
    	if(!cpu0max.equals("err")){
    	cpu0maxtxt.setText(cpu0max.substring(0, cpu0max.length()-3)+"Mhz");
    	cpu0maxtxt.setVisibility(View.VISIBLE);
		cpu0maxtxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu0maxtxt.setVisibility(View.GONE);
    		cpu0maxtxte.setVisibility(View.GONE);
    	}
    	TextView cpu1mintxt = (TextView)findViewById(R.id.textView13);
    	TextView cpu1mintxte = (TextView)findViewById(R.id.textView4);
    	if(!cpu1min.equals("err")){
    	cpu1mintxt.setText(cpu1min.substring(0, cpu1min.length()-3)+"Mhz");
    	cpu1mintxt.setVisibility(View.VISIBLE);
		cpu1mintxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu1mintxt.setVisibility(View.GONE);
    		cpu1mintxte.setVisibility(View.GONE);
    	}
    	TextView cpu1maxtxt = (TextView)findViewById(R.id.textView14);
    	TextView cpu1maxtxte = (TextView)findViewById(R.id.textView5);
    	if(!cpu1max.equals("err")){
    	cpu1maxtxt.setText(cpu1max.substring(0, cpu1max.length()-3)+"Mhz");
    	cpu1maxtxt.setVisibility(View.VISIBLE);
		cpu1maxtxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu1maxtxt.setVisibility(View.GONE);
    		cpu1maxtxte.setVisibility(View.GONE);
    	}
    	TextView cpu0gov = (TextView)findViewById(R.id.textView15);
    	TextView cpu0gove = (TextView)findViewById(R.id.textView20);
    	
    	if(!curentgovernorcpu0.equals("err")){
    		cpu0gov.setText(curentgovernorcpu0);
        	cpu0gov.setVisibility(View.VISIBLE);
    		cpu0gove.setVisibility(View.VISIBLE);
        	}
        	else{
        		cpu0gov.setVisibility(View.GONE);
        		cpu0gove.setVisibility(View.GONE);
        	}
    	TextView cpu1gov = (TextView)findViewById(R.id.textView23);
    	TextView cpu1gove = (TextView)findViewById(R.id.textView21);
    	if(!curentgovernorcpu1.equals("err")){
    		cpu1gov.setText(curentgovernorcpu1);
        	cpu1gov.setVisibility(View.VISIBLE);
    		cpu1gove.setVisibility(View.VISIBLE);
        	}
        	else{
        		cpu1gov.setVisibility(View.GONE);
        		cpu1gove.setVisibility(View.GONE);
        	}
    	
    	TextView cpu2mintxt = (TextView)findViewById(R.id.cpu2min);
    	TextView cpu2mintxte = (TextView)findViewById(R.id.textView256);
    	if(!cpu2min.equals("err")){
    	cpu2mintxt.setText(cpu2min.substring(0, cpu2min.length()-3)+"Mhz");
    	cpu2mintxt.setVisibility(View.VISIBLE);
		cpu2mintxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu2mintxt.setVisibility(View.GONE);
    		cpu2mintxte.setVisibility(View.GONE);
    	}
    	TextView cpu2maxtxt = (TextView)findViewById(R.id.cpu2max);
    	TextView cpu2maxtxte = (TextView)findViewById(R.id.textView300);
    	if(!cpu2max.equals("err")){
    	cpu2maxtxt.setText(cpu2max.substring(0, cpu2max.length()-3)+"Mhz");
    	cpu2maxtxt.setVisibility(View.VISIBLE);
		cpu2maxtxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu2maxtxt.setVisibility(View.GONE);
    		cpu2maxtxte.setVisibility(View.GONE);
    	}
    	TextView cpu3mintxt = (TextView)findViewById(R.id.cpu3min);
    	TextView cpu3mintxte = (TextView)findViewById(R.id.textView46);
    	if(!cpu3min.equals("err")){
    	cpu3mintxt.setText(cpu3min.substring(0, cpu3min.length()-3)+"Mhz");
    	cpu3mintxt.setVisibility(View.VISIBLE);
		cpu3mintxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu3mintxt.setVisibility(View.GONE);
    		cpu3mintxte.setVisibility(View.GONE);
    	}
    	TextView cpu3maxtxt = (TextView)findViewById(R.id.cpu3max);
    	TextView cpu3maxtxte = (TextView)findViewById(R.id.textView56);
    	if(!cpu3max.equals("err")){
    	cpu3maxtxt.setText(cpu3max.substring(0, cpu3max.length()-3)+"Mhz");
    	cpu3maxtxt.setVisibility(View.VISIBLE);
		cpu3maxtxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		cpu3maxtxt.setVisibility(View.GONE);
    		cpu3maxtxte.setVisibility(View.GONE);
    	}
    	TextView cpu2gov = (TextView)findViewById(R.id.cpu2gov);
    	TextView cpu2gove = (TextView)findViewById(R.id.textView201);
    	
    	if(!curentgovernorcpu2.equals("err")){
    		cpu2gov.setText(curentgovernorcpu2);
        	cpu2gov.setVisibility(View.VISIBLE);
    		cpu2gove.setVisibility(View.VISIBLE);
        	}
        	else{
        		cpu2gov.setVisibility(View.GONE);
        		cpu2gove.setVisibility(View.GONE);
        	}
    	TextView cpu3gov = (TextView)findViewById(R.id.cpu3gov);
    	TextView cpu3gove = (TextView)findViewById(R.id.textView213);
    	if(!curentgovernorcpu3.equals("err")){
    		cpu3gov.setText(curentgovernorcpu3);
        	cpu3gov.setVisibility(View.VISIBLE);
    		cpu3gove.setVisibility(View.VISIBLE);
        	}
        	else{
        		cpu3gov.setVisibility(View.GONE);
        		cpu3gove.setVisibility(View.GONE);
        	}
    	
    	TextView ledlight = (TextView)findViewById(R.id.textView16);
    	TextView ledlighte = (TextView)findViewById(R.id.textView7);
		
		try{
    	
    		ledlight.setText(Integer.parseInt(led)*100/60+"%");
    		//ledlight.setTextColor(Color.RED);
    		ledlight.setVisibility(View.VISIBLE);
    		ledlighte.setVisibility(View.VISIBLE);
    	
    	}
		catch(Exception e){
    //	else if(led.equals("UNSUPPORTED")){
    		ledlight.setVisibility(View.GONE);
    		ledlighte.setVisibility(View.GONE);
    //	}
		}
    	TextView gpu2dtxt = (TextView)findViewById(R.id.textView17);
    	TextView gpu2dtxte = (TextView)findViewById(R.id.textView8);
    	if(!gpu2d.equals("err")){
    	gpu2dtxt.setText(gpu2d.substring(0, gpu2d.length()-6)+"Mhz");
    	gpu2dtxt.setVisibility(View.VISIBLE);
		gpu2dtxte.setVisibility(View.VISIBLE);
    	}
    	else{
    		gpu2dtxt.setVisibility(View.GONE);
    		gpu2dtxte.setVisibility(View.GONE);
    	}
    	
    	TextView gpu3dtxt = (TextView)findViewById(R.id.textView18);
    	TextView gpu3dtxte = (TextView)findViewById(R.id.textView9);
    	if(!gpu3d.equals("err")){
    	gpu3dtxt.setText(gpu3d.substring(0, gpu3d.length()-6)+"Mhz");
    	gpu3dtxt.setVisibility(View.VISIBLE);
		gpu3dtxte.setVisibility(View.VISIBLE);
    	}
    	else{ 
    		gpu3dtxt.setVisibility(View.GONE);
    		gpu3dtxte.setVisibility(View.GONE);
    	}
    	TextView fastchargetxt = (TextView)findViewById(R.id.textView22);
    	TextView fastchargetxte = (TextView)findViewById(R.id.textView6);
    	if (fastcharge.equals("1")){
    		fastchargetxt.setText("ON");
    		fastchargetxt.setTextColor(Color.GREEN);
    		fastchargetxt.setVisibility(View.VISIBLE);
    		fastchargetxte.setVisibility(View.VISIBLE);
    	}
    	else if (fastcharge.equals("0")){
    		fastchargetxt.setText("OFF");
    		fastchargetxt.setTextColor(Color.RED);
    		fastchargetxt.setVisibility(View.VISIBLE);
    		fastchargetxte.setVisibility(View.VISIBLE);
    	}
    	else {
    		fastchargetxt.setVisibility(View.GONE);
    		fastchargetxte.setVisibility(View.GONE);
    	}
    	TextView vsynctxt = (TextView)findViewById(R.id.textView19);
    	TextView vsynctxte = (TextView)findViewById(R.id.textView10);
    	if (vsync.equals("1")){
    		vsynctxt.setText("ON");
    		vsynctxt.setTextColor(Color.GREEN);
    		vsynctxt.setVisibility(View.VISIBLE);
    		vsynctxte.setVisibility(View.VISIBLE);
    	}
    	else if (vsync.equals("0")){
    		vsynctxt.setText("OFF");
    		vsynctxt.setTextColor(Color.RED);
    		vsynctxt.setVisibility(View.VISIBLE);
    		vsynctxte.setVisibility(View.VISIBLE);
    	}
    	else {
    		vsynctxt.setVisibility(View.GONE);
    		vsynctxte.setVisibility(View.GONE);
    	}
    	TextView cdepthtxt = (TextView)findViewById(R.id.textView25);
    	TextView cdepthtxte = (TextView)findViewById(R.id.textView24);
    	if (cdepth.equals("16")){
    		cdepthtxt.setText("16");
    		cdepthtxt.setTextColor(Color.RED);
    		cdepthtxt.setVisibility(View.VISIBLE);
    		cdepthtxte.setVisibility(View.VISIBLE);
    	}
    	else if (cdepth.equals("24")){
    		cdepthtxt.setText("24");
    		cdepthtxt.setTextColor(Color.YELLOW);
    		cdepthtxt.setVisibility(View.VISIBLE);
    		cdepthtxte.setVisibility(View.VISIBLE);
    	}
    	else if (cdepth.equals("32")){
    		cdepthtxt.setText("32");
    		cdepthtxt.setTextColor(Color.GREEN);
    		cdepthtxt.setVisibility(View.VISIBLE);
    		cdepthtxte.setVisibility(View.VISIBLE);
    	}
    	else// if(cdepth==null) {
    	{	cdepthtxt.setVisibility(View.GONE);
    		cdepthtxte.setVisibility(View.GONE);
    	}
    	
    	TextView kinfo = (TextView)findViewById(R.id.textView26);
    	kinfo.setText(kernel);
		TextView sdcachetxt = (TextView)findViewById(R.id.textView34);
		TextView sdcachetxte = (TextView)findViewById(R.id.textView33);
		TextView ioschedulertxt = (TextView)findViewById(R.id.textView32);
		TextView ioschedulertxte = (TextView)findViewById(R.id.textView29);
		if (sdcache.equals("err")){
			sdcachetxt.setVisibility(View.GONE);
    		sdcachetxte.setVisibility(View.GONE);
		}
		else{
			sdcachetxt.setText(sdcache);
			sdcachetxt.setVisibility(View.VISIBLE);
    		sdcachetxte.setVisibility(View.VISIBLE);
		}
		if (scheduler.equals("err")){
			ioschedulertxt.setVisibility(View.GONE);
    		ioschedulertxte.setVisibility(View.GONE);
			}
			else{
				ioschedulertxt.setText(scheduler);
				ioschedulertxt.setVisibility(View.VISIBLE);
	    		ioschedulertxte.setVisibility(View.VISIBLE);
			}
		
		TextView s2wtxt = (TextView)findViewById(R.id.textView36);
    	TextView s2wtxte = (TextView)findViewById(R.id.textView35);
    	if (s2w.equals("1")){
    		s2wtxt.setText("ON with no backlight");
    		
    		s2wtxt.setVisibility(View.VISIBLE);
    		s2wtxte.setVisibility(View.VISIBLE);
    	}
    	else if (s2w.equals("2")){
    		s2wtxt.setText("ON with backlight");
    		
    		s2wtxt.setVisibility(View.VISIBLE);
    		s2wtxte.setVisibility(View.VISIBLE);
    	}
    	else if(s2w.equals("0")){
    		s2wtxt.setText("OFF");
    		s2wtxt.setTextColor(Color.RED);
    		s2wtxt.setVisibility(View.VISIBLE);
    		s2wtxte.setVisibility(View.VISIBLE);
    	}
    	else  {
    		s2wtxt.setVisibility(View.GONE);
    		s2wtxte.setVisibility(View.GONE);
    	}
    	SharedPreferences.Editor editor = preferences.edit();
	  //  editor.putString("","");
	   // editor.putBoolean("cputoggle", false);
	    editor.putString("gpu2d", gpu2d);
	    editor.putString("gpu3d", gpu3d);
	    editor.putString("cdepth", cdepth);
	    editor.putString("led", led);
	    editor.putString("io", scheduler);
  	    editor.putString("sdcache", sdcache);
  	    editor.putString("fastcharge", fastcharge);
  	    editor.putString("vsync", vsync);
  	    
  	    if (vsync.equals("1")){
  	    editor.putString("hw", "1");
  	    editor.putString("backbuf", "3");
  	  
  	    }
  	    else{
  	    	editor.putString("hw", "0");
  	  	    editor.putString("backbuf", "4");
  	  	    }
  	  editor.putString("onoff", onoff);
	    editor.putString("delaynew", delay);
	    editor.putString("pausenew", pause);
	    editor.putString("thruploadnew", thrupload);
	    editor.putString("thrdownloadnew", thrdownload);
	    editor.putString("thrupmsnew", thrupms);
	    editor.putString("thrdownmsnew", thrdownms);
	    editor.putString("idlefreq", curentidlefreq);
	    editor.putString("maxfreqselected", maxfreqselected);
		editor.putString("govselected", govselected);
		editor.putString("ldt", ldt);
		editor.putString("p1freq", p1freq);
 	    editor.putString("p2freq", p2freq);
 	    editor.putString("p3freq", p3freq);
 	    editor.putString("p1low", p1low);
 	    editor.putString("p1high", p1high);
 	    editor.putString("p2low", p2low);
 	    editor.putString("p2high", p2high);
 	   editor.putString("p3low", p3low);
	 	editor.putString("p3high", p3high);
	/*	editor.putString("cpu0min", cpu0min);
	    editor.putString("cpu0max", cpu0max);
	    editor.putString("cpu0gov", curentgovernorcpu0);
	    editor.putString("cpu1min", cpu1min);
	    editor.putString("cpu1max", cpu1max);
	    editor.putString("cpu1gov", curentgovernorcpu1);
  	 */ editor.commit();
	    
    	
        KernelTuner.this.data = result;

        
        //if (DualCore.this.pd != null){   
        //DualCore.this.pd.dismiss();
        //}// gpu.this.finish();
    }

    
    
	}


public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
	iscWindow3();
  
}


public void onPause() {
    super.onPause();
    
}


protected void onResume()
{
    //prefs();
    initialCheck();
new info().execute();
    
  
    	initdexport();
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

    	String boot = sharedPrefs.getString("boot2", "");
    	boolean cputoggle = sharedPrefs.getBoolean("cputoggle", true);
    	boolean cpu1off = sharedPrefs.getBoolean("cpu1off", false);
    	if (boot.equals("init.d")){
    		new initdApplyCpuGpuMisc().execute();
    		if(cpu1off==true){
    			new initdApplyCpu1Off().execute();
    		}
    		if(cputoggle==true){
    			new initdApplyCpu1forced().execute();
    		}
    	}
    	else {
    		new rmInitd().execute();
    	}
  
    super.onResume();
    
}


public void onStop() {
	
 
       super.onStop();
    
}

public void onDestroy() {
   
    
	thread=false;
AppWidgetBig updateBig = new AppWidgetBig();
	Context context = this;
	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_4x4);
	ComponentName thisWidget = new ComponentName(context, AppWidgetBig.class);
	//remoteViews.setTextViewText(R.id.my_text_view, "myText" + System.currentTimeMillis());
	appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	int[] appWidgetIds = null;
	updateBig.onUpdate(context, appWidgetManager,  appWidgetIds);
	
	
AppWidget updateSmall = new AppWidget();
	RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
	ComponentName thisWidgetSmall = new ComponentName(context, AppWidget.class);
	//remoteViews.setTextViewText(R.id.my_text_view, "myText" + System.currentTimeMillis());
	appWidgetManager.updateAppWidget(thisWidgetSmall, remoteViewsSmall);
	
	updateSmall.onUpdate(context, appWidgetManager,  appWidgetIds);
       super.onDestroy();
    finish();
}



public void iscWindow3(){
setContentView(R.layout.main);
batteryLevel = (TextView) this.findViewById(R.id.textView42);
batteryTemp = (TextView) this.findViewById(R.id.textView40);
this.registerReceiver(this.mBatInfoReceiver, 
					  new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
boolean ads = sharedPrefs.getBoolean("ads", true);
if (ads==true){AdView adView = (AdView)this.findViewById(R.id.ad);
adView.loadAd(new AdRequest());}



	changelog();
checkAnthrax();
new info().execute();
	
	File file = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
	try{

		InputStream fIn = new FileInputStream(file);

		
	}
	catch(FileNotFoundException e){ 
		new mountDebugFs().execute();
	}

	
	boolean ota = sharedPrefs.getBoolean("ota", true);
	
    if(ota==true){
    	new updateCheck().execute();
    }
	
	Button gpu = (Button)findViewById(R.id.button3);
	gpu.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			File file = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
			try{
			
			InputStream fIn = new FileInputStream(file);
			
			Intent myIntent = new Intent(KernelTuner.this, gpu.class);
			KernelTuner.this.startActivity(myIntent);
			}
			catch(FileNotFoundException e){
				AlertDialog alertDialog = new AlertDialog.Builder(
                        KernelTuner.this).create();

        alertDialog.setTitle("Unsupported kernel");
 
        alertDialog.setMessage("Your kernel doesnt support GPU Overclocking");
 
        alertDialog.setIcon(R.drawable.icon);
 
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              
                }
        });
 
        alertDialog.show();
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
			}
			}
	
	});
	
	Button voltage = (Button)findViewById(R.id.button6);
	voltage.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			File file = new File("/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
			try{
			
			InputStream fIn = new FileInputStream(file);
			
			Intent myIntent = new Intent(KernelTuner.this, uv.class);
			KernelTuner.this.startActivity(myIntent);
			}
			catch(FileNotFoundException e){
				AlertDialog alertDialog = new AlertDialog.Builder(
                        KernelTuner.this).create();

        alertDialog.setTitle("Unsupported kernel");
 
        alertDialog.setMessage("Your kernel doesnt support Undervolting");
 
        alertDialog.setIcon(R.drawable.icon);
 
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              
                }
        });
 
        alertDialog.show();
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
			}
			}
	
	});

//prefs();
initialCheck();
initdexport();




String boot = sharedPrefs.getString("boot2", "");
boolean cputoggle = sharedPrefs.getBoolean("cputoggle", true);
//boolean cpu1off = sharedPrefs.getBoolean("cpu1off", false);
if (boot.equals("init.d")){
	new initdApplyCpuGpuMisc().execute();
	/*if(cpu1off==true){
		new initdApplyCpu1Off().execute();
	}*/
	if(cputoggle==true){
		new initdApplyCpu1forced().execute();
	}
}
else {
	new rmInitd().execute();
}




new Thread(new Runnable() {
    
    public void run() {
        // TODO Auto-generated method stub
        while (thread) {
            try {
                Thread.sleep(700);
                mHandler.post(new Runnable() {

                    
                    public void run() {
                        // TODO Auto-generated method stub
                    	ReadCPU0Clock();
                    	
                    	ReadCPU0maxfreq();
                    	ReadCPU0freqs();
                    	
                    	if(new File("/sys/devices/system/cpu/cpu1/online").exists()){
                    	
                    		
                        	ReadCPU1Clock();
                        	ReadCPU1maxfreq();
                        	ReadCPU1freqs();
                    	}
                    	if(new File("/sys/devices/system/cpu/cpu2/online").exists()){
                    		ReadCPU2Clock();
                        	ReadCPU2maxfreq();
                        	ReadCPU2freqs();
                        	
                    	}
                    	if(new File("/sys/devices/system/cpu/cpu3/online").exists()){
                    		ReadCPU3Clock();
                        	ReadCPU3maxfreq();
                        	ReadCPU3freqs();
                        	
                    	}
               
                		
                   
                    	
                		
                    	
                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}).start();
new Thread(new Runnable() {
    
    public void run() {
        // TODO Auto-generated method stub
        while (thread) {
            try {
                Thread.sleep(1000);
                mHandler.post(new Runnable() {

                   
                    public void run() {
                        // TODO Auto-generated method stub
                    	
                    	uptime();
                		deepsleep();
                		cpuTemp();
                   
                    	
                		
                    	
                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}).start();

button2 = (Button)this.findViewById(R.id.button2);
button2.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		if(new File("/sys/devices/system/cpu/cpu3/online").exists()){
			Intent myIntent = new Intent(KernelTuner.this, OC_quad.class);
			KernelTuner.this.startActivity(myIntent);
		}
		if(new File("/sys/devices/system/cpu/cpu1/online").exists() && !(new File("/sys/devices/system/cpu/cpu3/online").exists())){
			Intent myIntent = new Intent(KernelTuner.this, OC.class);
			KernelTuner.this.startActivity(myIntent);
		}
		if(!(new File("/sys/devices/system/cpu/cpu1/online").exists()) && !(new File("/sys/devices/system/cpu/cpu3/online").exists()))
		{
			Intent myIntent = new Intent(KernelTuner.this, OC_single.class);
			KernelTuner.this.startActivity(myIntent);
		}
			
	}
});

Button buttontest = (Button)this.findViewById(R.id.button5);
buttontest.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		Intent myIntent = new Intent(KernelTuner.this, cpuStatsTab.class);
		KernelTuner.this.startActivity(myIntent);
		
	}
});
	
Button atweaks = (Button)this.findViewById(R.id.button7);
atweaks.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		Intent myIntent = new Intent(KernelTuner.this, anthraxTweaks.class);
		KernelTuner.this.startActivity(myIntent);
		
	}
});

	RelativeLayout info = (RelativeLayout)this.findViewById(R.id.rl2);
	info.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {

				new info().execute();
			}
		});

Button buttongpu = (Button)this.findViewById(R.id.button4);
buttongpu.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, miscTweaks.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

   
Button cpu1toggle = (Button)this.findViewById(R.id.button1);
cpu1toggle.setOnClickListener(new OnClickListener()
{
	

public void onClick(View v) {
	
	firstTimeDialog("CPU1");
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu1Toggle().execute();
	
	
}});

Button cpu2toggle = (Button)this.findViewById(R.id.button8);
cpu2toggle.setOnClickListener(new OnClickListener()
{
	

public void onClick(View v) {
	
	firstTimeDialog("CPU2");
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu2Toggle().execute();
	
	
}});

Button cpu3toggle = (Button)this.findViewById(R.id.button9);
cpu3toggle.setOnClickListener(new OnClickListener()
{
	

public void onClick(View v) {
	
	firstTimeDialog("CPU3");
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu3Toggle().execute();
	
	
}});

}

public void firstTimeDialog(String CPU){
	preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    boolean previouslyStarted = preferences.getBoolean("mpdecdialog", false);
	if (!previouslyStarted){
    AlertDialog alertDialog = new AlertDialog.Builder(
            KernelTuner.this).create();

// Setting Dialog Title
alertDialog.setTitle("Force CPU1 online");

// Setting Dialog Message
alertDialog.setMessage("By forcing "+CPU+ " online \"mpdecision\" needs to be disabled." +
		"It will be re enabled when you disable CPU1." +
		"If your kernel doesnt support \"mpdecision\" please ignore this message");

// Setting Icon to Dialog
alertDialog.setIcon(R.drawable.icon);

// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
    // Write your code here to execute after dialog closed
    }
});

// Showing Alert Message
alertDialog.show();
	alertDialog.setIcon(R.drawable.icon);
	alertDialog.show();
	}	
	SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean("mpdecdialog", true); // value to store
    editor.commit();
}

 public void download(){
	String url = "http://dualcore.netii.net/fm/KernelTuner-" + remoteversion + ".php";
	DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
	request.setTitle("Downloading new version");
	request.setDescription("Kernel Tuner-" + remoteversion + ".apk");
//in order for this if to run, you must use the android 3.2 to compile your app
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	}
	try{
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "KernelTuner-" + remoteversion + ".apk");
	DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
	manager.enqueue(request); 
	
	}catch(Exception e){
		Toast.makeText(getApplicationContext(), "SD card is not mounted", Toast.LENGTH_LONG).show();
	}
//get download service and enqueue file
	System.out.println(request);
}
 


	
	public void changelog(){
	preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    String versionpref = preferences.getString("version", "");
	
		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		String	version = pInfo.versionName;
			if (!versionpref.equals(version)){

				Intent myIntent = new Intent(KernelTuner.this, changelog.class);
				KernelTuner.this.startActivity(myIntent);

			}	
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("version", version); // value to store
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{}
	

}

	public void battTempWarning(){
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
    	anim.setDuration(500); //You can manage the time of the blink with this parameter
    	anim.setStartOffset(20);
    	anim.setRepeatMode(Animation.REVERSE);
    	anim.setRepeatCount(Animation.INFINITE);
    	batteryTemp.startAnimation(anim);
    	
	}
	public void battTempWarningStop(){
		batteryTemp.clearAnimation();
	}
	
	public void battLevelWarning(){
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
    	anim.setDuration(500); //You can manage the time of the blink with this parameter
    	anim.setStartOffset(20);
    	anim.setRepeatMode(Animation.REVERSE);
    	anim.setRepeatCount(Animation.INFINITE);
    	batteryLevel.startAnimation(anim);
    	
	}
	public void battLevelWarningStop(){
		batteryLevel.clearAnimation();
	}
	
	public void cpuTempWarning(){
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
    	anim.setDuration(500); //You can manage the time of the blink with this parameter
    	anim.setStartOffset(20);
    	anim.setRepeatMode(Animation.REVERSE);
    	anim.setRepeatCount(Animation.INFINITE);
    	cputemptxt.startAnimation(anim);
    	
	}
	public void cpuTempWarningStop(){
		cputemptxt.clearAnimation();
	}
	
public void uptime(){
	long uptime = SystemClock.uptimeMillis();
	int hr =  (int) ((uptime / 1000) / 3600);
   int   mn =  (int) (((uptime / 1000) / 60) % 60);
      int sc =  (int) ((uptime / 1000) % 60);
	String minut = String.valueOf(mn);
	String sekund = String.valueOf(sc);
	String sati = String.valueOf(hr);
	String tmp;
	tmp= sati+"h:"+minut+"m:"+sekund+"s";
	TextView uptimer = (TextView)findViewById(R.id.textView30);
	uptimer.setText(tmp);
	
}
public void deepsleep(){
	String temp2 = String.valueOf(SystemClock.elapsedRealtime()-SystemClock.uptimeMillis()); 
	  int time = Integer.parseInt(temp2);
	  int hr =  ((time / 1000) / 3600);
    int mn =  (((time / 1000) / 60) % 60);
    int sc =  ((time / 1000) % 60);
    String minut = String.valueOf(mn);
     String sekund = String.valueOf(sc);
     String sati = String.valueOf(hr);
     
     temp2= sati+"h:"+minut+"m:"+sekund+"s";
	TextView dstimer = (TextView)findViewById(R.id.textView31);
	dstimer.setText(temp2);
	
}

public void cpuTemp(){
	cputemptxt = (TextView)findViewById(R.id.textView38);
	TextView cputemptxte = (TextView)findViewById(R.id.textView37);
	
	try {

		File myFile = new File(
				"/sys/class/thermal/thermal_zone1/temp");
		FileInputStream fIn = new FileInputStream(myFile);

		BufferedReader myReader = new BufferedReader(new InputStreamReader(
				fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		cputemp = aBuffer.trim();
		cputemptxt.setVisibility(View.VISIBLE);
		cputemptxte.setVisibility(View.VISIBLE);
		cputemptxt.setText(cputemp+"°C");
		int temp = Integer.parseInt(cputemp);
		if(temp<45){
	    	  cputemptxt.setTextColor(Color.GREEN);
	    	  cpuTempWarningStop();
	      }
	      else if(temp>45 && temp<55){
	    	  cputemptxt.setTextColor(Color.YELLOW);
	    	  cpuTempWarningStop();
	      }
	      
	      else if(temp>59){
	    	  cpuTempWarning();
	    	  cputemptxt.setTextColor(Color.RED);

	      }
		myReader.close();

	} catch (Exception e2) {

		cputemptxt.setVisibility(View.GONE);
		cputemptxte.setVisibility(View.GONE);
		
	}
}

public void initialCheck(){
	
	if(new File("/sys/devices/system/cpu/cpu1/online").exists()){
	Button b2 = (Button) findViewById(R.id.button1);
	b2.setVisibility(View.VISIBLE);
		ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		cpu1progbar.setVisibility(View.VISIBLE);
		TextView tv1 = (TextView) findViewById(R.id.ptextView2);
		tv1.setVisibility(View.VISIBLE);
		TextView tv4 = (TextView) findViewById(R.id.ptextView4);
		tv4.setVisibility(View.VISIBLE);
	}
	else{
		Button b2 = (Button) findViewById(R.id.button1);
		b2.setVisibility(View.GONE);
		  ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		  cpu1progbar.setVisibility(View.GONE);
		 TextView tv1 = (TextView) findViewById(R.id.ptextView2);
		 tv1.setVisibility(View.GONE);
		 TextView tv4 = (TextView) findViewById(R.id.ptextView4);
		 tv4.setVisibility(View.GONE);
}
	if(new File("/sys/devices/system/cpu/cpu2/online").exists()){
		Button b3 = (Button) findViewById(R.id.button8);
		b3.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.VISIBLE);
		}
		else{
			Button b3 = (Button) findViewById(R.id.button8);
			b3.setVisibility(View.GONE);
			  ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			  cpu1progbar.setVisibility(View.GONE);
			 TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			 tv1.setVisibility(View.GONE);
			 TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			 tv4.setVisibility(View.GONE);
	}
	if(new File("/sys/devices/system/cpu/cpu3/online").exists()){
		Button b4 = (Button) findViewById(R.id.button9);
		  b4.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.VISIBLE);
		}
		else{
			Button b4 = (Button) findViewById(R.id.button9);
			b4.setVisibility(View.GONE);
			  ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			  cpu1progbar.setVisibility(View.GONE);
			 TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			 tv1.setVisibility(View.GONE);
			 TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			 tv4.setVisibility(View.GONE);
	}
	
	File file4 = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
	File file5 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
	try{
		InputStream fIn = new FileInputStream(file4);
	}
	catch(FileNotFoundException e){ 
		try{
			InputStream fIn = new FileInputStream(file5);
		}
		catch(FileNotFoundException e2){
			Button voltage = (Button)findViewById(R.id.button2);
		voltage.setVisibility(View.GONE);
		}
	

	}
	
		 	File file = new File("/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
		try{
			InputStream fIn = new FileInputStream(file);
		}
		catch(FileNotFoundException e){ 
		Button voltage = (Button)findViewById(R.id.button6);
		voltage.setVisibility(View.GONE);

		}
		
		File file2 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
		try{
			InputStream fIn = new FileInputStream(file2);
		}
		catch(FileNotFoundException e){ 
		Button times = (Button)findViewById(R.id.button5);
		times.setVisibility(View.GONE);

		}
		
		File file3 = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
		try{
			InputStream fIn = new FileInputStream(file3);
		}
		catch(FileNotFoundException e){ 
		Button gpu = (Button)findViewById(R.id.button3);
		gpu.setVisibility(View.GONE);

		}
	
	}

public void ReadCPU0Clock()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
		iscVa = aBuffer;
		myReader.close();
		
		
	} catch (Exception e) {
		
	}
	
	

cpu0update();

}


public void ReadCPU1Clock()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq");
		FileInputStream fIn = new FileInputStream(myFile);

		
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		iscVa2 = aBuffer;
		myReader.close();
		
	} catch (Exception e) {
		iscVa2 = "offline";

	}
	
	

	cpu1update();

}

public void ReadCPU2Clock()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
		freqcpu2 = aBuffer;
		myReader.close();
		
		
	} catch (Exception e) {
		
	}
	
	

cpu2update();

}


public void ReadCPU3Clock()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq");
		FileInputStream fIn = new FileInputStream(myFile);

		
		
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}

		freqcpu3 = aBuffer;
		myReader.close();
		
	} catch (Exception e) {
		iscVa2 = "offline";

	}
	
	

	cpu3update();

}



public void initdexport(){
	
	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	
	 // boolean cpu1off = sharedPrefs.getBoolean("cpu1off", false);
	  String gpu3d = sharedPrefs.getString("gpu3d", "");
	  String gpu2d = sharedPrefs.getString("gpu2d", "");
	 
	  String hw = sharedPrefs.getString("hw", "");
	  String cdepth = sharedPrefs.getString("cdepth", "");
	  String cpu1min = sharedPrefs.getString("cpu1min", "");
	  String cpu1max = sharedPrefs.getString("cpu1max", "");
	  String cpu0max = sharedPrefs.getString("cpu0max", "");
	  String cpu0min = sharedPrefs.getString("cpu0min", "");
	  String cpu3min = sharedPrefs.getString("cpu3min", "");
	  String cpu3max = sharedPrefs.getString("cpu3max", "");
	  String cpu2max = sharedPrefs.getString("cpu2max", "");
	  String cpu2min = sharedPrefs.getString("cpu2min", "");
	  boolean cputoggle = sharedPrefs.getBoolean("cputoggle", true);
	  boolean cpu2toggle = sharedPrefs.getBoolean("cputoggle", true);
	  boolean cpu3toggle = sharedPrefs.getBoolean("cputoggle", true);
	  String fastcharge = sharedPrefs.getString("fastcharge", "");
	  String mpdecisionscroff = sharedPrefs.getString("mpdecisionscroff", "");
	  String backbuff = sharedPrefs.getString("backbuf", "");
	  String vsync = sharedPrefs.getString("vsync", "");
	  String led = sharedPrefs.getString("led", "");
	  String cpu0gov = sharedPrefs.getString("cpu0gov", "");
	  String cpu1gov = sharedPrefs.getString("cpu1gov", "");
	  String cpu2gov = sharedPrefs.getString("cpu2gov", "");
	  String cpu3gov = sharedPrefs.getString("cpu3gov", "");
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
	  
	  String gpu = "#!/system/bin/sh \n" +
		"echo " + "\""+gpu3d + "\""+" > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk \n" +
		"echo " + "\""+gpu2d + "\""+" > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk \n" ;
	  
	  
	  String cpu ="#!/system/bin/sh \n chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
	  		"echo " + "\""+cpu0gov+"\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
	  		"chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu0max + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
	  		"chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n" +
		"echo " + "\""+cpu0min + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n" +
	  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
		"echo " + "\""+cpu1gov +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
	  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu1max +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
	  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n" +
		"echo " +"\""+ cpu1min +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n"+
		
		"chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
  		"echo " + "\""+cpu2gov+"\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
  		"chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
	"echo " + "\""+cpu2max + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
  		"chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n" +
	"echo " + "\""+cpu2min + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n" +
  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
	"echo " + "\""+cpu3gov +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
	"echo " + "\""+cpu3max +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
  		"chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n" +
	"echo " +"\""+ cpu3min +"\""+ " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n";
	  
	  String misc = "#!/system/bin/sh \n" +
	  		"mount -t debugfs debugfs /sys/kernel/debug \n\n" +
		"echo " + "\""+vsync + "\""+" > /sys/kernel/debug/msm_fb/0/vsync_enable \n" +
		"echo " + "\""+hw + "\""+" > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
		"echo " + "\""+backbuff + "\""+" > /sys/kernel/debug/msm_fb/0/backbuff \n" +
		"echo " +"\""+ led + "\""+" > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n" +
		"echo " + "\""+fastcharge + "\""+" > /sys/kernel/fast_charge/force_fast_charge \n" +
		"echo " + "\""+cdepth + "\""+" > /sys/kernel/debug/msm_fb/0/bpp \n" +
		"echo " + "\""+mpdecisionscroff + "\""+" > /sys/kernel/msm_mpdecision/conf/do_scroff_single_core \n\n" +
	  	    "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n" +
		"chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n" +
			 "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n" +
		"echo " + "\""+sdcache + "\""+" > /sys/block/mmcblk1/queue/read_ahead_kb \n"+
		"echo " + "\""+sdcache + "\""+" > /sys/block/mmcblk0/queue/read_ahead_kb \n"+
		"echo " + "\""+sdcache + "\""+" > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n"+
	        "chmod 777 /sys/block/mmcblk0/queue/scheduler \n"+
	        "chmod 777 /sys/block/mmcblk1/queue/scheduler \n"+
		"echo " + "\""+io + "\""+" > /sys/block/mmcblk0/queue/scheduler \n"+
		"echo " +"\""+ io + "\""+" > /sys/block/mmcblk1/queue/scheduler \n\n"+
	    
		"echo " + "\""+onoff.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/scroff_profile \n"+
		"echo " + "\""+delaynew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/delay \n"+
		"echo " + "\""+pausenew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/pause \n"+
		"echo " + "\""+thruploadnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n"+
		"echo " + "\""+thrdownloadnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n"+
		"echo " + "\""+thrupmsnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/twts_threshold_up \n"+
		"echo " + "\""+thrdownmsnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/twts_threshold_down \n"+
	         
		"echo " + "\""+maxfreqselected.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/mpdec_scroff_freq \n"+
		"echo " + "\""+govselected.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/mpdec_scroff_gov \n\n"+
		"echo " + "\""+ldt + "\""+" > /sys/kernel/notification_leds/off_timer_multiplier\n"+
		"echo " + "\""+s2w + "\""+" > /sys/android_touch/sweep2wake\n"+
		"echo " + "\""+s2w + "\""+" > /sys/android_touch/sweep2wake/s2w_switch\n\n"+
		
		"echo " + p1freq + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n"+
        "echo " + p2freq + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n"+
        "echo " + p3freq + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n"+
        "echo " + p1low+ " > /sys/kernel/msm_thermal/conf/allowed_low_low\n"+
        "echo " + p1high + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n"+
        "echo " + p2low + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n"+
        "echo " + p2high + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n"+
        "echo " + p3low + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n"+
        "echo " + p3high + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n"+
        
	  	  "umount /sys/kernel/debug \n" ;

	  String forcecpu1online ="#!/system/bin/sh \n" +
			  "echo 0 > /sys/kernel/msm_mpdecision/conf/enabled \n"+
 		    		"chmod 666 /sys/devices/system/cpu/cpu1/online \n"+
 		    		 "echo 1 > /sys/devices/system/cpu/cpu1/online \n"+
 		    		"hmod 444 /sys/devices/system/cpu/cpu1/online \n" +
 		    		"chown system /sys/devices/system/cpu/cpu1/online \n";
	  
	  /*String cpu1disable ="#!/system/bin/sh \n" +
 		    		 "echo 0 > /sys/devices/system/cpu/cpu1/online \n"+
 		    		"hmod 444 /sys/devices/system/cpu/cpu1/online \n" +
 		    		"chown system /sys/devices/system/cpu/cpu1/online \n";*/
	  
	  try { 
             
          FileOutputStream fOut = openFileOutput("99dccputweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(cpu);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	  try { 
          
          FileOutputStream fOut = openFileOutput("99dcgputweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(gpu);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	  try { 
          
          FileOutputStream fOut = openFileOutput("99dcmisctweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(misc);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	  try { 
          
          FileOutputStream fOut = openFileOutput("99dccpu1online",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(forcecpu1online);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	 /* try { 
          
          FileOutputStream fOut = openFileOutput("99dccpu1disable",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(cpu1disable);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }*/
	 
}

public void ReadCPU0freqs()
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
		
		cpu0freqs = aBuffer;
		myReader.close();
		cpu0freqslist = Arrays.asList(cpu0freqs.split("\\s"));
		cpu0progress();
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
 				
 				delims = strLine.split(" ");
 				String freq = delims[0];
 				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

 				frequencies3.add(freq);

 			}
 			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 			boolean ov = sharedPrefs.getBoolean("override", false);
 			if(ov==true){
 			Collections.reverse(frequencies3);
 			}
 			String[] strarray = frequencies3.toArray(new String[0]);
 			frequencies3.clear();
 			System.out.println(frequencies3);
 			StringBuilder builder = new StringBuilder();
 			for(String s : strarray) {
 			    builder.append(s);
 			    builder.append(" ");
 			}
 			cpu0freqs = builder.toString();
 			
 			cpu0freqslist = Arrays.asList(cpu0freqs.split("\\s"));
 			
 			cpu0progress();

 			
 			in.close();
		}
		catch(Exception ee){
		ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		cpu1progbar.setProgress(0);
		TextView cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
		cpu1prog.setText(iscVa2.trim());
		}
	}
	
}

public void ReadCPU1freqs()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_frequencies");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}		
		cpu1freqs = aBuffer;
		myReader.close();
		cpu1freqslist = Arrays.asList(cpu1freqs.split("\\s"));		
		cpu1progress();

	} catch (Exception e) {
		try{
			// Open the file that is the first 
 			// command line parameter
 			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state");
 			// Get the object of DataInputStream
 			DataInputStream in = new DataInputStream(fstream);
 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
 			String strLine;
 			//Read File Line By Line
 			
 			while ((strLine = br.readLine()) != null)   {
 				
 				delims = strLine.split(" ");
 				String freq = delims[0];
 				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

 				frequencies3.add(freq);

 			}
 			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 			boolean ov = sharedPrefs.getBoolean("override", false);
 			if(ov==true){
 			Collections.reverse(frequencies3);
 			}
 			String[] strarray = frequencies3.toArray(new String[0]);
 			frequencies3.clear();
 			StringBuilder builder = new StringBuilder();
 			for(String s : strarray) {
 			    builder.append(s);
 			    builder.append(" ");
 			}
 			cpu1freqs = builder.toString();
 			cpu1freqslist = Arrays.asList(cpu1freqs.split("\\s"));
 			cpu1progress();

 			
 			in.close();
		}
		catch(Exception ee){
		ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		cpu1progbar.setProgress(0);
		TextView cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
		cpu1prog.setText(iscVa2.trim());
		}
	}

}

public void ReadCPU2freqs()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_frequencies");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
		
		cpu2freqs = aBuffer;
		myReader.close();
		cpu2freqslist = Arrays.asList(cpu2freqs.split("\\s"));
		cpu0progress();
	} catch (Exception e) {
		try{
			// Open the file that is the first 
 			// command line parameter
 			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state");
 			// Get the object of DataInputStream
 			DataInputStream in = new DataInputStream(fstream);
 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
 			String strLine;
 			//Read File Line By Line
 			
 			while ((strLine = br.readLine()) != null)   {
 				
 				delims = strLine.split(" ");
 				String freq = delims[0];
 				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

 				frequencies4.add(freq);

 			}
 			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 			boolean ov = sharedPrefs.getBoolean("override", false);
 			if(ov==true){
 			Collections.reverse(frequencies4);
 			}
 			String[] strarray = frequencies4.toArray(new String[0]);
 			frequencies4.clear();
 			StringBuilder builder = new StringBuilder();
 			for(String s : strarray) {
 			    builder.append(s);
 			    builder.append(" ");
 			}
 			cpu2freqs = builder.toString();
 			cpu2freqslist = Arrays.asList(cpu2freqs.split("\\s"));
 			cpu2progress();

 			
 			in.close();
		}
		catch(Exception ee){
		ProgressBar cpu2progbar = (ProgressBar)findViewById(R.id.progressBar3);
		cpu2progbar.setProgress(0);
		TextView cpu2prog = (TextView)this.findViewById(R.id.ptextView7);
		cpu2prog.setText(iscVa2.trim());
		}
	}
	
}

public void ReadCPU3freqs()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_frequencies");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}		
		cpu3freqs = aBuffer;
		myReader.close();
		cpu3freqslist = Arrays.asList(cpu3freqs.split("\\s"));		
		cpu1progress();

	} catch (Exception e) {
		try{
			// Open the file that is the first 
 			// command line parameter
 			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
 			// Get the object of DataInputStream
 			DataInputStream in = new DataInputStream(fstream);
 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
 			String strLine;
 			//Read File Line By Line
 			
 			while ((strLine = br.readLine()) != null)   {
 				
 				delims = strLine.split(" ");
 				String freq = delims[0];
 				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

 				frequencies5.add(freq);

 			}
 			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 			boolean ov = sharedPrefs.getBoolean("override", false);
 			if(ov==true){
 			Collections.reverse(frequencies5);
 			}
 			String[] strarray = frequencies5.toArray(new String[0]);
 			frequencies5.clear();
 			StringBuilder builder = new StringBuilder();
 			for(String s : strarray) {
 			    builder.append(s);
 			    builder.append(" ");
 			}
 			cpu3freqs = builder.toString();
 			cpu3freqslist = Arrays.asList(cpu3freqs.split("\\s"));
 			cpu3progress();

 			
 			in.close();
		}
		catch(Exception ee){
		ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
		cpu1progbar.setProgress(0);
		TextView cpu1prog = (TextView)this.findViewById(R.id.ptextView8);
		cpu1prog.setText(iscVa2.trim());
		}
	}

}


public void ReadCPU0maxfreq()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
	
		
		cpu0max = aBuffer;
		myReader.close();
		
		;
		
		
	} catch (Exception e) {
		
	}
	
}

public void ReadCPU1maxfreq()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
	
		
		cpu1max = aBuffer;
		myReader.close();
		
		;
		
		
	} catch (Exception e) {
		
	}

}

public void ReadCPU2maxfreq()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
	
		
		cpu2max = aBuffer;
		myReader.close();
		
		;
		
		
	} catch (Exception e) {
		
	}
	
}

public void ReadCPU3maxfreq()
{
	 

	try {
		
		File myFile = new File("/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
	
		
		cpu3max = aBuffer;
		myReader.close();
		
		;
		
		
	} catch (Exception e) {
		
	}

}

public void cpu0update()
{

TextView cpu0prog = (TextView)this.findViewById(R.id.ptextView3);
cpu0prog.setText(iscVa.trim());
}

public void cpu0progress()
{

ProgressBar cpu0progbar = (ProgressBar)findViewById(R.id.progressBar1);
cpu0progbar.setMax(cpu0freqslist.indexOf(cpu0max.trim())+1);
cpu0progbar.setProgress(cpu0freqslist.indexOf(iscVa.trim())+1);

}

public void cpu1update()
{

TextView cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
cpu1prog.setText(iscVa2.trim());
}
public void cpu1progress()
{

ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
cpu1progbar.setMax(cpu1freqslist.indexOf(cpu1max.trim())+1);
cpu1progbar.setProgress(cpu1freqslist.indexOf(iscVa2.trim())+1);

}

public void cpu2update()
{

TextView cpu2prog = (TextView)this.findViewById(R.id.ptextView7);
cpu2prog.setText(freqcpu2.trim());
}

public void cpu2progress()
{

ProgressBar cpu2progbar = (ProgressBar)findViewById(R.id.progressBar3);
cpu2progbar.setMax(cpu2freqslist.indexOf(cpu2max.trim())+1);
cpu2progbar.setProgress(cpu2freqslist.indexOf(freqcpu2.trim())+1);

}

public void cpu3update()
{

TextView cpu3prog = (TextView)this.findViewById(R.id.ptextView8);
cpu3prog.setText(freqcpu3.trim());
}
public void cpu3progress()
{

ProgressBar cpu3progbar = (ProgressBar)findViewById(R.id.progressBar4);
cpu3progbar.setMax(cpu3freqslist.indexOf(cpu3max.trim())+1);
cpu3progbar.setProgress(cpu3freqslist.indexOf(freqcpu3.trim())+1);

}


private Menu mainMenu;
//private MenuItem menuItem;
public boolean onCreateOptionsMenu(Menu menu) {
   
	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options, menu);
   
    mainMenu = menu;
    //subMenu = menuItem;
    Log.d("Menu", "menu created");
    
    return true;
}

public boolean onPrepareOptionsMenu (Menu menu) {
    
    return true;
}




public boolean onOptionsItemSelected(MenuItem item) {
  
	if (item.getItemId() == R.id.settings) {
        startActivity(new Intent(this, Preferences.class));
        Log.d("Menu", "settings selected");
    }
    if (item.getItemId() == R.id.changelog) {
        startActivity(new Intent(this, changelog.class));
        Log.d("Menu", "changelog selected");
    }
    if (item.getItemId() == R.id.update) {
		new updateCheck().execute();
		Log.d("Menu", "update selected");
    }
	if (item.getItemId() == R.id.about) {
        startActivity(new Intent(this, about.class));
        Log.d("Menu", "about selected");
    }	
	if (item.getItemId() == R.id.check) {
        startActivity(new Intent(this, check.class));
        Log.d("Menu", "check selected");
    }	
	if (item.getItemId() == R.id.profiles) {
        startActivity(new Intent(this, profiles.class));
        Log.d("Menu", "check selected");
    }	
    return super.onOptionsItemSelected(item);
}

/*public void prefs(){
	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		  boolean a = sharedPrefs.getBoolean("cpu1off", false);
	      if (a == true){
	    	  Button b2 = (Button) findViewById(R.id.button1);
	    	  b2.setEnabled(false);
	    	  disablecpu1();
	    	  cpu1 = (TextView)this.findViewById(R.id.ptextView4);
	    	  cpu1.setTextColor(Color.RED);

	     }
	      else{
	    	
	    	  Button b2 = (Button) findViewById(R.id.button1);
	    	  b2.setEnabled(true);
			 
	    	  cpu1 = (TextView)this.findViewById(R.id.ptextView4);
	    	  cpu1.setTextColor(Color.WHITE);
	    	  
	      }
}

public void disablecpu1() {

	Process localProcess;
	try {
		localProcess = Runtime.getRuntime().exec("su");

		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
		localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
		localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
		localDataOutputStream.writeBytes("echo echo 0 > /sys/devices/system/cpu/cpu1/online\n");
		localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
		localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
		
	//	localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
		
		
		localDataOutputStream.writeBytes("exit\n");
		localDataOutputStream.flush();
		localDataOutputStream.close();
		localProcess.waitFor();
		localProcess.destroy();
			SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("thrcpu1",thrupload);
editor.commit();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}*/
	
	


public void checkAnthrax(){
	String anthrax = null;
	try {
		File myFile = new File("/proc/version");
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
		String aDataRow = "";
		String aBuffer = "";
		while ((aDataRow = myReader.readLine()) != null) {
			aBuffer += aDataRow + "\n";
		}
		anthrax = aBuffer;
		myReader.close();
		
	} catch (Exception e) {
		anthrax="notfound";
	}
	
	
	Button anth = (Button)findViewById(R.id.button7);
    int intIndex = anthrax.indexOf("anthrax");
    if(intIndex == - 1){
       System.out.println("not found");
       
       anth.setVisibility(View.GONE);
    }else{
       System.out.println("Found anthrax at index "
       + intIndex);
       anth.setVisibility(View.VISIBLE);
      
    }
}


	
}

