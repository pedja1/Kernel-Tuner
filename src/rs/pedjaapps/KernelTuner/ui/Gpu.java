package rs.pedjaapps.KernelTuner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.actionbarsherlock.app.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class Gpu extends SherlockActivity
{

	private int gpu2dcurent;
	private int gpu3dcurent ;
	private int gpu2dmax;
	private int gpu3dmax;
	private String selected2d;

	private String selected3d;


	private static String board;


	private List<String> gpu2d;
	private List<String> gpu3d;
	private List<String> gpu2dHr;
	private List<String> gpu3dHr;
	
	Spinner d2Spinner;
	Spinner d3Spinner;

	private ProgressDialog pd = null;
	private SharedPreferences preferences;
	

private class changegpu extends AsyncTask<String, Void, Object>
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

		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
		            
		            stdin.write(("echo " + selected3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
		            stdin.write(("echo " + selected2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
		            stdin.write(("echo " + selected2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
		            
		            stdin.flush();

		            stdin.close();
		            BufferedReader brCleanUp =
		                    new BufferedReader(new InputStreamReader(stdout));
		            while ((line = brCleanUp.readLine()) != null) {
		                Log.d("[KernelTuner GPU Output]", line);
		            }
		            brCleanUp.close();
		            brCleanUp =
		                    new BufferedReader(new InputStreamReader(stderr));
		            while ((line = brCleanUp.readLine()) != null) {
		            	Log.e("[KernelTuner GPU Error]", line);
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
	  	    editor.putString("gpu3d", selected3d+"");
	  	    editor.putString("gpu2d", selected2d+"");
	  	    editor.commit();


			Gpu.this.pd.dismiss();
			Gpu.this.finish();

		}

	}

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
    	board = android.os.Build.DEVICE;
    	preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.Theme_Sherlock_Light_Dialog_NoTitleBar);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.Theme_Sherlock_Dialog_NoTitleBar);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.Theme_Sherlock_Light_Dialog_NoTitleBar);
			
		}
		super.onCreate(savedInstanceState);
		   
		setContentView(R.layout.gpu);
			Button apply = (Button)findViewById(R.id.apply);
		d2Spinner = (Spinner) findViewById(R.id.spinner2);
		 d3Spinner = (Spinner) findViewById(R.id.spinner1);
		gpu2dmax = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
		gpu3dmax = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
		gpu2dcurent = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
		gpu3dcurent = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
		List<String> adreno220 = Arrays.asList(new String[] {"shooter", "shooteru", "pyramid", "tenderloin", "vigor", "rider", "nozomi", "LT26i", "hikari", "doubleshot", "su640","SHV-E160S" ,"SHV-E160L", "SHV-E120L", "holiday"});
		List<String> adreno225 = Arrays.asList(new String[] {"evita", "ville", "jewel", "d2spr", "d2tmo" });
		if (adreno220.contains(board)/*board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid") || board.equals("tenderloin") || board.equals("vigor") || board.equals("raider") || board.equals("nozomi") || board.equals("LT26i") || board.equals("hikari")*/)
		{
			gpu2dHr = Arrays.asList(new String[]{"160Mhz", "200Mhz", "228Mhz", "266Mhz"});
			gpu3dHr = Arrays.asList(new String[]{"200Mhz", "228Mhz", "266Mhz", "300Mhz", "320Mhz"});
			gpu2d = Arrays.asList(new String[]{"160000000", "200000000", "228571000", "266667000"});
			gpu3d = Arrays.asList(new String[]{"200000000", "228571000", "266667000", "300000000", "320000000"});
				createSpinners();
		}
		else if (adreno225.contains(board))
		{
			gpu2dHr = Arrays.asList(new String[]{"320Mhz", "266Mhz", "228Mhz", "200Mhz", "160Mhz", "96Mhz", "27Mhz"});
			gpu3dHr = Arrays.asList(new String[]{"512Mhz", "400Mhz", "320Mhz", "300Mhz", "266Mhz", "228Mhz", "200Mhz", "177Mhz", "27Mhz"});
			gpu2d = Arrays.asList(new String[]{"320000000", "266667000", "228571000", "200000000", "160000000", "96000000", "27000000"});
			gpu3d = Arrays.asList(new String[]{"512000000", "400000000", "320000000", "300000000", "266667000", "228571000", "200000000", "177778000", "27000000"});
			createSpinners();
		}
		else{
			d2Spinner.setEnabled(false);
			d3Spinner.setEnabled(false);
			apply.setEnabled(false);
		}

		
	
	
		
		TextView tv5 = (TextView)findViewById(R.id.textView5);
		TextView tv2 = (TextView)findViewById(R.id.textView7);

		tv5.setText((gpu3dcurent/1000000) + "Mhz");
		tv2.setText((gpu2dcurent/1000000) + "Mhz");

	
		Button cancel = (Button)findViewById(R.id.cancel);
		apply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Gpu.this.pd = ProgressDialog.show(Gpu.this, null, getResources().getString(R.string.applying_settings), true, false);
				new changegpu().execute();
				
			}
			
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
			finish();
			}
			
		});

		




	}

    private void createSpinners()
	{


	
		ArrayAdapter<String> d2Adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2dHr);
		d2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		d2Spinner.setAdapter(d2Adapter);

		d2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected2d = gpu2d.get(pos);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});
		
		int p = gpu2d.indexOf(gpu2dmax+"");
	//	Toast.makeText(Gpu.this, gpu2dmax+" "+p, Toast.LENGTH_LONG).show();
		if(p != -1){
		int d2Position = d2Adapter.getPosition(gpu2dHr.get(p));
		d2Spinner.setSelection(d2Position);
        }
	
		ArrayAdapter<String> d3Adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu3dHr);
		d3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		d3Spinner.setAdapter(d3Adapter);

		d3Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected3d = gpu3d.get(pos);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});
       
		int p1 = gpu3d.indexOf(gpu3dmax+"");
		if(p1 != -1){
		int d3Position = d3Adapter.getPosition(gpu3dHr.get(p1));
		d3Spinner.setSelection(d3Position);
		}
	}

    

    private Integer readFile(String path)
	{
		try
		{

			File myFile = new File(path/*"/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk"*/);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			myReader.close();
			return Integer.parseInt(aBuffer.trim());



		}
		catch (Exception e)
		{
			return 0;
				}
	}

}
