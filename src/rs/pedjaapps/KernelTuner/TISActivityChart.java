package rs.pedjaapps.KernelTuner;


import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class TISActivityChart extends SherlockActivity
{

	List<TimesEntry> times = CPUInfo.getTis();
	

	
	ViewGroup header;
	ViewGroup footer;
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

	  
	  LinearLayout chart;
	  
	  
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
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tis_chart);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(15);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(false);
	    mRenderer.setStartAngle(90);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		
		
		setDeepSleepAndUptime();
		getTISEntries();
		
		Button refresh = (Button)findViewById(R.id.button1);
		refresh.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					setDeepSleepAndUptime();
					times = CPUInfo.getTis();
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
	            SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	            
	          }
	        });
	      chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	          LayoutParams.MATCH_PARENT));
	    } else {
	      mChartView.repaint();
	    }
	  }

	public void setDeepSleepAndUptime(){
		String deepSleep = hrTimeSystem(SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());
		String bootTime = hrTimeSystem(SystemClock.elapsedRealtime());
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
			
			entries.add(new TISEntry(String.valueOf(t.getFreq()/1000)+"Mhz", hrTime(t.getTime()), String.valueOf(t.getTime()*100/totalTime) + "%", (int)(t.getTime()*100/totalTime)));
			mSeries.add(String.valueOf(t.getFreq()/1000)+"Mhz(" + hrTime(t.getTime())+")", t.getTime());
	        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
	        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
	        mRenderer.addSeriesRenderer(renderer);
	        if (mChartView != null) {
	          mChartView.repaint();
	        }
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
