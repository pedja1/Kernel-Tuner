package rs.pedjaapps.kerneltuner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PinnedSectionListView;
import android.widget.TextView;

import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.SystemInfo;
import rs.pedjaapps.kerneltuner.model.SystemInfo;

public class SystemInfoAdapter extends ArrayAdapter<SystemInfo>
{
	LayoutInflater inflater;

	public SystemInfoAdapter(Context context, List<SystemInfo> data)
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
        return getItemViewType(position) == SystemInfo.TYPE_ITEM;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SystemInfo item = getItem(position);
		if(getItem(position).getType() == SystemInfo.TYPE_HEADER)
		{

			if(convertView == null || convertView.getId() != R.id.tvHeader)
			{
				convertView = inflater.inflate(R.layout.list_item_cpu_header, parent, false);
			}
			((TextView)convertView).setText(item.getTitle());
		}
		else if(getItem(position).getType() == SystemInfo.TYPE_ITEM)
		{
			ViewHolder holder;
			if(convertView == null   || convertView.getId() != R.id.llContainer)
			{
				convertView = inflater.inflate(R.layout.list_item_system_info, parent, false);
				holder = new ViewHolder();
				holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
				holder.tvValue = (TextView)convertView.findViewById(R.id.tvValue);
                holder.ivIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
				convertView.setTag(holder);
			}
			else
			{
				holder = (SystemInfoAdapter.ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(item.getTitle());
			holder.tvValue.setText(item.getValue());
            holder.ivIcon.setImageResource(item.getIcon());
		}
		return convertView;
	}

    class ViewHolder
	{
		TextView tvTitle, tvValue;
        ImageView ivIcon;
	}
	
}
