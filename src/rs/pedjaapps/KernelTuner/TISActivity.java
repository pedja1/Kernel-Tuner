package rs.pedjaapps.KernelTuner;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class TISActivity extends Activity
{

	List<TimesEntry> times = CPUInfo.getTis();
	

	TISAdapter tisAdapter ;
	ListView tisListView;
	ViewGroup header;
	ViewGroup footer;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.times_in_state);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
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
					times = CPUInfo.getTis();
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

	public void setDeepSleepAndUptime(){
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
			entries.add(new TISEntry(String.valueOf(t.getFreq()/1000)+"Mhz", hrTime(t.getTime()), String.valueOf(t.getTime()*100/totalTime) + "%", (int)(t.getTime()*100/totalTime)));
			System.out.println(hrTime(t.getTime()));
		}


		return entries;
	}
	
	public String hrTime(long time)
	{
		
		String timeString;
		String s = String.valueOf((int)((time / 100) % 60));
		String m = String.valueOf((int)((time / (100 * 60)) % 60));
		String h = String.valueOf((int)((time / (100 * 3600)) % 24));
		String d = String.valueOf((int)(time / (100 * 60 * 60 * 24)));
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
	
	public String hrTimeSystem(long time)
	{
		
		String timeString;
		String s = String.valueOf((int)((time / 1000) % 60));
		String m = String.valueOf((int)((time / (1000 * 60)) % 60));
		String h = String.valueOf((int)((time / (1000 * 3600)) % 24));
		String d = String.valueOf((int)(time / (1000 * 60 * 60 * 24)));
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
	
	public long totalTime(){
		long a=0;
        for (int i =0; i < times.size(); i++)
        {
                a = a + times.get(i).getTime();
        }
		return a;
		
	}

}
