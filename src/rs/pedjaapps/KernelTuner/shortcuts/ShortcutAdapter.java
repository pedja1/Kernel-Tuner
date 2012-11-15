package rs.pedjaapps.KernelTuner.shortcuts;


import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.R.id;
import android.content.*;
import android.view.*;
import android.widget.*;

public final class ShortcutAdapter extends ArrayAdapter<ShortcutEntry>
{

	private final int shortcutItemLayoutResource;

	public ShortcutAdapter(final Context context, final int shortcutItemLayoutResource)
	{
		super(context, 0);
		this.shortcutItemLayoutResource = shortcutItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final ShortcutEntry entry = getItem(position);

		viewHolder.titleView.setText(entry.getTitle());
		viewHolder.descriptionView.setText(entry.getDesc());
		viewHolder.iconView.setImageResource(entry.getIcon());

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

			workingView = inflater.inflate(shortcutItemLayoutResource, null);
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

			viewHolder.titleView = (TextView) workingView.findViewById(R.id.title);
			viewHolder.descriptionView = (TextView) workingView.findViewById(R.id.description);
			viewHolder.iconView = (ImageView) workingView.findViewById(R.id.shortcut_icon);
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
		public TextView titleView;
		public TextView descriptionView;
		public ImageView iconView;

	}


}
