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
package rs.pedjaapps.KernelTuner.ui;

import android.app.*;
import android.app.ActivityManager.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.helpers.*;
import rs.pedjaapps.KernelTuner.services.*;
import rs.pedjaapps.KernelTuner.tools.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import rs.pedjaapps.KernelTuner.entry.SysCtlDatabaseEntry;

public class KernelTuner extends SherlockActivity {

	private List<IOHelper.FreqsEntry> freqEntries;
	private List<IOHelper.VoltageList> voltageFreqs;
	private List<String> voltages = new ArrayList<String>();
	private TextView batteryLevel;
	private TextView batteryTemp;
	private TextView cputemptxt;
	private String tempPref;
	private long mLastBackPressTime = 0;
	private Toast mToast;
	private RelativeLayout tempLayout;
	private AlertDialog alert;
	private String tmp;
	int i = 0;
    Context c;
	

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {

			int level = intent.getIntExtra("level", 0);
			double temperature = intent.getIntExtra(
					BatteryManager.EXTRA_TEMPERATURE, 0) / 10;

			if (tempPref.equals("fahrenheit")) {
				temperature = (temperature * 1.8) + 32;
				batteryTemp.setText(((int) temperature) + "°F");
				if (temperature <= 104) {
					batteryTemp.setTextColor(Color.GREEN);

				} else if (temperature > 104 && temperature < 131) {
					batteryTemp.setTextColor(Color.YELLOW);

				} else if (temperature >= 131 && temperature < 140) {
					batteryTemp.setTextColor(Color.RED);

				} else if (temperature >= 140) {

					batteryTemp.setTextColor(Color.RED);

				}
			}

			else if (tempPref.equals("celsius")) {
				batteryTemp.setText(temperature + "°C");
				if (temperature < 45) {
					batteryTemp.setTextColor(Color.GREEN);

				} else if (temperature > 45 && temperature < 55) {
					batteryTemp.setTextColor(Color.YELLOW);

				} else if (temperature >= 55 && temperature < 60) {
					batteryTemp.setTextColor(Color.RED);

				} else if (temperature >= 60) {

					batteryTemp.setTextColor(Color.RED);

				}
			} else if (tempPref.equals("kelvin")) {
				temperature = temperature + 273.15;
				batteryTemp.setText(temperature + "°K");
				if (temperature < 318.15) {
					batteryTemp.setTextColor(Color.GREEN);

				} else if (temperature > 318.15 && temperature < 328.15) {
					batteryTemp.setTextColor(Color.YELLOW);

				} else if (temperature >= 328.15 && temperature < 333.15) {
					batteryTemp.setTextColor(Color.RED);

				} else if (temperature >= 333.15) {

					batteryTemp.setTextColor(Color.RED);

				}
			}
			// /F = (C x 1.8) + 32
			batteryLevel.setText(level + "%");
			if (level < 15 && level >= 5) {
				batteryLevel.setTextColor(Color.RED);

			} else if (level > 15 && level <= 30) {
				batteryLevel.setTextColor(Color.YELLOW);

			} else if (level > 30) {
				batteryLevel.setTextColor(Color.GREEN);

			} else if (level < 5) {
				batteryLevel.setTextColor(Color.RED);

			}
		}
	};

	private boolean thread = true;
	private String freqcpu0 = "offline";
	private String freqcpu1= "offline";

	
	private String cpu0max = "       ";
	private String cpu1max = "       ";
	private String cpu2max = "       ";
	private String cpu3max = "       ";

	private String freqcpu2 = "offline";
	private String freqcpu3 = "offline";


	private float fLoad;
	private TextView cpu0prog;
	private TextView cpu1prog;
	private TextView cpu2prog;
	private TextView cpu3prog;

	private ProgressBar cpu0progbar;
	private ProgressBar cpu1progbar;
	private ProgressBar cpu2progbar;
	private ProgressBar cpu3progbar;

	private List<String> freqlist = new ArrayList<String>();
	private SharedPreferences preferences;
	private ProgressDialog pd = null;
	
	private int load;

	private Handler mHandler;

	private SharedPreferences.Editor editor;

	private class CPUToggle extends AsyncTask<String, Void, Object> {
		@Override
		protected Object doInBackground(String... args) {
			File file = new File("/sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_governor");
			if(file.exists()){
 
			RootExecuter.exec(new String[]{
					"echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/online\n",
					"echo 0 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n",
					"chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n"});
            }
				
			else{
		      RootExecuter.exec(new String[]{
					"echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n",
					"chmod 666 /sys/devices/system/cpu/cpu"+args[0]+"/online\n",
					"echo 1 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n",
					"chmod 444 /sys/devices/system/cpu/cpu"+args[0]+"/online\n",
					"chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n"});
			}	

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {

			KernelTuner.this.pd.dismiss();
		}

	}
	private class mountDebugFs extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {

			RootExecuter.exec(new String[]{
				"mount -t debugfs debugfs /sys/kernel/debug\n"});
			return "";
		}

		@Override
		protected void onPostExecute(Object result) {

		}

	}

	private class enableTempMonitor extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
 
             RootExecuter.exec(new String[]{
				"chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode\n",
				"chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode\n",
				"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode\n",
				"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode\n"});
			
			return "";
		}

	}


	boolean first;
	boolean isLight;
	String theme;
	boolean dump;
	Button[] buttons;
	Button gpu,
	       cpu, 
		   tis,
		   voltage,
		   mp,
		   thermal,
		   misc, 
		   sys, 
		   tm,
		   build, 
		   sd, 
		   profiles,
		   oom, 
		   swap,
		   info,
		   governor;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
         StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                 .detectAll().build());*/

		c = this;
		freqEntries = IOHelper.frequencies();
		voltageFreqs = IOHelper.voltages();
		preferences = PreferenceManager.getDefaultSharedPreferences(c);
		editor = preferences.edit();
		theme = preferences.getString("theme", "light");

		if (theme.equals("light")) {
			setTheme(R.style.Theme_Sherlock_Light);
		} else if (theme.equals("dark")) {
			setTheme(R.style.Theme_Sherlock);

		} else if (theme.equals("light_dark_action_bar")) {
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mHandler = new Handler();
		cpu0prog = (TextView)findViewById(R.id.ptextView3);
		cpu1prog = (TextView)findViewById(R.id.ptextView4);
		cpu2prog = (TextView)findViewById(R.id.ptextView7);
	    cpu3prog = (TextView)findViewById(R.id.ptextView8);
		
		cpu0progbar = (ProgressBar)findViewById(R.id.progressBar1);
		cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		cpu2progbar = (ProgressBar)findViewById(R.id.progressBar3);
		cpu3progbar = (ProgressBar)findViewById(R.id.progressBar4);
		/**
		 * Get temperature unit from preferences
		 */
		tempPref = preferences.getString("temp", "celsius");
		batteryLevel = (TextView) findViewById(R.id.textView42);
		batteryTemp = (TextView) findViewById(R.id.textView40);
		tempLayout = (RelativeLayout) findViewById(R.id.test1a);
		/**
		 * Extract assets if first launch
		 */
		first = preferences.getBoolean("first_launch", false);
		if (first == false) {
			CopyAssets();
		}

		ActionBar actionBar = getSupportActionBar();

		actionBar.setSubtitle("Various kernel and system tuning");
		actionBar.setHomeButtonEnabled(false);

		File file = new File("/sys/kernel/debug");

		if (file.exists() && file.list().length > 0) {

		} else {
			new mountDebugFs().execute();
		}

		/*
		 * Enable temperature monitor
		 */
		if (IOHelper.isTempEnabled() == false) {
			new enableTempMonitor().execute();
		}

		/*
		 * Load ads if not disabled
		 */
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true) {
			AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());
		}

		editor.putString("kernel", IOHelper.kernel());
		editor.commit();

		/**
		 * Show changelog if application updated
		 */
		changelog();

		/**
		 * Read all available frequency steps
		 */

		for(IOHelper.FreqsEntry f: freqEntries){
			freqlist.add(new StringBuilder().append(f.getFreq()).toString());
		}
		/*for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}*/
		for (IOHelper.VoltageList v : voltageFreqs) {
			voltages.add(new StringBuilder().append(v.getFreq()).toString());
		}
		
		/***
		 * Create new thread that will loop and show current frequency for each
		 * core
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (thread) {
					try {
						Thread.sleep(1000);
						freqcpu0 = IOHelper.cpu0CurFreq();
						cpu0max = IOHelper.cpu0MaxFreq();
						tmp = IOHelper.cpuTemp();
						
						if (IOHelper.cpu1Online() == true)
						{
							freqcpu1 = IOHelper.cpu1CurFreq();
							cpu1max = IOHelper.cpu1MaxFreq();
						}
						if (IOHelper.cpu2Online() == true)
						{
							freqcpu2 = IOHelper.cpu2CurFreq();
							cpu2max = IOHelper.cpu2MaxFreq();
						}
						if (IOHelper.cpu3Online() == true)
						{
							freqcpu3 = IOHelper.cpu3CurFreq();
							cpu3max = IOHelper.cpu3MaxFreq();

						}
						mHandler.post(new Runnable() {

							@Override
							public void run() {

										
										cpuTemp(tmp);
										cpu0update();
										
										if (IOHelper.cpu1Online())
										{
											cpu1update();

										}
										if (IOHelper.cpu2Online())
										{
											
											cpu2update();

										}
										if (IOHelper.cpu3Online())
										{
											cpu3update();

										}


									}
						});
					} catch (Exception e) {

					}
				}
			}
		}).start();

		
		/**
		 * Declare buttons and set onClickListener for each
		 */
		gpu = (Button) findViewById(R.id.button3);
		
		gpu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(c, Gpu.class);
				c.startActivity(myIntent);

			}

		});
		gpu.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.gpu, "GPU" ,"GPU(Graphic Processing Unit) is used for rendering 2D and 3D graphic\nThis option allows you to change GPU clock speed(frequency)\nOnly devices with Adreno 220, 225, and 320 are supported at the moment",Constants.G_S_URL_PREFIX+"GPU", true);
				return true;
			}

		});

		voltage = (Button) findViewById(R.id.button6);
		voltage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c,
						VoltageActivity.class);
				c.startActivity(myIntent);
			}

		});
		voltage.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.voltage, "Undervolting" ,"Allows you to set custom CPU voltage\nVoltage is managed on per frequency base(Each frequency step can be adjucted for specific votage)\nSome Qualcomm devices and most of the Tegra 3 devies are supported at the moment",Constants.G_S_URL_PREFIX+"undervolting cpu", true);
				return true;
			}

		});

		cpu = (Button) findViewById(R.id.button2);
		
		cpu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String cpu = preferences.getString("show_cpu_as", "full");
				Intent myIntent = null;
				if(cpu.equals("full")){
				myIntent = new Intent(c,
						CPUActivity.class);
				}
				else if(cpu.equals("minimal")){
					myIntent = new Intent(c,
							CPUActivityOld.class);
				}
				startActivity(myIntent);
			}
		});
		cpu.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.ic_launcher, "CPU" ,"CPU(Central Processing Unit)\nThis option allows you to change CPU clock speed(frequency) and governor\nChanging frequency and governors is core-independent. Most devices supports this function",Constants.G_S_URL_PREFIX+"CPU", true);
				return true;
			}

		});

		tis = (Button) findViewById(R.id.button5);
		tis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tisChoice = preferences.getString("tis_open_as", "ask");
				if (tisChoice.equals("ask")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							c);

					builder.setTitle("Display As");
					LayoutInflater inflater = (LayoutInflater) c
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.tis_dialog, null);
					ImageView list = (ImageView) view
							.findViewById(R.id.imageView1);
					ImageView chart = (ImageView) view
							.findViewById(R.id.imageView2);
					final CheckBox remember = (CheckBox) view
							.findViewById(R.id.checkBox1);

					list.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							Intent myIntent = new Intent(c,
									TISActivity.class);
							startActivity(myIntent);
							if (remember.isChecked()) {
								editor.putString("tis_open_as", "list");
								editor.commit();
							}
							alert.dismiss();

						}

					});

					chart.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							Intent myIntent = new Intent(c,
									TISActivityChart.class);
							startActivity(myIntent);
							if (remember.isChecked()) {
								editor.putString("tis_open_as", "chart");
								editor.commit();
							}
							alert.dismiss();

						}

					});

					builder.setView(view);
					alert = builder.create();

					alert.show();
				} else if (tisChoice.equals("list")) {
					Intent myIntent = new Intent(c,
							TISActivity.class);
					startActivity(myIntent);
				} else if (tisChoice.equals("chart")) {
					Intent myIntent = new Intent(c,
							TISActivityChart.class);
					startActivity(myIntent);
				}

			}
		});
		tis.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.times, "Times in State" ,"Displays time CPU has spent in each frequency",Constants.G_S_URL_PREFIX+"cpu times_in_state", true);
				return true;
			}

		});
		
		mp = (Button) findViewById(R.id.button7);
		mp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent myIntent = null; 
				if(new File(Constants.MPDEC_THR_DOWN).exists()){
				myIntent = new Intent(c, Mpdecision.class);
				}
				else if(new File(Constants.MPDEC_THR_0).exists()){
					myIntent = new Intent(c, MpdecisionNew.class);
				}
				startActivity(myIntent);

			}
		});
		mp.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.dual, "MP-Decision" ,"MP-Decision determins when to enable/disable CPU cores\n You can tune MP-Decision options here",Constants.G_S_URL_PREFIX+"mp-decision", true);
				return true;
			}

		});

		misc = (Button) findViewById(R.id.button4);
		misc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, MiscTweaks.class);
				startActivity(myIntent);

			}
		});
		misc.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.misc, "Misc Tweaks" ,"Contains various options for tweaking kernel including:\n\n    *Changing Capacitive buttons backlight\n    *Toggle Fastcharge mode\n    *Toggle vsync\n    *Change Color Depth\n    *Change Sweep2Wake\n    *...\n\nAll options are kernel dependend.","", false);
				return true;
			}

		});

		Button cpu1toggle = (Button)findViewById(R.id.button1);
		cpu1toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				KernelTuner.this.pd = ProgressDialog.show(c,
						null,
						getResources().getString(R.string.applying_settings),
						true, true);
				new CPUToggle().execute(new String[] {"1"});

			}
		});

		Button cpu2toggle = (Button) findViewById(R.id.button8);
		cpu2toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				KernelTuner.this.pd = ProgressDialog.show(c,
						null,
						getResources().getString(R.string.applying_settings),
						true, true);
				new CPUToggle().execute(new String[] {"2"});

			}
		});

		Button cpu3toggle = (Button) findViewById(R.id.button9);
		cpu3toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				KernelTuner.this.pd = ProgressDialog.show(c,
						null,
						getResources().getString(R.string.applying_settings),
						true, true);
				new CPUToggle().execute(new String[] {"3"});

			}
		});

	    governor = (Button) findViewById(R.id.button10);
		governor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(c,
						GovernorActivity.class);
				startActivity(myIntent);

			}

		});
		governor.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.main_governor, "CPU Governor Settings" ,"Governor manages CPU frequency scalling\nYou can tune governor settings here\n\nNot all governors supports settings.",Constants.G_S_URL_PREFIX+"linux governors", true);
				return true;
			}

		});

		oom = (Button) findViewById(R.id.button13);
		oom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, OOM.class);
				startActivity(myIntent);

			}
		});
		oom.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.oom, "OOM" ,"OOM(Out Of Memory)\nAndroid system will use this values to determin which application to terminate to release memory",Constants.G_S_URL_PREFIX+"oom", true);
				return true;
			}

		});

		profiles = (Button)findViewById(R.id.button12);
		profiles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, Profiles.class);
				startActivity(myIntent);

			}
		});
		profiles.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.profile, "Profiles" ,"Create settings profiles to be applied at differente conditions\nUse with one of these applications to apply profiles depending on conditions:\n\n    *Llama\n    *Tasker\n    *Locale","", false);
				return true;
			}

		});

		thermal = (Button)findViewById(R.id.button11);
		thermal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, Thermald.class);
				startActivity(myIntent);

			}
		});
		
		thermal.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.temp, "Thermal" ,"This option allows you to set thermal options for your cpu.\nEach time CPU temperature reaches certain threshold cpu frequency will be lowered to prevent overheating\nWhen temperature lowers cpu frequency will reset","", false);
				return true;
			}

		});

		sd = (Button)findViewById(R.id.button15);
		sd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, SDScannerConfigActivity.class);
				startActivity(myIntent);

			}
		});
		sd.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.sd, "SD Analyzer" ,"Set of tools for analizing what is using most space in your sdcard","", false);
				return true;
			}

		});

		info = (Button)findViewById(R.id.button14);
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, SystemInfo.class);
				startActivity(myIntent);

			}
		});
		info.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.info, "System Info" ,"Detailed system information","", false);
				return true;
			}

		});

		tm = (Button)findViewById(R.id.button16);
		tm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, TaskManager.class);
				startActivity(myIntent);

			}
		});
		tm.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.tm, "Task Manager" ,"Display and manage running processes on your device\n\nAt the moment you can:\n\n    *Kill Process\n    *Set process priority",Constants.G_S_URL_PREFIX+"task manager", true);
				return true;
			}

		});
		build = (Button)findViewById(R.id.button18);
		build.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent myIntent = new Intent(c, BuildpropEditor.class);
					startActivity(myIntent);

				}
			});
		build.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.build, "build.prop Editor" ,"Edit you devices build properties",Constants.G_S_URL_PREFIX+"build.prop", true);
				return true;
			}

		});
		sys = (Button)findViewById(R.id.button17);
		sys.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(c, SysCtl.class);
				startActivity(myIntent);

			}
		});
		sys.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.sysctl, "System Control" ,"",Constants.G_S_URL_PREFIX+"sysctl", true);
				return true;
			}

		});
	    swap = (Button)findViewById(R.id.button19);
		swap.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent myIntent = new Intent(c, Swap.class);
					startActivity(myIntent);

				}
			});
		swap.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.swap, "Swap Manager" ,"Create and manage swap files\nWhen memory is low kernel will push memory pages of lower priority processes to swap file releasing RAM for curently active application(s)\n\nThis option is depricated and no longer maintained, use at your own risk\nPlease DO NOT submit bug reports for Swap",Constants.G_S_URL_PREFIX+"swap", true);
				return true;
			}

		});

		initialCheck();
startCpuLoadThread();
		if (preferences.getBoolean("notificationService", false) == true
				&& isNotificationServiceRunning() == false) {
			startService(new Intent(c, NotificationService.class));
		} else if (preferences.getBoolean("notificationService", false) == false
				&& isNotificationServiceRunning() == true) {
			stopService(new Intent(c, NotificationService.class));
		}

	}
	
	private void infoDialog(int icon, String title, String text, final String url, boolean more)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(title);

		builder.setIcon(icon);
		LayoutInflater inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.text_view_layout, null);
		TextView tv = (TextView)view.findViewById(R.id.tv);
		tv.setText(text);
		
		builder.setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});
		if(more){
		builder.setNeutralButton("Im stil confused", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}

			});
		}
		builder.setView(view);
		AlertDialog alert = builder.create();

		alert.show();
	}
	
	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	protected void onResume() {

		/**
		 * Register BroadcastReceiver that will listen for battery changes and
		 * update ui
		 */

		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		/**
		 * I init.d is selected for restore settings on boot make inid.d files
		 * else remove them
		 */

		
		String boot = preferences.getString("boot", "init.d");
		if (boot.equals("init.d")) {
			initdExport();
		} else {
			new Initd().execute(new String[] { "rm" });
		}

		super.onResume();

	}

	@Override
	public void onStop() {

		/**
		 * Unregister receiver
		 */
		if (mBatInfoReceiver != null) {
			unregisterReceiver(mBatInfoReceiver);

			mBatInfoReceiver = null;
		}

		super.onStop();

	}

	@Override
	public void onDestroy() {

		/**
		 * set thread false so that cpu info thread stop repeating
		 */
		super.onDestroy();
		thread = false;
		
		

	}

	private void setCpuLoad(){
		TextView cpuLoadTxt = (TextView)findViewById(R.id.textView1);

		ProgressBar cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		cpuLoad.setProgress(load);
		
		cpuLoadTxt.setText(load+"%");
		
		
	}
	
	
/**
 * Start new thread that will get cpu load and update UI
 */
private void startCpuLoadThread() {
		// Do something long
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				while(thread) {
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

						fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
					load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {
							@Override
							public void run() {
							
								setCpuLoad();
								//	progress.setProgress(value);
							}
						});
				}
			}
		};
		new Thread(runnable).start();
	}
	/**
	 * Display changelog if version is higher than one stored on shared preferences than store curent version*/
	private void changelog() {
		
		String versionpref = preferences.getString("version", "");

		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			String version = pInfo.versionName;
			if (!versionpref.equals(version)) {

				Intent myIntent = new Intent(c, Changelog.class);
				startActivity(myIntent);
				if (first == true) {
					CopyAssets();
				}

			}
			
			editor.putString("version", version);
			editor.commit();
		} catch (PackageManager.NameNotFoundException e) {
		}

	}

	/**
	 * CPU Temperature
	 */

	private void cpuTemp(String cputemp) {
		cputemptxt = (TextView) findViewById(R.id.textView38);

		
			tempLayout.setVisibility(View.VISIBLE);

			/**
			 * If fahrenheit is selected in settings, convert temp to
			 * fahreinheit
			 */
			if(!cputemp.equals("") || cputemp.length()!=0)
			{
			if (tempPref.equals("fahrenheit")) {
				cputemp = String
						.valueOf((int) (Double.parseDouble(cputemp) * 1.8) + 32);
				cputemptxt.setText(cputemp + "°F");
				int temp = Integer.parseInt(cputemp);

				if (temp < 113) {
					cputemptxt.setTextColor(Color.GREEN);
					// cpuTempWarningStop();
				} else if (temp >= 113 && temp < 138) {
					cputemptxt.setTextColor(Color.YELLOW);
					// cpuTempWarningStop();
				}

				else if (temp >= 138) {
					// cpuTempWarning();
					cputemptxt.setTextColor(Color.RED);

				}
			}

			else if (tempPref.equals("celsius")) {
				cputemptxt.setText(cputemp + "°C");
				int temp = Integer.parseInt(cputemp);
				if (temp < 45) {
					cputemptxt.setTextColor(Color.GREEN);
					// cpuTempWarningStop();
				} else if (temp >= 45 && temp <= 59) {
					cputemptxt.setTextColor(Color.YELLOW);
					// cpuTempWarningStop();
				}

				else if (temp > 59) {
					// cpuTempWarning();
					cputemptxt.setTextColor(Color.RED);

				}
			}
			/**
			 * If kelvin is selected in settings convert cpu temp to kelvin
			 */
			else if (tempPref.equals("kelvin")) {
				cputemp = String
						.valueOf((int) (Double.parseDouble(cputemp) + 273.15));

				cputemptxt.setText(cputemp + "°K");
				int temp = Integer.parseInt(cputemp);
				if (temp < 318) {
					cputemptxt.setTextColor(Color.GREEN);
				} else if (temp >= 318 && temp <= 332) {
					cputemptxt.setTextColor(Color.YELLOW);
				}

				else if (temp > 332) {
					cputemptxt.setTextColor(Color.RED);

				}
			}

			}
			else{
			tempLayout.setVisibility(View.GONE);
			}
		
	}

	private void initialCheck() {
		
		/**
		 * Show/hide certain Views depending on number of cpus
		 */
		if (IOHelper.cpu1Online() == true) {
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.GONE);
		}
		if (IOHelper.cpu2Online() == true) {
			Button b3 = (Button) findViewById(R.id.button8);
			b3.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b3 = (Button) findViewById(R.id.button8);
			b3.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.GONE);
		}
		if (IOHelper.cpu3Online() == true) {
			Button b4 = (Button) findViewById(R.id.button9);
			b4.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b4 = (Button) findViewById(R.id.button9);
			b4.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.GONE);
		}

		/**
		 * Check for certain files in sysfs and if they doesnt exists hide
		 * depending views
		 */
		if(!(new File(Constants.CPU0_FREQS).exists())){
			if(!(new File(Constants.TIMES_IN_STATE_CPU0).exists())){
			 cpu.setVisibility(View.GONE);
			 }
		 }
		if(!(new File(Constants.VOLTAGE_PATH).exists())){
			if(!(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())){
				voltage.setVisibility(View.GONE);
			}
		}
		if(!(new File(Constants.TIMES_IN_STATE_CPU0).exists())){
			tis.setVisibility(View.GONE);
		}
		if(!(new File(Constants.MPDECISION).exists())){
			mp.setVisibility(View.GONE);
		}
		if(!(new File(Constants.THERMALD).exists())){
			thermal.setVisibility(View.GONE);
		}
		if(!(new File(Constants.GPU_3D).exists())){
			gpu.setVisibility(View.GONE);
		}

	}

	/**
	 * Create init.d files and export them to private application folder
	 */

	private void initdExport() {

		DatabaseHandler db = new DatabaseHandler(this);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
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
		String s2wStart = sharedPrefs.getString("s2wStart", "");
		String s2wEnd = sharedPrefs.getString("s2wEnd", "");

		String p1freq = sharedPrefs.getString("p1freq", "");
		String p2freq = sharedPrefs.getString("p2freq", "");
		String p3freq = sharedPrefs.getString("p3freq", "");
		String p1low = sharedPrefs.getString("p1low", "");
		String p1high = sharedPrefs.getString("p1high", "");
		String p2low = sharedPrefs.getString("p2low", "");
		String p2high = sharedPrefs.getString("p2high", "");
		String p3low = sharedPrefs.getString("p3low", "");
		String p3high = sharedPrefs.getString("p3high", "");
		boolean swap = sharedPrefs.getBoolean("swap", false);
		String swapLocation = sharedPrefs.getString("swap_location", "");
		String swappiness = sharedPrefs.getString("swappiness", "");
		String oom = sharedPrefs.getString("oom", "");
		String otg = sharedPrefs.getString("otg", "");

		String idle_freq = sharedPrefs.getString("idle_freq", "");
		String scroff = sharedPrefs.getString("scroff", "");
		String scroff_single = sharedPrefs.getString("scroff_single", "");
		String[] thr = new String[6];
		String[] tim = new String[6];
		thr[0] = sharedPrefs.getString("thr0", "");
		thr[1] = sharedPrefs.getString("thr2", "");
		thr[2] = sharedPrefs.getString("thr3", "");
		thr[3] = sharedPrefs.getString("thr4", "");
		thr[4] = sharedPrefs.getString("thr5", "");
		thr[5] = sharedPrefs.getString("thr7", "");
		tim[0] = sharedPrefs.getString("tim0", "");
		tim[1] = sharedPrefs.getString("tim2", "");
		tim[2] = sharedPrefs.getString("tim3", "");
		tim[3] = sharedPrefs.getString("tim4", "");
		tim[4] = sharedPrefs.getString("tim5", "");
		tim[5] = sharedPrefs.getString("tim7", "");
		String maxCpus = sharedPrefs.getString("max_cpus", "");
		String minCpus = sharedPrefs.getString("min_cpus", "");
		StringBuilder gpubuilder = new StringBuilder();

		gpubuilder.append("#!/system/bin/sh");
		gpubuilder.append("\n");
		if (!gpu3d.equals("")) {
			gpubuilder
					.append("echo ")
					.append("\"")
					.append(gpu3d)
					.append("\"")
					.append(" > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk")
					.append("\n");
			
		}
		if (!gpu2d.equals("")) {
			gpubuilder
					.append("echo ")
					.append("\"")
					.append(gpu2d)
					.append("\"")
					.append(" > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
			gpubuilder.append("\n");
			gpubuilder
					.append("echo ").append(
							 "\"").append(
							 gpu2d).append(
							 "\"").append(
							 " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
			gpubuilder.append("\n");

		}

		String gpu = gpubuilder.toString();

		StringBuilder cpubuilder = new StringBuilder();

		cpubuilder.append("#!/system/bin/sh");
		cpubuilder.append("\n");
		/**
		 * cpu0
		 * */
		if (!cpu0gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n");
		}
		if (!cpu0max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0max).append(
							 "\"").append(
							" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
		}
		if (!cpu0min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu0min).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n");
		}
		/**
		 * cpu1
		 * */
		if (!cpu1gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu1gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n");
		}
		if (!cpu1max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu1max).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n");
		}
		if (!cpu1min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n").append(
							"echo ").append(
							"\"").append(
							 cpu1min).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n").append(
							"chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n");
		}

		/**
		 * cpu2
		 * */
		if (!cpu2gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu2gov).append(
						 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n");
		}
		if (!cpu2max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							cpu2max).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n");
		}
		if (!cpu2min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu2min).append(
						 "\"").append(
							 " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n\n");
		}
		/**
		 * cpu3
		 * */

		if (!cpu3gov.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3gov).append(
							 "\"").append(
							 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n");
		}
		if (!cpu3max.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3max).append(
							"\"").append(
							 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n");
		}
		if (!cpu3min.equals("")) {
			cpubuilder
					.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n").append(
							 "echo ").append(
							 "\"").append(
							 cpu3min).append(
							 "\"").append(
						 " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n").append(
							 "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n\n");
		}
		List<String> govSettings = IOHelper.govSettings();
		List<String> availableGovs = IOHelper.availableGovs();

		for (String s : availableGovs) {
			for (String st : govSettings) {
				String temp = sharedPrefs.getString(s + "_" + st, "");

				if (!temp.equals("")) {
					cpubuilder
							.append("chmod 777 /sys/devices/system/cpu/cpufreq/")
							.append(s).append( "/").append(st).append("\n");
					cpubuilder.append("echo ").append("\"").append(temp).append("\"").append(
							 " > /sys/devices/system/cpu/cpufreq/").append(s).append("/").append(st).append("\n");

				}
			}
		}
		String cpu = cpubuilder.toString();

		StringBuilder miscbuilder = new StringBuilder();

		miscbuilder.append("#!/system/bin/sh \n\n#mount debug filesystem\nmount -t debugfs debugfs /sys/kernel/debug \n\n");
		if (!vsync.equals("")) {
			miscbuilder.append("#vsync\nchmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable \nchmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode \nchmod 777 /sys/kernel/debug/msm_fb/0/backbuff \necho " + "\"" + vsync + "\""
					+ " > /sys/kernel/debug/msm_fb/0/vsync_enable \n" + "echo "
					+ "\"" + hw + "\""
					+ " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n"
					+ "echo " + "\"" + backbuff + "\""
					+ " > /sys/kernel/debug/msm_fb/0/backbuff \n\n");
		}
		if (!led.equals("")) {
			miscbuilder
					.append("#capacitive buttons backlight\n"
							+ "chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n"
							+ "echo "
							+ "\""
							+ led
							+ "\""
							+ " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n\n");
		}
		if (!fastcharge.equals("")) {
			miscbuilder.append("#fastcharge\n"
					+ "chmod 777 /sys/kernel/fast_charge/force_fast_charge \n"
					+ "echo " + "\"" + fastcharge + "\""
					+ " > /sys/kernel/fast_charge/force_fast_charge \n\n");
		}
		if (!cdepth.equals("")) {
			miscbuilder.append("#color depth\n"
					+ "chmod 777 /sys/kernel/debug/msm_fb/0/bpp \n" + "echo "
					+ "\"" + cdepth + "\""
					+ " > /sys/kernel/debug/msm_fb/0/bpp \n\n");
		}

		if (!mpdecisionscroff.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core \n"
							+ "echo "
							+ "\""
							+ mpdecisionscroff
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/scroff_single_core \n");
		}
		if (!delaynew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/delay \n"
							+ "echo "
							+ "\""
							+ delaynew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/delay \n");
		}
		if (!pausenew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/pause \n"
							+ "echo "
							+ "\""
							+ pausenew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/pause \n");
		}
		if (!thruploadnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n"
							+ "echo "
							+ "\""
							+ thruploadnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n");

		}
		if (!thrdownloadnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n"
							+ "echo "
							+ "\""
							+ thrdownloadnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n");

		}
		if (!thrupmsnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n"
							+ "echo "
							+ "\""
							+ thrupmsnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up \n");
		}
		if (!thrdownmsnew.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n"
							+ "echo "
							+ "\""
							+ thrdownmsnew.trim()
							+ "\""
							+ " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down \n\n");
		}
		if(!thr[0].equals("")){
			for(int i = 0; i < 8; i++){
				miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+i+"\n");
			}
			miscbuilder.append("echo " + thr[0] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+0+"\n");
			miscbuilder.append("echo " + thr[1] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+2+"\n");
			miscbuilder.append("echo " + thr[2] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+3+"\n");
			miscbuilder.append("echo " + thr[3] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+4+"\n");
			miscbuilder.append("echo " + thr[4] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+5+"\n");
			miscbuilder.append("echo " + thr[5] + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_"+7+"\n");
			
		}
		if(!tim[0].equals("")){
			for(int i = 0; i < 8; i++){
				miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_"+i+"\n");
			}
			miscbuilder.append("echo " + tim[0] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+0+"\n");
			miscbuilder.append("echo " + tim[1] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+2+"\n");
			miscbuilder.append("echo " + tim[3] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+3+"\n");
			miscbuilder.append("echo " + tim[3] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+4+"\n");
			miscbuilder.append("echo " + tim[4] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+5+"\n");
			miscbuilder.append("echo " + tim[5] + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_"+7+"\n");
			
		}
		if(!maxCpus.equals("")){
			miscbuilder.append("echo " + maxCpus + " > /sys/kernel/msm_mpdecision/conf/max_cpus\n");
		}
		if(!minCpus.equals("")){
			miscbuilder.append("echo " + minCpus + " > /sys/kernel/msm_mpdecision/conf/min_cpus\n");
		}
		if (!sdcache.equals("")) {
			miscbuilder
					.append("#sd card cache size\n"
							+ "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n"
							+ "chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n"
							+ "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/block/mmcblk1/queue/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/block/mmcblk0/queue/read_ahead_kb \n"
							+ "echo "
							+ "\""
							+ sdcache
							+ "\""
							+ " > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n\n");
		}
		if (!io.equals("")) {
			miscbuilder.append("#IO scheduler\n"
					+ "chmod 777 /sys/block/mmcblk0/queue/scheduler \n"
					+ "chmod 777 /sys/block/mmcblk1/queue/scheduler \n"
					+ "echo " + "\"" + io + "\""
					+ " > /sys/block/mmcblk0/queue/scheduler \n" + "echo "
					+ "\"" + io + "\""
					+ " > /sys/block/mmcblk1/queue/scheduler \n\n");
		}
		if (!ldt.equals("")) {
			miscbuilder
					.append("#Notification LED Timeout\n"
							+ "chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n"
							+ "echo "
							+ "\""
							+ ldt
							+ "\""
							+ " > /sys/kernel/notification_leds/off_timer_multiplier\n\n");
		}
		if (!s2w.equals("")) {
			miscbuilder.append("#Sweep2Wake\n"
					+ "chmod 777 /sys/android_touch/sweep2wake\n" + "echo "
					+ "\"" + s2w + "\""
					+ " > /sys/android_touch/sweep2wake\n\n");
		}
		if (!s2wStart.equals("")) {
			miscbuilder
					.append("chmod 777 /sys/android_touch/sweep2wake_startbutton\n"
							+ "echo "
							+ s2wStart
							+ " > /sys/android_touch/sweep2wake_startbutton\n"
							+ "chmod 777 /sys/android_touch/sweep2wake_endbutton\n"
							+ "echo "
							+ s2wEnd
							+ " > /sys/android_touch/sweep2wake_endbutton\n\n");
		}

		if (!p1freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n"
							+ "echo "
							+ "\""
							+ p1freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
		}
		if (!p2freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n"
							+ "echo "
							+ "\""
							+ p2freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
		}
		if (!p3freq.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n"
							+ "echo "
							+ "\""
							+ p3freq.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
		}
		if (!p1low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n"
							+ "echo "
							+ "\""
							+ p1low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
		}
		if (!p1high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n"
							+ "echo "
							+ "\""
							+ p1high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
		}
		if (!p2low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n"
							+ "echo "
							+ "\""
							+ p2low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
		}
		if (!p2high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n"
							+ "echo "
							+ "\""
							+ p2high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
		}
		if (!p3low.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_low\n"
							+ "echo "
							+ "\""
							+ p3low.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_high_low\n");
		}

		if (!p3high.trim().equals("")) {
			miscbuilder
					.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_high\n"
							+ "echo "
							+ "\""
							+ p3high.trim()
							+ "\""
							+ " > /sys/kernel/msm_thermal/conf/allowed_high_high\n");
		}

		if (!idle_freq.equals("")) {
			miscbuilder.append("echo " + idle_freq
					+ " > /sys/kernel/msm_mpdecision/conf/idle_freq\n");
		}
		if (!scroff.equals("")) {
			miscbuilder.append("echo " + scroff
					+ " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n");
		}
		if (!scroff_single.equals("")) {
			miscbuilder
					.append("echo "
							+ scroff_single
							+ " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n\n");
		}
		if (swap == true) {
			miscbuilder.append("echo " + swappiness
					+ " > /proc/sys/vm/swappiness\n" + "swapon "
					+ swapLocation.trim() + "\n\n");

		} else if (swap == false) {
			miscbuilder.append("swapoff " + swapLocation.trim() + "\n\n");

		}
		if (!oom.equals("")) {
			miscbuilder.append("echo " + oom
					+ " > /sys/module/lowmemorykiller/parameters/minfree\n");

		}
		if (!otg.equals("")) {
			miscbuilder.append("echo " + otg
					+ " > /sys/kernel/debug/msm_otg/mode\n");
			miscbuilder.append("echo " + otg
					+ " > /sys/kernel/debug/otg/mode\n");
		}
		miscbuilder.append("#Umount debug filesystem\n"
				+ "umount /sys/kernel/debug \n");
		String misc = miscbuilder.toString();

		StringBuilder voltagebuilder = new StringBuilder();
		voltagebuilder.append("#!/system/bin/sh \n");
		for (String s : voltages) {
			String temp = sharedPrefs.getString("voltage_" + s, "");
			if (!temp.equals("")) {
				voltagebuilder
						.append("echo "
								+ "\""
								+ temp
								+ "\""
								+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
			}
		}
		String voltage = voltagebuilder.toString();
		StringBuilder sysBuilder = new StringBuilder();
		sysBuilder.append("#!/system/bin/sh \n");
			List<SysCtlDatabaseEntry> sysEntries = db.getAllSysCtlEntries();
			for(SysCtlDatabaseEntry e : sysEntries){
				sysBuilder.append(getFilesDir().getPath() + "/busybox sysctl -w " + e.getKey().trim() + "=" + e.getValue().trim()+"\n");
			}
		
		String sys = sysBuilder.toString();
		try {

			FileOutputStream fOut = openFileOutput("99ktsysctl",
												   MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(sys);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = openFileOutput("99ktcputweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(cpu);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = openFileOutput("99ktgputweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(gpu);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		try {

			FileOutputStream fOut = openFileOutput("99ktmisctweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(misc);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}

		try {

			FileOutputStream fOut = openFileOutput("99ktvoltage", MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(voltage);
			osw.flush();
			osw.close();

		} catch (IOException ioe) {
		}
		new Initd().execute(new String[] { "apply" });
	}

	
	/**
	Update UI with current frequency
	*/
	private void cpu0update()
	{

		if(!freqcpu0.equals("offline") && freqcpu0.length()!=0){
		cpu0prog.setText(freqcpu0.trim().substring(0, freqcpu0.length()-3)+"MHz");
		}
		else{
			cpu0prog.setText("offline");
		}
			if (freqlist != null)
		{
		
			cpu0progbar.setMax(freqlist.indexOf(cpu0max.trim()) + 1);
			cpu0progbar.setProgress(freqlist.indexOf(freqcpu0.trim()) + 1);
		}
	}


	private void cpu1update()
	{

		if(!freqcpu1.equals("offline") && freqcpu1.length()!=0){
			cpu1prog.setText(freqcpu1.trim().substring(0, freqcpu1.length()-3)+"MHz");
			}
			else{
				cpu1prog.setText("offline");
			}
		if (freqlist != null)
		{

			cpu1progbar.setMax(freqlist.indexOf(cpu1max.trim()) + 1);
			cpu1progbar.setProgress(freqlist.indexOf(freqcpu1.trim()) + 1);
		}
	}
	

	private void cpu2update()
	{
		if(!freqcpu2.equals("offline") && freqcpu2.length()!=0){
			cpu2prog.setText(freqcpu2.trim().substring(0, freqcpu2.length()-3)+"MHz");
			}
			else{
				cpu2prog.setText("offline");
			}
			if (freqlist != null)
		{
			
			cpu2progbar.setMax(freqlist.indexOf(cpu2max.trim()) + 1);
			cpu2progbar.setProgress(freqlist.indexOf(freqcpu2.trim()) + 1);
		} 
	}


	private void cpu3update()
	{

		if(!freqcpu3.equals("offline") && freqcpu3.length()!=0){
			cpu3prog.setText(freqcpu3.trim().substring(0, freqcpu3.length()-3)+"MHz");
			}
			else{
				cpu3prog.setText("offline");
			}
			
				if (freqlist != null)
		{
			

			cpu3progbar.setMax(freqlist.indexOf(cpu3max.trim()) + 1);
			cpu3progbar.setProgress(freqlist.indexOf(freqcpu3.trim()) + 1);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (theme.equals("light")) {
			isLight = true;
		} else if (theme.equals("dark")) {
			isLight = false;
		} else if (theme.equals("light_dark_action_bar")) {
			isLight = false;
		}
		menu.add(1, 1, 1, "Settings")
				.setIcon(
						isLight ? R.drawable.settings_light
								: R.drawable.settings_dark)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(2, 2, 2, "Compatibility Check").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 1) {
			startActivity(new Intent(c, Preferences.class));
		}
		else if (item.getItemId() == 2) {
			startActivity(new Intent(c, CompatibilityCheck.class));
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Press back button twice to exit application
	 */
	@Override
	public void onBackPressed() {
		if (mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000) {
			mToast = Toast.makeText(c,
					getResources().getString(R.string.press_again_to_exit),
					Toast.LENGTH_SHORT);
			mToast.show();
			mLastBackPressTime = java.lang.System.currentTimeMillis();
		} else {
			if (mToast != null)
				mToast.cancel();
			KernelTuner.this.finish();
			java.lang.System.exit(0);
			mLastBackPressTime = 0;
		}
	}

	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		File file;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				file = new File(this.getFilesDir().getAbsolutePath() + "/"
						+ filename);
				out = new FileOutputStream(file);
				copyFile(in, out);
				in.close();
				Runtime.getRuntime().exec("chmod 755 " + file);
				file.setExecutable(false);
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
		}

		editor.putBoolean("first_launch", true);
		editor.commit();
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private boolean isNotificationServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (NotificationService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}
