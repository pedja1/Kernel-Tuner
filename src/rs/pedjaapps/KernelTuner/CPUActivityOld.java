package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.slidingmenu.lib.SlidingMenu;

public class CPUActivityOld extends SherlockActivity
{

	private  List<CPUInfo.FreqsEntry> freqEntries = CPUInfo.frequencies();
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
	
	private Spinner cpu0MinSpinner;
	private Spinner cpu1MinSpinner;
	private Spinner cpu2MinSpinner;
	private Spinner cpu3MinSpinner;
	
	private Spinner cpu0MaxSpinner;
	private Spinner cpu1MaxSpinner;
	private Spinner cpu2MaxSpinner;
	private Spinner cpu3MaxSpinner;

	private String cpu0MinFreq ;
	private String cpu1MinFreq ;
	private String cpu2MinFreq ;
	private String cpu3MinFreq ;


	private final boolean cpu0Online = CPUInfo.cpu0Online();
	private final boolean cpu1Online = CPUInfo.cpu1Online();
	private final boolean cpu2Online = CPUInfo.cpu2Online();
	private final boolean cpu3Online = CPUInfo.cpu3Online();

	private LinearLayout rlcpu1;
	private LinearLayout rlcpu2;
	private LinearLayout rlcpu3;

	
	private TextView curFreq1;
	private TextView curFreq2;
	private TextView curFreq3;

	
	private TextView cpu1txt;
	private TextView cpu2txt;
	private TextView cpu3txt;

	private TextView cpu1govtxt;
	private TextView cpu2govtxt;
	private TextView cpu3govtxt;

	private ProgressBar cpuLoad;
	private TextView cpuLoadTxt;

	private TextView uptime;
	private TextView deepSleep;
	private TextView temp;
	
	private String tmp;
	private String upt;
	private String ds;
	
	//private int load;
	private float fLoad;

	private String tempUnit;

	private ProgressDialog pd;	
	
	private SharedPreferences sharedPrefs;


	/**
	 * AsyncTask class that will enable All CPUs
	 */
	private final class ToggleCPUs extends AsyncTask<Boolean, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Boolean... args)
		{
			if (args[0] == true)
			{
				try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();

		            if (CPUInfo.cpu1Online() == true)
					{
		            stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 666 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("echo 1 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
					
					}
		            if (CPUInfo.cpu2Online() == true)
					{
		            stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 666 /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("echo 1 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
					
					}
		            if (CPUInfo.cpu3Online() == true)
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

		            if (CPUInfo.cpu1Online() == true)
					{
		            stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("echo 0 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
		            stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
					
					}
		            if (CPUInfo.cpu2Online() == true)
					{
		            	stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
			            stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/online\n").getBytes());
			            stdin.write(("echo 0 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
			            stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
							
					}
		            if (CPUInfo.cpu3Online() == true)
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
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll().build());
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
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

		setContentView(R.layout.cpu_tweaks_old);
		mHandler = new Handler();
		final SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.side);
		
		final GridView sideView = (GridView) menu.findViewById(R.id.grid);
		SideMenuAdapter sideAdapter = new SideMenuAdapter(this, R.layout.side_item);
		
		sideView.setAdapter(sideAdapter);

		
		sideView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				List<SideMenuEntry> entries =  SideItems.getEntries();
				Intent intent = new Intent();
				intent.setClass(CPUActivityOld.this, entries.get(position).getActivity());
				startActivity(intent);
				menu.showContent();
			}
			
		});
		List<SideMenuEntry> entries =  SideItems.getEntries();
		for(SideMenuEntry e: entries){
			sideAdapter.add(e);
		}
		
		/**
		 * Show Progress Dialog and execute ToggleCpus class*/
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		CPUActivityOld.this.pd = ProgressDialog.show(CPUActivityOld.this, null, 
				  getResources().getString(R.string.enabling_cpus), true, false);
		new ToggleCPUs().execute(new Boolean[] {true});


		/**
		 * Load ads if enabled in settings*/
		final boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)this.findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		rlcpu1 = (LinearLayout)findViewById(R.id.cpu1lay);
		rlcpu2 = (LinearLayout)findViewById(R.id.cpu2lay);
		rlcpu3 = (LinearLayout)findViewById(R.id.cpu3lay);

		uptime = (TextView)findViewById(R.id.textView28);
		deepSleep = (TextView)findViewById(R.id.textView30);
		temp = (TextView)findViewById(R.id.textView32);

		
		curFreq1 = (TextView)findViewById(R.id.ptextView4);
		curFreq2 = (TextView)findViewById(R.id.ptextView7);
		curFreq3 = (TextView)findViewById(R.id.ptextView8);

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
		gov1spinner = (Spinner)findViewById(R.id.spinner1);
		gov2spinner = (Spinner)findViewById(R.id.spinner2);
		gov3spinner = (Spinner)findViewById(R.id.spinner4);
		
		cpu0MinSpinner = (Spinner)findViewById(R.id.spinner5);
		cpu1MinSpinner = (Spinner)findViewById(R.id.spinner7);
		cpu2MinSpinner = (Spinner)findViewById(R.id.spinner9);
		cpu3MinSpinner = (Spinner)findViewById(R.id.spinner11);
		
		cpu0MaxSpinner = (Spinner)findViewById(R.id.spinner6);
		cpu1MaxSpinner = (Spinner)findViewById(R.id.spinner8);
		cpu2MaxSpinner = (Spinner)findViewById(R.id.spinner10);
		cpu3MaxSpinner = (Spinner)findViewById(R.id.spinner12);

		cpu0prog = (TextView)findViewById(R.id.ptextView3);
		cpu1prog = (TextView)findViewById(R.id.ptextView4);
		cpu2prog = (TextView)findViewById(R.id.ptextView7);
		cpu3prog = (TextView)findViewById(R.id.ptextView8);


		
		cpuLoadTxt = (TextView)findViewById(R.id.textView26);

		cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		
		
		
		if (cpu1Online == false)
		{
			rlcpu1.setVisibility(View.GONE);
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
		/*for(CPUInfo.FreqsEntry f: freqEntries){
			frequencies.add(f.getFreq());
		}
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}*/
		
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
							cpu0CurFreq = CPUInfo.cpu0CurFreq();
							cpu0MaxFreq = CPUInfo.cpu0MaxFreq();
							tmp = CPUInfo.cpuTemp();
							upt = CPUInfo.uptime();
							ds = CPUInfo.deepSleep();
							if (cpu1Online == true)
							{
								cpu1CurFreq = CPUInfo.cpu1CurFreq();
								cpu1MaxFreq = CPUInfo.cpu1MaxFreq();
							}
							if (cpu2Online == true)
							{
								cpu2CurFreq = CPUInfo.cpu2CurFreq();
								cpu2MaxFreq = CPUInfo.cpu2MaxFreq();
							}
							if (cpu3Online == true)
							{
								cpu3CurFreq = CPUInfo.cpu3CurFreq();
								cpu3MaxFreq = CPUInfo.cpu3MaxFreq();

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

	private void setCpuLoad(int load){
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(load+"%");
	}
	
	private void startCpuLoadThread() {
		
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
					final int load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					mHandler.post(new Runnable() {
							@Override
							public void run() {
							
								setCpuLoad(load);
							}
						});
				}
			}
		};
		new Thread(runnable).start();
	}
	

	
	private final void updateUI()
	{
		
		
		for(CPUInfo.FreqsEntry f: freqEntries){
			frequencies.add(f.getFreq()+"");
		}
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}
		
		cpu0MinFreq = CPUInfo.cpu0MinFreq();
		cpu1MinFreq = CPUInfo.cpu1MinFreq();
		cpu2MinFreq = CPUInfo.cpu2MinFreq();
		cpu3MinFreq = CPUInfo.cpu3MinFreq();
		cpu0MaxFreq = CPUInfo.cpu0MaxFreq();
		cpu1MaxFreq = CPUInfo.cpu1MaxFreq();
		cpu2MaxFreq = CPUInfo.cpu2MaxFreq();
		cpu3MaxFreq = CPUInfo.cpu3MaxFreq();
		

		ArrayAdapter<String> mhzAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqNames);
		mhzAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cpu0MinSpinner.setAdapter(mhzAdapter);
		cpu1MinSpinner.setAdapter(mhzAdapter);
		cpu2MinSpinner.setAdapter(mhzAdapter);
		cpu3MinSpinner.setAdapter(mhzAdapter);
		
		cpu0MaxSpinner.setAdapter(mhzAdapter);
		cpu1MaxSpinner.setAdapter(mhzAdapter);
		cpu2MaxSpinner.setAdapter(mhzAdapter);
		cpu3MaxSpinner.setAdapter(mhzAdapter);

		int cpu0MinPosition = mhzAdapter.getPosition(cpu0MinFreq);
		cpu0MinSpinner.setSelection(cpu0MinPosition);

		gov1spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					//new ChangeGovernor(CPUActivity.this).execute(new String[] {"cpu1",parent.getItemAtPosition(pos).toString()});

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});

		

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

		if (!cpu0CurFreq.equals("offline"))
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

		if (!cpu1CurFreq.equals("offline"))
		{
			cpu1prog.setText(cpu1CurFreq.substring(0, cpu1CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu1prog.setText(getResources().getString(R.string.offline));
		}

		progCpu1.setMax(frequencies.indexOf(cpu1MaxFreq.trim()) + 1);
		progCpu1.setProgress(frequencies.indexOf(cpu1CurFreq.trim()) + 1);


	}

	private final void updateCpu2()
	{

		if (!cpu2CurFreq.equals("offline"))
		{
			cpu2prog.setText(cpu2CurFreq.substring(0, cpu2CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu2prog.setText(getResources().getString(R.string.offline));
		}

		progCpu2.setMax(frequencies.indexOf(cpu2MaxFreq.trim()) + 1);
		progCpu2.setProgress(frequencies.indexOf(cpu2CurFreq.trim()) + 1);


	}

	private final void updateCpu3()
	{

		if (!cpu3CurFreq.equals("offline"))
		{
			cpu3prog.setText(cpu3CurFreq.substring(0, cpu3CurFreq.length() - 3) + "Mhz");
		}
		else
		{
			cpu3prog.setText(getResources().getString(R.string.offline));
		}

		progCpu3.setMax(frequencies.indexOf(cpu3MaxFreq.trim()) + 1);
		progCpu3.setProgress(frequencies.indexOf(cpu3CurFreq.trim()) + 1);


	}

	private final void populateGovernorSpinners()
	{
		
		ArrayAdapter<String> gov0spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CPUInfo.governors());
		
		gov0spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gov0spinner.setAdapter(gov0spinnerArrayAdapter);

		
		int gov0spinnerPosition = gov0spinnerArrayAdapter.getPosition(CPUInfo.cpu0CurGov());
		gov0spinner.setSelection(gov0spinnerPosition);

		gov0spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new ChangeGovernor(CPUActivityOld.this).execute(new String[] {"cpu0",parent.getItemAtPosition(pos).toString()});
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

			int gov1spinnerPosition = gov1spinnerArrayAdapter.getPosition(CPUInfo.cpu1CurGov());
			gov1spinner.setSelection(gov1spinnerPosition);

			gov1spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivityOld.this).execute(new String[] {"cpu1",parent.getItemAtPosition(pos).toString()});

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

			int gov2spinnerPosition = gov2spinnerArrayAdapter.getPosition(CPUInfo.cpu2CurGov());
			gov2spinner.setSelection(gov2spinnerPosition);

			gov2spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivityOld.this).execute(new String[] {"cpu2",parent.getItemAtPosition(pos).toString()});

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

			int gov3spinnerPosition = gov3spinnerArrayAdapter.getPosition(CPUInfo.cpu3CurGov());
			gov3spinner.setSelection(gov3spinnerPosition);

			gov3spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						new ChangeGovernor(CPUActivityOld.this).execute(new String[] {"cpu3",parent.getItemAtPosition(pos).toString()});


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
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}

}
