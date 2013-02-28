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
import android.content.*;
import android.os.*;
import android.preference.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.*;
import android.widget.CompoundButton.*;
import android.widget.SeekBar.*;
import com.actionbarsherlock.app.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import de.ankri.views.Switch;

import java.lang.Process;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.tools.RootExecuter;
import org.apache.commons.io.FileUtils;

public class MiscTweaks extends SherlockActivity {

	private String led;
	private String ledHox;
	private SeekBar mSeekBar;

	private String fc = " ";
	private int fastcharge;
	private int vsync;
	private String vs;
	private String hw;
	private String backbuf;
	private String cdepth;
	private Integer sdcache ;
	private List<String> schedulers ;
	private String scheduler;
	private int ledprogress;
	private SharedPreferences preferences;
	private boolean userSelect = false;

	private String nlt;

	private String s2w;
	private String s2wnew;
	private boolean s2wmethod;
	private String s2wButtons;
	private String s2wStart;
	private String s2wEnd;
	private String s2wStartnew;
	private String s2wEndnew;
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

	private ImageView otgHeadImage;
	private TextView otgHead;
	private LinearLayout otgLayout;
	private Switch otgSwitch;
	private ProgressDialog pd;
	private String otg;
    
	
	private class LoadInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			otg = IOHelper.readOTG();
			cdepth = IOHelper.cDepth();
			sdcache = IOHelper.sdCache();
			schedulers = IOHelper.schedulers();
			scheduler = IOHelper.scheduler();
			led = IOHelper.leds();
			fastcharge = IOHelper.fcharge();
			vsync = IOHelper.vsync();
			ledHox = readFile("/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents");
			return "";
		}

		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(MiscTweaks.this);
			pd.setMessage("Wait a sec...");
			pd.setIndeterminate(true);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}
		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			setUI();

		}
	}
	
	private class ChangeColorDepth extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {

		RootExecuter.exec(new String[]{
				"chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n",
				"echo " + args[0] + " > /sys/kernel/debug/msm_fb/0/bpp\n"});
			return args[0];
		}

		@Override
		protected void onPostExecute(String result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cdepth", result);
			editor.commit();

		}
	}

	private class ChangeOTG extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {

		     RootExecuter.exec(new String[]{
				"chmod 777 /sys/kernel/debug/msm_otg/mode\n",
				"chmod 777 /sys/kernel/debug/otg/mode\n",
				"echo " + args[0] + " > /sys/kernel/debug/otg/mode\n",
				"echo " + args[0] + " > /sys/kernel/debug/msm_otg/mode\n"});
			return args[0];
		}

		@Override
		protected void onPostExecute(String result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("otg_mode", result);
			editor.commit();

		}
	}

	private class ChangeFastcharge extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {

		    RootExecuter.exec(new String[]{
				"chmod 777 /sys/kernel/fast_charge/force_fast_charge\n",
				"echo " + fc + " > /sys/kernel/fast_charge/force_fast_charge\n"});
			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("fastcharge", fc);
			editor.commit();
		}

	}

	private class ChangeVsync extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
			
			RootExecuter.exec(new String[]{
				"chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n",
			    "chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n",
				"chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n",
				"echo " + vs + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n",
				"echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n",
				"echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff\n"});
						
			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vsync", vs);
			editor.putString("hw", hw);
			editor.putString("backbuf", backbuf);
			editor.commit();
		}

	}

	private class ChangeButtonsLight extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {

			String[] temp = new String[3];
				temp[0] = "chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n";
				temp[1] = "chmod 777 /sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents\n";
				if (args[0].equals("e3d")) {
					temp[2] = "echo " + ledprogress + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n";
				} else if (args[0].equals("hox")) {
					temp[2] = "echo " + args[1] + " > /sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents\n";
				}

			RootExecuter.exec(temp);
			

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("led", ledprogress + "");
			editor.commit();
		}

	}

	private class ChangeNotificationLedTimeout extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {

			RootExecuter.exec(new String[]{
				"chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n",
				"echo " + args[0] + " > /sys/kernel/notification_leds/off_timer_multiplier\n"});
			return args[0];
		}

		@Override
		protected void onPostExecute(String result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("ldt", result);
			editor.commit();

		}

	}

	private class ChangeS2w extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
			try {
				String line;
				Process process = Runtime.getRuntime().exec("su");
				OutputStream stdin = process.getOutputStream();
				InputStream stderr = process.getErrorStream();
				InputStream stdout = process.getInputStream();

				if (s2wmethod == true) {
					stdin.write(("chmod 777 /sys/android_touch/sweep2wake\n")
							.getBytes());
					stdin.write(("echo " + s2wnew + " > /sys/android_touch/sweep2wake\n")
							.getBytes());
					stdin.write(("chmod 777 /sys/android_touch/sweep2wake_startbutton\n")
							.getBytes());
					stdin.write(("echo " + s2wStartnew + " > /sys/android_touch/sweep2wake_startbutton\n")
							.getBytes());
					stdin.write(("chmod 777 /sys/android_touch/sweep2wake_endbutton\n")
							.getBytes());
					stdin.write(("echo " + s2wEndnew + " > /sys/android_touch/sweep2wake_endbutton\n")
							.getBytes());

				} else {
					stdin.write(("chmod 777 /sys/android_touch/sweep2wake/s2w_switch\n")
							.getBytes());
					stdin.write(("echo " + s2wnew + " > /sys/android_touch/sweep2wake/s2w_switch\n")
							.getBytes());

				}
				stdin.flush();

				stdin.close();
				BufferedReader brCleanUp = new BufferedReader(
						new InputStreamReader(stdout));
				while ((line = brCleanUp.readLine()) != null) {
					Log.d("[KernelTuner MiscTweaks Output]", line);
				}
				brCleanUp.close();
				brCleanUp = new BufferedReader(new InputStreamReader(stderr));
				while ((line = brCleanUp.readLine()) != null) {
					Log.e("[KernelTuner MiscTweaks Error]", line);
				}
				brCleanUp.close();

			} catch (IOException ex) {
				Log.d("[KernelTuner MiscTweaks Error]", "" + ex.getMessage());

			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("s2w", s2wnew);
			editor.putString("s2wStart", s2wStartnew);
			editor.putString("s2wEnd", s2wEndnew);

			editor.commit();

		}

	}

	private class ChangeIO extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {

			RootExecuter.exec(new String[]{
				"chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n",
				"chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n",
				"chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n",
				"echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb\n",
				"echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb\n",
				"echo " + sdcache + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n",
				"chmod 777 /sys/block/mmcblk0/queue/scheduler\n",
				"chmod 777 /sys/block/mmcblk1/queue/scheduler\n",
			    "echo " + scheduler + " > /sys/block/mmcblk0/queue/scheduler\n",
				"echo " + scheduler + " > /sys/block/mmcblk1/queue/scheduler\n"});

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("io", scheduler);
			editor.putString("sdcache", sdcache + "");
			editor.commit();

		}

	}

	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		String theme = preferences.getString("theme", "light");

		if (theme.equals("light")) {
			setTheme(R.style.SwitchCompatAndSherlockLight);
		} else if (theme.equals("dark")) {
			setTheme(R.style.SwitchCompatAndSherlock);

		} else if (theme.equals("light_dark_action_bar")) {
			setTheme(R.style.SwitchCompatAndSherlockLightDark);

		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misc_tweaks);

		new LoadInfo().execute();

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		sdcacheLayout = (LinearLayout) findViewById(R.id.sdcache_layout);
		ioSchedulerLayout = (LinearLayout) findViewById(R.id.io_scheduler_layout);
		ioDivider = (ImageView) findViewById(R.id.io_divider);

		cdRadio = (RadioGroup) findViewById(R.id.cdGroup);
		rb16 = (RadioButton) findViewById(R.id.rb16);
		rb24 = (RadioButton) findViewById(R.id.rb24);
		rb32 = (RadioButton) findViewById(R.id.rb32);

		cdHeadImage = (ImageView) findViewById(R.id.cd_head_image);
		cdHead = (TextView) findViewById(R.id.cd_head);

		fchargeLayout = (LinearLayout) findViewById(R.id.fcharge_layout);
		fchargeHead = (TextView) findViewById(R.id.fastcharge_head);
		fchargeHeadImage = (ImageView) findViewById(R.id.fastcharge_head_image);
		fchargeSwitch = (Switch) findViewById(R.id.fcharge_switch);

		vsyncLayout = (LinearLayout) findViewById(R.id.vsync_layout);
		vsyncHead = (TextView) findViewById(R.id.vsync_head);
		vsyncHeadImage = (ImageView) findViewById(R.id.vsync_head_image);
		vsyncSwitch = (Switch) findViewById(R.id.vsync_switch);

		nltLayout = (LinearLayout) findViewById(R.id.nlt_layout);
		nltHead = (TextView) findViewById(R.id.nlt_head);
		nltHeadImage = (ImageView) findViewById(R.id.nlt_head_image);

		s2wLayout = (LinearLayout) findViewById(R.id.s2w_layout);
		s2wLayoutStart = (LinearLayout) findViewById(R.id.s2w_layout_start);
		s2wLayoutEnd = (LinearLayout) findViewById(R.id.s2w_layout_end);

		s2wHeadImage = (ImageView) findViewById(R.id.s2w_head_image);
		s2wDivider1 = (ImageView) findViewById(R.id.s2w_divider1);
		s2wDivider2 = (ImageView) findViewById(R.id.s2w_divider2);

		s2wHead = (TextView) findViewById(R.id.s2w_head);

		otgHeadImage = (ImageView) findViewById(R.id.otg_head_image);
		otgHead = (TextView) findViewById(R.id.otg_head);
		otgLayout = (LinearLayout) findViewById(R.id.otg_layout);
		otgSwitch = (Switch) findViewById(R.id.otg_switch);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true) {
			AdView adView = (AdView) findViewById(R.id.ad);
			adView.loadAd(new AdRequest());
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
	}

	private void setUI(){
		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {

				ledprogress = progress;
				TextView perc = (TextView) findViewById(R.id.progtextView1);
				perc.setText(ledprogress * 100 / 60 + "%");

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

				ledprogress = mSeekBar.getProgress();

				new ChangeButtonsLight().execute(new String[] { "e3d" });
			}
		});

		ImageView btminus = (ImageView) findViewById(R.id.ImageView1);
		btminus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mSeekBar.setProgress(mSeekBar.getProgress() - 3);
				new ChangeButtonsLight().execute(new String[] { "e3d" });

			}
		});

		ImageView btplus = (ImageView) findViewById(R.id.ImageView2);
		btplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mSeekBar.setProgress(mSeekBar.getProgress() + 3);
				new ChangeButtonsLight().execute(new String[] { "e3d" });

			}
		});

		final Switch fastchargechbx = (Switch) findViewById(R.id.fcharge_switch);
		fastchargechbx
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							fc = "1";
							new ChangeFastcharge().execute();
						} else {
							fc = "0";
							new ChangeFastcharge().execute();
						}

						preferences = PreferenceManager
								.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("fastcharge", fc);
						editor.commit();

					}
				});

		final Switch vsynchbx = (Switch) findViewById(R.id.vsync_switch);
		vsynchbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					vs = "1";
					hw = "1";
					backbuf = "3";
					new ChangeVsync().execute();
				} else {
					vs = "0";
					hw = "0";
					backbuf = "4";
					new ChangeVsync().execute();
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

		
		if (schedulers.isEmpty()) {
			ioSchedulerLayout.setVisibility(View.GONE);
			ioDivider.setVisibility(View.GONE);
		} else {
			createSpinnerIO();
		}

		TextView backlightHead = (TextView) findViewById(R.id.backlight_head);
		TextView sb1 = (TextView) findViewById(R.id.progtextView1);
		ImageView im = (ImageView) findViewById(R.id.backlight_head_image);
		RadioGroup buttonsGroup = (RadioGroup) findViewById(R.id.buttonsGroup);
		RadioButton off = (RadioButton) findViewById(R.id.off);
		RadioButton dim = (RadioButton) findViewById(R.id.dim);
		RadioButton bright = (RadioButton) findViewById(R.id.bright);
		if (led.equals("")) {
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);
		} else {
			mSeekBar.setProgress(Integer.parseInt(led));
		}
		if (new File(Constants.BUTTONS_LIGHT_2).exists()) {
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);

		} else if (new File(Constants.BUTTONS_LIGHT).exists()) {
			buttonsGroup.setVisibility(View.GONE);
		} else {
			mSeekBar.setVisibility(View.GONE);
			btminus.setVisibility(View.GONE);
			btplus.setVisibility(View.GONE);
			backlightHead.setVisibility(View.GONE);
			sb1.setVisibility(View.GONE);
			im.setVisibility(View.GONE);
			buttonsGroup.setVisibility(View.GONE);
		}
		if (ledHox.equals("0")) {
			off.setChecked(true);
		} else if (ledHox.equals("1")) {
			dim.setChecked(true);
		} else if (ledHox.equals("2")) {
			bright.setChecked(true);
		}
		off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] { "hox", "0" });

			}

		});
		dim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] { "hox", "1" });

			}

		});
		bright.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new ChangeButtonsLight().execute(new String[] { "hox", "2" });

			}

		});

		setCheckBoxes();
	}
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onStop() {

		super.onStop();

	}

	private final void setCheckBoxes()
	{

		
		if(!IOHelper.fchargeExists()){
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
		

		if(!IOHelper.vsyncExists()){
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
			sd.setText(sdcache+"");
		}
		if(!IOHelper.sdcacheExists()){
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
		if(IOHelper.cdExists()){
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
		if(new File(Constants.NLT).exists()){
			nlt = readFile(Constants.NLT);
			createNLT();
		}
		else{
			nltHead.setVisibility(View.GONE);
			nltHeadImage.setVisibility(View.GONE);
			nltLayout.setVisibility(View.GONE);
		}
		
		if(new File(Constants.S2W).exists()){
			
			
			s2w = readFile(Constants.S2W);
			s2wmethod = true;
			s2wEnd = readFile(Constants.S2W_END);
			s2wButtons = readFile(Constants.S2W_BUTTONS);
			s2wStart = readFile(Constants.S2W_START);
			createSpinnerS2W();
			
		}
		else if(new File(Constants.S2W_ALT).exists()){
			s2w = readFile(Constants.S2W_ALT);
			s2wmethod = false;
			s2wEnd = readFile(Constants.S2W_END);
			s2wButtons = readFile(Constants.S2W_BUTTONS);
			s2wStart = readFile(Constants.S2W_START);
			createSpinnerS2W();
		}
		else{
			s2wHead.setVisibility(View.GONE);
			s2wHeadImage.setVisibility(View.GONE);
			s2wLayout.setVisibility(View.GONE);
		}
		if(new File(Constants.S2W_BUTTONS).exists()){
			createSpinnerS2WEnd();
			createSpinnerS2WStart();
		}
		else{
			s2wDivider1.setVisibility(View.GONE);
			s2wDivider2.setVisibility(View.GONE);
			s2wLayoutStart.setVisibility(View.GONE);
			s2wLayoutEnd.setVisibility(View.GONE);
		}
		
		if(IOHelper.otgExists()){
			if(otg.equals("host")){
				otgSwitch.setChecked(true);
			}
			else{
				otgSwitch.setChecked(false);
			}
			otgSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if(isChecked){
						new ChangeOTG().execute(new String[] {"host"});
					}
					else{
						new ChangeOTG().execute(new String[] {"peripheral"});
					}
					
				}
				
			});
		}
		else{
			otgHead.setVisibility(View.GONE);
			otgHeadImage.setVisibility(View.GONE);
			otgLayout.setVisibility(View.GONE);
		}

	}

	

	private String readFile(String path) {
		try
		{
			return FileUtils.readFileToString(new File(path)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	private final void createSpinnerIO() {

		final Spinner spinner = (Spinner) findViewById(R.id.bg);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, schedulers);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				scheduler = parent.getItemAtPosition(pos).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		int spinnerPosition = spinnerArrayAdapter.getPosition(scheduler);
		spinner.setSelection(spinnerPosition);

	}

	private final void createSpinnerS2W() {
		String[] MyStringAray = { "OFF", "ON with no backlight",
				"ON with backlight" };

		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				s2wnew = pos + "";
				if (s2w.length()==0) {
					Spinner spinner = (Spinner) findViewById(R.id.spinner2);
					TextView s2wtxt = (TextView) findViewById(R.id.textView13);
					spinner.setVisibility(View.GONE);
					s2wtxt.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		if (s2w.equals("0")) {
			int spinnerPosition = spinnerArrayAdapter.getPosition("OFF");
			spinner.setSelection(spinnerPosition);
		} else if (s2w.equals("1")) {
			int spinnerPosition = spinnerArrayAdapter
					.getPosition("ON with no backlight");
			spinner.setSelection(spinnerPosition);
		} else if (s2w.equals("2")) {
			int spinnerPosition = spinnerArrayAdapter
					.getPosition("ON with backlight");
			spinner.setSelection(spinnerPosition);
		}

	}

	private final void createSpinnerS2WStart() {
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
					int pos, long id) {
				s2wStartnew = parent.getItemAtPosition(pos).toString();
				if (s2wStart.length()==0) {
					Spinner spinner = (Spinner) findViewById(R.id.spinner3);
					TextView s2wtxt = (TextView) findViewById(R.id.textView14);
					spinner.setVisibility(View.GONE);
					s2wtxt.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		int spinnerPosition = spinnerArrayAdapter.getPosition(s2wStart);
		spinner.setSelection(spinnerPosition);

	}

	private final void createSpinnerS2WEnd() {
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
					int pos, long id) {
				s2wEndnew = parent.getItemAtPosition(pos).toString();
				if (s2wEnd.length()==0) {
					Spinner spinner = (Spinner) findViewById(R.id.spinner4);
					TextView s2wtxt = (TextView) findViewById(R.id.textView15);
					spinner.setVisibility(View.GONE);
					s2wtxt.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		int spinnerPosition = spinnerArrayAdapter.getPosition(s2wEnd);
		spinner.setSelection(spinnerPosition);

	}

	private final void createNLT() {
		String[] MyStringAray = { "Never", "App Default", "Custom" };

		final Spinner spinner = (Spinner) findViewById(R.id.spinner_nlt);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				userSelect = true;
				return false;
			}

		});

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos < 2) {
					new ChangeNotificationLedTimeout()
							.execute(new String[] { pos + "" });
				} else {
					if (userSelect) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MiscTweaks.this);

						builder.setTitle("Notification LED Timeout");

						builder.setMessage("Set custom multiplier");

						builder.setIcon(R.drawable.ic_menu_cc);

						final EditText input = new EditText(MiscTweaks.this);

						input.setGravity(Gravity.CENTER_HORIZONTAL);
						input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

						builder.setPositiveButton(
								getResources().getString(R.string.done),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new ChangeNotificationLedTimeout()
												.execute(new String[] { input
														.getText().toString() });

									}
								});
						builder.setView(input);

						AlertDialog alert = builder.create();

						alert.show();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		if (nlt.equals("Infinite")) {
			spinner.setSelection(0);
			userSelect = false;
		} else if (nlt.equals("As requested by process")) {
			spinner.setSelection(1);
			userSelect = false;
		} else {
			spinner.setSelection(2);
			userSelect = false;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.misc_tweaks_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
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

	private final void apply() {
		EditText sd = (EditText) findViewById(R.id.editText1);
		sdcache = Integer.parseInt(sd.getText().toString());
		new ChangeIO().execute();
		new ChangeS2w().execute();
		finish();
	}

}
