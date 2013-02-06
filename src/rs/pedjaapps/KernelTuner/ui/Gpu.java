package rs.pedjaapps.KernelTuner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
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

	private int new3d;
	private int new2d;

	private static final String board = android.os.Build.DEVICE;


	private List<Integer> gpu2d;
	private List<String> gpu3d;
	private List<String> gpu2dHr;
	private List<String> gpu3dHr;
	

	private ProgressDialog pd = null;
	private SharedPreferences preferences;
	

private class changegpu extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
			if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid") || board.equals("tenderloin"))
				{
					//3d freqs for shooter,shooteru,pyramid(msm8x60)
					if (selected3d.equals("200"))
					{
						new3d = 200000000;
					}
					else if (selected3d.equals("228"))
					{
						new3d = 228571000;
					}
					else if (selected3d.equals("266"))
					{
						new3d = 266667000;
					}
					else if (selected3d.equals("300"))
					{
						new3d = 300000000;
					}
					else if (selected3d.equals("320"))
					{
						new3d = 320000000;
					}

					//2d freqs for shooter,shooteru,pyramid(msm8x60)
					if (selected2d.equals("160"))
					{
						new2d = 160000000;

					}
					else if (selected2d.equals("200"))
					{
						new2d = 200000000;
						//System.out.println("new clock = " +new2d);
					}
					else if (selected2d.equals("228"))
					{
						new2d = 228571000;
						//System.out.println("new clock = " +new2d);
					}
					else if (selected2d.equals("266"))
					{
						new2d = 266667000;
						//System.out.println("new clock = " +new2d);
					}
				}
				//freqs for one s and one xl
				else if (board.equals("evita") || board.equals("ville") || board.equals("jewel") || board.equals("d2spr"))
				{
					// 3d freqs for evita
					if (selected3d.equals("27"))
					{
						new3d = 27000000;
					}
					else if (selected3d.equals("177"))
					{
						new3d = 177778000;
					}
					else if (selected3d.equals("200"))
					{
						new3d = 200000000;
					}
					else if (selected3d.equals("228"))
					{
						new3d = 228571000;
					}
					else if (selected3d.equals("266"))
					{
						new3d = 266667000;
					}
					else if (selected3d.equals("300"))
					{
						new3d = 300000000;
					}
					else if (selected3d.equals("320"))
					{
						new3d = 320000000;
					}
					else if (selected3d.equals("400"))
					{
						new3d = 400000000;
					}
					else if (selected2d.equals("512"))
					{
						new2d = 512000000;
					}

					//2d freqs for evita
					if (selected2d.equals("27"))
					{
						new2d = 27000000;

					}
					else if (selected2d.equals("96"))
					{
						new2d = 96000000;
					}
					else if (selected2d.equals("160"))
					{
						new2d = 160000000;
					}
					else if (selected2d.equals("200"))
					{
						new2d = 200000000;
					}
					else if (selected2d.equals("228"))
					{
						new2d = 228571000;
					}
					else if (selected2d.equals("266"))
					{
						new2d = 266667000;
					}
					else if (selected2d.equals("320"))
					{
						new2d = 320000000;
					}



				}
				try {
		            String line;
		            Process process = Runtime.getRuntime().exec("su");
		            OutputStream stdin = process.getOutputStream();
		            InputStream stderr = process.getErrorStream();
		            InputStream stdout = process.getInputStream();

		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
		            stdin.write(("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
		            
		            stdin.write(("echo " + new3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n").getBytes());
		            stdin.write(("echo " + new2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n").getBytes());
		            stdin.write(("echo " + new2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n").getBytes());
		            
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
	  	    editor.putString("gpu3d", new3d+"");
	  	    editor.putString("gpu2d", new2d+"");
	  	    editor.commit();


			Gpu.this.pd.dismiss();
			Gpu.this.finish();

		}

	}

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
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
		if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid")|| board.equals("tenderloin"))
		{
			gpu2dHr = Arrays.asList(new String[]{"160Mhz", "200Mhz", "228Mhz", "266Mhz"});
			gpu3dHr = Arrays.asList(new String[]{"200Mhz", "228Mhz", "266Mhz", "300Mhz", "320Mhz"});
			gpu2d = Arrays.asList(new int[]{160000000, 200000000, 228571000, 266666000});
			gpu3d = Arrays.asList(new String[]{"200000000", "228571000", "266666000", "300000000", "320000000"});
		}
		else if (board.equals("evita") || board.equals("ville") || board.equals("jewel") || board.equals("d2spr"))
		{
			gpu2dHr = Arrays.asList(new String[]{"320Mhz", "266Mhz", "228Mhz", "200Mhz", "160Mhz", "96Mhz", "27Mhz"});
			gpu3dHr = Arrays.asList(new String[]{"512Mhz", "400Mhz", "320Mhz", "300Mhz", "266Mhz", "228Mhz", "200Mhz", "177Mhz", "27Mhz"});
		//	gpu2d = Arrays.asList(new []{"320000000", "266666000", "228571000", "200000000", "160000000", "96000000", "27000000"});
			gpu3d = Arrays.asList(new String[]{"512000000", "400000000", "320000000", "300000000", "266666000", "228571000", "200000000", "177778000", "27000000"});
		}

		gpu2dmax = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
		gpu3dmax = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
		gpu2dcurent = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
		gpu3dcurent = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
		
		createSpinners();
	

		TextView tv5 = (TextView)findViewById(R.id.textView5);
		TextView tv2 = (TextView)findViewById(R.id.textView7);

		tv5.setText((gpu3dcurent/1000000) + "Mhz");
		tv2.setText((gpu2dcurent/1000000) + "Mhz");

		Button apply = (Button)findViewById(R.id.apply);
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

    private final void createSpinners()
	{


		final Spinner d2Spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> d2Adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2dHr);
		d2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		d2Spinner.setAdapter(d2Adapter);

		d2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected2d = parent.getItemAtPosition(gpu2d.indexOf(gpu2dHr.get(pos))).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		int d2Position = d2Adapter.getPosition(gpu2dHr.get(gpu2d.indexOf(gpu2dmax)));

		d2Spinner.setSelection(d2Position);

		final Spinner d3Spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> d3Adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu3dHr);
		d3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		d3Spinner.setAdapter(d3Adapter);

		d3Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected3d = parent.getItemAtPosition(gpu3d.indexOf(gpu3dHr.get(pos))).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});


		int d3Position = d3Adapter.getPosition(gpu3dHr.get(gpu3d.indexOf(gpu3dmax+"")));
		d3Spinner.setSelection(d3Position);
		
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
