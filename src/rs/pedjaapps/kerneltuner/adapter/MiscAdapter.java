package rs.pedjaapps.kerneltuner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PinnedSectionListView;
import android.widget.TextView;

import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.Misc;
import rs.pedjaapps.kerneltuner.model.Misc;

public class MiscAdapter extends ArrayAdapter<Misc> implements PinnedSectionListView.PinnedSectionListAdapter
{
	LayoutInflater inflater;

	public MiscAdapter(Context context, List<Misc> data)
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
        return getItemViewType(position) == Misc.TYPE_ITEM && getItem(position).getItemType() != Misc.ITEM_TYPE_INFO;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
        Misc item = getItem(position);
		if(getItem(position).getType() == Misc.TYPE_HEADER)
		{

			if(convertView == null || convertView.getId() != R.id.tvHeader)
			{
				convertView = inflater.inflate(R.layout.list_item_cpu_header, parent, false);
			}
			((TextView)convertView).setText(item.getTitle());
		}
		else if(getItem(position).getType() == Misc.TYPE_ITEM)
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
				holder = (MiscAdapter.ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(item.getTitle());
			holder.tvValue.setText(item.getValue());
		}
		return convertView;
	}

    @Override
    public boolean isItemViewTypePinned(int viewType)
    {
        return viewType == Misc.TYPE_HEADER;
    }

    class ViewHolder
	{
		TextView tvTitle, tvValue;
	}
	
}
