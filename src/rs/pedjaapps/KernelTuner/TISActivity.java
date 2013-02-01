package rs.pedjaapps.KernelTuner;


import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.slidingmenu.lib.SlidingMenu;
public class TISActivity extends SherlockActivity {

	private List<TimesEntry> times = CPUInfo.getTis();
	

	private TISAdapter tisAdapter ;
	private ListView tisListView;
	private ViewGroup header;
	private ViewGroup footer;
	private ActionBar actionBar;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
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

		final SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.side);
		
		GridView sideView = (GridView) menu.findViewById(R.id.grid);
		SideMenuAdapter sideAdapter = new SideMenuAdapter(this, R.layout.side_item);
		System.out.println("check "+sideView+" "+sideAdapter);
		sideView.setAdapter(sideAdapter);

		
		sideView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				List<SideMenuEntry> entries =  SideItems.getEntries();
				Intent intent = new Intent();
				intent.setClass(TISActivity.this, entries.get(position).getActivity());
				startActivity(intent);
				menu.showContent();
			}
			
		});
		List<SideMenuEntry> entries =  SideItems.getEntries();
		for(SideMenuEntry e: entries){
			sideAdapter.add(e);
		}
		
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
