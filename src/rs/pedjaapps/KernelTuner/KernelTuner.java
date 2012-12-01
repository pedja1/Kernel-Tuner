package rs.pedjaapps.KernelTuner;

import android.annotation.*;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.*;
import android.appwidget.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.google.ads.*;
import java.io.*;
import java.util.*;

import java.lang.Process;



//EndImports 

@SuppressLint("WorldReadableFiles")
public class KernelTuner extends SherlockActivity
{

	private TextView batteryLevel;
	private TextView batteryTemp;
	private TextView cputemptxt;
	public SharedPreferences sharedPrefs;
	String tempPref;
	boolean tempMonitor;
	private long mLastBackPressTime = 0;
	Toast mToast;
	LinearLayout tempLayout;
	AlertDialog alert;
	private boolean enableTmp(){
		boolean b;
		try
		{

			File myFile = new File("/sys/devices/virtual/thermal/thermal_zone1/mode");
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
			if(aBuffer.trim().equals("enabled")){
				b = false;
			}
			else{
				b=true;
			}
			
			

		}
		catch (Exception e)
		{
			b=true;
		}
		return b;
	}
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context arg0, Intent intent)
		{


			int level = intent.getIntExtra("level", 0);
			double temperature =  intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;



			if (tempPref.equals("fahrenheit"))
			{
				temperature = (temperature * 1.8) + 32;
				batteryTemp.setText(String.valueOf((int)temperature) + "°F");
				if (temperature <= 104)
				{
					batteryTemp.setTextColor(Color.GREEN);
					
				}
				else if (temperature > 104 && temperature < 131)
				{
					batteryTemp.setTextColor(Color.YELLOW);
					
				}
				else if (temperature >= 131 && temperature < 140)
				{
					batteryTemp.setTextColor(Color.RED);
				
				}
				else if (temperature >= 140)
				{
					
					batteryTemp.setTextColor(Color.RED);
					

				}
			}

			else if (tempPref.equals("celsius"))
			{
				batteryTemp.setText(String.valueOf(temperature) + "°C");
				if (temperature < 45)
				{
					batteryTemp.setTextColor(Color.GREEN);
					
				}
				else if (temperature > 45 && temperature < 55)
				{
					batteryTemp.setTextColor(Color.YELLOW);
					
				}
				else if (temperature >= 55 && temperature < 60)
				{
					batteryTemp.setTextColor(Color.RED);
					
				}
				else if (temperature >= 60)
				{
					
					batteryTemp.setTextColor(Color.RED);
					

				}
			}
			else if (tempPref.equals("kelvin"))
			{
				temperature = temperature + 273.15; 
				batteryTemp.setText(String.valueOf(temperature) + "°K");
				if (temperature < 318.15)
				{
					batteryTemp.setTextColor(Color.GREEN);
					
				}
				else if (temperature > 318.15 && temperature < 328.15)
				{
					batteryTemp.setTextColor(Color.YELLOW);
				
				}
				else if (temperature >= 328.15 && temperature < 333.15)
				{
					batteryTemp.setTextColor(Color.RED);
					
				}
				else if (temperature >= 333.15)
				{
					
					batteryTemp.setTextColor(Color.RED);
					

				}
			}
			///F = (C x 1.8) + 32 
			batteryLevel.setText(String.valueOf(level) + "%");
			if (level < 15 && level >= 5)
			{
				batteryLevel.setTextColor(Color.RED);
				
			}
			else if (level > 15 && level <= 30)
			{
				batteryLevel.setTextColor(Color.YELLOW);
			
			}
			else if (level > 30)
			{
				batteryLevel.setTextColor(Color.GREEN);
				
			}
			else if (level < 5)
			{
				batteryLevel.setTextColor(Color.RED);
				

			}
	    }
	};




	boolean thread = true;
	public String freqcpu0 = "offline";
	public String freqcpu1= "offline";

	public String led;
	SeekBar mSeekBar;
	TextView progresstext;
	
	public String cpu0max = "       ";
	public String cpu1max = "       ";
	public String cpu0min = "       ";
	public String cpu1min = "       ";
	public String cpu2max = "       ";
	public String cpu3max = "       ";
	public String cpu2min = "       ";
	public String cpu3min = "       ";

	public String freqcpu2 = "offline";
	public String freqcpu3 = "offline";


	float fLoad;
	
	public String cputemp;

	public static String cpu0online = CPUInfo.cpu0online; 
	public static String cpu1online = CPUInfo.cpu1online; 
	public static String cpu2online = CPUInfo.cpu2online; 
	public static String cpu3online = CPUInfo.cpu3online; 


	public static String CPU0_FREQS = CPUInfo.CPU0_FREQS;

	public static String CPU0_CURR_FREQ = CPUInfo.CPU0_CURR_FREQ;
	public static String CPU1_CURR_FREQ = CPUInfo.CPU1_CURR_FREQ;
	public static String CPU2_CURR_FREQ = CPUInfo.CPU2_CURR_FREQ;
	public static String CPU3_CURR_FREQ = CPUInfo.CPU3_CURR_FREQ;

	public static String CPU0_MAX_FREQ = CPUInfo.CPU0_MAX_FREQ;
	public static String CPU1_MAX_FREQ = CPUInfo.CPU1_MAX_FREQ;
	public static String CPU2_MAX_FREQ = CPUInfo.CPU2_MAX_FREQ;
	public static String CPU3_MAX_FREQ = CPUInfo.CPU3_MAX_FREQ;

	public static String CPU0_MIN_FREQ = CPUInfo.CPU0_MIN_FREQ;
	public static String CPU1_MIN_FREQ = CPUInfo.CPU1_MIN_FREQ;
	public static String CPU2_MIN_FREQ = CPUInfo.CPU2_MIN_FREQ;
	public static String CPU3_MIN_FREQ = CPUInfo.CPU3_MIN_FREQ;

	public static String CPU0_CURR_GOV = CPUInfo.CPU0_CURR_GOV;
	public static String CPU1_CURR_GOV = CPUInfo.CPU1_CURR_GOV;
	public static String CPU2_CURR_GOV = CPUInfo.CPU2_CURR_GOV;
	public static String CPU3_CURR_GOV = CPUInfo.CPU3_CURR_GOV;

	public static String CPU0_GOVS = CPUInfo.CPU0_GOVS;
	public static String CPU1_GOVS = CPUInfo.CPU1_GOVS;
	public static String CPU2_GOVS = CPUInfo.CPU2_GOVS;
	public static String CPU3_GOVS = CPUInfo.CPU3_GOVS;

	
	TextView cpu0prog;
	TextView cpu1prog;
	TextView cpu2prog;
	TextView cpu3prog;

	ProgressBar cpu0progbar;
	ProgressBar cpu1progbar;
	ProgressBar cpu2progbar;
	ProgressBar cpu3progbar;

	public List<String> freqlist;
	public SharedPreferences preferences;
	private ProgressDialog pd = null;
	
	int load;

	Handler mHandler = new Handler();

	SharedPreferences.Editor editor;

	ProgressDialog mProgressDialog;
	
	private class cpu1Toggle extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			File file = new File(CPU1_CURR_GOV);
			try
			{

				InputStream fIn = new FileInputStream(file);

				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 777 " + cpu1online + "\n");
					localDataOutputStream.writeBytes("echo 0 > " + cpu1online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu1online + "\n");
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
					System.out.println("KernelTuner: Toggling CPU1");
				}
				catch (IOException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				catch (InterruptedException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}

				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cputoggle", false);
				editor.commit();
				fIn.close();
			}

			catch (FileNotFoundException e)
			{
				//enable cpu1


				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 666 " + cpu1online + "\n");
					localDataOutputStream.writeBytes("echo 1 > " + cpu1online + "\n");
					localDataOutputStream.writeBytes("chmod 444 " + cpu1online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu1online + "\n");

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
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cputoggle", true);
				editor.commit();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{

			KernelTuner.this.pd.dismiss();

		}

	}

	private class cpu2Toggle extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
		

			File file = new File(CPU2_CURR_GOV);
			try
			{

				InputStream fIn = new FileInputStream(file);

				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 777 " + cpu2online + "\n");
					localDataOutputStream.writeBytes("echo 0 > " + cpu2online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu2online + "\n"); 
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
				}
				catch (IOException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				catch (InterruptedException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}

				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cpu2toggle", false);
				editor.commit();
				fIn.close();
			}

			catch (FileNotFoundException e)
			{
				//enable cpu1


				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 666 " + cpu2online + "\n");
					localDataOutputStream.writeBytes("echo 1 > " + cpu2online + "\n");
					localDataOutputStream.writeBytes("chmod 444 " + cpu2online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu2online + "\n");
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
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cpu2toggle", true);
				editor.commit();
			} catch (IOException e) {
				
				new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{

			KernelTuner.this.pd.dismiss();

		}

	}

	private class cpu3Toggle extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
			//Log.i("MyApp", "Background thread starting");

			File file = new File(CPU3_CURR_GOV);
			try
			{

				InputStream fIn = new FileInputStream(file);

				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 777 " + cpu3online + "\n");
					localDataOutputStream.writeBytes("echo 0 > " + cpu3online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu3online + "\n");
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
				}
				catch (IOException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				catch (InterruptedException e)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}

				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cpu3toggle", false);
				editor.commit();
				fIn.close();
			}

			catch (FileNotFoundException e)
			{
				//enable cpu1


				Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 666 " + cpu3online + "\n");
					localDataOutputStream.writeBytes("echo 1 > " + cpu3online + "\n");
					localDataOutputStream.writeBytes("chmod 444 " + cpu3online + "\n");
					localDataOutputStream.writeBytes("chown system " + cpu3online + "\n");
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				}
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("cpu3toggle", true);
				editor.commit();
			} catch (IOException e) {
				new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
				
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			KernelTuner.this.pd.dismiss();

		}

	}

	private class mountDebugFs extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{
			//Log.i("MyApp", "Background thread starting");

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
				localDataOutputStream
					.writeBytes("mount -t debugfs debugfs /sys/kernel/debug\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("KernelTuner: mounting debug fs");
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
			

			//KernelTuner.this.pd.dismiss();

		}

	}
	
	private class enableTempMonitor extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");
				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode\n");
				
				localDataOutputStream.writeBytes("echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode\n");
				localDataOutputStream.writeBytes("echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode\n");
				//Toast.makeText(KernelTuner.this, "App: Enabling temp monitor", Toast.LENGTH_SHORT).show();
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("KernelTuner: enable temp monitor");
			}
			catch (Exception e)
			{
				new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
			}

			return "";
		}



	}
	boolean first;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main);
		
	
		cpu0prog = (TextView)this.findViewById(R.id.ptextView3);
		cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
		cpu2prog = (TextView)this.findViewById(R.id.ptextView7);
	    cpu3prog = (TextView)this.findViewById(R.id.ptextView8);
		
		cpu0progbar = (ProgressBar)findViewById(R.id.progressBar1);
		cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		cpu2progbar = (ProgressBar)findViewById(R.id.progressBar3);
		cpu3progbar = (ProgressBar)findViewById(R.id.progressBar4);
		 
		 editor = preferences.edit();
		 /**
		  * Extract assets if first launch*/
		first = preferences.getBoolean(
				"first_launch", false);
		if (first == false) {
			CopyAssets();
		}
		
		tempLayout = (LinearLayout)findViewById(R.id.test1a);
		
			ActionBar actionBar = getSupportActionBar();
		
			actionBar.setSubtitle("Various kernel and system tuning");
			actionBar.setHomeButtonEnabled(false);
        
		
		
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		/**
		If auto update check is enabled check for updates
		*/
		File file = new File("/sys/kernel/debug");
		
		if(file.exists() && file.list().length>0){
			
			System.out.println("Mounting debug fs");
		}
		else{
			System.out.println("Debug fs already mounted");
			new mountDebugFs().execute();
		}
		
		/*boolean update = sharedPrefs.getBoolean("update", true);
		if (update == true)
		{
			new updateCheck().execute();
		}*/
		/*
		Enable temperature monitor
		*/
		if(enableTmp()==true){
		new enableTempMonitor().execute();
		}
		batteryLevel = (TextView) this.findViewById(R.id.textView42);
		batteryTemp = (TextView) this.findViewById(R.id.textView40);

		/**
		Get temperature unit from preferences
		*/
		tempPref = sharedPrefs.getString("temp", "celsius");


		/*
		Load ads if not disabled
		*/
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)this.findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

        /**
		Show changelog if application updated
		*/
		changelog();

		/**
		Read all available frequency steps
		*/
		
		freqlist = CPUInfo.frequencies();
		initialCheck();

		/***
		Create new thread that will loop and show current frequency for each core
		*/
		new Thread(new Runnable() {
				@Override
				public void run()
				{
					while (thread)
					{
						try
						{
							Thread.sleep(1000);
							mHandler.post(new Runnable() {

									@Override
									public void run()
									{

										
										ReadCPU0Clock();
										ReadCPU0maxfreq();
										cpuTemp();
									
										
										if (new File(cpu1online).exists())
										{


											ReadCPU1Clock();
											ReadCPU1maxfreq();

										}
										if (new File(cpu2online).exists())
										{
											ReadCPU2Clock();
											ReadCPU2maxfreq();


										}
										if (new File(cpu3online).exists())
										{
											ReadCPU3Clock();
											ReadCPU3maxfreq();

										}


									}
								});
						}
						catch (Exception e)
						{

						}
					}
				}
			}).start();

			
			
			/**
			Declare buttons and set onClickListener for each
			*/
			
		Button gpu = (Button)findViewById(R.id.button3);
		gpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{		
					Intent myIntent = new Intent(KernelTuner.this, Gpu.class);
					KernelTuner.this.startActivity(myIntent);

				}

			});

		Button voltage = (Button)findViewById(R.id.button6);
		voltage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Intent myIntent = new Intent(KernelTuner.this, VoltageActivity.class);
					KernelTuner.this.startActivity(myIntent);
				}

			});

		Button cpu = (Button)this.findViewById(R.id.button2);
		cpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Intent myIntent = new Intent(KernelTuner.this, CPUActivity.class);
					KernelTuner.this.startActivity(myIntent);
				}
			});

		Button tis = (Button)this.findViewById(R.id.button5);
		tis.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
                    String tisChoice = sharedPrefs.getString("tis_open_as", "ask");
				   if(tisChoice.equals("ask")){
					AlertDialog.Builder builder = new AlertDialog.Builder(
		                    KernelTuner.this);

						builder.setTitle("Display As");
						LayoutInflater inflater = (LayoutInflater)KernelTuner.this.getSystemService
						(Context.LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(R.layout.tis_dialog, null);
						ImageView list = (ImageView)view.findViewById(R.id.imageView1);
						ImageView chart = (ImageView)view.findViewById(R.id.imageView2);
						final CheckBox remember =(CheckBox)view.findViewById(R.id.checkBox1);
					 
						list.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								
								Intent myIntent = new Intent(KernelTuner.this, TISActivity.class);
								KernelTuner.this.startActivity(myIntent);
								if(remember.isChecked()){
								editor.putString("tis_open_as","list");
								editor.commit();
								}
								alert.dismiss();
								
							}
							
						});
						
						chart.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								
								Intent myIntent = new Intent(KernelTuner.this, TISActivityChart.class);
								KernelTuner.this.startActivity(myIntent);
								if(remember.isChecked()){
								editor.putString("tis_open_as","chart");
								editor.commit();
								}
								alert.dismiss();
								
							}
							
						});
						
						
						builder.setView(view);
						alert = builder.create();

						alert.show();
						}
						else if(tisChoice.equals("list")){
								Intent myIntent = new Intent(KernelTuner.this, TISActivity.class);
								KernelTuner.this.startActivity(myIntent);
						}
						else if(tisChoice.equals("chart")){
								Intent myIntent = new Intent(KernelTuner.this, TISActivityChart.class);
								KernelTuner.this.startActivity(myIntent);
						}
						
						

				}
			});

		
		Button mpdec = (Button)this.findViewById(R.id.button7);
		mpdec.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Intent myIntent = new Intent(KernelTuner.this, Mpdecision.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});



		Button buttongpu = (Button)this.findViewById(R.id.button4);
		buttongpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, MiscTweaks.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});


		Button cpu1toggle = (Button)this.findViewById(R.id.button1);
		cpu1toggle.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{


					KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, null, getResources().getString(R.string.applying_settings), true, true);
					new cpu1Toggle().execute();


				}});

		Button cpu2toggle = (Button)this.findViewById(R.id.button8);
		cpu2toggle.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{


					KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, null, getResources().getString(R.string.applying_settings), true, true);
					new cpu2Toggle().execute();


				}});

		Button cpu3toggle = (Button)this.findViewById(R.id.button9);
		cpu3toggle.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{


					KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, null, getResources().getString(R.string.applying_settings), true, true);
					new cpu3Toggle().execute();


				}});

		Button governor = (Button)findViewById(R.id.button10);
		governor.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
					Intent myIntent = new Intent(KernelTuner.this, GovernorActivity.class);
					KernelTuner.this.startActivity(myIntent);

				}

			});

		Button oom = (Button)this.findViewById(R.id.button13);
		oom.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, OOM.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button profiles = (Button)this.findViewById(R.id.button12);
		profiles.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, Profiles.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button thermald = (Button)this.findViewById(R.id.button11);
		thermald.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, Thermald.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button about = (Button)this.findViewById(R.id.button15);
		about.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, About.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button sys = (Button)this.findViewById(R.id.button14);
		sys.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, SystemInfo.class);
					KernelTuner.this.startActivity(myIntent);
					

				}
			});

startCpuLoadThread();

if(preferences.getBoolean("notificationService", false)==true && isNotificationServiceRunning()==false){
	startService(new Intent(KernelTuner.this, NotificationService.class));
}
else if(preferences.getBoolean("notificationService", false)==false && isNotificationServiceRunning()==true){
	stopService(new Intent(KernelTuner.this, NotificationService.class));
}
	}


	@Override
	public void onPause()
	{

		super.onPause();

	}

	@Override
	protected void onResume()
	{
		System.out.println("ktuner test"+preferences.getString("notif", "freq"));
		/**
		Register BroadcastReceiver that will listen for battery changes and update ui
		*/
		this.registerReceiver(this.mBatInfoReceiver, 
							  new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		

		/**
		I init.d is selected for restore settings on boot make inid.d files else remove them
		*/

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String boot = sharedPrefs.getString("boot", "init.d");
		if (boot.equals("init.d"))
		{
			initdExport();
		}
		else
		{
			new Initd().execute(new String[] {"rm"});
		}



		super.onResume();

	}

	@Override
	public void onStop()
	{
		
		/**
		Unregister receiver
		*/
		if (mBatInfoReceiver != null)
		{
			unregisterReceiver(mBatInfoReceiver);

			mBatInfoReceiver = null;
		}

		super.onStop();

	}
	@Override
	public void onDestroy()
	{

		/**
		set thread false so that cpu info thread stop repeating
		*/
		thread = false;
		
		/**
		Update widgets after application is closed
		*/
		AppWidgetBig updateBig = new AppWidgetBig();
		Context context = this;
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_4x4);
		ComponentName thisWidget = new ComponentName(context, AppWidgetBig.class);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		int[] appWidgetIds = null;
		updateBig.onUpdate(context, appWidgetManager,  appWidgetIds);


		AppWidget updateSmall = new AppWidget();
		RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
		ComponentName thisWidgetSmall = new ComponentName(context, AppWidget.class);
		appWidgetManager.updateAppWidget(thisWidgetSmall, remoteViewsSmall);

		updateSmall.onUpdate(context, appWidgetManager,  appWidgetIds);

		super.onDestroy();

	}

	public void setCpuLoad(){
		TextView cpuLoadTxt = (TextView)findViewById(R.id.textView1);

		ProgressBar cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(String.valueOf(load) + "%");
		
	}
	
	
public void startCpuLoadThread() {
		// Do something long
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while(thread) {
					try {
						RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
						String load = reader.readLine();

						String[] toks = load.split(" ");

						long idle1 = Long.parseLong(toks[5]);
						long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						try {
							Thread.sleep(360);
						} catch (Exception e) {}

						reader.seek(0);
						load = reader.readLine();
						reader.close();

						toks = load.split(" ");

						long idle2 = Long.parseLong(toks[5]);
						long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
					load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {
							@Override
							public void run() {
							
								setCpuLoad();
								//	progress.setProgress(value);
							}
						});
				}
			}
		};
		new Thread(runnable).start();
	}
	
	
	public void changelog()
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String versionpref = preferences.getString("version", "");

		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String	version = pInfo.versionName;
			if (!versionpref.equals(version))
			{

				Intent myIntent = new Intent(KernelTuner.this, Changelog.class);
				KernelTuner.this.startActivity(myIntent);
					if (first == true) {
			            CopyAssets();
	             	}
				

			}	
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("version", version);
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
		}


	}

	
	

	/**
	CPU Temperature
	*/

	public void cpuTemp()
	{
		cputemptxt = (TextView)findViewById(R.id.textView38);
		

		try
		{

			File myFile = new File(
				"/sys/class/thermal/thermal_zone1/temp");
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			cputemp = aBuffer.trim();
			tempLayout.setVisibility(View.VISIBLE);

			/**
			If fahrenheit is selected in settings, convert temp to fahreinheit
			*/
			if (tempPref.equals("fahrenheit"))
			{
				cputemp = String.valueOf((int)(Double.parseDouble(cputemp) * 1.8) + 32);
		  		cputemptxt.setText(cputemp + "°F");
		  		int temp = Integer.parseInt(cputemp);

		  		if (temp < 113)
				{
					cputemptxt.setTextColor(Color.GREEN);
					//cpuTempWarningStop();
				}
				else if (temp >= 113 && temp < 138)
				{
					cputemptxt.setTextColor(Color.YELLOW);
					//cpuTempWarningStop();
				}

				else if (temp >= 138)
				{
					//cpuTempWarning();
					cputemptxt.setTextColor(Color.RED);

				}
			}

			else if (tempPref.equals("celsius"))
			{
				cputemptxt.setText(cputemp + "°C");
				int temp = Integer.parseInt(cputemp);
				if (temp < 45)
				{
					cputemptxt.setTextColor(Color.GREEN);
					//cpuTempWarningStop();
				}
				else if (temp >= 45 && temp <= 59)
				{
					cputemptxt.setTextColor(Color.YELLOW);
					//cpuTempWarningStop();
				}

				else if (temp > 59)
				{
					//cpuTempWarning();
					cputemptxt.setTextColor(Color.RED);

				}
			}
			/**
			If kelvin is selected in settings convert cpu temp to kelvin
			*/
			else if (tempPref.equals("kelvin"))
			{
				cputemp = String.valueOf((int)(Double.parseDouble(cputemp) + 273.15));
				
				cputemptxt.setText(cputemp + "°K");
				System.out.println(cputemp);
				int temp = Integer.parseInt(cputemp);
				System.out.println("temp int" + temp);
				if (temp < 318)
				{
					cputemptxt.setTextColor(Color.GREEN);
					//cpuTempWarningStop();
				}
				else if (temp >= 318 && temp <= 332)
				{
					cputemptxt.setTextColor(Color.YELLOW);
					//cpuTempWarningStop();
				}

				else if (temp > 332)
				{
					//cpuTempWarning();
					cputemptxt.setTextColor(Color.RED);

				}
			}

			myReader.close();

		}
		catch (Exception e2)
		{

			tempLayout.setVisibility(View.GONE);

		}
	}

	public void initialCheck()
	{

		/**
		Show/hide certain Views depending on number of cpus
		*/
		if (CPUInfo.cpu1Online() == true)
		{
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.VISIBLE);
		}
		else
		{
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.GONE);
		}
		if (CPUInfo.cpu2Online() == true)
		{
			Button b3 = (Button) findViewById(R.id.button8);
			b3.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.VISIBLE);
		}
		else
		{
			Button b3 = (Button) findViewById(R.id.button8);
			b3.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar3);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView5);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView7);
			tv4.setVisibility(View.GONE);
		}
		if (CPUInfo.cpu3Online() == true)
		{
			Button b4 = (Button) findViewById(R.id.button9);
			b4.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.VISIBLE);
		}
		else
		{
			Button b4 = (Button) findViewById(R.id.button9);
			b4.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar4);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView6);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView8);
			tv4.setVisibility(View.GONE);
		}

		/**
		Check for certain files in sysfs and if they doesnt exists hide depending views
		*/
		File file4 = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
		File file5 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
		try
		{
			InputStream fIn = new FileInputStream(file4);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			try
			{
				InputStream fIn = new FileInputStream(file5);
				fIn.close();
			}
			catch (FileNotFoundException e2)
			{
				Button cpu = (Button)findViewById(R.id.button2);
				cpu.setVisibility(View.GONE);
			} catch (IOException e1) {
				
			}


		} catch (IOException e) {
			
		}

		File file = new File(CPUInfo.VOLTAGE_PATH);
		try
		{
			InputStream fIn = new FileInputStream(file);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
		 	File file2 = new File(CPUInfo.VOLTAGE_PATH_TEGRA_3);
			try
			{
				InputStream fIn = new FileInputStream(file2);
				fIn.close();
			}
			catch (FileNotFoundException ex)
			{ 
				Button voltage = (Button)findViewById(R.id.button6);
				voltage.setVisibility(View.GONE);

			} catch (IOException e1) {
				
			}

		} catch (IOException e) {
			
		}

		File file2 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
		try
		{
			InputStream fIn = new FileInputStream(file2);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			Button times = (Button)findViewById(R.id.button5);
			times.setVisibility(View.GONE);

		} catch (IOException e) {
			
		}

		File file3 = new File("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
		try
		{
			InputStream fIn = new FileInputStream(file3);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			Button gpu = (Button)findViewById(R.id.button3);
			gpu.setVisibility(View.GONE);

		} catch (IOException e) {
			
		}

		File file6 = new File("/sys/kernel/msm_mpdecision/conf/enabled");
		try
		{
			InputStream fIn = new FileInputStream(file6);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			Button mpdec = (Button)findViewById(R.id.button7);
			mpdec.setVisibility(View.GONE);

		} catch (IOException e) {
			
		}

		File file7 = new File("/sys/kernel/msm_thermal/conf/allowed_low_freq");
		try
		{
			InputStream fIn = new FileInputStream(file7);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			Button td = (Button)findViewById(R.id.button11);
			td.setVisibility(View.GONE);

		} catch (IOException e) {
		
		}
		
		



	}

	
	/**
	Create init.d files and export them to private application folder
	*/

	public void initdExport()
	{
	
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String gpu3d = sharedPrefs.getString("gpu3d", "");
		String gpu2d = sharedPrefs.getString("gpu2d", "");
	
		String hw = sharedPrefs.getString("hw", "");
		String cdepth = sharedPrefs.getString("cdepth", "");
		String cpu1min = sharedPrefs.getString("cpu1min", "");
		String cpu1max = sharedPrefs.getString("cpu1max", "");
		String cpu0max = sharedPrefs.getString("cpu0max", "");
		String cpu0min = sharedPrefs.getString("cpu0min", "");
		String cpu3min = sharedPrefs.getString("cpu3min", "");
		String cpu3max = sharedPrefs.getString("cpu3max", "");
		String cpu2max = sharedPrefs.getString("cpu2max", "");
		String cpu2min = sharedPrefs.getString("cpu2min", "");
	
		String fastcharge = sharedPrefs.getString("fastcharge", "");
		String mpdecisionscroff = sharedPrefs.getString("mpdecisionscroff", "");
		String backbuff = sharedPrefs.getString("backbuf", "");
		String vsync = sharedPrefs.getString("vsync", "");
		String led = sharedPrefs.getString("led", "");
		String cpu0gov = sharedPrefs.getString("cpu0gov", "");
		String cpu1gov = sharedPrefs.getString("cpu1gov", "");
		String cpu2gov = sharedPrefs.getString("cpu2gov", "");
		String cpu3gov = sharedPrefs.getString("cpu3gov", "");
		String io = sharedPrefs.getString("io", "");
		String sdcache = sharedPrefs.getString("sdcache", "");
	
	
		String delaynew = sharedPrefs.getString("delaynew", "");
		String pausenew = sharedPrefs.getString("pausenew", "");
		String thruploadnew = sharedPrefs.getString("thruploadnew", "");
		String thrupmsnew = sharedPrefs.getString("thrupmsnew", "");
		String thrdownloadnew = sharedPrefs.getString("thrdownloadnew", "");
		String thrdownmsnew = sharedPrefs.getString("thrdownmsnew", "");
		String ldt = sharedPrefs.getString("ldt", "");
		String s2w = sharedPrefs.getString("s2w", "");
		String s2wStart = sharedPrefs.getString("s2wStart", "");
		String s2wEnd = sharedPrefs.getString("s2wEnd", "");
	
		String p1freq = sharedPrefs.getString("p1freq", "");
		String p2freq = sharedPrefs.getString("p2freq", "");
		String p3freq = sharedPrefs.getString("p3freq", "");
		String p1low = sharedPrefs.getString("p1low", "");
		String p1high = sharedPrefs.getString("p1high", "");
		String p2low = sharedPrefs.getString("p2low", "");
		String p2high = sharedPrefs.getString("p2high", "");
		String p3low = sharedPrefs.getString("p3low", "");
		String p3high = sharedPrefs.getString("p3high", "");
		boolean swap = sharedPrefs.getBoolean("swap", false);
		String swapLocation = sharedPrefs.getString("swap_location", "");
		String swappiness = sharedPrefs.getString("swappiness", "");
		String oom = sharedPrefs.getString("oom", "");
	
		StringBuilder gpubuilder = new StringBuilder();
	
		gpubuilder.append("#!/system/bin/sh");
		gpubuilder.append("\n");
		if (!gpu3d.equals(""))
		{
			gpubuilder.append("echo " + "\"" + gpu3d + "\"" + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
			gpubuilder.append("\n");
		}
		if (!gpu2d.equals(""))
		{
			gpubuilder.append("echo " + "\"" + gpu2d + "\"" + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
			gpubuilder.append("\n");
			gpubuilder.append("echo " + "\"" + gpu2d + "\"" + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
			gpubuilder.append("\n");
	
		}
	
	
		String gpu = gpubuilder.toString();
	
		StringBuilder cpubuilder = new StringBuilder();
	
		cpubuilder.append("#!/system/bin/sh");
		cpubuilder.append("\n");
		/**
		 * cpu0
		 * */
		if (!cpu0gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu0gov + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n");
		}
		if (!cpu0max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu0max + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" );
		}
		if (!cpu0min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu0min + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n");
		}
		/**
		 * cpu1
		 * */
		if (!cpu1gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu1gov + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n"+
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" );
		}
		if (!cpu1max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu1max + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n");
		}
		if (!cpu1min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu1min + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n");
		}
	
		/**
		 * cpu2
		 * */
		if (!cpu2gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu2gov + "\"" + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_governor \n" );
		}
		if (!cpu2max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu2max + "\"" + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq \n");
		}
		if (!cpu2min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu2min + "\"" + " > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq \n\n");
		}
		/**
		 * cpu3
		 * */
	
		if (!cpu3gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu3gov + "\"" + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_governor \n");
		}
		if (!cpu3max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu3max + "\"" + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq \n");
		}
		if (!cpu3min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu3min + "\"" + " > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq \n\n");
		}
		List<String> govSettings = CPUInfo.govSettings();
		List<String> availableGovs = CPUInfo.availableGovs();
	
		for (String s : availableGovs)
		{
			for (String st : govSettings)
			{
				String temp = sharedPrefs.getString(s + "_" + st, "");
	
	 		    if (!temp.equals(""))
				{
	 		    	cpubuilder.append("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
	 		    	cpubuilder.append("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
	
	 		    }
			}
		}
		String cpu = cpubuilder.toString();
	
		StringBuilder miscbuilder = new StringBuilder();
	
		miscbuilder.append("#!/system/bin/sh \n\n" + "#mount debug filesystem\n" +
						   "mount -t debugfs debugfs /sys/kernel/debug \n\n");
		if (!vsync.equals(""))
		{
			miscbuilder.append("#vsync\n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable \n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/backbuff \n" +
							   "echo " + "\"" + vsync + "\"" + " > /sys/kernel/debug/msm_fb/0/vsync_enable \n" +
							   "echo " + "\"" + hw + "\"" + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
							   "echo " + "\"" + backbuff + "\"" + " > /sys/kernel/debug/msm_fb/0/backbuff \n\n");
		}
		if (!led.equals(""))
		{
			miscbuilder.append("#capacitive buttons backlight\n" + "chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n" +
							   "echo " + "\"" + led + "\"" + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n\n");
		}
		if (!fastcharge.equals(""))
		{
			miscbuilder.append("#fastcharge\n" + "chmod 777 /sys/kernel/fast_charge/force_fast_charge \n" +
							   "echo " + "\"" + fastcharge + "\"" + " > /sys/kernel/fast_charge/force_fast_charge \n\n");
		}
		if (!cdepth.equals(""))
		{
			miscbuilder.append("#color depth\n" + "chmod 777 /sys/kernel/debug/msm_fb/0/bpp \n" +
							   "echo " + "\"" + cdepth + "\"" + " > /sys/kernel/debug/msm_fb/0/bpp \n\n");
		}
	
		if (!mpdecisionscroff.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core \n" +
							   "echo " + "\"" + mpdecisionscroff + "\"" + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core \n");
		}
		if (!delaynew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/delay \n" +
							   "echo " + "\"" + delaynew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/delay \n");
		}
		if (!pausenew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/pause \n" +
							   "echo " + "\"" + pausenew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/pause \n");
		}
		if (!thruploadnew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n" +
							   "echo " + "\"" + thruploadnew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up \n");
	
		}
		if (!thrdownloadnew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n" +
							   "echo " + "\"" + thrdownloadnew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down \n");
	
		}
		if (!thrupmsnew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_up\n" +
							   "echo " + "\"" + thrupmsnew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_up \n");
		}
		if (!thrdownmsnew.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_down\n" +
							   "echo " + "\"" + thrdownmsnew.trim() + "\"" + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_down \n\n");
		}
		if (!sdcache.equals(""))
		{
			miscbuilder.append("#sd card cache size\n" +
							   "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n" +
							   "chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n" +
							   "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/block/mmcblk1/queue/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n\n");
		}
		if (!io.equals(""))
		{
			miscbuilder.append("#IO scheduler\n" +
							   "chmod 777 /sys/block/mmcblk0/queue/scheduler \n" +
							   "chmod 777 /sys/block/mmcblk1/queue/scheduler \n" +
							   "echo " + "\"" + io + "\"" + " > /sys/block/mmcblk0/queue/scheduler \n" +
							   "echo " + "\"" + io + "\"" + " > /sys/block/mmcblk1/queue/scheduler \n\n");
		}
		if (!ldt.equals(""))
		{
			miscbuilder.append("#Notification LED Timeout\n" +
							   "chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n" +
							   "echo " + "\"" + ldt + "\"" + " > /sys/kernel/notification_leds/off_timer_multiplier\n\n");
		}
		if (!s2w.equals(""))
		{
			miscbuilder.append("#Sweep2Wake\n" +
							   "chmod 777 /sys/android_touch/sweep2wake\n" +
							   "echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake\n\n");
		}
		if (!s2wStart.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/android_touch/sweep2wake_startbutton\n" +
							   "echo " + s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n" +
							   "chmod 777 /sys/android_touch/sweep2wake_endbutton\n" +
							   "echo " + s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n\n");
		}
	
		if (!p1freq.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq\n" +
							   "echo " + "\"" + p1freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_freq\n");
		}
		if (!p2freq.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq\n" +
							   "echo " + "\"" + p2freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq\n");
		}
		if (!p3freq.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq\n" +
							   "echo " + "\"" + p3freq.trim() + "\"" + " > /sys/kernel/msm_thermal/conf/allowed_max_freq\n");
		}
		if (!p1low.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low\n" +
							   "echo " + "\"" + p1low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_low\n");
		}
		if (!p1high.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high\n" +
							   "echo " + "\"" + p1high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_low_high\n");
		}
		if (!p2low.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low\n" +
							   "echo " + "\"" + p2low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_low\n");
		}
		if (!p2high.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high\n" +
							   "echo " + "\"" + p2high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_mid_high\n");
		}
		if (!p3low.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_low\n" +
							   "echo " + "\"" + p3low.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_high_low\n");
		}
		if (!p3high.trim().equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_thermal/conf/allowed_high_high\n" +
							   "echo " + "\"" + p3high.trim() +  "\"" + " > /sys/kernel/msm_thermal/conf/allowed_high_high\n\n");
		}
		if (swap == true)
		{
			miscbuilder.append("echo " + swappiness + " > /proc/sys/vm/swappiness\n"
							   + "swapon " + swapLocation.trim() + "\n\n"
							   );
	
	
		}
		else if (swap == false)
		{
			miscbuilder.append("swapoff " + swapLocation.trim() + "\n\n");
	
		}
		if(!oom.equals(""))
		{
			miscbuilder.append("echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree\n");
	
		}
		miscbuilder.append("#Umount debug filesystem\n" +
						   "umount /sys/kernel/debug \n");
		String misc = miscbuilder.toString();
	
	
	
		StringBuilder voltagebuilder = new StringBuilder();
		voltagebuilder.append("#!/system/bin/sh \n");
		for (String s : CPUInfo.voltageFreqs())
		{
			String temp = sharedPrefs.getString("voltage_" + s, "");
		    if (!temp.equals(""))
			{
				voltagebuilder.append("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
		    }
		}
		String voltage = voltagebuilder.toString();
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktcputweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(cpu);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			new LogWriter().execute(new String[] {getClass().getName(), ioe.getMessage()});
		}
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktgputweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(gpu);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			new LogWriter().execute(new String[] {getClass().getName(), ioe.getMessage()});
		}
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktmisctweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(misc);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			new LogWriter().execute(new String[] {getClass().getName(), ioe.getMessage()});
		}
	
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktvoltage",
												   MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(voltage);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			new LogWriter().execute(new String[] {getClass().getName(), ioe.getMessage()});
		} 
		new Initd().execute(new String[] {"apply"});
	}


	/**
	Read current cpu0 frequency
	*/
	public void ReadCPU0Clock()
	{


		try
		{

			File myFile = new File(CPU0_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			freqcpu0 = aBuffer;
			myReader.close();


		}
		catch (Exception e)
		{
			freqcpu0 = "offline";
		}


		cpu0progress();
		cpu0update();

	}

	/**
	 Read current cpu1 frequency
	 */
	public void ReadCPU1Clock()
	{


		try
		{

			File myFile = new File(CPU1_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);



			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			freqcpu1= aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			freqcpu1 = "offline";

		}


		cpu1progress();
		cpu1update();

	}

	/**
	 Read current cpu2 frequency
	 */
	public void ReadCPU2Clock()
	{


		try
		{

			File myFile = new File(CPU2_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			freqcpu2 = aBuffer;
			myReader.close();


		}
		catch (Exception e)
		{
			freqcpu2 = "offline";
		}


		cpu2progress();
		cpu2update();

	}


	/**
	 Read current cpu3 frequency
	 */
	public void ReadCPU3Clock()
	{


		try
		{

			File myFile = new File(CPU3_CURR_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);



			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			freqcpu3 = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			freqcpu3 = "offline";

		}


		cpu3progress();
		cpu3update();

	}


    /**
	Read max frequency of cpu0
	*/
	public void ReadCPU0maxfreq()
	{


		try
		{

			File myFile = new File(CPU0_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}


			cpu0max = aBuffer;
			myReader.close();


		}
		catch (Exception e)
		{

		}

	}
	/**
	 Read max frequency of cpu1
	 */
	public void ReadCPU1maxfreq()
	{


		try
		{

			File myFile = new File(CPU1_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}


			cpu1max = aBuffer;
			myReader.close();

			;


		}
		catch (Exception e)
		{

		}

	}
	/**
	 Read max frequency of cpu2
	 */
	public void ReadCPU2maxfreq()
	{


		try
		{

			File myFile = new File(CPU2_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}


			cpu2max = aBuffer;
			myReader.close();


		}
		catch (Exception e)
		{

		}

	}

	/**
	 Read max frequency of cpu1
	 */
	public void ReadCPU3maxfreq()
	{


		try
		{

			File myFile = new File(CPU3_MAX_FREQ);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}


			cpu3max = aBuffer;
			myReader.close();


		}
		catch (Exception e)
		{

		}

	}

	/**
	Update UI with current frequency
	*/
	public void cpu0update()
	{

		
		cpu0prog.setText(freqcpu0.trim());
	}

	/**
	Set Progress of progressBar
	*/
	public void cpu0progress()
	{
		if (freqlist != null)
		{
		
			cpu0progbar.setMax(freqlist.indexOf(cpu0max.trim()) + 1);
			cpu0progbar.setProgress(freqlist.indexOf(freqcpu0.trim()) + 1);
		}

	}

	public void cpu1update()
	{

	
		cpu1prog.setText(freqcpu1.trim());
	}
	public void cpu1progress()
	{
		if (freqlist != null)
		{
			
			cpu1progbar.setMax(freqlist.indexOf(cpu1max.trim()) + 1);
			cpu1progbar.setProgress(freqlist.indexOf(freqcpu1.trim()) + 1);
		}
	}

	public void cpu2update()
	{

		
		cpu2prog.setText(freqcpu2.trim());
	}

	public void cpu2progress()
	{
		if (freqlist != null)
		{
			
			cpu2progbar.setMax(freqlist.indexOf(cpu2max.trim()) + 1);
			cpu2progbar.setProgress(freqlist.indexOf(freqcpu2.trim()) + 1);
		}
	}

	public void cpu3update()
	{

		
		cpu3prog.setText(freqcpu3.trim());
	}
	public void cpu3progress()
	{
		if (freqlist != null)
		{
			

			cpu3progbar.setMax(freqlist.indexOf(cpu3max.trim()) + 1);
			cpu3progbar.setProgress(freqlist.indexOf(freqcpu3.trim()) + 1);
		}
	}




	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
	{

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.options, menu);

		return true;
	}
	



	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{

		if (item.getItemId() == R.id.settings)
		{
			startActivity(new Intent(this, Preferences.class));

		}
		if (item.getItemId() == R.id.changelog)
		{
			startActivity(new Intent(this, Changelog.class));

		}
		
		if (item.getItemId() == R.id.check) {
        startActivity(new Intent(this, CompatibilityCheck.class));
        
		}	
	
		if (item.getItemId() == R.id.log) {
      
		AlertDialog.Builder builder = new AlertDialog.Builder(
                KernelTuner.this);

builder.setTitle(getResources().getString(R.string.dump));

LinearLayout ln = new LinearLayout(KernelTuner.this);
ln.setOrientation(LinearLayout.VERTICAL);
TextView tv = new TextView(KernelTuner.this);
TextView tv2 = new TextView(KernelTuner.this);
ln.addView(tv);
ln.addView(tv2);

tv.setText(R.string.system_dump_text1);
tv2.setText(R.string.system_dump_text2);
tv2.setTextColor(Color.RED);


builder.setIcon(R.drawable.ic_menu_paste_holo_dark);

builder.setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
        @Override
		public void onClick(DialogInterface dialog, int which) {

   StringBuilder builder = new StringBuilder();
   builder.append(CPUInfo.deviceInfoDebug());
   builder.append(CPUInfo.voltDebug());
   builder.append(CPUInfo.tisDebug());
   builder.append(CPUInfo.frequenciesDebug());
   builder.append(CPUInfo.logcat());
   
   
   try
	{ 
	   File myFile = new File(Environment.getExternalStorageDirectory()+"/ktuner_log.txt");
       	myFile.createNewFile();
		FileOutputStream fOut = new FileOutputStream(myFile);;
		OutputStreamWriter osw = new OutputStreamWriter(fOut); 
		osw.write(builder.toString());        
		osw.flush();
		osw.close();
		
		mToast = Toast.makeText(KernelTuner.this, getResources().getString(R.string.log_writen)+myFile.toString(), Toast.LENGTH_LONG);
		mToast.show();
	}
	catch (IOException ioe)
	{
		ioe.printStackTrace();
		mToast = Toast.makeText(KernelTuner.this, getResources().getString(R.string.log_error) + ioe.getMessage() , Toast.LENGTH_LONG);
		mToast.show();
	}
        
        }
});
builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
    @Override
	public void onClick(DialogInterface dialog, int which) {
  
    }
});
builder.setView(ln);
AlertDialog alert = builder.create();
		
		alert.show();
        
    }
	
		if (item.getItemId() == R.id.swap) {
			startActivity(new Intent(this, Swap.class));

		}
		if (item.getItemId() == R.id.scanner) {
		Intent myIntent = new Intent(KernelTuner.this, SDScannerConfigActivity.class);
		KernelTuner.this.startActivity(myIntent);
		}
		

		return super.onOptionsItemSelected(item);
	}

	/**
	Press back button twice to exit application
	*/
	@Override
	public void onBackPressed()
	{
		if (mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000)
		{
			mToast = Toast.makeText(this, getResources().getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT);
			mToast.show();
			mLastBackPressTime = java.lang.System.currentTimeMillis();
		}
		else
		{
			if (mToast != null)
				mToast.cancel();
			KernelTuner.this.finish();


			mLastBackPressTime = 0;
		}
	}

	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		File file;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				file = new File(this.getFilesDir().getAbsolutePath() + "/"
						+ filename);
				out = new FileOutputStream(file);
				copyFile(in, out);
				in.close();
				Runtime.getRuntime().exec("chmod 755 " + file);
				file.setExecutable(false);
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
		}
		
		editor.putBoolean("first_launch", true);
		editor.commit();
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	
	private boolean isNotificationServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (NotificationService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	
	
}
