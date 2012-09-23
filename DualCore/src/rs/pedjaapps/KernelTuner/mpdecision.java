package rs.pedjaapps.KernelTuner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import java.util.List;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import rs.pedjaapps.KernelTuner.R;
import android.widget.*; 

public class mpdecision extends Activity {

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



private class apply extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");

         
         
         Process localProcess;
   		try {
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
   		} catch (IOException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			} catch (InterruptedException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			}
         
         return "";
     }

     protected void onPostExecute(Object result) {
         // Pass the result data back to the main activity
    	 
         mpdecision.this.data = result;
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
         mpdecision.this.pd.dismiss();
         
     }

	}

  


public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

iscWindow3();


}


public void onPause() {
    super.onPause();
}


protected void onResume()
{
    
    
    super.onResume();
    
}



public void iscWindow3(){
setContentView(R.layout.mpdecision);
this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 




	
	Button apply = (Button)findViewById(R.id.button1);
	apply.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			mpdecision.this.pd = ProgressDialog.show(mpdecision.this, "Working..", "Applying settings...", true, false);
			readEditTexts();
			new apply().execute();
		    
		}
	});

	/*final CheckBox mpdecscroff = (CheckBox)findViewById(R.id.checkBox4);
	mpdecscroff.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			if (mpdecscroff.isChecked()){
				mpscroff= "1";
				
				
			}
			else if(!mpdecscroff.isChecked()){
				mpscroff= "0";
				
				
			}
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("mpdecisionscroff", mpscroff);
	  	  // value to store
	  	    editor.commit();
		    
		}
	});
	
	final CheckBox govfreqonoff = (CheckBox)findViewById(R.id.checkBox1);
	govfreqonoff.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			if (govfreqonoff.isChecked()){
				onoff= "1";
				TextView tv1 = (TextView)findViewById(R.id.textView1);
		         TextView tv2 = (TextView)findViewById(R.id.textView2);
		         Spinner sp1 = (Spinner)findViewById(R.id.spinner2);
		         Spinner sp2 = (Spinner)findViewById(R.id.spinner3);
		         sp1.setVisibility(View.VISIBLE);
		         sp2.setVisibility(View.VISIBLE);
		         tv1.setVisibility(View.VISIBLE);
		         tv2.setVisibility(View.VISIBLE);
				
			}
			else if(!govfreqonoff.isChecked()){
				onoff= "0";
				
				 TextView tv1 = (TextView)findViewById(R.id.textView1);
		         TextView tv2 = (TextView)findViewById(R.id.textView2);
		         Spinner sp1 = (Spinner)findViewById(R.id.spinner2);
		         Spinner sp2 = (Spinner)findViewById(R.id.spinner3);
		         sp1.setVisibility(View.GONE);
		         sp2.setVisibility(View.GONE);
		         tv1.setVisibility(View.GONE);
		         tv2.setVisibility(View.GONE);
			}
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("atwgovfreqonoff", onoff);
	  	  // value to store
	  	    editor.commit();
		    
		}
	});*/
	

//readMpdecisionStatus();
//readFreqs();
//readIdleFreq();
	readMpdec();
setCheckBoxes();
	//readGovs();
	//createSpinnerGovs();
	//createSpinnerfreq();

	//createSpinnerIdleFreq();

	
}





public void setCheckBoxes(){
	

	
       
    /*CheckBox mpdidle = (CheckBox)findViewById(R.id.checkBox4);
    if(mpdecisionidle.equals("1")){
    	mpdidle.setChecked(true);
    }
    else if (mpdecisionidle.equals("0")){
    	mpdidle.setChecked(false);
    }
    else{
    	mpdidle.setEnabled(false);
    }
    
    
    
    CheckBox profile = (CheckBox)findViewById(R.id.checkBox1);
    
    if (profile.isChecked()){
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
         TextView tv2 = (TextView)findViewById(R.id.textView2);
         Spinner sp1 = (Spinner)findViewById(R.id.spinner2);
         Spinner sp2 = (Spinner)findViewById(R.id.spinner3);
         sp1.setVisibility(View.VISIBLE);
         sp2.setVisibility(View.VISIBLE);
         tv1.setVisibility(View.VISIBLE);
         tv2.setVisibility(View.VISIBLE);
		
	}
	else if(!profile.isChecked()){
		
		
		 TextView tv1 = (TextView)findViewById(R.id.textView1);
         TextView tv2 = (TextView)findViewById(R.id.textView2);
         Spinner sp1 = (Spinner)findViewById(R.id.spinner2);
         Spinner sp2 = (Spinner)findViewById(R.id.spinner3);
         sp1.setVisibility(View.GONE);
         sp2.setVisibility(View.GONE);
         tv1.setVisibility(View.GONE);
         tv2.setVisibility(View.GONE);
	}
    if(scroff_profile.equals("1")){
    	profile.setChecked(true);
    }
    else if (scroff_profile.equals("0")){
    	profile.setChecked(false);
    }
    else{
    	profile.setEnabled(false);
    }*/
	
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

public void readEditTexts(){
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
	
	//System.out.println(delaynew);
	
}

/*public void createSpinnerIdleFreq(){
	String[] MyStringAray = idlefreqs.split("\\s");
	
	final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
	spinner.setAdapter(spinnerArrayAdapter);
	
	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	freqselected = parent.getItemAtPosition(pos).toString();
	    	if (curentidlefreq=="notfound"){
	    		spinner.setEnabled(false);
	    	}
	    }

	    
	    public void onNothingSelected(AdapterView<?> parent) {
	        //do nothing
	    }
	});

	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

	int spinnerPosition = myAdap.getPosition(curentidlefreq);

	//set the default according to value
	spinner.setSelection(spinnerPosition);
	
}

	public void createSpinnerGovs(){
		String[] MyStringAray = govs.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					govselected = parent.getItemAtPosition(pos).toString();
					if (currentscroffgov=="notfound"){
						spinner.setEnabled(false);
					}
				}

				
				public void onNothingSelected(AdapterView<?> parent) {
					//do nothing
				}
			});

		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(currentscroffgov);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}

	public void createSpinnerfreq(){
		String[] MyStringAray = idlefreqs.split("\\s");

		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						maxfreqselected = parent.getItemAtPosition(pos).toString();
				    	if (currentscrofffreq=="notfound"){
							spinner.setEnabled(false);
						}
				}

				
				public void onNothingSelected(AdapterView<?> parent) {
					//do nothing
				}
			});

		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(currentscrofffreq);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}
	*/
	
/*public void readFreqs(){
	try {
			
			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
			FileInputStream fIn = new FileInputStream(myFile);
			
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			idlefreqs = aBuffer;
			myReader.close();
						
		} catch (Exception e) {
			
			
		}
}

	public void readGovs(){
		try {

			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			govs = aBuffer;
			myReader.close();

		} catch (Exception e) {


		}
	}

public void readIdleFreq(){
	try {
			
			File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_idle_freq");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			curentidlefreq = aBuffer.trim();
			myReader.close();
						
		} catch (Exception e) {
			curentidlefreq = "notfound";
			
		}
}
*/


/*public void readMpdecisionStatus(){


try {
	String aBuffer = "";
 			File myFile = new File("/sys/kernel/msm_mpdecision/conf/do_scroff_single_core");
 			FileInputStream fIn = new FileInputStream(myFile);
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			mpdecisionidle = aBuffer.trim();
 			myReader.close();
 	
 		} catch (Exception e) {
 
 		}


	}*/


   public void readMpdec(){
		try {

			File myFile = new File("/sys/kernel/msm_mpdecision/conf/delay");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			delay = aBuffer;
			myReader.close();

		} catch (Exception e) {
			delay="err";
			EditText ed=(EditText)findViewById(R.id.ed1);
			
			ed.setFocusable(false);
		}
		
		
	   try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/pause");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   pause = aBuffer;
		   myReader.close();

	   } catch (Exception e) {
		   pause = "err";
		   EditText ed=(EditText)findViewById(R.id.ed2);
		
			ed.setFocusable(false);

	   }
	   
	      try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_up");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   thrupload = aBuffer;
		   myReader.close();

	   } catch (Exception e) {
		   thrupload="err";
		   EditText ed=(EditText)findViewById(R.id.ed3);
		  
			ed.setFocusable(false);
	   }
	   
	     try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_up");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   thrupms = aBuffer;
		   myReader.close();

	   } catch (Exception e) {
		   thrupms="err";
		   EditText ed=(EditText)findViewById(R.id.ed4);
		   
			ed.setFocusable(false);
	   }
	   
	    try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_down");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   thrdownms = aBuffer;
		   myReader.close();

	   } catch (Exception e) {
		   thrdownms="err";
		   EditText ed=(EditText)findViewById(R.id.ed5);
		   
			ed.setFocusable(false);
	   }
	   
	   try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_down");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   thrdownload = aBuffer;
		   myReader.close();

	   } catch (Exception e) {
		   thrdownload="err";
		   EditText ed=(EditText)findViewById(R.id.ed6);
		  
			ed.setFocusable(false);
	   }
	   
	   /*try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_scroff_gov");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   currentscroffgov = aBuffer;
		   myReader.close();

	   } catch (Exception e) {

		   currentscroffgov="notfound";
	   }*/
	   
	  /* try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/mpdec_scroff_freq");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   currentscrofffreq = aBuffer;
		   myReader.close();

	   } catch (Exception e) {

		   currentscrofffreq="notfound";
	   }
	   
	   try {

		   File myFile = new File("/sys/kernel/msm_mpdecision/conf/scroff_profile");
		   FileInputStream fIn = new FileInputStream(myFile);

		   BufferedReader myReader = new BufferedReader(
			   new InputStreamReader(fIn));
		   String aDataRow = "";
		   String aBuffer = "";
		   while ((aDataRow = myReader.readLine()) != null) {
			   aBuffer += aDataRow + "\n";
		   }

		   scroff_profile = aBuffer;
		   myReader.close();

	   } catch (Exception e) {

		   scroff_profile="notfound";
	   }*/
	}
	
}
