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

import android.app.*;
import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.*;

import java.io.*;
import java.util.*;

import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.utility.*;

import android.view.View.OnClickListener;

public final class VoltageAdapter extends ArrayAdapter<Voltage>
{
    public VoltageAdapter(final Context context)
    {
        super(context, 0);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {

        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Voltage voltage = getItem(position);      

        viewHolder.tvTitle.setText(voltage.getFreq());
		viewHolder.tvValue.setText(voltage.getName());
		
        return view;
    }

    private View getWorkingView(final View convertView)
    {
        View workingView = null;

        if (null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(R.layout.list_item_cpu, null);
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

            viewHolder.tvTitle = (TextView) workingView.findViewById(R.id.tvTitle);
			viewHolder.tvValue = (TextView) workingView.findViewById(R.id.tvValue);
			
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
        public TextView tvTitle, tvValue;

    }


}
