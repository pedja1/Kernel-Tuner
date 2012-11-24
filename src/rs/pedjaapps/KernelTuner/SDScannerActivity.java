package rs.pedjaapps.KernelTuner;


import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.ads.*;
import java.io.*;
import java.text.*;
import java.util.*;

import java.lang.Process;

public class SDScannerActivity extends Activity
{

	
	ProgressDialog pd;
	SDScannerAdapter sdAdapter ;
	ListView sdListView;
	List<SDScannerEntry> entries = new ArrayList<SDScannerEntry>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sd_scanner);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
	//	new ScannSDCard().execute(new String[] {"/sdcard/"});
	Toast.makeText(this, size(1276), Toast.LENGTH_LONG).show();
		sdListView = (ListView) findViewById(R.id.list);
		sdAdapter = new SDScannerAdapter(this, R.layout.sd_scanner_list_item);
		sdListView.setAdapter(sdAdapter);

		sdListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
				new ScannSDCard().execute(new String[] {entries.get(arg2).getPath()});
			}
			
		});
		
	}

	/*private List<SDScannerEntry> getSDScannerEntries()
	{

		final List<TISEntry> entries = new ArrayList<TISEntry>();

		


		return entries;
	}*/

	private class ScannSDCard extends AsyncTask<String, Integer, Void> {
		String line;
		int i = 0;
		

		
		
		
		@Override
		protected Void doInBackground(String... args) {
			
			Process proc = null;
		
			try
			{
				proc = Runtime.getRuntime().exec("du -d 1 "+args[0]);


				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			
				
				while ( ( line = bufferedReader.readLine() ) != null )
				{

					entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), size(Integer.parseInt(line.substring(0, line.indexOf("/")).trim())), line.substring(line.indexOf("/"), line.length()).trim()) );
				}
			}
			catch (IOException e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res) {
			pd.dismiss();
			
			Collections.sort(entries,new MyComparator());
			entries.remove(entries.get(0));
			for(int i = entries.size(); i>20; i--){
				entries.remove(entries.size()-1);
			}
				
			sdAdapter.clear();
			
			for (final SDScannerEntry entry : entries)
			{
				sdAdapter.add(entry);
				
			}
			sdListView.invalidate();
			sdAdapter.notifyDataSetChanged();
			
			
		}
		@Override
		protected void onPreExecute(){
		
			entries.clear();
			pd = new ProgressDialog(SDScannerActivity.this);
			pd.setIndeterminate(true);
			pd.setTitle("Scanning SD Card");
			pd.setIcon(R.drawable.info);
			pd.setMessage("Please Wait\n" +
					"This can take a while");
		
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
	
	public String size(int size){
		String hrSize = "";
		int k = size;
		double m = size/1024.0;
		double g = size/1048576.0;
		double t = size/1073741824.0;
		
		DecimalFormat dec = new DecimalFormat("0.00");
	
		if (k!=0)
		{
	
			hrSize = dec.format(k).concat("KB");

		}
	else if (m!=0)
		{
		
			hrSize = dec.format(m).concat("MB");
		}
	else if (g!=0)
		{
			
			hrSize = dec.format(g).concat("GB");
		}
	else if (t!=0)
		{
	
			hrSize = dec.format(t).concat("TB");
		}
		
		return hrSize;
		
	}
	
}
