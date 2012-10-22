package rs.pedjaapps.KernelTuner;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


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


		@Override
		protected Object doInBackground(String... args) {
			//Log.i("DualCore", "Check for new version");

			
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
		protected void onPreExecute() {
	        super.onPreExecute();
	      //  DualCore.this.pd = ProgressDialog.show(DualCore.this, "Working..", "Checking for updates...", true, false );
		//	pd.setCancelable(true);
		
			
	    }


	    @Override
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
	        	notif();
			
			
			}
			else if(remoteversion==null){
				//Toast.makeText(getApplicationContext(), "Problem connecting to server", Toast.LENGTH_LONG).show();
			}
	        else{
	        	//Toast.makeText(getApplicationContext(), "You have the latest version", Toast.LENGTH_LONG).show();
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
		//System.out.println("service started");
		new updateCheck().execute();
        return mStartMode;
    }
	
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	public void notif(){
		String ns = Context.NOTIFICATION_SERVICE;
		  NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		  int icon = R.drawable.icon;
		  CharSequence tickerText = "Update Available";
		  long when = System.currentTimeMillis();

		  Notification notification = new Notification(icon, tickerText, when);
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		  Context context = getApplicationContext();
		  CharSequence contentTitle = "Update Avalable";
		  CharSequence contentText = "Kernel Tuner " + remoteversion;
		  Intent notificationIntent = new Intent(this, KernelTuner.class);
		  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

		  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		  
		  final int HELLO_ID = 1;

		  mNotificationManager.notify(HELLO_ID, notification);
	}
	
	

}
