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
import rs.pedjaapps.KernelTuner.entry.*;

public final class TISAdapter extends ArrayAdapter<TISEntry>
{

	private final int tisItemLayoutResource;

	public TISAdapter(final Context context, final int tisItemLayoutResource)
	{
		super(context, 0);
		this.tisItemLayoutResource = tisItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final TISEntry entry = getItem(position);

		viewHolder.nameView.setText(entry.getName());
		viewHolder.timeView.setText(entry.getTime());
		viewHolder.percentView.setText(entry.getPercent());
		viewHolder.progressView.setProgress(entry.getProgress());

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

			workingView = inflater.inflate(tisItemLayoutResource, null);
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
			viewHolder.timeView = (TextView) workingView.findViewById(R.id.time);
			viewHolder.percentView = (TextView) workingView.findViewById(R.id.percent);
			viewHolder.progressView = (ProgressBar) workingView.findViewById(R.id.progress);

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
		public TextView timeView;
		public TextView percentView;
		public ProgressBar progressView;

	}


}
