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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.widget.VerticalSeekBar;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.tools.ChangeGovernor;
import rs.pedjaapps.KernelTuner.tools.FrequencyChanger;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import android.content.Context;




public class CPUActivity extends SherlockActivity
{

	private  List<IOHelper.FreqsEntry> freqEntries;
	private boolean thread = true;
	private  Handler mHandler;
	private TextView cpu0prog;
	private ProgressBar progCpu0;
	private TextView cpu1prog;
	private ProgressBar progCpu1;
	private TextView cpu2prog;
	private ProgressBar progCpu2;
	private TextView cpu3prog;
	private ProgressBar progCpu3;
	private  List<String> frequencies = new ArrayList<String>();
	private  List<String> freqNames = new ArrayList<String>();
	private String cpu0MaxFreq ;
	private String cpu0CurFreq ;
	private String cpu1MaxFreq ;
	private String cpu1CurFreq ;
	private String cpu2MaxFreq ;
	private String cpu2CurFreq ;
	private String cpu3MaxFreq ;
	private String cpu3CurFreq ;
	private Spinner gov0spinner;
	private Spinner gov1spinner;
	private Spinner gov2spinner;
	private Spinner gov3spinner;

	private String cpu0MinFreq ;
	private String cpu1MinFreq ;
	private String cpu2MinFreq ;
	private String cpu3MinFreq ;

	private VerticalSeekBar cpu0minSeek;
	private VerticalSeekBar cpu0maxSeek;
	private VerticalSeekBar cpu1minSeek;
	private VerticalSeekBar cpu1maxSeek;
	private VerticalSeekBar cpu2minSeek;
	private VerticalSeekBar cpu2maxSeek;
	private VerticalSeekBar cpu3minSeek;
	private VerticalSeekBar cpu3maxSeek;

	private boolean cpu0Online;
	private boolean cpu1Online;
	private boolean cpu2Online;
	private boolean cpu3Online;

	private RelativeLayout rlcpu1;
	private RelativeLayout rlcpu2;
	private RelativeLayout rlcpu3;

	private TextView cpu1txt;
	private TextView cpu2txt;
	private TextView cpu3txt;

	private TextView cpu1govtxt;
	private TextView cpu2govtxt;
	private TextView cpu3govtxt;

	private TextView cpu0min;
	private TextView cpu0max;
	private TextView cpu1min;
	private TextView cpu1max;
	private TextView cpu2min;
	private TextView cpu2max;
	private TextView cpu3min;
	private TextView cpu3max;

	private ProgressBar cpuLoad;
	private TextView cpuLoadTxt;

	private TextView uptime;
	private TextView deepSleep;
	private TextView temp;
	
	private String tmp;
	private String upt;
	private String ds;
	
	private int load;
	private float fLoad;

	private String tempUnit;

	private ProgressDialog pd;	
	
	private SharedPreferences sharedPrefs;

	private CheckBox cb;
	private boolean cpuLock;
	Context c;

	/**
	 * AsyncTask class that will enable All CPUs
	 */
	private final class ToggleCPUs extends AsyncTask<Boolean, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Boolean... args)
		{
			freqEntries = IOHelper.frequencies();
			for(IOHelper.FreqsEntry f: freqEntries){
				frequencies.add(f.getFreq()+"");
			}
			for(IOHelper.FreqsEntry f: freqEntries){
				freqNames.add(f.getFreqName());
			}
			cpu0Online = IOHelper.cpu0Online();
			cpu1Online = IOHelper.cpu1Online();
			cpu2Online = IOHelper.cpu2Online();
			cpu3Online = IOHelper.cpu3Online();
			if (args[0] == true)
			{
				try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();

					String[] str = new String[]{};
		            if (IOHelper.cpu1Online() == true)
					{
		            stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 666 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("echo 1 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
					}
		            if (IOHelper.cpu2Online() == true)
					{
		            stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 666 /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("echo 1 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
					
					}
		            if (IOHelper.cpu3Online() == true)
					{
		            stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 666 /sys/devices/system/cpu/cpu3/online\n").getBytes());
		            stdin.write(("echo 1 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
		            stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
					
					}
		            stdin.flush();

		            stdin.close();
		            BufferedReader brCleanUp =
		                    new BufferedReader(new InputStreamReader(stdout));
		            while ((line = brCleanUp.readLine()) != null) {
		                Log.d("[KernelTuner ToggleCPUs Output]", line);
		            }
		            brCleanUp.close();
		            brCleanUp =
		                    new BufferedReader(new InputStreamReader(stderr));
		            while ((line = brCleanUp.readLine()) != null) {
		            	Log.e("[KernelTuner ToggleCPUs Error]", line);
		            }
		            brCleanUp.close();

		        } catch (IOException ex) {
		        }
				
			}
			else
			{
				
				try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();

		            if (IOHelper.cpu1Online() == true)
					{
		            stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("echo 0 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
					
					}
		            if (IOHelper.cpu2Online() == true)
					{
		            	stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
			            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/online\n").getBytes());
			            stdin.write(("echo 0 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
			            stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
							
					}
		            if (IOHelper.cpu3Online() == true)
					{
		            	stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
			            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/online\n").getBytes());
			            stdin.write(("echo 0 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
			            stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
						
					}
		            stdin.flush();

		            stdin.close();
		            BufferedReader brCleanUp =
		                    new BufferedReader(new InputStreamReader(stdout));
		            while ((line = brCleanUp.readLine()) != null) {
		                Log.d("[KernelTuner ToggleCPUs Output]", line);
		            }
		            brCleanUp.close();
		            brCleanUp =
		                    new BufferedReader(new InputStreamReader(stderr));
		            while ((line = brCleanUp.readLine()) != null) {
		            	Log.e("[KernelTuner ToggleCPUs Error]", line);
		            }
		            brCleanUp.close();

		        } catch (IOException ex) {
		        }
			}

			return args[0];
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (result == true)
			{
				updateUI();
			}
			pd.dismiss();
		}
	}	



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		c = this;
		mHandler = new Handler();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		final String theme = sharedPrefs.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.IndicatorLight);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.IndicatorDark);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.IndicatorLightDark);
			
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cpu_tweaks);
		/**
		 * Show Progress Dialog and execute ToggleCpus class*/
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		pd = ProgressDialog.show(c, null, 
				  getResources().getString(R.string.enabling_cpus), true, false);
		new ToggleCPUs().execute(new Boolean[] {true});


		/**
		 * Load ads if enabled in settings*/
		final boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		rlcpu1 = (RelativeLayout)findViewById(R.id.rlcpu1);
		rlcpu2 = (RelativeLayout)findViewById(R.id.rlcpu2);
		rlcpu3 = (RelativeLayout)findViewById(R.id.rlcpu3);

		uptime = (TextView)findViewById(R.id.textView28);
		deepSleep = (TextView)findViewById(R.id.textView30);
		temp = (TextView)findViewById(R.id.textView32);

		progCpu0 = (ProgressBar)findViewById(R.id.progressBar1);
		progCpu1 = (ProgressBar)findViewById(R.id.progressBar2);
		progCpu2 = (ProgressBar)findViewById(R.id.progressBar3);
		progCpu3 = (ProgressBar)findViewById(R.id.progressBar4);

		
		cpu1txt = (TextView)findViewById(R.id.ptextView2);
		cpu2txt = (TextView)findViewById(R.id.ptextView5);
		cpu3txt = (TextView)findViewById(R.id.ptextView6);

		cpu1govtxt = (TextView)findViewById(R.id.textView4);
		cpu2govtxt = (TextView)findViewById(R.id.textView3);
		cpu3govtxt = (TextView)findViewById(R.id.textView2);

		gov0spinner = (Spinner)findViewById(R.id.spinner3);
		gov1spinner = (Spinner)findViewById(R.id.bg);
		gov2spinner = (Spinner)findViewById(R.id.spinner2);
		gov3spinner = (Spinner)findViewById(R.id.spinner4);

		cpu0prog = (TextView)findViewById(R.id.ptextView3);
		cpu1prog = (TextView)findViewById(R.id.ptextView4);
		cpu2prog = (TextView)findViewById(R.id.ptextView7);
		cpu3prog = (TextView)findViewById(R.id.ptextView8);


		cpu0min = (TextView)findViewById(R.id.textView6);
		cpu0max = (TextView)findViewById(R.id.textView8);
		cpu1min = (TextView)findViewById(R.id.textView11);
		cpu1max = (TextView)findViewById(R.id.textView13);

		cpu2min = (TextView)findViewById(R.id.textView16);
		cpu2max = (TextView)findViewById(R.id.textView18);
		cpu3min = (TextView)findViewById(R.id.textView21);
		cpu3max = (TextView)findViewById(R.id.textView23);
		cpuLoadTxt = (TextView)findViewById(R.id.textView26);

		cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		
		
		cb = (CheckBox)findViewById(R.id.cb);
		cpuLock = sharedPrefs.getBoolean("cpuLock", false);
		if(cpuLock==false){
			cb.setChecked(false);
		}
		else if(cpuLock==true){
			cb.setChecked(true);
		}
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				
					SharedPreferences.Editor editor = sharedPrefs.edit();
					editor.putBoolean("cpuLock", arg1);
					editor.commit();
				
			}
			
		});
		

startCpuLoadThread();


	}
	@Override
	public void onResume()
	{
		tempUnit = sharedPrefs.getString("temp", "celsius");
		
		thread = true;

		new Thread(new Runnable() {

				@Override
				public void run()
				{
				
					while (thread)
					{
						try
						{
							Thread.sleep(1000);
							cpu0CurFreq = IOHelper.cpu0CurFreq();
							cpu0MaxFreq = IOHelper.cpu0MaxFreq();
							tmp = IOHelper.cpuTemp();
							upt = IOHelper.uptime();
							ds = IOHelper.deepSleep();
							if (cpu1Online == true)
							{
								cpu1CurFreq = IOHelper.cpu1CurFreq();
								cpu1MaxFreq = IOHelper.cpu1MaxFreq();
							}
							if (cpu2Online == true)
							{
								cpu2CurFreq = IOHelper.cpu2CurFreq();
								cpu2MaxFreq = IOHelper.cpu2MaxFreq();
							}
							if (cpu3Online == true)
							{
								cpu3CurFreq = IOHelper.cpu3CurFreq();
								cpu3MaxFreq = IOHelper.cpu3MaxFreq();

							}
							mHandler.post(new Runnable() {


									@Override
									public void run()
									{

										updateCpu0();
										cpuInfo(upt, ds);
										cpuTemp(tmp);

										if (cpu1Online == true)
										{
											updateCpu1();
										}
										if (cpu2Online == true)
										{		
											updateCpu2();
										}
										if (cpu3Online == true)
										{
											updateCpu3();
										}

									}
								});
						}
						catch (Exception e)
						{
							
						}
					}
				}
			}).start();

		super.onResume();
	}
	@Override
	public void onDestroy()
	{
		thread = false;
		if(sharedPrefs.getBoolean("htc_one_workaround", false)==false){
		new ToggleCPUs().execute(new Boolean[] {false});
		}
		super.onDestroy();
	}

	@Override
	public void onStop()
	{
		thread = false;

		super.onStop();
	}

	private final void setCpuLoad(){
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(load + "%");
	}
	
	private final void startCpuLoadThread() {
		
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
						} catch (Exception e) {
							}

						reader.seek(0);
						load = reader.readLine();
						reader.close();

						toks = load.split(" ");

						long idle2 = Long.parseLong(toks[5]);
						long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

					} catch (IOException ex) {
							}
					load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
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
	

	
	private final void updateUI()
	{
		
		
		if (cpu1Online == false)
		{
			rlcpu1.setVisibility(View.GONE);
			cb.setVisibility(View.GONE);
			cpu1prog.setVisibility(View.GONE);
			progCpu1.setVisibility(View.GONE);
			cpu1txt.setVisibility(View.GONE);
			cpu1govtxt.setVisibility(View.GONE);
			gov1spinner.setVisibility(View.GONE);
			
		}
		if (cpu2Online == false)
		{
			rlcpu2.setVisibility(View.GONE);

			cpu2prog.setVisibility(View.GONE);
			progCpu2.setVisibility(View.GONE);
			cpu2txt.setVisibility(View.GONE);
			cpu2govtxt.setVisibility(View.GONE);
			gov2spinner.setVisibility(View.GONE);
		}
		if (cpu3Online == false)
		{
			rlcpu3.setVisibility(View.GONE);

			cpu3prog.setVisibility(View.GONE);
			progCpu3.setVisibility(View.GONE);
			cpu3txt.setVisibility(View.GONE);
			cpu3govtxt.setVisibility(View.GONE);
			gov3spinner.setVisibility(View.GONE);
		}
		
		cpu0MinFreq = IOHelper.cpu0MinFreq();
		cpu0MaxFreq = IOHelper.cpu0MaxFreq();
		cpu0min.setText(cpu0MinFreq.substring(0, cpu0MinFreq.length() - 3) + "Mhz");
		cpu0max.setText(cpu0MaxFreq.substring(0, cpu0MaxFreq.length() - 3) + "Mhz");
		cpu0maxSeek = (VerticalSeekBar)findViewById(R.id.cpu0MaxSeekbar);
		cpu0minSeek = (VerticalSeekBar)findViewById(R.id.cpu0MinSeekbar);
		cpu0minSeek.setMax(frequencies.size() - 1);
		cpu0maxSeek.setMax(frequencies.size() - 1);
		cpu0minSeek.setProgress(frequencies.indexOf(cpu0MinFreq));
		cpu0maxSeek.setProgress(frequencies.indexOf(cpu0MaxFreq));
		final int size = freqNames.size();
		cpu0minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				

				int origProg;
				int prog;
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
											  boolean fromUser)
				{
					prog = progress;
					if(cb.isChecked()){
						if(cpu1Online){
						cpu1minSeek.setProgressAndThumb(progress);
						}
						if(cpu2Online){
							cpu2minSeek.setProgressAndThumb(progress);
							}
						if(cpu3Online){
							cpu3minSeek.setProgressAndThumb(progress);
							}
					}

					if(size>progress){
					cpu0min.setText(freqNames.get(progress));
					}

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{

					origProg = seekBar.getProgress();

				} 

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					if (seekBar.getProgress() > frequencies.indexOf(IOHelper.cpu0MaxFreq()))
					{
						try{
							cpu0minSeek.setProgressAndThumb(origProg);
						}
						catch(Exception e){
							cpu0minSeek.setProgressAndThumb(origProg);	
							cpu0min.setText(freqNames.get(origProg));	
						}
					}
					else
					{
						
						new FrequencyChanger(c).execute(new String[] {"cpu0","min", frequencies.get(prog)+""});
						if(cb.isChecked()){
							if(cpu1Online){
							new FrequencyChanger(c).execute(new String[] {"cpu1","min", frequencies.get(prog)+""});
							}
							if(cpu2Online){
								new FrequencyChanger(c).execute(new String[] {"cpu2","min", frequencies.get(prog)+""});
								}
							if(cpu3Online){
								new FrequencyChanger(c).execute(new String[] {"cpu3","min", frequencies.get(prog)+""});
								}
							
						}
					}


				}

				

			});
		cpu0maxSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				int prog;
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
											  boolean fromUser)
				{
					prog = progress;

					if(cb.isChecked()){
						if(cpu1Online){
						cpu1maxSeek.setProgressAndThumb(progress);
						}
						if(cpu2Online){
							cpu2maxSeek.setProgressAndThumb(progress);
							}
						if(cpu3Online){
							cpu3maxSeek.setProgressAndThumb(progress);
							}
					}
					if(size>progress){
					cpu0max.setText(freqNames.get(progress));
					}

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{


				} 

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{

					if (prog < frequencies.indexOf(IOHelper.cpu0MinFreq()))
					{
						cpu0maxSeek.setProgressAndThumb(frequencies.indexOf(IOHelper.cpu0MaxFreq()));
					}
					else
					{
						new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(prog)+""});
						if(cb.isChecked()){
							if(cpu1Online){
							new FrequencyChanger(c).execute(new String[] {"cpu1","max", frequencies.get(prog)+""});
							}
							if(cpu2Online){
								new FrequencyChanger(c).execute(new String[] {"cpu2","max", frequencies.get(prog)+""});
								}
							if(cpu3Online){
								new FrequencyChanger(c).execute(new String[] {"cpu3","max", frequencies.get(prog)+""});
								}
						}
					}


				}

			});
		if (cpu1Online == true)
		{
			cpu1MinFreq = IOHelper.cpu1MinFreq();
			cpu1MaxFreq = IOHelper.cpu1MaxFreq();
			cpu1min.setText(cpu1MinFreq.substring(0, cpu1MinFreq.length() - 3) + "Mhz");
			cpu1max.setText(cpu1MaxFreq.substring(0, cpu1MaxFreq.length() - 3) + "Mhz");
			cpu1maxSeek = (VerticalSeekBar)findViewById(R.id.cpu1MaxSeekbar);
			cpu1minSeek = (VerticalSeekBar)findViewById(R.id.cpu1MinSeekbar);
			
			cpu1minSeek.setMax(frequencies.size() - 1);
			cpu1maxSeek.setMax(frequencies.size() - 1);
			cpu1minSeek.setProgress(frequencies.indexOf(cpu1MinFreq));
			cpu1maxSeek.setProgress(frequencies.indexOf(cpu1MaxFreq));
			cpu1minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					int origProg;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(cpu2Online){
								cpu2minSeek.setProgressAndThumb(progress);
								}
							if(cpu3Online){
								cpu3minSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
							System.out.println(freqNames.get(progress)+"+"+progress);
						cpu1min.setText(freqNames.get(progress));
							System.out.println(freqNames.get(progress)+"+"+progress);
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{
					
					origProg=seekBar.getProgress();


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (seekBar.getProgress() > frequencies.indexOf(IOHelper.cpu1MaxFreq()))
						{
							try{
							cpu1minSeek.setProgressAndThumb(origProg);//frequencies.indexOf(IOHelper.cpu1MinFreq()));
							}
							catch(Exception e){
								cpu1minSeek.setProgressAndThumb(origProg);	
								cpu1min.setText(freqNames.get(origProg));	
							}
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu1","min", frequencies.get(prog)+""});
							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","min", frequencies.get(prog)+""});
								}
								if(cpu2Online){
									new FrequencyChanger(c).execute(new String[] {"cpu2","min", frequencies.get(prog)+""});
									}
								if(cpu3Online){
									new FrequencyChanger(c).execute(new String[] {"cpu3","min", frequencies.get(prog)+""});
									}
							}
						}


					}

				});
			cpu1maxSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(cpu2Online){
								cpu2maxSeek.setProgressAndThumb(progress);
								}
							if(cpu3Online){
								cpu3maxSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
						cpu1max.setText(freqNames.get(progress));
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(IOHelper.cpu1MinFreq()))
						{
							cpu1maxSeek.setProgressAndThumb(frequencies.indexOf(IOHelper.cpu1MaxFreq()));
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu1","max", frequencies.get(prog)+""});
							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(prog)+""});
								}
								if(cpu2Online){
									new FrequencyChanger(c).execute(new String[] {"cpu2","max", frequencies.get(prog)+""});
									}
								if(cpu3Online){
									new FrequencyChanger(c).execute(new String[] {"cpu3","max", frequencies.get(prog)+""});
									}
							}
						}


					}

				});

		}
		if (cpu2Online == true)
		{
			cpu2MinFreq = IOHelper.cpu2MinFreq();
			cpu2MaxFreq = IOHelper.cpu2MaxFreq();
			cpu2min.setText(cpu2MinFreq.substring(0, cpu2MinFreq.length() - 3) + "Mhz");
			cpu2max.setText(cpu2MaxFreq.substring(0, cpu2MaxFreq.length() - 3) + "Mhz");
			cpu2minSeek = (VerticalSeekBar)findViewById(R.id.cpu2MinSeekbar);
			cpu2maxSeek = (VerticalSeekBar)findViewById(R.id.cpu2MaxSeekbar);
			
			cpu2minSeek.setMax(frequencies.size() - 1);
			cpu2maxSeek.setMax(frequencies.size() - 1);
			cpu2minSeek.setProgress(frequencies.indexOf(cpu2MinFreq));
			cpu2maxSeek.setProgress(frequencies.indexOf(cpu2MaxFreq));
			cpu2minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					int origProg;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(cpu1Online){
								cpu1minSeek.setProgressAndThumb(progress);
								}
							if(cpu3Online){
								cpu3minSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
						cpu2min.setText(freqNames.get(progress));
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{

						origProg = seekBar.getProgress();


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (seekBar.getProgress() > frequencies.indexOf(IOHelper.cpu2MaxFreq()))
						{
							try{
								cpu2minSeek.setProgressAndThumb(origProg);//frequencies.indexOf(IOHelper.cpu1MinFreq()));
							}
							catch(Exception e){
								cpu2minSeek.setProgressAndThumb(origProg);	
								cpu2min.setText(freqNames.get(origProg));	
							}
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu2","min", frequencies.get(prog)+""});

							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","min", frequencies.get(prog)+""});
								}
								if(cpu1Online){
									new FrequencyChanger(c).execute(new String[] {"cpu1","min", frequencies.get(prog)+""});
									}
								if(cpu3Online){
									new FrequencyChanger(c).execute(new String[] {"cpu3","min", frequencies.get(prog)+""});
									}
							}
						}


					}

				});
			cpu2maxSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(cpu1Online){
								cpu1maxSeek.setProgressAndThumb(progress);
								}
							if(cpu3Online){
								cpu3maxSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
						cpu2max.setText(freqNames.get(progress));
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(IOHelper.cpu2MinFreq()))
						{
							cpu2maxSeek.setProgressAndThumb(frequencies.indexOf(IOHelper.cpu2MaxFreq()));
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu2","max", frequencies.get(prog)+""});
							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(prog)+""});
								}
								if(cpu1Online){
									new FrequencyChanger(c).execute(new String[] {"cpu1","max", frequencies.get(prog)+""});
									}
								if(cpu3Online){
									new FrequencyChanger(c).execute(new String[] {"cpu3","max", frequencies.get(prog)+""});
									}
							}
						}


					}

				});
		}
		if (cpu3Online == true)
		{
			cpu3MinFreq = IOHelper.cpu3MinFreq();
			cpu3MaxFreq = IOHelper.cpu3MaxFreq();
			cpu3min.setText(cpu3MinFreq.substring(0, cpu3MinFreq.length() - 3) + "Mhz");
			cpu3max.setText(cpu3MaxFreq.substring(0, cpu3MaxFreq.length() - 3) + "Mhz");
			cpu3minSeek = (VerticalSeekBar)findViewById(R.id.cpu3MinSeekbar);
			cpu3maxSeek = (VerticalSeekBar)findViewById(R.id.cpu3MaxSeekbar);
			cpu3minSeek.setMax(frequencies.size() - 1);
			cpu3maxSeek.setMax(frequencies.size() - 1);
			cpu3minSeek.setProgress(frequencies.indexOf(cpu3MinFreq));
			cpu3maxSeek.setProgress(frequencies.indexOf(cpu3MaxFreq));
			cpu3minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					int origProg;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(cpu1Online){
								cpu1minSeek.setProgressAndThumb(progress);
								}
							if(cpu2Online){
								cpu2minSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
						cpu3min.setText(freqNames.get(progress));
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{

						origProg = seekBar.getProgress();


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (seekBar.getProgress() > frequencies.indexOf(IOHelper.cpu3MaxFreq()))
						{
							try{
								cpu3minSeek.setProgressAndThumb(origProg);//frequencies.indexOf(IOHelper.cpu1MinFreq()));
							}
							catch(Exception e){
								cpu3minSeek.setProgressAndThumb(origProg);	
								cpu3min.setText(freqNames.get(origProg));	
							}
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu3","min", frequencies.get(prog)+""});
							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(prog)+""});
								}
								if(cpu1Online){
									new FrequencyChanger(c).execute(new String[] {"cpu1","min", frequencies.get(prog)+""});
									}
								if(cpu2Online){
									new FrequencyChanger(c).execute(new String[] {"cpu2","min", frequencies.get(prog)+""
									});
									}
							}
						}


					}

				});
			cpu3maxSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(cpu0Online){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(cpu1Online){
								cpu1maxSeek.setProgressAndThumb(progress);
								}
							if(cpu2Online){
								cpu2maxSeek.setProgressAndThumb(progress);
								}
						}
						if(size>progress){
						cpu3max.setText(freqNames.get(progress));
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(IOHelper.cpu3MinFreq()))
						{
							cpu3maxSeek.setProgressAndThumb(frequencies.indexOf(IOHelper.cpu3MaxFreq()));
						}
						else
						{
							new FrequencyChanger(c).execute(new String[] {"cpu3","max", frequencies.get(prog)+""});
							if(cb.isChecked()){
								if(cpu0Online){
								new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(prog)+""});
								}
								if(cpu1Online){
									new FrequencyChanger(c).execute(new String[] {"cpu1","max", frequencies.get(prog)+""});
									}
								if(cpu2Online){
									new FrequencyChanger(c).execute(new String[] {"cpu2","max", frequencies.get(prog)+""});
									}
							}
						}


					}

				});
		}



		

		populateGovernorSpinners();
	}

	private final void cpuInfo(String upt, String ds)
	{
		uptime.setText(upt);
		deepSleep.setText(ds);

	}



	

	private final void cpuTemp(String tmp)
	{

		if (tempUnit.equals("celsius"))
		{
			temp.setText(tmp + "°C");
		}
		else if (tempUnit.equals("fahrenheit"))
		{
			if (!tmp.equals(""))
			{
				temp.setText(Double.parseDouble(tmp) * 1.8 + 32 + "°F");
			}
		}
		else if (tempUnit.equals("kelvin"))
		{
			temp.setText(Double.parseDouble(tmp) + 273.15 + "°K");
		}

	}

	private final void updateCpu0()
	{

		if (!cpu0CurFreq.equals("offline") && cpu0CurFreq.length()!=0)
		{
			cpu0prog.setText(cpu0CurFreq.substring(0, cpu0CurFreq.length() - 3) + "Mhz");
		}
		else
		{			
			cpu0prog.setText(getResources().getString(R.string.offline));
		}
		if (frequencies != null && !cpu0MaxFreq.equals("") && !cpu0CurFreq.equals(""))
		{
			progCpu0.setMax(frequencies.indexOf(cpu0MaxFreq.trim()) + 1);
			progCpu0.setProgress(frequencies.indexOf(cpu0CurFreq.trim()) + 1);
		}

	}

	private final void updateCpu1()
	{

		if (!cpu1CurFreq.equals("offline") && cpu0CurFreq.length()!=0)
		{
			cpu1prog.setText(cpu1CurFreq.substring(0, cpu1CurFreq.length() - 3) + "Mhz");

			if(rlcpu1.getVisibility()==View.GONE){
				rlcpu1.setVisibility(View.VISIBLE);
				cb.setVisibility(View.VISIBLE);
				cpu1prog.setVisibility(View.VISIBLE);
				progCpu1.setVisibility(View.VISIBLE);
				cpu1txt.setVisibility(View.VISIBLE);
				cpu1govtxt.setVisibility(View.VISIBLE);
				gov1spinner.setVisibility(View.VISIBLE);
			
				}
		}
		else
		{
			if(rlcpu1.getVisibility()==View.VISIBLE){
			rlcpu1.setVisibility(View.GONE);
			cb.setVisibility(View.GONE);
			cpu1prog.setVisibility(View.GONE);
			progCpu1.setVisibility(View.GONE);
			cpu1txt.setVisibility(View.GONE);
			cpu1govtxt.setVisibility(View.GONE);
			gov1spinner.setVisibility(View.GONE);
			}
			cpu1prog.setText(getResources().getString(R.string.offline));
			
		}

		progCpu1.setMax(frequencies.indexOf(cpu1MaxFreq.trim()) + 1);
		progCpu1.setProgress(frequencies.indexOf(cpu1CurFreq.trim()) + 1);


	}

	private final void updateCpu2()
	{

		if (!cpu2CurFreq.equals("offline") && cpu0CurFreq.length()!=0)
		{
			if(rlcpu2.getVisibility()==View.GONE){
				rlcpu2.setVisibility(View.VISIBLE);
				cpu2prog.setVisibility(View.VISIBLE);
				progCpu2.setVisibility(View.VISIBLE);
				cpu2txt.setVisibility(View.VISIBLE);
				cpu2govtxt.setVisibility(View.VISIBLE);
				gov2spinner.setVisibility(View.VISIBLE);

			}
			cpu2prog.setText(cpu2CurFreq.substring(0, cpu2CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			if(rlcpu2.getVisibility()==View.VISIBLE){
				rlcpu2.setVisibility(View.GONE);
				
				cpu2prog.setVisibility(View.GONE);
				progCpu2.setVisibility(View.GONE);
				cpu2txt.setVisibility(View.GONE);
				cpu2govtxt.setVisibility(View.GONE);
				gov2spinner.setVisibility(View.GONE);
			}
			cpu2prog.setText(getResources().getString(R.string.offline));
		}

		progCpu2.setMax(frequencies.indexOf(cpu2MaxFreq.trim()) + 1);
		progCpu2.setProgress(frequencies.indexOf(cpu2CurFreq.trim()) + 1);


	}

	private final void updateCpu3()
	{

		if (!cpu3CurFreq.equals("offline") && cpu0CurFreq.length()!=0)
		{
			if(rlcpu3.getVisibility()==View.GONE){
				rlcpu3.setVisibility(View.VISIBLE);
				cpu3prog.setVisibility(View.VISIBLE);
				progCpu3.setVisibility(View.VISIBLE);
				cpu3txt.setVisibility(View.VISIBLE);
				cpu3govtxt.setVisibility(View.VISIBLE);
				gov3spinner.setVisibility(View.VISIBLE);

			}
			cpu3prog.setText(cpu3CurFreq.substring(0, cpu3CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			if(rlcpu3.getVisibility()==View.VISIBLE){
				rlcpu3.setVisibility(View.GONE);
				cpu3prog.setVisibility(View.GONE);
				progCpu3.setVisibility(View.GONE);
				cpu3txt.setVisibility(View.GONE);
				cpu3govtxt.setVisibility(View.GONE);
				gov3spinner.setVisibility(View.GONE);
			}
			cpu3prog.setText(getResources().getString(R.string.offline));
		}

		progCpu3.setMax(frequencies.indexOf(cpu3MaxFreq.trim()) + 1);
		progCpu3.setProgress(frequencies.indexOf(cpu3CurFreq.trim()) + 1);


	}

	private final void populateGovernorSpinners()
	{
		
		ArrayAdapter<String> govAdapter = new ArrayAdapter<String>(c,   android.R.layout.simple_spinner_item, IOHelper.governors());
		govAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
    	gov0spinner.setAdapter(govAdapter);

		
		int gov0spinnerPosition = govAdapter.getPosition(IOHelper.cpu0CurGov());
		gov0spinner.setSelection(gov0spinnerPosition);

		gov0spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new ChangeGovernor(c).execute(new String[] {"cpu0",parent.getItemAtPosition(pos).toString()});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});




		//govrnors for cpu1
		if (cpu0Online == true)
		{
			gov1spinner.setAdapter(govAdapter);

			int gov1spinnerPosition = govAdapter.getPosition(IOHelper.cpu1CurGov());
			gov1spinner.setSelection(gov1spinnerPosition);

			gov1spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(c).execute(new String[] {"cpu1",parent.getItemAtPosition(pos).toString()});

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});


		}

		//cpu2 governors
		if (cpu2Online == true)
		{
			gov2spinner.setAdapter(govAdapter);

			int gov2spinnerPosition = govAdapter.getPosition(IOHelper.cpu2CurGov());
			gov2spinner.setSelection(gov2spinnerPosition);

			gov2spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(c).execute(new String[] {"cpu2",parent.getItemAtPosition(pos).toString()});

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});


		}
		if (cpu3Online == true)
		{
			//cpu3 governors
			gov3spinner.setAdapter(govAdapter);

			int gov3spinnerPosition = govAdapter.getPosition(IOHelper.cpu3CurGov());
			gov3spinner.setSelection(gov3spinnerPosition);

			gov3spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(c).execute(new String[] {"cpu3",parent.getItemAtPosition(pos).toString()});


					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});


		}
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(c, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}

}
