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


import android.app.ActionBar;
import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;

import java.util.*;
import org.achartengine.*;
import org.achartengine.model.*;
import org.achartengine.renderer.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.tools.Tools;

public class TISActivityChart extends Activity
{

	private List<TimesEntry> times;
	public static final String TYPE = "type";

	  private static int[] COLORS = new int[] {Color.parseColor("#FF0000"), 
		  Color.parseColor("#F88017"), 
		  Color.parseColor("#FBB117"), 
		  Color.parseColor("#FDD017"),
		  Color.parseColor("#FFFF00"),
		  Color.parseColor("#FFFF00"),
		  Color.parseColor("#5FFB17"),
		  Color.GREEN,
		  Color.parseColor("#347C17"),
		  Color.parseColor("#387C44"),
		  Color.parseColor("#348781"),
		  Color.parseColor("#6698FF"),
		  Color.BLUE,
		  Color.parseColor("#6C2DC7"),
		  Color.parseColor("#7D1B7E"),
		  Color.WHITE,
		  Color.CYAN,
		  Color.MAGENTA,
		  Color.GRAY};

	  private CategorySeries mSeries = new CategorySeries("");

	  private DefaultRenderer mRenderer = new DefaultRenderer();

	  private String mDateFormat;

	  private GraphicalView mChartView;

	  
	  private LinearLayout chart;
	  
	  
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    mSeries = (CategorySeries) savedState.getSerializable("current_series");
	    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
	    mDateFormat = savedState.getString("date_format");
	  }

	  @Override
	  protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putSerializable("current_series", mSeries);
	    outState.putSerializable("current_renderer", mRenderer);
	    outState.putString("date_format", mDateFormat);
	  }
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		times = IOHelper.getTis();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = preferences.getString("theme", "light");
		
		setTheme(Tools.getPreferedTheme(theme));
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tis_chart);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(15);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(false);
	    mRenderer.setStartAngle(90);
		boolean ads = preferences.getBoolean("ads", true);
        final AdView adView = (AdView)findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
                .addTestDevice(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5750ECFACEA6FCE685DE7A97D8C59A5F")
                .addTestDevice("05FBCDCAC44495595ACE7DC1AEC5C208")
                .addTestDevice("40AA974617D79A7A6C155B1A2F57D595")
                .build();
        if(ads)adView.loadAd(adRequest);
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
			
		setDeepSleepAndUptime();
		getTISEntries();
		
		Button refresh = (Button)findViewById(R.id.button1);
		refresh.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					setDeepSleepAndUptime();
					times = IOHelper.getTis();
					getTISEntries();
					
				}

			});

	}
	@Override
	  protected void onResume() {
	    super.onResume();
	    if (mChartView == null) {
	      chart = (LinearLayout) findViewById(R.id.chart);
	      
	      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
	      mRenderer.setClickEnabled(true);
	      mRenderer.setSelectableBuffer(10);
	      mChartView.setOnClickListener(new View.OnClickListener() {
	          @Override
	          public void onClick(View v) {
	       
	          }
	        });
	      chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	          LayoutParams.MATCH_PARENT));
	    } else {
	      mChartView.repaint();
	    }
	  }

	private void setDeepSleepAndUptime(){
		String deepSleep = Tools.msToHumanReadableTime(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());
		String bootTime = Tools.msToHumanReadableTime(SystemClock.elapsedRealtime());
		TextView deepSleepText = (TextView)findViewById(R.id.deep_sleep);
		TextView bootTimeText = (TextView)findViewById(R.id.boot_time);
		deepSleepText.setText(deepSleep);
		bootTimeText.setText(bootTime);
		
	}
	
	private List<TISEntry> getTISEntries()
	{

		final List<TISEntry> entries = new ArrayList<TISEntry>();
		
		long totalTime = totalTime();
		
		entries.clear();
		mSeries.clear();
		
		for (TimesEntry t : times)
		{
			
			entries.add(new TISEntry((t.getFreq()/1000)+getResources().getString(R.string.mhz), Tools.msToHumanReadableTime2(t.getTime()), (t.getTime()*100/totalTime) + "%", (int)(t.getTime()*100/totalTime)));
			mSeries.add((t.getFreq()/1000)+"Mhz(" + Tools.msToHumanReadableTime2(t.getTime())+")", t.getTime());
	        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
	        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
	        mRenderer.addSeriesRenderer(renderer);
	        if (mChartView != null) {
	          mChartView.repaint();
	        }
		}


		return entries;
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
	public boolean onOptionsItemSelected(MenuItem item) {
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
