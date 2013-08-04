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
import android.widget.*;

import com.google.ads.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.SDAdapter;

import java.lang.Process;
import rs.pedjaapps.KernelTuner.tools.Tools;

public class SDScannerActivityList extends Activity
{
	private ProgressDialog pd;
	private List<SDScannerEntry> entries = new ArrayList<SDScannerEntry>();
	  int labelColor = Color.BLACK;
	  String depth;
	  String numberOfItems;
	  String scannType;
	  SDAdapter sDAdapter;
	  String arch;
	  
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    
	  }

	  @Override
	  protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    
	  }
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sd_analyzer_list);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
	
	    arch = Tools.getAbi();
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		depth = intent.getStringExtra("depth");
		numberOfItems = intent.getStringExtra("items");
		scannType = intent.getStringExtra("scannType");
		ListView sDListView = (ListView) findViewById(R.id.list);
		sDAdapter = new SDAdapter(this, R.layout.sd_list_row);
		sDListView.setAdapter(sDAdapter);
		sDListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,
					long arg3) {
				
				
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle(entries.get(pos).getFileName());

					builder.setMessage(getResources().getString(R.string.sd_alert_location)+" "+entries.get(pos).getPath()+"\n"+getResources().getString(R.string.sd_alert_location)+" "
							+entries.get(pos).getHRsize());

					builder.setIcon(R.drawable.ic_menu_cc);

					if(new File(entries.get(pos).getPath()).isDirectory()){
						
						
					
					builder.setPositiveButton(getResources().getString(R.string.sd_alert_scan), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								new ScannSDCard().execute( new String[] {entries.get(pos).getPath(),
										depth,
										numberOfItems,
										scannType});

							}
						});
					}
					builder.setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							new File(entries.get(pos).getPath()).delete();
							sDAdapter.remove(sDAdapter.getItem(pos));
							sDAdapter.notifyDataSetChanged();
							entries.remove(pos);
						}
					});

					AlertDialog alert = builder.create();

					alert.show();
				
				
			}
			
		});
		new ScannSDCard().execute( new String[] {path,
				depth,
				numberOfItems,
				scannType});
		
	}

	@Override
	  protected void onResume() {
	    super.onResume();
	    
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
					if(new File(line.substring(line.indexOf("/"), line.length()).trim()).isFile()){
						entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), Tools.kByteToHumanReadableSize(Integer.parseInt(line.substring(0, line.indexOf("/")).trim())), line.substring(line.indexOf("/"), line.length()).trim(), false) );	
					}
					else{
						entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), Tools.kByteToHumanReadableSize(Integer.parseInt(line.substring(0, line.indexOf("/")).trim())), line.substring(line.indexOf("/"), line.length()).trim(), true) );
					}
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
			sDAdapter.clear();
			Collections.sort(entries,new MyComparator());
			if(entries.isEmpty()==false){
			entries.remove(entries.get(0));
			}
			for(int i = entries.size(); i>numberOfItems; i--){
				entries.remove(entries.size()-1);
			}
			
				for(SDScannerEntry e : entries){
					sDAdapter.add(e);
				}
				sDAdapter.notifyDataSetChanged();
			
		}
		@Override
		protected void onPreExecute(){
			pd = new ProgressDialog(SDScannerActivityList.this);
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
