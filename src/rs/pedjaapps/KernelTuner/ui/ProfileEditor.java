/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.ui;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import android.view.View.OnClickListener;
import rs.pedjaapps.KernelTuner.tools.Tools;

public class ProfileEditor extends Activity
{
	
	
	private List<Frequency> freqEntries = IOHelper.frequencies();
	private List<String> freqNames = new ArrayList<String>();
	private String cpu0min;
	private String cpu0max; 
	private String cpu1min;
	private String cpu1max;

	private String cpu2min;
	private String cpu2max; 
	private String cpu3min;
	private String cpu3max;
	private String cpu0gov;
	private String cpu1gov;

	private String cpu2gov;
	private String cpu3gov;
	private String gpu2d;
	private String gpu3d;

	private String mtu;
	private String mtd;
	private String Name;
	private String voltage;
	private String scheduler;
	private String cdepth;
	
	private String profileName;
	private Profile profile;
	
	public String[] gpu2ds ;
	public String[] gpu3ds ;

	private int s2w;

	
	
	private EditText ed1;
	private EditText ed2;
	private EditText ed3;
	private EditText ed4;
	private EditText name;
	
	private RadioGroup vsyncGroup;
	private RadioGroup fchargeGroup;
	private RadioButton vsyncUnchanged;
	private RadioButton vsyncOff;
	private RadioButton vsyncOn;
	private RadioButton fchargeUnchanged;
	private RadioButton fchargeOff;
	private RadioButton fchargeOn;
	private DatabaseHandler db;
	private TextView vsyncText;
	private TextView fchargetext;
	
	private String board = android.os.Build.DEVICE;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		setTheme(Tools.getPreferedTheme(theme));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_editor);
		Intent intent = getIntent();
		db = new DatabaseHandler(this);
		profileName = intent.getExtras().getString("profileName");
		
		if(profileName!=null && !profileName.equals("")){
		profile = db.getProfileByName(profileName);
		}
		RelativeLayout cpu = (RelativeLayout)findViewById(R.id.cpu);
		final LinearLayout cpuInfo = (LinearLayout)findViewById(R.id.cpu_settings);
		final ImageView cpuImg = (ImageView)findViewById(R.id.cpu_img);

		RelativeLayout other = (RelativeLayout)findViewById(R.id.other);
		final LinearLayout otherInfo = (LinearLayout)findViewById(R.id.other_settings);
		final ImageView otherImg = (ImageView)findViewById(R.id.other_img);

		RelativeLayout gpu = (RelativeLayout)findViewById(R.id.gpu);
		final LinearLayout gpuInfo = (LinearLayout)findViewById(R.id.gpu_settings);
		final ImageView gpuImg = (ImageView)findViewById(R.id.gpu_img);
		
		name = (EditText)findViewById(R.id.editText3);//profile name
		ed1  = (EditText)findViewById(R.id.editText2);//mpdec thr down
		ed2  = (EditText)findViewById(R.id.editText1);//mpdec thrs up
		ed3  = (EditText)findViewById(R.id.editText4);//capacitive lights
		ed4  = (EditText)findViewById(R.id.editText5);//sd cache
		vsyncGroup = (RadioGroup)findViewById(R.id.vsyncGroup);
		fchargeGroup = (RadioGroup)findViewById(R.id.fchargeGroup);
		vsyncUnchanged = (RadioButton)findViewById(R.id.vsyncUnchanged);
		vsyncOff = (RadioButton)findViewById(R.id.vsyncOff);
		vsyncOn = (RadioButton)findViewById(R.id.vsyncOn);
		fchargeUnchanged = (RadioButton)findViewById(R.id.fchargeUnchanged);
		fchargeOff = (RadioButton)findViewById(R.id.fchargeOff);
		fchargeOn = (RadioButton)findViewById(R.id.fchargeOn);
		
		vsyncText = (TextView)findViewById(R.id.vsync);
		fchargetext = (TextView)findViewById(R.id.fcharge);
	
		
		cpu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if (cpuInfo.getVisibility() == View.VISIBLE)
				{
					cpuInfo.setVisibility(View.GONE);
					cpuImg.setImageResource(R.drawable.arrow_right);
				}
				else if (cpuInfo.getVisibility() == View.GONE)
				{
					cpuInfo.setVisibility(View.VISIBLE);
					cpuImg.setImageResource(R.drawable.arrow_down);
				}
			}

		});

	gpu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if (gpuInfo.getVisibility() == View.VISIBLE)
				{
					gpuInfo.setVisibility(View.GONE);
					gpuImg.setImageResource(R.drawable.arrow_right);
				}
				else if (gpuInfo.getVisibility() == View.GONE)
				{
					gpuInfo.setVisibility(View.VISIBLE);
					gpuImg.setImageResource(R.drawable.arrow_down);
				}
			}

		});

	other.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if (otherInfo.getVisibility() == View.VISIBLE)
				{
					otherInfo.setVisibility(View.GONE);
					otherImg.setImageResource(R.drawable.arrow_right);
				}
				else if (otherInfo.getVisibility() == View.GONE)
				{
					otherInfo.setVisibility(View.VISIBLE);
					otherImg.setImageResource(R.drawable.arrow_down);
				}
			}

		});
	
		final Spinner spinner1 = (Spinner)findViewById(R.id.bg);
		Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
		Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
		Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
		Spinner spinner5 = (Spinner)findViewById(R.id.spinner5);
		Spinner spinner6 = (Spinner)findViewById(R.id.spinner6);
		Spinner spinner7 = (Spinner)findViewById(R.id.spinner7);
		Spinner spinner8 = (Spinner)findViewById(R.id.spinner8);
		Spinner spinner9 = (Spinner)findViewById(R.id.spinner13);
		Spinner spinner10 = (Spinner)findViewById(R.id.spinner10);
		Spinner spinner11 = (Spinner)findViewById(R.id.spinner11);
		Spinner spinner12 = (Spinner)findViewById(R.id.spinner9);
		Spinner spinner13 = (Spinner)findViewById(R.id.spinner12);
		Spinner spinner14 = (Spinner)findViewById(R.id.spinner14);
		Spinner spinner15 = (Spinner)findViewById(R.id.spinner15);
		Spinner spinner16 = (Spinner)findViewById(R.id.spinner16);
		Spinner spinner17 = (Spinner)findViewById(R.id.spinner17);
		Spinner spinner18 = (Spinner)findViewById(R.id.spinner19);

		List<String> freqs = new ArrayList<String>();
		freqs.add(getResources().getString(R.string.unchanged));
		for(Frequency f: freqEntries){
			freqs.add(f.getFrequencyValue()+"");
		}
		for(Frequency f: freqEntries){
			freqNames.add(f.getFrequencyString());
		}


		List<String> govs = new ArrayList<String>();
		govs.add(getResources().getString(R.string.unchanged));
		govs.addAll(IOHelper.governors());

		List<String> schedulers = new ArrayList<String>();
		schedulers.add(getResources().getString(R.string.unchanged));
		schedulers.addAll(IOHelper.schedulers());


		List<String> voltageProfiles = new ArrayList<String>();
		List<Voltage> voltages = db.getAllVoltages();
		voltageProfiles.add(getResources().getString(R.string.unchanged));
		for(Voltage v : voltages){
			voltageProfiles.add(v.getName());
		}



		LinearLayout mpup  = (LinearLayout)findViewById(R.id.mpup);//mpdec thr down
		LinearLayout mpdown  = (LinearLayout)findViewById(R.id.mpdown);//mpdec thrs up
		LinearLayout buttons  = (LinearLayout)findViewById(R.id.buttons);//capacitive lights
		LinearLayout sd  = (LinearLayout)findViewById(R.id.sd);//sd cache


		if(profileName!=null && !profileName.equals("")){
			name.setText(profileName.toString());
		}
		if(profileName!=null && !profileName.equals("")){
			ed2.setText(profile.getMtu());
		}
		if(profileName!=null && !profileName.equals("")){
			ed1.setText(profile.getMtd());
		}
		if(profileName!=null && !profileName.equals("")){
			ed3.setText(profile.getButtonsLight());
		}
		if(profileName!=null && !profileName.equals("")){
			ed4.setText(profile.getSdcache()+"");
		}
		if(profileName!=null && !profileName.equals("")){
			if(profile.getVsync()==0){
				vsyncOff.setChecked(true);
			}
			else if(profile.getVsync()==1){
				vsyncOn.setChecked(true);
			}
			else{
				vsyncUnchanged.setChecked(true);
			}
		}
		if(profileName!=null && !profileName.equals("")){
			if(profile.getFcharge()==0){
				fchargeOff.setChecked(true);
			}
			else if(profile.getFcharge()==1){
				fchargeOn.setChecked(true);
			}
			else{
				fchargeUnchanged.setChecked(true);
			}
		}

		if(IOHelper.mpdecisionExists()==false){
			mpup.setVisibility(View.GONE);
			mpdown.setVisibility(View.GONE);
		}
		if(IOHelper.buttonsExists()==false){
			buttons.setVisibility(View.GONE);
		}
		if(IOHelper.sdcacheExists()==false){
			sd.setVisibility(View.GONE);
		}

		if(IOHelper.vsyncExists()==false){
			vsyncGroup.setVisibility(View.GONE);
			vsyncText.setVisibility(View.GONE);
		}
		if(IOHelper.fchargeExists()==false){
			fchargeGroup.setVisibility(View.GONE);
			fchargetext.setVisibility(View.GONE);
		}

		if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid") || board.equals("tenderloin") )
		{
			gpu2ds = new String[] {getResources().getString(R.string.unchanged),"160000000", "200000000", "228571000", "266667000"};
			gpu3ds = new String[]{getResources().getString(R.string.unchanged),"200000000", "228571000", "266667000", "300000000", "320000000"};
		}
		else if (board.equals("evita") || board.equals("ville") || board.equals("jwel") || board.equals("jet"))
		{
			gpu2ds = new String[]{getResources().getString(R.string.unchanged),"266667000", "228571000", "200000000", "160000000", "96000000", "27000000"};
			gpu3ds = new String[]{getResources().getString(R.string.unchanged),"400000000", "320000000", "300000000", "228571000", "266667000", "200000000", "177778000", "27000000"};
		}
		else{
			gpu2ds = new String[]{getResources().getString(R.string.unchanged)};
			gpu3ds = new String[]{getResources().getString(R.string.unchanged)};
		}

		String[] cd = {getResources().getString(R.string.unchanged),"16","24","32"};
		String[] sweep2wake = {getResources().getString(R.string.unchanged),"OFF","ON with no backlight","ON with backlight"};


		/*if(CPUInfo.cpu0Exists()==true)
		 {*/
		/**spinner1*/
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner1.setAdapter(spinnerArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinnerArrayAdapter.getPosition(profile.getCpu0min());
			spinner1.setSelection(spinner1Position);
		}
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override 
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0min = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }



        	});


		/**spinner2*/
		ArrayAdapter<String> spinner2ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinner2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner2.setAdapter(spinner2ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner2ArrayAdapter.getPosition(profile.getCpu0max());
			spinner2.setSelection(spinner1Position);
		}

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0max = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});

		/**spinner9*/
		ArrayAdapter<String> spinner9ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
		spinner9ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner9.setAdapter(spinner9ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner9ArrayAdapter.getPosition(profile.getCpu0gov());
			spinner9.setSelection(spinner1Position);
		}

		spinner9.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0gov = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});
		
		if(IOHelper.cpu1Exists()==true)
		{
			/**spinner3*/
			ArrayAdapter<String> spinner3ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner3.setAdapter(spinner3ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner3ArrayAdapter.getPosition(profile.getCpu1min());
				spinner3.setSelection(spinner1Position);
			}

			spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1min = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner4*/
			ArrayAdapter<String> spinner4ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner4ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner4.setAdapter(spinner4ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner4ArrayAdapter.getPosition(profile.getCpu1max());
				spinner4.setSelection(spinner1Position);
			}

			spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1max = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner10*/
			ArrayAdapter<String> spinner12ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
			spinner12ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner12.setAdapter(spinner12ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner12ArrayAdapter.getPosition(profile.getCpu1gov());
				spinner12.setSelection(spinner1Position);
			}

			spinner12.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1gov = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cpu1minll = (LinearLayout)findViewById(R.id.cpu1min);
			LinearLayout cpu1maxll = (LinearLayout)findViewById(R.id.cpu1max);
			LinearLayout cpu1govll = (LinearLayout)findViewById(R.id.cpu1gov);

			cpu1minll.setVisibility(View.GONE);
			cpu1maxll.setVisibility(View.GONE);
			cpu1govll.setVisibility(View.GONE);
		}
		if(IOHelper.cpu2Exists()==true)
		{
			/**spinner5*/
			ArrayAdapter<String> spinner5ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner5ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner5.setAdapter(spinner5ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner5ArrayAdapter.getPosition(profile.getCpu2min());
				spinner5.setSelection(spinner1Position);
			}

			spinner5.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu2min = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner6*/
			ArrayAdapter<String> spinner6ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner6ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner6.setAdapter(spinner6ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner6ArrayAdapter.getPosition(profile.getCpu2max());
				spinner6.setSelection(spinner1Position);
			}

			spinner6.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu2max = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner11*/
			ArrayAdapter<String> spinner10ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
			spinner10ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner10.setAdapter(spinner10ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner10ArrayAdapter.getPosition(profile.getCpu2gov());
				spinner10.setSelection(spinner1Position);
			}

			spinner10.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu2gov = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cpu2minll = (LinearLayout)findViewById(R.id.cpu2min);
			LinearLayout cpu2maxll = (LinearLayout)findViewById(R.id.cpu2max);
			LinearLayout cpu2govll = (LinearLayout)findViewById(R.id.cpu2gov);

			cpu2minll.setVisibility(View.GONE);
			cpu2maxll.setVisibility(View.GONE);
			cpu2govll.setVisibility(View.GONE);
		}
		if(IOHelper.cpu3Exists()==true)
		{
			/**spinner7*/

			ArrayAdapter<String> spinner7ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner7ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner7.setAdapter(spinner7ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner7ArrayAdapter.getPosition(profile.getCpu3min());
				spinner7.setSelection(spinner1Position);
			}

			spinner7.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu3min = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner8*/
			ArrayAdapter<String> spinner8ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner8ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner8.setAdapter(spinner8ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner8ArrayAdapter.getPosition(profile.getCpu3max());
				spinner8.setSelection(spinner1Position);
			}

			spinner8.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu3max = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner12*/
			ArrayAdapter<String> spinner11ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
			spinner11ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner11.setAdapter(spinner11ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner11ArrayAdapter.getPosition(profile.getCpu3gov());
				spinner11.setSelection(spinner1Position);
			}

			spinner11.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu3gov = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cpu3minll = (LinearLayout)findViewById(R.id.cpu3min);
			LinearLayout cpu3maxll = (LinearLayout)findViewById(R.id.cpu3max);
			LinearLayout cpu3govll = (LinearLayout)findViewById(R.id.cpu3gov);

			cpu3minll.setVisibility(View.GONE);
			cpu3maxll.setVisibility(View.GONE);
			cpu3govll.setVisibility(View.GONE);
		}
		if(IOHelper.voltageExists())
		{
			/**spinner13*/
			ArrayAdapter<String> spinner13ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, voltageProfiles);
			spinner13ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner13.setAdapter(spinner13ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner13ArrayAdapter.getPosition(profile.getVoltage());
				spinner13.setSelection(spinner1Position);
			}

			spinner13.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						voltage = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout voltage = (LinearLayout)findViewById(R.id.voltage);

			voltage.setVisibility(View.GONE);
		}
		if(IOHelper.gpuExists())
		{
			/**spinner14*/
			ArrayAdapter<String> spinner14ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2ds);
			spinner14ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner14.setAdapter(spinner14ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner14ArrayAdapter.getPosition(profile.getGpu2d());
				spinner14.setSelection(spinner1Position);
			}

			spinner14.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						gpu2d = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner15*/
			ArrayAdapter<String> spinner15ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu3ds);
			spinner15ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner15.setAdapter(spinner15ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner15ArrayAdapter.getPosition(profile.getGpu3d());
				spinner15.setSelection(spinner1Position);
			}

			spinner15.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						gpu3d = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout gpu2dll = (LinearLayout)findViewById(R.id.gpu2d);
			LinearLayout gpu3dll = (LinearLayout)findViewById(R.id.gpu3d);

			gpu2dll.setVisibility(View.GONE);
			gpu3dll.setVisibility(View.GONE);
		}
		if(IOHelper.cdExists())
		{
			/**spinner16*/
			ArrayAdapter<String> spinner16ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cd);
			spinner16ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner16.setAdapter(spinner16ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner16ArrayAdapter.getPosition(profile.getCdepth());
				spinner16.setSelection(spinner1Position);
			}

			spinner16.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cdepth = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cdepthll = (LinearLayout)findViewById(R.id.cdepth);

			cdepthll.setVisibility(View.GONE);
		}
		/**spinner17*/ 
		ArrayAdapter<String> spinner17ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, schedulers);
		spinner17ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner17.setAdapter(spinner17ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner17ArrayAdapter.getPosition(profile.getIoScheduler());
			spinner17.setSelection(spinner1Position);
		}

		spinner17.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	scheduler = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});
		if(IOHelper.s2wExists())
		{
			/**spinner18*/
			ArrayAdapter<String> spinner18ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sweep2wake);
			spinner18ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner18.setAdapter(spinner18ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				if(profile.getSweep2wake()==0){
					spinner18.setSelection(0);
				}
				else if(profile.getSweep2wake()==1){
					spinner18.setSelection(1);
				}
				else if(profile.getSweep2wake()==2){
					spinner18.setSelection(2);
				}

			}

			spinner18.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						if(parent.getItemAtPosition(pos).toString().equals(getResources().getString(R.string.s2w_on_no_bl)))
						{
							s2w = 1;
						}
						else if(parent.getItemAtPosition(pos).toString().equals(getResources().getString(R.string.s2w_off)))
						{
							s2w = 0;
						}
						else if(parent.getItemAtPosition(pos).toString().equals(getResources().getString(R.string.s2w_on)))
						{
							s2w = 2;
						}
						else if(parent.getItemAtPosition(pos).toString().equals("Unchanged"))
						{
							s2w = 3;
						}


					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout s2wll = (LinearLayout)findViewById(R.id.s2w);

			s2wll.setVisibility(View.GONE);
		}
			
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_editor_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
}


@Override
public boolean onOptionsItemSelected(MenuItem item) {

	if (item.getItemId() == R.id.save)
	{
    	
		if(name.getText().toString().length()<1 || name.getText().toString().equals(""))
		{
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_profile_name), Toast.LENGTH_LONG).show();
			
		}
		else{
		int vsync = -1;
		int fcharge = -1;
		int sdcache = 0;
		try{
			sdcache = Integer.parseInt(ed4.getText().toString());
		}catch(NumberFormatException e){
			
		}
		if(vsyncOn.isChecked()){
			vsync=1;
		}
		else if(vsyncOff.isChecked()){
			vsync = 0;
		}
		
		if(fchargeOn.isChecked()){
			fcharge=1;
		}
		else if(fchargeOff.isChecked()){
			fcharge = 0;
		}
		mtd = ed1.getText().toString();
		mtu = ed2.getText().toString();
		if(profileName!=null && !profileName.equals("")){
			db.deleteProfileByName(profile);
		}
		Name = name.getText().toString();
		Intent intent = new Intent();
		intent.putExtra("Name", Name);
		intent.putExtra("cpu0min", cpu0min);
		intent.putExtra("cpu0max", cpu0max);
		intent.putExtra("cpu1min", cpu1min);
		intent.putExtra("cpu1max", cpu1max);
		intent.putExtra("cpu2min", cpu2min);
		intent.putExtra("cpu2max", cpu2max);
		intent.putExtra("cpu3min", cpu3min);
		intent.putExtra("cpu3max", cpu3max);
		intent.putExtra("cpu0gov", cpu0gov);
		intent.putExtra("cpu1gov", cpu1gov);
		intent.putExtra("cpu2gov", cpu2gov);
		intent.putExtra("cpu3gov", cpu3gov);
		intent.putExtra("voltageProfile", voltage);
		intent.putExtra("mtd", mtd);
		intent.putExtra("mtu", mtu);
		intent.putExtra("gpu2d", gpu2d);
		intent.putExtra("gpu3d", gpu3d);
		intent.putExtra("buttonsBacklight", ed3.getText().toString());
		intent.putExtra("vsync", vsync);
		intent.putExtra("fcharge", fcharge);
		intent.putExtra("cdepth", cdepth);
		intent.putExtra("io", scheduler);
		intent.putExtra("sdcache", sdcache);
		intent.putExtra("s2w", s2w);
		setResult(RESULT_OK, intent);
		finish();
		}
	}
		
	if (item.getItemId() == R.id.cancel)
	{
    	finish();
	}

return super.onOptionsItemSelected(item);

}

}

