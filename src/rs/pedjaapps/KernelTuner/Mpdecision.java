package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;

import java.lang.Process; 

public class Mpdecision extends Activity
{

	public String iscVa = "";
	public String iscVa2 = "offline";
	public String governors;
	public String governorscpu1;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
	public String led;
	SeekBar mSeekBar;
	TextView progresstext;
	public String cpu0freqs;
	public String cpu1freqs;
	public String cpu0max;
	public String cpu1max;
	public int countcpu0;
	public int countcpu1;
	public String fastcharge = " ";
	public String vsync = " ";
	public String fc = " ";
	public String mpdecision = " ";
	public String mpdecisionidle =" ";
	public String vs;
	public String hw;
	public String backbuf;
	public String idlefreqs;
	public String freqselected;
	public String curentidlefreq;
	public String mp;
	public String mpscroff;
	public String cdepth = " ";
	public List<String> cpu0freqslist;
	public List<String> cpu1freqslist;
	public SharedPreferences preferences;
	ProgressBar prog;
	public String delay;
	public String pause;
	public String thrupload;
	public String thrupms;
	public String thrdownload;
	public String thrdownms;

	public String delaynew;
	public String pausenew;
	public String thruploadnew;
	public String thrupmsnew;
	public String thrdownloadnew;
	public String thrdownmsnew;

	public String govs;
	public String currentscrofffreq;
	public String currentscroffgov;
	public String govselected;
	public String maxfreqselected;
	public String onoff;
	public String scroff_profile;

	private ProgressDialog pd = null;
	private Object data = null;

	Handler mHandler = new Handler();


//EndOfGlobalVariables



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
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_scroff_gov\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_scroff_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/dealy\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/pause\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_profile\n");

				localDataOutputStream.writeBytes("echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/do_scroff_single_core\n");
				localDataOutputStream.writeBytes("echo " + onoff + " > /sys/kernel/msm_mpdecision/conf/scroff_profile\n");
				localDataOutputStream.writeBytes("echo " + delaynew + " > /sys/kernel/msm_mpdecision/conf/delay\n");
				localDataOutputStream.writeBytes("echo " + pausenew + " > /sys/kernel/msm_mpdecision/conf/pause\n");
				localDataOutputStream.writeBytes("echo " + thruploadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				localDataOutputStream.writeBytes("echo " + thrdownloadnew + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");
				localDataOutputStream.writeBytes("echo " + thrupmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n");
				localDataOutputStream.writeBytes("echo " + thrdownmsnew + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n");
				/*   localDataOutputStream.writeBytes("echo " + freqselected + " > /sys/kernel/msm_mpdecision/conf/mpdec_idlefreq\n");
				 localDataOutputStream.writeBytes("echo " + maxfreqselected + " > /sys/kernel/msm_mpdecision/conf/mpdec_scroff_freq\n");
				 localDataOutputStream.writeBytes("echo " + govselected + " > /sys/kernel/msm_mpdecision/conf/mpdec_scroff_gov\n");
				 */ 
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

			Mpdecision.this.data = result;
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("onoff", onoff);
			editor.putString("delaynew", delaynew);
			editor.putString("pausenew", pausenew);
			editor.putString("thruploadnew", thruploadnew);
			editor.putString("thrdownloadnew", thrdownloadnew);
			editor.putString("thrupmsnew", thrupmsnew);
			editor.putString("thrdownmsnew", thrdownmsnew);
			/*  editor.putString("idlefreq", freqselected);
			 editor.putString("maxfreqselected", maxfreqselected);
			 editor.putString("govselected", govselected);*/
			editor.commit();
			Mpdecision.this.pd.dismiss();

		}

	}



	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.mpdecision);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 





		Button apply = (Button)findViewById(R.id.button1);
		apply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
					Mpdecision.this.pd = ProgressDialog.show(Mpdecision.this, "Working..", "Applying settings...", true, false);
					readEditTexts();
					new apply().execute();

				}
			});




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


	}

}
