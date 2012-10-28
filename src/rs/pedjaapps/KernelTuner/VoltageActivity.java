package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
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
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, "Working..", "Loading...", true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"minus"});


				}

			});

		Button plus = (Button)findViewById(R.id.button2);
		plus.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, "Working..", "Loading...", true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"plus"});


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
				System.out.println(allVoltages.indexOf(voltages.get(i)));

			}	

		}
		else if (new File(CPUInfo.VOLTAGE_PATH_TEGRA_3).exists())
		{
			for (int i= 0; i < voltages.size(); i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqs.get(i) + "Mhz", allVoltagesTegra3.indexOf(voltages.get(i))));
				System.out.println(allVoltagesTegra3.indexOf(voltages.get(i)));

			}	
		}
		return entries;
	}
}
