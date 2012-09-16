package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.*;
import java.util.*;

import rs.pedjaapps.KernelTuner.R;
import android.widget.*;

public class ProfileEditor extends Activity{

String freqs;
String govs;
String cpu0min;
String cpu0max; 
String cpu1min;
String cpu1max;
String cpu0gov;
String cpu1gov;
String gpu2d;
String gpu3d;
String vsync;
String number_of_cores;
String mtu;
String mtd;
String Name;
List<String> frequencies = new ArrayList<String>();
	public String[] delims;
    	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.profile_editor);
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

			freqs = aBuffer;
			myReader.close();
			    			
		} catch (Exception e) {
			try{
     			// Open the file that is the first 
     			// command line parameter
     			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
     			// Get the object of DataInputStream
     			DataInputStream in = new DataInputStream(fstream);
     			BufferedReader br = new BufferedReader(new InputStreamReader(in));
     			String strLine;
     			//Read File Line By Line
     			
     			while ((strLine = br.readLine()) != null)   {
     				
     				delims = strLine.split(" ");
     				String freq = delims[0];
     				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

     				frequencies.add(freq);

     			}
     			String[] strarray = frequencies.toArray(new String[0]);
     			StringBuilder builder = new StringBuilder();
     			for(String s : strarray) {
     			    builder.append(s);
     			    builder.append(" ");
     			}
     			freqs = builder.toString();


     			
     			in.close();
     		}catch (Exception e2){
     			System.err.println("Error: " + e.getMessage());
     		}			
		}
	
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
  setUI();
  
  Button done = (Button)findViewById(R.id.button1);
  done.setOnClickListener(new OnClickListener(){

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		EditText name = (EditText)findViewById(R.id.editText1);
		Name = String.valueOf(name.getText());
		 Intent intent = new Intent();
	     intent.putExtra("cpu0min", cpu0min);
	     intent.putExtra("cpu1min", cpu1min);
	     intent.putExtra("cpu0max", cpu0max);
	     intent.putExtra("cpu1max", cpu1max);
	     intent.putExtra("cpu0gov", cpu0gov);
	     intent.putExtra("cpu1gov", cpu1gov);
	     intent.putExtra("gpu2d", gpu2d);
	     intent.putExtra("gpu3d", gpu3d);
	     intent.putExtra("vsync", vsync);
	     intent.putExtra("noc", number_of_cores);
	     intent.putExtra("mtd", mtd);
	     intent.putExtra("mtu", mtu);
	     intent.putExtra("Name", Name);
	     setResult(RESULT_OK, intent);
	        finish();
		
	}
	  
  });
	}
    	
    	public void setUI()
    	{
    		Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
    		Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
    		Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
    		Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
    		Spinner spinner5 = (Spinner)findViewById(R.id.spinner5);
    		Spinner spinner6 = (Spinner)findViewById(R.id.spinner6);
    		Spinner spinner7 = (Spinner)findViewById(R.id.spinner7);
    		Spinner spinner8 = (Spinner)findViewById(R.id.spinner8);
    		Spinner spinner9 = (Spinner)findViewById(R.id.spinner9);
    		Spinner spinner10 = (Spinner)findViewById(R.id.spinner10);
    		Spinner spinner11 = (Spinner)findViewById(R.id.spinner11);
    		Spinner spinner12 = (Spinner)findViewById(R.id.spinner12);
    		
    		String[] freqsStringAray = freqs.split("\\s");
    		String[] govsStringAray = govs.split("\\s");
    		String[] gpu2darr = {"160000000", "200000000", "228571000", "266667000"};
    		String[] gpu3darr = {"200000000", "228857100", "266667000", "300000000", "320000000"};
    		List<String> numbers = new ArrayList<String>();
    		for (int i = 0; i <100; i++){
    			numbers.add(String.valueOf(i));
    		}
    		
    		
    		
        	/**spinner1*/
        	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqsStringAray);
        	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner1.setAdapter(spinnerArrayAdapter);
        	
        	spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu0min = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});

        /*	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

        	int spinnerPosition = myAdap.getPosition(curentgovernorcpu0);

        	//set the default according to value
        	spinner.setSelection(spinnerPosition);
        	//*/
        	/**spinner2*/
        	ArrayAdapter<String> spinner2ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqsStringAray);
        	spinner2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner2.setAdapter(spinner2ArrayAdapter);
        	
        	spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu0max = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	/**spinner3*/
        	ArrayAdapter<String> spinner3ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqsStringAray);
        	spinner3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner3.setAdapter(spinner3ArrayAdapter);
        	
        	spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu1min = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner4*/
        	ArrayAdapter<String> spinner4ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqsStringAray);
        	spinner4ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner4.setAdapter(spinner4ArrayAdapter);
        	
        	spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu1max = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner5*/
        	ArrayAdapter<String> spinner5ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govsStringAray);
        	spinner5ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner5.setAdapter(spinner5ArrayAdapter);
        	
        	spinner5.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu0gov = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner6*/
        	ArrayAdapter<String> spinner6ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govsStringAray);
        	spinner6ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner6.setAdapter(spinner6ArrayAdapter);
        	
        	spinner6.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	cpu1gov = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner7*/
        	ArrayAdapter<String> spinner7ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2darr);
        	spinner7ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner7.setAdapter(spinner7ArrayAdapter);
        	
        	spinner7.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	gpu2d = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner8*/
        	ArrayAdapter<String> spinner8ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu3darr);
        	spinner8ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner8.setAdapter(spinner8ArrayAdapter);
        	
        	spinner8.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	gpu3d = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner9*/
        	ArrayAdapter<String> spinner9ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, new String[] {"ON", "OFF"});
        	spinner9ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner9.setAdapter(spinner9ArrayAdapter);
        	
        	spinner9.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	if(parent.getItemAtPosition(pos).toString().equals("ON")){
        	    		vsync = "1";
        	    	}
        	    	else{
        	    		vsync = "0";
        	    	}
        	    	
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner10*/
        	ArrayAdapter<String> spinner10ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, new String[] {"1", "2"});
        	spinner10ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner10.setAdapter(spinner10ArrayAdapter);
        	
        	spinner10.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	number_of_cores = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner11*/
        	ArrayAdapter<String> spinner11ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, numbers);
        	spinner11ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner11.setAdapter(spinner11ArrayAdapter);
        	
        	spinner11.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	mtu = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        	
        	/**spinner12*/
        	ArrayAdapter<String> spinner12ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, numbers);
        	spinner12ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        	spinner12.setAdapter(spinner12ArrayAdapter);
        	
        	spinner12.setOnItemSelectedListener(new OnItemSelectedListener() {
        	    
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	    	mtd = parent.getItemAtPosition(pos).toString();
        	    	
        	    }

        	    
        	    public void onNothingSelected(AdapterView<?> parent) {
        	        //do nothing
        	    }
        	});
        }
    		
    		
    		
    	}

