package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.slidingmenu.lib.SlidingMenu;

public class Thermald extends SherlockActivity
{

	private List<CPUInfo.FreqsEntry> freqEntries = CPUInfo.frequencies();
	private List<String> freqs = new ArrayList<String>();
	private List<String> freqNames = new ArrayList<String>();
	private String p1freq;
	private String p2freq;
	private String p3freq;
	private String p1freqnew;
	private String p2freqnew;
	private String p3freqnew;
	private String p1low;
	private String p1high;
	private String p2low;
	private String p2high;
	private String p3low;
	private String p3high;
	private String p1lownew;
	private String p1highnew;
	private String p2lownew;
	private String p2highnew;
	private String p3lownew;
	private String p3highnew;
	
	private EditText ed1;
	private EditText ed2;
	private EditText ed3;
	private EditText ed4;
	private EditText ed5;
	private EditText ed6;
	

	private SharedPreferences preferences;
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

	            stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_low\n").getBytes());
				stdin.write(("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_high\n").getBytes());


				stdin.write(("echo " + p1freqnew + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n").getBytes());
				stdin.write(("echo " + p2freqnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n").getBytes());
				stdin.write(("echo " + p3freqnew + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n").getBytes());
				stdin.write(("echo " + p1lownew + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n").getBytes());
				stdin.write(("echo " + p1highnew + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n").getBytes());
				stdin.write(("echo " + p2lownew + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n").getBytes());
				stdin.write(("echo " + p2highnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n").getBytes());
				stdin.write(("echo " + p3lownew + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n").getBytes());
				stdin.write(("echo " + p3highnew + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n").getBytes());

	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Thermal Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Thermal Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			// Pass the result data back to the main activity

			
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	 	    editor.putString("p1freq", p1freqnew);
	 	    editor.putString("p2freq", p2freqnew);
	 	    editor.putString("p3freq", p3freqnew);
	 	    editor.putString("p1low", p1lownew);
	 	    editor.putString("p1high", p1highnew);
	 	    editor.putString("p2low", p2lownew);
	 	    editor.putString("p2high", p2highnew);
			editor.putString("p3low", p3lownew);
		 	editor.putString("p3high", p3highnew);

	 	    editor.commit();
			Thermald.this.pd.dismiss();
			Thermald.this.finish();

		}

	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
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

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 


		setContentView(R.layout.thermald);
		
		final SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.side);
		
		GridView sideView = (GridView) menu.findViewById(R.id.grid);
		SideMenuAdapter sideAdapter = new SideMenuAdapter(this, R.layout.side_item);
		System.out.println("check "+sideView+" "+sideAdapter);
		sideView.setAdapter(sideAdapter);

		
		sideView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				List<SideMenuEntry> entries =  SideItems.getEntries();
				Intent intent = new Intent();
				intent.setClass(Thermald.this, entries.get(position).getActivity());
				startActivity(intent);
				menu.showContent();
			}
			
		});
		List<SideMenuEntry> entries =  SideItems.getEntries();
		for(SideMenuEntry e: entries){
			sideAdapter.add(e);
		}
		
		for(CPUInfo.FreqsEntry f: freqEntries){
			freqs.add(f.getFreq()+"");
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
		
		readCurrentPhase1();
		readCurrentPhase2();
		readCurrentPhase3();
		readP1Low();
		readP1High();
		readP2Low();
		readP2High();
		readP3Low();
		readP3High();

		  ed1 = (EditText)findViewById(R.id.editText1);
		  ed2 = (EditText)findViewById(R.id.editText2);
		 ed3 = (EditText)findViewById(R.id.editText3);
		ed4 = (EditText)findViewById(R.id.editText4);
		 ed5 = (EditText)findViewById(R.id.editText5);
		 ed6 = (EditText)findViewById(R.id.editText6);
		ed1.setText(p1low);
		ed2.setText(p1high);
		ed3.setText(p2low);
		ed4.setText(p2high);
		ed5.setText(p3low);
		ed6.setText(p3high);
		createSpinnerp1();
		createSpinnerp2();
		createSpinnerp3();

		
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

	private final void createSpinnerp1()
	{
		

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqNames);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p1freqnew = freqs.get(pos)+"";

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int spinnerPosition = spinnerArrayAdapter.getPosition(freqNames.get(freqs.indexOf(p1freq)));

		//set the default according to value
		spinner.setSelection(spinnerPosition);
	}
	catch(Exception e){
		//idleSpinner.set
		System.out.println("err"+e.getMessage());
	}
	}

	private final void createSpinnerp2()
	{
		

		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqNames);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p2freqnew = freqs.get(pos)+"";

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int spinnerPosition = spinnerArrayAdapter.getPosition(freqNames.get(freqs.indexOf(p2freq)));

		//set the default according to value
		spinner.setSelection(spinnerPosition);
	}
	catch(Exception e){
		//idleSpinner.set
		System.out.println("err"+e.getMessage());
	}
	}

	private final void createSpinnerp3()
	{


		final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqNames);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p3freqnew = freqs.get(pos)+"";

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		try{
		int spinnerPosition = spinnerArrayAdapter.getPosition(freqNames.get(freqs.indexOf(p3freq)));

		//set the default according to value
		spinner.setSelection(spinnerPosition);
	}
	catch(Exception e){
		//idleSpinner.set
		System.out.println("err"+e.getMessage());
	}
	}

	

	private final void readCurrentPhase1()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p1freq = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readCurrentPhase2()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p2freq = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readCurrentPhase3()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_freq");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p3freq = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}
	private final void readP1Low()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_low");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p1low = aBuffer.trim();
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readP1High()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_low_high");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p1high = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}
	private final void readP2Low()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_low");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p2low = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readP2High()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_mid_high");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p2high = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readP3Low()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_low");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p3low = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


		}
	}

	private final void readP3High()
	{
		try
		{

			File myFile = new File("/sys/kernel/msm_thermal/conf/allowed_max_high");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			p3high = aBuffer;
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{


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
		Thermald.this.pd = ProgressDialog.show(Thermald.this, null, getResources().getString(R.string.applying_settings), true, false);
		p1lownew = ed1.getText().toString();
		p1highnew = ed2.getText().toString();
		p2lownew = ed3.getText().toString();
		p2highnew = ed4.getText().toString();
		p3lownew = ed5.getText().toString();
		p3highnew = ed6.getText().toString();
		new apply().execute();
	}

}
	

