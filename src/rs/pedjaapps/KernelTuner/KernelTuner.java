package rs.pedjaapps.KernelTuner;

import java.io.BufferedInputStream;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;


//EndImports

@SuppressLint("WorldReadableFiles")
public class KernelTuner extends Activity {

	private TextView batteryLevel;
	private TextView batteryTemp;
	private TextView cputemptxt;
	public SharedPreferences sharedPrefs;
	String tempPref;
	boolean tempMonitor;
	private long mLastBackPressTime = 0;
	Toast mToast;
	  private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent intent) {
	    	
	    	
	      int level = intent.getIntExtra("level", 0);
	      double temperature =  intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10;
	      
	     
	      
	      if (tempPref.equals("fahrenheit"))
	      {
	    	  temperature = (temperature*1.8)+32;
	    	  batteryTemp.setText(String.valueOf((int)temperature) + "°F");
		      if(temperature<=104){
		    	  batteryTemp.setTextColor(Color.GREEN);
		    	  battTempWarningStop();
		      }
		      else if(temperature>104 && temperature<131){
		    	  batteryTemp.setTextColor(Color.YELLOW);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=131 && temperature < 140 ){
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=140){
		    	 // Log.e("Battery warning","start animation");
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarning();

		      }
	      }
	      
	      else if(tempPref.equals("celsius")){
	    	  batteryTemp.setText(String.valueOf(temperature) + "°C");
		      if(temperature<45){
		    	  batteryTemp.setTextColor(Color.GREEN);
		    	  battTempWarningStop();
		      }
		      else if(temperature>45 && temperature<55){
		    	  batteryTemp.setTextColor(Color.YELLOW);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=55 && temperature < 60 ){
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=60){
		    	 // Log.e("Battery warning","start animation");
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarning();

		      }
	      }
	      else if(tempPref.equals("kelvin")){
	    	  temperature = temperature + 273.15; 
	    	  batteryTemp.setText(String.valueOf(temperature) + "°K");
		      if(temperature<318.15){
		    	  batteryTemp.setTextColor(Color.GREEN);
		    	  battTempWarningStop();
		      }
		      else if(temperature>318.15 && temperature<328.15){
		    	  batteryTemp.setTextColor(Color.YELLOW);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=328.15 && temperature < 333.15 ){
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarningStop();
		      }
		      else if(temperature>=333.15){
		    	 // Log.e("Battery warning","start animation");
		    	  batteryTemp.setTextColor(Color.RED);
		    	  battTempWarning();

		      }
	      }
	      ///F = (C x 1.8) + 32 
	      batteryLevel.setText(String.valueOf(level) + "%");
	      if(level<15 && level>=5){
	    	  batteryLevel.setTextColor(Color.RED);
	    	  battLevelWarningStop();
	      }
	      else if(level>15 && level<=30){
	    	  batteryLevel.setTextColor(Color.YELLOW);
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

public static String cpu0online = CPUInfo.cpu0online; 
public static String cpu1online = CPUInfo.cpu1online; 
public static String cpu2online = CPUInfo.cpu2online; 
public static String cpu3online = CPUInfo.cpu3online; 


public static String CPU0_FREQS = CPUInfo.CPU0_FREQS;

public static String CPU0_CURR_FREQ = CPUInfo.CPU0_CURR_FREQ;
public static String CPU1_CURR_FREQ = CPUInfo.CPU1_CURR_FREQ;
public static String CPU2_CURR_FREQ = CPUInfo.CPU2_CURR_FREQ;
public static String CPU3_CURR_FREQ = CPUInfo.CPU3_CURR_FREQ;

public static String CPU0_MAX_FREQ = CPUInfo.CPU0_MAX_FREQ;
public static String CPU1_MAX_FREQ = CPUInfo.CPU1_MAX_FREQ;
public static String CPU2_MAX_FREQ = CPUInfo.CPU2_MAX_FREQ;
public static String CPU3_MAX_FREQ = CPUInfo.CPU3_MAX_FREQ;

public static String CPU0_MIN_FREQ = CPUInfo.CPU0_MIN_FREQ;
public static String CPU1_MIN_FREQ = CPUInfo.CPU1_MIN_FREQ;
public static String CPU2_MIN_FREQ = CPUInfo.CPU2_MIN_FREQ;
public static String CPU3_MIN_FREQ = CPUInfo.CPU3_MIN_FREQ;

public static String CPU0_CURR_GOV = CPUInfo.CPU0_CURR_GOV;
public static String CPU1_CURR_GOV = CPUInfo.CPU1_CURR_GOV;
public static String CPU2_CURR_GOV = CPUInfo.CPU2_CURR_GOV;
public static String CPU3_CURR_GOV = CPUInfo.CPU3_CURR_GOV;

public static String CPU0_GOVS = CPUInfo.CPU0_GOVS;
public static String CPU1_GOVS = CPUInfo.CPU1_GOVS;
public static String CPU2_GOVS = CPUInfo.CPU2_GOVS;
public static String CPU3_GOVS = CPUInfo.CPU3_GOVS;

String mpdec;

List<String> frequencies3 = new ArrayList<String>();
List<String> frequencies4 = new ArrayList<String>();
List<String> frequencies5 = new ArrayList<String>();
	public String[] delims;

String freqs;
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
public List<String> freqlist;
public SharedPreferences preferences;
private ProgressDialog pd = null;
public String s2w;

Handler mHandler = new Handler();



ProgressDialog mProgressDialog;
private class DownloadNewVersion extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... sUrl) {
        try {
            URL url = new URL(sUrl[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/download/KernelTuner-" + remoteversion + ".apk");

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(KernelTuner.this);
    	mProgressDialog.setMessage("Downloading Update");
    	mProgressDialog.setIndeterminate(false);
    	mProgressDialog.setMax(100);
    	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setProgress(progress[0]);
        
    }
    @Override
    protected void onPostExecute(String result) {
    	mProgressDialog.dismiss();
    	Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "KernelTuner-" + remoteversion + ".apk")), "application/vnd.android.package-archive");
        startActivity(intent);
        
    }
}

class updateCheck extends AsyncTask<String, Void, Object> {


	@Override
	protected Object doInBackground(String... args) {


		
		try {
			// Create a URL for the desired page
			URL url = new URL("http://kerneltuner.pedjaapps.in.rs/ktuner/version");

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
			URL url = new URL("http://kerneltuner.pedjaapps.in.rs/ktuner/changelog");

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
	


    @Override
	protected void onPostExecute(Object result) {

        //KernelTuner.this.pd.dismiss();
        
		try {
		PackageInfo	pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		version = pInfo.versionName;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		
        if (remoteversion!= null && !remoteversion.equals(version)){
        	AlertDialog.Builder builder = new AlertDialog.Builder(
                    KernelTuner.this);

    builder.setTitle("Kernel Tuner "+ remoteversion+" Available");

    WebView cl = new WebView(KernelTuner.this);
    cl.loadUrl("http://kerneltuner.pedjaapps.in.rs/ktuner/changelog_latest.html");
    builder.setMessage("Download Now?");

    builder.setIcon(R.drawable.icon);

    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {
            	//download();
            	
            	// execute this when the downloader must be fired
            	DownloadNewVersion downloadNewVersion = new DownloadNewVersion();
            	downloadNewVersion.execute("http://kerneltuner.pedjaapps.in.rs/ktuner/KernelTuner-" + remoteversion + ".php");
            	
            }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
		public void onClick(DialogInterface dialog, int which) {
      
        }
});
    builder.setView(cl);
    AlertDialog alert = builder.create();
			
			alert.show();
		
		
		}
		else if(remoteversion==null){
			//Toast.makeText(getApplicationContext(), "Problem connecting to server", Toast.LENGTH_LONG).show();
		Log.e("Update Check","problem connecting to update server");
		}
        else{
        	//Toast.makeText(getApplicationContext(), "You have the latest version", Toast.LENGTH_LONG).show();
       Log.d("Update Check","Latest version already installed");
        }


		

    }

	}

private class mountDebugFs extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {

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

     @Override
	protected void onPostExecute(Object result) {

 
     }

	}


private class cpu1Toggle extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
         
         File file = new File(CPU1_CURR_GOV);
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 " + cpu1online+"\n");
         localDataOutputStream.writeBytes("echo 0 > " +cpu1online +"\n");
         localDataOutputStream.writeBytes("chown system " +cpu1online +"\n");
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
            localDataOutputStream.writeBytes("chmod 666 "+cpu1online+"\n");
            localDataOutputStream.writeBytes("echo 1 > "+cpu1online+"\n");
            localDataOutputStream.writeBytes("chmod 444 "+cpu1online+"\n");
            localDataOutputStream.writeBytes("chown system "+cpu1online+"\n");
            
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

     @Override
	protected void onPostExecute(Object result) {

             KernelTuner.this.pd.dismiss();
         
     }

	}

private class cpu2Toggle extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
        // Log.i("MyApp", "Background thread starting");
         
         File file = new File(CPU2_CURR_GOV);
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 " + cpu2online+"\n");
         localDataOutputStream.writeBytes("echo 0 > " +cpu2online +"\n");
         localDataOutputStream.writeBytes("chown system " +cpu2online +"\n"); 
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
            localDataOutputStream.writeBytes("chmod 666 "+cpu2online+"\n");
            localDataOutputStream.writeBytes("echo 1 > "+cpu2online+"\n");
            localDataOutputStream.writeBytes("chmod 444 "+cpu2online+"\n");
            localDataOutputStream.writeBytes("chown system "+cpu2online+"\n");
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

     @Override
	protected void onPostExecute(Object result) {

             KernelTuner.this.pd.dismiss();
         
     }

	}

private class cpu3Toggle extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
         //Log.i("MyApp", "Background thread starting");
         
         File file = new File(CPU3_CURR_GOV);
     	try{
     	
     	InputStream fIn = new FileInputStream(file);
	
     	 Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
		
         DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
         localDataOutputStream.writeBytes("chmod 777 " + cpu3online+"\n");
         localDataOutputStream.writeBytes("echo 0 > " +cpu3online +"\n");
         localDataOutputStream.writeBytes("chown system " +cpu3online +"\n");
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
            localDataOutputStream.writeBytes("chmod 666 "+cpu3online+"\n");
            localDataOutputStream.writeBytes("echo 1 > "+cpu3online+"\n");
            localDataOutputStream.writeBytes("chmod 444 "+cpu3online+"\n");
            localDataOutputStream.writeBytes("chown system "+cpu3online+"\n");
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

     @Override
	protected void onPostExecute(Object result) {
             KernelTuner.this.pd.dismiss();
         
     }

	}

private class cpuLoad extends AsyncTask<String, Void, Float> {
	
	

    
	@Override
	protected Float doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	        String load = reader.readLine();

	        String[] toks = load.split(" ");

	        long idle1 = Long.parseLong(toks[5]);
	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        try {
	            Thread.sleep(360);
	        } catch (Exception e) {}

	        reader.seek(0);
	        load = reader.readLine();
	        reader.close();

	        toks = load.split(" ");

	        long idle2 = Long.parseLong(toks[5]);
	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        return  ((float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)))*100;

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return  (float)0;
		
	}

	@Override
	protected void onPostExecute(Float result) {
		TextView cpuLoadTxt = (TextView)findViewById(R.id.textView1);
		
		ProgressBar cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
    	 cpuLoad.setProgress(Math.round(result));
 		cpuLoadTxt.setText(String.valueOf(Math.round(result))+"%");
     }

	}

private class enableTempMonitor extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
        
		Process localProcess;
		try {
			localProcess = Runtime.getRuntime().exec("su");
			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode\n");
			System.out.println("enabling cpu temp");
			localDataOutputStream.writeBytes("echo enabled > /sys/devices/virtual/thermal/thermal_zone1/mode\n");
			 localDataOutputStream.writeBytes("exit\n");
		     localDataOutputStream.flush();
		     localDataOutputStream.close();
		     localProcess.waitFor();
		     localProcess.destroy();
		     System.out.println("cpu temp enabled");
		}
		catch(Exception e){
			
		}
		
         return "";
     }

     

	}

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main);

sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
boolean update = sharedPrefs.getBoolean("update", true);
if(update==true){
new updateCheck().execute();
}
new enableTempMonitor().execute();
batteryLevel = (TextView) this.findViewById(R.id.textView42);
batteryTemp = (TextView) this.findViewById(R.id.textView40);

tempPref = sharedPrefs.getString("temp", "celsius");


boolean ads = sharedPrefs.getBoolean("ads", true);
if (ads==true){AdView adView = (AdView)this.findViewById(R.id.ad);
adView.loadAd(new AdRequest());}




	changelog();
	
	File file = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
	try{

		InputStream fIn = new FileInputStream(file);

		
	}
	catch(FileNotFoundException e){ 
		new mountDebugFs().execute();
	}

	
	
	Button gpu = (Button)findViewById(R.id.button3);
	gpu.setOnClickListener(new OnClickListener(){
		
		@Override
		public void onClick(View v) {		
			Intent myIntent = new Intent(KernelTuner.this, gpu.class);
			KernelTuner.this.startActivity(myIntent);

			}
	
	});
	
	Button voltage = (Button)findViewById(R.id.button6);
	voltage.setOnClickListener(new OnClickListener(){
		
		@Override
		public void onClick(View v) {
			
			Intent myIntent = new Intent(KernelTuner.this, VoltageActivity.class);
			KernelTuner.this.startActivity(myIntent);
			}
	
	});


	readFreqs();
initialCheck();


new Thread(new Runnable() {
	@Override
    public void run() {
        while (thread) {
            try {
                Thread.sleep(1000);
                mHandler.post(new Runnable() {

                	@Override
                    public void run() {
                      
                    	ReadCPU0Clock();
                    	ReadCPU0maxfreq();
                    	cpuTemp();
                    	new cpuLoad().execute();
                    	//times();
                    	
                    	
                    	
                    	
                    	if(new File(cpu1online).exists()){
                    	
                    		
                        	ReadCPU1Clock();
                        	ReadCPU1maxfreq();
                        	
                    	}
                    	if(new File(cpu2online).exists()){
                    		ReadCPU2Clock();
                    		ReadCPU2maxfreq();
                        	
                        	
                    	}
                    	if(new File(cpu3online).exists()){
                    		ReadCPU3Clock();
                    		ReadCPU3maxfreq();
                        	
                    	}
               
                		
                   
                    	
                		
                    	
                    }
                });
            } catch (Exception e) {
               
            }
        }
    }
}).start();


Button cpu = (Button)this.findViewById(R.id.button2);
cpu.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		Intent myIntent = new Intent(KernelTuner.this, CPUActivity.class);
		KernelTuner.this.startActivity(myIntent);
	}
});

Button tis = (Button)this.findViewById(R.id.button5);
tis.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		Intent myIntent = new Intent(KernelTuner.this, TISActivity.class);
		KernelTuner.this.startActivity(myIntent);
		
	}
});
	
Button mpdec = (Button)this.findViewById(R.id.button7);
mpdec.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		Intent myIntent = new Intent(KernelTuner.this, mpdecision.class);
		KernelTuner.this.startActivity(myIntent);
		
	}
});

	

Button buttongpu = (Button)this.findViewById(R.id.button4);
buttongpu.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, miscTweaks.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

   
Button cpu1toggle = (Button)this.findViewById(R.id.button1);
cpu1toggle.setOnClickListener(new OnClickListener()
{
	
	@Override
public void onClick(View v) {
	
	
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu1Toggle().execute();
	
	
}});

Button cpu2toggle = (Button)this.findViewById(R.id.button8);
cpu2toggle.setOnClickListener(new OnClickListener()
{
	
	@Override
public void onClick(View v) {
	
	
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu2Toggle().execute();
	
	
}});

Button cpu3toggle = (Button)this.findViewById(R.id.button9);
cpu3toggle.setOnClickListener(new OnClickListener()
{
	
	@Override
public void onClick(View v) {
	
	
	KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, "Working..", "Applying settings...", true, false);
	new cpu3Toggle().execute();
	
	
}});

Button governor = (Button)findViewById(R.id.button10);
governor.setOnClickListener(new OnClickListener(){

	@Override
	public void onClick(View v) {
		Intent myIntent = new Intent(KernelTuner.this, GovernorActivity.class);
		KernelTuner.this.startActivity(myIntent);
		
	}
	
});

Button swap = (Button)this.findViewById(R.id.button13);
swap.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, Swap.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

Button profiles = (Button)this.findViewById(R.id.button12);
profiles.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, profiles.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

Button thermald = (Button)this.findViewById(R.id.button11);
thermald.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, thermald.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

Button about = (Button)this.findViewById(R.id.button15);
about.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, about.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});

Button sys = (Button)this.findViewById(R.id.button14);
sys.setOnClickListener(new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		
		
			Intent myIntent = new Intent(KernelTuner.this, SystemInfo.class);
			KernelTuner.this.startActivity(myIntent);
		
	}
});


}



@Override
public void onPause() {
	

    super.onPause();
    
}

@Override
protected void onResume()
{
	this.registerReceiver(this.mBatInfoReceiver, 
			  new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    initialCheck();
//new info().execute();
    
  
SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
String boot = sharedPrefs.getString("boot", "");
if(boot.equals("init.d")){
	initdExport();
}
else{
	new Initd().execute(new String[] {"rm"});
}
  
    	
    	
    super.onResume();
    
}

@Override
public void onStop() {
	if (mBatInfoReceiver != null){
        unregisterReceiver(mBatInfoReceiver);
        
        mBatInfoReceiver = null;
    }
 
       super.onStop();
    
}
@Override
public void onDestroy() {
   
	
	thread=false;
AppWidgetBig updateBig = new AppWidgetBig();
	Context context = this;
	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_4x4);
	ComponentName thisWidget = new ComponentName(context, AppWidgetBig.class);
	appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	int[] appWidgetIds = null;
	updateBig.onUpdate(context, appWidgetManager,  appWidgetIds);
	
	
AppWidget updateSmall = new AppWidget();
	RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
	ComponentName thisWidgetSmall = new ComponentName(context, AppWidget.class);
	appWidgetManager.updateAppWidget(thisWidgetSmall, remoteViewsSmall);
	
	updateSmall.onUpdate(context, appWidgetManager,  appWidgetIds);
	
       super.onDestroy();
    
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
			editor.putString("version", version);
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{}
	

}

	public void battTempWarning(){
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
    	anim.setDuration(500); 
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
    	anim.setDuration(500); 
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
    	anim.setDuration(500); 
    	anim.setStartOffset(20);
    	anim.setRepeatMode(Animation.REVERSE);
    	anim.setRepeatCount(Animation.INFINITE);
    	cputemptxt.startAnimation(anim);
    	
	}
	public void cpuTempWarningStop(){
		cputemptxt.clearAnimation();
	}
	
	

	public void times(){
		TextView deepSleep = (TextView)findViewById(R.id.textView31);
		TextView uptime = (TextView)findViewById(R.id.textView30);
			uptime.setText(CPUInfo.uptime());
			deepSleep.setText(CPUInfo.deepSleep());
			
		
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
		
		
	      if (tempPref.equals("fahrenheit"))
	      {
	    	  cputemp = String.valueOf((int)(Double.parseDouble(cputemp)*1.8)+32);
		  		cputemptxt.setText(cputemp+"°F");
		  		int temp = Integer.parseInt(cputemp);
		  		
		  		if(temp<113){
		  	    	  cputemptxt.setTextColor(Color.GREEN);
		  	    	  cpuTempWarningStop();
		  	      }
		  	      else if(temp>=113 && temp<138){
		  	    	  cputemptxt.setTextColor(Color.YELLOW);
		  	    	  cpuTempWarningStop();
		  	      }
		  	      
		  	      else if(temp>=138){
		  	    	  cpuTempWarning();
		  	    	  cputemptxt.setTextColor(Color.RED);

		  	      }
	      }
	      
	      else if(tempPref.equals("celsius")){
	    	  cputemptxt.setVisibility(View.VISIBLE);
	  		cputemptxte.setVisibility(View.VISIBLE);
	  		cputemptxt.setText(cputemp+"°C");
	  		int temp = Integer.parseInt(cputemp);
	  		if(temp<45){
	  	    	  cputemptxt.setTextColor(Color.GREEN);
	  	    	  cpuTempWarningStop();
	  	      }
	  	      else if(temp>=45 && temp<=59){
	  	    	  cputemptxt.setTextColor(Color.YELLOW);
	  	    	  cpuTempWarningStop();
	  	      }
	  	      
	  	      else if(temp>59){
	  	    	  cpuTempWarning();
	  	    	  cputemptxt.setTextColor(Color.RED);

	  	      }
	      }
	      else if(tempPref.equals("kelvin")){
	    	  cputemp=String.valueOf((int)(Double.parseDouble(cputemp)+273.15));
	    	  cputemptxt.setVisibility(View.VISIBLE);
	  		cputemptxte.setVisibility(View.VISIBLE);
	  		cputemptxt.setText(cputemp+"°K");
	  		System.out.println(cputemp);
	  		int temp = Integer.parseInt(cputemp);
	  		System.out.println("temp int"+temp);
	  		if(temp<318){
	  	    	  cputemptxt.setTextColor(Color.GREEN);
	  	    	  cpuTempWarningStop();
	  	      }
	  	      else if(temp>=318 && temp<=332){
	  	    	  cputemptxt.setTextColor(Color.YELLOW);
	  	    	  cpuTempWarningStop();
	  	      }
	  	      
	  	      else if(temp>332){
	  	    	  cpuTempWarning();
	  	    	  cputemptxt.setTextColor(Color.RED);

	  	      }
	      }
		
		myReader.close();

	} catch (Exception e2) {

		
		
	}
}

public void initialCheck(){
	
	if(CPUInfo.cpu1Online()==true){
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
	if(CPUInfo.cpu2Online()==true){
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
	if(CPUInfo.cpu3Online()==true){
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
			Button cpu = (Button)findViewById(R.id.button2);
		cpu.setVisibility(View.GONE);
		}
	

	}
	
		 	File file = new File(CPUInfo.VOLTAGE_PATH);
		try{
			InputStream fIn = new FileInputStream(file);
		}
		catch(FileNotFoundException e){ 
		 	File file2 = new File(CPUInfo.VOLTAGE_PATH_TEGRA_3);
			try{
				InputStream fIn = new FileInputStream(file2);
			}
			catch(FileNotFoundException ex){ 
			Button voltage = (Button)findViewById(R.id.button6);
			voltage.setVisibility(View.GONE);

			}

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
		
		File file6 = new File("/sys/kernel/msm_mpdecision/conf/enabled");
		try{
			InputStream fIn = new FileInputStream(file6);
		}
		catch(FileNotFoundException e){ 
		Button mpdec = (Button)findViewById(R.id.button7);
		mpdec.setVisibility(View.GONE);

		}
		
		File file7 = new File("/sys/kernel/msm_thermal/conf/allowed_low_freq");
		try{
			InputStream fIn = new FileInputStream(file7);
		}
		catch(FileNotFoundException e){ 
		Button td = (Button)findViewById(R.id.button11);
		td.setVisibility(View.GONE);

		}
	
		 
		
	}

public void ReadCPU0Clock()
{
	 

	try {
		
		File myFile = new File(CPU0_CURR_FREQ);
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
		iscVa = "offline";
	}
	
	
	cpu0progress();
cpu0update();

}


public void ReadCPU1Clock()
{
	 

	try {
		
		File myFile = new File(CPU1_CURR_FREQ);
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
	
	
	cpu1progress();
	cpu1update();

}

public void ReadCPU2Clock()
{
	 

	try {
		
		File myFile = new File(CPU2_CURR_FREQ);
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
		freqcpu2="offline";
	}
	
	
	cpu2progress();
cpu2update();

}


public void ReadCPU3Clock()
{
	 

	try {
		
		File myFile = new File(CPU3_CURR_FREQ);
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
		freqcpu3 = "offline";

	}
	
	
	cpu3progress();
	cpu3update();

}



public void initdExport(){
	
	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
	  
	  
	  String delaynew = sharedPrefs.getString("delaynew", "");
	  String pausenew = sharedPrefs.getString("pausenew", "");
	  String thruploadnew = sharedPrefs.getString("thruploadnew", "");
	  String thrupmsnew = sharedPrefs.getString("thrupmsnew", "");
	  String thrdownloadnew = sharedPrefs.getString("thrdownloadnew", "");
	  String thrdownmsnew = sharedPrefs.getString("thrdownmsnew", "");
	  String ldt = sharedPrefs.getString("ldt", "");
	  String s2w = sharedPrefs.getString("s2w", "");
	  String s2wStart = sharedPrefs.getString("s2wStart","");
	  String s2wEnd = sharedPrefs.getString("s2wEnd","");
	  
	  String p1freq = sharedPrefs.getString("p1freq", "");
	  String p2freq = sharedPrefs.getString("p2freq", "");
	  String p3freq = sharedPrefs.getString("p3freq", "");
	  String p1low = sharedPrefs.getString("p1low", "");
	  String p1high = sharedPrefs.getString("p1high", "");
	  String p2low = sharedPrefs.getString("p2low", "");
	  String p2high = sharedPrefs.getString("p2high", "");
	  String p3low = sharedPrefs.getString("p3low", "");
	  String p3high = sharedPrefs.getString("p3high", "");
	  boolean swap = sharedPrefs.getBoolean("swap",false);
	  String swapLocation = sharedPrefs.getString("swap_location","");
	  String swappiness = sharedPrefs.getString("swappiness","");
	  
	  StringBuilder gpubuilder = new StringBuilder();
		
		    gpubuilder.append("#!/system/bin/sh");
		    gpubuilder.append("\n");
		    if(!gpu3d.equals("")){
		    	gpubuilder.append("echo " + "\""+gpu3d + "\""+" > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
		    	gpubuilder.append("\n");
		    }
		    if(!gpu2d.equals("")){
		    	gpubuilder.append("echo " + "\""+gpu2d + "\""+" > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
		    	gpubuilder.append("\n");
		    	gpubuilder.append("echo " + "\""+gpu2d + "\""+" > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
		    	gpubuilder.append("\n");

		    }
		
		 
	  String gpu = gpubuilder.toString();
			 
	  StringBuilder cpubuilder = new StringBuilder();
	  
	  cpubuilder.append("#!/system/bin/sh");
	  cpubuilder.append("\n");
	  /**
	   * cpu0
	   * */
	  if(!cpu0gov.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
	  		"echo " + "\""+cpu0gov+"\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
	  }
	  if(!cpu0max.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu0max + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n");
	  }
	  if(!cpu0min.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n" +
		"echo " + "\""+cpu0min + "\""+" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n");
	  }
	  /**
	   * cpu1
	   * */
	  if(!cpu1gov.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
	  		"echo " + "\""+cpu1gov+"\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
	  }
	  if(!cpu1max.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu1max + "\""+" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n");
	  }
	  if(!cpu1min.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n" +
		"echo " + "\""+cpu1min + "\""+" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n");
	  }
	  
	  /**
	   * cpu2
	   * */
	  if(!cpu2gov.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n" +
	  		"echo " + "\""+cpu2gov+"\"" + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n");
	  }
	  if(!cpu2max.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu2max + "\""+" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n");
	  }
	  if(!cpu2min.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n" +
		"echo " + "\""+cpu2min + "\""+" > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n\n");
	  }
	  /**
	   * cpu3
	   * */
	  
	  if(!cpu3gov.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n" +
	  		"echo " + "\""+cpu3gov+"\"" + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n");
	  }
	  if(!cpu3max.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n" +
		"echo " + "\""+cpu3max + "\""+" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n");
	  }
	  if(!cpu3min.equals("")){
		  cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n" +
		"echo " + "\""+cpu3min + "\""+" > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n\n");
	  }
	  List<String> govSettings = CPUInfo.govSettings();
      List<String> availableGovs = CPUInfo.availableGovs();
      
      for(String s : availableGovs){
   	   for(String st : govSettings){
   		   String temp = sharedPrefs.getString(s+"_"+st, "");
   	   		  
     		    if(!temp.equals("")){
     		    	cpubuilder.append("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
     		    	cpubuilder.append("echo " + "\""+temp+"\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
     		    	
     		    }
   	   }
      }
	  String cpu = cpubuilder.toString();
		
	  StringBuilder miscbuilder = new StringBuilder();
	  
	  miscbuilder.append("#!/system/bin/sh \n\n"+"#mount debug filesystem\n"+
			  "mount -t debugfs debugfs /sys/kernel/debug \n\n");
	  if(!vsync.equals("")){
	  miscbuilder.append("#vsync\n"+
			  "chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable \n"+
			  "chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n"+
			  "chmod 777 /sys/kernel/debug/msm_fb/0/backbuff \n"+
			  "echo " + "\""+vsync + "\""+" > /sys/kernel/debug/msm_fb/0/vsync_enable \n"+
			  "echo " + "\""+hw + "\""+" > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
			  "echo " + "\""+backbuff + "\""+" > /sys/kernel/debug/msm_fb/0/backbuff \n\n");
	  }
	  if(!led.equals("")){
		  miscbuilder.append("#capacitive buttons backlight\n"+"chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n"+
				  "echo " +"\""+ led + "\""+" > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n\n");
	  }
	  if(!fastcharge.equals("")){
		  miscbuilder.append("#fastcharge\n"+"chmod 777 /sys/kernel/fast_charge/force_fast_charge \n"+
				  "echo " + "\""+fastcharge + "\""+" > /sys/kernel/fast_charge/force_fast_charge \n\n" );
	  }
	  if(!cdepth.equals("")){
		  miscbuilder.append("#color depth\n"+"chmod 777 /sys/kernel/debug/msm_fb/0/bpp \n"+
	  "echo " + "\""+cdepth + "\""+" > /sys/kernel/debug/msm_fb/0/bpp \n\n");
	  }
	  
	  if(!mpdecisionscroff.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core \n"+
				  "echo " + "\""+mpdecisionscroff + "\""+" > /sys/kernel/msm_mpdecision/conf/scroff_single_core \n");
	  }
	  if(!delaynew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/delay \n"+
	  "echo " + "\""+delaynew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/delay \n");
	  }
	  if(!pausenew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/pause \n"+
	  "echo " + "\""+pausenew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/pause \n");
	  }
	  if(!thruploadnew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n"+
	  "echo " + "\""+thruploadnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n");
					
	  }
	  if(!thrdownloadnew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n"+
	  "echo " + "\""+thrdownloadnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n");
					
	  }
	  if(!thrupmsnew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up"+
	  "echo " + "\""+thrupmsnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/twts_threshold_up \n");
	  }
	  if(!thrdownmsnew.equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down"+
	  "echo " + "\""+thrdownmsnew.trim() + "\""+" > /sys/kernel/msm_mpdecision/conf/twts_threshold_down \n\n");
	  }
	  if(!sdcache.equals("")){
		  miscbuilder.append("#sd card cache size\n"+
	  "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n" +
					"chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n" +
					 "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n" +
				"echo " + "\""+sdcache + "\""+" > /sys/block/mmcblk1/queue/read_ahead_kb \n"+
				"echo " + "\""+sdcache + "\""+" > /sys/block/mmcblk0/queue/read_ahead_kb \n"+
				"echo " + "\""+sdcache + "\""+" > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n\n");
	  }
	  if(!io.equals("")){
		  miscbuilder.append("#IO scheduler\n"+
				  "chmod 777 /sys/block/mmcblk0/queue/scheduler \n"+
			        "chmod 777 /sys/block/mmcblk1/queue/scheduler \n"+
					"echo " + "\""+io + "\""+" > /sys/block/mmcblk0/queue/scheduler \n"+
					"echo " +"\""+ io + "\""+" > /sys/block/mmcblk1/queue/scheduler \n\n");
	  }
	  if(!ldt.equals("")){
		  miscbuilder.append("#Notification LED Timeout\n"+
	  "chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n"+
	  "echo " + "\""+ldt + "\""+" > /sys/kernel/notification_leds/off_timer_multiplier\n\n");
	  }
	  if(!s2w.equals("")){
		  miscbuilder.append("#Sweep2Wake\n"+
	  "chmod 777 /sys/android_touch/sweep2wake\n"+
	  "echo " + "\""+s2w + "\""+" > /sys/android_touch/sweep2wake\n\n");
	  }
	  if(!s2wStart.equals("")){
		  miscbuilder.append("chmod 777 /sys/android_touch/sweep2wake_startbutton\n"+
			"echo "+ s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n"+
			"chmod 777 /sys/android_touch/sweep2wake_endbutton\n"+
			"echo "+ s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n\n");
	  }
	  
	  if(!p1freq.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n"+
				  "echo " + "\""+p1freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
	  }
	  if(!p2freq.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n"+
				  "echo " + "\""+p2freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
	  }
	  if(!p3freq.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n"+
				  "echo " + "\""+p3freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
	  }
	  if(!p1low.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n"+
	  "echo " + "\""+p1low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
	  }
	  if(!p1high.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n"+
	  "echo " + "\""+p1high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
	  }
	  if(!p2low.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n"+
	  "echo " + "\""+p2low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
	  }
	  if(!p2high.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n"+
	  "echo " + "\""+p2high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
	  }
	  if(!p3low.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_low\n"+
	  "echo " + "\""+p3low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_high_low\n");
	  }
	  if(!p3high.trim().equals("")){
		  miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_high\n"+
	  "echo " + "\""+p3high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_high_high\n\n");
	  }
	  if(swap==true){
		  miscbuilder.append("echo "+swappiness+" > /proc/sys/vm/swappiness\n"
	  +"swapon "+swapLocation.trim()+"\n\n"
				  );
	        
	        
	        }
	        else if(swap==false){
	        	miscbuilder.append("swapoff "+swapLocation.trim()+"\n\n");
	            
	        }
	 
	  miscbuilder.append("#Umount debug filesystem\n"+
	  "umount /sys/kernel/debug \n");
	  String misc = miscbuilder.toString();
		
        
	  	   
	  StringBuilder voltagebuilder = new StringBuilder();
	  voltagebuilder.append("#!/system/bin/sh \n");
	  for(String s : CPUInfo.voltageFreqs()){
			String temp = sharedPrefs.getString("voltage_"+s, "");
		    if(!temp.equals("")){
			voltagebuilder.append("echo " + "\""+temp+"\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
		    }
		}
	  String voltage = voltagebuilder.toString();
	  try { 
             
          FileOutputStream fOut = openFileOutput("99ktcputweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(cpu);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	  try { 
          
          FileOutputStream fOut = openFileOutput("99ktgputweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(gpu);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	  try { 
          
          FileOutputStream fOut = openFileOutput("99ktmisctweaks",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(misc);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  }
	 
	  try { 
          
          FileOutputStream fOut = openFileOutput("99ktvoltage",
                                                  MODE_WORLD_READABLE);
          OutputStreamWriter osw = new OutputStreamWriter(fOut); 
          osw.write(voltage);        
          osw.flush();
          osw.close();

  } catch (IOException ioe) {
          ioe.printStackTrace();
  } 
	  new Initd().execute(new String[] {"apply"});
}

public void readFreqs()
{
	 

	try {
		
		File myFile = new File(CPU0_FREQS);
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
		if(Integer.parseInt(frequencies.get(0))>Integer.parseInt(frequencies.get(frequencies.size()))){
				Collections.reverse(freqlist);
			}
		
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

 				frequencies.add(freq);

 			}
 			/*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 			boolean ov = sharedPrefs.getBoolean("override", false);
 			if(ov==true){
 			Collections.reverse(frequencies);
 			}*/ 
 			if(frequencies.get(0).length()>frequencies.get(frequencies.size()-1).length()){
 				Collections.reverse(frequencies);
 			}
 			//System.out.println();
 			String[] strarray = frequencies.toArray(new String[0]);
 			frequencies.clear();
 			//System.out.println(frequencies);
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
		System.out.println("failed to read frequencies");
		}
	}
	
}


public void ReadCPU0maxfreq()
{
	 

	try {
		
		File myFile = new File(CPU0_MAX_FREQ);
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
		
		File myFile = new File(CPU1_MAX_FREQ);
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
		
		File myFile = new File(CPU2_MAX_FREQ);
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
		
		File myFile = new File(CPU3_MAX_FREQ);
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
if(freqlist!=null){
ProgressBar cpu0progbar = (ProgressBar)findViewById(R.id.progressBar1);
cpu0progbar.setMax(freqlist.indexOf(cpu0max.trim())+1);
cpu0progbar.setProgress(freqlist.indexOf(iscVa.trim())+1);
}

}

public void cpu1update()
{

TextView cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
cpu1prog.setText(iscVa2.trim());
}
public void cpu1progress()
{
	if(freqlist!=null){
ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
cpu1progbar.setMax(freqlist.indexOf(cpu1max.trim())+1);
cpu1progbar.setProgress(freqlist.indexOf(iscVa2.trim())+1);
	}
}

public void cpu2update()
{

TextView cpu2prog = (TextView)this.findViewById(R.id.ptextView7);
cpu2prog.setText(freqcpu2.trim());
}

public void cpu2progress()
{
	if(freqlist!=null){
ProgressBar cpu2progbar = (ProgressBar)findViewById(R.id.progressBar3);
cpu2progbar.setMax(freqlist.indexOf(cpu2max.trim())+1);
cpu2progbar.setProgress(freqlist.indexOf(freqcpu2.trim())+1);
	}
}

public void cpu3update()
{

TextView cpu3prog = (TextView)this.findViewById(R.id.ptextView8);
cpu3prog.setText(freqcpu3.trim());
}
public void cpu3progress()
{
	if(freqlist!=null){
ProgressBar cpu3progbar = (ProgressBar)findViewById(R.id.progressBar4);

cpu3progbar.setMax(freqlist.indexOf(cpu3max.trim())+1);
cpu3progbar.setProgress(freqlist.indexOf(freqcpu3.trim())+1);
	}
}




@Override
public boolean onCreateOptionsMenu(Menu menu) {
   
	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options, menu);

    return true;
}
@Override
public boolean onPrepareOptionsMenu (Menu menu) {
    
    return true;
}



@Override
public boolean onOptionsItemSelected(MenuItem item) {
  
	if (item.getItemId() == R.id.settings) {
        startActivity(new Intent(this, Preferences.class));
      
    }
    if (item.getItemId() == R.id.changelog) {
        startActivity(new Intent(this, changelog.class));
       
    }

		
	if (item.getItemId() == R.id.check) {
        startActivity(new Intent(this, check.class));
        
    }	
	
	
    return super.onOptionsItemSelected(item);
}

@Override
public void onBackPressed() {
if( mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000 )
{
mToast = Toast.makeText( this, "Press back again to exit.", Toast.LENGTH_SHORT );
mToast.show();
mLastBackPressTime = java.lang.System.currentTimeMillis();
}
else
{
if( mToast != null )
mToast.cancel();
KernelTuner.this.finish();


mLastBackPressTime = 0;
}
}



}

