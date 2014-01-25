package rs.pedjaapps.KernelTuner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.model.MainOption;

/**
 * Created by pedja on 9/28/13.
 */
public class MenuAdapter extends ArrayAdapter<MainOption>
{
    List<MainOption> options;
    int resourceId;
    LayoutInflater inflater;
    public MenuAdapter(Context context, int resourceId, List<MainOption> objects)
    {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.options = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MainOption option = getItem(position);
        ViewHolder holder = null;

        if(convertView == null)
        {
            convertView = inflater.inflate(resourceId, null);

            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.itemIcon);
            holder.description = (TextView) convertView.findViewById(R.id.itemDescription);
            holder.title = (TextView) convertView.findViewById(R.id.itemTitle);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(option.getIcon());
        holder.description.setText(option.getDescription());
        holder.title.setText(option.getTitle());
        holder.description.setSelected(true);
        if(option.isSelected())
        {
            convertView.setBackgroundResource(R.drawable.list_activated_holo);
            holder.description.setTextColor(Color.WHITE);
            holder.title.setTextColor(Color.WHITE);
        }
        else
        {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.description.setTextColor(getContext().getResources().getColor(R.color.gray_text));
            holder.title.setTextColor(getContext().getResources().getColor(R.color.app_color));
        }

        return convertView;
    }

    class ViewHolder
    {
        ImageView icon;
        TextView title, description;
    }

    public void setSelectedItem(int position)
    {
        for(int i = 0; i < options.size(); i++)
        {
            options.get(i).setSelected(position == i);
        }
        notifyDataSetChanged();
    }
}