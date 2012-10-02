package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rs.pedjaapps.KernelTuner.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;




public class cpuTimesCpu3 extends ListActivity{
	
	
	public String cpu0freq= "1 2 2 3 4 5 6 7";
	public String time;
	public String[] delims;
	public String first;
	String[] freqs;
	List<String> where = new ArrayList<String>();
	List<String> percent = new ArrayList<String>();
	List<String> frequencies = new ArrayList<String>();
	List<Integer> perint = new ArrayList<Integer>();
	
	
	String[] strarray;
	String[] strarray2;
	String[] strarray3;
	Integer[] intarray;
	
	
	String mynewstring;
	int itemCount;
	SharedPreferences sharedPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = sharedPrefs.getString("theme", "system");
		if (theme.equals("system")) {
			setTheme(android.R.style.Theme_DeviceDefault);
		} else if (theme.equals("holo")) {
			setTheme(android.R.style.Theme_Holo);
		} else if (theme.equals("holo_light")) {
			setTheme(android.R.style.Theme_Holo_Light);
		} else if (theme.equals("dark")) {
			setTheme(android.R.style.Theme_Black);
		} else if (theme.equals("light")) {
			setTheme(android.R.style.Theme_Light);
		} else if (theme.equals("holo_no_ab")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar);
		} else if (theme.equals("holo_wp")) {
			setTheme(android.R.style.Theme_Holo_Wallpaper);
		} else if (theme.equals("holo_fs")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		} else if (theme.equals("holo_light_dark_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		} else if (theme.equals("holo_light_no_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		} else if (theme.equals("holo_light_fs")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		}
	super.onCreate(savedInstanceState);
	
	
	read();
	read2();
	read3();
	read4();
	apply();

	}
public void apply(){
	strarray = where.toArray(new String[0]);
	strarray2 = percent.toArray(new String[0]);
	strarray3 = frequencies.toArray(new String[0]);
	intarray = perint.toArray(new Integer[0]);
	
	


MobileArrayAdapter adapter = new MobileArrayAdapter(this, strarray3, strarray, strarray2, intarray);
setListAdapter(adapter);
}
	
	public void read(){
	
		
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			frequencies.add("Up-time");
			frequencies.add("Deep Sleep");
			while ((strLine = br.readLine()) != null)   {

				delims = strLine.split(" ");
				String freq = delims[0];
				freq= 	freq.substring(0, freq.length()-3)+"Mhz";

				frequencies.add(freq);

			}
			
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	
	public void read4(){
		

		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			long fulltime = SystemClock.elapsedRealtime();
			long uptime = SystemClock.uptimeMillis();
			long sleeptime = (fulltime-uptime);
			int pc = (int) (sleeptime*100/fulltime);
			perint.add((int) (uptime*100/fulltime));
			perint.add(pc);
			
			while ((strLine = br.readLine()) != null)   {


				delims = strLine.split(" ");
				String freq = delims[1];

			long	temp = Integer.parseInt(freq);	
			temp = (int) (temp*1000/uptime);
				perint.add((int) temp);

			}
			
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	
	
	public void read2(){
	
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  String temp2 = String.valueOf(SystemClock.elapsedRealtime()-SystemClock.uptimeMillis()); 
			  int time = Integer.parseInt(temp2);
			  int hr =  ((time / 1000) / 3600);
		      int mn =  (((time / 1000) / 60) % 60);
		      int sc =  ((time / 1000) % 60);
		      String minut = String.valueOf(mn);
		       String sekund = String.valueOf(sc);
		       String sati = String.valueOf(hr);
		       
		       temp2= sati+"h:"+minut+"m:"+sekund+"s";
		       
				long uptime = SystemClock.uptimeMillis();
				hr =  (int) ((uptime / 1000) / 3600);
			      mn =  (int) (((uptime / 1000) / 60) % 60);
			      sc =  (int) ((uptime / 1000) % 60);
				minut = String.valueOf(mn);
				sekund = String.valueOf(sc);
				sati = String.valueOf(hr);
				String tmp;
				tmp= sati+"h:"+minut+"m:"+sekund+"s";
		       where.add(tmp);
		       where.add(temp2);
			  
			  while ((strLine = br.readLine()) != null)   {
				  int temp;
				  int h;
				 int m;
				  int s;
				  String min;
				  String sec;
				  String sat;

			      delims = strLine.split(" ");
			       first = delims[1];
			       temp = Integer.parseInt(first);
			    
			      h =  ((temp / 100) / 3600);
			      m =  (((temp / 100) / 60) % 60);
			      s =  ((temp / 100) % 60);

			      
			       min = String.valueOf(m);
			       sec = String.valueOf(s);
			       sat = String.valueOf(h);
			       first = sat+"h:"+min+"m:"+sec+"s";
			 
			       where.add(first);
			       
			  }
			
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }

	}
	

	public void read3(){

		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			long fulltime = SystemClock.elapsedRealtime();
			long uptime = SystemClock.uptimeMillis();
			long sleeptime = (fulltime-uptime);
			String pc = String.valueOf(sleeptime*100/fulltime);
			
			
			String pc2 = String.valueOf(uptime*100/fulltime);
			percent.add(pc2+"%");
			percent.add(pc+"%");
			while ((strLine = br.readLine()) != null)   {
			

				delims = strLine.split(" ");
				String perc = delims[1];
				long	temp = Integer.parseInt(perc);	
				temp = (int) (temp*1000/uptime);
				perc = String.valueOf(temp);
				percent.add(perc+"%");

			}
			
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	

	
	public class MobileArrayAdapter extends BaseAdapter implements OnClickListener{
		private Context mContext;
		private LayoutInflater linflater;
		private TextView txt_1, txt_2, txt_3;
		 private ProgressBar pr1;
		private String[] str1;
		private String[] str2;
		private String[] str3;
		 private Integer[] int1;
		 
		 private class OnItemClickListener implements OnClickListener{       
			    private int mPosition;
			    OnItemClickListener(int position){
			        mPosition = position;
			    }
			    
			    @Override
			    public void onClick(View arg0) {
			        Log.v("TAG", "onItemClick at position" + mPosition);          
			    }       
			}

		public MobileArrayAdapter(Context context, String[] s1, String[] s2, String[] s3, Integer[] i1) {
		    mContext = context;
		    str1 = s1;
		    str2 = s2;
			str3 = s3;
			int1 = i1;
		    linflater = LayoutInflater.from(context);
		}


		public int getCount() {
		    return str1.length;
		}


		public Object getItem(int arg0) {
		    return str1[arg0];
		}


		public long getItemId(int arg0) {
		    return arg0;
		}



		public View getView(final int position, View convertView, ViewGroup parent) {

		    if (convertView == null) {

		        convertView = linflater.inflate(R.layout.rowlayout, null);

		    }
		    convertView.setOnClickListener(new OnItemClickListener(position));
		    txt_1 = (TextView) convertView.findViewById(R.id.textView1);
		    txt_2 = (TextView) convertView.findViewById(R.id.textView2);
			txt_3 = (TextView) convertView.findViewById(R.id.label);
			pr1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			pr1.setProgress(int1[position]);
		    txt_1.setText(str1[position]);
		    txt_2.setText(str2[position]);
			txt_3.setText(str3[position]);

		    return convertView;

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}

		}

}
