package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.*;
import android.widget.*;

import com.google.ads.*;
import java.io.*;
import java.util.*;

public class VoltageActivity extends Activity
{

	static VoltageAdapter voltageAdapter ;
	ListView voltageListView;
	static ProgressDialog pd = null;
	DatabaseHandler db;
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.voltage);

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
					// TODO Auto-generated method stub
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"minus"});


				}

			});

		Button plus = (Button)findViewById(R.id.button2);
		plus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"plus"});


				}

			});

		Button save = (Button)findViewById(R.id.button3);
		save.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
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
								String name = String.valueOf(input.getText());
								List<String> freqs = CPUInfo.voltageFreqs();
								List<Integer> values = CPUInfo.voltages();
								String freqTemp;
								String valueTemp;
								StringBuilder freqBuilder  = new StringBuilder();
								StringBuilder valueBuilder  = new StringBuilder();
								for (String s : freqs){
									freqBuilder.append(s+" ");
								}
								for (int i=0; i<values.size(); i++){
									valueBuilder.append(String.valueOf(values.get(i))+" ");
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

					builder.setIcon(R.drawable.ic_menu_delete);

					
					
					builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								List<Voltage> voltages = db.getAllVoltages();
								for(int i =0; i<voltages.size(); i++){
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



		List<Integer> allVoltages = CPUInfo.allVoltages();
		List<Integer> allVoltagesTegra3 = CPUInfo.allVoltagesTegra3();
		List<Integer> voltages = CPUInfo.voltages();
		List<String> voltageFreqs = CPUInfo.voltageFreqs();

		if (new File(CPUInfo.VOLTAGE_PATH).exists())
		{
			for (int i= 0; i < voltages.size(); i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqs.get(i).substring(0, voltageFreqs.get(i).length() - 3) + "Mhz", allVoltages.indexOf(voltages.get(i))));
				

			}	

		}
		else if (new File(CPUInfo.VOLTAGE_PATH_TEGRA_3).exists())
		{
			for (int i= 0; i < voltages.size(); i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqs.get(i) + "Mhz", allVoltagesTegra3.indexOf(voltages.get(i))));
				

			}	
		}
		return entries;
	}
	
		
}
