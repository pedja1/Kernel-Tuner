package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.io.InputStreamReader;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import de.ankri.views.Switch;

public class MiscTweaks extends SherlockActivity
{

	public String led = CPUInfo.leds();
	public String ledHox;
	SeekBar mSeekBar;
	TextView progresstext;
	
	
	public String fc = " ";
	private int fastcharge = CPUInfo.fcharge();
	private int vsync = CPUInfo.vsync();
	public String vs;
	public String hw;
	public String backbuf;
	public String cdepth = CPUInfo.cDepth();
	public Integer sdcache = CPUInfo.sdCache();
	private  List<String> schedulers = CPUInfo.schedulers();
	public String scheduler = CPUInfo.scheduler();
	public int ledprogress;
	public SharedPreferences preferences;
	ProgressBar prog;
	boolean userSelect = false;
	
	public String nlt;
	
	public String s2w;
	public String s2wnew;
	public boolean s2wmethod;
	public String s2wButtons;
	public String s2wStart;
	public String s2wEnd;
	public String s2wStartnew;
	public String s2wEndnew;
	private LinearLayout sdcacheLayout;
	private LinearLayout ioSchedulerLayout;
	private ImageView ioDivider;
	private RadioGroup cdRadio;
	private RadioButton rb16;
	private RadioButton rb24;
	private RadioButton rb32;
	private ImageView cdHeadImage;
	private TextView cdHead;
	private ImageView fchargeHeadImage;
	private TextView fchargeHead;
	private LinearLayout fchargeLayout;
	private Switch fchargeSwitch;
	
	private ImageView vsyncHeadImage;
	private TextView vsyncHead;
	private LinearLayout vsyncLayout;
	private Switch vsyncSwitch;
	
	private ImageView nltHeadImage;
	private TextView nltHead;
	private LinearLayout nltLayout;
	
	private LinearLayout s2wLayout;
	
	private LinearLayout s2wLayoutStart;
	private LinearLayout s2wLayoutEnd;
	
	private ImageView s2wHeadImage;
	private ImageView s2wDivider1;
	private ImageView s2wDivider2;
	
	private TextView s2wHead;
	

	Handler mHandler = new Handler();

	

	private class ChangeColorDepth extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... args)
		{
			

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
				localDataOutputStream.writeBytes("echo " + args[0]
												 + " > /sys/kernel/debug/msm_fb/0/bpp\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing color depth");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
			
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return args[0];
		}

		@Override
		protected void onPostExecute(String result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cdepth", result);
			editor.commit();
			

		}
	}

	

	private class ChangeFastcharge extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n");
				localDataOutputStream.writeBytes("echo " + fc
												 + " > /sys/kernel/fast_charge/force_fast_charge\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing fc");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}

		

	}

	private class ChangeVsync extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
				localDataOutputStream.writeBytes("echo " + vs
												 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("echo " + hw
												 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("echo " + backbuf
												 + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing vs");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}


	}

	private class ChangeButtonsLight extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
				if(args[0].equals("e3d")){
				localDataOutputStream.writeBytes("echo " + ledprogress + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
				}
				else if(args[0].equals("hox")){
					localDataOutputStream.writeBytes("echo " + args[1] + " > /sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents\n");
				}
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing buttons backlight");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("led", String.valueOf(ledprogress));
			editor.commit();
		}

	}

	private class ChangeNotificationLedTimeout extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... args)
		{

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n");
				localDataOutputStream
					.writeBytes("echo "
								+ args[0]
								+ " > /sys/kernel/notification_leds/off_timer_multiplier\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing not led timeout");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
			
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("ldt", result);
			editor.commit();

		}

	}

	private class ChangeS2w extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{
			

			Process localProcess;

			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				if (s2wmethod == true)
				{
					localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake\n");
					localDataOutputStream.writeBytes("echo " + s2wnew + " > /sys/android_touch/sweep2wake\n");
					localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake_startbutton\n");
					localDataOutputStream.writeBytes("echo " + s2wStartnew + " > /sys/android_touch/sweep2wake_startbutton\n");
					localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake_endbutton\n");
					localDataOutputStream.writeBytes("echo " + s2wEndnew + " > /sys/android_touch/sweep2wake_endbutton\n");

				}
				else
				{
					localDataOutputStream.writeBytes("chmod 777 /sys/android_touch/sweep2wake/s2w_switch\n");
					localDataOutputStream.writeBytes("echo " + s2wnew + " > /sys/android_touch/sweep2wake/s2w_switch\n");

				}
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing s2w");
			}
			catch (IOException e1)
			{
			
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
			
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("s2w", s2wnew);
			editor.putString("s2wStart", s2wStartnew);
			editor.putString("s2wEnd", s2wEndnew);

			editor.commit();

		}

	}

	private class ChangeIO extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{
			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
				localDataOutputStream.writeBytes("echo " + sdcache
												 + " > /sys/block/mmcblk1/queue/read_ahead_kb\n");
				localDataOutputStream.writeBytes("echo " + sdcache
												 + " > /sys/block/mmcblk0/queue/read_ahead_kb\n");
				localDataOutputStream.writeBytes("echo " + sdcache
												 + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/block/mmcblk0/queue/scheduler\n");
				localDataOutputStream
					.writeBytes("chmod 777 /sys/block/mmcblk1/queue/scheduler\n");
				localDataOutputStream.writeBytes("echo " + scheduler
												 + " > /sys/block/mmcblk0/queue/scheduler\n");
				localDataOutputStream.writeBytes("echo " + scheduler
												 + " > /sys/block/mmcblk1/queue/scheduler\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("MiscTweaks: Changing io");
			}
			catch (IOException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}
			catch (InterruptedException e1)
			{
				
				new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("io", scheduler);
			editor.putString("sdcache", String.valueOf(sdcache));
			editor.commit();

		}

	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.misc_tweaks);
		
		sdcacheLayout = (LinearLayout)findViewById(R.id.sdcache_layout);
		ioSchedulerLayout = (LinearLayout)findViewById(R.id.io_scheduler_layout);
		ioDivider = (ImageView)findViewById(R.id.io_divider);
		
		
		cdRadio = (RadioGroup) findViewById(R.id.cdGroup);
		rb16 = (RadioButton) findViewById(R.id.rb16);
		rb24 = (RadioButton) findViewById(R.id.rb24);
		rb32 = (RadioButton) findViewById(R.id.rb32);
		
		cdHeadImage = (ImageView)findViewById(R.id.cd_head_image);
		cdHead = (TextView)findViewById(R.id.cd_head);
		
		fchargeLayout = (LinearLayout)findViewById(R.id.fcharge_layout);
		fchargeHead = (TextView)findViewById(R.id.fastcharge_head);
		fchargeHeadImage = (ImageView)findViewById(R.id.fastcharge_head_image);
		fchargeSwitch = (Switch) findViewById(R.id.fcharge_switch);
		
		vsyncLayout = (LinearLayout)findViewById(R.id.vsync_layout);
		vsyncHead = (TextView)findViewById(R.id.vsync_head);
		vsyncHeadImage = (ImageView)findViewById(R.id.vsync_head_image);
		vsyncSwitch = (Switch) findViewById(R.id.vsync_switch);
		
		nltLayout = (LinearLayout)findViewById(R.id.nlt_layout);
		nltHead = (TextView)findViewById(R.id.nlt_head);
		nltHeadImage = (ImageView)findViewById(R.id.nlt_head_image);
		
		s2wLayout = (LinearLayout)findViewById(R.id.s2w_layout);
		s2wLayoutStart = (LinearLayout)findViewById(R.id.s2w_layout_start);
		s2wLayoutEnd = (LinearLayout)findViewById(R.id.s2w_layout_end);
		
		s2wHeadImage = (ImageView)findViewById(R.id.s2w_head_image);
		s2wDivider1 = (ImageView)findViewById(R.id.s2w_divider1);
		s2wDivider2 = (ImageView)findViewById(R.id.s2w_divider2);
		
		s2wHead = (TextView)findViewById(R.id.s2w_head);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
			{

				ledprogress = progress;
				TextView perc = (TextView) findViewById(R.id.progtextView1);
				perc.setText(ledprogress * 100 / 60 + "%");


			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{

			}
			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{

				ledprogress = mSeekBar.getProgress();

				new ChangeButtonsLight().execute(new String[] {"e3d"});
			}
		});

		

		

		ImageView btminus = (ImageView) findViewById(R.id.ImageView1);
		btminus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{

					mSeekBar.setProgress(mSeekBar.getProgress() - 3);
					new ChangeButtonsLight().execute(new String[] {"e3d"});

				}
			});

		ImageView btplus = (ImageView) findViewById(R.id.ImageView2);
		btplus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{

					mSeekBar.setProgress(mSeekBar.getProgress() + 3);
					new ChangeButtonsLight().execute(new String[] {"e3d"});

				}
			});

		

		final Switch fastchargechbx = (Switch) findViewById(R.id.fcharge_switch);
		fastchargechbx.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{

					if (fastchargechbx.isChecked())
					{
						fc = "1";
						new ChangeFastcharge().execute();
					}
					else if (!fastchargechbx.isChecked())
					{
						fc = "0";
						new ChangeFastcharge().execute();
					}
					try
					{
						preferences = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("fastcharge", fc);
						editor.commit();
					}
					catch (Exception e)
					{
						
					}

				}
			});

		final Switch vsynchbx = (Switch) findViewById(R.id.vsync_switch);
		vsynchbx.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{

					if (vsynchbx.isChecked())
					{
						vs = "1";
						hw = "1";
						backbuf = "3";
						new ChangeVsync().execute();
					}
					else if (!vsynchbx.isChecked())
					{
						vs = "0";
						hw = "0";
						backbuf = "4";
						new ChangeVsync().execute();
					}
					else
					{

					}
					preferences = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("vsync", vs);
					editor.putString("hw", hw);
					editor.putString("backbuf", backbuf);
					editor.commit();

				}
			});

		

			
		readButtons2();
		
		
		if (schedulers.isEmpty())
		{
			ioSchedulerLayout.setVisibility(View.GONE);
			ioDivider.setVisibility(View.GONE);
		}
		else{
			createSpinnerIO();
		}
		
		TextView backlightHead = (TextView) findViewById(R.id.backlight_head);
		TextView sb1 = (TextView) findViewById(R.id.progtextView1);
		ImageView im = (ImageView) findViewById(R.id.backlight_head_image);
		RadioGroup buttonsGroup = (RadioGroup)findViewById(R.id.buttonsGroup);
		RadioButton off = (RadioButton)findViewById(R.id.off);
		RadioButton dim = (RadioButton)findViewById(R.id.dim);
		RadioButton bright = (RadioButton)findViewById(R.id.bright);
		if(led.equals("")){
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);
		}
		else{
			mSeekBar.setProgress(Integer.parseInt(led));
		}
		if(new File(CPUInfo.BUTTONS_LIGHT_2).exists()){
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);
			
		}
		else if(new File(CPUInfo.BUTTONS_LIGHT).exists()){
			buttonsGroup.setVisibility(View.GONE);
		}
		else{
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);
			buttonsGroup.setVisibility(View.GONE);
		}
		if(ledHox.equals("0"))
		{
			off.setChecked(true);
		}
		else if(ledHox.equals("1")){
			dim.setChecked(true);
		}
		else if(ledHox.equals("2")){
			bright.setChecked(true);
		}
		off.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] {"hox", "0"});
				
			}
			
		});
		dim.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] {"hox", "1"});
				
			}
			
		});
		bright.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] {"hox", "2"});
				
			}
			
		});
		
		setCheckBoxes();
		
		
			
		


	}
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	protected void onResume()
	{

		super.onResume();

	}
	@Override
	protected void onStop()
	{

		super.onStop();

	}

	public void setCheckBoxes()
	{

		
		if(!CPUInfo.fchargeExists()){
			fchargeHead.setVisibility(View.GONE);
			fchargeHeadImage.setVisibility(View.GONE);
			fchargeLayout.setVisibility(View.GONE);
		}
		if (fastcharge==0)
		{
			fchargeSwitch.setChecked(false);
		}
		else if (fastcharge==1)
		{
			fchargeSwitch.setChecked(true);
		}
		

		if(!CPUInfo.vsyncExists()){
			vsyncHead.setVisibility(View.GONE);
			vsyncHeadImage.setVisibility(View.GONE);
			vsyncLayout.setVisibility(View.GONE);
		}

		if (vsync==1)
		{
			vsyncSwitch.setChecked(true);
		}
		else if (vsync==0)
		{
			vsyncSwitch.setChecked(false);
		}
		
		if (sdcache!=null)
		{
			EditText sd = (EditText) findViewById(R.id.editText1);
			sd.setText(String.valueOf(sdcache));
		}
		if(!CPUInfo.sdcacheExists()){
			sdcacheLayout.setVisibility(View.GONE);
			ioDivider.setVisibility(View.GONE);
		}
		

		
		rb16.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					new ChangeColorDepth().execute(new String[] {"16"});
				}

			});

		rb24.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					new ChangeColorDepth().execute(new String[] {"24"});
				}

			});

		rb32.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					new ChangeColorDepth().execute(new String[] {"32"});
				}

			});
		if(CPUInfo.cdExists()){
			if(cdepth.equals("16")){
				rb16.setChecked(true);
			}
			if(cdepth.equals("24")){
				rb24.setChecked(true);
			}
			if(cdepth.equals("32")){
				rb32.setChecked(true);
			}
		}
		else{
			cdHeadImage.setVisibility(View.GONE);
			cdHead.setVisibility(View.GONE);
			cdRadio.setVisibility(View.GONE);
		}
		if(new File("/sys/kernel/notification_leds/off_timer_multiplier").exists()){
			readNLT();
			createNLT();
		}
		else{
			nltHead.setVisibility(View.GONE);
			nltHeadImage.setVisibility(View.GONE);
			nltLayout.setVisibility(View.GONE);
		}
		
		if(new File("/sys/android_touch/sweep2wake").exists() || new File("/sys/android_touch/sweep2wake/s2w_switch").exists()){
			
			readS2W();
			createSpinnerS2W();
			
		}
		else{
			s2wHead.setVisibility(View.GONE);
			s2wHeadImage.setVisibility(View.GONE);
			s2wLayout.setVisibility(View.GONE);
		}
		if(new File("/sys/android_touch/sweep2wake_buttons").exists()){
			createSpinnerS2WEnd();
			createSpinnerS2WStart();
		}
		else{
			s2wDivider1.setVisibility(View.GONE);
			s2wDivider2.setVisibility(View.GONE);
			s2wLayoutStart.setVisibility(View.GONE);
			s2wLayoutEnd.setVisibility(View.GONE);
		}

	}

	

	public void readS2W()
	{
		try
		{

			File myFile = new File(
				"/sys/android_touch/sweep2wake");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			s2w = aBuffer.trim();
			s2wmethod = true;
			myReader.close();


		}
		catch (Exception e)
		{

			try
			{

				File myFile = new File(
					"/sys/android_touch/sweep2wake/s2w_switch");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(new InputStreamReader(
																 fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				s2w = aBuffer.trim();
				s2wmethod = false;
				myReader.close();

			}
			catch (Exception e2)
			{

				s2w = "err";
			}
		}

		try
		{
			File myFile = new File(
				"/sys/android_touch/sweep2wake_buttons");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			s2wButtons = aBuffer.trim();

			myReader.close();
		}
		catch (IOException e)
		{

		}

		try
		{
			File myFile = new File(
				"/sys/android_touch/sweep2wake_startbutton");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			s2wStart = aBuffer.trim();

			myReader.close();
		}
		catch (IOException e)
		{
			s2wStart = "err";
		}

		try
		{
			File myFile = new File(
				"/sys/android_touch/sweep2wake_endbutton");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			s2wEnd = aBuffer.trim();

			myReader.close();
		}
		catch (IOException e)
		{
			s2wEnd = "err";
		}
	}

	public void readNLT()
	{
		try
		{

			File myFile = new File(
				"/sys/kernel/notification_leds/off_timer_multiplier");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			nlt = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

			nlt = "266";
		}
	}

	public void readButtons2()
	{
		try
		{

			File myFile = new File(
				"/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			ledHox = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

			ledHox = "266";
		}
	}
	
	

	public void createSpinnerIO()
	{
		

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item, schedulers);
		spinnerArrayAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id)
				{
					scheduler = parent.getItemAtPosition(pos).toString();
					
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					// do nothing
				}
			});

	

		int spinnerPosition = spinnerArrayAdapter .getPosition(scheduler);
		spinner.setSelection(spinnerPosition);

	}

	public void createSpinnerS2W()
	{
		String[] MyStringAray = {"OFF","ON with no backlight","ON with backlight"};

		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id)
				{
					s2wnew = String.valueOf(pos);
					if (s2w == "err")
					{
						Spinner spinner = (Spinner) findViewById(R.id.spinner2);
						TextView s2wtxt = (TextView) findViewById(R.id.textView13);
						spinner.setVisibility(View.GONE);
						s2wtxt.setVisibility(View.GONE);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					// do nothing
				}
			});

		
		if (s2w.equals("0"))
		{
			int spinnerPosition = spinnerArrayAdapter .getPosition("OFF");
			spinner.setSelection(spinnerPosition);
		}
		else if (s2w.equals("1"))
		{
			int spinnerPosition = spinnerArrayAdapter .getPosition("ON with no backlight");
			spinner.setSelection(spinnerPosition);
		}
		else if (s2w.equals("2"))
		{
			int spinnerPosition = spinnerArrayAdapter .getPosition("ON with backlight");
			spinner.setSelection(spinnerPosition);
		}

	}

	public void createSpinnerS2WStart()
	{
		String[] MyStringAray = s2wButtons.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id)
				{
					s2wStartnew = parent.getItemAtPosition(pos).toString();
					if (s2wStart == "err")
					{
						Spinner spinner = (Spinner) findViewById(R.id.spinner3);
						TextView s2wtxt = (TextView) findViewById(R.id.textView14);
						spinner.setVisibility(View.GONE);
						s2wtxt.setVisibility(View.GONE);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
		
				}
			});

	
		int spinnerPosition = spinnerArrayAdapter .getPosition(s2wStart);
		spinner.setSelection(spinnerPosition);


	}

	public void createSpinnerS2WEnd()
	{
		String[] MyStringAray = s2wButtons.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner4);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id)
				{
					s2wEndnew = parent.getItemAtPosition(pos).toString();
					if (s2wEnd == "err")
					{
						Spinner spinner = (Spinner) findViewById(R.id.spinner4);
						TextView s2wtxt = (TextView) findViewById(R.id.textView15);
						spinner.setVisibility(View.GONE);
						s2wtxt.setVisibility(View.GONE);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					// do nothing
				}
			});


		int spinnerPosition = spinnerArrayAdapter.getPosition(s2wEnd);
		spinner.setSelection(spinnerPosition);


	}

	

	public void createNLT()
	{
		String[] MyStringAray = {"Never", "App Default", "Custom"};

		final Spinner spinner = (Spinner) findViewById(R.id.spinner_nlt);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
			
		spinner.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
			userSelect=true;
				return false;
			}
			
		});
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id)
				{
					if(pos<2){
						new ChangeNotificationLedTimeout().execute(new String[] {String.valueOf(pos)});
						}
						else{
							if(userSelect){
							AlertDialog.Builder builder = new AlertDialog.Builder(MiscTweaks.this);

							builder.setTitle("Notification LED Timeout");

							builder.setMessage("Set custom multiplier");

							builder.setIcon(R.drawable.ic_menu_cc);

							final EditText input = new EditText(MiscTweaks.this);
							
							input.setGravity(Gravity.CENTER_HORIZONTAL);
							input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
							
							builder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										new ChangeNotificationLedTimeout().execute(new String[] {input.getText().toString()});
										
									}
								});
							builder.setView(input);

							AlertDialog alert = builder.create();

							alert.show();
							}
						}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					// do nothing
				}
			});


		if (nlt.equals("Infinite"))
		{
			spinner.setSelection(0);
			userSelect=false;
		}
		else if (nlt.equals("As requested by process"))
		{
			spinner.setSelection(1);
			userSelect=false;
		}
		else
		{
			spinner.setSelection(2);
			userSelect=false;
		}
		


	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.misc_tweaks_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
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
	        case R.id.apply:
	        	apply();
	        	return true;
	        case R.id.cancel:
	        	finish();
	        	return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	public void apply(){
		EditText sd = (EditText) findViewById(R.id.editText1);
		sdcache = Integer.parseInt(sd.getText().toString());
		new ChangeIO().execute();
		new ChangeS2w().execute();
		finish();
	}

}
