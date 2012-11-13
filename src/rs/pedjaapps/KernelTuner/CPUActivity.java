package rs.pedjaapps.KernelTuner;


import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView.*;
import android.widget.SeekBar.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;

import java.lang.Process;

public class CPUActivity extends Activity
{

	boolean thread = true;
	Handler mHandler = new Handler();
	TextView cpu0prog;
	ProgressBar progCpu0;
	TextView cpu1prog;
	ProgressBar progCpu1;
	TextView cpu2prog;
	ProgressBar progCpu2;
	TextView cpu3prog;
	ProgressBar progCpu3;
	List<String> frequencies;
	String cpu0MaxFreq ;
	String cpu0CurFreq ;
	String cpu1MaxFreq ;
	String cpu1CurFreq ;
	String cpu2MaxFreq ;
	String cpu2CurFreq ;
	String cpu3MaxFreq ;
	String cpu3CurFreq ;
	Spinner gov0spinner;
	Spinner gov1spinner;
	Spinner gov2spinner;
	Spinner gov3spinner;

	String cpu0MinFreq ;
	String cpu1MinFreq ;
	String cpu2MinFreq ;
	String cpu3MinFreq ;

	VerticalSeekBar cpu0minSeek;
	VerticalSeekBar cpu0maxSeek;
	VerticalSeekBar cpu1minSeek;
	VerticalSeekBar cpu1maxSeek;
	VerticalSeekBar cpu2minSeek;
	VerticalSeekBar cpu2maxSeek;
	VerticalSeekBar cpu3minSeek;
	VerticalSeekBar cpu3maxSeek;

	boolean cpu0Online;
	boolean cpu1Online;
	boolean cpu2Online;
	boolean cpu3Online;

	RelativeLayout rlcpu1;
	RelativeLayout rlcpu2;
	RelativeLayout rlcpu3;

	TextView curFreq0;
	TextView curFreq1;
	TextView curFreq2;
	TextView curFreq3;

	TextView cpu0txt;
	TextView cpu1txt;
	TextView cpu2txt;
	TextView cpu3txt;

	TextView cpu1govtxt;
	TextView cpu2govtxt;
	TextView cpu3govtxt;

	TextView cpu0min;
	TextView cpu0max;
	TextView cpu1min;
	TextView cpu1max;
	TextView cpu2min;
	TextView cpu2max;
	TextView cpu3min;
	TextView cpu3max;

	ProgressBar cpuLoad;
	TextView cpuLoadTxt;

	TextView uptime;
	TextView deepSleep;
	TextView temp;
	
	int load;
	float fLoad;

	String tempUnit;

	ProgressDialog pd;	
	
	SharedPreferences sharedPrefs;

	CheckBox cb;
	boolean cpuLock;

	private class ToggleCPUs extends AsyncTask<Boolean, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Boolean... args)
		{

			Process localProcess;
			if (args[0] == true)
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					if (CPUInfo.cpu1Online() == true)
					{
						localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
					}
					if (CPUInfo.cpu2Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
					}
					if (CPUInfo.cpu3Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					if (CPUInfo.cpu1Online() == true)
					{
						localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
						localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
					}
					if (CPUInfo.cpu2Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu2/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
					}
					if (CPUInfo.cpu3Online() == true)
					{
						localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu3/online\n");
						localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
				}
				catch (IOException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				catch (InterruptedException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
					
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
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cpu_tweaks);
		this.pd = ProgressDialog.show(this, "Working..",
				  "Enabling all CPUs...\nPlease wait...", true, false);
		new ToggleCPUs().execute(new Boolean[] {true});


		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)this.findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		rlcpu1 = (RelativeLayout)findViewById(R.id.rlcpu1);
		rlcpu2 = (RelativeLayout)findViewById(R.id.rlcpu2);
		rlcpu3 = (RelativeLayout)findViewById(R.id.rlcpu3);

		uptime = (TextView)findViewById(R.id.textView28);
		deepSleep = (TextView)findViewById(R.id.textView30);
		temp = (TextView)findViewById(R.id.textView32);

		curFreq0 = (TextView)findViewById(R.id.ptextView3);
		curFreq1 = (TextView)findViewById(R.id.ptextView4);
		curFreq2 = (TextView)findViewById(R.id.ptextView7);
		curFreq3 = (TextView)findViewById(R.id.ptextView8);

		progCpu0 = (ProgressBar)findViewById(R.id.progressBar1);
		progCpu1 = (ProgressBar)findViewById(R.id.progressBar2);
		progCpu2 = (ProgressBar)findViewById(R.id.progressBar3);
		progCpu3 = (ProgressBar)findViewById(R.id.progressBar4);

		cpu0txt = (TextView)findViewById(R.id.ptextView1);
		cpu1txt = (TextView)findViewById(R.id.ptextView2);
		cpu2txt = (TextView)findViewById(R.id.ptextView5);
		cpu3txt = (TextView)findViewById(R.id.ptextView6);

		cpu1govtxt = (TextView)findViewById(R.id.textView4);
		cpu2govtxt = (TextView)findViewById(R.id.textView3);
		cpu3govtxt = (TextView)findViewById(R.id.textView2);

		gov0spinner = (Spinner)findViewById(R.id.spinner3);
		gov1spinner = (Spinner)findViewById(R.id.spinner1);
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
		
		cpu0Online = CPUInfo.cpu0Online();
		cpu1Online = CPUInfo.cpu1Online();
		cpu2Online = CPUInfo.cpu2Online();
		cpu3Online = CPUInfo.cpu3Online();
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
		if (cpu1Online == false)
		{
			rlcpu1.setVisibility(View.GONE);
			cb.setVisibility(View.GONE);
			curFreq1.setVisibility(View.GONE);
			progCpu1.setVisibility(View.GONE);
			cpu1txt.setVisibility(View.GONE);
			cpu1govtxt.setVisibility(View.GONE);
			gov1spinner.setVisibility(View.GONE);
			
		}
		if (cpu2Online == false)
		{
			rlcpu2.setVisibility(View.GONE);

			curFreq2.setVisibility(View.GONE);
			progCpu2.setVisibility(View.GONE);
			cpu2txt.setVisibility(View.GONE);
			cpu2govtxt.setVisibility(View.GONE);
			gov2spinner.setVisibility(View.GONE);
		}
		if (cpu3Online == false)
		{
			rlcpu3.setVisibility(View.GONE);

			curFreq3.setVisibility(View.GONE);
			progCpu3.setVisibility(View.GONE);
			cpu3txt.setVisibility(View.GONE);
			cpu3govtxt.setVisibility(View.GONE);
			gov3spinner.setVisibility(View.GONE);
		}

startCpuLoadThread();


	}
	@Override
	public void onResume()
	{
		tempUnit = sharedPrefs.getString("temp", "celsius");
		frequencies = CPUInfo.frequencies();
		thread = true;

		new Thread(new Runnable() {

				public void run()
				{
				
					while (thread)
					{
						try
						{
							Thread.sleep(1000);
							mHandler.post(new Runnable() {


									public void run()
									{

										cpu0CurFreq = CPUInfo.cpu0CurFreq();
										cpu0MaxFreq = CPUInfo.cpu0MaxFreq();


										updateCpu0();
										cpuInfo();
										cpuTemp();


										if (cpu1Online == true)
										{

											cpu1CurFreq = CPUInfo.cpu1CurFreq();
											cpu1MaxFreq = CPUInfo.cpu1MaxFreq();
											updateCpu1();


										}
										if (cpu2Online == true)
										{
											cpu2CurFreq = CPUInfo.cpu2CurFreq();
											cpu2MaxFreq = CPUInfo.cpu2MaxFreq();

											updateCpu2();


										}
										if (cpu3Online == true)
										{

											cpu3CurFreq = CPUInfo.cpu3CurFreq();
											cpu3MaxFreq = CPUInfo.cpu3MaxFreq();

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
		new ToggleCPUs().execute(new Boolean[] {false});
		super.onDestroy();
	}

	@Override
	public void onStop()
	{
		thread = false;

		super.onStop();
	}

	public void setCpuLoad(){
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(String.valueOf(load) + "%");
	}
	
	public void startCpuLoadThread() {
		
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
							new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
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
						new LogWriter().execute(new String[] {getClass().getName(), ex.getMessage()});
					}
					load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
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
	

	
	public void updateUI()
	{
		

		frequencies = CPUInfo.frequencies();
		cpu0MinFreq = CPUInfo.cpu0MinFreq();
		cpu1MinFreq = CPUInfo.cpu1MinFreq();
		cpu2MinFreq = CPUInfo.cpu2MinFreq();
		cpu3MinFreq = CPUInfo.cpu3MinFreq();
		cpu0MaxFreq = CPUInfo.cpu0MaxFreq();
		cpu1MaxFreq = CPUInfo.cpu1MaxFreq();
		cpu2MaxFreq = CPUInfo.cpu2MaxFreq();
		cpu3MaxFreq = CPUInfo.cpu3MaxFreq();
		cpu0min.setText(cpu0MinFreq.substring(0, cpu0MinFreq.length() - 3) + "Mhz");
		cpu0max.setText(cpu0MaxFreq.substring(0, cpu0MaxFreq.length() - 3) + "Mhz");
		cpu1min.setText(cpu1MinFreq.substring(0, cpu1MinFreq.length() - 3) + "Mhz");
		cpu1max.setText(cpu1MaxFreq.substring(0, cpu1MaxFreq.length() - 3) + "Mhz");
		cpu2min.setText(cpu2MinFreq.substring(0, cpu2MinFreq.length() - 3) + "Mhz");
		cpu2max.setText(cpu2MaxFreq.substring(0, cpu2MaxFreq.length() - 3) + "Mhz");
		cpu3min.setText(cpu3MinFreq.substring(0, cpu3MinFreq.length() - 3) + "Mhz");
		cpu3max.setText(cpu3MaxFreq.substring(0, cpu3MaxFreq.length() - 3) + "Mhz");
		
		cpu0maxSeek = (VerticalSeekBar)findViewById(R.id.cpu0MaxSeekbar);
		cpu0minSeek = (VerticalSeekBar)findViewById(R.id.cpu0MinSeekbar);
		cpu1maxSeek = (VerticalSeekBar)findViewById(R.id.cpu1MaxSeekbar);
		cpu1minSeek = (VerticalSeekBar)findViewById(R.id.cpu1MinSeekbar);
		cpu2minSeek = (VerticalSeekBar)findViewById(R.id.cpu2MinSeekbar);
		cpu2maxSeek = (VerticalSeekBar)findViewById(R.id.cpu2MaxSeekbar);
		cpu3minSeek = (VerticalSeekBar)findViewById(R.id.cpu3MinSeekbar);
		cpu3maxSeek = (VerticalSeekBar)findViewById(R.id.cpu3MaxSeekbar);
		cpu0minSeek.setMax(frequencies.size() - 1);
		cpu0maxSeek.setMax(frequencies.size() - 1);
		cpu0minSeek.setProgress(frequencies.indexOf(cpu0MinFreq));
		cpu0maxSeek.setProgress(frequencies.indexOf(cpu0MaxFreq));
		cpu0minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				int prog;
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
											  boolean fromUser)
				{
					prog = progress;
					if(cb.isChecked()){
						if(CPUInfo.cpu1Online()){
						cpu1minSeek.setProgressAndThumb(progress);
						}
						if(CPUInfo.cpu2Online()){
							cpu2minSeek.setProgressAndThumb(progress);
							}
						if(CPUInfo.cpu3Online()){
							cpu3minSeek.setProgressAndThumb(progress);
							}
					}

					cpu0min.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{


				} 

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					if (prog > frequencies.indexOf(CPUInfo.cpu0MaxFreq()))
					{
						cpu0minSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu0MinFreq()));
					}
					else
					{
						
						new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","min", frequencies.get(prog)});
						if(cb.isChecked()){
							if(CPUInfo.cpu1Online()){
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","min", frequencies.get(prog)});
							}
							if(CPUInfo.cpu2Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","min", frequencies.get(prog)});
								}
							if(CPUInfo.cpu3Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","min", frequencies.get(prog)});
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
						if(CPUInfo.cpu1Online()){
						cpu1maxSeek.setProgressAndThumb(progress);
						}
						if(CPUInfo.cpu2Online()){
							cpu2maxSeek.setProgressAndThumb(progress);
							}
						if(CPUInfo.cpu3Online()){
							cpu3maxSeek.setProgressAndThumb(progress);
							}
					}

					cpu0max.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{


				} 

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{

					if (prog < frequencies.indexOf(CPUInfo.cpu0MinFreq()))
					{
						cpu0maxSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu0MaxFreq()));
					}
					else
					{
						new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","max", frequencies.get(prog)});
						if(cb.isChecked()){
							if(CPUInfo.cpu1Online()){
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","max", frequencies.get(prog)});
							}
							if(CPUInfo.cpu2Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","max", frequencies.get(prog)});
								}
							if(CPUInfo.cpu3Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","max", frequencies.get(prog)});
								}
						}
					}


				}

			});
		if (cpu1Online == true)
		{
			
			cpu1minSeek.setMax(frequencies.size() - 1);
			cpu1maxSeek.setMax(frequencies.size() - 1);
			cpu1minSeek.setProgress(frequencies.indexOf(cpu1MinFreq));
			cpu1maxSeek.setProgress(frequencies.indexOf(cpu1MaxFreq));
			cpu1minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(CPUInfo.cpu0Online()){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu2Online()){
								cpu2minSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu3Online()){
								cpu3minSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu1min.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{



					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog > frequencies.indexOf(CPUInfo.cpu1MaxFreq()))
						{
							cpu1minSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu1MinFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","min", frequencies.get(prog)});
							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","min", frequencies.get(prog)});
								}
								if(CPUInfo.cpu2Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","min", frequencies.get(prog)});
									}
								if(CPUInfo.cpu3Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","min", frequencies.get(prog)});
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
							if(CPUInfo.cpu0Online()){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu2Online()){
								cpu2maxSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu3Online()){
								cpu3maxSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu1max.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(CPUInfo.cpu1MinFreq()))
						{
							cpu1maxSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu1MaxFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","max", frequencies.get(prog)});
							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","max", frequencies.get(prog)});
								}
								if(CPUInfo.cpu2Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","max", frequencies.get(prog)});
									}
								if(CPUInfo.cpu3Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","max", frequencies.get(prog)});
									}
							}
						}


					}

				});

		}
		if (cpu2Online == true)
		{
			cpu2minSeek.setMax(frequencies.size() - 1);
			cpu2maxSeek.setMax(frequencies.size() - 1);
			cpu2minSeek.setProgress(frequencies.indexOf(cpu2MinFreq));
			cpu2maxSeek.setProgress(frequencies.indexOf(cpu2MaxFreq));
			cpu2minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(CPUInfo.cpu0Online()){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu1Online()){
								cpu1minSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu3Online()){
								cpu3minSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu2min.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{



					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog > frequencies.indexOf(CPUInfo.cpu2MaxFreq()))
						{
							cpu2minSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu2MinFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","min", frequencies.get(prog)});

							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","min", frequencies.get(prog)});
								}
								if(CPUInfo.cpu1Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","min", frequencies.get(prog)});
									}
								if(CPUInfo.cpu3Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","min", frequencies.get(prog)});
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
							if(CPUInfo.cpu0Online()){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu1Online()){
								cpu1maxSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu3Online()){
								cpu3maxSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu2max.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(CPUInfo.cpu2MinFreq()))
						{
							cpu2maxSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu2MaxFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","max", frequencies.get(prog)});
							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","max", frequencies.get(prog)});
								}
								if(CPUInfo.cpu1Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","max", frequencies.get(prog)});
									}
								if(CPUInfo.cpu3Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","max", frequencies.get(prog)});
									}
							}
						}


					}

				});
		}
		if (cpu3Online == true)
		{
			cpu3minSeek.setMax(frequencies.size() - 1);
			cpu3maxSeek.setMax(frequencies.size() - 1);
			cpu3minSeek.setProgress(frequencies.indexOf(cpu3MinFreq));
			cpu3maxSeek.setProgress(frequencies.indexOf(cpu3MaxFreq));
			cpu3minSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
					int prog;
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
												  boolean fromUser)
					{
						prog = progress;

						if(cb.isChecked()){
							if(CPUInfo.cpu0Online()){
							cpu0minSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu1Online()){
								cpu1minSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu2Online()){
								cpu2minSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu3min.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{



					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog > frequencies.indexOf(CPUInfo.cpu3MaxFreq()))
						{
							cpu3minSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu3MinFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","min", frequencies.get(prog)});
							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","max", frequencies.get(prog)});
								}
								if(CPUInfo.cpu1Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","min", frequencies.get(prog)});
									}
								if(CPUInfo.cpu2Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","min", frequencies.get(prog)});
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
							if(CPUInfo.cpu0Online()){
							cpu0maxSeek.setProgressAndThumb(progress);
							}
							if(CPUInfo.cpu1Online()){
								cpu1maxSeek.setProgressAndThumb(progress);
								}
							if(CPUInfo.cpu2Online()){
								cpu2maxSeek.setProgressAndThumb(progress);
								}
						}
						
						cpu3max.setText(frequencies.get(progress).substring(0, frequencies.get(progress).length() - 3) + "Mhz");


					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{


					} 

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (prog < frequencies.indexOf(CPUInfo.cpu3MinFreq()))
						{
							cpu3maxSeek.setProgressAndThumb(frequencies.indexOf(CPUInfo.cpu3MaxFreq()));
						}
						else
						{
							new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu3","max", frequencies.get(prog)});
							if(cb.isChecked()){
								if(CPUInfo.cpu0Online()){
								new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu0","max", frequencies.get(prog)});
								}
								if(CPUInfo.cpu1Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu1","max", frequencies.get(prog)});
									}
								if(CPUInfo.cpu2Online()){
									new FrequencyChanger(CPUActivity.this).execute(new String[] {"cpu2","max", frequencies.get(prog)});
									}
							}
						}


					}

				});
		}



		

		populateGovernorSpinners();
	}

	public void cpuInfo()
	{
		uptime.setText(CPUInfo.uptime());
		deepSleep.setText(CPUInfo.deepSleep());

	}



	

	public void cpuTemp()
	{

		if (tempUnit.equals("celsius"))
		{
			temp.setText(CPUInfo.cpuTemp() + "°C");
		}
		else if (tempUnit.equals("fahrenheit"))
		{
			if (!CPUInfo.cpuTemp().equals(""))
			{
				temp.setText(Double.parseDouble(CPUInfo.cpuTemp()) * 1.8 + 32 + "°F");
			}
		}
		else if (tempUnit.equals("kelvin"))
		{
			temp.setText(Double.parseDouble(CPUInfo.cpuTemp()) + 273.15 + "°K");
		}

	}

	public void updateCpu0()
	{

		if (!cpu0CurFreq.equals("offline"))
		{
			cpu0prog.setText(cpu0CurFreq.substring(0, cpu0CurFreq.length() - 3) + "Mhz");
		}
		else
		{			
			cpu0prog.setText("offline");
		}
		if (frequencies != null && !cpu0MaxFreq.equals("") && !cpu0CurFreq.equals(""))
		{
			progCpu0.setMax(frequencies.indexOf(cpu0MaxFreq.trim()) + 1);
			progCpu0.setProgress(frequencies.indexOf(cpu0CurFreq.trim()) + 1);
		}

	}

	public void updateCpu1()
	{

		if (!cpu1CurFreq.equals("offline"))
		{
			cpu1prog.setText(cpu1CurFreq.substring(0, cpu1CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu1prog.setText("offline");
		}

		progCpu1.setMax(frequencies.indexOf(cpu1MaxFreq.trim()) + 1);
		progCpu1.setProgress(frequencies.indexOf(cpu1CurFreq.trim()) + 1);


	}

	public void updateCpu2()
	{

		if (!cpu2CurFreq.equals("offline"))
		{
			cpu2prog.setText(cpu2CurFreq.substring(0, cpu2CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu2prog.setText("offline");
		}

		progCpu2.setMax(frequencies.indexOf(cpu2MaxFreq.trim()) + 1);
		progCpu2.setProgress(frequencies.indexOf(cpu2CurFreq.trim()) + 1);


	}

	public void updateCpu3()
	{

		if (!cpu3CurFreq.equals("offline"))
		{
			cpu3prog.setText(cpu3CurFreq.substring(0, cpu3CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu3prog.setText("offline");
		}

		progCpu3.setMax(frequencies.indexOf(cpu3MaxFreq.trim()) + 1);
		progCpu3.setProgress(frequencies.indexOf(cpu3CurFreq.trim()) + 1);


	}

	public void populateGovernorSpinners()
	{
		
		ArrayAdapter<String> gov0spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CPUInfo.governors());
		System.out.println(CPUInfo.governors());
		gov0spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gov0spinner.setAdapter(gov0spinnerArrayAdapter);

		ArrayAdapter<String> gov0Adap = (ArrayAdapter<String>) gov0spinner.getAdapter();
		int gov0spinnerPosition = gov0Adap.getPosition(CPUInfo.cpu0CurGov());
		gov0spinner.setSelection(gov0spinnerPosition);

		gov0spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new ChangeGovernor(CPUActivity.this).execute(new String[] {"cpu0",parent.getItemAtPosition(pos).toString()});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});




		//govrnors for cpu1
		if (cpu0Online == true)
		{
			ArrayAdapter<String> gov1spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CPUInfo.governors());
			gov1spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			gov1spinner.setAdapter(gov1spinnerArrayAdapter);

			ArrayAdapter<String> gov1Adap = (ArrayAdapter<String>) gov1spinner.getAdapter();
			int gov1spinnerPosition = gov1Adap.getPosition(CPUInfo.cpu1CurGov());
			gov1spinner.setSelection(gov1spinnerPosition);

			gov1spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivity.this).execute(new String[] {"cpu1",parent.getItemAtPosition(pos).toString()});

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
			ArrayAdapter<String> gov2spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CPUInfo.governors());
			gov2spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			gov2spinner.setAdapter(gov2spinnerArrayAdapter);

			ArrayAdapter<String> gov2Adap = (ArrayAdapter<String>) gov2spinner.getAdapter();
			int gov2spinnerPosition = gov2Adap.getPosition(CPUInfo.cpu2CurGov());
			gov2spinner.setSelection(gov2spinnerPosition);

			gov2spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivity.this).execute(new String[] {"cpu2",parent.getItemAtPosition(pos).toString()});

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
			ArrayAdapter<String> gov3spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CPUInfo.governors());
			gov3spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			gov3spinner.setAdapter(gov3spinnerArrayAdapter);

			ArrayAdapter<String> gov3Adap = (ArrayAdapter<String>) gov3spinner.getAdapter();
			int gov3spinnerPosition = gov3Adap.getPosition(CPUInfo.cpu3CurGov());
			gov3spinner.setSelection(gov3spinnerPosition);

			gov3spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivity.this).execute(new String[] {"cpu3",parent.getItemAtPosition(pos).toString()});


					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});


		}
	}


}
