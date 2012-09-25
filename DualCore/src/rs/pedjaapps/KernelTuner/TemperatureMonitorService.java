package rs.pedjaapps.KernelTuner;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TemperatureMonitorService extends Service
{
	boolean check = true;
	double temperature;
	boolean tempPref;
	String unit;
	public String cputemp;
	SharedPreferences sharedPrefs;
	String tempcheck;
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent intent) {
	      // TODO Auto-generated method stub
	    	
	    	
	     
	      temperature =  intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10;
	      tempPref = sharedPrefs.getBoolean("temp", false);
	      if (tempPref==false)
	      {
	    	  
	    	  if(temperature>60){
	    		  unit = "C";
	    		  System.out.println(unit);
		    	  notif();
		    	  
		      }
	      }
	      else if(tempPref==true){
	    	  temperature = (temperature*1.8)+32;
	    	  if(temperature>140){
	    		  unit = "F";
	    		  System.out.println(unit);
		    	  notif();
		      }
	      }
		      

	    }
	  };
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//run();
		
		System.out.println("service started");
		this.registerReceiver(this.mBatInfoReceiver, 
				  new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
	}

	
	
	@Override
	public void onDestroy() {
		System.out.println("service destroyed");
		check=false;
		super.onDestroy();
	}

	
	public void notif(){
		String ns = Context.NOTIFICATION_SERVICE;
		  NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		  int icon = R.drawable.icon;
		  CharSequence tickerText = "Temperature Warning";
		  long when = System.currentTimeMillis();

		  Notification notification = new Notification(icon, tickerText, when);
		  
		  Context context = getApplicationContext();
		  CharSequence contentTitle = "Battery Temperature to high";
		  CharSequence contentText = "Temperature: "+ temperature + "°"+ unit;
		  Intent notificationIntent = new Intent(this, KernelTuner.class);
		  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		  
		  final int HELLO_ID = 1;

		  mNotificationManager.notify(HELLO_ID, notification);
	}
	
	public void notifCpu(){
		String ns = Context.NOTIFICATION_SERVICE;
		  NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		  int icon = R.drawable.icon;
		  CharSequence tickerText = "Temperature Warning";
		  long when = System.currentTimeMillis();

		  Notification notification = new Notification(icon, tickerText, when);
		  
		  Context context = getApplicationContext();
		  CharSequence contentTitle = "CPU Temperature to high";
		  CharSequence contentText = "Temperature: "+ cputemp + "°"+ unit;
		  Intent notificationIntent = new Intent(this, KernelTuner.class);
		  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		  
		  final int HELLO_ID = 1;

		  mNotificationManager.notify(HELLO_ID, notification);
	}
	
	public void run(){
		while(check){
			try {
				tempcheck = sharedPrefs.getString("tempCheckInterval", "180000");
		int temp = Integer.parseInt(tempcheck);
		Thread.sleep(temp);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
				System.out.println(cputemp);
				
			      if (tempPref==true)
			      {
			    	  cputemp = String.valueOf((int)(Double.parseDouble(cputemp)*1.8)+32);
				  		
				  		int temp = Integer.parseInt(cputemp);
				  		
				  		 if(temp>138){
				  			unit = "F";
				  	    	 notifCpu();
				  	    	 
				  	      }
			      }
			      else if(tempPref==false){
			    	  
			  		int temp = Integer.parseInt(cputemp);
			  		 if(temp>59){
			  			unit = "C";
			  			 notifCpu();
			  	      }
			      }
				
				myReader.close();

			} catch (Exception e2) {

				
				
			}
		}
	}
	
	


}
