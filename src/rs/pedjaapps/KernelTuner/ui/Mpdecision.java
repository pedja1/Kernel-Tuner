package rs.pedjaapps.KernelTuner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.CompoundButton.*;
import com.actionbarsherlock.app.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import de.ankri.views.Switch;
import java.lang.Process;
import rs.pedjaapps.KernelTuner.R;

public class Mpdecision extends SherlockActivity
{

	
	private List<CPUInfo.FreqsEntry> freqEntries;
	private List<Integer> freqs = new ArrayList<Integer>();
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

			
			 try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();

		            stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_freq\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/idle_freq\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n").getBytes());
					stdin.write(("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n").getBytes());

					stdin.write(("echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n").getBytes());
					stdin.write(("echo " + onoff + " > /sys/kernel/msm_mpdecision/conf/scroff_profile\n").getBytes());
					stdin.write(("echo " + delaynew + " > /sys/kernel/msm_mpdecision/conf/delay\n").getBytes());
					stdin.write(("echo " + pausenew + " > /sys/kernel/msm_mpdecision/conf/pause\n").getBytes());
					stdin.write(("echo " + thruploadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n").getBytes());
					stdin.write(("echo " + thrdownloadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n").getBytes());
					stdin.write(("echo " + thrupmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n").getBytes());
					stdin.write(("echo " + thrdownmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n").getBytes());
					stdin.write(("echo " + idleNew + " > /sys/kernel/msm_mpdecision/conf/idle_freq\n").getBytes());
					stdin.write(("echo " + scroffNew + " > /sys/kernel/msm_mpdecision/conf/scroff_freq\n").getBytes());
					stdin.write(("echo " + scroff_singleNew + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core\n").getBytes());
					 
		            
		            stdin.flush();

		            stdin.close();
		            BufferedReader brCleanUp =
		                    new BufferedReader(new InputStreamReader(stdout));
		            while ((line = brCleanUp.readLine()) != null) {
		                Log.d("[KernelTuner ChangeGovernor Output]", line);
		            }
		            brCleanUp.close();
		            brCleanUp =
		                    new BufferedReader(new InputStreamReader(stderr));
		            while ((line = brCleanUp.readLine()) != null) {
		            	Log.e("[KernelTuner ChangeGovernor Error]", line);
		            }
		            brCleanUp.close();

		        } catch (IOException ex) {
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
		
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.SwitchCompatAndSherlockLight);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.SwitchCompatAndSherlock);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.SwitchCompatAndSherlockLightDark);
			
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mpdecision);
		
		
		mp_switch = (Switch)findViewById(R.id.mp_switch);
		idleSpinner =(Spinner)findViewById(R.id.spinner1);
		scroffSpinner =(Spinner)findViewById(R.id.spinner2);
		freqEntries = CPUInfo.frequencies();
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqs.add(f.getFreq());
		}
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		boolean ads = preferences.getBoolean("ads", true);
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


	private final void setCheckBoxes()
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
					scroffNew = freqs.get(pos)+1;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int scroffPosition = freqsArrayAdapter.getPosition(freqNames.get(freqs.indexOf(Integer.parseInt(scroff))));
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
					idleNew = freqs.get(pos)+1;
					System.out.println(idleNew);

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int idlePosition = freqsArrayAdapter.getPosition(freqNames.get(freqs.indexOf(Integer.parseInt(idle))));
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




	private final void readMpdec()
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
			fIn.close();
		}
		catch (Exception e)
		{
			delay = "err";
			EditText ed=(EditText)findViewById(R.id.ed1);

			ed.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			pause = "err";
			EditText ed=(EditText)findViewById(R.id.ed2);

			ed.setEnabled(false);

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
			fIn.close();
		}
		catch (Exception e)
		{
			thrupload = "err";
			EditText ed=(EditText)findViewById(R.id.ed3);

			ed.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			thrupms = "err";
			EditText ed=(EditText)findViewById(R.id.ed4);

			ed.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			thrdownms = "err";
			EditText ed=(EditText)findViewById(R.id.ed5);

			ed.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			thrdownload = "err";
			EditText ed=(EditText)findViewById(R.id.ed6);

			ed.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			idle = "err";
			

			idleSpinner.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			scroff= "err";
			

			scroffSpinner.setEnabled(false);
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
			fIn.close();
		}
		catch (Exception e)
		{
			scroff_single = "err";
			

			mp_switch.setEnabled(false);
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
	
	private final void apply(){
		Mpdecision.this.pd = ProgressDialog.show(Mpdecision.this, null, getResources().getString(R.string.applying_settings), true, true);
		readEditTexts();
		new apply().execute();
	}

}
