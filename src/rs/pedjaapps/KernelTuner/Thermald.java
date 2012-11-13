package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.ads.*;
import java.io.*;
import rs.pedjaapps.KernelTuner.*;

import android.view.View.OnClickListener;
import java.lang.Process; 

public class Thermald extends Activity
{

	public String freqs;
	public String p1freq;
	public String p2freq;
	public String p3freq;
	public String p1freqnew;
	public String p2freqnew;
	public String p3freqnew;
	public String p1low;
	public String p1high;
	public String p2low;
	public String p2high;
	public String p3low;
	public String p3high;
	public String p1lownew;
	public String p1highnew;
	public String p2lownew;
	public String p2highnew;
	public String p3lownew;
	public String p3highnew;

	public SharedPreferences preferences;
	private ProgressDialog pd = null;
	private Object data = null;

	private class apply extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
			//Log.i("MyApp", "Background thread starting");



			Process localProcess;
	   		try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_low\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_high\n");


				localDataOutputStream.writeBytes("echo " + p1freqnew + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
				localDataOutputStream.writeBytes("echo " + p2freqnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
				localDataOutputStream.writeBytes("echo " + p3freqnew + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
				localDataOutputStream.writeBytes("echo " + p1lownew + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
				localDataOutputStream.writeBytes("echo " + p1highnew + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
				localDataOutputStream.writeBytes("echo " + p2lownew + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
				localDataOutputStream.writeBytes("echo " + p2highnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
				localDataOutputStream.writeBytes("echo " + p3lownew + " > /sys/kernel/msm_thermal/conf/allowed_max_low\n");
				localDataOutputStream.writeBytes("echo " + p3highnew + " > /sys/kernel/msm_thermal/conf/allowed_max_high\n");

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

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			// Pass the result data back to the main activity

			Thermald.this.data = result;
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
		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 


		setContentView(R.layout.thermald);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		readFreqs();
		readCurrentPhase1();
		readCurrentPhase2();
		readCurrentPhase3();
		readP1Low();
		readP1High();
		readP2Low();
		readP2High();
		readP3Low();
		readP3High();

		final EditText ed1 = (EditText)findViewById(R.id.editText1);
		final EditText ed2 = (EditText)findViewById(R.id.editText2);
		final EditText ed3 = (EditText)findViewById(R.id.editText3);
		final EditText ed4 = (EditText)findViewById(R.id.editText4);
		final EditText ed5 = (EditText)findViewById(R.id.editText5);
		final EditText ed6 = (EditText)findViewById(R.id.editText6);
		ed1.setText(p1low);
		ed2.setText(p1high);
		ed3.setText(p2low);
		ed4.setText(p2high);
		ed5.setText(p3low);
		ed6.setText(p3high);
		createSpinnerp1();
		createSpinnerp2();
		createSpinnerp3();

		Button apply = (Button)findViewById(R.id.button1);
		apply.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0)
				{
					Thermald.this.pd = ProgressDialog.show(Thermald.this, "Working..", "Applying settings...", true, false);
					p1lownew = String.valueOf(ed1.getText());
					p1highnew = String.valueOf(ed2.getText());
					p2lownew = String.valueOf(ed3.getText());
					p2highnew = String.valueOf(ed4.getText());
					p3lownew = String.valueOf(ed5.getText());
					p3highnew = String.valueOf(ed6.getText());
					new apply().execute();

				}

			});
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

	public void createSpinnerp1()
	{
		String[] MyStringAray = freqs.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p1freqnew = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(p1freq);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}

	public void createSpinnerp2()
	{
		String[] MyStringAray = freqs.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p2freqnew = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(p2freq);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}

	public void createSpinnerp3()
	{
		String[] MyStringAray = freqs.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					p3freqnew = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(p3freq);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}

	public void readFreqs()
	{
		try
		{

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			freqs = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{


		}
	}

	public void readCurrentPhase1()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readCurrentPhase2()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readCurrentPhase3()
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

		}
		catch (Exception e)
		{


		}
	}
	public void readP1Low()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readP1High()
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

		}
		catch (Exception e)
		{


		}
	}
	public void readP2Low()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readP2High()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readP3Low()
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

		}
		catch (Exception e)
		{


		}
	}

	public void readP3High()
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

		}
		catch (Exception e)
		{


		}
	}

}
	

