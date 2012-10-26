package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class TISFragment extends Fragment {
	public TISFragment() {
	}

	public static final String ARG_SECTION_NUMBER = "section_number";

	TISAdapter tisAdapter ;
	ListView tisListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		View v = null;

		v = inflater.inflate(R.layout.times_in_state,
				container, false);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads==true){AdView adView = (AdView)v.findViewById(R.id.ad);
		adView.loadAd(new AdRequest());}
		
		tisListView = (ListView) v.findViewById(R.id.list);
	      tisAdapter = new TISAdapter(this.getActivity(), R.layout.tis_list_item);
	     tisListView.setAdapter(tisAdapter);
	    
	     for(final TISEntry entry : getTISEntries()) {
	      tisAdapter.add(entry);
	     }
	     Button refresh = (Button)v.findViewById(R.id.button1);
	     refresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				tisAdapter.clear();
				for(final TISEntry entry : getTISEntries()) {
					tisAdapter.add(entry);
				}
				tisAdapter.notifyDataSetChanged();
				tisListView.invalidate();
			}
	    	 
	     });
		return v;
	}
	
	private List<TISEntry> getTISEntries() {

	     final List<TISEntry> entries = new ArrayList<TISEntry>();
	     
	     List<String> frequencies = CPUInfo.frequencies();
	     List<String> tisTime = CPUInfo.tisTime();
	     List<String> tisPercent = CPUInfo.tisPercent();
	     long deepSleep = SystemClock.elapsedRealtime()-SystemClock.uptimeMillis();
	     
	     String min = String.valueOf((((deepSleep / 1000) / 60) % 60));
	       String sec = String.valueOf(((deepSleep / 1000) % 60));
	       String sat = String.valueOf(((deepSleep/ 1000) / 3600));
	       String time = sat+"h:"+min+"m:"+sec+"s";
	      entries.add(new TISEntry("offline", time, String.valueOf(deepSleep*100/SystemClock.elapsedRealtime())+"%", (int)(deepSleep*100/SystemClock.elapsedRealtime())));
	     
	     for(int i =0; i<frequencies.size(); i++){
	    	 entries.add(new TISEntry(frequencies.get(i).substring(0, frequencies.get(i).length() - 3) + "Mhz", tisTime.get(i), tisPercent.get(i)+"%", Integer.parseInt(tisPercent.get(i))));
	     }
		    			
		    		
	     return entries;
	    }
	
}