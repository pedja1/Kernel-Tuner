package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
 @Override
 public void onCreate(Bundle savedInstanceState)
 {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.system_info);

 ActionBar actionBar = getSupportActionBar();
 actionBar.setDisplayHomeAsUpEnabled(true);

 new info().execute();
 RelativeLayout cpu = (RelativeLayout)findViewById(R.id.cpu);
 final RelativeLayout cpuInfo = (RelativeLayout)findViewById(R.id.cpu_info);
 final ImageView cpuImg = (ImageView)findViewById(R.id.cpu_img);

 RelativeLayout other = (RelativeLayout)findViewById(R.id.other);
 final RelativeLayout otherInfo = (RelativeLayout)findViewById(R.id.other_info);
 final ImageView otherImg = (ImageView)findViewById(R.id.other_img);

 RelativeLayout kernel = (RelativeLayout)findViewById(R.id.kernel);
 final RelativeLayout kernelInfo = (RelativeLayout)findViewById(R.id.kernel_info);
 final ImageView kernelImg = (ImageView)findViewById(R.id.kernel_img);

 RelativeLayout device = (RelativeLayout)findViewById(R.id.device);
 final RelativeLayout deviceInfo = (RelativeLayout)findViewById(R.id.device_info);
 final ImageView deviceImg = (ImageView)findViewById(R.id.device_img);


 cpu.setOnClickListener(new OnClickListener(){

 @Override
 public void onClick(View arg0)
 {
 if (cpuInfo.getVisibility() == View.VISIBLE)
 {
 cpuInfo.setVisibility(View.GONE);
 cpuImg.setImageResource(R.drawable.arrow_right);
 }
 else if (cpuInfo.getVisibility() == View.GONE)
 {
 cpuInfo.setVisibility(View.VISIBLE);
 cpuImg.setImageResource(R.drawable.arrow_down);
 }
 }

 });

 device.setOnClickListener(new OnClickListener(){

 @Override
 public void onClick(View arg0)
 {
 if (deviceInfo.getVisibility() == View.VISIBLE)
 {
 deviceInfo.setVisibility(View.GONE);
 deviceImg.setImageResource(R.drawable.arrow_right);
 }
 else if (deviceInfo.getVisibility() == View.GONE)
 {
 deviceInfo.setVisibility(View.VISIBLE);
 deviceImg.setImageResource(R.drawable.arrow_down);
 }
 }

 });

 kernel.setOnClickListener(new OnClickListener(){

 @Override
 public void onClick(View arg0)
 {
 if (kernelInfo.getVisibility() == View.VISIBLE)
 {
 kernelInfo.setVisibility(View.GONE);
 kernelImg.setImageResource(R.drawable.arrow_right);
 }
 else if (kernelInfo.getVisibility() == View.GONE)
 {
 kernelInfo.setVisibility(View.VISIBLE);
 kernelImg.setImageResource(R.drawable.arrow_down);
 }
 }

 });

 other.setOnClickListener(new OnClickListener(){

 @Override
 public void onClick(View arg0)
 {
 if (otherInfo.getVisibility() == View.VISIBLE)
 {
 otherInfo.setVisibility(View.GONE);
 otherImg.setImageResource(R.drawable.arrow_right);
 }
 else if (otherInfo.getVisibility() == View.GONE)
 {
 otherInfo.setVisibility(View.VISIBLE);
 otherImg.setImageResource(R.drawable.arrow_down);
 }
 }

 });
 }

 @Override
 public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
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
 }*/
public class SystemInfo extends SherlockFragmentActivity implements
		ActionBar.TabListener {

	private Integer cpu0max;
	private Integer cpu1max;
	private Integer cpu0min;
	private Integer cpu1min;
	private Integer cpu2max;
	private Integer cpu3max;
	private Integer cpu2min;
	private Integer cpu3min;
	private Integer gpu2d;
	private Integer gpu3d;
	private String vsync;
	private String fastcharge;
	private String cdepth;
	private String kernel;
	private String schedulers;
	private String scheduler;
	private String sdcache;;
	private String curentgovernorcpu0;
	private String curentgovernorcpu1;
	private String curentgovernorcpu2;
	private String curentgovernorcpu3;
	private String led;
	private String mpdec;
	private String s2w;
	private String cpu_info;
	private ProgressDialog pd;

	private static String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	private static String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	private static String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	private static String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	private static String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	private static String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	private static String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	private static String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	private static String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	private static String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	private static String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	private static String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";
	private String battcap;
	private static Integer battperc;
	private String charge;
	private static Double batttemp;
	private String battvol;
	private String batttech;
	private static String battcurrent;
	private String batthealth;
	private SharedPreferences prefs;
	private static String tempPref;
	
	private class info extends AsyncTask<String, Void, Object> {

		

		@Override
		protected Object doInBackground(String... args) {

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/capacity");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				battperc = Integer.parseInt(aBuffer.trim());
				myReader.close();

			}
			catch (Exception e)
			{
				
			}

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/charging_source");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				charge = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				charge = "0";
			}

			//System.out.println(cpu0min);

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/batt_temp");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				batttemp = Double.parseDouble(aBuffer.trim())/10;
				myReader.close();

			}
			catch (Exception e)
			{
		
			}

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/batt_vol");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				battvol = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				battvol = "0";
			}

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/technology");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				batttech = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				batttech = "err";
			}	

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/batt_current");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				battcurrent = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				battcurrent = "err";
			}	

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/health");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				batthealth = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				batthealth = "err";
			}	

			try
			{

				File myFile = new File("/sys/class/power_supply/battery/full_bat");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				battcap = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				battcap = "err";
			}	

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

			} catch (Exception e) {
				cpu_info = "err";
			}

			try {

				File myFile = new File(CPU0_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu0min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

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

				cpu0max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(CPU1_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu1min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

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

				cpu1max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(CPU0_CURR_GOV);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu0 = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				curentgovernorcpu0 = "err";
			}

			try {

				File myFile = new File(CPU1_CURR_GOV);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu1 = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				curentgovernorcpu1 = "err";
			}

			try {

				File myFile = new File(CPU2_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu2min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

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

				cpu2max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(CPU3_MIN_FREQ);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu3min = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

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

				cpu3max = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(CPU2_CURR_GOV);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu2 = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				curentgovernorcpu2 = "err";
			}

			try {

				File myFile = new File(CPU3_CURR_GOV);
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu3 = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				curentgovernorcpu3 = "err";
			}

			try {
				String aBuffer = "";
				File myFile = new File(
						"/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				led = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				led = "err";
			}

			try {

				File myFile = new File(
						"/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu3d = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(
						"/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu2d = Integer.parseInt(aBuffer.trim());

				myReader.close();

			} catch (Exception e) {

			}

			try {
				String aBuffer = "";
				File myFile = new File(
						"/sys/kernel/fast_charge/force_fast_charge");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				fastcharge = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				fastcharge = "err";
			}

			try {
				String aBuffer = "";
				File myFile = new File(
						"/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				vsync = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				vsync = "err";
			}

			try {

				File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cdepth = aBuffer.trim();
				myReader.close();
				// Log.d("done",cdepth);

			} catch (IOException e) {
				cdepth = "err";
				;
			}

			try {

				File myFile = new File("/proc/version");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				kernel = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				kernel = "Kernel version file not found";

			}

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
						"/sys/devices/virtual/bdi/179:0/read_ahead_kb");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				sdcache = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				sdcache = "err";

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

				mpdec = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				mpdec = "err";

			}

			try {

				File myFile = new File("/sys/android_touch/sweep2wake");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				s2w = aBuffer.trim();

				myReader.close();

			} catch (Exception e) {

				try {

					File myFile = new File(
							"/sys/android_touch/sweep2wake/s2w_switch");
					FileInputStream fIn = new FileInputStream(myFile);

					BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}

					s2w = aBuffer.trim();

					myReader.close();

				} catch (Exception e2) {

					s2w = "err";
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			
			pd.dismiss();
			/*TextView cpuinfo = (TextView) findViewById(R.id.cpu_i);
			cpuinfo.setText(cpu_info);
			TextView board = (TextView) findViewById(R.id.board);
			TextView device = (TextView) findViewById(R.id.deviceTxt);
			TextView display = (TextView) findViewById(R.id.display);
			TextView bootloader = (TextView) findViewById(R.id.bootloader);
			TextView brand = (TextView) findViewById(R.id.brand);
			TextView hardware = (TextView) findViewById(R.id.hardware);
			TextView manufacturer = (TextView) findViewById(R.id.manufacturer);
			TextView model = (TextView) findViewById(R.id.model);
			TextView product = (TextView) findViewById(R.id.product);
			TextView radio = (TextView) findViewById(R.id.radio);
			board.setText(android.os.Build.BOARD);
			device.setText(android.os.Build.DEVICE);
			display.setText(android.os.Build.DISPLAY);
			bootloader.setText(android.os.Build.BOOTLOADER);
			brand.setText(android.os.Build.BRAND);
			hardware.setText(android.os.Build.HARDWARE);
			manufacturer.setText(android.os.Build.MANUFACTURER);
			model.setText(android.os.Build.MODEL);
			product.setText(android.os.Build.PRODUCT);
			if (android.os.Build.VERSION.SDK_INT > 10) {
				if (android.os.Build.getRadioVersion() != null) {
					radio.setText(android.os.Build.getRadioVersion());
				}
			}*/
		}

	}

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private List<String> tabTitles = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info_test);
		System.out.println(getTotalRAM());
		pd = ProgressDialog.show(this, null, "Gathering system information\nPlease wait...");
		new info().execute();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tempPref = prefs.getString("temp", "celsius");
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabTitles.add("Overview");
		tabTitles.add("Device");
		tabTitles.add("CPU");
		tabTitles.add("Sensors");
		tabTitles.add("Other");
		// For each of the sections in the app, add a tab to the action bar.

		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(0))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(1))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(2))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(3))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(tabTitles.get(4))
				.setTabListener(this));

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.

		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		if (tab.getText().equals("Overview")) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 0);
		}
		else if (tab.getText().equals("Device")) {
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 1);
		}
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
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
			}
			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				inflater.inflate(R.layout.system_info, container);
				
			}
			return null;
		}
	}
	
	public void Overview(LayoutInflater inflater, ViewGroup container){
		Integer freeRAM = getFreeRAM();
		Integer totalRAM = getTotalRAM();
		Integer usedRAM = getTotalRAM()- getFreeRAM();
		long freeInternal =  getAvailableSpaceInBytesOnInternalStorage();
		long usedInternal =  getUsedSpaceInBytesOnInternalStorage();
		long totalInternal = getTotalSpaceInBytesOnInternalStorage();
		long freeExternal =  getAvailableSpaceInBytesOnExternalStorage();
		long usedExternal =  getUsedSpaceInBytesOnExternalStorage();
		long totalExternal = getTotalSpaceInBytesOnExternalStorage();
		
		inflater.inflate(R.layout.system_info_overview, container);
		TextView level = (TextView)container.findViewById(R.id.textView1);
		ProgressBar levelProgress = (ProgressBar)container.findViewById(R.id.progressBar1);
		TextView temp = (TextView)container.findViewById(R.id.textView3);
		TextView drain = (TextView)container.findViewById(R.id.textView5);
		TextView totalRAMtxt = (TextView)container.findViewById(R.id.textView7);
		TextView freeRAMtxt = (TextView)container.findViewById(R.id.textView8);
		ProgressBar ramProgress = (ProgressBar)container.findViewById(R.id.progressBar2);
		TextView totalInternaltxt = (TextView)container.findViewById(R.id.textView10);
		TextView freeInternaltxt = (TextView)container.findViewById(R.id.textView11);
		ProgressBar internalProgress = (ProgressBar)container.findViewById(R.id.progressBar3);
		TextView totalExternaltxt = (TextView)container.findViewById(R.id.textView13);
		TextView freeExternaltxt = (TextView)container.findViewById(R.id.textView14);
		ProgressBar externalProgress = (ProgressBar)container.findViewById(R.id.progressBar4);
		
		if(battperc!=null){
		level.setText("Level: "+String.valueOf(battperc)+"%");
		levelProgress.setProgress(battperc);
		}
		else{
			level.setText("Unknown");
		}
		if(batttemp!=null){
		temp.setText(tempConverter(tempPref, batttemp));
		}
		else{
			temp.setText("Unknown");
		}
		if(!battcurrent.equals("err")){
		drain.setText(battcurrent+"mAh");
		}
		else{
			drain.setText("Unknown");
		}
		totalRAMtxt.setText("Total: "+String.valueOf(totalRAM)+"MB");
		freeRAMtxt.setText("Free: "+String.valueOf(freeRAM)+"MB");
		ramProgress.setProgress(usedRAM*100/totalRAM);
		
		totalInternaltxt.setText("Total: "+humanReadableSize(totalInternal));
		freeInternaltxt.setText("Free: "+humanReadableSize(freeInternal));
		internalProgress.setProgress((int)(usedInternal*100/totalInternal));
		
		totalExternaltxt.setText("Total: "+humanReadableSize(totalExternal));
		freeExternaltxt.setText("Free: "+humanReadableSize(freeExternal));
		externalProgress.setProgress((int)(usedExternal*100/totalExternal));
		
	}
	
	public static String tempConverter(String tempPref, double cTemp){
		String tempNew = "";
		/**
		 * cTemp = temperature in celsius
		 * tempPreff = string from shared preferences with value fahrenheit, celsius or kelvin
		*/
		if (tempPref.equals("fahrenheit"))
		{
			tempNew = String.valueOf((cTemp * 1.8) + 32)+"°F";
			
			
		}
		else if (tempPref.equals("celsius"))
		{
			tempNew = String.valueOf(cTemp)+"°C";
			
		}
		else if (tempPref.equals("kelvin"))
		{
			
			 tempNew = String.valueOf(cTemp+273.15)+"°C";
			
		}
		return tempNew;
	}
	
	public static Integer getTotalRAM() {
	    RandomAccessFile reader = null;
	    String load = null;
	    Integer mem = null;
	    try {
	        reader = new RandomAccessFile("/proc/meminfo", "r");
	        load = reader.readLine();
	        mem= Integer.parseInt(load.substring(load.indexOf(":")+1, load.lastIndexOf(" ")).trim())/1024;
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
	
	public Integer getFreeRAM(){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		Integer mem = (int) (mi.availMem / 1048576L);
		return mem;
		
	}
	
	public static long getAvailableSpaceInBytesOnInternalStorage() {
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
	public static long getUsedSpaceInBytesOnInternalStorage() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks()) * (long) stat.getBlockSize();

	    return usedSpace;
	}
	
	public static long getTotalSpaceInBytesOnInternalStorage() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

	    return usedSpace;
	}
	
	public static long getAvailableSpaceInBytesOnExternalStorage() {
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
	public static long getUsedSpaceInBytesOnExternalStorage() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks()) * (long) stat.getBlockSize();

	    return usedSpace;
	}
	
	public static long getTotalSpaceInBytesOnExternalStorage() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

	    return usedSpace;
	}

	public String humanReadableSize(long size){
		String hrSize = "";
		
		long b = size;
		double k = size/1024.0;
		double m = size/1048576.0;
		double g = size/1073741824.0;
		double t = size/1099511627776.0;
		
		DecimalFormat dec = new DecimalFormat("0.00");
	
		if (t>1)
		{
	
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
		
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
	
			hrSize = dec.format(k).concat("KB");

		}
		else if(b>1){
			hrSize = dec.format(b).concat("B");
		}
		
		
		
		
		return hrSize;
		
	}

}
