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
package rs.pedjaapps.KernelTuner.helpers;


import android.content.*;
import android.view.*;
import android.widget.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.model.*;

public final class SDAdapter extends ArrayAdapter<SDScannerEntry>
{

	private final int sdItemLayoutResource;

	public SDAdapter(final Context context, final int sdItemLayoutResource)
	{
		super(context, 0);
		this.sdItemLayoutResource = sdItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final SDScannerEntry entry = getItem(position);

		viewHolder.nameView.setText(entry.getFileName());
		viewHolder.sizeView.setText(entry.getHRsize());
		if(entry.isFolder()==false){
			viewHolder.iconView.setImageResource(R.drawable.file);
		}
		else{
			viewHolder.iconView.setImageResource(R.drawable.folder);
		}
		

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

			workingView = inflater.inflate(sdItemLayoutResource, null);
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
			viewHolder.sizeView = (TextView)workingView.findViewById(R.id.size);
			viewHolder.iconView = (ImageView)workingView.findViewById(R.id.icon);
			

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
		public TextView sizeView;
		public ImageView iconView;

	}


}
