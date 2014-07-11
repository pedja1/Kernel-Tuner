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
package rs.pedjaapps.kerneltuner.receiver;

import java.io.*;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import java.util.Calendar;
import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;

public class WidgetToggle extends AppWidgetProvider {

	public static String ACTION_WIDGET_TOGGLE_CPU1 = "ActionReceiverToggle1";
	public static String ACTION_WIDGET_TOGGLE_CPU2 = "ActionReceiverToggle2";
	public static String ACTION_WIDGET_TOGGLE_CPU3 = "ActionReceiverToggle3";
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	
	RemoteViews remoteViews;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    
		int bgRes = R.drawable.lcd_background_grey;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String widgetBgPref = pref.getString("widget_bg","grey");
		if(widgetBgPref.equals("grey")){
			bgRes = R.drawable.lcd_background_grey;
		}
		else if(widgetBgPref.equals("dark")){
			bgRes = R.drawable.appwidget_dark_bg;
		}
		else if(widgetBgPref.equals("transparent")){
			bgRes = 0;
		}
		
	    Intent active = new Intent(context, WidgetToggle.class);
	    active.setAction(ACTION_WIDGET_TOGGLE_CPU1);
	    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	    remoteViews.setOnClickPendingIntent(R.id.button1, actionPendingIntent);

	    active = new Intent(context, WidgetToggle.class);
	    active.setAction(ACTION_WIDGET_TOGGLE_CPU2);
	    actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	    remoteViews.setOnClickPendingIntent(R.id.button2, actionPendingIntent);

	    active = new Intent(context, WidgetToggle.class);
	    active.setAction(ACTION_WIDGET_TOGGLE_CPU3);
	    actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	    remoteViews.setOnClickPendingIntent(R.id.button3, actionPendingIntent);
	    
	    active = new Intent(context, WidgetToggle.class);
	    active.setAction(ACTION_WIDGET_REFRESH);
	    actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
	    remoteViews.setOnClickPendingIntent(R.id.refresh, actionPendingIntent);
	    
	    if(bgRes!=0){
	    remoteViews.setInt(R.id.widget_layout, "setBackgroundResource", bgRes);
	    }
	    setView(context);
	    String timer = pref.getString("widget_time", "");
	    double time;
	 	try
		{
			time = Double.parseDouble(timer.trim());
		}
		catch (Exception e)
		{
			time = 30;
		}
	    
	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.add(Calendar.SECOND, (int)time*60);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendar.getTimeInMillis(), actionPendingIntent);
	     
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	private void setView(Context context){
		
		if(IOHelper.cpu1Exists()==false){
	    	remoteViews.setViewVisibility(R.id.button1, View.GONE);
	    	remoteViews.setViewVisibility(R.id.cpu1, View.GONE);
	    	remoteViews.setViewVisibility(R.id.single_core_info, View.VISIBLE);
	    }
	    else{
		    remoteViews.setViewVisibility(R.id.button1, View.VISIBLE);
		    remoteViews.setViewVisibility(R.id.cpu1, View.VISIBLE);
		    if(new File(Constants.CPU1_CURR_GOV).exists()){
				remoteViews.setTextViewText(R.id.cpu1, "ON");
				remoteViews.setTextColor(R.id.cpu1, Color.GREEN);
			}
		    else{
		    	remoteViews.setTextViewText(R.id.cpu1, "OFF");
				remoteViews.setTextColor(R.id.cpu1, Color.RED);
		    }
		    if(IOHelper.cpu2Exists()==false){
		    	remoteViews.setViewVisibility(R.id.button2, View.GONE);
		    	remoteViews.setViewVisibility(R.id.cpu2, View.GONE);
		    }
		    else{
			    remoteViews.setViewVisibility(R.id.button2, View.VISIBLE);
			    remoteViews.setViewVisibility(R.id.cpu2, View.VISIBLE);
			    if(new File(Constants.CPU2_CURR_GOV).exists()){
					remoteViews.setTextViewText(R.id.cpu2, "ON");
					remoteViews.setTextColor(R.id.cpu2, Color.GREEN);
				}
			    else{
			    	remoteViews.setTextViewText(R.id.cpu2, "OFF");
					remoteViews.setTextColor(R.id.cpu2, Color.RED);
			    }
		    }
			if(IOHelper.cpu3Exists()==false){
		    	remoteViews.setViewVisibility(R.id.button3, View.GONE);
		    	remoteViews.setViewVisibility(R.id.cpu3, View.GONE);
		    }
		    else{
			    remoteViews.setViewVisibility(R.id.button3, View.VISIBLE);
			    remoteViews.setViewVisibility(R.id.cpu3, View.VISIBLE);
			    if(new File(Constants.CPU3_CURR_GOV).exists()){
					remoteViews.setTextViewText(R.id.cpu3, "ON");
					remoteViews.setTextColor(R.id.cpu3, Color.GREEN);
				}
			    else{
			    	remoteViews.setTextViewText(R.id.cpu3, "OFF");
					remoteViews.setTextColor(R.id.cpu3, Color.RED);
			    }
		    }
	    }
		
		ComponentName thiswidget = new ComponentName(context, WidgetToggle.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_toggle);
	    if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_CPU1)) {
	    	//setView(context);
	    	new CPUToggle(context).execute(new String[] {"1"});
	    } 
	    else if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_CPU2)) {
	    	//setView(context);
	    	new CPUToggle(context).execute(new String[] {"2"});
	    } 
	    else if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_CPU3)) {
	    	//setView(context);
	    	new CPUToggle(context).execute(new String[] {"3"});
	    } 
	    else if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
	    	setView(context);
	    } 
	    else {
	        super.onReceive(context, intent);
	    }
	    
	}
	
	private class CPUToggle extends AsyncTask<String, Void, Object> {
		Context c;
		public CPUToggle(Context c){
			this.c = c;
		}
		@Override
		protected Object doInBackground(String... args) {
			Process process;
			File file = new File("/sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_governor");
			try {

				InputStream fIn = new FileInputStream(file);

				try {
					String line;
					process = Runtime.getRuntime().exec("su");
					OutputStream stdin = process.getOutputStream();
					InputStream stderr = process.getErrorStream();
					InputStream stdout = process.getInputStream();

					stdin.write(("echo 1 > "+Constants.MPDECISION+"\n")
							.getBytes());
					stdin.write(("chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("echo 0 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());

					stdin.flush();

					stdin.close();
					BufferedReader brCleanUp = new BufferedReader(
							new InputStreamReader(stdout));
					while ((line = brCleanUp.readLine()) != null) {
						Log.d("[KernelTuner ToggleCPU Output]", line);
					}
					brCleanUp.close();
					brCleanUp = new BufferedReader(
							new InputStreamReader(stderr));
					while ((line = brCleanUp.readLine()) != null) {
						Log.e("[KernelTuner ToggleCPU Error]", line);
					}
					brCleanUp.close();
					if (process != null) {
						process.getErrorStream().close();
						process.getInputStream().close();
						process.getOutputStream().close();
					}
				} catch (IOException e) {

				}
				fIn.close();
			}

			catch (FileNotFoundException e) {

				try {
					String line;
					process = Runtime.getRuntime().exec("su");
					OutputStream stdin = process.getOutputStream();
					InputStream stderr = process.getErrorStream();
					InputStream stdout = process.getInputStream();

					stdin.write(("echo 0 > "+Constants.MPDECISION+"\n")
							.getBytes());
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());

					stdin.flush();

					stdin.close();
					BufferedReader brCleanUp = new BufferedReader(
							new InputStreamReader(stdout));
					while ((line = brCleanUp.readLine()) != null) {
						Log.d("[KernelTuner ToggleCPU Output]", line);
					}
					brCleanUp.close();
					brCleanUp = new BufferedReader(
							new InputStreamReader(stderr));
					while ((line = brCleanUp.readLine()) != null) {
						Log.e("[KernelTuner ToggleCPU Error]", line);
					}
					brCleanUp.close();

				} catch (IOException ex) {
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {

			setView(c);
		}

	}
}
