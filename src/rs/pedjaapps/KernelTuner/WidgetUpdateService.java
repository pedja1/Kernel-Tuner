package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

import rs.pedjaapps.KernelTuner.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service {
  private static final String LOG = "";
public String led;
public String fastcharge;
public String vsync;
public String cdepth;
public String curentfreq;
public String gov;
public String battperc;
public String charge;
public int timeint;

public void read(){
	
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

		curentfreq = aBuffer;
		myReader.close();
		
	} catch (Exception e) {
		curentfreq = "offline";

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
		
		gov = aBuffer.trim();
		myReader.close();
		
	} catch (Exception e) {
		gov="offline";
	}

try {
	
	File myFile = new File("/sys/class/power_supply/battery/capacity");
	FileInputStream fIn = new FileInputStream(myFile);

	BufferedReader myReader = new BufferedReader(
			new InputStreamReader(fIn));
	String aDataRow = "";
	String aBuffer = "";
	while ((aDataRow = myReader.readLine()) != null) {
		aBuffer += aDataRow + "\n";
	}
	
	battperc = aBuffer.trim();
	myReader.close();
	
} catch (Exception e) {
	battperc="0";
}

try {
	
	File myFile = new File("/sys/class/power_supply/battery/charging_source");
	FileInputStream fIn = new FileInputStream(myFile);

	BufferedReader myReader = new BufferedReader(
			new InputStreamReader(fIn));
	String aDataRow = "";
	String aBuffer = "";
	while ((aDataRow = myReader.readLine()) != null) {
		aBuffer += aDataRow + "\n";
	}
	
	charge = aBuffer.trim();
	myReader.close();
	
} catch (Exception e) {
	charge="0";
}


}
  @Override
  public void onStart(Intent intent, int startId) {
    //Log.i(LOG, "Called");
    

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    ComponentName thisWidget = new ComponentName(getApplicationContext(),
        AppWidget.class);
    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
   // Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
    //Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));

    read();
    
    for (int widgetId : allWidgetIds) {
      // Create some random data
      int number = (new Random().nextInt(100));

      RemoteViews remoteViews = new RemoteViews(this
          .getApplicationContext().getPackageName(),
          R.layout.widget_2x1);
      //Log.w("Widget", String.valueOf(number));
      remoteViews.setTextViewText(R.id.textView5, curentfreq.substring(0, curentfreq.length()-4)+"Mhz");
		int fr = Integer.parseInt(curentfreq.substring(0, curentfreq.length()-4));
		if (fr <= 918){
			remoteViews.setTextColor(R.id.textView5, Color.YELLOW);
		}
		else if(fr<=1512 && fr>918){
			remoteViews.setTextColor(R.id.textView5, Color.GREEN);
		}
		else if(fr>1512){
			remoteViews.setTextColor(R.id.textView5, Color.RED);
		}
      remoteViews.setTextViewText(R.id.textView6, gov);
      int battperciconint = Integer.parseInt(battperc.substring(0, battperc.length()).trim());
      if (battperciconint <= 15 && battperciconint!=0 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_low);
			remoteViews.setTextColor(R.id.textView1, Color.RED);
		}
		else if(battperciconint > 30 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_full);
			remoteViews.setTextColor(R.id.textView1, Color.GREEN);

		}
		else if(battperciconint < 30 && battperciconint > 15 && charge.equals("0")){
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_half);
			remoteViews.setTextColor(R.id.textView1, Color.YELLOW);
		}
		else if(charge.equals("1")){
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_usb);
			remoteViews.setTextColor(R.id.textView1, Color.CYAN);
		}
		else if(charge.equals("2")){
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.battery_charge_ac);
			remoteViews.setTextColor(R.id.textView1, Color.CYAN);
		}
      remoteViews.setProgressBar(R.id.progressBar1, 100, battperciconint, false );
      remoteViews.setTextViewText(R.id.textView1, battperc+"%");
      // Register an onClickListener
      Intent clickIntent = new Intent(this.getApplicationContext(),
          AppWidget.class);

      clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
          allWidgetIds);
      final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 	 
 	 
	  String timer = sharedPrefs.getString("widget_time", "");
	  //System.out.println(timer);
	  
		 	try{
				timeint = Integer.parseInt(timer.trim());
		}
		catch(Exception e){
			timeint = 1800;
		}
     
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
     
      AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(System.currentTimeMillis());
      calendar.add(Calendar.SECOND, timeint);
      alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 20*1000, pendingIntent);

      appWidgetManager.updateAppWidget(widgetId, remoteViews);
  
    }
    stopSelf();

    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
} 
