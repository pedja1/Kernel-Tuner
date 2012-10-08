package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Swap extends Activity{

	
	private ProgressDialog pd = null;
    private Object data = null;
   String[] swapSize = {"128","256","512","758","1024"};
    String[] swapLocation = {"/data/",String.valueOf(Environment.getExternalStorageDirectory())+"/"};
    String[] swappiness = {"10","20","30","40","50","60","70","80","90","100"};
    
    int swapSizeSelected;
    String swapLocationSelected;
    String swappinessSelected;
    String swaps;
    String swaps2;
    String currentSwappiness;
    String swapLocationCurrent;
    public SharedPreferences preferences;
    
    List<String> swapsList = new ArrayList<String>();

    	
    	
    	
    	
private class deactivateSwap extends AsyncTask<String, Void, Object> {
        	
        	
        	@Override
			protected Object doInBackground(String... args) {
                 Log.i("MyApp", "Background thread starting");
                
                 Process localProcess;
          		try {
     				localProcess = Runtime.getRuntime().exec("su");
     			
          		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
                 localDataOutputStream.writeBytes("swapoff "+swapLocationCurrent.trim()+"\n");
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

             @Override
			protected void onPostExecute(Object result) {
            	/* preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            	 SharedPreferences.Editor editor = preferences.edit();
            	   /* editor.putString("cpu0min", minselected);
            	    editor.putString("cpu0max", maxselected);
            	    editor.putString("cpu0gov", govselected);// value to store
            	    editor.commit();
            	 
                 Swap.this.data = result;*/
            	 updateUI();
                 Swap.this.pd.dismiss();
                
             }
        	}

private class activateSwap extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
        
         Process localProcess;
  		try {
				localProcess = Runtime.getRuntime().exec("su");
			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("swapon "+swapLocationSelected.trim()+"/swap"+"\n");
         localDataOutputStream.writeBytes("echo "+swappinessSelected+"/proc/sys/vm/swappiness\n");
         
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

     @Override
	protected void onPostExecute(Object result) {
    	/* preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	 SharedPreferences.Editor editor = preferences.edit();
    	   /* editor.putString("cpu0min", minselected);
    	    editor.putString("cpu0max", maxselected);
    	    editor.putString("cpu0gov", govselected);// value to store
    	    editor.commit();
    	 
         Swap.this.data = result;*/
    	 updateUI();
         Swap.this.pd.dismiss();
        
     }
	}

private class createSwap extends AsyncTask<String, Void, Object> {
	
	
	@Override
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");
        
         Process localProcess;
  		try {
				localProcess = Runtime.getRuntime().exec("su");
			
  		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
         localDataOutputStream.writeBytes("swapon "+swapLocationCurrent.trim()+"\n");
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

     @Override
	protected void onPostExecute(Object result) {
    	/* preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	 SharedPreferences.Editor editor = preferences.edit();
    	   /* editor.putString("cpu0min", minselected);
    	    editor.putString("cpu0max", maxselected);
    	    editor.putString("cpu0gov", govselected);// value to store
    	    editor.commit();
    	 
         Swap.this.data = result;*/
    	 updateUI();
         Swap.this.pd.dismiss();
        
     }
	}
    	
    	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	preferences = PreferenceManager.getDefaultSharedPreferences(this);
	String theme = preferences.getString("theme", "system");
	if (theme.equals("system")) {
		setTheme(android.R.style.Theme_DeviceDefault);
	} else if (theme.equals("holo")) {
		setTheme(android.R.style.Theme_Holo);
	} else if (theme.equals("holo_light")) {
		setTheme(android.R.style.Theme_Holo_Light);
	} else if (theme.equals("dark")) {
		setTheme(android.R.style.Theme_Black);
	} else if (theme.equals("light")) {
		setTheme(android.R.style.Theme_Light);
	} else if (theme.equals("holo_no_ab")) {
		setTheme(android.R.style.Theme_Holo_NoActionBar);
	} else if (theme.equals("holo_wp")) {
		setTheme(android.R.style.Theme_Holo_Wallpaper);
	} else if (theme.equals("holo_fs")) {
		setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
	} else if (theme.equals("holo_light_dark_ab")) {
		setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
	} else if (theme.equals("holo_light_no_ab")) {
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
	} else if (theme.equals("holo_light_fs")) {
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
	}
	setContentView(R.layout.swap);
	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

	Button activate = (Button)findViewById(R.id.button1);
	Button create = (Button)findViewById(R.id.button2);
	
	Button deactivate = (Button)findViewById(R.id.button4);
	TextView status = (TextView)findViewById(R.id.textView2);
	final EditText ed = (EditText)findViewById(R.id.EditText1);
	updateUI();
		
	status.setText(swaps2);
	
	String[] swapsArray = swaps.split("\\s");
	swapLocationCurrent = swapsArray[0];
	System.out.println(swapLocationCurrent);
	
	
	
	deactivate.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Deactivating swap...", true, false);

			new deactivateSwap().execute();
		}
		
	});
	
	activate.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Activating swap...", true, false);
			swappinessSelected=ed.getText().toString();
			new activateSwap().execute();
		}
		
	});
	
	create.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Creating swap file...", true, false);

			new createSwap().execute();
		}
		
	});
	
    }

    	public void updateUI(){
    		currentSwappiness();
    		spinnerSwapSize();
    		spinnerSwapLocation();
    		swaps();
    		EditText ed = (EditText)findViewById(R.id.EditText1);
    		Button activate = (Button)findViewById(R.id.button1);
    		Button create = (Button)findViewById(R.id.button2);
    		Button delete = (Button)findViewById(R.id.button3);
    		Button deactivate = (Button)findViewById(R.id.button4);
    		TextView status = (TextView)findViewById(R.id.textView2);
    		LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
    		if(!currentSwappiness.equals("err"))
    		{
    			ed.setText(currentSwappiness);
    		}
    		else{
    			ll.setVisibility(View.GONE);
    		}
    	
    	if(new File("/data/swap").exists()){
    		System.out.println("swap file found on /data");
    		create.setVisibility(View.GONE);
    	}	
    	else if(new File(String.valueOf(Environment.getExternalStorageDirectory())+"/swap").exists()){
    		System.out.println("swap file found on /sdcard");
    		create.setVisibility(View.GONE);
    	}
    	else{
    		
    		activate.setVisibility(View.GONE);
    		deactivate.setVisibility(View.GONE);
    	}
    	
    	if(swaps.equals("")){
    		deactivate.setVisibility(View.GONE);
    	}
    	else{
    		activate.setVisibility(View.GONE);
    		
    	}
    	} 
    	
	public void currentSwappiness(){
		
	    
		
		try {
    		
    		File myFile = new File("/proc/sys/vm/swappiness");
    		FileInputStream fIn = new FileInputStream(myFile);
		
    		BufferedReader myReader = new BufferedReader(
    				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		while ((aDataRow = myReader.readLine()) != null) {
    			aBuffer += aDataRow + "\n";
    		}

    		currentSwappiness = aBuffer.trim();
    		myReader.close();
    	           		           		
    	} catch (Exception e) {
    		currentSwappiness = "err";
    	}
		

	}
	
public void swaps(){
		
	    
		
		try {
    		
    		File myFile = new File("/proc/swaps");
    		FileInputStream fIn = new FileInputStream(myFile);
		
    		BufferedReader myReader = new BufferedReader(
    				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		myReader.readLine();
    		while ((aDataRow = myReader.readLine()) != null) {
    			
    			aBuffer += aDataRow + "\n";
    		}

    		swaps = aBuffer.trim();
    		System.out.println(swaps);
    		myReader.close();
    	           		           		
    	} catch (Exception e) {
    		swaps = "err";
    	}
		
try {
    		
    		File myFile = new File("/proc/swaps");
    		FileInputStream fIn = new FileInputStream(myFile);
		
    		BufferedReader myReader = new BufferedReader(
    				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		//myReader.readLine();
    		while ((aDataRow = myReader.readLine()) != null) {
    			
    			aBuffer += aDataRow + "\n";
    		}

    		swaps2 = aBuffer.trim();
    		System.out.println(swaps2);
    		myReader.close();
    	           		           		
    	} catch (Exception e) {
    		swaps2 = "err";
    	}

	}
	
    public void spinnerSwapSize(){
    	
    	
    	Spinner spinner = (Spinner) findViewById(R.id.spinner1);
    	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, swapSize);
    	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
    	spinner.setAdapter(spinnerArrayAdapter);
    	
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		@Override
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	    	swapSizeSelected = Integer.parseInt(parent.getItemAtPosition(pos).toString())*1024;
    	    	
    	    }

    		@Override
    	    public void onNothingSelected(AdapterView<?> parent) {
    	        //do nothing
    	    }
    	});

    	/*ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

    	int spinnerPosition = myAdap.getPosition(curentgovernorcpu0);

    	//set the default according to value
    	spinner.setSelection(spinnerPosition);*/
    	//
    	
    }

    public void spinnerSwapLocation(){
		
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner2);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Swap.this,   android.R.layout.simple_spinner_item, swapLocation);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	swapLocationSelected = parent.getItemAtPosition(pos).toString();
		    	
		    }

			@Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        //do nothing
		    }
		});
		/*ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(curentminfreqcpu0);

		//set the default according to value
		spinner.setSelection(spinnerPosition);*/

	}

	
}
