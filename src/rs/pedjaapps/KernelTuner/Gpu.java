package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.actionbarsherlock.app.SherlockActivity;



import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Gpu extends SherlockActivity
{

	private String gpu2dcurent;
	private String gpu3dcurent ;
	private String gpu2dmax;
	private String gpu3dmax;
	private String selected2d;

	private String selected3d;

	private int new3d;
	private int new2d;

	private String board = android.os.Build.DEVICE;


	private String[] gpu2d ;
	private String[] gpu3d ;

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
	  	    editor.putString("gpu3d", String.valueOf(new3d));
	  	    editor.putString("gpu2d", String.valueOf(new2d));
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
			gpu2d = new String[]{"160", "200", "228", "266"};
			gpu3d = new String[]{"200", "228", "266", "300", "320"};
		}
		else if (board.equals("evita") || board.equals("ville") || board.equals("jewel") || board.equals("d2spr"))
		{
			gpu2d = new String[]{"320", "266", "228", "200", "160", "96", "27"};
			gpu3d = new String[]{"512", "400", "320", "300", "266", "228", "200", "177", "27"};
		}

		readgpu2dcurent();

		readgpu3dcurent();
		readgpu2dmax();

		readgpu3dmax();

		TextView tv5 = (TextView)findViewById(R.id.textView5);
		TextView tv2 = (TextView)findViewById(R.id.textView7);

		tv5.setText(gpu3dcurent.substring(0, gpu3dcurent.length() - 6) + "Mhz");
		tv2.setText(gpu2dcurent.substring(0, gpu2dcurent.length() - 6) + "Mhz");

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

    private void createSpinner2D()
	{


		final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2d);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected2d = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

		int spinnerPosition = spinnerArrayAdapter.getPosition(gpu2dmax.substring(0, gpu2dmax.length() - 6));

		spinner.setSelection(spinnerPosition);

	}



    private void createSpinner3D()
	{


		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu3d);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					selected3d = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

	
		int spinnerPosition = spinnerArrayAdapter.getPosition(gpu3dmax.substring(0, gpu3dmax.length() - 6));
		spinner.setSelection(spinnerPosition);

	}


    private void readgpu3dcurent()
	{
		try
		{

			File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			gpu3dcurent = aBuffer.trim();

			myReader.close();



		}
		catch (Exception e)
		{
			}
	}

    private void readgpu2dcurent()
	{
		try
		{

			File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			gpu2dcurent = aBuffer.trim();

			myReader.close();




		}
		catch (Exception e)
		{
			}
	}


    private void readgpu3dmax()
	{
		try
		{

			File myFile = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			gpu3dmax = aBuffer.trim();
			createSpinner3D();
			myReader.close();

		}
		catch (Exception e)
		{
			}
	}

    private void readgpu2dmax()
	{
		try
		{

			File myFile = new File("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			gpu2dmax = aBuffer.trim();

			createSpinner2D();

			myReader.close();




		}
		catch (Exception e)
		{
				}
	}

}
