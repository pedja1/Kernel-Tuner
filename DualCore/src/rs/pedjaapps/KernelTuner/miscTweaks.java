package rs.pedjaapps.KernelTuner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rs.pedjaapps.KernelTuner.R; 

public class miscTweaks extends Activity implements SeekBar.OnSeekBarChangeListener{

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
public String sdcache;
public String schedulers;
public String scheduler;
public int ledprogress;
public List<String> cpu0freqslist;
public List<String> cpu1freqslist;
public SharedPreferences preferences;
ProgressBar prog;
private ProgressDialog pd = null;
private Object data = null;
public String ldt;
public String ldtnew;

Handler mHandler = new Handler();


//EndOfGlobalVariables



private class colorDepth extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Backgrond thread starting");
         
        
     
         Process localProcess;
 		try {
				localProcess = Runtime.getRuntime().exec("su");
			
 		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
       localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n");
       localDataOutputStream.writeBytes("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n");
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
    	 preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("cdepth", cdepth);
	  	  // value to store
	  	    editor.commit();
         miscTweaks.this.data = result;
        
     }
	}

private class mountDebugFs extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
    
         
         Process localProcess;
  		try {
 				localProcess = Runtime.getRuntime().exec("su");
 			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
        localDataOutputStream.writeBytes("mount -t debugfs debugfs /sys/kernel/debug\n");
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
    	 
         miscTweaks.this.data = result;

         
             miscTweaks.this.pd.dismiss();
         
     }

	}

private class fastcharge extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
         Process localProcess;
  		try {
 				localProcess = Runtime.getRuntime().exec("su");
 			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
        localDataOutputStream.writeBytes("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n");
        localDataOutputStream.writeBytes("echo " + fc + " > /sys/kernel/fast_charge/force_fast_charge\n");
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
    	 
         miscTweaks.this.data = result;
         
     }

	}




private class vsync extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");

         
         Process localProcess;
    		try {
   				localProcess = Runtime.getRuntime().exec("su");
   			
    		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
          localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
          localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
          localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");
          localDataOutputStream.writeBytes("echo " + vs + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
          localDataOutputStream.writeBytes("echo " + hw + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
          localDataOutputStream.writeBytes("echo " + backbuf + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
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
    	 
         miscTweaks.this.data = result;       
     }

	}


private class changeled extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
         
         Process localProcess;
    		try {
   				localProcess = Runtime.getRuntime().exec("su");
   			
    		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
          localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
          localDataOutputStream.writeBytes("echo " + ledprogress + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n");
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
    	 preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("led", String.valueOf(ledprogress));
	  	  // value to store
	  	    editor.commit();
	  	    
	  	  System.out.println(ledprogress);
         miscTweaks.this.data = result;
  
     }

	}

	private class applyldt extends AsyncTask<String, Void, Object> {


		protected Object doInBackground(String... args) {
			Log.i("MyApp", "Background thread starting");

			Process localProcess;
    		try {
   				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n");
				localDataOutputStream.writeBytes("echo " + ldtnew + " > /sys/kernel/notification_leds/off_timer_multiplier\n");
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
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("ldt", String.valueOf(ldtnew));
			// value to store
	  	    editor.commit();

			
			miscTweaks.this.data = result;

		}

	}
	
	
private class applyIO extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");        
         Process localProcess;
 		try {
				localProcess = Runtime.getRuntime().exec("su");
			
 		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
       localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n");
       localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n");
	   localDataOutputStream.writeBytes("chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
       localDataOutputStream.writeBytes("echo " + sdcache + " > /sys/block/mmcblk1/queue/read_ahead_kb\n");
		localDataOutputStream.writeBytes("echo " + sdcache + " > /sys/block/mmcblk0/queue/read_ahead_kb\n");
	   localDataOutputStream.writeBytes("echo " + sdcache + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n");
       localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk0/queue/scheduler\n");
       localDataOutputStream.writeBytes("chmod 777 /sys/block/mmcblk1/queue/scheduler\n");
       localDataOutputStream.writeBytes("echo " + scheduler + " > /sys/block/mmcblk0/queue/scheduler\n");
       localDataOutputStream.writeBytes("echo " + scheduler + " > /sys/block/mmcblk1/queue/scheduler\n");
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
    	 preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("io", scheduler);
	  	    editor.putString("sdcache", sdcache);
	  	  // value to store
	  	    editor.commit();
         // Pass the result data back to the main activity
    	 
         miscTweaks.this.data = result;
         
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

protected void onStop()
{
	EditText sd = (EditText)findViewById(R.id.editText1); 
	sdcache = String.valueOf(sd.getText());
	new applyIO().execute();
    
	EditText ldttv = (EditText)findViewById(R.id.editText2); 
	RadioButton dva = (RadioButton)findViewById(R.id.radio2);
	if(dva.isChecked()){
		ldtnew=String.valueOf(ldttv.getText());
	}
	new applyldt().execute();
    super.onStop();
    
}



public void iscWindow3(){
setContentView(R.layout.misc_tweaks);


mSeekBar = (SeekBar)findViewById(R.id.seekBar1);
mSeekBar.setOnSeekBarChangeListener(this);

Button vsyncexplanation = (Button)findViewById(R.id.button1);
vsyncexplanation.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		AlertDialog alertDialog = new AlertDialog.Builder(
                miscTweaks.this).create();

// Setting Dialog Title
alertDialog.setTitle("Vsync");

// Setting Dialog Message
alertDialog.setMessage("VSYNC is when the GPU will lock refresh rate to that of the LCD screen. " +
		"Disabling this will create higher FPS rates, but on some hardware it can distort the display.");

// Setting Icon to Dialog
alertDialog.setIcon(R.drawable.icon);

// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        }
	    
	
	});

// Showing Alert Message
alertDialog.show();
		alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	
});

Button fastchargeexplanation = (Button)findViewById(R.id.button2);
fastchargeexplanation.setOnClickListener(new OnClickListener(){
	
	
	public void onClick(View v) {
		
		AlertDialog alertDialog = new AlertDialog.Builder(
                miscTweaks.this).create();

// Setting Dialog Title
alertDialog.setTitle("Fastcharge");

// Setting Dialog Message
alertDialog.setMessage("This option will forces AC charging mode when connected to a USB connection");

// Setting Icon to Dialog
alertDialog.setIcon(R.drawable.icon);

// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        }
	    
	
	});

// Showing Alert Message
alertDialog.show();
		alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	    
	
});

Button btminus = (Button)findViewById(R.id.progbutton2);
	btminus.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			mSeekBar.setProgress(mSeekBar.getProgress() - 5);
			new changeled().execute();
		    // Start a new thread that will download all the data
		    
		}
	});
	
	Button btplus = (Button)findViewById(R.id.progbutton3);
	btplus.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			mSeekBar.setProgress(mSeekBar.getProgress() + 5);
			new changeled().execute();
		    // Start a new thread that will download all the data
		    
		}
	});
	
	Button btminuscdepth = (Button)findViewById(R.id.button3);
	btminuscdepth.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			prog = (ProgressBar)findViewById(R.id.progressBar1);
			if(prog.getProgress()==2){
				cdepth= "24";
			}
			else if (prog.getProgress()==1){
				cdepth= "16";
			}
			
			prog.setProgress(prog.getProgress() - 1);
			new colorDepth().execute();
		    // Start a new thread that will download all the data
		    
		}
	});
	
	Button btpluscdepth = (Button)findViewById(R.id.button7);
	btpluscdepth.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			prog = (ProgressBar)findViewById(R.id.progressBar1);
			if(prog.getProgress()==0){
				cdepth= "24";
			}
			else if (prog.getProgress()==1){
				cdepth= "32";
			}
			prog.setProgress(prog.getProgress() + 1);
			new colorDepth().execute();
		    // Start a new thread that will download all the data
		    
		}
	});
	
	
	final CheckBox fastchargechbx = (CheckBox)findViewById(R.id.checkBox1);
	fastchargechbx.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			if (fastchargechbx.isChecked()){
				fc = "1";
				new fastcharge().execute();
			}
			else if(!fastchargechbx.isChecked()){
				fc = "0";
				new fastcharge().execute();
			}
			try{
				preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("fastcharge", fc);// value to store
	  	    editor.commit();
		} catch (Exception e) {
			//iscVa2 = "offline";
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		    
		}
	});
	
	final CheckBox vsynchbx = (CheckBox)findViewById(R.id.checkBox2);
	vsynchbx.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
			if (vsynchbx.isChecked()){
				vs = "1";
				hw = "1";
				backbuf = "3";
				new vsync().execute();
			}
			else if(!vsynchbx.isChecked()){
				vs = "0";
				hw = "0";
				backbuf = "4";
				new vsync().execute();
			}
			else{
				
			}
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
	  	    editor.putString("vsync", vs);
	  	  editor.putString("hw", hw);
	  	editor.putString("backbuf", backbuf);
	  	  // value to store
	  	    editor.commit();
		    
		}
	});
	
	
	
	
		
	File file = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
	try{

		InputStream fIn = new FileInputStream(file);


	}
	catch(FileNotFoundException e){
		this.pd = ProgressDialog.show(this, "Working..", "Mounting debug filesystem", true, false);
		new mountDebugFs().execute();
	}
		readSDCache();
		readIOScheduler();
		createSpinnerIO();
readleds();
readLDT();
readFastchargeStatus();
readVsyncStatus();
setCheckBoxes();


readColorDepth();
setColorDepth();

}

public void setCheckBoxes(){
	

	CheckBox fc = (CheckBox)findViewById(R.id.checkBox1);
	Button bt = (Button)findViewById(R.id.button2);
	TextView tv = (TextView)findViewById(R.id.textView1);
    if(fastcharge.equals("0")){
    	fc.setChecked(false);
    }
    else if (fastcharge.equals("1")){
    	fc.setChecked(true);
    }
    else{
    	fc.setVisibility(View.GONE);
		bt.setVisibility(View.GONE);
		tv.setVisibility(View.GONE);
		ImageView im = (ImageView)findViewById(R.id.imageView2);
		im.setVisibility(View.GONE);
    }
   
    
    CheckBox vs = (CheckBox)findViewById(R.id.checkBox2);
    Button bt2 = (Button)findViewById(R.id.button1);
	TextView tv2 = (TextView)findViewById(R.id.textView2);
    
    if(vsync.equals("1")){
    	vs.setChecked(true);
    }
    else if (vsync.equals("0")){
    	vs.setChecked(false);
    }
    else{
    	vs.setVisibility(View.GONE);
		bt2.setVisibility(View.GONE);
		tv2.setVisibility(View.GONE);
		ImageView im = (ImageView)findViewById(R.id.imageView3);
		im.setVisibility(View.GONE);
    }
	if (!sdcache.equals("err")){
      EditText sd = (EditText)findViewById(R.id.editText1); 
      sd.setText(sdcache);
   }
	else{
		EditText sd = (EditText)findViewById(R.id.editText1);
		TextView sdtxt = (TextView)findViewById(R.id.textView11);
		sd.setVisibility(View.GONE);
		sdtxt.setVisibility(View.GONE);
	}
	
	RadioGroup ldtradio = (RadioGroup)findViewById(R.id.radioGroup1);
	//int selected = ldtradio.getCheckedRadioButtonId();
final EditText et = (EditText)findViewById(R.id.editText2);
TextView ldttitle = (TextView)findViewById(R.id.textView12);
	RadioButton nula = (RadioButton)findViewById(R.id.radio0);
	RadioButton jedan = (RadioButton)findViewById(R.id.radio1);
	RadioButton dva = (RadioButton)findViewById(R.id.radio2);
	nula.setOnClickListener(new OnClickListener(){

			public void onClick(View p1)
			{
				et.setVisibility(View.GONE);
				ldtnew="0";
			}

	
	});
	
	jedan.setOnClickListener(new OnClickListener(){

			public void onClick(View p1)
			{
				et.setVisibility(View.GONE);
				ldtnew="1";
			}


		});
		
	dva.setOnClickListener(new OnClickListener(){

			public void onClick(View p1)
			{
				et.setVisibility(View.VISIBLE);
			}


		});
	File file = new File("/sys/kernel/notification_leds/off_timer_multiplier");


	int ldtint;
	try{

		InputStream fIn = new FileInputStream(file);
	/*	try{
		ldtint = Integer.parseInt(ldt);
		}
		catch(Exception e){
			ldtint=500;
		}*/
		if (ldt.equals("Infinite")){
			nula.setChecked(true);
		}
		else if(ldt.equals("As requested by process")){
			jedan.setChecked(true);
		}
		else{
			dva.setChecked(true);
			et.setText(ldt);
		}
		if(dva.isChecked()){
			et.setVisibility(View.VISIBLE);
		}
		else{
		et.setVisibility(View.GONE);
		}

	}
	catch(FileNotFoundException e){
		ldtradio.setVisibility(View.GONE);
		ldttitle.setVisibility(View.GONE);
		et.setVisibility(View.GONE);
		
	}
	

	
	
	
	
}

	

public void setColorDepth(){
	prog = (ProgressBar)findViewById(R.id.progressBar1);
	if (cdepth.equals("16")){
		prog.setProgress(0);
	}
	else if (cdepth.equals("24")){
		prog.setProgress(1);
	}
	else if (cdepth.equals("32")){
		prog.setProgress(2);
	}
	else{
		Button btpluscdepth = (Button)findViewById(R.id.button7);
		Button btminuscdepth = (Button)findViewById(R.id.button3);
		TextView tv = (TextView)findViewById(R.id.textView5);
		TextView tv2 = (TextView)findViewById(R.id.textView6);
		TextView tv3 = (TextView)findViewById(R.id.textView7);
		TextView tv4 = (TextView)findViewById(R.id.textView8);
		ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
		ImageView im = (ImageView)findViewById(R.id.imageView4);
		btpluscdepth.setVisibility(View.GONE);
		pb.setVisibility(View.GONE);
		btminuscdepth.setVisibility(View.GONE);
		tv2.setVisibility(View.GONE);
		tv3.setVisibility(View.GONE);
		tv4.setVisibility(View.GONE);
		
		tv.setVisibility(View.GONE);
		im.setVisibility(View.GONE);
		
	}
}

public void readLDT(){
	try {
			
			File myFile = new File("/sys/kernel/notification_leds/off_timer_multiplier");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			ldt = aBuffer.trim();
			myReader.close();
					
		} catch (Exception e) {
			
			ldt="266";
		}
}

public void readColorDepth(){
	try {
			
			File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			cdepth = aBuffer.trim();
			myReader.close();
					
		} catch (Exception e) {
			
			
		}
}

public void readSDCache(){
	try {
			
			File myFile = new File("/sys/devices/virtual/bdi/179:0/read_ahead_kb");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			sdcache = aBuffer.trim();
			myReader.close();
					
		} catch (Exception e) {
			sdcache = "err";
			
		}
}

public void createSpinnerIO(){
	String[] MyStringAray = schedulers.split("\\s");
	
	final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
	spinner.setAdapter(spinnerArrayAdapter);
	
	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	scheduler = parent.getItemAtPosition(pos).toString();
	    	if (scheduler=="err"){
	    		Spinner spinner = (Spinner)findViewById(R.id.spinner1);
	    		TextView iotxt = (TextView)findViewById(R.id.textView10);
	    		spinner.setVisibility(View.GONE);
	    		iotxt.setVisibility(View.GONE);
	    	}
	    }

	    
	    public void onNothingSelected(AdapterView<?> parent) {
	        //do nothing
	    }
	});

	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

	int spinnerPosition = myAdap.getPosition(scheduler);

	//set the default according to value
	spinner.setSelection(spinnerPosition);
	
}

public void readIOScheduler(){
	try {
			
			File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
			FileInputStream fIn = new FileInputStream(myFile);
		
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			schedulers = aBuffer;
			myReader.close();
			/*String[] schedaray = schedulers.split(" ");
			int schedlength = schedaray.length;
			List<String> wordList = Arrays.asList(schedaray); 
			int index = wordList.indexOf(curentfreq);
			int index2 = wordList.indexOf(curentfreqcpu1);
				scheduler = */
			//String between = schedulers.split("]|[")[1];
			scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
			scheduler.trim();
			schedulers = schedulers.replace("[", "");
			schedulers = schedulers.replace("]", "");
			System.out.println(scheduler);
System.out.println(schedulers);

		} catch (Exception e) {
			schedulers = "err";
			scheduler="err";
			System.out.println(schedulers);
		}
	
	
}


public void readFastchargeStatus(){
try {
	String aBuffer = "";
 			File myFile = new File("/sys/kernel/fast_charge/force_fast_charge");
 			FileInputStream fIn = new FileInputStream(myFile);
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			fastcharge = aBuffer.trim();
 			myReader.close();
 	
 		} catch (Exception e) {
 
 		}

	}





public void readVsyncStatus(){
try {
	String aBuffer = "";
 			File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
 			FileInputStream fIn = new FileInputStream(myFile);
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			vsync = aBuffer.trim();
 			myReader.close();
 	
 		} catch (Exception e) {
 
 		}


	}

	public void readleds(){
try {
	String aBuffer = "";
 			File myFile = new File("/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
 			FileInputStream fIn = new FileInputStream(myFile);
 			BufferedReader myReader = new BufferedReader(
 					new InputStreamReader(fIn));
 			String aDataRow = "";
 			while ((aDataRow = myReader.readLine()) != null) {
 				aBuffer += aDataRow + "\n";
 			}

 			led = aBuffer.trim();
 			myReader.close();
 	
 		} catch (Exception e) {
 			led="err";
 		}
try{
	mSeekBar.setProgress(Integer.parseInt(led));
/*if (Integer.parseInt(led)<2){
	 mSeekBar.setProgress(0);
}
else if (Integer.parseInt(led)==2){
	 mSeekBar.setProgress(1);
}
else if (Integer.parseInt(led)>10){
	 mSeekBar.setProgress(2); //
}*/
	}
	 catch(Exception e){
//else if(led.equals("err")){
	mSeekBar.setVisibility(View.GONE);
	Button btminus = (Button)findViewById(R.id.progbutton2);
	Button btplus = (Button)findViewById(R.id.progbutton3);
	btminus.setVisibility(View.GONE);
	btplus.setVisibility(View.GONE);
	TextView sb = (TextView)findViewById(R.id.textView9);
	sb.setVisibility(View.GONE);
	TextView sb1 = (TextView)findViewById(R.id.progtextView1);
	sb1.setVisibility(View.GONE);
	ImageView im = (ImageView)findViewById(R.id.imageView1);
	im.setVisibility(View.GONE);
}
	}


public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
	// TODO Auto-generated method stub
	ledprogress = progress;
	TextView perc = (TextView)findViewById(R.id.progtextView1);
	perc.setText(ledprogress*100/60+"%");
	/*if (progress == 0){
		
    	new ledoff().execute();

	}
	else if (progress == 1){
    	new leddim().execute();
	}
	else if (progress == 2){
    	new ledfull().execute();
	}*/
	
}


public void onStartTrackingTouch(SeekBar arg0) {
	// TODO Auto-generated method stub	
}


public void onStopTrackingTouch(SeekBar arg0) {
	// TODO Auto-generated method stub
	ledprogress = mSeekBar.getProgress();
	System.out.println(ledprogress);
	new changeled().execute();
}

}
