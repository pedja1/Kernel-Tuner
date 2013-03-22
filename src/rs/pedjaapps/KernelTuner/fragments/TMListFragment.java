package rs.pedjaapps.KernelTuner.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.TMEntry;
import rs.pedjaapps.KernelTuner.helpers.TMAdapter;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;


/**
 * A list fragment representing a list of processes. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link TMDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TMListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TMListFragment() {
	}

	ListView tmListView;
	TMAdapter tmAdapter;
	static Drawable dr;
	List<TMEntry> entries;
	ProgressDialog pd;
	PackageManager pm;
	String set;
	CheckBox system, user, other;
	SharedPreferences preferences;
	ProgressBar loading;
	String arch = "arm";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: replace with a real list adapter.
		/*setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, DummyContent.ITEMS));*/
		pm = getActivity().getPackageManager();
		 preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		final boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{
			AdView adView = (AdView)getActivity().findViewById(R.id.ad);
			adView.loadAd(new AdRequest());
		}
		
		/*loading = (ProgressBar)getActivity().findViewById(R.id.loading);
		system = (CheckBox)getActivity().findViewById(R.id.system);
		user = (CheckBox)getActivity().findViewById(R.id.user);
		other = (CheckBox)getActivity().findViewById(R.id.other);
	
		system.setChecked(preferences.getBoolean("tm_system", false));
		user.setChecked(preferences.getBoolean("tm_user", true));
		other.setChecked(preferences.getBoolean("tm_other", false));
	
		system.setOnCheckedChangeListener(new Listener());
		user.setOnCheckedChangeListener(new Listener());
		other.setOnCheckedChangeListener(new Listener());*/
	
		
		//tmListView = (ListView) getActivity().findViewById(R.id.list);
		tmAdapter = new TMAdapter(getActivity(), R.layout.tm_row);
		setListAdapter(tmAdapter);
		tmAdapter.add(new TMEntry(arch, mActivatedPosition, null, mActivatedPosition, mActivatedPosition));
		tmAdapter.notifyDataSetChanged();
		/*tmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int pos,
					long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setTitle("Change Process Priority");
				Integer value = null;
				try {
					value = Integer.parseInt(FileUtils.readFileToString(new File("/proc/"+tmAdapter.getItem(pos).getPid()+"/oom_adj")).trim());
				} catch (Exception e) {
					Log.e("", e.getMessage());
				}
				LayoutInflater inflater = (LayoutInflater) TaskManager.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.tm_priority_layout, null);
				final TextView nice  = (TextView)view.findViewById(R.id.nice);
				SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
				LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
				
				if(value!=null){
					seekBar.setProgress(32-(15-value));
					Log.e("", 32-(15-value)+"");
					nice.setText(value+"");
					
					builder.setPositiveButton("Set", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							RootExecuter.exec(new String[]{"echo " + nice.getText().toString().trim() + " > /proc/"+tmAdapter.getItem(pos).getPid()+"/oom_adj"});		
						}
						
					});
					seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

						@Override
						public void onProgressChanged(SeekBar arg0, int progress,
								boolean fromUser) {
						
							nice.setText(progress-(32-15)+"");
							
						}

						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
							
						}

						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
							set = nice.getText().toString().trim();
							System.out.println(set);
						}
						
					});
					
				}
				else{
					ll.setVisibility(View.GONE);
					nice.setText("Something went wrong");
					
				}
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						
					}
					
				});
				
				

				builder.setView(view);
				AlertDialog alert = builder.create();
				alert.show();
				
			}
		});*/
			//new GetRunningApps().execute();
			arch = Tools.getAbi();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(tmAdapter.getItemId(position)+"");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	private class Listener implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			new GetRunningApps().execute();
		}

	}
	
	private class GetRunningApps extends AsyncTask<String, /*TMEntry*/Void, Void> {
		String line;
		@Override
		protected Void doInBackground(String... args) {
			entries = new ArrayList<TMEntry>();
			Process proc = null;
			try
			{
				proc = Runtime.getRuntime().exec(getActivity().getFilesDir().getPath()+"/ps-"+arch+"\n");
				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				int i = 0;
				while ( ( line = bufferedReader.readLine() ) != null )
				{
					line = line.trim().replaceAll(" {1,}", " ");
					String[] temp = line.split("\\s");
					List<String> tmp = Arrays.asList(temp);
					if(i>0){
						if(!tmp.get(4).equals("0")){
					TMEntry tmpEntry = new TMEntry(getApplicationName(tmp.get(8)), Integer.parseInt(tmp.get(1)), getApplicationIcon(tmp.get(8)), Integer.parseInt(tmp.get(4)), appType(tmp.get(8)));
					entries.add(tmpEntry);
					//publishProgress(tmpEntry);
					}
					}
					else{
						i++;
					}
					
				}
			}
			catch (Exception e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}

	/*	@Override
		protected void onProgressUpdate(TMEntry... values)
		{
			if(values[0].getType()==2){
				if(other.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			else if(values[0].getType()==1){
				if(user.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			else if(values[0].getType()==0){
				if(system.isChecked())
				{
		        	tmAdapter.add(values[0]);
			        tmAdapter.notifyDataSetChanged();
			    }
			}
			super.onProgressUpdate();
		}*/

		@Override
		protected void onPostExecute(Void res) {
		//	setProgressBarIndeterminateVisibility(false);
			
			Collections.sort(entries, new SortByMb());
			
			for(TMEntry e : entries){
				if(e.getType()==2){
					if(other.isChecked())
					{
						tmAdapter.add(e);
					}
				}
				else if(e.getType()==1){
					if(user.isChecked())
					{
						tmAdapter.add(e);
					}
				}
				else if(e.getType()==0){
					if(system.isChecked())
					{
						tmAdapter.add(e);
					}
				}
				loading.setVisibility(View.GONE);
			}
			tmAdapter.notifyDataSetChanged();

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("tm_system", system.isChecked())
				.putBoolean("tm_user", user.isChecked())
				.putBoolean("tm_other",other.isChecked())
				.apply();
		}
		@Override
		protected void onPreExecute(){
			//setProgressBarIndeterminateVisibility(true);
			tmAdapter.clear();
			loading.setVisibility(View.VISIBLE);
		}

	}
	
	public Drawable getApplicationIcon(String packageName){
		try
		{
			return pm.getApplicationIcon(packageName);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return getResources().getDrawable(R.drawable.apk);
		}
	}
	public String getApplicationName(String packageName){
		try
		{
			return (String)pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return packageName;
		}
	}
	
	/**
	* @return 0 if system, 1 if user, 2 if unknown
	*/
	public int appType(String packageName) {
		try{
		ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
		int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
	    if((ai.flags & mask) == 0){
			return 1;
		}
		else{
     	    return 0;
		}
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return 2;
		}
	}
	
	class SortByMb implements Comparator<TMEntry>
	{
		  @Override
		  public int compare(TMEntry ob1, TMEntry ob2)
		  {
		   return ob2.getRss() - ob1.getRss() ;
		  }
	}
		
	static class SortByName implements Comparator<TMEntry>
	{
		@Override
		public int compare(TMEntry s1, TMEntry s2)
		{
		    String sub1 = s1.getName();
		    String sub2 = s2.getName();
		    return sub2.compareTo(sub1);
		} 
	}
	
	class SortByType implements Comparator<TMEntry>
	{
		@Override
		public int compare(TMEntry ob1, TMEntry ob2)
		{
			return ob1.getType() - ob2.getType() ;
		}
	}
}
