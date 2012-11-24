package rs.pedjaapps.KernelTuner;


import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
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
	
		final EditText path = (EditText)findViewById(R.id.editText1);
		final EditText depth = (EditText)findViewById(R.id.editText2);
		final EditText numberOfItems = (EditText)findViewById(R.id.editText3);
		Button scan = (Button)findViewById(R.id.button2);
		final LinearLayout l = (LinearLayout)findViewById(R.id.ll);
		scan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				new ScannSDCard().execute(new String[] {path.getText().toString(), depth.getText().toString(), numberOfItems.getText().toString()});
				l.setVisibility(View.GONE);
			}
			
		});
		sdListView = (ListView) findViewById(R.id.list);
		sdAdapter = new SDScannerAdapter(this, R.layout.sd_scanner_list_item);
		sdListView.setAdapter(sdAdapter);

		sdListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
				//new ScannSDCard().execute(new String[] {entries.get(arg2).getPath()});
			}
			
		});
		
	}



	private class ScannSDCard extends AsyncTask<String, Integer, Void> {
		String line;
		int numberOfItems;

		
		
		
		@Override
		protected Void doInBackground(String... args) {
			
			Process proc = null;
			numberOfItems = Integer.parseInt(args[2]);
			try
			{
				proc = Runtime.getRuntime().exec("du -d "+args[1] + " " +args[0]);


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
			for(int i = entries.size(); i>numberOfItems; i--){
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
		
		
		
		
		return hrSize;
		
	}
	
	
		
}
