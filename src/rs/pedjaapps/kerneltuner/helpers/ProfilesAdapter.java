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

public final class ProfilesAdapter extends ArrayAdapter<ProfilesEntry>
{

	private final int profilesItemLayoutResource;

	public ProfilesAdapter(final Context context, final int profilesItemLayoutResource)
	{
		super(context, 0);
		this.profilesItemLayoutResource = profilesItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final ProfilesEntry entry = getItem(position);

		 int check = entry.getCheck();
		
		 if (check==1){
			 
			 viewHolder.checkView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.checkView.setVisibility(View.GONE);
		 }
	
		viewHolder.nameView.setText(entry.getName());
		

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

			workingView = inflater.inflate(profilesItemLayoutResource, null);
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

			viewHolder.nameView = (TextView) workingView.findViewById(R.id.profile_name);
			viewHolder.checkView = (ImageView)workingView.findViewById(R.id.check);
		

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
		public ImageView checkView;
	

	}


}
