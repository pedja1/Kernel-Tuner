package rs.pedjaapps.KernelTuner.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;
import org.apache.commons.io.FileUtils;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.tools.RootExecuter;
import rs.pedjaapps.KernelTuner.ui.TaskManager;
import com.actionbarsherlock.app.SherlockFragment;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.graphics.Color;


/**
 * A fragment representing a single process detail screen. This fragment is
 * either contained in a {@link processListActivity} in two-pane mode (on
 * tablets) or a {@link processDetailActivity} on handsets.
 */
public class TMDetailFragment extends SherlockFragment
{
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	//private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TMDetailFragment()
	{
	}


	Integer value;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID))
		{
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			/*mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			 ARG_ITEM_ID));*/

			try
			{
				value = Integer.parseInt(FileUtils.readFileToString(new File("/proc/" + TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid() + "/oom_adj")).trim());
			}
			catch (Exception e)
			{
				Log.e("", e.getMessage());
			}
		}
		getSherlockActivity().getSupportActionBar().setSubtitle("PID: "+TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid());
		getSherlockActivity().getSupportActionBar().setTitle(TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getName());
		getSherlockActivity().getSupportActionBar().setIcon(TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getIcon());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.tm_priority_layout,
										 container, false);

		// Show the dummy content as text in a TextView.
		/*if (mItem != null) {
		 ((TextView) rootView.findViewById(R.id.process_detail))
		 .setText(mItem.content);
		 }*/
		final TextView nice  = (TextView)rootView.findViewById(R.id.nice);
		SeekBar seekBar = (SeekBar)rootView.findViewById(R.id.seekBar);
		LinearLayout ll = (LinearLayout)rootView.findViewById(R.id.ll);

		if (value != null)
		{
			seekBar.setProgress(32 - (15 - value));
			Log.e("", 32 - (15 - value) + "");
			nice.setText(value + "");

				

			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

					@Override
					public void onProgressChanged(SeekBar arg0, int progress,
												  boolean fromUser)
					{
						nice.setText(progress - (32 - 15) + "");
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0)
					{

					}

					@Override
					public void onStopTrackingTouch(SeekBar arg0)
					{
						//String set = nice.getText().toString().trim();
						RootExecuter.exec(new String[]{"echo " + nice.getText().toString().trim() + " > /proc/" + TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid() + "/oom_adj"});	
					}

				});

		}
		else
		{
			ll.setVisibility(View.GONE);
			nice.setText("Something went wrong");
		}
		TextView p_name = (TextView)rootView.findViewById(R.id.p_name);
		TextView app_name = (TextView)rootView.findViewById(R.id.app_name);
		TextView pid = (TextView)rootView.findViewById(R.id.pid);
		TextView memory = (TextView)rootView.findViewById(R.id.memory);
		TextView status = (TextView)rootView.findViewById(R.id.status);
		
		app_name.setText(TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getName());
		pid.setText(""+TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid());
		memory.setText(Tools.kByteToHumanReadableSize(TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getRss()));
		try{
	    	p_name.setText(FileUtils.readFileToString(new File( "/proc/"+TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid()+"/cmdline")));
		}
		catch(Exception e){
		
		}
		try{
			String stat = FileUtils.readFileToString(new File( "/proc/"+TMListFragment.tmAdapter.getItem(getArguments().getInt(ARG_ITEM_ID)).getPid()+"/stat"));
			status.setText(Tools.getProcessStatus(stat.split("\\s")[2]));
		}
		catch(Exception e){

		}
		status.setTextColor(Color.parseColor("#FF9900"));
		return rootView;
	}
}
