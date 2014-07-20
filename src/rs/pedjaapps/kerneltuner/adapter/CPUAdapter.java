package rs.pedjaapps.kerneltuner.adapter;

import android.content.*;
import android.widget.*;
import java.util.*;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.*;
import android.view.*;

public class CPUAdapter extends ArrayAdapter<CPU> implements PinnedSectionListView.PinnedSectionListAdapter
{
	LayoutInflater inflater;
	
	public CPUAdapter(Context context, List<CPU> data)
	{
		super(context, 0, data);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemViewType(int position)
	{
		return getItem(position).getType();
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

    @Override
    public boolean isEnabled(int position)
    {
        return getItemViewType(position) == CPU.TYPE_ITEM && getItem(position).getItemType() != CPU.ITEM_TYPE_INFO;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		CPU item = getItem(position);
		if(getItem(position).getType() == CPU.TYPE_HEADER)
		{
		    
			if(convertView == null || convertView.getId() != R.id.tvHeader)
			{
				convertView = inflater.inflate(R.layout.list_item_cpu_header, parent, false);
			}
			((TextView)convertView).setText(item.getTitle());
		}
		else if(getItem(position).getType() == CPU.TYPE_ITEM)
		{
			ViewHolder holder;
			if(convertView == null   || convertView.getId() != R.id.llContainer)
			{
				convertView = inflater.inflate(R.layout.list_item_cpu, parent, false);
				holder = new ViewHolder();
				holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
				holder.tvValue = (TextView)convertView.findViewById(R.id.tvValue);
				convertView.setTag(holder);
			}
			else
			{
				holder = (CPUAdapter.ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(item.getTitle());
			holder.tvValue.setText(item.getValue());
		}
		return convertView;
	}

    @Override
    public boolean isItemViewTypePinned(int viewType)
    {
        return viewType == CPU.TYPE_HEADER;
    }

    class ViewHolder
	{
		TextView tvTitle, tvValue;
	}
	
}
