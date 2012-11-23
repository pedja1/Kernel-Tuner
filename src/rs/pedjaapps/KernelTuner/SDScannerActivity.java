package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

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
		new ScannSDCard().execute(new String[] {"AppProjects"});
		sdListView = (ListView) findViewById(R.id.list);
		sdAdapter = new SDScannerAdapter(this, R.layout.sd_scanner_list_item);
		sdListView.setAdapter(sdAdapter);

		sdListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sdAdapter.clear();
				new ScannSDCard().execute(new String[] {entries.get(arg2).getName()});
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
				proc = Runtime.getRuntime().exec("du -d 1 /sdcard/"+args[0]);


				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			
				
				while ( ( line = bufferedReader.readLine() ) != null )
				{

				entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), size(Integer.parseInt(line.substring(0, line.indexOf("/")).trim()))));
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
			
				
				
				
			for (final SDScannerEntry entry : entries)
			{
				sdAdapter.add(entry);
				
			}
			sdAdapter.notifyDataSetChanged();
			
			
		}
		@Override
		protected void onPreExecute(){
			
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
		int m = size/1024;
		int g = size/1048576;
		int t = size/1073741824;
		
		if (k!=0)
		{
			hrSize = String.valueOf(k)+"KB";
		}
		if (m!=0)
		{
			hrSize = String.valueOf(m)+"MB";
		}
		if (g!=0)
		{
			hrSize = String.valueOf(g)+"GB";
		}
		if (t!=0)
		{
			hrSize = String.valueOf(t)+"TB";
		}
		
		return hrSize;
		
	}
	
}
