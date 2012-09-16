package rs.pedjaapps.DualCore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

public class gpu extends Activity{

	static String gpu2dcurent = "1234567";
	public String gpu3dcurent = "0000000";
	public String selected2d;
	public String selected3d;
	public String freq2d;
	public String freq3d;
	public String[] gpu2d = {"160", "200", "228", "266"};
	public String[] gpu3d = {"200", "228", "266", "300", "320"};
	public int selected;
	public int curent;
	public int curent3d;
	private ProgressDialog pd = null;
	private Object data = null;
	public SharedPreferences preferences;
	
private class changegpu2d extends AsyncTask<String, Void, Object> {
    	
    	
    	protected Object doInBackground(String... args) {
             Log.i("MyApp", "Background thread starting");
    
    
             Process localProcess;
       		try {
  				localProcess = Runtime.getRuntime().exec("su");
  			
       		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
              localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
              localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
              localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n");
              localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk\n");
              localDataOutputStream.writeBytes("echo " + freq2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
              localDataOutputStream.writeBytes("echo " + freq2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
              localDataOutputStream.writeBytes("echo " + freq2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n");
              localDataOutputStream.writeBytes("echo " + freq2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/gpuclk\n");
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
 	  	    editor.putString("gpu2d", freq2d);
 	  	  // value to store
 	  	    editor.commit();
            // Pass the result data back to the main activity
        	TextView text2d = (TextView)findViewById(R.id.textView1);
   		 text2d.setText(freq2d.substring(0, freq2d.length()-6)+"Mhz");
            gpu.this.data = result;

            
               gpu.this.pd.dismiss();
             
        }

   	}

private class changegpu3d extends AsyncTask<String, Void, Object> {
	
	
	protected Object doInBackground(String... args) {
         Log.i("MyApp", "Background thread starting");



         Process localProcess;
    		try {
				localProcess = Runtime.getRuntime().exec("su");
			
    		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
           localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk\n");
           localDataOutputStream.writeBytes("echo " + freq3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
           localDataOutputStream.writeBytes("echo " + freq3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk\n");
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
	  	    editor.putString("gpu3d", freq3d);
	  	  // value to store
	  	    editor.commit();
        // Pass the result data back to the main activity
    	TextView text3d = (TextView)findViewById(R.id.textView2);
		 text3d.setText(freq3d.substring(0, freq3d.length()-6)+"Mhz");
        gpu.this.data = result;

        
           gpu.this.pd.dismiss();
          
    }

	}
	
    	
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.gpu);
	readgpu2dcurent();
	readgpu3dcurent();
	setprogress2d();
	setprogress3d();
	
	Button btminus2d = (Button)findViewById(R.id.button3);
	final ProgressBar prog2d = (ProgressBar)findViewById(R.id.gpuprogressBar1);
	btminus2d.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			gpu.this.pd = ProgressDialog.show(gpu.this, "Working..", "Applying settings", true, false);
			if(prog2d.getProgress()==3){
				freq2d= "228571000";
			}
			else if (prog2d.getProgress()==2){
				freq2d= "200000000";
			}
			else if (prog2d.getProgress()==1){
				freq2d= "160000000";
			}
			prog2d.setProgress(prog2d.getProgress() - 1);
			new changegpu2d().execute();
			
		    // Start a new thread that will download all the data
		    
		}
	});
	
	Button btplus2d = (Button)findViewById(R.id.button4);
	btplus2d.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			gpu.this.pd = ProgressDialog.show(gpu.this, "Working..", "Applying settings", true, false);
			if(prog2d.getProgress()==0){
				freq2d= "200000000";
			}
			else if (prog2d.getProgress()==1){
				freq2d= "228571000";
			}
			else if (prog2d.getProgress()==2){
				freq2d= "266667000";
			}
			prog2d.setProgress(prog2d.getProgress() + 1);
			new changegpu2d().execute();
		}
	});
	
	Button btminus3d = (Button)findViewById(R.id.button5);
	final ProgressBar prog3d = (ProgressBar)findViewById(R.id.gpuprogressBar2);
	btminus3d.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			gpu.this.pd = ProgressDialog.show(gpu.this, "Working..", "Applying settings", true, false);
			if(prog3d.getProgress()==4){
				freq3d= "300000000";
			}
			else if(prog3d.getProgress()==3){
				freq3d= "266667000";
			}
			else if (prog3d.getProgress()==2){
				freq3d= "228571000";
			}
			else if (prog3d.getProgress()==1){
				freq3d= "200000000";
			}
			prog3d.setProgress(prog3d.getProgress() - 1);
			new changegpu3d().execute();
			
		    // Start a new thread that will download all the data
		    
		}
	});
	
	Button btplus3d = (Button)findViewById(R.id.button6);
	btplus3d.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			gpu.this.pd = ProgressDialog.show(gpu.this, "Working..", "Applying settings", true, false);
			if(prog3d.getProgress()==0){
				freq3d= "228571000";
			}
			else if (prog3d.getProgress()==1){
				freq3d= "266667000";
			}
			else if (prog3d.getProgress()==2){
				freq3d= "300000000";
			}
			else if (prog3d.getProgress()==3){
				freq3d= "320000000";
			}
			prog3d.setProgress(prog3d.getProgress() + 1);
			new changegpu3d().execute();
		
		
		}
	});
	
    
}
    	 public void setprogress2d(){
    		 ProgressBar prog2d = (ProgressBar)findViewById(R.id.gpuprogressBar1);
    		 prog2d.setProgress(curent);
    		 TextView text2d = (TextView)findViewById(R.id.textView1);
    		 text2d.setText(gpu2dcurent.substring(0, gpu2dcurent.length()-6)+"Mhz");
    	   }
    	 
    	 public void setprogress3d(){
    		 ProgressBar prog3d = (ProgressBar)findViewById(R.id.gpuprogressBar2);
    		 prog3d.setProgress(curent3d);
    		 TextView text3d = (TextView)findViewById(R.id.textView2);
    		 text3d.setText(gpu3dcurent.substring(0, gpu3dcurent.length()-6)+"Mhz");
    	   }

    	public void readgpu3dcurent(){
    		try {
     			
     			File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
     			FileInputStream fIn = new FileInputStream(myFile);

     			BufferedReader myReader = new BufferedReader(
     					new InputStreamReader(fIn));
     			String aDataRow = "";
     			String aBuffer = "";
     			while ((aDataRow = myReader.readLine()) != null) {
     				aBuffer += aDataRow + "\n";
     			}

     			gpu3dcurent = aBuffer.trim();
      			if(gpu3dcurent.equals("200000000")){
      				curent3d = 0;
      				freq3d= "200000000";
      			}
      			else if(gpu3dcurent.equals("228571000")){
      				curent3d = 1;
      				freq3d= "228571000";
      			}
      			else if(gpu3dcurent.equals("266667000")){
      				curent3d = 2;
      				freq3d= "266667000";
      			}
      			else if(gpu3dcurent.equals("300000000")){
      				curent3d = 3;
      				freq3d= "300000000";
      			}
      			else if(gpu3dcurent.equals("320000000")){
      				curent3d = 4;
      				freq3d= "320000000";
      			}
     			myReader.close();
     			

     			
     		} catch (Exception e) {
     			Toast.makeText(getBaseContext(), e.getMessage(),
    					Toast.LENGTH_SHORT).show();
     			
     		}
    	}
    	
    	public void readgpu2dcurent(){
    	     try {
      			
      			File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
      			FileInputStream fIn = new FileInputStream(myFile);
      			BufferedReader myReader = new BufferedReader(
      					new InputStreamReader(fIn));
      			String aDataRow = "";
      			String aBuffer = "";
      			while ((aDataRow = myReader.readLine()) != null) {
      				aBuffer += aDataRow + "\n";
      			}

      			gpu2dcurent = aBuffer.trim();
      			if(gpu2dcurent.equals("160000000")){
      				curent = 0;
      				freq2d= "160000000";
      			}
      			else if(gpu2dcurent.equals("200000000")){
      				curent = 1;
      				freq2d= "200000000";
      			}
      			else if(gpu2dcurent.equals("228571000")){
      				curent = 2;
      				freq2d= "228571000";
      			}
      			else if(gpu2dcurent.equals("266667000")){
      				curent = 3;
      				freq2d= "266667000";
      			}
      			myReader.close();
      			
      			

      			
      		} catch (Exception e) {
      			
      		}
      		}
    	
   
	
  
    

}
