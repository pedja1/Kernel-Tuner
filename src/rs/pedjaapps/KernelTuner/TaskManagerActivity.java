package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.*;
import android.widget.*;

import com.google.ads.*;
import java.io.*;
import java.util.*;
import java.lang.Process;
import java.net.URL;
import java.net.URLConnection;

public class TaskManagerActivity extends Activity
{
	static TaskManagerAdapter taskManagerAdapter ;
	ListView taskManagerListView;
	static ProgressDialog pd = null;
	DatabaseHandler db;
	List<String> names;
	List<String> names2;
	List<Drawable> icons;
	List<String> pids;
	List<String> cpu;
	List<String> mem;
	boolean check = false;
	Button displayAll;
	
	ProgressDialog mProgressDialog;
	private class LoadProcesses extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... arg)
		{
			
			 names = new ArrayList<String>();
		        names2 = new ArrayList<String>();
	        BufferedReader br = null;
	        pids = new ArrayList<String>();
	        cpu = new ArrayList<String>();
	        mem = new ArrayList<String>();
	        try
			{
	        	
	        	Process process = null;
				process = Runtime.getRuntime().exec("top -s cpu -n 1 -d 1");

				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String strLine;
				int i =0;
				PackageManager pm = getApplicationContext().getPackageManager();
				ApplicationInfo ai;
				while ((strLine = br.readLine()) != null)
				{
					i = i +1;
					if(i > 8){
						try {
							ai = pm.getApplicationInfo( strLine.substring(51, strLine.length()).trim(), 0);
							
							names.add(pm.getApplicationLabel(ai).toString());
							pids.add(strLine.substring(0, 5).trim());
							cpu.add(strLine.substring(10, 13));
							mem.add(strLine.substring(30, 37));
							names2.add(strLine.substring(51, strLine.length()).trim());
							} 
						catch (final NameNotFoundException e)
						{ 
						ai = null; 
						if(arg[0].equals("1")){
						names2.add(strLine.substring(51, strLine.length()).trim());
						names.add(strLine.substring(51, strLine.length()).trim());
						pids.add(strLine.substring(0, 5).trim());
						cpu.add(strLine.substring(10, 13).trim());
						mem.add(strLine.substring(30, 37).trim());
							}
						}
				
						
					}
				}
					

				//br.close();
			}
			catch (Exception e)
			{
			e.printStackTrace();
			}
	        finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		System.out.println(names.size());
			
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
	
        icons = new ArrayList<Drawable>();
		
		final PackageManager pm = getApplicationContext().getPackageManager(); 
		ApplicationInfo ai; 
		//System.out.println(runningProcesses.size());
		for(int i = 0; i < names2.size(); i++){
			try {
				
				ai = pm.getApplicationInfo( names2.get(i), 0);
				icons.add(pm.getApplicationIcon(ai));
				} 
			catch (final NameNotFoundException e)
			{ 
			ai = null; 
			try {
				icons.add(pm.getApplicationIcon("com.android.packageinstaller"));
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			
		}
		
		
		
		System.out.println(icons.size());
	
		
			return null;
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			taskManagerAdapter.clear();
			mProgressDialog = new ProgressDialog(TaskManagerActivity.this);
			mProgressDialog.setMessage("Loading process information");
			mProgressDialog.show();
			
		}

		
		@Override
		protected void onPostExecute(String result)
		{

			check();
			for (final TaskManagerEntry entry : getTaskManagerEntries())
			{
				taskManagerAdapter.add(entry);
			}
			taskManagerAdapter.notifyDataSetChanged();
			mProgressDialog.dismiss();
			

		}
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_manager);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		
		taskManagerListView = (ListView) findViewById(R.id.list);
		taskManagerAdapter = new TaskManagerAdapter(this, R.layout.task_manager_list_item);
		taskManagerListView.setAdapter(taskManagerAdapter);
		
		
		displayAll = (Button)findViewById(R.id.all);
		check();
		
		//if(check==false){
			new LoadProcesses().execute(new String[] {"0"});
			
			/*}
			else if(check==true){
			new LoadProcesses().execute(new String[] {"0"});
			
			}*/
		
		displayAll.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					if(check==false){
					new LoadProcesses().execute(new String[] {"1"});
					check=true;
					}
					else if(check==true){
					new LoadProcesses().execute(new String[] {"0"});
					check=false;
					}

				}

			});

		
	}
	
public void check(){
	if(check==false){
		
		displayAll.setText("All Processes");
		
	}
	else if(check == true){
		displayAll.setText("Applications Only");
		
	}
}
	
	
	private  List<TaskManagerEntry> getTaskManagerEntries()
	{
		

		final List<TaskManagerEntry> entries = new ArrayList<TaskManagerEntry>();

			 
		 for(int i = 0; i<names.size(); i++){
			 entries.add(new TaskManagerEntry(names.get(i), pids.get(i), 0, icons.get(i), "CPU: "+cpu.get(i), "Mem: "+mem.get(i)));

		 }
				
				
		return entries;
	}
	
		
}
