package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import rs.pedjaapps.KernelTuner.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class Gpu extends Activity
{

	public String gpu2dcurent;
	public String gpu3dcurent ;
	public String gpu2dmax;
	public String gpu3dmax;
	public String selected2d;

	public String selected3d;

	public int new3d;
	public int new2d;

	String board = android.os.Build.DEVICE;


	public String[] gpu2d ;
	public String[] gpu3d ;

	private ProgressDialog pd = null;
	private Object data = null;
	public SharedPreferences preferences;

private class changegpu extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
			Process localProcess;
    		try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid"))
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
				else if (board.equals("evita") || board.equals("ville") || board.equals("jwel"))
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



				}
				localDataOutputStream.writeBytes("echo " + new3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");

				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");




				localDataOutputStream.writeBytes("echo " + new2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + new2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");   




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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpu);
		if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid")|| board.equals("tenderloin"))
		{
			gpu2d = new String[]{"160", "200", "228", "266"};
			gpu3d = new String[]{"200", "228", "266", "300", "320"};
		}
		else if (board.equals("evita") || board.equals("ville") || board.equals("jwel"))
		{
			gpu2d = new String[]{"266", "228", "200", "160", "96", "27"};
			gpu3d = new String[]{"400", "320", "300", "266", "228", "200", "177", "27"};
		}

		readgpu2dcurent();

		readgpu3dcurent();
		readgpu2dmax();

		readgpu3dmax();

		TextView tv5 = (TextView)findViewById(R.id.textView5);
		TextView tv2 = (TextView)findViewById(R.id.textView7);

		tv5.setText(gpu3dcurent.substring(0, gpu3dcurent.length() - 6) + "Mhz");
		tv2.setText(gpu2dcurent.substring(0, gpu2dcurent.length() - 6) + "Mhz");

		Button apply = (Button)findViewById(R.id.button2);

		apply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Gpu.this.pd = ProgressDialog.show(Gpu.this, null, getResources().getString(R.string.applying_settings), true, false);

					new changegpu().execute();



				}
			});

		Button cancel = (Button)findViewById(R.id.button1);

		cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Gpu.this.finish();


				}
			});




	}

	public void createSpinner2D()
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



	public void createSpinner3D()
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


	public void readgpu3dcurent()
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
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});

		}
	}

	public void readgpu2dcurent()
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
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
		}
	}


	public void readgpu3dmax()
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
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});

		}
	}

	public void readgpu2dmax()
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
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
		}
	}


}
