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

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.*;
import rs.pedjaapps.KernelTuner.tools.*;

import android.view.View.OnClickListener;
import com.actionbarsherlock.app.ActionBar;
import rs.pedjaapps.KernelTuner.R;

public class VoltageActivity extends SherlockActivity
{

	private static VoltageAdapter voltageAdapter ;
	private ListView voltageListView;
	private DatabaseHandler db;
	
	private static List<Integer> voltages = new ArrayList<Integer>();
	private static List<String> voltageFreqs =  new ArrayList<String>();
	private static List<String> voltageFreqNames =  new ArrayList<String>();
boolean isLight;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.IndicatorLight);
			isLight = true;
		}
		else if(theme.equals("dark")){
			setTheme(R.style.IndicatorDark);
			isLight = false;
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.IndicatorLightDark);
			isLight = true;
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.voltage);
		
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		db = new DatabaseHandler(this);
		
		voltageListView = (ListView) findViewById(R.id.list);
		voltageAdapter = new VoltageAdapter(this, R.layout.voltage_list_item);
		voltageListView.setAdapter(voltageAdapter);

		for (final VoltageEntry entry : getVoltageEntries())
		{
			voltageAdapter.add(entry);
		}

		Button minus = (Button)findViewById(R.id.button1);
		minus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"minus"});


				}

			});

		Button plus = (Button)findViewById(R.id.button2);
		plus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"plus"});


				}

			});

		Button save = (Button)findViewById(R.id.button3);
		save.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle(getResources().getString(R.string.voltage_profile_name));

					builder.setMessage(getResources().getString(R.string.enter_voltage_profile_name));

					builder.setIcon(R.drawable.ic_menu_cc);

					final EditText input = new EditText(arg0.getContext());

					input.setGravity(Gravity.CENTER_HORIZONTAL);
					
					builder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								String name = input.getText().toString();
								
								String freqTemp;
								String valueTemp;
								StringBuilder freqBuilder  = new StringBuilder();
								StringBuilder valueBuilder  = new StringBuilder();
								for (String s : voltageFreqs){
									freqBuilder.append(s+" ");
								}
								int voltagesSize = voltages.size();
								for (int i=0; i<voltagesSize; i++){
									valueBuilder.append(voltages.get(i)+" ");
								}
								freqTemp = freqBuilder.toString();
								valueTemp = valueBuilder.toString();
								db.addVoltage(new Voltage(name, freqTemp, 
										  valueTemp));

							}
						});
					builder.setView(input);

					AlertDialog alert = builder.create();

					alert.show();

				}

			});

		Button load = (Button)findViewById(R.id.button4);
		
		load.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				List<Voltage> voltages = db.getAllVoltages();
				List<CharSequence> items = new ArrayList<CharSequence>();
				for(Voltage v : voltages){
					items.add(v.getName());
				}
				final CharSequence[] items2;
				items2 = items.toArray(new String[0]);
				AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
				builder.setTitle(getResources().getString(R.string.select_profile));
				builder.setItems(items2, new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog, int item) {
				    	Voltage voltage = db.getVoltageByName(items2[item].toString()) ;
				    	VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
						new ChangeVoltage(VoltageActivity.this).execute(new String[] {"profile", voltage.getValue()});
				    	

				    	
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				
			}

		});

		Button clear = (Button)findViewById(R.id.button5);
		clear.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle(getResources().getString(R.string.clear_voltage_profiles));

					builder.setMessage(getResources().getString(R.string.clear_voltage_profiles_confirm));

					builder.setIcon(isLight ? R.drawable.delete_light : R.drawable.delete_dark);


					
					
					builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								List<Voltage> voltages = db.getAllVoltages();
								int voltagesSize = voltages.size();
								for(int i =0; i<voltagesSize; i++){
									db.deleteVoltageByName(voltages.get(i));
								}

							}
						});
					builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							

						}
					});
					
					AlertDialog alert = builder.create();

					alert.show();
					
			    	

				}

			});
		
		Button delete = (Button)findViewById(R.id.button6);
		delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					final List<Voltage> voltages = db.getAllVoltages();
					List<CharSequence> items = new ArrayList<CharSequence>();
					for(Voltage v : voltages){
						items.add(v.getName());
					}
					final CharSequence[] items2;
					items2 = items.toArray(new String[0]);
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
					builder.setTitle(getResources().getString(R.string.select_profile_to_delte));
					builder.setItems(items2, new DialogInterface.OnClickListener() {
					    @Override
						public void onClick(DialogInterface dialog, int item) {
					    	db.deleteVoltageByName(voltages.get(item));
					    	
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();

				}

			});
		
	}

	public static void notifyChanges()
	{
		voltageAdapter.clear();
		for (final VoltageEntry entry : getVoltageEntries())
		{
			voltageAdapter.add(entry);
		}
		voltageAdapter.notifyDataSetChanged();
	}

	private static List<VoltageEntry> getVoltageEntries()
	{

		final List<VoltageEntry> entries = new ArrayList<VoltageEntry>();

		List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
		if(voltageFreqs.isEmpty()==false){
			voltageFreqs.clear();
		}
		if(voltages.isEmpty()==false){
			voltages.clear();
		}
		if(voltageFreqNames.isEmpty()==false){
			voltageFreqNames.clear();
		}
		for(IOHelper.VoltageList v: voltageList){
			voltageFreqs.add((v.getFreq()));
		}
		for(IOHelper.VoltageList v: voltageList){
			voltages.add(v.getVoltage());
		}
		for(IOHelper.VoltageList v: voltageList){
			voltageFreqNames.add(v.getFreqName());
		}

		if (new File(IOHelper.VOLTAGE_PATH).exists())
		{
			int voltagesSize = voltages.size();
			for (int i= 0; i < voltagesSize; i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqNames.get(i), voltages.get(i)));
				

			}	

		}
		else if (new File(IOHelper.VOLTAGE_PATH_TEGRA_3).exists())
		{
			int voltagesSize = voltages.size();
			for (int i= 0; i < voltagesSize; i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqNames.get(i), voltages.get(i)));
				

			}	
		}
		return entries;
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
		
}
