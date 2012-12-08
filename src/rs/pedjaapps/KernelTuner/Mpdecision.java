package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import de.ankri.views.Switch;

public class Mpdecision extends SherlockActivity
{

	
	private List<CPUInfo.FreqsEntry> freqEntries = CPUInfo.frequencies();
	private List<String> freqs = new ArrayList<String>();
	private List<String> freqNames = new ArrayList<String>();
	
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
	
	private String idleNew;
	private String scroffNew;
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

			Process localProcess;
			try
			{
  				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/idle_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");

				localDataOutputStream.writeBytes("echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
				localDataOutputStream.writeBytes("echo " + onoff + " > /sys/kernel/msm_mpdecision/conf/scroff_profile\n");
				localDataOutputStream.writeBytes("echo " + delaynew + " > /sys/kernel/msm_mpdecision/conf/delay\n");
				localDataOutputStream.writeBytes("echo " + pausenew + " > /sys/kernel/msm_mpdecision/conf/pause\n");
				localDataOutputStream.writeBytes("echo " + thruploadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				localDataOutputStream.writeBytes("echo " + thrdownloadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				localDataOutputStream.writeBytes("echo " + thrupmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				localDataOutputStream.writeBytes("echo " + thrdownmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");
				localDataOutputStream.writeBytes("echo " + idleNew + " > /sys/kernel/msm_mpdecision/conf/idle_freq\n");
				localDataOutputStream.writeBytes("echo " + scroffNew + " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n");
				localDataOutputStream.writeBytes("echo " + scroff_singleNew + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n");
				 
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("Mpdecision: Changing mpdec");
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
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("onoff", onoff);
			editor.putString("delaynew", delaynew);
			editor.putString("pausenew", pausenew);
			editor.putString("thruploadnew", thruploadnew);
			editor.putString("thrdownloadnew", thrdownloadnew);
			editor.putString("thrupmsnew", thrupmsnew);
			editor.putString("thrdownmsnew", thrdownmsnew);
			editor.putString("idle_freq", idleNew);
			editor.putString("scroff", scroffNew);
			editor.putString("scroff_single", scroff_singleNew);
			editor.commit();
			Mpdecision.this.pd.dismiss();

		}

	}



	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.mpdecision);
		
		mp_switch = (Switch)findViewById(R.id.mp_switch);
		idleSpinner =(Spinner)findViewById(R.id.spinner1);
		scroffSpinner =(Spinner)findViewById(R.id.spinner2);
		
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqs.add(String.valueOf(f.getFreq()));
		}
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		readMpdec();
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


	public void setCheckBoxes()
	{


		EditText del=(EditText)findViewById(R.id.ed1);
		del.setText(delay.trim());

		EditText paus=(EditText)findViewById(R.id.ed2);
		paus.setText(pause.trim());

		EditText thruploadtext=(EditText)findViewById(R.id.ed3);
		thruploadtext.setText(thrupload.trim());

		EditText thrupmstext=(EditText)findViewById(R.id.ed4);
		thrupmstext.setText(thrupms.trim());

		EditText thrdownloadtext=(EditText)findViewById(R.id.ed5);
		thrdownloadtext.setText(thrdownload.trim());

		EditText thrdownmstext=(EditText)findViewById(R.id.ed6);
		thrdownmstext.setText(thrdownms.trim());
		
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
		
		ArrayAdapter<String> freqsArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqNames);
		freqsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scroffSpinner.setAdapter(freqsArrayAdapter);

		scroffSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					scroffNew = String.valueOf(freqs.get(pos));

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		
		int scroffPosition = freqsArrayAdapter.getPosition(freqNames.get(freqs.indexOf(scroff)));
		scroffSpinner.setSelection(scroffPosition);
		
		idleSpinner.setAdapter(freqsArrayAdapter);

		idleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					idleNew = String.valueOf(freqs.get(pos));

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		
		int idlePosition = freqsArrayAdapter.getPosition(freqNames.get(freqs.indexOf(idle)));
		idleSpinner.setSelection(idlePosition);

	
	}

	public void readEditTexts()
	{
		EditText del=(EditText)findViewById(R.id.ed1);
		EditText pause=(EditText)findViewById(R.id.ed2);
		EditText thruploadtext=(EditText)findViewById(R.id.ed3);
		EditText thrupmstext=(EditText)findViewById(R.id.ed4);
		EditText thrdownloadtext=(EditText)findViewById(R.id.ed5);
		EditText thrdownmstext=(EditText)findViewById(R.id.ed6);

		delaynew = String.valueOf(del.getText());
		pausenew = String.valueOf(pause.getText());
		thruploadnew = String.valueOf(thruploadtext.getText());
		thrupmsnew = String.valueOf(thrupmstext.getText());
		thrdownloadnew = String.valueOf(thrdownloadtext.getText());
		thrdownmsnew = String.valueOf(thrdownmstext.getText());



	}




	public void readMpdec()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/delay");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			delay = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			delay = "err";
			EditText ed=(EditText)findViewById(R.id.ed1);

			ed.setFocusable(false);
		}


		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/pause");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			pause = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			pause = "err";
			EditText ed=(EditText)findViewById(R.id.ed2);

			ed.setFocusable(false);

		}

		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_up");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			thrupload = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			thrupload = "err";
			EditText ed=(EditText)findViewById(R.id.ed3);

			ed.setFocusable(false);
		}

		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_up");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			thrupms = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			thrupms = "err";
			EditText ed=(EditText)findViewById(R.id.ed4);

			ed.setFocusable(false);
		}

	    try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_down");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			thrdownms = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			thrdownms = "err";
			EditText ed=(EditText)findViewById(R.id.ed5);

			ed.setFocusable(false);
		}

		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_down");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			thrdownload = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			thrdownload = "err";
			EditText ed=(EditText)findViewById(R.id.ed6);

			ed.setFocusable(false);
		}

		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/idle_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			idle = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{
			idle = "err";
			

			idleSpinner.setFocusable(false);
		}
		
		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/scroff_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			scroff = aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{
			scroff= "err";
			

			scroffSpinner.setFocusable(false);
		}
		
		try
		{

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/scroff_single_core");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			scroff_single= aBuffer.trim();
			myReader.close();

		}
		catch (Exception e)
		{
			scroff_single = "err";
			

			mp_switch.setFocusable(false);
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
		Mpdecision.this.pd = ProgressDialog.show(Mpdecision.this, null, getResources().getString(R.string.applying_settings), true, true);
		readEditTexts();
		new apply().execute();
	}

}
