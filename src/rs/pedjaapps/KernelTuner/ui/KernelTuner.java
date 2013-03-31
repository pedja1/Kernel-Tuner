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

import android.content.*;
import android.widget.*;
import java.io.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.services.NotificationService;
import rs.pedjaapps.KernelTuner.tools.Initd;
import rs.pedjaapps.KernelTuner.tools.Tools;



public class KernelTuner extends Activity {

	private List<IOHelper.FreqsEntry>   freqEntries;
	private List<IOHelper.VoltageList>  voltageFreqs;
	private List<String>                voltages           = new ArrayList<String>();
	private TextView                    batteryLevel;
	private TextView                    batteryTemp;
	private TextView                    cputemptxt;
	private String                      tempPref;
	private long                        mLastBackPressTime = 0;
	private Toast                       mToast;
	private RelativeLayout              tempLayout;
	private AlertDialog                 alert;
	private String                      tmp;
    private Context                     c;
	private boolean                     thread             = true;
	
	private String                      freqcpu0           = "offline";
	private String                      freqcpu1           = "offline";
	private String                      freqcpu2           = "offline";
	private String                      freqcpu3           = "offline";
	
	private String                      cpu0max            = "       ";
	private String                      cpu1max            = "       ";
	private String                      cpu2max            = "       ";
	private String                      cpu3max            = "       ";

	private float                       fLoad;
	
	private TextView                    cpu0prog;
	private TextView                    cpu1prog;
	private TextView                    cpu2prog;
	private TextView                    cpu3prog;

	private ProgressBar                 cpu0progbar;
	private ProgressBar                 cpu1progbar;
	private ProgressBar                 cpu2progbar;
	private ProgressBar                 cpu3progbar;

	private List<String>                freqlist          = new ArrayList<String>();
	private SharedPreferences           preferences;
	private ProgressDialog              pd                = null;
	
	LinearLayout                        tempPanel;
	RelativeLayout                      cpuPanel;
	LinearLayout                        togglesPanel;
	LinearLayout                        mainPanel;
	
	private int                         load;

	private Handler                     mHandler;

	private SharedPreferences.Editor    editor;

	private boolean                     first;
	private boolean                     isLight;
	private String                      theme;
	private Button                      gpu,
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
		                                log,
		                                info,
		                                governor;
	private boolean                     minimal;
	private TextView                    cpuLoadTxt;
	private ProgressBar                 cpuLoad;
	private int                         refresh         = 1000;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/**
		*Strict mode for debuging
		*/
		/*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
         StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                 .detectAll().build());*/

		c = this;
		//new Chmod().execute();
		freqEntries = IOHelper.frequencies();
		voltageFreqs = IOHelper.voltages();
		preferences = PreferenceManager.getDefaultSharedPreferences(c);
		editor = preferences.edit();
		theme = preferences.getString("theme", "light");

		minimal = preferences.getBoolean("main_style",false);
		if(minimal == true){
			setTheme(Tools.getPreferedThemeTranslucent(theme));
			setContentView(R.layout.main_popup);
		}
		else{
			setTheme(Tools.getPreferedTheme(theme));
			setContentView(R.layout.main);
		}
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		try{
			refresh = Integer.parseInt(preferences.getString("refresh","1000"));
		}
		catch(Exception e){
			refresh = 1000;
		}
	    if(minimal==false){
		    tempPanel = (LinearLayout)findViewById(R.id.test1);
	    	mainPanel = (LinearLayout)findViewById(R.id.scrollView1);
		    cpuPanel = (RelativeLayout)findViewById(R.id.rl);
	    	togglesPanel = (LinearLayout)findViewById(R.id.ly2);
		
		    if(preferences.getBoolean("main_temp",true)==false){
			    tempPanel.setVisibility(View.GONE);
	    	}
		    if(preferences.getBoolean("main_cpu",true)==false){
			    cpuPanel.setVisibility(View.GONE);
		    }
		    if(preferences.getBoolean("main_toggles",true)==false){
			    togglesPanel.setVisibility(View.GONE);
		    }
		    if(preferences.getBoolean("main_buttons",true)==false){
			    mainPanel.setVisibility(View.GONE);
		    }
		
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
			
		    ActionBar actionBar = getActionBar();
	    	actionBar.setSubtitle("Various kernel and system tuning");
	    	actionBar.setHomeButtonEnabled(false);
		
	    	/**
		     * Read all available frequency steps
		     */

	    	for(IOHelper.FreqsEntry f: freqEntries){
		    	freqlist.add(new StringBuilder().append(f.getFreq()).toString());
	    	}
	    	for (IOHelper.VoltageList v : voltageFreqs) {
		    	voltages.add(new StringBuilder().append(v.getFreq()).toString());
	    	}

	    	/***
		     * Create new thread that will loop and show current frequency for each
		     * core
		     */
	    	final int cpuTempPath = IOHelper.getCpuTempPath();
	    	new Thread(new Runnable() {
				@Override
				public void run() {
					while (thread) {
						try {
							Thread.sleep(refresh);
							freqcpu0 = IOHelper.cpu0CurFreq();
							cpu0max = IOHelper.cpu0MaxFreq();
							tmp = IOHelper.cpuTemp(cpuTempPath);

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
	    cputemptxt = (TextView) findViewById(R.id.textView38);
	    cpuLoadTxt = (TextView)findViewById(R.id.textView1);
		cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		startCpuLoadThread();
		    
		}
		/**
		 * Extract assets if first launch
		 */
		first = preferences.getBoolean("first_launch", false);
		if (first == false) {
			CopyAssets();
		}

	
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

				infoDialog(R.drawable.gpu, getResources().getString(R.string.info_gpu_title) ,getResources().getString(R.string.info_gpu_text),Constants.G_S_URL_PREFIX+"GPU", true);
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

				infoDialog(R.drawable.voltage, getResources().getString(R.string.info_voltage_title) ,getResources().getString(R.string.info_voltage_text),Constants.G_S_URL_PREFIX+"undervolting cpu", true);
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

				infoDialog(R.drawable.ic_launcher, getResources().getString(R.string.info_cpu_title) ,getResources().getString(R.string.info_cpu_text),Constants.G_S_URL_PREFIX+"CPU", true);
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

				infoDialog(R.drawable.times, getResources().getString(R.string.info_tis_title) ,getResources().getString(R.string.info_tis_text),Constants.G_S_URL_PREFIX+"cpu times_in_state", true);
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

				infoDialog(R.drawable.dual, getResources().getString(R.string.info_mpd_title) ,getResources().getString(R.string.info_mpd_text),Constants.G_S_URL_PREFIX+"mp-decision", true);
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

				infoDialog(R.drawable.misc, getResources().getString(R.string.info_misc_title) ,getResources().getString(R.string.info_misc_text),"", false);
				return true;
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

				infoDialog(R.drawable.main_governor, getResources().getString(R.string.info_gov_title) ,getResources().getString(R.string.info_gov_text),Constants.G_S_URL_PREFIX+"linux governors", true);
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

				infoDialog(R.drawable.oom, getResources().getString(R.string.info_oom_title) ,getResources().getString(R.string.info_oom_text),Constants.G_S_URL_PREFIX+"oom", true);
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

				infoDialog(R.drawable.profile, getResources().getString(R.string.info_profiles_title) ,getResources().getString(R.string.info_profiles_text),"", false);
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

				infoDialog(R.drawable.temp, getResources().getString(R.string.info_thermal_title) ,getResources().getString(R.string.info_thermal_text),"", false);
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

				infoDialog(R.drawable.sd, getResources().getString(R.string.info_sd_title) ,getResources().getString(R.string.info_sd_text),"", false);
				return true;
			}

		});

		info = (Button)findViewById(R.id.button14);
		if(minimal){
			info.setText("Settings");
			info.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable( R.drawable.settings ), null, null);
		}
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(minimal){
					Intent myIntent = new Intent(c, Preferences.class);
					startActivity(myIntent);
				}
				else{
				Intent myIntent = new Intent(c, SystemInfo.class);
				startActivity(myIntent);
				}

			}
		});
		info.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if(minimal==false){
				infoDialog(R.drawable.info, getResources().getString(R.string.info_sys_info_title) ,getResources().getString(R.string.info_sys_info_text),"", false);
				}
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

				infoDialog(R.drawable.tm, getResources().getString(R.string.info_tm_title) ,getResources().getString(R.string.info_tm_text),Constants.G_S_URL_PREFIX+"task manager", true);
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

				infoDialog(R.drawable.build, getResources().getString(R.string.info_build_title) ,getResources().getString(R.string.info_build_text),Constants.G_S_URL_PREFIX+"build.prop", true);
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

				infoDialog(R.drawable.sysctl, getResources().getString(R.string.info_sysctl_title) ,getResources().getString(R.string.info_sysctl_text),Constants.G_S_URL_PREFIX+"sysctl", true);
				return true;
			}

		});
	    log = (Button)findViewById(R.id.button19);
		log.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent myIntent = new Intent(c, LogCat.class);
				    startActivity(myIntent);

				}
			});
		log.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				infoDialog(R.drawable.swap, getResources().getString(R.string.info_logs_title) ,getResources().getString(R.string.info_logs_text),Constants.G_S_URL_PREFIX+"swap", true);
				return true;
			}

		});

		initialCheck();
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
		
		builder.setPositiveButton(getResources().getString(R.string.info_ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});
		if(more){
		    builder.setNeutralButton(getResources().getString(R.string.info_more), new DialogInterface.OnClickListener(){

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

		if(minimal==false){
		    this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		}
		/**
		 * I init.d is selected for restore settings on boot make inid.d files
		 * else remove them
		 */
		String boot = preferences.getString("boot", "init.d");
		if (boot.equals("init.d")) {
			Tools.exportInitdScripts(c, voltages);
		} else {
			new Initd(this).execute(new String[] { "rm" });
		}

		super.onResume();

	}

	@Override
	public void onStop() {

		/**
		 * Unregister receiver
		 */
		if(minimal==false){
	    	if (mBatInfoReceiver != null) {
		    	unregisterReceiver(mBatInfoReceiver);
            	mBatInfoReceiver = null;
		    }
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
		try{
		RootTools.closeAllShells();
		}
		catch(IOException e){
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
	}

	private void setCpuLoad(){
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
						Thread.sleep(refresh);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {
							@Override
							public void run() {
							
								setCpuLoad();
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
		
		String dOpt = preferences.getString("unsupported_items_display","hide");
		/**
		 * Show/hide certain Views depending on number of cpus
		 */
		if(minimal==false){
		if (IOHelper.cpu1Online() == true) {
			Button b2 = (Button) findViewById(R.id.button1);

			if(dOpt.equals("hide")){
				b2.setVisibility(View.VISIBLE);
			}
			else{
				b2.setEnabled(true);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b2 = (Button) findViewById(R.id.button1);
			if(dOpt.equals("hide")){
				b2.setVisibility(View.GONE);
			}
			else{
				b2.setEnabled(false);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.GONE);
		}
		if (IOHelper.cpu2Online() == true) {
			Button b3 = (Button) findViewById(R.id.button8);
			if(dOpt.equals("hide")){
				b3.setVisibility(View.VISIBLE);
			}
			else{
				b3.setEnabled(true);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b3 = (Button) findViewById(R.id.button8);
			if(dOpt.equals("hide")){
				b3.setVisibility(View.GONE);
			}
			else{
				b3.setEnabled(false);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.GONE);
		}
		if (IOHelper.cpu3Online() == true) {
			Button b4 = (Button) findViewById(R.id.button9);
			if(dOpt.equals("hide")){
				b4.setVisibility(View.VISIBLE);
			}
			else{
				b4.setEnabled(true);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.VISIBLE);

		} else {
			Button b4 = (Button) findViewById(R.id.button9);
			if(dOpt.equals("hide")){
				b4.setVisibility(View.GONE);
			}
			else{
				b4.setEnabled(false);
			}
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.GONE);
		}

		}
		/**
		 * Check for certain files in sysfs and if they doesnt exists hide
		 * depending views
		 */
		if(!(new File(Constants.CPU0_FREQS).exists())){
			if(!(new File(Constants.TIMES_IN_STATE_CPU0).exists())){
				if(dOpt.equals("hide")){
			      cpu.setVisibility(View.GONE);
			    }
			    else{
			      cpu.setEnabled(false);
			    }
			 }
		 }
		if(!(new File(Constants.VOLTAGE_PATH).exists())){
			if(!(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())){
				if(dOpt.equals("hide")){
					voltage.setVisibility(View.GONE);
			    }
			    else{
					voltage.setEnabled(false);
			    }
			}
		}
		if(!(new File(Constants.TIMES_IN_STATE_CPU0).exists())){
			if(dOpt.equals("hide")){
				tis.setVisibility(View.GONE);
			}
			else{
				tis.setEnabled(false);
			}
		}
		if(!(new File(Constants.MPDECISION).exists())){
			if(dOpt.equals("hide")){
				mp.setVisibility(View.GONE);
			}
			else{
				mp.setEnabled(false);
			}
		}
		if(!(new File(Constants.THERMALD).exists())){
			if(dOpt.equals("hide")){
				thermal.setVisibility(View.GONE);
			}
			else{
				thermal.setEnabled(false);
			}
		}
		if(!(new File(Constants.GPU_3D).exists())){
			if(dOpt.equals("hide")){
				gpu.setVisibility(View.GONE);
			}
			else{
				gpu.setEnabled(false);
			}
		}

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
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(2, 2, 2, "Compatibility Check").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(3, 3, 3, "Swap").setShowAsAction(
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
		else if (item.getItemId() == 3) {
		Intent myIntent = new Intent(c, Swap.class);
		startActivity(myIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Press back button twice to exit application
	 */
	@Override
	public void onBackPressed() {
		if(minimal==false){
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
			//java.lang.System.exit(0);
			mLastBackPressTime = 0;
		}
		}
		else{
			KernelTuner.this.finish();
			//java.lang.System.exit(0);
		}
	}

	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		File file;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag1", e.getMessage());
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				file = new File(this.getFilesDir().getAbsolutePath() + "/"
						+ filename);
				out = new FileOutputStream(file);
				if(file.isFile()){
				copyFile(in, out);
				}
				else{
					file.mkdir();
				}
				in.close();
				Runtime.getRuntime().exec("chmod 755 " + file);
				file.setExecutable(true);
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag2", e.getMessage());
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
	
	private class CPUToggle extends AsyncTask<String, Void, Object> {
		@Override
		protected Object doInBackground(String... args) {
			File file = new File("/sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_governor");
			if(file.exists()){
 
		    	CommandCapture command = new CommandCapture(0, 
					"echo 1 > /sys/kernel/msm_mpdecision/conf/enabled",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"echo 0 > /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"chown system /sys/devices/system/cpu/cpu"+args[0]+"/online");
				try{
					RootTools.getShell(true).add(command).waitForFinish();
				}
				catch(Exception e){

				}
            }
				
			else{
		        CommandCapture command = new CommandCapture(0, 
					"echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
					"chmod 666 /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"echo 1 > /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"chmod 444 /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"chown system /sys/devices/system/cpu/cpu"+args[0]+"/online",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_max_freq",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_min_freq",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_cur_freq",
					"chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_governor");
				try{
			RootTools.getShell(true).add(command).waitForFinish();
			}
			catch(Exception e){
				
			}
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

			CommandCapture command = new CommandCapture(0, 
				"mount -t debugfs debugfs /sys/kernel/debug");
			try{
				RootTools.getShell(true).add(command).waitForFinish();
			}
			catch(Exception e){

			}
			return "";
		}

		@Override
		protected void onPostExecute(Object result) {

		}

	}


	
	private class enableTempMonitor extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
 
             CommandCapture command = new CommandCapture(0, 
				"chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode",
				"chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode",
				"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode",
				"echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode");
             try{
            	 RootTools.getShell(true).add(command).waitForFinish();
             }
             catch(Exception e){
            	 
             }
			return "";
		}

	}
	
}
