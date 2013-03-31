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



import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.widget.*;
import com.google.ads.*;
import java.io.*;
import java.util.*;
import org.achartengine.*;
import org.achartengine.model.*;
import org.achartengine.renderer.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.entry.*;
import java.lang.Process;
import rs.pedjaapps.KernelTuner.tools.Tools;

public class SDScannerActivity extends Activity
{

	
	private ProgressDialog pd;
	private List<SDScannerEntry> entries = new ArrayList<SDScannerEntry>();
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
	  int labelColor;

	  private CategorySeries mSeries = new CategorySeries("");

	  private DefaultRenderer mRenderer = new DefaultRenderer();

	  private String mDateFormat;

	  private GraphicalView mChartView;

	  
	  LinearLayout chart;
	  String depth;
	  String numberOfItems;
	  String scannType;
	  String arch;
	  
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
		
		if (theme.equals("light")) 
		{
			setTheme(android.R.style.Theme_Holo_Light);
			labelColor = Color.BLACK;
		} 
		else if (theme.equals("dark")) 
		{
			setTheme(android.R.style.Theme_Holo);
			labelColor = Color.WHITE;
		} 
		else if (theme.equals("light_dark_action_bar")) 
		{
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			labelColor = Color.BLACK;
		}
		else if (theme.equals("miui_light")) 
		{
			setTheme(R.style.Theme_Miui_Light);
			labelColor = Color.BLACK;
		} 
		else if (theme.equals("miui_dark")) 
		{
			setTheme(R.style.Theme_Miui_Dark);
			labelColor = Color.WHITE;
		} 
		else if (theme.equals("sense5")) 
		{
			setTheme(R.style.Theme_Sense5);
			labelColor = Color.WHITE;
		}
		else if (theme.equals("sense5_light")) 
		{
			setTheme(R.style.Theme_Light_Sense5);
			labelColor = Color.BLACK;
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sd_scanner);
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
		mRenderer.setAntialiasing(true);
		mRenderer.setLabelsColor(labelColor);
		mRenderer.setApplyBackgroundColor(false);
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
	
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		depth = intent.getStringExtra("depth");
		numberOfItems = intent.getStringExtra("items");
		scannType = intent.getStringExtra("scannType");
		arch = Tools.getAbi();
		new ScannSDCard().execute(new String[] {path,
				depth,
				numberOfItems,
				scannType});
		
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
	        	  /*SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	              if (seriesSelection == null) {
	                Toast.makeText(SDScannerActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
	                    .show();
	              } else {
	               
	            	  new ScannSDCard().execute(new String[] {entries.get(seriesSelection.getPointIndex()).getPath(),
	          				depth,
	          				numberOfItems,
	          				scannType});
	              }*/

	          }
	        });
	      chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	          LayoutParams.MATCH_PARENT));
	    } else {
	      mChartView.repaint();
	    }
	  }

	private class ScannSDCard extends AsyncTask<String, String, Void> {
		String line;
		int numberOfItems;
		
		
		
		
		@Override
		protected Void doInBackground(String... args) {
			entries = new ArrayList<SDScannerEntry>();
			Process proc = null;
			try{
			numberOfItems = Integer.parseInt(args[2]);
			}
			catch(NumberFormatException e){
				numberOfItems = 20;
			}
			
			try
			{
				proc = Runtime.getRuntime().exec(getFilesDir().getPath()+"/du-"+arch+" -d "+args[1] + args[3] +args[0]);
				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				while ( ( line = bufferedReader.readLine() ) != null )
				{
					entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), Tools.kByteToHumanReadableSize(Integer.parseInt(line.substring(0, line.indexOf("/")).trim())), line.substring(line.indexOf("/"), line.length()).trim(), false) );
					publishProgress(line.substring(line.indexOf("/"), line.length()).trim());
				}
				proc.waitFor();
				proc.destroy();
			}
			catch (Exception e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values)
		{
			pd.setMessage(getResources().getString(R.string.sd_scanning)+values[0]);
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res) {
			pd.dismiss();
			
			Collections.sort(entries,new MyComparator());
			if(entries.isEmpty()==false){
			entries.remove(entries.get(0));
			}
			for(int i = entries.size(); i>numberOfItems; i--){
				entries.remove(entries.size()-1);
			}
			mSeries.clear();
				for(SDScannerEntry e : entries){
					mSeries.add(e.getFileName()   + " " +e.getHRsize(), e.getSize());
			        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			        mRenderer.addSeriesRenderer(renderer);
			        if (mChartView != null) {
			          mChartView.repaint();
			        }
				}
			
			
			
		}
		@Override
		protected void onPreExecute(){
		
			pd = new ProgressDialog(SDScannerActivity.this);
			pd.setIndeterminate(true);
			pd.setTitle(getResources().getString(R.string.sd_please_wait));
			pd.setIcon(R.drawable.info);
		pd.setOnCancelListener(new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				new ScannSDCard(). cancel(true);
			}
			
		});
			pd.show();
		}

	}
	
	class MyComparator implements Comparator<SDScannerEntry>{
	  public int compare(SDScannerEntry ob1, SDScannerEntry ob2){
	   return ob2.getSize() - ob1.getSize() ;
	  }
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
