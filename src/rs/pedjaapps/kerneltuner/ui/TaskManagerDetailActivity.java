package rs.pedjaapps.kerneltuner.ui;

import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import rs.pedjaapps.kerneltuner.model.*;
import rs.pedjaapps.kerneltuner.utility.*;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.root.*;

/**
 * An activity representing a single process detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link processListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link TMDetailFragment}.
 */
public class TaskManagerDetailActivity extends AbsActivity
{

	public static final String INTENT_EXTRA_TASK = "task";
	Task task;
	int value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tm_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		/*if (mItem != null) {
		 ((TextView) rootView.findViewById(R.id.process_detail))
		 .setText(mItem.content);
		 }*/
		 task = getIntent().getParcelableExtra(INTENT_EXTRA_TASK);
		 value = getOomAdj();

		final TextView nice  = (TextView)findViewById(R.id.nice);
		SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll);

		if (value != -1)
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
					public void onStopTrackingTouch(final SeekBar arg0)
					{
						//String set = nice.getText().toString().trim();
						new RootUtils().exec(new RootUtils.CommandCallbackImpl(){
							@Override
							public void onComplete(RootUtils.Status status, String out)
							{
								value = getOomAdj();
								arg0.setProgress(32 - (15 - value));
								
								nice.setText(value + "");
							}
						}, "echo " + nice.getText().toString().trim() + " > /proc/" + task.getPid() + "/oom_adj");
					}

				});

		}
		else
		{
			ll.setVisibility(View.GONE);
			nice.setText("Something went wrong");
		}
		TextView p_name = (TextView)findViewById(R.id.p_name);
		TextView app_name = (TextView)findViewById(R.id.app_name);
		TextView tvPid = (TextView)findViewById(R.id.pid);
		TextView memory = (TextView)findViewById(R.id.memory);
		TextView status = (TextView)findViewById(R.id.status);

		app_name.setText(task.getName());
		tvPid.setText(task.getPid() + "");
		memory.setText(Tools.kByteToHumanReadableSize(task.getRss()));
		try
		{
	    	p_name.setText(RCommand.readFileContent("/proc/" + task.getPid() + "/cmdline"));
		}
		catch (Exception e)
		{

		}
		try
		{
			String stat = RCommand.readFileContent("/proc/" + task.getPid() + "/stat");
			status.setText(Tools.getProcessStatus(stat.split("\\s")[2]));
		}
		catch (Exception e)
		{

		}
		status.setTextColor(Color.parseColor("#FF9900"));

	}

	private int getOomAdj()
	{
		try
		{
			return Tools.parseInt(RCommand.readFileContent("/proc/" + task.getPid() + "/oom_adj").trim(), -1);
		}
		catch (IOException e)
		{
			return -1;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:

				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
