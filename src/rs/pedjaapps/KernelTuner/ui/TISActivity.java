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


import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.*;
import com.google.ads.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import android.view.View.OnClickListener;
import rs.pedjaapps.KernelTuner.R;
public class TISActivity extends SherlockActivity {

	private List<TimesEntry> times;
	

	private TISAdapter tisAdapter ;
	private ListView tisListView;
	private ViewGroup header;
	private ViewGroup footer;
	private ActionBar actionBar;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		times = IOHelper.getTis();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.IndicatorLight);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.IndicatorDark);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.IndicatorLightDark);
			
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.times_in_state);
			actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
        
		
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		tisListView = (ListView) findViewById(R.id.list);
		tisAdapter = new TISAdapter(this, R.layout.tis_list_item);
		LayoutInflater inflater = getLayoutInflater();
		header = (ViewGroup)inflater.inflate(R.layout.tis_header, tisListView, false);
		footer = (ViewGroup)inflater.inflate(R.layout.tis_footer, tisListView, false);
		
		tisListView.addHeaderView(header, null, false);
		tisListView.addFooterView(footer, null, false);
		String deepSleep = hrTimeSystem(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());
		String bootTime = hrTimeSystem(SystemClock.elapsedRealtime());
		TextView deepSleepText = (TextView)footer.findViewById(R.id.deep_sleep);
		TextView bootTimeText = (TextView)footer.findViewById(R.id.boot_time);
		deepSleepText.setText(deepSleep);
		bootTimeText.setText(bootTime);
		setDeepSleepAndUptime();
		tisListView.setAdapter(tisAdapter);

		for (final TISEntry entry : getTISEntries())
		{
			tisAdapter.add(entry);
		}
		Button refresh = (Button)findViewById(R.id.button1);
		refresh.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					setDeepSleepAndUptime();
					times = IOHelper.getTis();
					tisAdapter.clear();
					for (final TISEntry entry : getTISEntries())
					{
						tisAdapter.add(entry);
					}
					tisAdapter.notifyDataSetChanged();
					tisListView.invalidate();
				}

			});

	}

	private void setDeepSleepAndUptime(){
		String deepSleep = hrTimeSystem(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());
		String bootTime = hrTimeSystem(SystemClock.elapsedRealtime());
		TextView deepSleepText = (TextView)footer.findViewById(R.id.deep_sleep);
		TextView bootTimeText = (TextView)footer.findViewById(R.id.boot_time);
		deepSleepText.setText(deepSleep);
		bootTimeText.setText(bootTime);
		
	}
	
	private List<TISEntry> getTISEntries()
	{

		final List<TISEntry> entries = new ArrayList<TISEntry>();
		
		long totalTime = totalTime();
		
		
		
		for (TimesEntry t : times)
		{
			entries.add(new TISEntry((t.getFreq()/1000)+"Mhz", hrTime(t.getTime()), (t.getTime()*100/totalTime) + "%", (int)(t.getTime()*100/totalTime)));
			System.out.println(hrTime(t.getTime()));
		}


		return entries;
	}
	
	private  String hrTime(long time)
	{
		
		String timeString;
		String s = ""+((int)((time / 100) % 60));
		String m = ""+((int)((time / (100 * 60)) % 60));
		String h = ""+((int)((time / (100 * 3600)) % 24));
		String d = ""+((int)(time / (100 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		timeString = builder.toString();
		return timeString;


	}
	
	private String hrTimeSystem(long time)
	{
		
		String timeString;
		String s = ""+((int)((time / 1000) % 60));
		String m = ""+((int)((time / (1000 * 60)) % 60));
		String h = ""+((int)((time / (1000 * 3600)) % 24));
		String d = ""+((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		timeString = builder.toString();
		return timeString;


	}
	
	private long totalTime(){
		long a=0;
		int timesSize = times.size();
        for (int i =0; i < timesSize; i++)
        {
                a = a + times.get(i).getTime();
        }
		return a;
		
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
