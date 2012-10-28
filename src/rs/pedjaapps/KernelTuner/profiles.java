package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class profiles extends Activity
{
	List<String> profilenames;
	public SharedPreferences preferences;
	public String currentprofile;
	DatabaseHandler db;
	private static final int GET_CODE = 0;
	Spinner spinner;
	private ProgressDialog pd = null;
	private Object data = null;
	public String thrupload;
	public String thrdownload;
	public String cpu0min;
	public String cpu1min;
	public String cpu0max;
	public String cpu1max;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
	public String gpu3d;
	public String gpu2d;
	public String vsync;
	public String cpu1;
	public boolean cpu1check;
	public String name;


	private class apply extends AsyncTask<String, Void, Object>
	{
		String aBuffer = "";
        @Override
		protected Object doInBackground(String... args)
		{
            //Log.i("MyApp", "Background thread starting");
            Process localProcess;
            Profile profile = db.getProfileByName(currentprofile);
      		try
			{
 				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				if (profile.getNOC().equals("1"))
				{
					localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
				}
				else if (profile.getNOC().equals("2"))
				{
					localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
				} 
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu0gov() + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu0min() + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu0max() + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu1gov() + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu1min() + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n");
				localDataOutputStream.writeBytes("echo \"" + profile.getCpu1max() + "\" > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk\n");
				localDataOutputStream.writeBytes("echo " + profile.getGpu2d() + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + profile.getGpu2d() + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("echo " + profile.getGpu3d() + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n");

				localDataOutputStream.writeBytes("echo " + profile.getVsync() + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n");
				if (profile.getVsync().equals("1"))
				{
					localDataOutputStream.writeBytes("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
					localDataOutputStream.writeBytes("echo " + 3 + " > /sys/kernel/debug/msm_fb/0/backbuff\n");
				}
				else if (profile.getVsync().equals("0"))
				{
					localDataOutputStream.writeBytes("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n");
					localDataOutputStream.writeBytes("echo " + 4 + " > /sys/kernel/debug/msm_fb/0/backbuff\n");

				}


				localDataOutputStream.writeBytes("echo " + profile.getMtu() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_up\n");
				localDataOutputStream.writeBytes("echo " + profile.getMtd() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_down\n");

				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
      		}
			catch (IOException e1)
			{
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
			catch (InterruptedException e1)
			{
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}



            return aBuffer;
        }

        @Override
		protected void onPostExecute(Object result)
		{

        	pd.dismiss();
            profiles.this.data = result;

        }
	}

	private class SaveCurrent extends AsyncTask<String, Void, Object>
	{
		String aBuffer = "";
        @Override
		protected Object doInBackground(String... args)
		{
            //Log.i("MyApp", "Background thread starting");

            File file = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor");
			try
			{

				InputStream fIn = new FileInputStream(file);
				cpu1check = true;

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
					localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
					cpu1check = false;
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

            try
			{

	 			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq");
	 			FileInputStream fIn = new FileInputStream(myFile);	
	 			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
	 			String aDataRow = "";
	 			String aBuffer = "";
	 			while ((aDataRow = myReader.readLine()) != null)
				{
	 				aBuffer += aDataRow + "\n";
	 			}

	 			cpu0min = aBuffer.trim();
	 			myReader.close();

	 		}
			catch (Exception e)
			{
	 			cpu0min = "err";
	 		}

			try
			{

	  			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
	  			FileInputStream fIn = new FileInputStream(myFile);	
	  			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
	  			String aDataRow = "";
	  			String aBuffer = "";
	  			while ((aDataRow = myReader.readLine()) != null)
				{
	  				aBuffer += aDataRow + "\n";
	  			}

	  			cpu0max = aBuffer.trim();
	  			myReader.close();

	  		}
			catch (Exception e)
			{
	  			cpu0max = "err";
	  		}

			try
			{

	  			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq");
	  			FileInputStream fIn = new FileInputStream(myFile);

	  			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
	  			String aDataRow = "";
	  			String aBuffer = "";
	  			while ((aDataRow = myReader.readLine()) != null)
				{
	  				aBuffer += aDataRow + "\n";
	  			}

	  			cpu1min = aBuffer.trim();
	  			myReader.close();

	  		}
			catch (Exception e)
			{
	  			cpu1min = "err";
	  		}

			try
			{

	  			File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq");
	  			FileInputStream fIn = new FileInputStream(myFile);		
	  			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
	  			String aDataRow = "";
	  			String aBuffer = "";
	  			while ((aDataRow = myReader.readLine()) != null)
				{
	  				aBuffer += aDataRow + "\n";
	  			}

	  			cpu1max = aBuffer.trim();
	  			myReader.close();

	  		}
			catch (Exception e)
			{
	  			cpu1max = "err";
	  		}

			try
			{

	   			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
	   			FileInputStream fIn = new FileInputStream(myFile);

	   			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
	   			String aDataRow = "";
	   			String aBuffer = "";
	   			while ((aDataRow = myReader.readLine()) != null)
				{
	   				aBuffer += aDataRow + "\n";
	   			}

	   			curentgovernorcpu0 = aBuffer.trim();
	   			myReader.close();

	   		}
			catch (Exception e)
			{
	   			curentgovernorcpu0 = "err";
	   		}

			try
			{

				File myFile = new File("/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				curentgovernorcpu1 = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				curentgovernorcpu1 = "err";
			}
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

	   			gpu3d = aBuffer.trim();
	   			myReader.close();



	   		}
			catch (Exception e)
			{
	   			gpu3d = "err";

	   		}

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

				gpu2d = aBuffer.trim();

				myReader.close();

			}
			catch (Exception e)
			{
	 			gpu2d = "err";
			}
			try
			{
				String aBuffer = "";
				File myFile = new File("/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				vsync = aBuffer.trim();
				myReader.close();

			}
			catch (Exception e)
			{
				vsync = "err";
			}
			try
			{

				File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_up");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				thrupload = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{
				thrupload = "err";

			}

			try
			{

				File myFile = new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_down");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null)
				{
					aBuffer += aDataRow + "\n";
				}

				thrdownload = aBuffer;
				myReader.close();

			}
			catch (Exception e)
			{
				thrdownload = "err";

			}

			if (cpu1check == true)
			{
				cpu1 = "2";

			}
			else if (cpu1check == false)
			{
				cpu1 = "1";
			}

			if (cpu1check == false)
			{
	  			Process localProcess;
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
					localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
					localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}



            return aBuffer;
        }

        @Override
		protected void onPreExecute()
		{
            super.onPreExecute();
			//  DualCore.this.pd = ProgressDialog.show(DualCore.this, "Working..", "Checking for updates...", true, false );
			//	pd.setCancelable(true);

    		pd = ProgressDialog.show(
    			profiles.this,
    			"Working...",
    			"Checking for updates...",
    			true,
    			true




            );
        }

        @Override
		protected void onPostExecute(Object result)
		{
        	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
			myAdap.clear();
			db.addProfile(new Profile(name, cpu0min, 
									  cpu0max, 
									  cpu1min,
									  cpu1max,
									  curentgovernorcpu0,
									  curentgovernorcpu1,
									  gpu2d,
									  gpu3d,
									  vsync,
									  cpu1,
									  thrupload,
									  thrdownload));

			getprofiles();
			//spinnerProfiles(); 

			int spinnerPosition = myAdap.getPosition(name);

			//set the default according to value
			spinner.setSelection(spinnerPosition);
        	pd.dismiss();
            profiles.this.data = result;

        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profiles);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		Button current = (Button)findViewById(R.id.button1);
		current.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle("Profile Name:");

					builder.setMessage("Enter Profile Name: ");

					builder.setIcon(R.drawable.ic_menu_cc);

					final EditText input = new EditText(arg0.getContext());//)layout.findViewById(R.id.editText2);




					input.setGravity(Gravity.CENTER_HORIZONTAL);
					//input.selectAll();
					builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								name = String.valueOf(input.getText());
								new SaveCurrent().execute();

							}
						});
					builder.setView(input);

					AlertDialog alert = builder.create();

					alert.show();
				}

			});

		db = new DatabaseHandler(this);

		/**
		 * CRUD Operations
		 * */
		// Inserting Contacts
		//Log.d("Insert: ", "Inserting ..");
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean createpreofilesfirsttime = preferences.getBoolean("profiles", false);
		if (createpreofilesfirsttime == false)
		{
			db.addProfile(new Profile("Battery", "192000", 
									  "1188000", 
									  "192000",
									  "1188000",
									  "conservative",
									  "conservative",
									  "160000000",
									  "200000000",
									  "1",
									  "1",
									  "80",
									  "5"));
			db.addProfile(new Profile("Balanced", "192000", 
									  "1188000", 
									  "192000",
									  "1188000",
									  "xondemand",
									  "xondemand",
									  "228571000",
									  "300000000",
									  "1",
									  "1",
									  "35",
									  "5"));
			db.addProfile(new Profile("Performance", "384000", 
									  "1512000", 
									  "384000",
									  "1512000",
									  "xondemand",
									  "xondemand",
									  "266667000",
									  "320000000",
									  "0",
									  "1",
									  "10",
									  "70"));
			db.addProfile(new Profile("Xtreme Performance", "384000", 
									  "1620000", 
									  "384000",
									  "1620000",
									  "Performance",
									  "Performance",
									  "266667000",
									  "320000000",
									  "0",
									  "2",
									  "0",
									  "100"));
			SharedPreferences.Editor editor = preferences.edit();

			editor.putBoolean("profiles", true);
			editor.commit();
		}
		// Reading all contacts
		//Log.d("Reading: ", "Reading all contacts..");
		profilenames = new ArrayList<String>();


		// Profile pr = db.getProfile(1);
		//Profile pr2 = db.getProfileByName("Performance") ;   
		//System.out.println(pr.getName());
		//System.out.println(pr2.getName());
		//System.out.println(pr);

		getprofiles();

	}
	public void getprofiles()
	{
		List<Profile> profiles = db.getAllProfiles();
    	for (Profile pr : profiles)
		{
        	profilenames.add(pr.getName());
            //String log = "Id: "+pr.getID()+"Name: "+pr.getName()+" ,CPU0 Max freq: " + pr.getCpu0min() + " ,CPU0 max freq: " + pr.getCpu0max();
			// Writing Contacts to log
			//Log.d("Name: ", log);

		}
    	//System.out.println(profilenames);
    	spinnerProfiles();
	}
	public void spinnerProfiles()
	{
		//String[] MyStringAray = profiles.split("\\s");

		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, profilenames);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    		    @Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
    		    	currentprofile = parent.getItemAtPosition(pos).toString();
    		    	//System.out.println(currentprofile);
    		    	Profile profile = db.getProfileByName(currentprofile) ;
    		    	TextView cpu0min = (TextView)findViewById(R.id.textView11);
    		    	TextView cpu0max = (TextView)findViewById(R.id.textView12);
    		    	TextView cpu1min = (TextView)findViewById(R.id.textView13);
    		    	TextView cpu1max = (TextView)findViewById(R.id.textView14);
    		    	TextView cpu0gov = (TextView)findViewById(R.id.textView15);
    		    	TextView cpu1gov = (TextView)findViewById(R.id.textView23);
    		    	TextView gpu2d = (TextView)findViewById(R.id.textView17);
    		    	TextView gpu3d = (TextView)findViewById(R.id.textView18);
    		    	TextView vsync = (TextView)findViewById(R.id.textView19);
    		    	TextView noc = (TextView)findViewById(R.id.textView2201);
    		    	TextView mtu = (TextView)findViewById(R.id.textView2202);
    		    	TextView mtd = (TextView)findViewById(R.id.textView2203);
    		    	cpu0min.setText(profile.getCpu0min().substring(0, profile.getCpu0min().length() - 3) + "Mhz");
    		    	cpu0max.setText(profile.getCpu0max().substring(0, profile.getCpu0max().length() - 3) + "Mhz");
    		    	cpu1min.setText(profile.getCpu1min().substring(0, profile.getCpu1min().length() - 3) + "Mhz");
    		    	cpu1max.setText(profile.getCpu1max().substring(0, profile.getCpu1max().length() - 3) + "Mhz");
    		    	cpu1gov.setText(profile.getCpu1gov());
    		    	cpu0gov.setText(profile.getCpu0gov());
    		    	gpu2d.setText(profile.getGpu2d().substring(0, profile.getGpu2d().length() - 6) + "Mhz");
    		    	gpu3d.setText(profile.getGpu3d().substring(0, profile.getGpu3d().length() - 6) + "Mhz");
    		    	if (profile.getVsync().equals("1"))
					{
    		    		vsync.setText("ON");
    		    	}
    		    	else
					{
    		    		vsync.setText("OFF");
    		    	}

    		    	noc.setText(profile.getNOC());
    		    	mtu.setText(profile.getMtu());
    		    	mtd.setText(profile.getMtd());

    		    }


    		    @Override
				public void onNothingSelected(AdapterView<?> parent)
				{
    		        //do nothing
    		    }
    		});

		//ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		//int spinnerPosition = myAdap.getPosition(scheduler);

		//set the default according to value
		//spinner.setSelection(spinnerPosition);

	}@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    menu.add(Menu.NONE, 0, 0, "Add ").setIcon(R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	    menu.add(Menu.NONE, 1, 1, "Delete").setIcon(R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(Menu.NONE, 2, 2, "Apply").setIcon(R.drawable.ic_menu_mark).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{

	    return true;
	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
		{
	        case 0:
	        	//startActivity(new Intent(this, ProfileEditor.class));
	        	Intent intent = new Intent();
	            intent.setClass(this, ProfileEditor.class);
	            startActivityForResult(intent, GET_CODE);
				return true;
	        case 1:
	        	Profile pr2 = db.getProfileByName(currentprofile); 
	        	db.deleteProfileByName(pr2);
	        	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
				myAdap.clear();
				getprofiles();				
				return true;
	        case 2:
	        	this.pd = ProgressDialog.show(this, "Working..", "Applying settings... ", true, false);
	        	new apply().execute();
	        	return true;

	    }
	    return false;

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GET_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				//String d = data.getStringExtra("Color");
				ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
				myAdap.clear();
				db.addProfile(new Profile(data.getStringExtra("Name"),
										  data.getStringExtra("cpu0min"), 
										  data.getStringExtra("cpu0max"), 
										  data.getStringExtra("cpu1min"),
										  data.getStringExtra("cpu1max"),
										  data.getStringExtra("cpu0gov"),
										  data.getStringExtra("cpu1gov"),
										  data.getStringExtra("gpu2d"),
										  data.getStringExtra("gpu3d"),
										  data.getStringExtra("vsync"),
										  data.getStringExtra("noc"),
										  data.getStringExtra("mtd"),
										  data.getStringExtra("mtu")));
				getprofiles();
				//spinnerProfiles(); 

				int spinnerPosition = myAdap.getPosition(data.getStringExtra("Name"));

				//set the default according to value
				spinner.setSelection(spinnerPosition);
			}

			else
			{
				//text.setText("Cancelled");
			}
		}
	}
}
