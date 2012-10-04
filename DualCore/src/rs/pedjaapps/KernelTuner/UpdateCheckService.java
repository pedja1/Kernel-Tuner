package rs.pedjaapps.KernelTuner;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class UpdateCheckService extends Service
{
	int mStartMode;
	boolean check = true;
	double temperature;
	boolean tempPref;
	String changelog;
	public String cputemp;
	SharedPreferences sharedPrefs;
	String remoteversion;
	String version;
	private ProgressDialog pd;
	
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
				URL url = new URL("http://kerneltuner.co.cc/ktuner/version");

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
				URL url = new URL("http://kerneltuner.co.cc/ktuner/changelog");

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
		
			
	    }


	    protected void onPostExecute(Object result) {
		
	     //   UpdateCheckService.this.data = result;
	        
	        
			try {
			PackageInfo	pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	        if (remoteversion!= null && !remoteversion.equals(version)){
	        	download();
			
			
			}
			else if(remoteversion==null){
				Toast.makeText(getApplicationContext(), "Problem connecting to server", Toast.LENGTH_LONG).show();
			}
	        else{
	        	Toast.makeText(getApplicationContext(), "You have the latest version", Toast.LENGTH_LONG).show();
	        }


			

	    }

		}
	
    
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		

		//run();
		
		
		
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
		System.out.println("service started");
		new updateCheck().execute();
        return mStartMode;
    }
	
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	public void download(){
		 BroadcastReceiver onComplete=new BroadcastReceiver() {
			    public void onReceive(Context ctxt, Intent intent) {
			        // Do Something
			    	intent = new Intent(Intent.ACTION_VIEW);
			        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "KernelTuner-" + remoteversion + ".apk")), "application/vnd.android.package-archive");
			       System.out.println( Environment.getExternalStorageDirectory() + "/download/" + "KernelTuner-" + remoteversion + ".apk");
			        startActivity(intent);
			    }
			};
		String url = "http://kerneltuner.co.cc/ktuner/KernelTuner-" + remoteversion + ".php";
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		request.setDescription("Downloading new version");
		request.setTitle("Kernel Tuner-" + remoteversion + ".apk");
		//in order for this if to run, you must use the android 3.2 to compile your app
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		try{ 
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "KernelTuner-" + remoteversion + ".apk");
		DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request); 
		
		registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "SD card is not mounted", Toast.LENGTH_LONG).show();
		}
		

		

	//get download service and enqueue file
		System.out.println(request);
	}
	
	

	


}
