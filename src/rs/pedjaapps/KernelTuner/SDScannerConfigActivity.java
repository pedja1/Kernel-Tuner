package rs.pedjaapps.KernelTuner;


import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.ads.*;

import de.ankri.views.Switch;

import java.text.*;
import java.util.*;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class SDScannerConfigActivity extends Activity
{

	
	
	List<SDScannerEntry> entries = new ArrayList<SDScannerEntry>();
	
	  LinearLayout l;
	  Switch sw;
	  public static final String TYPE = "type";

	  private static int[] COLORS = new int[] {Color.RED, 
		  Color.GREEN};

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

		setContentView(R.layout.sd_scanner_config);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(25);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(false);
	    mRenderer.setStartAngle(90);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = sharedPrefs.edit();
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
	System.out.println(humanRadableSize(getAvailableSpaceInBytes()));
	mSeries.add("Used: "   + humanRadableSize(getUsedSpaceInBytes()), getUsedSpaceInBytes());
	SimpleSeriesRenderer renderer2 = new SimpleSeriesRenderer();
    renderer2.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
    mRenderer.addSeriesRenderer(renderer2);
	if (mChartView != null) {
	      mChartView.repaint();
	    }
	mSeries.add("Free: "   + humanRadableSize(getAvailableSpaceInBytes()), getAvailableSpaceInBytes());
	SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
    renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
    mRenderer.addSeriesRenderer(renderer);
    
    if (mChartView != null) {
      mChartView.repaint();
    }
		sw = (Switch)findViewById(R.id.switch1);
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				if(arg0.isChecked()){
					arg0.setText("Scann Folders+Files");
				}
				else if(arg0.isChecked()==false){
					arg0.setText("Scann Folders");
				}
			}
			
		});
		
		final EditText path = (EditText)findViewById(R.id.editText1);
		final EditText depth = (EditText)findViewById(R.id.editText2);
		final EditText numberOfItems = (EditText)findViewById(R.id.editText3);
		path.setText(sharedPrefs.getString("SDScanner_path", "/sdcard/"));
		depth.setText(sharedPrefs.getString("SDScanner_depth", "1"));
		numberOfItems.setText(sharedPrefs.getString("SDScanner_items", "20"));
		if(sharedPrefs.getBoolean("SDScanner_scann_type", false)){
		sw.setChecked(true);
		}
		else{
			sw.setChecked(false);
		}
		Button scan = (Button)findViewById(R.id.button2);
		l = (LinearLayout)findViewById(R.id.ll);
		scan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String scannType = " ";
				if(sw.isChecked()){
					scannType = " -a ";
					editor.putBoolean("SDScanner_scann_type", true);
				}
				else{
					editor.putBoolean("SDScanner_scann_type", false);
				}
				Intent intent = new Intent();
				intent.putExtra("path", path.getText().toString());
				intent.putExtra("depth", depth.getText().toString());
				intent.putExtra("items", numberOfItems.getText().toString());
				intent.putExtra("scannType", scannType);
				intent.setClass(SDScannerConfigActivity.this, SDScannerActivity.class);
				startActivity(intent);
				editor.putString("SDScanner_path", path.getText().toString());
				editor.putString("SDScanner_depth", depth.getText().toString());
				editor.putString("SDScanner_items", numberOfItems.getText().toString());
				
				editor.commit();
				
				
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

	
	public static long getAvailableSpaceInBytes() {
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
	public static long getUsedSpaceInBytes() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks()) * (long) stat.getBlockSize();

	    return usedSpace;
	}

	public String humanRadableSize(long size){
		String hrSize = "";
		
		long b = size;
		double k = size/1024.0;
		double m = size/1048576.0;
		double g = size/1073741824.0;
		double t = size/1099511627776.0;
		
		DecimalFormat dec = new DecimalFormat("0.00");
	
		if (t>1)
		{
	
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
		
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
	
			hrSize = dec.format(k).concat("KB");

		}
		else if(b>1){
			hrSize = dec.format(b).concat("B");
		}
		
		
		
		
		return hrSize;
		
	}
	
	
	
}
