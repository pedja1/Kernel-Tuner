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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FrequencyCollection;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.services.NotificationService;
import rs.pedjaapps.KernelTuner.tools.Initd;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

public class KernelTuner extends Activity implements Runnable
{

	private List<String> voltages = new ArrayList<String>();
	private TextView batteryTemp;
	private TextView cputemptxt;
	private String tempPref;
	private long mLastBackPressTime = 0;
	private Toast mToast;
	private LinearLayout cpuTempLayout;
	private AlertDialog alert;
	private String tmp;
	private Context c;
	private boolean thread = true;

	private String freqcpu0 = "offline";
	private String freqcpu1 = "offline";
	private String freqcpu2 = "offline";
	private String freqcpu3 = "offline";

	private String cpu0max = "       ";
	private String cpu1max = "       ";
	private String cpu2max = "       ";
	private String cpu3max = "       ";

	private float fLoad;

	private TextView cpu0prog;
	private TextView cpu1prog;
	private TextView cpu2prog;
	private TextView cpu3prog;

	private ProgressBar cpu0progbar;
	private ProgressBar cpu1progbar;
	private ProgressBar cpu2progbar;
	private ProgressBar cpu3progbar;
	private List<String> freqlist = FrequencyCollection.getInstance()
			.getFrequencyValues();

	private SharedPreferences preferences;
	private ProgressDialog pd = null;

	LinearLayout tempPanel;
	LinearLayout cpuPanel;
	LinearLayout togglesPanel;
	LinearLayout mainPanel;

	private int load;

	private Handler mHandler;

	private SharedPreferences.Editor editor;

	private boolean first;
	private String theme;
	private Button gpu;
	private Button cpu;
	private Button tis;
	private Button voltage;
	private Button mp;
	private Button thermal;
	private boolean minimal;
	private TextView cpuLoadTxt;
	private ProgressBar cpuLoad;
	private int refresh = 1000;
	Button cpu1toggle;
	Button cpu2toggle;
	Button cpu3toggle;
	final int cpuTempPath = IOHelper.getCpuTempPath();
	MainApp app;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		/**
		 * Strict mode for debuging
		 */
		/*
		 * StrictMode.setThreadPolicy(new
		 * StrictMode.ThreadPolicy.Builder().detectAll().build());
		 * StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		 * .detectAll().build());
		 */
		app = MainApp.getInstance();
		c = this;
		List<IOHelper.VoltageList> voltageFreqs = IOHelper.voltages();
		preferences = app.getPrefs();
		editor = preferences.edit();
		theme = preferences.getString("theme", "light");
		minimal = preferences.getBoolean("main_style", false);
		if (minimal)
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Light);
			setContentView(R.layout.activity_main_popup);
		}
		else
		{
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			setContentView(R.layout.activity_main);
		}
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		try
		{
			refresh = Integer
					.parseInt(preferences.getString("refresh", "1000"));
		}
		catch (Exception e)
		{
			refresh = 1000;
		}
		if (!minimal)
		{
			tempPanel = (LinearLayout) findViewById(R.id.temperature_layout);
			mainPanel = (LinearLayout) findViewById(R.id.buttons_layout);
			cpuPanel = (LinearLayout) findViewById(R.id.cpu_info_layout);
			togglesPanel = (LinearLayout) findViewById(R.id.toggles_layout);

			if (!preferences.getBoolean("main_temp", true))
			{
				tempPanel.setVisibility(View.GONE);
			}
			if (!preferences.getBoolean("main_cpu", true))
			{
				cpuPanel.setVisibility(View.GONE);
			}
			if (!preferences.getBoolean("main_toggles", true))
			{
				togglesPanel.setVisibility(View.GONE);
			}
			if (!preferences.getBoolean("main_buttons", true))
			{
				mainPanel.setVisibility(View.GONE);
			}

			cpu0prog = (TextView) findViewById(R.id.txtCpu0Freq);
			cpu1prog = (TextView) findViewById(R.id.txtCpu1Freq);
			cpu2prog = (TextView) findViewById(R.id.txtCpu2Freq);
			cpu3prog = (TextView) findViewById(R.id.txtCpu3Freq);

			cpu0progbar = (ProgressBar) findViewById(R.id.prgCpu0);
			cpu1progbar = (ProgressBar) findViewById(R.id.prgCpu1);
			cpu2progbar = (ProgressBar) findViewById(R.id.prgCpu2);
			cpu3progbar = (ProgressBar) findViewById(R.id.prgCpu3);
			/**
			 * Get temperature unit from preferences
			 */
			tempPref = preferences.getString("temp", "celsius");
			batteryTemp = (TextView) findViewById(R.id.txtBatteryTemp);
			cpuTempLayout = (LinearLayout) findViewById(R.id.temp_cpu_layout);

			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("Various kernel and system tuning");
			actionBar.setHomeButtonEnabled(false);

			for (IOHelper.VoltageList v : voltageFreqs)
			{
				voltages.add(v.getFreq());
			}

			/***
			 * Create new thread that will loop and show current frequency for
			 * each core
			 */

			Thread cpuInfo = new Thread(this);
			cpuInfo.start();

			cpu1toggle = (Button) findViewById(R.id.btn_cpu1_toggle);
			cpu1toggle.setOnClickListener(new ToggleListener(1));

			cpu2toggle = (Button) findViewById(R.id.btn_cpu2_toggle);
			cpu2toggle.setOnClickListener(new ToggleListener(2));

			cpu3toggle = (Button) findViewById(R.id.btn_cpu3_toggle);
			cpu3toggle.setOnClickListener(new ToggleListener(3));

			cputemptxt = (TextView) findViewById(R.id.txtCpuTemp);
			cpuLoadTxt = (TextView) findViewById(R.id.txtCpuLoad);
			cpuLoad = (ProgressBar) findViewById(R.id.prgCpuLoad);

		}
		/**
		 * Extract assets if first launch
		 */
		first = preferences.getBoolean("first_launch", false);
		if (!first)
		{
			CopyAssets();
		}

		File file = new File("/sys/kernel/debug");

		if (file.exists() && file.list().length > 0)
		{

		}
		else
		{
			new mountDebugFs().execute();
		}

		/*
		 * Enable temperature monitor
		 */
		if (!IOHelper.isTempEnabled())
		{
			new enableTempMonitor().execute();
		}

		/*
		 * Load ads if not disabled
		 */
		if (preferences.getBoolean("ads", true))
			((AdView) findViewById(R.id.ad)).loadAd(new AdRequest());

		editor.putString("kernel", IOHelper.kernel());
		editor.commit();

		/**
		 * Show changelog if application updated
		 */
		changelog();

		/**
		 * Declare buttons and set onClickListener for each
		 */
		gpu = (Button) findViewById(R.id.btn_gpu);

		//TODO listener must be implemented differently(include both checks)
		gpu.setOnClickListener(new StartActivityListener((new File(Constants.GPU_SGX540).exists()) ? GpuSGX540.class : Gpu.class)); 
		gpu.setOnLongClickListener(new InfoListener(R.drawable.gpu,
				getResources().getString(R.string.info_gpu_title),
				getResources().getString(R.string.info_gpu_text),
				Constants.G_S_URL_PREFIX + "GPU", true));

		voltage = (Button) findViewById(R.id.btn_voltage);
		voltage.setOnClickListener(new StartActivityListener(
				VoltageActivity.class));
		voltage.setOnLongClickListener(new InfoListener(R.drawable.voltage,
				getResources().getString(R.string.info_voltage_title),
				getResources().getString(R.string.info_voltage_text),
				Constants.G_S_URL_PREFIX + "undervolting cpu", true));

		cpu = (Button) findViewById(R.id.btn_cpu);

		cpu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				String cpu = preferences.getString("show_cpu_as", "full");
				Intent myIntent = null;
				if (cpu.equals("full"))
				{
					myIntent = new Intent(c, CPUActivity.class);
				}
				else if (cpu.equals("minimal"))
				{
					myIntent = new Intent(c, CPUActivityOld.class);
				}
				startActivity(myIntent);
			}
		});
		cpu.setOnLongClickListener(new InfoListener(R.drawable.ic_launcher,
				getResources().getString(R.string.info_cpu_title),
				getResources().getString(R.string.info_cpu_text),
				Constants.G_S_URL_PREFIX + "CPU", true));

		tis = (Button) findViewById(R.id.btn_times);
		tis.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String tisChoice = preferences.getString("tis_open_as", "ask");
				if (tisChoice.equals("ask"))
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(c);

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

					list.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View arg0)
						{

							Intent myIntent = new Intent(c, TISActivity.class);
							startActivity(myIntent);
							if (remember.isChecked())
							{
								editor.putString("tis_open_as", "list");
								editor.commit();
							}
							alert.dismiss();

						}

					});

					chart.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View arg0)
						{

							Intent myIntent = new Intent(c,
									TISActivityChart.class);
							startActivity(myIntent);
							if (remember.isChecked())
							{
								editor.putString("tis_open_as", "chart");
								editor.commit();
							}
							alert.dismiss();

						}

					});

					builder.setView(view);
					alert = builder.create();

					alert.show();
				}
				else if (tisChoice.equals("list"))
				{
					Intent myIntent = new Intent(c, TISActivity.class);
					startActivity(myIntent);
				}
				else if (tisChoice.equals("chart"))
				{
					Intent myIntent = new Intent(c, TISActivityChart.class);
					startActivity(myIntent);
				}

			}
		});
		tis.setOnLongClickListener(new InfoListener(R.drawable.times,
				getResources().getString(R.string.info_tis_title),
				getResources().getString(R.string.info_tis_text),
				Constants.G_S_URL_PREFIX + "cpu times_in_state", true));

		mp = (Button) findViewById(R.id.btn_mpdecision);
		mp.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				Intent myIntent = null;
				if (new File(Constants.MPDEC_THR_DOWN).exists())
				{
					myIntent = new Intent(c, Mpdecision.class);
				}
				else if (new File(Constants.MPDEC_THR_0).exists())
				{
					myIntent = new Intent(c, MpdecisionNew.class);
				}
				startActivity(myIntent);

			}
		});
		mp.setOnLongClickListener(new InfoListener(R.drawable.dual,
				getResources().getString(R.string.info_mpd_title),
				getResources().getString(R.string.info_mpd_text),
				Constants.G_S_URL_PREFIX + "mp-decision", true));

		Button misc = (Button) findViewById(R.id.btn_misc);
		misc.setOnClickListener(new StartActivityListener(MiscTweaks.class));
		misc.setOnLongClickListener(new InfoListener(R.drawable.misc,
				getResources().getString(R.string.info_misc_title),
				getResources().getString(R.string.info_misc_text), "", false));

		Button governor = (Button) findViewById(R.id.btn_governor);
		governor.setOnClickListener(new StartActivityListener(
				GovernorActivity.class));
		governor.setOnLongClickListener(new InfoListener(
				R.drawable.main_governor, getResources().getString(
						R.string.info_gov_title), getResources().getString(
						R.string.info_gov_text), Constants.G_S_URL_PREFIX
						+ "linux governors", true));

		Button oom = (Button) findViewById(R.id.btn_oom);
		oom.setOnClickListener(new StartActivityListener(OOM.class));
		oom.setOnLongClickListener(new InfoListener(R.drawable.oom,
				getResources().getString(R.string.info_oom_title),
				getResources().getString(R.string.info_oom_text),
				Constants.G_S_URL_PREFIX + "oom", true));

		Button profiles = (Button) findViewById(R.id.btn_profiles);
		profiles.setOnClickListener(new StartActivityListener(Profiles.class));
		profiles.setOnLongClickListener(new InfoListener(R.drawable.profile,
				getResources().getString(R.string.info_profiles_title),
				getResources().getString(R.string.info_profiles_text), "",
				false));

		thermal = (Button) findViewById(R.id.btn_thermal);
		thermal.setOnClickListener(new StartActivityListener(Thermald.class));

		thermal.setOnLongClickListener(new InfoListener(R.drawable.temp,
				getResources().getString(R.string.info_thermal_title),
				getResources().getString(R.string.info_thermal_text), "", false));

		Button sd = (Button) findViewById(R.id.btn_sd);
		sd.setOnClickListener(new StartActivityListener(
				SDScannerConfigActivity.class));
		sd.setOnLongClickListener(new InfoListener(R.drawable.sd,
				getResources().getString(R.string.info_sd_title),
				getResources().getString(R.string.info_sd_text), "", false));

		Button info = (Button) findViewById(R.id.btn_info);
		if (minimal)
		{
			info.setText("Settings");
			info.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
					.getDrawable(R.drawable.settings), null, null);
		}
		info.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (minimal)
				{
					Intent myIntent = new Intent(c, Preferences.class);
					startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(c, SystemInfo.class);
					startActivity(myIntent);
				}

			}
		});
		if (!minimal)
		{
			info.setOnLongClickListener(new InfoListener(R.drawable.info,
					getResources().getString(R.string.info_sys_info_title),
					getResources().getString(R.string.info_sys_info_text), "",
					false));
		}

		Button tm = (Button) findViewById(R.id.btn_task_manager);
		tm.setOnClickListener(new StartActivityListener(TaskManager.class));
		tm.setOnLongClickListener(new InfoListener(R.drawable.tm,
				getResources().getString(R.string.info_tm_title),
				getResources().getString(R.string.info_tm_text),
				Constants.G_S_URL_PREFIX + "task manager", true));

		Button build = (Button) findViewById(R.id.btn_build);
		build.setOnClickListener(new StartActivityListener(
				BuildpropEditor.class));
		build.setOnLongClickListener(new InfoListener(R.drawable.build,
				getResources().getString(R.string.info_build_title),
				getResources().getString(R.string.info_build_text),
				Constants.G_S_URL_PREFIX + "build.prop", true));

		Button sys = (Button) findViewById(R.id.btn_sysctl);
		sys.setOnClickListener(new StartActivityListener(SysCtl.class));
		sys.setOnLongClickListener(new InfoListener(R.drawable.sysctl,
				getResources().getString(R.string.info_sysctl_title),
				getResources().getString(R.string.info_sysctl_text),
				Constants.G_S_URL_PREFIX + "sysctl", true));

		Button log = (Button) findViewById(R.id.btn_logcat);
		log.setOnClickListener(new StartActivityListener(LogCat.class));
		log.setOnLongClickListener(new InfoListener(R.drawable.swap,
				getResources().getString(R.string.info_logs_title),
				getResources().getString(R.string.info_logs_text),
				Constants.G_S_URL_PREFIX + "swap", true));

		initialCheck();
		if (preferences.getBoolean("notificationService", false)
				&& !isNotificationServiceRunning())
		{
			startService(new Intent(c, NotificationService.class));
		}
		else if (!preferences.getBoolean("notificationService", false)
				&& isNotificationServiceRunning())
		{
			stopService(new Intent(c, NotificationService.class));
		}
	}

	class ToggleListener implements OnClickListener
	{

		int cpu;

		public ToggleListener(int cpu)
		{
			this.cpu = cpu;
		}

		@Override
		public void onClick(View v)
		{
			KernelTuner.this.pd = ProgressDialog.show(c, null, getResources()
					.getString(R.string.applying_settings), true, true);
			new CPUToggle().execute(cpu + "");
		}

	}

	class StartActivityListener implements OnClickListener
	{

		Class<?> cls;

		public StartActivityListener(Class<?> cls)
		{
			this.cls = cls;
		}

		@Override
		public void onClick(View v)
		{
			startActivity(new Intent(c, cls));
		}

	}

	class InfoListener implements OnLongClickListener
	{

		int icon;
		String title;
		String text;
		String url;
		boolean more;

		public InfoListener(int icon, String title, String text, String url,
				boolean more)
		{
			this.icon = icon;
			this.title = title;
			this.text = text;
			this.url = url;
			this.more = more;
		}

		@Override
		public boolean onLongClick(View v)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(c);

			builder.setTitle(title);

			builder.setIcon(icon);
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.text_view_layout, null);
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(text);

			builder.setPositiveButton(getResources()
					.getString(R.string.info_ok),
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{

						}
					});
			if (more)
			{
				builder.setNeutralButton(
						getResources().getString(R.string.info_more),
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								Uri uri = Uri.parse(url);
								Intent intent = new Intent(Intent.ACTION_VIEW,
										uri);
								startActivity(intent);
							}

						});
			}
			builder.setView(view);
			AlertDialog alert = builder.create();

			alert.show();
			return true;
		}

	}

	@Override
	public void onPause()
	{

		super.onPause();

	}

	@Override
	protected void onResume()
	{

		/**
		 * Register BroadcastReceiver that will listen for battery changes and
		 * update ui
		 */

		if (!minimal)
		{
			this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED));

		}
		/**
		 * I init.d is selected for restore settings on boot make inid.d files
		 * else remove them
		 */
		String boot = preferences.getString("boot", "init.d");
		if (boot.equals("init.d"))
		{
			Tools.exportInitdScripts(c, voltages);
		}
		else
		{
			new Initd(this).execute("rm");
		}

		super.onResume();

	}

	@Override
	public void onStop()
	{

		/**
		 * Unregister receiver
		 */
		if (!minimal && mBatInfoReceiver != null)
		{
			unregisterReceiver(mBatInfoReceiver);
			mBatInfoReceiver = null;
		}

		super.onStop();

	}

	@Override
	public void onDestroy()
	{

		/**
		 * set thread false so that cpu info thread stop repeating
		 */
		super.onDestroy();
		thread = false;
		try
		{
			RootTools.closeAllShells();
		}
		catch (IOException e)
		{
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
	}

	/**
	 * Display changelog if version is higher than one stored on shared
	 * preferences than store curent version
	 */
	private void changelog()
	{

		String versionpref = preferences.getString("version", "");

		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			String version = pInfo.versionName;
			if (!versionpref.equals(version))
			{

				Intent myIntent = new Intent(c, Changelog.class);
				startActivity(myIntent);
				if (first)
				{
					CopyAssets();
				}

			}

			editor.putString("version", version);
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			Log.e(Constants.LOG_TAG, e.getMessage());
		}

	}

	/**
	 * CPU Temperature
	 */

	private void cpuTemp(String cputemp)
	{
		cpuTempLayout.setVisibility(View.VISIBLE);

		/**
		 * If fahrenheit is selected in settings, convert temp to fahreinheit
		 */
		if (!cputemp.equals("") || cputemp.length() != 0)
		{
			if (tempPref.equals("fahrenheit"))
			{
				cputemp = String
						.valueOf((int) (Double.parseDouble(cputemp) * 1.8) + 32);
				cputemptxt.setText(cputemp + "°F");
				int temp = Integer.parseInt(cputemp);

				if (temp < 113)
				{
					cputemptxt.setTextColor(Color.GREEN);
				}
				else if (temp >= 113 && temp < 138)
				{
					cputemptxt.setTextColor(Color.YELLOW);
				}
				else if (temp >= 138)
				{
					cputemptxt.setTextColor(Color.RED);
				}
			}

			else if (tempPref.equals("celsius"))
			{
				cputemptxt.setText(cputemp + "°C");
				int temp = Integer.parseInt(cputemp);
				if (temp < 45)
				{
					cputemptxt.setTextColor(Color.GREEN);
				}
				else if (temp >= 45 && temp <= 59)
				{
					cputemptxt.setTextColor(Color.YELLOW);
				}
				else if (temp > 59)
				{
					cputemptxt.setTextColor(Color.RED);

				}
			}
			/**
			 * If kelvin is selected in settings convert cpu temp to kelvin
			 */
			else if (tempPref.equals("kelvin"))
			{
				cputemp = String
						.valueOf((int) (Double.parseDouble(cputemp) + 273.15));

				cputemptxt.setText(cputemp + "°K");
				int temp = Integer.parseInt(cputemp);
				if (temp < 318)
				{
					cputemptxt.setTextColor(Color.GREEN);
				}
				else if (temp >= 318 && temp <= 332)
				{
					cputemptxt.setTextColor(Color.YELLOW);
				}
				else if (temp > 332)
				{
					cputemptxt.setTextColor(Color.RED);

				}
			}

		}
		else
		{
			cpuTempLayout.setVisibility(View.GONE);
		}

	}

	private void initialCheck()
	{

		String dOpt = preferences
				.getString("unsupported_items_display", "hide");
		/**
		 * Show/hide certain Views depending on number of cpus
		 */
		if (!minimal)
		{
			RelativeLayout cpu1ProgLayout = (RelativeLayout) findViewById(R.id.cpu1ProgLayout);
			RelativeLayout cpu2ProgLayout = (RelativeLayout) findViewById(R.id.cpu2ProgLayout);
			RelativeLayout cpu3ProgLayout = (RelativeLayout) findViewById(R.id.cpu3ProgLayout);
			if (IOHelper.cpu1Exists())
			{

				if (dOpt.equals("hide"))
					cpu1toggle.setVisibility(View.VISIBLE);
				else
					cpu1toggle.setEnabled(true);

				cpu1ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{

				if (dOpt.equals("hide"))
					cpu1toggle.setVisibility(View.GONE);
				else
					cpu1toggle.setEnabled(false);

				cpu1ProgLayout.setVisibility(View.GONE);
			}
			if (IOHelper.cpu2Exists())
			{

				if (dOpt.equals("hide"))
					cpu2toggle.setVisibility(View.VISIBLE);
				else
					cpu2toggle.setEnabled(true);

				cpu2ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{

				if (dOpt.equals("hide"))
					cpu2toggle.setVisibility(View.GONE);
				else
					cpu2toggle.setEnabled(false);

				cpu2ProgLayout.setVisibility(View.GONE);
			}
			if (IOHelper.cpu3Exists())
			{

				if (dOpt.equals("hide"))
					cpu3toggle.setVisibility(View.VISIBLE);
				else
					cpu3toggle.setEnabled(true);

				cpu3ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{
				if (dOpt.equals("hide"))
					cpu3toggle.setVisibility(View.GONE);
				else
					cpu3toggle.setEnabled(false);

				cpu3ProgLayout.setVisibility(View.GONE);
			}

		}
		/**
		 * Check for certain files in sysfs and if they doesnt exists hide
		 * depending views
		 */
		if (!(new File(Constants.CPU0_FREQS).exists()))
		{
			if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
			{
				if (dOpt.equals("hide"))
				{
					cpu.setVisibility(View.GONE);
				}
				else
				{
					cpu.setEnabled(false);
				}
			}
		}
		if (!(new File(Constants.VOLTAGE_PATH).exists()))
		{
			if (!(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()))
			{
				if (dOpt.equals("hide"))
				{
					voltage.setVisibility(View.GONE);
				}
				else
				{
					voltage.setEnabled(false);
				}
			}
		}
		if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
		{
			if (dOpt.equals("hide"))
			{
				tis.setVisibility(View.GONE);
			}
			else
			{
				tis.setEnabled(false);
			}
		}
		if (!(new File(Constants.MPDECISION).exists()))
		{
			if (dOpt.equals("hide"))
			{
				mp.setVisibility(View.GONE);
			}
			else
			{
				mp.setEnabled(false);
			}
		}
		if (!(new File(Constants.THERMALD).exists()))
		{
			if (dOpt.equals("hide"))
			{
				thermal.setVisibility(View.GONE);
			}
			else
			{
				thermal.setEnabled(false);
			}
		}
		if (!(new File(Constants.GPU_3D).exists()))
		{
			if (!(new File(Constants.GPU_SGX540).exists()))
			{
			if (dOpt.equals("hide"))
			{
				gpu.setVisibility(View.GONE);
			}
			else
			{
				gpu.setEnabled(false);
			}
			}
		}

	}

	/**
	 * Update UI with current frequency
	 */
	private void cpu0update()
	{

		if (!freqcpu0.equals("offline") && freqcpu0.length() != 0)
		{
			cpu0prog.setText(freqcpu0.trim()
					.substring(0, freqcpu0.length() - 3) + "MHz");
		}
		else
		{
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

		if (!freqcpu1.equals("offline") && freqcpu1.length() != 0)
		{
			cpu1prog.setText(freqcpu1.trim()
					.substring(0, freqcpu1.length() - 3) + "MHz");
		}
		else
		{
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
		if (!freqcpu2.equals("offline") && freqcpu2.length() != 0)
		{
			cpu2prog.setText(freqcpu2.trim()
					.substring(0, freqcpu2.length() - 3) + "MHz");
		}
		else
		{
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

		if (!freqcpu3.equals("offline") && freqcpu3.length() != 0)
		{
			cpu3prog.setText(freqcpu3.trim()
					.substring(0, freqcpu3.length() - 3) + "MHz");
		}
		else
		{
			cpu3prog.setText("offline");
		}

		if (freqlist != null)
		{

			cpu3progbar.setMax(freqlist.indexOf(cpu3max.trim()) + 1);
			cpu3progbar.setProgress(freqlist.indexOf(freqcpu3.trim()) + 1);
		}
	}

	private void setCpuLoad()
	{
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(load + "%");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

	
		menu.add(1, 1, 1, "Settings")
				.setIcon(R.drawable.settings_dark)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(2, 2, 2, "Compatibility Check").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(3, 3, 3, "Swap")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getItemId() == 1)
		{

			startActivity(new Intent(c, Preferences.class));

		}
		else if (item.getItemId() == 2)
		{
			startActivity(new Intent(c, CompatibilityCheck.class));
		}
		else if (item.getItemId() == 3)
		{
			Intent myIntent = new Intent(c, Swap.class);
			startActivity(myIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Press back button twice to exit application
	 */
	@Override
	public void onBackPressed()
	{
		if (!minimal)
		{
			if (mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000)
			{
				mToast = Toast.makeText(c,
						getResources().getString(R.string.press_again_to_exit),
						Toast.LENGTH_SHORT);
				mToast.show();
				mLastBackPressTime = java.lang.System.currentTimeMillis();
			}
			else
			{
				if (mToast != null)
					mToast.cancel();
				KernelTuner.this.finish();
				// java.lang.System.exit(0);
				mLastBackPressTime = 0;
			}
		}
		else
		{
			KernelTuner.this.finish();
			// java.lang.System.exit(0);
		}
	}

	private void CopyAssets()
	{
		AssetManager assetManager = getAssets();
		String[] files = null;
		File file;
		try
		{
			files = assetManager.list("");
		}
		catch (IOException e)
		{
			Log.e("tag1", e.getMessage());
		}
		for (String filename : files)
		{
			InputStream in = null;
			OutputStream out = null;
			try
			{
				in = assetManager.open(filename);
				file = new File(this.getFilesDir().getAbsolutePath() + "/"
						+ filename);
				out = new FileOutputStream(file);
				if (file.isFile())
				{
					copyFile(in, out);
				}
				else
				{
					file.mkdir();
				}
				in.close();
				Runtime.getRuntime().exec("chmod 755 " + file);
				file.setExecutable(true);
				in = null;
				out.flush();
				out.close();
				out = null;
			}
			catch (Exception e)
			{
				Log.e("tag2", e.getMessage());
			}
		}

		editor.putBoolean("first_launch", true);
		editor.commit();
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, read);
		}
	}

	private boolean isNotificationServiceRunning()
	{
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE))
		{
			if (NotificationService.class.getName().equals(
					service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context arg0, Intent intent)
		{

			double temperature = intent.getIntExtra(
					BatteryManager.EXTRA_TEMPERATURE, 0) / 10;

			if (tempPref.equals("fahrenheit"))
			{
				temperature = (temperature * 1.8) + 32;
				batteryTemp.setText(((int) temperature) + "°F");
				if (temperature <= 104)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 104 && temperature < 131)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 131 && temperature < 140)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 140)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}

			else if (tempPref.equals("celsius"))
			{
				batteryTemp.setText(temperature + "°C");
				if (temperature < 45)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 45 && temperature < 55)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 55 && temperature < 60)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 60)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}
			else if (tempPref.equals("kelvin"))
			{
				temperature = temperature + 273.15;
				batteryTemp.setText(temperature + "°K");
				if (temperature < 318.15)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 318.15 && temperature < 328.15)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 328.15 && temperature < 333.15)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 333.15)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}
			// /F = (C x 1.8) + 32

		}
	};

	private class CPUToggle extends AsyncTask<String, Void, Object>
	{
		@Override
		protected Object doInBackground(String... args)
		{
			File file = new File("/sys/devices/system/cpu/cpu" + args[0]
					+ "/cpufreq/scaling_governor");
			if (file.exists())
			{

				CommandCapture command = new CommandCapture(0,
						"echo 1 > /sys/kernel/msm_mpdecision/conf/enabled",
						"chmod 777 /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"echo 0 > /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"chown system /sys/devices/system/cpu/cpu" + args[0]
								+ "/online");
				try
				{
					RootTools.getShell(true).add(command).waitForFinish();
				}
				catch (Exception e)
				{

				}
			}

			else
			{
				CommandCapture command = new CommandCapture(0,
						"echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
						"chmod 666 /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"echo 1 > /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"chmod 444 /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"chown system /sys/devices/system/cpu/cpu" + args[0]
								+ "/online",
						"chmod 777 /sys/devices/system/cpu/cpu" + args[0]
								+ "/cpufreq/scaling_max_freq",
						"chmod 777 /sys/devices/system/cpu/cpu" + args[0]
								+ "/cpufreq/scaling_min_freq",
						"chmod 777 /sys/devices/system/cpu/cpu" + args[0]
								+ "/cpufreq/scaling_cur_freq",
						"chmod 777 /sys/devices/system/cpu/cpu" + args[0]
								+ "/cpufreq/scaling_governor");
				try
				{
					RootTools.getShell(true).add(command).waitForFinish();
				}
				catch (Exception e)
				{

				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{

			KernelTuner.this.pd.dismiss();
		}

	}

	private class mountDebugFs extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{

			CommandCapture command = new CommandCapture(0,
					"mount -t debugfs debugfs /sys/kernel/debug");
			try
			{
				RootTools.getShell(true).add(command).waitForFinish();
			}
			catch (Exception e)
			{

			}
			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{

		}

	}

	private class enableTempMonitor extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{

			CommandCapture command = new CommandCapture(
					0,
					"chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode",
					"chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode",
					"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode",
					"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode");
			try
			{
				RootTools.getShell(true).add(command).waitForFinish();
			}
			catch (Exception e)
			{

			}
			return "";
		}

	}

	@Override
	public void run()
	{
		while (thread)
		{
			try
			{
				Thread.sleep(refresh);
				freqcpu0 = IOHelper.cpu0CurFreq();
				cpu0max = IOHelper.cpu0MaxFreq();
				tmp = IOHelper.cpuTemp(cpuTempPath);

				if (IOHelper.cpu1Exists())
				{
					freqcpu1 = IOHelper.cpu1CurFreq();
					cpu1max = IOHelper.cpu1MaxFreq();
				}
				if (IOHelper.cpu2Exists())
				{
					freqcpu2 = IOHelper.cpu2CurFreq();
					cpu2max = IOHelper.cpu2MaxFreq();
				}
				if (IOHelper.cpu3Exists())
				{
					freqcpu3 = IOHelper.cpu3CurFreq();
					cpu3max = IOHelper.cpu3MaxFreq();

				}
				RandomAccessFile reader = new RandomAccessFile("/proc/stat",
						"r");
				String sLoad = reader.readLine();

				String[] toks = sLoad.split(" ");

				long idle1 = Long.parseLong(toks[5]);
				long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
						+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
						+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

				try
				{
					Thread.sleep(360);
				}
				catch (Exception e)
				{
				}

				reader.seek(0);
				sLoad = reader.readLine();
				reader.close();

				toks = sLoad.split(" ");

				long idle2 = Long.parseLong(toks[5]);
				long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
						+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
						+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

				fLoad = (float) (cpu2 - cpu1)
						/ ((cpu2 + idle2) - (cpu1 + idle1));
				load = (int) (fLoad * 100);
				mHandler.post(new Runnable()
				{

					@Override
					public void run()
					{

						cpuTemp(tmp);
						cpu0update();

						if (IOHelper.cpu1Exists())
						{
							cpu1update();
						}
						if (IOHelper.cpu2Exists())
						{
							cpu2update();
						}
						if (IOHelper.cpu3Exists())
						{
							cpu3update();
						}
						setCpuLoad();
					}
				});
			}
			catch (Exception e)
			{

			}
		}
	}

}
