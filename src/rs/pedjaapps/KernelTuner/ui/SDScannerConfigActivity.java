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



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import de.ankri.views.Switch;

import java.text.DecimalFormat;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.ui.SDScannerActivity;
import rs.pedjaapps.KernelTuner.ui.SDScannerConfigActivity;

public class SDScannerConfigActivity extends SherlockActivity
{

	
	
	
	private Switch sw;
	private static final int GET_CODE = 0;
	  public static final String TYPE = "type";

	  private static int[] COLORS = new int[] {Color.RED, 
		  Color.GREEN};
	  
	  String pt;

	  private CategorySeries mSeries = new CategorySeries("");

	  private DefaultRenderer mRenderer = new DefaultRenderer();

	  private String mDateFormat;

	  private GraphicalView mChartView;

	  TextView path;
	  LinearLayout chart;
	  int labelColor;
	  
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
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.SwitchCompatAndSherlockLight);
			labelColor = Color.BLACK;
		}
		else if(theme.equals("dark")){
			setTheme(R.style.SwitchCompatAndSherlock);
			labelColor = Color.WHITE;
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.SwitchCompatAndSherlockLightDark);
			labelColor = Color.BLACK;
			
		}
		super.onCreate(savedInstanceState);

		boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSDPresent==false){
			finish();
			Toast.makeText(this, "External Storage not mounted", Toast.LENGTH_LONG).show();
		}
		setContentView(R.layout.sd_scanner_config);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(25);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(false);
	    mRenderer.setStartAngle(90);
		mRenderer.setAntialiasing(true);
		mRenderer.setLabelsColor(labelColor);
		mRenderer.setApplyBackgroundColor(false);
		final SharedPreferences.Editor editor = preferences.edit();
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
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
		final Switch displayType = (Switch)findViewById(R.id.display_in_switch);
		displayType.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				if(arg0.isChecked()){
					arg0.setText("Dispay Result in List");
				}
				else if(arg0.isChecked()==false){
					arg0.setText("Dispay Result in Chart");
				}
			}
			
		});
		
		path = (TextView)findViewById(R.id.path);
		final EditText depth = (EditText)findViewById(R.id.editText2);
		final EditText numberOfItems = (EditText)findViewById(R.id.editText3);
		path.setText(preferences.getString("SDScanner_path", Environment.getExternalStorageDirectory().getPath()));
		pt = preferences.getString("SDScanner_path", Environment.getExternalStorageDirectory().getPath());
		depth.setText(preferences.getString("SDScanner_depth", "1"));
		numberOfItems.setText(preferences.getString("SDScanner_items", "20"));
		if(preferences.getBoolean("SDScanner_scann_type", false)){
		sw.setChecked(true);
		}
		else{
			sw.setChecked(false);
		}
		if(preferences.getBoolean("SDScanner_display_type", false)){
			displayType.setChecked(true);
		}
		else{
			displayType.setChecked(false);
		}
		Button scan = (Button)findViewById(R.id.button2);
		scan.setOnClickListener(new View.OnClickListener(){
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
				intent.putExtra("path", pt);
				intent.putExtra("depth", depth.getText().toString());
				intent.putExtra("items", numberOfItems.getText().toString());
				intent.putExtra("scannType", scannType);
				if(displayType.isChecked()){
					intent.setClass(SDScannerConfigActivity.this, SDScannerActivityList.class);
					editor.putBoolean("SDScanner_display_type", true);
				}
				else{
					intent.setClass(SDScannerConfigActivity.this, SDScannerActivity.class);
					editor.putBoolean("SDScanner_display_type", false);
				}
				startActivity(intent);
				editor.putString("SDScanner_path", path.getText().toString());
				editor.putString("SDScanner_depth", depth.getText().toString());
				editor.putString("SDScanner_items", numberOfItems.getText().toString());
				
				editor.commit();
				
				
			}
			
		});
		
		((Button)findViewById(R.id.browse)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(SDScannerConfigActivity.this, FMActivity.class), GET_CODE);
				
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if (requestCode == GET_CODE){
	   if (resultCode == RESULT_OK) {
		   pt = data.getStringExtra("path");
		   path.setText(pt);
	   }
	   
	  }
	 }
	
}
