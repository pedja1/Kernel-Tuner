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


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.ui.KernelTuner;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.tools.ChangeGovernor;
import rs.pedjaapps.KernelTuner.tools.FrequencyChanger;
import android.content.Context;

public class CPUActivityOld extends Activity
{

	private  List<IOHelper.FreqsEntry> freqEntries;
	private  List<String> frequencies = new ArrayList<String>();
	private  List<String> freqNames = new ArrayList<String>();
	private String cpu0MaxFreq ;
	private String cpu1MaxFreq ;
	private String cpu2MaxFreq ;
	private String cpu3MaxFreq ;
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


	private boolean cpu0Online;
	private boolean cpu1Online;
	private boolean cpu2Online;
	private boolean cpu3Online;

	private LinearLayout rlcpu1;
	private LinearLayout rlcpu2;
	private LinearLayout rlcpu3;

	
	

	private TextView cpu1govtxt;
	private TextView cpu2govtxt;
	private TextView cpu3govtxt;

	private ProgressDialog pd;	
	
	private SharedPreferences sharedPrefs;

	private ArrayAdapter<String> mhzAdapter;

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
			cpu0Online = IOHelper.cpu0Online();
			cpu1Online = IOHelper.cpu1Online();
			cpu2Online = IOHelper.cpu2Online();
			cpu3Online = IOHelper.cpu3Online();
			for(IOHelper.FreqsEntry f: freqEntries){
				frequencies.add(f.getFreq()+"");
			}
			for(IOHelper.FreqsEntry f: freqEntries){
				freqNames.add(f.getFreqName());
			}
			
			
			if (args[0] == true)
			{
				try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();
					stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors\n").getBytes());
					stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies\n").getBytes());
		            if (IOHelper.cpu1Online() == true)
					{
						stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
						stdin.write(("chmod 666 /sys/devices/system/cpu/cpu1/online\n").getBytes());
						stdin.write(("echo 1 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
						stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/online\n").getBytes());
						stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
					}
		            if (IOHelper.cpu2Online() == true)
					{
						stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
						stdin.write(("chmod 666 /sys/devices/system/cpu/cpu2/online\n").getBytes());
						stdin.write(("echo 1 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
						stdin.write(("chmod 444 /sys/devices/system/cpu/cpu2/online\n").getBytes());
						stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n").getBytes());

					}
		            if (IOHelper.cpu3Online() == true)
					{
						stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
						stdin.write(("chmod 666 /sys/devices/system/cpu/cpu3/online\n").getBytes());
						stdin.write(("echo 1 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
						stdin.write(("chmod 444 /sys/devices/system/cpu/cpu3/online\n").getBytes());
						stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq\n").getBytes());
						stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n").getBytes());
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
			cpu0MinFreq = IOHelper.cpu0MinFreq();
			cpu1MinFreq = IOHelper.cpu1MinFreq();
			cpu2MinFreq = IOHelper.cpu2MinFreq();
			cpu3MinFreq = IOHelper.cpu3MinFreq();
			cpu0MaxFreq = IOHelper.cpu0MaxFreq();
			cpu1MaxFreq = IOHelper.cpu1MaxFreq();
			cpu2MaxFreq = IOHelper.cpu2MaxFreq();
			cpu3MaxFreq = IOHelper.cpu3MaxFreq();
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
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
		
		final String theme = sharedPrefs.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.Theme_Sherlock_Light_Dialog_NoTitleBar);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.Theme_Sherlock_Dialog_NoTitleBar);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.Theme_Sherlock_Light_Dialog_NoTitleBar);
			
		}
		else if (theme.equals("miui_dark")) 
		{
			setTheme(R.style.Theme_Sherlock_Dialog_NoTitleBar);
		} 
		else if (theme.equals("sense5")) 
		{
			setTheme(R.style.Theme_Sherlock_Dialog_NoTitleBar);
		}
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cpu_tweaks_old);
		
		pd = ProgressDialog.show(c, null, 
				  getResources().getString(R.string.enabling_cpus), true, false);
		new ToggleCPUs().execute(new Boolean[] {true});
		rlcpu1 = (LinearLayout)findViewById(R.id.cpu1lay);
		rlcpu2 = (LinearLayout)findViewById(R.id.cpu2lay);
		rlcpu3 = (LinearLayout)findViewById(R.id.cpu3lay);

		cpu1govtxt = (TextView)findViewById(R.id.textView4);
		cpu2govtxt = (TextView)findViewById(R.id.textView3);
		cpu3govtxt = (TextView)findViewById(R.id.textView2);

		gov0spinner = (Spinner)findViewById(R.id.spinner3);
		gov1spinner = (Spinner)findViewById(R.id.bg);
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


	}
	@Override
	public void onResume()
	{
		
		super.onResume();
	}
	@Override
	public void onDestroy()
	{
		if(sharedPrefs.getBoolean("htc_one_workaround", false)==false){
		new ToggleCPUs().execute(new Boolean[] {false});
		}
		super.onDestroy();
	}

	@Override
	public void onStop()
	{
		
		super.onStop();
	}
	
	private final void updateUI()
	{
		if (cpu1Online == false)
		{
			rlcpu1.setVisibility(View.GONE);
			cpu1govtxt.setVisibility(View.GONE);
			gov1spinner.setVisibility(View.GONE);
			
		}
		if (cpu2Online == false)
		{
			rlcpu2.setVisibility(View.GONE);
			cpu2govtxt.setVisibility(View.GONE);
			gov2spinner.setVisibility(View.GONE);
		}
		if (cpu3Online == false)
		{
			rlcpu3.setVisibility(View.GONE);
			cpu3govtxt.setVisibility(View.GONE);
			gov3spinner.setVisibility(View.GONE);
		}
		
		 mhzAdapter = new ArrayAdapter<String>(c,   android.R.layout.simple_spinner_item, freqNames);
		mhzAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
	    cpu0Spinners();
		if(cpu1Online){
			cpu1Spinner();
		}
		if(cpu2Online){
			cpu2Spinner();
		}
		if(cpu3Online){
			cpu3Spinner();
		}
		populateGovernorSpinners();
	}

	private void cpu0Spinners(){
		cpu0MinSpinner.setAdapter(mhzAdapter);
		cpu0MaxSpinner.setAdapter(mhzAdapter);
		int cpu0MinPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu0MinFreq)));
		int cpu0MaxPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu0MaxFreq)));
		cpu0MinSpinner.setSelection(cpu0MinPosition);
		cpu0MaxSpinner.setSelection(cpu0MaxPosition);
		cpu0MinSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu0","min", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		cpu0MaxSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu0","max", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
	}
	
	private void cpu1Spinner(){
		cpu1MinSpinner.setAdapter(mhzAdapter);
		cpu1MaxSpinner.setAdapter(mhzAdapter);
		System.out.println(cpu1MinFreq+"df");
		int cpu1MinPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu1MinFreq)));
		int cpu1MaxPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu1MaxFreq)));
		cpu1MinSpinner.setSelection(cpu1MinPosition);
		cpu1MaxSpinner.setSelection(cpu1MaxPosition);
		cpu1MinSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu1","min", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		cpu1MaxSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu1","max", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});	
	}
	
	public void cpu2Spinner(){
		cpu2MinSpinner.setAdapter(mhzAdapter);
		cpu2MaxSpinner.setAdapter(mhzAdapter);
		int cpu2MinPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu2MinFreq)));
		int cpu2MaxPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu2MaxFreq)));
		cpu2MinSpinner.setSelection(cpu2MinPosition);
		cpu2MaxSpinner.setSelection(cpu2MaxPosition);
		cpu2MinSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu2","min", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		cpu2MaxSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu2","max", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});	
	}
	
	private void cpu3Spinner(){
		cpu3MinSpinner.setAdapter(mhzAdapter);
		cpu3MaxSpinner.setAdapter(mhzAdapter);
		int cpu3MinPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu3MinFreq)));
		int cpu3MaxPosition = mhzAdapter.getPosition(freqNames.get(frequencies.indexOf(cpu3MaxFreq)));
		cpu3MinSpinner.setSelection(cpu3MinPosition);
		cpu3MaxSpinner.setSelection(cpu3MaxPosition);
		cpu3MinSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu3","min", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		cpu3MaxSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					new FrequencyChanger(c).execute(new String[] {"cpu3","max", frequencies.get(pos)+""});
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});	
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
	public boolean onOptionsItemSelected(MenuItem item) {
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
