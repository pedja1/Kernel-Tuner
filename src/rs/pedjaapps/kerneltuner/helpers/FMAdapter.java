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


import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.FMEntry;
import android.content.*;
import android.view.*;
import android.widget.*;

public final class FMAdapter extends ArrayAdapter<FMEntry>
{

	private final int itemsItemLayoutResource;
    Context c;
	public FMAdapter(final Context context, final int itemsItemLayoutResource)
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
		final FMEntry entry = getItem(position);

		
			
		viewHolder.nameView.setText(entry.getName());
		viewHolder.dateView.setText(entry.getDate());
		viewHolder.sizeView.setText(entry.getSize());
		if(entry.getFolder()==1){
			viewHolder.imageView.setImageResource(R.drawable.folder);
		}
		else{
			viewHolder.imageView.setImageResource(R.drawable.file);
			
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
			viewHolder.dateView = (TextView) workingView.findViewById(R.id.date);
			viewHolder.sizeView = (TextView) workingView.findViewById(R.id.size);
			viewHolder.imageView = (ImageView) workingView.findViewById(R.id.image);
			
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
		public TextView dateView;
		public TextView sizeView;
		public ImageView imageView;
	
	}


}
