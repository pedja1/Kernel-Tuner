package rs.pedjaapps.DualCore;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import rs.pedjaapps.DualCore.OC.NewsEntry;
import rs.pedjaapps.DualCore.OC.NewsEntryAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class OC_single extends Activity{

	public String freqscpu0;
	public String freqscpu1;
	public String curentminfreqcpu0;
	public String curentmaxfreqcpu0;
	public String curentminfreqcpu1;
	public String curentmaxfreqcpu1;
	public String governors;
	public String governorscpu1;
	public String curentgovernorcpu0;
	public String curentgovernorcpu1;
	public String minselected;
	public String maxselected;
	public String govselected;
	private ProgressDialog pd = null;
    private Object data = null;
    List<String> frequencies = new ArrayList<String>();
	public String[] delims;
    public SharedPreferences preferences;
    
    public String[] filesx;
    List<String> fileList;
    List<String> govvalues;
    public String newvalue;
    public String curfile;
    	NewsEntryAdapter newsEntryAdapter ;
    	ListView newsEntryListView;
    	public String governor;
    	List<String> frequencies2 = new ArrayList<String>();

    	private class applygovsettings extends AsyncTask<String, Void, Object> {


    		protected Object doInBackground(String... args) {
    			Log.i("MyApp", "Background thread starting");

    			

    				

    				Process localProcess;
    				try {
    					localProcess = Runtime.getRuntime().exec("su");

    					DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
    				
    					localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/"+governor+"/" + curfile.trim() + "\n");
    					localDataOutputStream.writeBytes("echo " + newvalue.trim() +" > /sys/devices/system/cpu/cpufreq/"+governor+"/"+ curfile.trim() +"\n");
    				

    					localDataOutputStream.writeBytes("exit\n");
    					localDataOutputStream.flush();
    					localDataOutputStream.close();
    					localProcess.waitFor();
    					localProcess.destroy();
    				
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				} catch (InterruptedException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    		
    			

    			return "";
    		}

    		
    		
    		protected void onPostExecute(Object result) {
    			// Pass the result data back to the main activity
    			getNewsEntries();

    			newsEntryAdapter.clear();
    			for(final NewsEntry entry : getNewsEntries()) {
    				newsEntryAdapter.add(entry);
    			}
    			newsEntryAdapter.notifyDataSetChanged();
    			newsEntryListView.invalidate();
    			OC_single.this.data = result;

    			

    		}

    	}	
    	
    	
    private class ReadCPU0freq extends AsyncTask<String, Void, Object> {
    	  	
    	protected Object doInBackground(String... args) {
             Log.i("MyApp", "Background thread starting");
             
             String aBuffer = "";
             // This is where you would do all the work of downloading your data
             try {
     			
     			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
     			FileInputStream fIn = new FileInputStream(myFile);     			
     			
     			BufferedReader myReader = new BufferedReader(
     					new InputStreamReader(fIn));
     			String aDataRow = "";
     			//String aBuffer = "";
     			while ((aDataRow = myReader.readLine()) != null) {
     				aBuffer += aDataRow + "\n";
     			}

     			freqscpu0 = aBuffer;
     			myReader.close();
     			    			
     		} catch (Exception e) {
     			    			
     		}
             
             return aBuffer;
         }

         protected void onPostExecute(Object result) {
             // Pass the result data back to the main activity
        	 
             OC_single.this.data = result;
            
         }
		 }
    
    private class ReadCPU0freqAlt extends AsyncTask<String, Void, Object> {
      	
    	protected Object doInBackground(String... args) {
             Log.i("MyApp", "Background thread starting");
             
             
             // This is where you would do all the work of downloading your data
             try{
     			// Open the file that is the first 
     			// command line parameter
     			FileInputStream fstream = new FileInputStream("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
     			// Get the object of DataInputStream
     			DataInputStream in = new DataInputStream(fstream);
     			BufferedReader br = new BufferedReader(new InputStreamReader(in));
     			String strLine;
     			//Read File Line By Line
     			
     			while ((strLine = br.readLine()) != null)   {
     				
     				delims = strLine.split(" ");
     				String freq = delims[0];
     				//freq= 	freq.substring(0, freq.length()-3)+"Mhz";

     				frequencies.add(freq);

     			}
     			String[] strarray = frequencies.toArray(new String[0]);
     			StringBuilder builder = new StringBuilder();
     			for(String s : strarray) {
     			    builder.append(s);
     			    builder.append(" ");
     			}
     			freqscpu0 = builder.toString();


     			
     			in.close();
     		}catch (Exception e){
     			System.err.println("Error: " + e.getMessage());
     		}
             
             return "";
         }

         protected void onPostExecute(Object result) {
             // Pass the result data back to the main activity       	 
             OC_single.this.data = result;             
         }    	
    	}
    
private class spinnerMaxCpu0 extends AsyncTask<String, Void, Object> {
    	
        protected Object doInBackground(String... args) {
            Log.i("MyApp", "Background thread starting");

            // This is where you would do all the work of downloading your data            
            try {
    			
    			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
    			FileInputStream fIn = new FileInputStream(myFile);   			
    			BufferedReader myReader = new BufferedReader(
    					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null) {
    				aBuffer += aDataRow + "\n";
    			}

    			curentmaxfreqcpu0 = aBuffer.trim();
    			myReader.close();
    			
    		} catch (Exception e) {
     		}
    		
            return "replace this with your data object";
        }

        protected void onPostExecute(Object result) {
            // Pass the result data back to the main activity
       	 spinnermaxcpu0();
            OC_single.this.data = result;

            
        }
    	}
    
private class spinnerMinCpu0 extends AsyncTask<String, Void, Object> {
    	
        protected Object doInBackground(String... args) {
            Log.i("MyApp", "Background thread starting");

            // This is where you would do all the work of downloading your data
            
try {
    			
    			File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq");
    			FileInputStream fIn = new FileInputStream(myFile);
    			BufferedReader myReader = new BufferedReader(
    					new InputStreamReader(fIn));
    			String aDataRow = "";
    			String aBuffer = "";
    			while ((aDataRow = myReader.readLine()) != null) {
    				aBuffer += aDataRow + "\n";
    			}

    			curentminfreqcpu0 = aBuffer.trim();
    			myReader.close();
  			
    		} catch (Exception e) {
  		}
            return "replace this with your data object";
        }

        protected void onPostExecute(Object result) {
            // Pass the result data back to the main activity
        	spinnermincpu0();
            OC_single.this.data = result;
        }
    	}
    
private class ReadgovernorsCpu0 extends AsyncTask<String, Void, Object> {
    		String aBuffer = "";
            protected Object doInBackground(String... args) {
                Log.i("MyApp", "Background thread starting");

                // This is where you would do all the work of downloading your data
                try {
            		
            		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors");
            		FileInputStream fIn = new FileInputStream(myFile);
            		BufferedReader myReader = new BufferedReader(
            				new InputStreamReader(fIn));
            		String aDataRow = "";
            		
            		while ((aDataRow = myReader.readLine()) != null) {
            			aBuffer += aDataRow + "\n";
            		}

            		governors = aBuffer;
            		myReader.close();
            		         		
            	} catch (Exception e) {
           	}
            	
                return aBuffer;
            }

            protected void onPostExecute(Object result) {
                // Pass the result data back to the main activity
            	
                OC_single.this.data = result;
            }

        	}
    	
    	private class govspinnercpu0 extends AsyncTask<String, Void, Object> {
    		String aBuffer = "";
            protected Object doInBackground(String... args) {
                Log.i("MyApp", "Background thread starting");

                // This is where you would do all the work of downloading your data
                
                try {
            		
            		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
            		FileInputStream fIn = new FileInputStream(myFile);
            		BufferedReader myReader = new BufferedReader(
            				new InputStreamReader(fIn));
            		String aDataRow = "";
            		String aBuffer = "";
            		while ((aDataRow = myReader.readLine()) != null) {
            			aBuffer += aDataRow + "\n";
            		}

            		curentgovernorcpu0 = aBuffer.trim();
            		myReader.close();
            		
            	} catch (Exception e) {
            		
            	}
                
                return aBuffer;
            }

            protected void onPostExecute(Object result) {
                // Pass the result data back to the main activity
            	createspinnerforcpu0();
                OC_single.this.data = result;

                if (OC_single.this.pd != null) {
                    OC_single.this.pd.dismiss();
                }
            }

        	}
    	
    	private class apply extends AsyncTask<String, Void, Object> {
        	
        	
        	protected Object doInBackground(String... args) {
                 Log.i("MyApp", "Background thread starting");
                
                 Process localProcess;
          		try {
     				localProcess = Runtime.getRuntime().exec("su");
     			
          		DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
                 localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
                 localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
                 localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
                 localDataOutputStream.writeBytes("echo \"" + govselected + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n");
                 localDataOutputStream.writeBytes("echo \"" + minselected + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n");
                 localDataOutputStream.writeBytes("echo \"" + maxselected + "\" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n");
                 
                 
                 localDataOutputStream.writeBytes("exit\n");
                 localDataOutputStream.flush();
                 localDataOutputStream.close();
                 localProcess.waitFor();
                 localProcess.destroy();
          		} catch (IOException e1) {
     				// TODO Auto-generated catch block
     				e1.printStackTrace();
     			} catch (InterruptedException e1) {
     				// TODO Auto-generated catch block
     				e1.printStackTrace();
     			}
          	           
                 return "";
             }

             protected void onPostExecute(Object result) {
            	 preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            	 SharedPreferences.Editor editor = preferences.edit();
            	    editor.putString("cpu0min", minselected);
            	    editor.putString("cpu0max", maxselected);
            	    editor.putString("cpu0gov", govselected);// value to store
            	    editor.commit();
            	 
                 OC_single.this.data = result;
                 OC_single.this.pd.dismiss();
                 OC_single.this.finish();
             }
        	}
    	
    	
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.oc_single);
	readgovernor();
	File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
	File file2 = new File("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
	try{
	
	InputStream fIn = new FileInputStream(file);
	
	this.pd = ProgressDialog.show(this, "Working...", "Loading...", true, false);

    // Start a new thread that will download all the data
    new ReadCPU0freq().execute();
    new spinnerMinCpu0().execute();
    new spinnerMaxCpu0().execute();
    new ReadgovernorsCpu0().execute();
    new govspinnercpu0().execute();
	}

	catch(FileNotFoundException e){
		try{
			InputStream fIn = new FileInputStream(file2);
			
			this.pd = ProgressDialog.show(this, "Working..", "Loading...", true, false);

		    // Start a new thread that will download all the data
		    new ReadCPU0freqAlt().execute();
		    new spinnerMinCpu0().execute();
		    new spinnerMaxCpu0().execute();
		    new ReadgovernorsCpu0().execute();
		    new govspinnercpu0().execute();
		}
		catch(FileNotFoundException e2){
			AlertDialog alertDialog = new AlertDialog.Builder(
		            OC_single.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Available Frequency File Not Found");

		// Setting Dialog Message
		alertDialog.setMessage("We are sorry but your phone doesnt support overclocking");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.icon);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    // Write your code here to execute after dialog closed
		    	finish();
		    }
		});

		// Showing Alert Message
		alertDialog.show();
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}
		
		
	}
	
	 newsEntryListView = (ListView) findViewById(R.id.list);
     newsEntryAdapter = new NewsEntryAdapter(this, R.layout.governor_list_item);
    newsEntryListView.setAdapter(newsEntryAdapter);
    
    // Populate the list, through the adapter
    for(final NewsEntry entry : getNewsEntries()) {
     newsEntryAdapter.add(entry);
    }
	 
	 
     // 	ListView lv = (ListView)findViewById(R.id.list);
		newsEntryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
			{
				// When clicked, show a toast with the TextView text 
			Toast.makeText(getApplicationContext(),
			("Position " + position), Toast.LENGTH_SHORT).show();
		//	Context context = null;
			String[] valuess = govvalues.toArray(new String[0]);
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

				builder.setTitle(filesx[position]);

				builder.setMessage("Set new value: ");

				builder.setIcon(R.drawable.icon);

				
			
				//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//final View layout = inflater.inflate(R.layout.gov_edit, null);
              final EditText input = new EditText(view.getContext());//)layout.findViewById(R.id.editText2);
				
              input.setHint(valuess[position]);
				input.selectAll();
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setGravity(Gravity.CENTER_HORIZONTAL);
				//input.selectAll();
				builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						newvalue = String.valueOf(input.getText());
						curfile = filesx[position];
						Log.d("new value", newvalue);
						Log.d("curent file", curfile);
						new applygovsettings().execute();
					 
					}
				});
              builder.setView(input);
				
				AlertDialog alert = builder.create();

				alert.show();
				
			
			} 
			});
	// Show the ProgressDialog on this thread
   
    
	  /*Button apply = (Button)findViewById(R.id.button2);
		apply.setOnClickListener(new OnClickListener(){
			
			
			public void onClick(View v) {
				OC_single.this.pd = ProgressDialog.show(OC_single.this, "Working..", "Applying settings", true, false);
				new apply().execute();
				
				
			    // Start a new thread that will download all the dat
			}
		});
		
		Button cancel = (Button)findViewById(R.id.button1);
		cancel.setOnClickListener(new OnClickListener(){
			
			
			public void onClick(View v) {
				OC_single.this.finish();
				
			    // Start a new thread that will download all the data
			}
		});*/
    	}

	public void readgovernor(){
		
	    
		governor = null;
		try {
    		
    		File myFile = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
    		FileInputStream fIn = new FileInputStream(myFile);
		
    		BufferedReader myReader = new BufferedReader(
    				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		while ((aDataRow = myReader.readLine()) != null) {
    			aBuffer += aDataRow + "\n";
    		}

    		governor = aBuffer.trim();
    		myReader.close();
    	           		           		
    	} catch (Exception e) {
    	}
		

	}
	
    public void createspinnerforcpu0(){
    	String[] MyStringAray = governors.split("\\s");
    	
    	Spinner spinner = (Spinner) findViewById(R.id.spinner1);
    	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, MyStringAray);
    	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
    	spinner.setAdapter(spinnerArrayAdapter);
    	
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	    	govselected = parent.getItemAtPosition(pos).toString();
    	    	
    	    }

    	    
    	    public void onNothingSelected(AdapterView<?> parent) {
    	        //do nothing
    	    }
    	});

    	ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

    	int spinnerPosition = myAdap.getPosition(curentgovernorcpu0);

    	//set the default according to value
    	spinner.setSelection(spinnerPosition);
    	//
    	
    }

    public void spinnermincpu0(){
		String[] MyStringAray = freqscpu0.split("\\s");
		//String MyStringAray[] = {"1","2",};
		Spinner spinner = (Spinner) findViewById(R.id.spinner3);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(OC_single.this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	minselected = parent.getItemAtPosition(pos).toString();
		    	minselected.trim();
		    }

		    
		    public void onNothingSelected(AdapterView<?> parent) {
		        //do nothing
		    }
		});
		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(curentminfreqcpu0);

		//set the default according to value
		spinner.setSelection(spinnerPosition);

	}
	
	public void spinnermaxcpu0(){
    	String[] MyStringAray = freqscpu0.split("\\s");
		Spinner spinner = (Spinner) findViewById(R.id.spinner4);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(OC_single.this,   android.R.layout.simple_spinner_item, MyStringAray);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	maxselected = parent.getItemAtPosition(pos).toString();
		    	maxselected.trim();

		    }
		    
		    public void onNothingSelected(AdapterView<?> parent) {
		        //do nothing
		    }
		});
		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

		int spinnerPosition = myAdap.getPosition(curentmaxfreqcpu0);

		//set the default according to value
		spinner.setSelection(spinnerPosition);
    }
	
	public final class NewsEntryAdapter extends ArrayAdapter<NewsEntry> {

		private final int newsItemLayoutResource;

		public NewsEntryAdapter(final Context context, final int newsItemLayoutResource) {
		super(context, 0);
		this.newsItemLayoutResource = newsItemLayoutResource;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {

		// We need to get the best view (re-used if possible) and then
		// retrieve its corresponding ViewHolder, which optimizes lookup efficiency
		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final NewsEntry entry = getItem(position);

		// Setting the title view is straightforward
		viewHolder.titleView.setText(entry.getTitle());

		// Setting the subTitle view requires a tiny bit of formatting
		/*final String formattedSubTitle = String.format("By %s",
		entry.getAuthor());
		*/

		viewHolder.subTitleView.setText(entry.getAuthor());


		return view;
		}

		private View getWorkingView(final View convertView) {
		// The workingView is basically just the convertView re-used if possible
		// or inflated new if not possible
		View workingView = null;

		if(null == convertView) {
		final Context context = getContext();
		final LayoutInflater inflater = (LayoutInflater)context.getSystemService
		(Context.LAYOUT_INFLATER_SERVICE);

		workingView = inflater.inflate(newsItemLayoutResource, null);
		} else {
		workingView = convertView;
		}

		return workingView;
		}

		private ViewHolder getViewHolder(final View workingView) {
		// The viewHolder allows us to avoid re-looking up view references
		// Since views are recycled, these references will never change
		final Object tag = workingView.getTag();
		ViewHolder viewHolder = null;


		if(null == tag || !(tag instanceof ViewHolder)) {
		viewHolder = new ViewHolder();

		viewHolder.titleView = (TextView) workingView.findViewById(R.id.news_entry_title);
		viewHolder.subTitleView = (TextView) workingView.findViewById(R.id.news_entry_subtitle);
		
		workingView.setTag(viewHolder);

		} else {
		viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
		}

		/**
		* ViewHolder allows us to avoid re-looking up view references
		* Since views are recycled, these references will never change
		*/
		private class ViewHolder {
		public TextView titleView;
		public TextView subTitleView;
		
		}


		}
	
	public final class NewsEntry {

		private final String title;
		private final String author;
		
		

		public NewsEntry(final String title, final String author) {
		this.title = title;
		this.author = author;
		
		
		}

		/**
		* @return Title of news entry
		*/
		public String getTitle() {
		return title;
		}

		/**
		* @return Author of news entry
		*/
		public String getAuthor() {
		return author;
		}

		}
	
	

	private List<NewsEntry> getNewsEntries() {
	    
	     // Let's setup some test data.
	     // Normally this would come from some asynchronous fetch into a data source
	     // such as a sqlite database, or an HTTP request
	    
		
	     final List<NewsEntry> entries = new ArrayList<NewsEntry>();
	     File gov = new File("/sys/devices/system/cpu/cpufreq/"+governor+"/");
	     fileList = new ArrayList<String>();
	     govvalues = new ArrayList<String>();
	     //Log.d("governor", curentgovernorcpu0);
	     //System.out.println(governor);
	      if(gov.exists()){ // ListDir(gov);
	     File[] files = gov.listFiles();
	     
	     for (File file : files){
	      fileList.add(file.getName());
	     // Log.d("List files",file.getName());
	     }
	     filesx = fileList.toArray(new String[0]);
	     
	     for(int i = 0; i < filesx.length; i++) {
	    	 //Log.d("List files",filesx[i]);
	    	 try {
	    			
	    			File myFile = new File("/sys/devices/system/cpu/cpufreq/"+governor+"/"+filesx[i].trim());
	    			FileInputStream fIn = new FileInputStream(myFile);
	    			BufferedReader myReader = new BufferedReader(
	    					new InputStreamReader(fIn));
	    			String aDataRow = "";
	    			String aBuffer = "";
	    			while ((aDataRow = myReader.readLine()) != null) {
	    				aBuffer += aDataRow + "\n";
	    			}	    			
	    			//cpu1max = aBuffer;
	    			Log.d("values",aBuffer);
	    			
	    			myReader.close();
	    			
	    			entries.add(new NewsEntry(filesx[i], aBuffer));
	    			govvalues.add(aBuffer);
	    			Log.d(filesx[i], aBuffer);
	    			
	    		} catch (Exception e) {
	    			//entries.add(new NewsEntry(null, null));
	    			Log.d("catch statement","catche");
	    		}
	    	 
	     }
	     
	     System.out.println(govvalues);
	    
	     
	      }
	     return entries;
	    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(Menu.NONE, 0, 0, "Apply").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    menu.add(Menu.NONE, 1, 1, "Cancel").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    return super.onCreateOptionsMenu(menu);
	}

	public boolean onPrepareOptionsMenu (Menu menu) {
	    
	    return true;
	}


	


	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case 0:
	        	OC_single.this.pd = ProgressDialog.show(OC_single.this, "Working..", "Applying settings", true, false);
				new apply().execute();
				//OC_single.this.finish();
				return true;
	        case 1:
	        	
	        						OC_single.this.finish();
				return true;
			
	           
	    }
	    return false;
		
	}
	
}
