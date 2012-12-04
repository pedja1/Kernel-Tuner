package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MiscTweaks extends SherlockActivity implements
SeekBar.OnSeekBarChangeListener
{

	public String iscVa = "";
	public String iscVa2 = "offline";
	public String governors;
	public String governorscpu1;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
	public String led = CPUInfo.leds();
	public String ledHox;
	SeekBar mSeekBar;
	TextView progresstext;
	public String cpu0freqs;
	public String cpu1freqs;
	public String cpu0max;
	public String cpu1max;
	public int countcpu0;
	public int countcpu1;
	public String fastcharge = " ";
	public String vsync = " ";
	public String fc = " ";
	public String vs;
	public String hw;
	public String backbuf;
	public String cdepth = " ";
	public String sdcache;
	public String schedulers;
	public String scheduler;
	public int ledprogress;
	public SharedPreferences preferences;
	ProgressBar prog;
	
	public String ldt;
	public String ldtnew;
	public String s2w;
	public String s2wnew;
	public boolean s2wmethod;
	public String s2wButtons;
	public String s2wStart;
	public String s2wEnd;
	public String s2wStartnew;
	public String s2wEndnew;

	Handler mHandler = new Handler();

	// EndOfGlobalVariables

	private class ChangeColorDepth extends AsyncTask<String, Void, Object>
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
					.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
				localDataOutputStream.writeBytes("echo " + cdepth
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

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cdepth", cdepth);
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

	private class ChangeNotificationLedTimeout extends AsyncTask<String, Void, Object>
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
					.writeBytes("chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n");
				localDataOutputStream
					.writeBytes("echo "
								+ ldtnew
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
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("ldt", String.valueOf(ldtnew));
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
			editor.putString("sdcache", sdcache);
			editor.commit();

		}

	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.misc_tweaks);
		

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(this);

		Button apply = (Button)findViewById(R.id.apply);
		apply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					EditText sd = (EditText) findViewById(R.id.editText1);
					sdcache = String.valueOf(sd.getText());
					new ChangeIO().execute();

					EditText ldttv = (EditText) findViewById(R.id.editText2);
					RadioButton dva = (RadioButton) findViewById(R.id.radio2);
					if (dva.isChecked())
					{
						ldtnew = String.valueOf(ldttv.getText());
					}
					new ChangeNotificationLedTimeout().execute();
					new ChangeS2w().execute();
					finish();

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

		ImageView btminuscdepth = (ImageView) findViewById(R.id.button3);
		btminuscdepth.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					prog = (ProgressBar) findViewById(R.id.progressBar1);
					if (prog.getProgress() == 2)
					{
						cdepth = "24";
					}
					else if (prog.getProgress() == 1)
					{
						cdepth = "16";
					}

					prog.setProgress(prog.getProgress() - 1);
					new ChangeColorDepth().execute();

				}
			});

		ImageView btpluscdepth = (ImageView) findViewById(R.id.button7);
		btpluscdepth.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{

					prog = (ProgressBar) findViewById(R.id.progressBar1);
					if (prog.getProgress() == 0)
					{
						cdepth = "24";
					}
					else if (prog.getProgress() == 1)
					{
						cdepth = "32";
					}
					prog.setProgress(prog.getProgress() + 1);
					new ChangeColorDepth().execute();

				}
			});

		final CheckBox fastchargechbx = (CheckBox) findViewById(R.id.checkBox1);
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

		final CheckBox vsynchbx = (CheckBox) findViewById(R.id.checkBox2);
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
		readSDCache();
		readIOScheduler();
		createSpinnerIO();
		TextView sb = (TextView) findViewById(R.id.textView9);
		TextView sb1 = (TextView) findViewById(R.id.progtextView1);
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		RadioGroup buttonsGroup = (RadioGroup)findViewById(R.id.buttonsGroup);
		RadioButton off = (RadioButton)findViewById(R.id.off);
		RadioButton dim = (RadioButton)findViewById(R.id.dim);
		RadioButton bright = (RadioButton)findViewById(R.id.bright);
		if(led.equals("")){
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			sb.setVisibility(View.GONE);
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
			sb.setVisibility(View.GONE);
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
			sb.setVisibility(View.GONE);
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
		readLDT();
		readFastchargeStatus();
		readVsyncStatus();
		setCheckBoxes();

		readColorDepth();
		setColorDepth();
		readS2W();
		createSpinnerS2W();
		if (new File("/sys/android_touch/sweep2wake_buttons").exists())
		{
			createSpinnerS2WEnd();
			createSpinnerS2WStart();
		}
		else
		{
			TextView tv = (TextView) findViewById(R.id.textView14);
			TextView tv2 = (TextView) findViewById(R.id.textView15);
			Spinner sp = (Spinner)findViewById(R.id.spinner3);
			Spinner sp2 = (Spinner)findViewById(R.id.spinner4);
			tv.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			sp.setVisibility(View.GONE);
			sp2.setVisibility(View.GONE);

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

		super.onResume();

	}
	@Override
	protected void onStop()
	{

		super.onStop();

	}

	public void setCheckBoxes()
	{

		CheckBox fc = (CheckBox) findViewById(R.id.checkBox1);
		TextView tv = (TextView) findViewById(R.id.textView1);
		if (fastcharge.equals("0"))
		{
			fc.setChecked(false);
		}
		else if (fastcharge.equals("1"))
		{
			fc.setChecked(true);
		}
		else
		{
			fc.setVisibility(View.GONE);
			
			tv.setVisibility(View.GONE);
			ImageView im = (ImageView) findViewById(R.id.imageView2);
			im.setVisibility(View.GONE);
		}

		CheckBox vs = (CheckBox) findViewById(R.id.checkBox2);
		
		TextView tv2 = (TextView) findViewById(R.id.textView2);

		if (vsync.equals("1"))
		{
			vs.setChecked(true);
		}
		else if (vsync.equals("0"))
		{
			vs.setChecked(false);
		}
		else
		{
			vs.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			ImageView im = (ImageView) findViewById(R.id.imageView3);
			im.setVisibility(View.GONE);
		}
		if (!sdcache.equals("err"))
		{
			EditText sd = (EditText) findViewById(R.id.editText1);
			sd.setText(sdcache);
		}
		else
		{
			EditText sd = (EditText) findViewById(R.id.editText1);
			TextView sdtxt = (TextView) findViewById(R.id.textView11);
			sd.setVisibility(View.GONE);
			sdtxt.setVisibility(View.GONE);
		}

		RadioGroup ldtradio = (RadioGroup) findViewById(R.id.radioGroup1);
		final EditText et = (EditText) findViewById(R.id.editText2);
		TextView ldttitle = (TextView) findViewById(R.id.textView12);
		RadioButton nula = (RadioButton) findViewById(R.id.radio0);
		RadioButton jedan = (RadioButton) findViewById(R.id.radio1);
		RadioButton dva = (RadioButton) findViewById(R.id.radio2);
		nula.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					et.setVisibility(View.GONE);
					ldtnew = "0";
				}

			});

		jedan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					et.setVisibility(View.GONE);
					ldtnew = "1";
				}

			});

		dva.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					et.setVisibility(View.VISIBLE);
				}

			});
		File file = new File(
			"/sys/kernel/notification_leds/off_timer_multiplier");

		try
		{

			InputStream fIn = new FileInputStream(file);

			if (ldt.equals("Infinite"))
			{
				nula.setChecked(true);
			}
			else if (ldt.equals("As requested by process"))
			{
				jedan.setChecked(true);
			}
			else
			{
				dva.setChecked(true);
				et.setText(ldt);
			}
			if (dva.isChecked())
			{
				et.setVisibility(View.VISIBLE);
			}
			else
			{
				et.setVisibility(View.GONE);
			}
			fIn.close();

		}
		catch (FileNotFoundException e)
		{
			ldtradio.setVisibility(View.GONE);
			ldttitle.setVisibility(View.GONE);
			et.setVisibility(View.GONE);

		} catch (IOException e) {
			
		}

	}

	public void setColorDepth()
	{
		prog = (ProgressBar) findViewById(R.id.progressBar1);
		if (cdepth.equals("16"))
		{
			prog.setProgress(0);
		}
		else if (cdepth.equals("24"))
		{
			prog.setProgress(1);
		}
		else if (cdepth.equals("32"))
		{
			prog.setProgress(2);
		}
		if(new File("/sys/kernel/debug/msm_fb/0/bpp").exists()){
			
		}
		else
		{
			ImageView btpluscdepth = (ImageView) findViewById(R.id.button7);
			ImageView btminuscdepth = (ImageView) findViewById(R.id.button3);
			TextView tv = (TextView) findViewById(R.id.textView5);
			TextView tv2 = (TextView) findViewById(R.id.textView6);
			TextView tv3 = (TextView) findViewById(R.id.textView7);
			TextView tv4 = (TextView) findViewById(R.id.textView8);
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
			ImageView im = (ImageView) findViewById(R.id.imageView4);
			btpluscdepth.setVisibility(View.GONE);
			pb.setVisibility(View.GONE);
			btminuscdepth.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			tv4.setVisibility(View.GONE);

			tv.setVisibility(View.GONE);
			im.setVisibility(View.GONE);

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
				Spinner spinner = (Spinner) findViewById(R.id.spinner3);
				TextView s2wtxt = (TextView) findViewById(R.id.textView14);
				spinner.setVisibility(View.GONE);
				s2wtxt.setVisibility(View.GONE);
				Spinner spinner2 = (Spinner) findViewById(R.id.spinner4);
				TextView s2wtxt2 = (TextView) findViewById(R.id.textView15);
				ImageView img = (ImageView) findViewById(R.id.imageView6);
				img.setVisibility(View.GONE);
				spinner2.setVisibility(View.GONE);
				s2wtxt2.setVisibility(View.GONE);
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

	public void readLDT()
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

			ldt = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

			ldt = "266";
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
	
	public void readColorDepth()
	{
		try
		{

			File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			cdepth = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

		}
	}

	public void readSDCache()
	{
		try
		{

			File myFile = new File(
				"/sys/devices/virtual/bdi/179:0/read_ahead_kb");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			sdcache = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{
			sdcache = "err";

		}
	}

	public void createSpinnerIO()
	{
		String[] MyStringAray = schedulers.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
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
					scheduler = parent.getItemAtPosition(pos).toString();
					if (scheduler == "err")
					{
						Spinner spinner = (Spinner) findViewById(R.id.spinner1);
						TextView iotxt = (TextView) findViewById(R.id.textView10);
						spinner.setVisibility(View.GONE);
						iotxt.setVisibility(View.GONE);
					}
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

	public void readIOScheduler()
	{
		try
		{

			File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			schedulers = aBuffer;
			myReader.close();
			scheduler = schedulers.substring(schedulers.indexOf("[") + 1,
											 schedulers.indexOf("]"));
			scheduler.trim();
			schedulers = schedulers.replace("[", "");
			schedulers = schedulers.replace("]", "");

		}
		catch (Exception e)
		{
			schedulers = "err";
			scheduler = "err";
		}

	}

	public void readFastchargeStatus()
	{
		try
		{
			String aBuffer = "";
			File myFile = new File("/sys/kernel/fast_charge/force_fast_charge");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			fastcharge = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

		}

	}

	public void readVsyncStatus()
	{
		try
		{
			String aBuffer = "";
			File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			vsync = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{

		}

	}

	
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

}
