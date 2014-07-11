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
package rs.pedjaapps.kerneltuner.helpers;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.TMEntry;
import rs.pedjaapps.kerneltuner.utility.Tools;

public final class TMAdapter extends ArrayAdapter<TMEntry>
{

	private final int itemsItemLayoutResource;
    Context c;
	public TMAdapter(final Context context, final int itemsItemLayoutResource)
	{
		
		super(context, 0);
		this.itemsItemLayoutResource = itemsItemLayoutResource;
		this.c = context;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final TMEntry entry = getItem(position);
		viewHolder.imageView.setImageDrawable(entry.getIcon());
		viewHolder.nameView.setText(entry.getName());
		switch(entry.getType()){
			case 0:
			    viewHolder.nameView.setTextColor(Color.parseColor("#cc3300"));
			    break;
			case 1:
			    viewHolder.nameView.setTextColor(Color.parseColor("#6699ff"));
			    break;
			case 2:
			    viewHolder.nameView.setTextColor(Color.parseColor("#ff9900"));
				viewHolder.imageView.setImageResource(R.drawable.non_app);
			    break;
		}
		viewHolder.mbView.setText(Tools.kByteToHumanReadableSize(entry.getRss()));
		viewHolder.pidView.setText("PID: "+entry.getPid());
		
		viewHolder.killView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CommandCapture command = new CommandCapture(0, "kill "+entry.getPid());
				try{
					RootTools.getShell(true).add(command);
				}
				catch(Exception e){

				}
				remove(entry);
				
			}
		});
		
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

			workingView = inflater.inflate(itemsItemLayoutResource, null);
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

			viewHolder.nameView = (TextView) workingView.findViewById(R.id.name);
			viewHolder.mbView = (TextView) workingView.findViewById(R.id.mb);
			viewHolder.imageView = (ImageView) workingView.findViewById(R.id.icon);
			viewHolder.killView = (Button) workingView.findViewById(R.id.kill);
			viewHolder.pidView = (TextView) workingView.findViewById(R.id.pid);
			
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
		public TextView nameView;
		public TextView mbView;
		public ImageView imageView;
		public Button killView;
		public TextView pidView;
	
	}


}
