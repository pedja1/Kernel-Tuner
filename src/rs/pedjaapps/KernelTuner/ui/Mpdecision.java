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

import java.util.List;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.model.Frequency;
import rs.pedjaapps.KernelTuner.model.FrequencyCollection;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

public class Mpdecision extends Activity
{

	private List<Frequency> freqs     = FrequencyCollection.getInstance().getFrequencies();
	private String mpscroff;
	private SharedPreferences preferences;
	private String delay;
	private String pause;
	private String thrupload;
	private String thrupms;
	private String thrdownload;
	private String thrdownms;

	private String delaynew;
	private String pausenew;
	private String thruploadnew;
	private String thrupmsnew;
	private String thrdownloadnew;
	private String thrdownmsnew;
	
	private String idle;
	private String scroff;
	private String scroff_single;
	
	private int idleNew;
	private int scroffNew;
	private String scroff_singleNew;
	
	private Switch mp_switch;
	private Spinner idleSpinner;
	private Spinner scroffSpinner;

	private String onoff;

	private ProgressDialog pd = null;
	private class apply extends AsyncTask<String, Void, Object>
	{
		@Override
		protected Object doInBackground(String... args)
		{
			CommandCapture command = new CommandCapture(0,
		            "chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_freq",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/idle_freq",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/dealy",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/pause",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down",
					"chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down",

					"echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core",
					"echo " + onoff + " > /sys/kernel/msm_mpdecision/conf/scroff_profile",
					"echo " + delaynew + " > /sys/kernel/msm_mpdecision/conf/delay",
					"echo " + pausenew + " > /sys/kernel/msm_mpdecision/conf/pause",
					"echo " + thruploadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up",
					"echo " + thrdownloadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down",
					"echo " + thrupmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up",
					"echo " + thrdownmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down",
					"echo " + idleNew + " > /sys/kernel/msm_mpdecision/conf/idle_freq",
					"echo " + scroffNew + " > /sys/kernel/msm_mpdecision/conf/scroff_freq",
					"echo " + scroff_singleNew + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core");
			try{
				RootTools.getShell(true).add(command).waitForFinish();
			}
			catch(Exception e){

			}
					 
			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("onoff", onoff);
			editor.putString("delaynew", delaynew);
			editor.putString("pausenew", pausenew);
			editor.putString("thruploadnew", thruploadnew);
			editor.putString("thrdownloadnew", thrdownloadnew);
			editor.putString("thrupmsnew", thrupmsnew);
			editor.putString("thrdownmsnew", thrdownmsnew);
			editor.putString("idle_freq", idleNew+"");
			editor.putString("scroff", scroffNew+"");
			editor.putString("scroff_single", scroff_singleNew);
			editor.commit();
			Mpdecision.this.pd.dismiss();
			finish();

		}

	}



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mpdecision);
		
		
		mp_switch = (Switch)findViewById(R.id.mp_switch);
		idleSpinner =(Spinner)findViewById(R.id.bg);
		scroffSpinner =(Spinner)findViewById(R.id.spinner2);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		boolean ads = preferences.getBoolean("ads", true);
		if (ads)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		delay = IOHelper.mpDelay();
		pause = IOHelper.mpPause();
		thrupload = IOHelper.mpup();
		thrdownload = IOHelper.mpdown();
		thrupms = IOHelper.mpTimeUp();
		thrdownms = IOHelper.mpTimeDown();
		idle = IOHelper.mpIdleFreq();
		scroff = IOHelper.mpScroffFreq();
		scroff_single = IOHelper.mpScroffSingleCore();
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


	private void setCheckBoxes()
	{

		EditText del=(EditText)findViewById(R.id.ed1);
		if(delay.equals("err")){
			del.setEnabled(false);
		}
		del.setText(delay.trim());
		
		EditText paus=(EditText)findViewById(R.id.ed2);
		paus.setText(pause.trim());
		if(pause.equals("err")){
			paus.setEnabled(false);
		}

		EditText thruploadtext=(EditText)findViewById(R.id.ed3);
		thruploadtext.setText(thrupload.trim());
		if(thrupload.equals("err")){
			thruploadtext.setEnabled(false);
		}

		EditText thrupmstext=(EditText)findViewById(R.id.ed4);
		thrupmstext.setText(thrupms.trim());
        if(thrupms.equals("err")){
			thrupmstext.setEnabled(false);
		}
		
		EditText thrdownloadtext=(EditText)findViewById(R.id.ed5);
		thrdownloadtext.setText(thrdownload.trim());
        if(thrupload.equals("err")){
			thrdownloadtext.setEnabled(false);
		}
		
		EditText thrdownmstext=(EditText)findViewById(R.id.ed6);
		thrdownmstext.setText(thrdownms.trim());
		if(thrdownms.equals("err")){
			thrdownmstext.setEnabled(false);
		}
		
		if(idle.equals("err")){
			idleSpinner.setEnabled(false);
		}
		if(scroff.equals("err")){
			scroffSpinner.setEnabled(false);
		}
		
		if(scroff_single.equals("1")){
			mp_switch.setChecked(true);
		}
		else if(scroff_single.equals("0")){
			mp_switch.setChecked(false);
		}
		else{
			mp_switch.setEnabled(false);
		}
		mp_switch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					scroff_singleNew = "1";
				}
				else{
					scroff_singleNew = "0";
				}
				
			}
			
		});
		
		ArrayAdapter<Frequency> freqsArrayAdapter = new ArrayAdapter<Frequency>(this,   android.R.layout.simple_spinner_item, freqs);
		freqsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scroffSpinner.setAdapter(freqsArrayAdapter);

		scroffSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					scroffNew = freqs.get(pos).getFrequencyValue()+1;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int scroffPosition = freqsArrayAdapter.getPosition(freqs.get(freqs.indexOf(scroff)));
		scroffSpinner.setSelection(scroffPosition);
		}
		catch(Exception e){
			System.out.println("err"+e.getMessage());;
		}
		idleSpinner.setAdapter(freqsArrayAdapter);

		idleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					idleNew = freqs.get(pos).getFrequencyValue()+1;
					System.out.println(idleNew);

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int idlePosition = freqsArrayAdapter.getPosition(freqs.get(freqs.indexOf(idle)));
		idleSpinner.setSelection(idlePosition);
		}
		catch(Exception e){
			//idleSpinner.set
			System.out.println("err"+e.getMessage());
		}
		
	
	}

	private final void readEditTexts()
	{
		EditText del=(EditText)findViewById(R.id.ed1);
		EditText pause=(EditText)findViewById(R.id.ed2);
		EditText thruploadtext=(EditText)findViewById(R.id.ed3);
		EditText thrupmstext=(EditText)findViewById(R.id.ed4);
		EditText thrdownloadtext=(EditText)findViewById(R.id.ed5);
		EditText thrdownmstext=(EditText)findViewById(R.id.ed6);

		delaynew = del.getText().toString();
		pausenew = pause.getText().toString();
		thruploadnew = thruploadtext.getText().toString();
		thrupmsnew = thrupmstext.getText().toString();
		thrdownloadnew = thrdownloadtext.getText().toString();
		thrdownmsnew = thrdownmstext.getText().toString();



	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.misc_tweaks_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	
	private final void apply(){
		Mpdecision.this.pd = ProgressDialog.show(Mpdecision.this, null, getResources().getString(R.string.applying_settings), true, true);
		readEditTexts();
		new apply().execute();
	}

}
