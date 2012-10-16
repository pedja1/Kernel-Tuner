package rs.pedjaapps.KernelTuner;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
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
	     List<Integer> voltages = CPUInfo.voltages();
	     List<String> voltageFreqs = CPUInfo.voltageFreqs();
	     
	    // if(voltages!=null){
	     for(int i= 0; i<voltages.size(); i++){
	     entries.add(new VoltageEntry(voltageFreqs.get(i).substring(0, voltageFreqs.get(i).length() - 3)+"Mhz", allVoltages.indexOf(voltages.get(i))));
	     }	
	    // }
	     return entries;
	    }
}