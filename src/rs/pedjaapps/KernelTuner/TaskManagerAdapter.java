package rs.pedjaapps.KernelTuner;


import android.app.*;
import android.content.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.SeekBar.*;
import java.io.*;
import java.util.*;

import android.view.View.OnClickListener;
public final class TaskManagerAdapter extends ArrayAdapter<TaskManagerEntry>
{

	static ProgressDialog pd = null;
	private final int taskManagerItemLayoutResource;

	public TaskManagerAdapter(final Context context, final int taskManagerItemLayoutResource)
	{
		super(context, 0);
		this.taskManagerItemLayoutResource = taskManagerItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final TaskManagerEntry entry = getItem(position);
	

		viewHolder.nameView.setText(entry.getName());
		viewHolder.pidView.setText("pid: "+entry.getPid());
		if(entry.getStatus()==400){
			
		 viewHolder.statusView.setText("BACKGROUND APPLICATIONS");
		}
		else if(entry.getStatus()==500){
			viewHolder.statusView.setText("EMPTY APPLICATIONS");	
		}
		else if(entry.getStatus()==100){
			viewHolder.statusView.setText("FOREGROUND APPLICATIONS");	
		}
		else if(entry.getStatus()==130){
			viewHolder.statusView.setText("PERCEPTIBLE APPLICATIONS");
		}
		else if(entry.getStatus()==300){
			viewHolder.statusView.setText("SERVICE");
		}
		else if(entry.getStatus()==200){
			viewHolder.statusView.setText("VISIBLE APPLICATIONS");
		}
		viewHolder.iconView.setImageDrawable(entry.getIcon());
		 viewHolder.memoryView.setText(entry.getMemory());
		 viewHolder.cpuView.setText(entry.getCpu());

		return view;
	}

	private View getWorkingView(final View convertView)
	{
		View workingView = null;

		if (null == convertView)
		{
			final Context context = getContext();
			final LayoutInflater inflater = (LayoutInflater)context.getSystemService
			(Context.LAYOUT_INFLATER_SERVICE);

			workingView = inflater.inflate(taskManagerItemLayoutResource, null);
		}
		else
		{
			workingView = convertView;
		}

		return workingView;
	}

	private ViewHolder getViewHolder(final View workingView)
	{
		final Object tag = workingView.getTag();
		ViewHolder viewHolder = null;


		if (null == tag || !(tag instanceof ViewHolder))
		{
			viewHolder = new ViewHolder();

			viewHolder.iconView = (ImageView) workingView.findViewById(R.id.imageView1);
			viewHolder.nameView = (TextView)workingView.findViewById(R.id.processName);
			viewHolder.pidView = (TextView)workingView.findViewById(R.id.pid);
			viewHolder.statusView = (TextView)workingView.findViewById(R.id.processStatus);
			viewHolder.memoryView = (TextView)workingView.findViewById(R.id.memory);
			viewHolder.cpuView = (TextView)workingView.findViewById(R.id.usage);

			workingView.setTag(viewHolder);

		}
		else
		{
			viewHolder = (ViewHolder) tag;
		}

		return viewHolder;
	}

	private class ViewHolder
	{
		public ImageView iconView;
		public TextView nameView;
		public TextView pidView;
		public TextView statusView;
		public TextView memoryView;
		public TextView cpuView;

	}


}
