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


import android.content.*;
import android.view.*;
import android.widget.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.model.*;

public final class ChangelogAdapter extends ArrayAdapter<ChangelogEntry>
{

	private final int govItemLayoutResource;

	public ChangelogAdapter(final Context context, final int govItemLayoutResource)
	{
		super(context, 0);
		this.govItemLayoutResource = govItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final ChangelogEntry entry = getItem(position);

		if(entry.isVersion()){
			viewHolder.versionView.setVisibility(View.VISIBLE);
			viewHolder.versionView.setText(entry.getVersionCode());
			viewHolder.changelogView.setVisibility(View.GONE);
			viewHolder.iconView.setVisibility(View.GONE);
		}
		else{
			viewHolder.versionView.setVisibility(View.GONE);
			viewHolder.changelogView.setText(entry.getChangelog());
			switch(entry.getType())
			{
				case 0:
					viewHolder.iconView.setImageResource(R.drawable.changelog_new);
					break;
				case 1:
					viewHolder.iconView.setImageResource(R.drawable.changelog_fixed);
					break;
				case 2:
					viewHolder.iconView.setImageResource(R.drawable.changelog_removed);
					break;
			}
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

			workingView = inflater.inflate(govItemLayoutResource, null);
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

			viewHolder.versionView = (TextView) workingView.findViewById(R.id.version);
			viewHolder.changelogView = (TextView) workingView.findViewById(R.id.changelog);
			viewHolder.iconView = (ImageView) workingView.findViewById(R.id.icon);

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
		public TextView versionView;
		public TextView changelogView;
		public ImageView iconView;

	}


}
