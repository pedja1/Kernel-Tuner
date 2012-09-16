package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

import java.util.List;

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
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;

import android.widget.AdapterView.OnItemSelectedListener;
import java.io.*;

import rs.pedjaapps.KernelTuner.R;

public class uv extends Activity {

	public SharedPreferences preferences;
	
	private class apply extends AsyncTask<String, Void, Object> {

		protected Object doInBackground(String... args) {
			Log.i("MyApp", "Background thread starting");

			Process localProcess;
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
				localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
				localDataOutputStream
						.writeBytes("echo "
								+ freqselected
								+ "000"
								+ " "
								+ edittextValue.trim()
								+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
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
			/*SharedPreferences.Editor editor = preferences.edit();
		    editor.putString("uv"+freqselected, freqselected + " " +edittextValue.trim());
		    editor.commit();*/
			
			 
			getuvtable();
			
			uv.this.data = result;
			uv.this.pd.dismiss();
			// uv.this.finish();
		}
	}

	private class applyminus extends AsyncTask<String, Void, Object> {

		
		
		protected Object doInBackground(String... args) {
			Log.i("MyApp", "Background thread starting");

			Process localProcess;
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
				localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
				for (int i = 0; i < frequencies2.size(); i++) {
					int volt = Integer.parseInt(vddarray[i]) - 12500;
					localDataOutputStream
							.writeBytes("echo "
									+ freqarray2[i]
									+ " "
									+ volt
									+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
					/*SharedPreferences.Editor editor = preferences.edit();
				    editor.putString("uv"+freqarray2[i], String.valueOf(freqarray2[i]+" "+volt));
				    editor.commit();*/
				}

				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				vdd.clear();
				getvdd();
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
			
	     	
			getuvtable();

			// TextView uvtable = (TextView)findViewById(R.id.textView3);
			// uvtable.setText(vddtable);
			uv.this.data = result;
			uv.this.pd.dismiss();
			// uv.this.finish();
		}
	}

	private class applyplus extends AsyncTask<String, Void, Object> {

		protected Object doInBackground(String... args) {
			Log.i("MyApp", "Background thread starting");

			Process localProcess;
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
				localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
				for (int i = 0; i < frequencies2.size(); i++) {
					int volt = Integer.parseInt(vddarray[i]) + 12500;
					localDataOutputStream
							.writeBytes("echo "
									+ freqarray2[i]
									+ " "
									+ volt
									+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
					SharedPreferences.Editor editor = preferences.edit();
				   editor.putString("uv"+freqarray2[i], String.valueOf(freqarray2[i]+" "+volt));
				    editor.commit();
				}

				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				vdd.clear();
				getvdd();

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
			getuvtable();

			// TextView uvtable = (TextView)findViewById(R.id.textView3);
			// uvtable.setText(vddtable);
			uv.this.data = result;
			uv.this.pd.dismiss();
			// uv.this.finish();
		}
	}

	public String cpu0freq = "1 2 2 3 4 5 6 7";
	public String time;
	public String[] delims;
	public String first;
	String[] freqs;
	List<String> vdd = new ArrayList<String>();

	List<String> frequencies = new ArrayList<String>();
	List<String> frequencies2 = new ArrayList<String>();
	List<Integer> perint = new ArrayList<Integer>();
	String[] freqarray;
	String[] freqarray2;
	String[] vddarray;
	String[] strarray3;
	Integer[] intarray;
	String mynewstring;
	int max = 1400000;
	int min = 700000;
	int itemCount;

	public String freqselected;
	public String vddtable;
	int position;
	public String edittextValue;
	private ProgressDialog pd = null;
	private Object data = null;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File file = new File("/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
		try{

			InputStream fIn = new FileInputStream(file);
           uv();
		   

		}
		catch(FileNotFoundException e){ 
			setContentView(R.layout.not_supported);
			TextView ns = (TextView)findViewById(R.id.textView1);
			ns.setText("Your kernel doesnt support undervolting");
		}
		}
		public void uv(){
		setContentView(R.layout.uv);
		 preferences = PreferenceManager.getDefaultSharedPreferences(this);
		getuvtable();

		freqs();
		freqs2();
		getvdd();
		spinner();

		/*
		 * read();
		 * 
		 * read4(); read2(); read3();
		 */

		Button apply = (Button) findViewById(R.id.button1);
		apply.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				EditText uv = (EditText) findViewById(R.id.editText1);
				edittextValue = String.valueOf(uv.getText());
				uv.this.pd = ProgressDialog.show(uv.this, "Working..",
						"Applying settings", true, false);
				new apply().execute();

				// Start a new thread that will download all the dat
			}
		});

		Button minus = (Button) findViewById(R.id.button2);
		minus.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				// EditText uv =(EditText)findViewById(R.id.editText1);
				// edittextValue = String.valueOf(uv.getText());
				uv.this.pd = ProgressDialog.show(uv.this, "Working..",
						"Applying settings", true, false);
				new applyminus().execute();

				// Start a new thread that will download all the dat
			}
		});

		Button plus = (Button) findViewById(R.id.button3);
		plus.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				// EditText uv =(EditText)findViewById(R.id.editText1);
				// edittextValue = String.valueOf(uv.getText());
				uv.this.pd = ProgressDialog.show(uv.this, "Working..",
						"Applying settings", true, false);
				new applyplus().execute();

				// Start a new thread that will download all the dat
			}
		});
	}

	
	protected void onResume() {
		
		super.onResume();

	}

	public void getuvtable() {
		try {

			File myFile = new File(
					"/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			vddtable = aBuffer;
			myReader.close();

		} catch (Exception e) {

		}
		TextView uvtable = (TextView) findViewById(R.id.textView3);
		uvtable.setText(vddtable);

	}

	public void spinner() {

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, frequencies);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
																							// drop
																							// down
																							// vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				position = pos;
				freqselected = parent.getItemAtPosition(pos).toString();
				EditText uv = (EditText) findViewById(R.id.editText1);
				uv.setText(vddarray[pos]);
			}

			
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

	}

	public void freqs() {

		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(
					"/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				int line = Integer.parseInt(strLine.substring(0,
						strLine.length() - 13).trim());
				String freq = String.valueOf(line);
				frequencies.add(freq);

			}
			System.out.println(frequencies);
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		freqarray = frequencies.toArray(new String[0]);
	}

	public void freqs2() {

		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(
					"/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				int line = Integer.parseInt(strLine.substring(0,
						strLine.length() - 10).trim());
				String freq = String.valueOf(line);
				frequencies2.add(freq);

			}
			System.out.println(frequencies2);
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		freqarray2 = frequencies2.toArray(new String[0]);
	}

	public void getvdd() {

		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(
					"/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line

			while ((strLine = br.readLine()) != null) {
				int line = Integer.parseInt(strLine.substring(9,
						strLine.length() - 0).trim());
				String temp = String.valueOf(line);
				vdd.add(temp);
			}

			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		vddarray = vdd.toArray(new String[0]);
	}

}
