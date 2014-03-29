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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FrequencyCollection;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SystemInfo extends Activity implements
		ActionBar.TabListener {

	private Integer gpu2d;
	private Integer gpu3d;
	private Integer vsync;
	private Integer fastcharge;
	private Integer cdepth;
	private String kernel;
	private String schedulers;
	private String scheduler;
	private Integer mpdec;
	private Integer s2w;
	private String cpu_info;
	private ProgressDialog pd;

	private static Integer battperc;

	private static Double batttemp;

	private static String battcurrent;

	private SharedPreferences prefs;
	private static String tempPref;
	private List<String>                       freqs    = FrequencyCollection.getInstance().getFrequencyValues();
	private List<IOHelper.VoltageList> voltEntries;
	private List<Integer> voltages = new ArrayList<Integer>();
	private List<String> voltFreq = new ArrayList<String>();
	private String governors;
	private String androidVersion;
	private Integer apiLevel;
	private String cpuAbi;
	private String manufacturer;
	private String bootloader;
	private String hardware;
	private String radio;
	private String board;
	private String brand;
	private String device;
	private String display;
	private String fingerprint;
	private String host;
	private String id;
	private String model;
	private String product;
	private String tags;
	private String type;
	private String user;
	private List<PackageInfo> userApps = new ArrayList<PackageInfo>();
	private List<PackageInfo> systemApps = new ArrayList<PackageInfo>();
	private Integer numberOfInstalledApps;
	private Integer numberOfSystemApps;
	private String screenRezolution;
	private String screenRefreshRate;
	private String screenDensity;
	private String screenPpi;
	TextView oriHead, accHead, magHead, ligHead, proxHead, presHead, tempHead,
			gyroHead, gravHead, humHead, oriAccu, accAccu, magAccu, ligAccu,
			proxAccu, presAccu, tempAccu, gyroAccu, gravAccu, humAccu,
			tv_orientationA, tv_orientationB, tv_orientationC, tv_accelA,
			tv_accelB, tv_accelC, tv_magneticA, tv_magneticB, tv_magneticC,
			tv_lightA, tv_proxA, tv_presA, tv_tempA, tv_gravityA, tv_gravityB,
			tv_gravityC, tv_gyroscopeA, tv_gyroscopeB, tv_gyroscopeC,
			tv_humidity_A;
	ProgressBar pb_orientationA, pb_orientationB, pb_orientationC, pb_accelA,
			pb_accelB, pb_accelC, pb_magneticA, pb_magneticB, pb_magneticC,
			pb_lightA, pb_proxA, pb_presA, pb_tempA, pb_gravityA, pb_gravityB,
			pb_gravityC, pb_gyroscopeA, pb_gyroscopeB, pb_gyroscopeC,
			pb_humidity_A;
	LinearLayout oriLayout, accLayout, magLayout, ligLayout, proxLayout,
			tempLayout, presLayout;
	SensorManager m_sensormgr;
	List<Sensor> m_sensorlist;
	static final int FLOATTOINTPRECISION = 100;

	Boolean isSDPresent;
	String unknown;

	private class info extends AsyncTask<String, Void, Object> {

		private boolean isSystemPackage(PackageInfo pkgInfo) {
			return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
					: false;
		}

		@Override
		protected Object doInBackground(String... args) {
			isSDPresent = android.os.Environment.getExternalStorageState()
					.equals(android.os.Environment.MEDIA_MOUNTED);
			
			voltEntries = IOHelper.voltages();
			
			for (IOHelper.VoltageList v : voltEntries) {
				voltFreq.add(v.getFreqName());
			}
			for (IOHelper.VoltageList v : voltEntries) {
				voltages.add(v.getVoltage());
			}

			List<String> govs = IOHelper.governors();
			StringBuilder builder = new StringBuilder();
			for (String s : govs) {
				builder.append(s + ", ");
			}
			governors = builder.toString();
			androidVersion = Build.VERSION.RELEASE;
			apiLevel = Build.VERSION.SDK_INT;
			cpuAbi = android.os.Build.CPU_ABI;
			manufacturer = android.os.Build.MANUFACTURER;
			bootloader = android.os.Build.BOOTLOADER;
			hardware = android.os.Build.HARDWARE;
			radio = android.os.Build.getRadioVersion();
			
			board = android.os.Build.BOARD;
			brand = android.os.Build.BRAND;
			device = android.os.Build.DEVICE;
			fingerprint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			id = android.os.Build.ID;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			tags = android.os.Build.TAGS;
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;

			List<PackageInfo> apps = getPackageManager()
					.getInstalledPackages(0);
			for (PackageInfo packageInfo : apps) {
				if (isSystemPackage(packageInfo)) {
					systemApps.add(packageInfo);
				} else {
					userApps.add(packageInfo);
				}
			}
			numberOfInstalledApps = userApps.size();
			numberOfSystemApps = systemApps.size();
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();

			display.getSize(size);
				screenRezolution = size.x + "x"
						+ size.y;
			
			screenRefreshRate = display.getRefreshRate()
					+ "fps";

			DisplayMetrics dm = SystemInfo.this.getResources()
					.getDisplayMetrics();
			screenDensity = dm.densityDpi + "dpi";
			screenPpi = "X: " + dm.xdpi + ", Y "
					+ dm.ydpi;

			
				battperc = IOHelper.batteryLevel();
				
				batttemp = IOHelper.batteryTemp()/10.0;
				
				battcurrent = IOHelper.batteryDrain();

			try {

				File myFile = new File("/proc/cpuinfo");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu_info = aBuffer.trim();
				myReader.close();
				fIn.close();
			} catch (Exception e) {
				cpu_info = "err";
			}
			try {
				gpu3d = Integer.parseInt(IOHelper.gpu3d());
			} catch (Exception e) {

			}

			try 
			{
				gpu2d = Integer.parseInt(IOHelper.gpu2d());
			} catch (Exception e) {

			}
				fastcharge = IOHelper.fcharge();
				vsync = IOHelper.vsync();
			try 
			{
				cdepth = Integer.parseInt(IOHelper.cDepth());
			} catch (Exception e) {
				
			}

			
				kernel = IOHelper.kernel();
				

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
				fIn.close();
				scheduler = schedulers.substring(schedulers.indexOf("[") + 1,
						schedulers.indexOf("]"));
				scheduler.trim();
				schedulers = schedulers.replace("[", "");
				schedulers = schedulers.replace("]", "");

			} catch (Exception e) {
				schedulers = "err";
				scheduler = "err";
			}

			

			try {

				File myFile = new File(
						"/sys/kernel/msm_mpdecision/conf/enabled");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				mpdec = Integer.parseInt(aBuffer.trim());
				myReader.close();
				fIn.close();
			} catch (Exception e) {

			}

			

				s2w = IOHelper.s2w();

			

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
		addTabs();
		
		pd.dismiss();
		
		}

	}

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private List<String> tabTitles = new ArrayList<String>();
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		unknown = getResources().getString(R.string.unknown);
		pd = ProgressDialog.show(this, null,getResources().getString(R.string.system_info_please_wait));
		new info().execute();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tempPref = prefs.getString("temp", "celsius");
		// Set up the action bar to show tabs.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		m_sensormgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		m_sensorlist = m_sensormgr.getSensorList(Sensor.TYPE_ALL);

		tabTitles.add(getResources().getString(R.string.system_info_overview));
		tabTitles.add(getResources().getString(R.string.system_info_device));
		tabTitles.add(getResources().getString(R.string.system_info_cpu));
		tabTitles.add(getResources().getString(R.string.system_info_sensors));
		// For each of the sections in the app, add a tab to the action bar.

	}

	public void addTabs() {
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(0))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(1))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(2))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(3))
				.setTabListener(this));
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		/*if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM))
	{
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
				System.out.println("tab count restore"+getSupportActionBar().getNavigationItemCount());
		}*/
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		/*outState.putInt("tr", getSupportActionBar()
				.getSelectedNavigationIndex());*/
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.

		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		if (tab.getText().equals(getResources().getString(R.string.system_info_overview))) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 0);
		} else if (tab.getText().equals(getResources().getString(R.string.system_info_device))) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 1);
		} else if (tab.getText().equals(getResources().getString(R.string.system_info_cpu))) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 2);
		} else if (tab.getText().equals(getResources().getString(R.string.system_info_sensors))) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 3);
		}
		fragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commitAllowingStateLoss();
		
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	
	@SuppressLint("ValidFragment")
	public class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		
		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			container.removeAllViews();
			if (getArguments().getInt(ARG_SECTION_NUMBER) == 0) {
				Overview(inflater, container);
			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				Device(inflater, container);

			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				CPU(inflater, container);

			} else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				Sensors(inflater, container);
			}
			return null;
		}
	}

	public void Sensors(LayoutInflater inflater, ViewGroup container) {
		inflater.inflate(R.layout.system_info_sensors, container);

		oriHead = (TextView) container.findViewById(R.id.TextView_oriHead);
		accHead = (TextView) container.findViewById(R.id.TextView_accHead);
		magHead = (TextView) container.findViewById(R.id.TextView_magHead);
		ligHead = (TextView) container.findViewById(R.id.TextView_ligHead);
		proxHead = (TextView) container.findViewById(R.id.TextView_proxHead);
		presHead = (TextView) container.findViewById(R.id.TextView_presHead);
		tempHead = (TextView) container.findViewById(R.id.TextView_tempHead);
		gravHead = (TextView) container.findViewById(R.id.TextView_gravHead);
		gyroHead = (TextView) container.findViewById(R.id.TextView_gyrHead);
		humHead = (TextView) container.findViewById(R.id.TextView_humHead);

		oriAccu = (TextView) container.findViewById(R.id.oriAccuracy);
		accAccu = (TextView) container.findViewById(R.id.accAccuracy);
		magAccu = (TextView) container.findViewById(R.id.magAccuracy);
		ligAccu = (TextView) container.findViewById(R.id.ligAccuracy);
		proxAccu = (TextView) container.findViewById(R.id.proxAccuracy);
		presAccu = (TextView) container.findViewById(R.id.presAccuracy);
		tempAccu = (TextView) container.findViewById(R.id.tempAccuracy);
		gravAccu = (TextView) container.findViewById(R.id.gravAccuracy);
		gyroAccu = (TextView) container.findViewById(R.id.gyrAccuracy);
		humAccu = (TextView) container.findViewById(R.id.humAccuracy);

		tv_orientationA = (TextView) container.findViewById(R.id.TextView_oriA);
		pb_orientationA = (ProgressBar) this
				.findViewById(R.id.ProgressBar_oriA);
		tv_orientationB = (TextView) container.findViewById(R.id.TextView_oriB);
		pb_orientationB = (ProgressBar) this
				.findViewById(R.id.ProgressBar_oriB);
		tv_orientationC = (TextView) container.findViewById(R.id.TextView_oriC);
		pb_orientationC = (ProgressBar) this
				.findViewById(R.id.ProgressBar_oriC);
		tv_accelA = (TextView) container.findViewById(R.id.TextView_accA);
		pb_accelA = (ProgressBar) this.findViewById(R.id.ProgressBar_accA);
		tv_accelB = (TextView) container.findViewById(R.id.TextView_accB);
		pb_accelB = (ProgressBar) this.findViewById(R.id.ProgressBar_accB);
		tv_accelC = (TextView) container.findViewById(R.id.TextView_accC);
		pb_accelC = (ProgressBar) this.findViewById(R.id.ProgressBar_accC);
		tv_magneticA = (TextView) container.findViewById(R.id.TextView_magA);
		pb_magneticA = (ProgressBar) this.findViewById(R.id.ProgressBar_magA);
		tv_magneticB = (TextView) container.findViewById(R.id.TextView_magB);
		pb_magneticB = (ProgressBar) this.findViewById(R.id.ProgressBar_magB);
		tv_magneticC = (TextView) container.findViewById(R.id.TextView_magC);
		pb_magneticC = (ProgressBar) this.findViewById(R.id.ProgressBar_magC);
		tv_lightA = (TextView) container.findViewById(R.id.TextView_ligA);
		pb_lightA = (ProgressBar) this.findViewById(R.id.ProgressBar_ligA);
		tv_proxA = (TextView) container.findViewById(R.id.TextView_proxA);
		pb_proxA = (ProgressBar) this.findViewById(R.id.ProgressBar_proxA);
		tv_presA = (TextView) container.findViewById(R.id.TextView_presA);
		pb_presA = (ProgressBar) this.findViewById(R.id.ProgressBar_presA);
		tv_tempA = (TextView) container.findViewById(R.id.TextView_tempA);
		pb_tempA = (ProgressBar) this.findViewById(R.id.ProgressBar_tempA);

		tv_gravityA = (TextView) container.findViewById(R.id.TextView_gravA);
		pb_gravityA = (ProgressBar) this.findViewById(R.id.ProgressBar_gravA);
		tv_gravityB = (TextView) container.findViewById(R.id.TextView_gravB);
		pb_gravityB = (ProgressBar) this.findViewById(R.id.ProgressBar_gravB);
		tv_gravityC = (TextView) container.findViewById(R.id.TextView_gravC);
		pb_gravityC = (ProgressBar) this.findViewById(R.id.ProgressBar_gravC);
		tv_gyroscopeA = (TextView) container.findViewById(R.id.TextView_gyrA);
		pb_gyroscopeA = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrA);
		tv_gyroscopeB = (TextView) container.findViewById(R.id.TextView_gyrB);
		pb_gyroscopeB = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrB);
		tv_gyroscopeC = (TextView) container.findViewById(R.id.TextView_gyrC);
		pb_gyroscopeC = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrC);
		tv_humidity_A = (TextView) container.findViewById(R.id.TextView_humA);
		pb_humidity_A = (ProgressBar) this.findViewById(R.id.ProgressBar_humA);

		oriLayout = (LinearLayout) container.findViewById(R.id.oriLayout);
		accLayout = (LinearLayout) container.findViewById(R.id.accLayout);
		magLayout = (LinearLayout) container.findViewById(R.id.magLayout);
		ligLayout = (LinearLayout) container.findViewById(R.id.ligLayout);
		proxLayout = (LinearLayout) container.findViewById(R.id.proxLayout);
		presLayout = (LinearLayout) container.findViewById(R.id.pressLayout);
		tempLayout = (LinearLayout) container.findViewById(R.id.tempLayout);
		connectSensors();

	}

	public void Overview(LayoutInflater inflater, ViewGroup container) {
		Integer freeRAM = getFreeRAM();
		Integer totalRAM = getTotalRAM();
		Integer usedRAM = getTotalRAM() - getFreeRAM();
		long freeInternal = Tools.getAvailableSpaceInBytesOnInternalStorage();
		long usedInternal = Tools.getUsedSpaceInBytesOnInternalStorage();
		long totalInternal = Tools.getTotalSpaceInBytesOnInternalStorage();
		long freeExternal = Tools.getAvailableSpaceInBytesOnExternalStorage();
		long usedExternal = Tools.getUsedSpaceInBytesOnExternalStorage();
		long totalExternal = Tools.getTotalSpaceInBytesOnExternalStorage();

		inflater.inflate(R.layout.system_info_overview, container);
		TextView level = (TextView) container.findViewById(R.id.textView1);
		ProgressBar levelProgress = (ProgressBar) container
				.findViewById(R.id.progressBar1);
		TextView temp = (TextView) container.findViewById(R.id.textView3);
		TextView drain = (TextView) container.findViewById(R.id.textView5);
		TextView totalRAMtxt = (TextView) container
				.findViewById(R.id.textView7);
		TextView freeRAMtxt = (TextView) container.findViewById(R.id.textView8);
		ProgressBar ramProgress = (ProgressBar) container
				.findViewById(R.id.progressBar2);
		TextView totalInternaltxt = (TextView) container.findViewById(R.id.textView10);
		TextView freeInternaltxt = (TextView) container.findViewById(R.id.textView11);
		ProgressBar internalProgress = (ProgressBar) container
				.findViewById(R.id.progressBar3);
		TextView totalExternaltxt = (TextView) container
				.findViewById(R.id.textView13);
		TextView freeExternaltxt = (TextView) container
				.findViewById(R.id.textView14);
		ProgressBar externalProgress = (ProgressBar) container
				.findViewById(R.id.progressBar4);
		TextView Externaltxt = (TextView) container
				.findViewById(R.id.textView12);

		if (battperc != null) {
			level.setText("Level: " + battperc + "%");
			levelProgress.setProgress(battperc);
		} else {
			level.setText(unknown);
		}
		if (batttemp != null) {
			temp.setText(Tools.tempConverter(tempPref, batttemp));
		} else {
			temp.setText(unknown);
		}
		if (battcurrent.length()>0) {
			drain.setText(battcurrent + "mAh");
			if (battcurrent.substring(0, 1).equals("-"))
			{
				
			drain.setTextColor(Color.RED);
			}
			else
			{
				drain.setText("+"+battcurrent + "mAh");
			drain.setTextColor(Color.GREEN);
			}
		} else {
			drain.setText(unknown);
		}
		totalRAMtxt.setText("Total: " + totalRAM + "MB");
		freeRAMtxt.setText("Free: " +freeRAM + "MB");
		ramProgress.setProgress(usedRAM * 100 / totalRAM);

		totalInternaltxt.setText("Total: " + Tools.byteToHumanReadableSize(totalInternal));
		freeInternaltxt.setText("Free: " + Tools.byteToHumanReadableSize(freeInternal));
		internalProgress
				.setProgress((int) (usedInternal * 100 / totalInternal));
		if (isSDPresent) {
			totalExternaltxt.setText("Total: "
									 + Tools.byteToHumanReadableSize(totalExternal));
			freeExternaltxt.setText("Free: " + Tools.byteToHumanReadableSize(freeExternal));
			externalProgress
					.setProgress((int) (usedExternal * 100 / totalExternal));
		} else {
			Externaltxt.setText("External Storage not present");
			totalExternaltxt.setVisibility(View.GONE);
			freeExternaltxt.setVisibility(View.GONE);
			externalProgress.setVisibility(View.GONE);
		}
	}

	public void Device(LayoutInflater inflater, ViewGroup container) {
		inflater.inflate(R.layout.system_info_device, container);
		TextView androidVersiontxt = (TextView) container
				.findViewById(R.id.androidVersion);
		TextView apitxt = (TextView) container.findViewById(R.id.api);
		TextView cpuAbitxt = (TextView) container.findViewById(R.id.cpuAbi);
		TextView manufacturertxt = (TextView) container
				.findViewById(R.id.manufacturer);
		TextView bootloadertxt = (TextView) container
				.findViewById(R.id.bootloader);
		TextView hardwaretxt = (TextView) container.findViewById(R.id.hardware);
		TextView radiotxt = (TextView) container.findViewById(R.id.radio);
		TextView boardtxt = (TextView) container.findViewById(R.id.board);
		TextView brandtxt = (TextView) container.findViewById(R.id.brand);
		TextView devicetxt = (TextView) container.findViewById(R.id.device);
		TextView displaytxt = (TextView) container.findViewById(R.id.display);
		TextView fingerprinttxt = (TextView) container
				.findViewById(R.id.fingerprint);
		TextView hosttxt = (TextView) container.findViewById(R.id.host);
		TextView idtxt = (TextView) container.findViewById(R.id.id);
		TextView modeltxt = (TextView) container.findViewById(R.id.model);
		TextView producttxt = (TextView) container.findViewById(R.id.product);
		TextView tagstxt = (TextView) container.findViewById(R.id.tags);
		TextView typetxt = (TextView) container.findViewById(R.id.type);
		TextView usertxt = (TextView) container.findViewById(R.id.user);
		TextView userAppstxt = (TextView) container.findViewById(R.id.userApps);
		TextView systemAppstxt = (TextView) container
				.findViewById(R.id.systemApps);
		TextView screenRestxt = (TextView) container
				.findViewById(R.id.screenResolution);
		TextView screenRefreshratetxt = (TextView) container
				.findViewById(R.id.screenRefreshRate);
		TextView screenDensitytxt = (TextView) container
				.findViewById(R.id.screenDensity);
		TextView screenPPItxt = (TextView) container
				.findViewById(R.id.screenPPI);
		TextView kerneltxt = (TextView) container.findViewById(R.id.kernel);
		TextView gpu2dtxt = (TextView) container.findViewById(R.id.gpu2d);
		TextView gpu3dtxt = (TextView) container.findViewById(R.id.gpu3d);
		TextView vsynctxt = (TextView) container.findViewById(R.id.vsync);
		TextView fastchargetxt = (TextView) container.findViewById(R.id.fastcharge);
		TextView colorDepthtxt = (TextView) container.findViewById(R.id.cdepth);
		TextView schedulerstxt = (TextView) container.findViewById(R.id.schedulers);
		TextView s2wtxt = (TextView) container.findViewById(R.id.s2w);
		if(gpu2d!=null){
			gpu2dtxt.setText((gpu2d/1000000)+"MHz");
		}
		else{
			gpu2dtxt.setText(unknown);
		}
		if(gpu3d!=null){
			gpu3dtxt.setText((gpu3d/1000000)+"MHz");
		}
		else{
			gpu3dtxt.setText(unknown);
		}
		if(vsync!=null){
			if(vsync==0){
			vsynctxt.setText("OFF");
			vsynctxt.setTextColor(Color.RED);
			}
			else if(vsync==1){
				vsynctxt.setText("ON");
				vsynctxt.setTextColor(Color.GREEN);
			}
			else{
				vsynctxt.setText(unknown);
			}
		}
		else{
			vsynctxt.setText(unknown);
		}
		if(fastcharge!=null){
			if(fastcharge==0){
				fastchargetxt.setText("OFF");
				fastchargetxt.setTextColor(Color.RED);
			}
			else if(fastcharge==1){
				fastchargetxt.setText("ON");
				fastchargetxt.setTextColor(Color.GREEN);
			}
			else{
				fastchargetxt.setText(unknown);
			}
		}
		else{
			fastchargetxt.setText(unknown);
		}
		if(cdepth!=null){
			colorDepthtxt.setText(cdepth+"-bit");
		}
		else{
			colorDepthtxt.setText(unknown);
		}
		schedulerstxt.setText(schedulers);
		if(s2w!=null){
			if(s2w==0){
			s2wtxt.setText("OFF");
			}
			else{
				s2wtxt.setText("ON");
			}
		}
		else{
			s2wtxt.setText(unknown);
		}
		androidVersiontxt.setText(androidVersion);
		apitxt.setText(apiLevel+"");
		if (apiLevel >= 14) {
			androidVersiontxt.setTextColor(Color.GREEN);
			apitxt.setTextColor(Color.GREEN);
		} else {
			androidVersiontxt.setTextColor(Color.RED);
			apitxt.setTextColor(Color.RED);
		}
		cpuAbitxt.setText(cpuAbi);
		manufacturertxt.setText(manufacturer);
		bootloadertxt.setText(bootloader);
		hardwaretxt.setText(hardware);
		radiotxt.setText(radio);
		boardtxt.setText(board);
		brandtxt.setText(brand);
		devicetxt.setText(device);
		displaytxt.setText(display);
		fingerprinttxt.setText(fingerprint);
		hosttxt.setText(host);
		idtxt.setText(id);
		modeltxt.setText(model);
		producttxt.setText(product);
		tagstxt.setText(tags);
		typetxt.setText(type);
		usertxt.setText(user);
		userAppstxt.setText(numberOfInstalledApps+"");
		systemAppstxt.setText(numberOfSystemApps+"");
		screenRestxt.setText(screenRezolution);
		screenRefreshratetxt.setText(screenRefreshRate);
		screenDensitytxt.setText(screenDensity);
		screenPPItxt.setText(screenPpi);
		kerneltxt.setText(kernel);
	}

	public void CPU(LayoutInflater inflater, ViewGroup container) {
		inflater.inflate(R.layout.system_info_cpu, container);
		TextView cpu = (TextView) container.findViewById(R.id.tv);
		TextView freqRange = (TextView) container.findViewById(R.id.freqRange);
		TextView mpdectxt = (TextView) container.findViewById(R.id.mpdec);
		TextView thermal = (TextView) container.findViewById(R.id.thermal);
		TextView governorstxt = (TextView) container
				.findViewById(R.id.governors);
		TextView voltRange = (TextView) container.findViewById(R.id.voltRange);
		LinearLayout mpdecLayout = (LinearLayout) container
				.findViewById(R.id.mpdecLayout);
		LinearLayout thermalLayout = (LinearLayout) container
				.findViewById(R.id.thermalLayout);
		LinearLayout voltageLayout = (LinearLayout) container
				.findViewById(R.id.voltageLayout);

		cpu.setText(cpu_info);
		if (freqs.isEmpty() == false) {
			freqRange.setText(freqs.get(0) + " - "
					+ freqs.get(freqs.size() - 1));
		} else {
			freqRange.setText(unknown);
		}
		if (mpdec != null) {
			if (mpdec == 0) {
				mpdectxt.setText("OFF");
				mpdectxt.setTextColor(Color.RED);
			} else if (mpdec == 1) {
				mpdectxt.setText("ON");
				mpdectxt.setTextColor(Color.GREEN);
			} else {
				mpdectxt.setText(unknown);
				mpdectxt.setTextColor(Color.RED);
			}
		} else {
			mpdecLayout.setVisibility(View.GONE);
		}
		if (new File("/sys/kernel/msm_thermal/conf").exists()) {
			thermal.setText("ON");
			thermal.setTextColor(Color.GREEN);
		} else {
			thermalLayout.setVisibility(View.GONE);
		}
		if (governors.equals("")) {
			governorstxt.setText(unknown);
		} else {
			governorstxt.setText(governors);
		}
		if (voltages.isEmpty() == false) {
			voltRange.setText((voltages.get(0) / 1000) + "mV("
					+ voltFreq.get(0) + ") - "
					+ (voltages.get(voltages.size() - 1) / 1000)
					+ "mV(" + voltFreq.get(voltFreq.size() - 1) + ")");
		} else {
			voltageLayout.setVisibility(View.GONE);
		}
	}


	public static Integer getTotalRAM() {
		RandomAccessFile reader = null;
		String load = null;
		Integer mem = null;
		try {
			reader = new RandomAccessFile("/proc/meminfo", "r");
			load = reader.readLine();
			mem = Integer.parseInt(load.substring(load.indexOf(":") + 1,
					load.lastIndexOf(" ")).trim()) / 1024;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return mem;
	}

	public Integer getFreeRAM() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		Integer mem = (int) (mi.availMem / 1048576L);
		return mem;

	}



	protected String getSensorInfo(Sensor sen) {
		String sensorInfo = "INVALID";
		String snsType;

		switch (sen.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			snsType = "TYPE_ACCELEROMETER";
			break;
		case Sensor.TYPE_ALL:
			snsType = "TYPE_ALL";
			break;
		case Sensor.TYPE_GYROSCOPE:
			snsType = "TYPE_GYROSCOPE";
			break;
		case Sensor.TYPE_LIGHT:
			snsType = "TYPE_LIGHT";
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			snsType = "TYPE_MAGNETIC_FIELD";
			break;
		case Sensor.TYPE_ORIENTATION:
			snsType = "TYPE_ORIENTATION";
			break;
		case Sensor.TYPE_PRESSURE:
			snsType = "TYPE_PRESSURE";
			break;
		case Sensor.TYPE_PROXIMITY:
			snsType = "TYPE_PROXIMITY";
			break;
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
			snsType = "TYPE_TEMPERATURE";
			break;
		default:
			snsType = "UNKNOWN_TYPE " + sen.getType();
			break;
		}

		sensorInfo = sen.getName() + "\n";
		sensorInfo += "Version: " + sen.getVersion() + "\n";
		sensorInfo += "Vendor: " + sen.getVendor() + "\n";
		sensorInfo += "Type: " + snsType + "\n";
		sensorInfo += "MaxRange: " + sen.getMaximumRange() + "\n";
		sensorInfo += "Resolution: "
				+ String.format("%.5f", sen.getResolution()) + "\n";
		sensorInfo += "Power: " + sen.getPower() + " mA\n";
		return sensorInfo;
	}

	SensorEventListener senseventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			String accuracy;

			switch (event.accuracy) {
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
				accuracy = "SENSOR_STATUS_ACCURACY_HIGH";
				break;
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
				accuracy = "SENSOR_STATUS_ACCURACY_MEDIUM";
				break;
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
				accuracy = "SENSOR_STATUS_ACCURACY_LOW";
				break;
			case SensorManager.SENSOR_STATUS_UNRELIABLE:
				accuracy = "SENSOR_STATUS_UNRELIABLE";
				break;
			default:
				accuracy = unknown;
			}

			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				oriAccu.setText(accuracy);
				pb_orientationA.setProgress((int) event.values[0]);
				pb_orientationB.setProgress(Math.abs((int) event.values[1]));
				pb_orientationC.setProgress(Math.abs((int) event.values[2]));
				tv_orientationA.setText(String.format("%.1f", event.values[0]));
				tv_orientationB.setText(String.format("%.1f", event.values[1]));
				tv_orientationC.setText(String.format("%.1f", event.values[2]));
			}
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accAccu.setText(accuracy);
				pb_accelA.setProgress(Math.abs((int) event.values[0]
						* FLOATTOINTPRECISION));
				pb_accelB.setProgress(Math.abs((int) event.values[1]
						* FLOATTOINTPRECISION));
				pb_accelC.setProgress(Math.abs((int) event.values[2]
						* FLOATTOINTPRECISION));
				tv_accelA.setText(String.format("%.2f", event.values[0]));
				tv_accelB.setText(String.format("%.2f", event.values[1]));
				tv_accelC.setText(String.format("%.2f", event.values[2]));
			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magAccu.setText(accuracy);
				pb_magneticA.setProgress(Math.abs((int) event.values[0]
						* FLOATTOINTPRECISION));
				pb_magneticB.setProgress(Math.abs((int) event.values[1]
						* FLOATTOINTPRECISION));
				pb_magneticC.setProgress(Math.abs((int) event.values[2]
						* FLOATTOINTPRECISION));
				tv_magneticA.setText(String.format("%.2f", event.values[0]));
				tv_magneticB.setText(String.format("%.2f", event.values[1]));
				tv_magneticC.setText(String.format("%.2f", event.values[2]));
			}
			if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
				ligAccu.setText(accuracy);
				pb_lightA.setProgress(Math.abs((int) event.values[0]));
				tv_lightA.setText(String.format("%.2f", event.values[0]));
			}
			if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
				proxAccu.setText(accuracy);
				pb_proxA.setProgress(Math.abs((int) event.values[0]));
				tv_proxA.setText(String.format("%.2f", event.values[0]));
			}
			if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
				presAccu.setText(accuracy);
				pb_presA.setProgress(Math.abs((int) event.values[0]));
				tv_presA.setText(String.format("%.2f", event.values[0]));
			}
			if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				tempAccu.setText(accuracy);
				pb_tempA.setProgress(Math.abs((int) event.values[0]));
				tv_tempA.setText(String.format("%.2f", event.values[0]));
			}
			if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
				gravAccu.setText(accuracy);
				pb_gravityA.setProgress(Math.abs((int) event.values[0]
						* FLOATTOINTPRECISION));
				pb_gravityB.setProgress(Math.abs((int) event.values[1]
						* FLOATTOINTPRECISION));
				pb_gravityC.setProgress(Math.abs((int) event.values[2]
						* FLOATTOINTPRECISION));
				tv_gravityA.setText(String.format("%.2f", event.values[0]));
				tv_gravityB.setText(String.format("%.2f", event.values[1]));
				tv_gravityC.setText(String.format("%.2f", event.values[2]));
			}
			if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				gyroAccu.setText(accuracy);
				pb_gyroscopeA.setProgress(Math.abs((int) event.values[0]
						* FLOATTOINTPRECISION));
				pb_gyroscopeB.setProgress(Math.abs((int) event.values[1]
						* FLOATTOINTPRECISION));
				pb_gyroscopeC.setProgress(Math.abs((int) event.values[2]
						* FLOATTOINTPRECISION));
				tv_gyroscopeA.setText(String.format("%.2f", event.values[0]));
				tv_gyroscopeB.setText(String.format("%.2f", event.values[1]));
				tv_gyroscopeC.setText(String.format("%.2f", event.values[2]));
			}
			if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
				humAccu.setText(accuracy);
				pb_humidity_A.setProgress(Math.abs((int) event.values[0]));
				tv_humidity_A.setText(String.format("%.2f", event.values[0]));
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	};

	protected void connectSensors() {
		m_sensormgr.unregisterListener(senseventListener);
		if (!m_sensorlist.isEmpty()) {
			Sensor snsr;
			int m_sensorListSize = m_sensorlist.size();
			for (int i = 0; i < m_sensorListSize; i++) {
				snsr = m_sensorlist.get(i);

				if (snsr.getType() == Sensor.TYPE_ORIENTATION) {
					oriHead.setText(getSensorInfo(snsr));
					pb_orientationA.setMax((int) snsr.getMaximumRange());
					pb_orientationB.setMax((int) snsr.getMaximumRange());
					pb_orientationC.setMax((int) snsr.getMaximumRange());
					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_ACCELEROMETER) {
					accHead.setText(getSensorInfo(snsr));
					pb_accelA
							.setMax((int) (snsr.getMaximumRange()
									* SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
					pb_accelB
							.setMax((int) (snsr.getMaximumRange()
									* SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
					pb_accelC
							.setMax((int) (snsr.getMaximumRange()
									* SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));

					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
					magHead.setText(getSensorInfo(snsr));
					pb_magneticA
							.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
					pb_magneticB
							.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
					pb_magneticC
							.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));

					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_LIGHT) {
					ligHead.setText(getSensorInfo(snsr));
					pb_lightA.setMax((int) (snsr.getMaximumRange()));
					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_PROXIMITY) {

					proxHead.setText(getSensorInfo(snsr));
					pb_proxA.setMax((int) (snsr.getMaximumRange()));
					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_PRESSURE) {
					presHead.setText(getSensorInfo(snsr));
					pb_presA.setMax((int) (snsr.getMaximumRange()));
					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {

					tempHead.setText(getSensorInfo(snsr));
					pb_tempA.setMax((int) (snsr.getMaximumRange()));
					m_sensormgr.registerListener(senseventListener, snsr,
							SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_GYROSCOPE) {
					gyroHead.setText(getSensorInfo(snsr));
					pb_gyroscopeA
						.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
					pb_gyroscopeB
						.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
					pb_gyroscopeC
						.setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));

					m_sensormgr.registerListener(senseventListener, snsr,
												 SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_GRAVITY) {
					gravHead.setText(getSensorInfo(snsr));
					pb_gravityA
						.setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
					pb_gravityB
						.setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
					pb_gravityC
						.setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));

					m_sensormgr.registerListener(senseventListener, snsr,
												 SensorManager.SENSOR_DELAY_NORMAL);
				}
				if (snsr.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {

					humHead.setText(getSensorInfo(snsr));
					pb_humidity_A.setMax((int) (snsr.getMaximumRange()));
					m_sensormgr.registerListener(senseventListener, snsr,
												 SensorManager.SENSOR_DELAY_NORMAL);
				}

			}
		}
	}

	@Override
	protected void onDestroy() {
		m_sensormgr.unregisterListener(senseventListener);
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, KernelTuner.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
