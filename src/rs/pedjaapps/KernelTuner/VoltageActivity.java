package rs.pedjaapps.KernelTuner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class VoltageFragment extends Fragment {
	public VoltageFragment() {
	}
	static VoltageAdapter voltageAdapter ;
	ListView voltageListView;
	static ProgressDialog pd = null;
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		View v = null;

		v = inflater.inflate(R.layout.voltage,
				container, false);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads==true){AdView adView = (AdView)v.findViewById(R.id.ad);
		adView.loadAd(new AdRequest());}
		
		voltageListView = (ListView) v.findViewById(R.id.list);
	      voltageAdapter = new VoltageAdapter(this.getActivity(), R.layout.voltage_list_item);
	     voltageListView.setAdapter(voltageAdapter);
	    
	     for(final VoltageEntry entry : getVoltageEntries()) {
	      voltageAdapter.add(entry);
	     }
	     
	     Button minus = (Button)v.findViewById(R.id.button1);
	     minus.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				VoltageAdapter.pd = ProgressDialog.show(VoltageFragment.this.getActivity(), "Working..", "Loading...", true, false);
				new ChangeVoltage(VoltageFragment.this.getActivity()).execute(new String[] {"minus"});
				
				
			}
	    	 
	     });
	     
	     Button plus = (Button)v.findViewById(R.id.button2);
	     plus.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				VoltageAdapter.pd = ProgressDialog.show(VoltageFragment.this.getActivity(), "Working..", "Loading...", true, false);
				new ChangeVoltage(VoltageFragment.this.getActivity()).execute(new String[] {"plus"});
				
				
			}
	    	 
	     });
		//System.out.println(CPUInfo.allVoltages());
		return v;
	}
	
	public static void notifyChanges(){
		voltageAdapter.clear();
		for(final VoltageEntry entry : getVoltageEntries()) {
		      voltageAdapter.add(entry);
		     }
		voltageAdapter.notifyDataSetChanged();
	}
	
	private static List<VoltageEntry> getVoltageEntries() {

	     final List<VoltageEntry> entries = new ArrayList<VoltageEntry>();
	     
	  
	    
	     List<Integer> allVoltages = CPUInfo.allVoltages();
	     List<Integer> allVoltagesTegra3 = CPUInfo.allVoltagesTegra3();
	     List<Integer> voltages = CPUInfo.voltages();
	     List<String> voltageFreqs = CPUInfo.voltageFreqs();
	     
	     if(new File(CPUInfo.VOLTAGE_PATH).exists()){
	    	 for(int i= 0; i<voltages.size(); i++){	    	 
	    		 entries.add(new VoltageEntry(voltageFreqs.get(i).substring(0, voltageFreqs.get(i).length() - 3)+"Mhz", allVoltages.indexOf(voltages.get(i))));
	    		 System.out.println(allVoltages.indexOf(voltages.get(i)));

	    	 }	
	    
	     }
	     else if(new File(CPUInfo.VOLTAGE_PATH_TEGRA_3).exists()){
	    	 for(int i= 0; i<voltages.size(); i++){	    	 
	    		 entries.add(new VoltageEntry(voltageFreqs.get(i)+"Mhz", allVoltagesTegra3.indexOf(voltages.get(i))));
	    		 System.out.println(allVoltagesTegra3.indexOf(voltages.get(i)));

	    	 }	
	     }
	     return entries;
	    }
}