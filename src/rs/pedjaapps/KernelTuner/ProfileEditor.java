package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

import android.view.View.OnClickListener;

public class ProfileEditor extends Activity
{

	String freqs;
	String govs;
	String cpu0min;
	String cpu0max; 
	String cpu1min;
	String cpu1max;

	String cpu2min;
	String cpu2max; 
	String cpu3min;
	String cpu3max;
	String cpu0gov;
	String cpu1gov;

	String cpu2gov;
	String cpu3gov;
	String gpu2d;
	String gpu3d;

	String mtu;
	String mtd;
	String Name;
	String voltage;
	String scheduler;
	String cdepth;
	List<String> frequencies = new ArrayList<String>();
	SharedPreferences sharedPrefs;
	public String[] delims;
	
	public String[] gpu2ds ;
	public String[] gpu3ds ;

	int s2w;

	public String[] gpu2d(String[] gpu2d)
	{
		gpu2ds = gpu2d;
		return gpu2d;

	}
	public String[] gpu3d(String[] gpu3d)
	{
		gpu3ds = gpu3d;
		return gpu3d;

	}
	
	String board = android.os.Build.DEVICE;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_editor);
		RelativeLayout cpu = (RelativeLayout)findViewById(R.id.cpu);
		final LinearLayout cpuInfo = (LinearLayout)findViewById(R.id.cpu_settings);
		final ImageView cpuImg = (ImageView)findViewById(R.id.cpu_img);

		RelativeLayout other = (RelativeLayout)findViewById(R.id.other);
		final LinearLayout otherInfo = (LinearLayout)findViewById(R.id.other_settings);
		final ImageView otherImg = (ImageView)findViewById(R.id.other_img);

		RelativeLayout gpu = (RelativeLayout)findViewById(R.id.gpu);
		final LinearLayout gpuInfo = (LinearLayout)findViewById(R.id.gpu_settings);
		final ImageView gpuImg = (ImageView)findViewById(R.id.gpu_img);

		Button cancel = (Button)findViewById(R.id.cancel);
		
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
	
	cancel.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0)
		{
			finish();
		}

	});
	
	setUI();
	
		

		Button apply = (Button)findViewById(R.id.apply);
		apply.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0)
				{
					
					EditText name = (EditText)findViewById(R.id.editText3);//profile name
					EditText ed1  = (EditText)findViewById(R.id.editText1);//mpdec thr down
					EditText ed2  = (EditText)findViewById(R.id.editText2);//mpdec thrs up
					EditText ed3  = (EditText)findViewById(R.id.editText4);//capacitive lights
					EditText ed4  = (EditText)findViewById(R.id.editText5);//sd cache
					
					CheckBox vsyncBox = (CheckBox)findViewById(R.id.vsync);
					CheckBox fchargeBox = (CheckBox)findViewById(R.id.fcharge);
					if(name.getText().toString().length()<1 || name.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(), "Profile Name cannot be empty!\nPlease enter Profile Name", Toast.LENGTH_LONG).show();
						
					}
					else{
					int vsync;
					int fcharge;
					int sdcache = 0;
					try{
						sdcache = Integer.parseInt(ed4.getText().toString());
					}catch(NumberFormatException e){
						
					}
					if(vsyncBox.isChecked()){
						vsync=1;
					}
					else{
						vsync = 0;
					}
					
					if(fchargeBox.isChecked()){
						fcharge=1;
					}
					else{
						fcharge = 0;
					}
					mtd = ed1.getText().toString();
					mtu = ed2.getText().toString();
					
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

			});
	}

	public void setUI()
	{
		Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
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
				freqs.add("Unchanged");
				freqs.addAll(CPUInfo.frequencies());
				
				System.out.println("Freqs"+freqs);
				
		List<String> govs = new ArrayList<String>();
				govs.add("Unchanged");
				govs.addAll(CPUInfo.governors());
				
		List<String> schedulers = new ArrayList<String>();
		schedulers.add("Unchanged");
		schedulers.addAll(CPUInfo.schedulers());
				
		DatabaseHandler db = new DatabaseHandler(this); 
		List<String> voltageProfiles = new ArrayList<String>();
		List<Voltage> voltages = db.getAllVoltages();
		voltageProfiles.add("Unchanged");
		for(Voltage v : voltages){
			voltageProfiles.add(v.getName());
		}
		
		List<String> numbers = new ArrayList<String>();
		for (int i = 0; i < 100; i++)
		{
			numbers.add(String.valueOf(i));
		}

		EditText name = (EditText)findViewById(R.id.editText3);//profile name
		LinearLayout mpup  = (LinearLayout)findViewById(R.id.mpup);//mpdec thr down
		LinearLayout mpdown  = (LinearLayout)findViewById(R.id.mpdown);//mpdec thrs up
		LinearLayout buttons  = (LinearLayout)findViewById(R.id.buttons);//capacitive lights
		LinearLayout sd  = (LinearLayout)findViewById(R.id.sd);//sd cache
		
		CheckBox vsyncBox = (CheckBox)findViewById(R.id.vsync);
		CheckBox fchargeBox = (CheckBox)findViewById(R.id.fcharge);
		
		if(CPUInfo.mpdecisionExists()==false){
			mpup.setVisibility(View.GONE);
			mpdown.setVisibility(View.GONE);
		}
		if(CPUInfo.buttonsExists()==false){
			buttons.setVisibility(View.GONE);
		}
		if(CPUInfo.sdcacheExists()==false){
			sd.setVisibility(View.GONE);
		}
		
		if(CPUInfo.vsyncExists()==false){
			vsyncBox.setVisibility(View.GONE);
		}
		if(CPUInfo.fchargeExists()==false){
			fchargeBox.setVisibility(View.GONE);
		}
		
		if (board.equals("shooter") || board.equals("shooteru") || board.equals("pyramid"))
		{
			gpu2d(new String[]{"Unchanged","160000000", "200000000", "228571000", "266667000"});
			gpu3d(new String[]{"Unchanged","200000000", "228571000", "266667000", "300000000", "320000000"});
		}
		else if (board.equals("evita") || board.equals("ville") || board.equals("jwel"))
		{
			gpu2d(new String[]{"Unchanged","266667000", "228571000", "200000000", "160000000", "96000000", "27000000"});
			gpu3d(new String[]{"Unchanged","400000000", "320000000", "300000000", "228571000", "266667000", "200000000", "177778000", "27000000"});
		}

		String[] cd = {"Unchanged","16","24","32"};
		String[] sweep2wake = {"Unchanged","OFF","ON with no backlight","ON with backlight"};

		
		/*if(CPUInfo.cpu0Online()==true)
		{*/
		/**spinner1*/
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner1.setAdapter(spinnerArrayAdapter);

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
		/*}
		else{
			LinearLayout cpu0minll = (LinearLayout)findViewById(R.id.cpu0min);
			LinearLayout cpu0maxll = (LinearLayout)findViewById(R.id.cpu0max);
			LinearLayout cpu0govll = (LinearLayout)findViewById(R.id.cpu0gov);
			
			cpu0minll.setVisibility(View.GONE);
			cpu0maxll.setVisibility(View.GONE);
			cpu0govll.setVisibility(View.GONE);
		}*/
		if(CPUInfo.cpu1Online()==true)
		{
		/**spinner3*/
		ArrayAdapter<String> spinner3ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinner3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner3.setAdapter(spinner3ArrayAdapter);

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
		if(CPUInfo.cpu2Online()==true)
		{
		/**spinner5*/
		ArrayAdapter<String> spinner5ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinner5ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner5.setAdapter(spinner5ArrayAdapter);

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
		if(CPUInfo.cpu3Online()==true)
		{
		/**spinner7*/
		
		ArrayAdapter<String> spinner7ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinner7ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner7.setAdapter(spinner7ArrayAdapter);

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
		if(CPUInfo.voltageExists())
		{
		/**spinner13*/
		ArrayAdapter<String> spinner13ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, voltageProfiles);
		spinner13ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner13.setAdapter(spinner13ArrayAdapter);

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
		if(CPUInfo.gpuExists())
		{
		/**spinner14*/
		ArrayAdapter<String> spinner14ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gpu2ds);
		spinner14ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner14.setAdapter(spinner14ArrayAdapter);

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
			LinearLayout gpu2d = (LinearLayout)findViewById(R.id.gpu2d);
			LinearLayout gpu3d = (LinearLayout)findViewById(R.id.gpu3d);
			
			gpu2d.setVisibility(View.GONE);
			gpu3d.setVisibility(View.GONE);
		}
		if(CPUInfo.cdExists())
		{
		/**spinner16*/
		ArrayAdapter<String> spinner16ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cd);
		spinner16ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner16.setAdapter(spinner16ArrayAdapter);

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
			LinearLayout cdepth = (LinearLayout)findViewById(R.id.cdepth);
			
			cdepth.setVisibility(View.GONE);
		}
		/**spinner17*/
		ArrayAdapter<String> spinner17ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, schedulers);
		spinner17ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner17.setAdapter(spinner17ArrayAdapter);

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
		if(CPUInfo.s2wExists())
		{
		/**spinner18*/
		ArrayAdapter<String> spinner18ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sweep2wake);
		spinner18ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner18.setAdapter(spinner18ArrayAdapter);

		spinner18.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        			if(parent.getItemAtPosition(pos).toString().equals("ON with no backlight"))
        	    	{
        				s2w = 1;
        	    	}
        			else if(parent.getItemAtPosition(pos).toString().equals("OFF"))
        	    	{
        				s2w = 0;
        	    	}
        			else if(parent.getItemAtPosition(pos).toString().equals("ON with backlight"))
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
			LinearLayout s2w = (LinearLayout)findViewById(R.id.s2w);
			
			s2w.setVisibility(View.GONE);
		}
		
	}



}

