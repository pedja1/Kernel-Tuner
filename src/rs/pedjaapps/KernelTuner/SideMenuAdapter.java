package rs.pedjaapps.KernelTuner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class SideMenuAdapter extends ArrayAdapter<SideMenuEntry>
{

	private final int sideItemLayoutResource;

	public SideMenuAdapter(final Context context, final int sideItemLayoutResource)
	{
		super(context, 0);
		this.sideItemLayoutResource = sideItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final SideMenuEntry entry = getItem(position);

		viewHolder.imageView.setImageResource(entry.getImage());
		viewHolder.titleView.setText(entry.getTitle());

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

			workingView = inflater.inflate(sideItemLayoutResource, null);
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

			viewHolder.imageView = (ImageView) workingView.findViewById(R.id.image);
			viewHolder.titleView = (TextView) workingView.findViewById(R.id.title);
			
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
		public ImageView imageView;
		public TextView titleView;

	}


}
