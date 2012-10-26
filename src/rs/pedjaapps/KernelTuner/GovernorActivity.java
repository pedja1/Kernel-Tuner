package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class GovernorActivity extends Activity {
	
	GovernorSettingsAdapter govAdapter ;
	ListView govListView;
	String[] filesx;
	List<String> fileList;
	List<String> availableGovs;
	List<String> govValues;
	String newvalue;
	String curfile;
	String governor = "xondemand";
	List<String> governors;
	List<String> temp;
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.governor_settings);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads==true){AdView adView = (AdView)findViewById(R.id.ad);
		adView.loadAd(new AdRequest());}
		scanAvailableGovernors();
		 govListView = (ListView) findViewById(R.id.list);
		if(!availableGovs.isEmpty()){
		
	      govAdapter = new GovernorSettingsAdapter(this, R.layout.governor_list_item);
	     govListView.setAdapter(govAdapter);
	    
	     for(final GovEntry entry : getGovEntries()) {
	      govAdapter.add(entry);
	     }
		}
		else{
			TextView tv = (TextView)findViewById(R.id.textView1);
			tv.setVisibility(View.VISIBLE);
			tv.setText("Current Governor(s) doesn't have any settings\n\n" +
					"Only following governors can be configured:\n" +
					"ondemand, conservative, interactive, xondemand");
		}
			govListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
				{
					
				String[] valuess = govValues.toArray(new String[0]);
					AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

					builder.setTitle(fileList.get(position));

					builder.setMessage("Set new value: ");

					builder.setIcon(R.drawable.icon);

					
				 final EditText input = new EditText(view.getContext());
	               input.setHint(valuess[position]);
					input.selectAll();
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					input.setGravity(Gravity.CENTER_HORIZONTAL);
					
					builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							newvalue = String.valueOf(input.getText());
							curfile = fileList.get(position);
							new ChangeGovernorSettings(GovernorActivity.this).execute(new String[] {String.valueOf(input.getText()), fileList.get(position), governors.get(position)});
						 
							try {
								Thread.sleep(700);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							getGovEntries();

							govAdapter.clear();
							for(final GovEntry entry : getGovEntries()) {
								govAdapter.add(entry);
							}
							govAdapter.notifyDataSetChanged();
							govListView.invalidate();
						}
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
						}
						
					});
	               builder.setView(input);
					
					AlertDialog alert = builder.create();

					alert.show();
					
				
				} 
				});
		
	}
	
	public void scanAvailableGovernors(){
		File govs = new File("/sys/devices/system/cpu/cpufreq/");
	     availableGovs = new ArrayList<String>();
	  
	   
	      if(govs.exists()){
	     File[] files = govs.listFiles();
	     
	     for (File file : files){
	      availableGovs.add(file.getName());
	      

	     }
	      }
	    
	      availableGovs.removeAll(Arrays.asList("vdd_table"));
	   
	}
	
	private List<GovEntry> getGovEntries() {

	     final List<GovEntry> entries = new ArrayList<GovEntry>();
	     fileList = new ArrayList<String>();
	     govValues = new ArrayList<String>();
	     governors = new ArrayList<String>();
	     temp = new ArrayList<String>();
	     
	     for(String s : availableGovs){
	    	 File gov = new File("/sys/devices/system/cpu/cpufreq/"+s+"/");
		    
		      if(gov.exists()){
		     File[] files = gov.listFiles();
		     
		     for (File file : files){
		      temp.add(file.getName());
		      fileList.add(file.getName());
		    
		     }
		   
		    
		     for(int i = 0; i < temp.size(); i++) {
		    	 	
		    	 try {
		    		
		    			File myFile = new File("/sys/devices/system/cpu/cpufreq/"+s+"/"+temp.get(i).trim());
		    			FileInputStream fIn = new FileInputStream(myFile);
		    			BufferedReader myReader = new BufferedReader(
		    					new InputStreamReader(fIn));
		    			String aDataRow = "";
		    			String aBuffer = "";
		    			while ((aDataRow = myReader.readLine()) != null) {
		    				aBuffer += aDataRow + "\n";
		    			}	    			
		    			
		    			
		    			myReader.close();
		    			
		    			entries.add(new GovEntry(temp.get(i), aBuffer));
		    			govValues.add(aBuffer);
		    			governors.add(s);
		    			
		    			
		    		} catch (Exception e) {
		    			
		    		}
		    	 
		     }
		     
		      }
		      
		      temp.clear();
		    
		     
	     }
	     
	    
	     return entries;
	    }
}
