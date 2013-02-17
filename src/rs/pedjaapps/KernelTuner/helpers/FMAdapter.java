package rs.pedjaapps.KernelTuner.helpers;


import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FMEntry;
import android.content.*;
import android.view.*;
import android.widget.*;

public final class FMAdapter extends ArrayAdapter<FMEntry>
{

	private final int itemsItemLayoutResource;
    Context c;
	public FMAdapter(final Context context, final int itemsItemLayoutResource)
	{
		
		super(context, 0);
		this.itemsItemLayoutResource = itemsItemLayoutResource;
		this.c = context;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{

		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final FMEntry entry = getItem(position);

		
			
		viewHolder.nameView.setText(entry.getName());
		viewHolder.dateView.setText(entry.getDate());
		viewHolder.sizeView.setText(entry.getSize());
		if(entry.getFolder()==1){
			viewHolder.imageView.setImageResource(R.drawable.folder);
		}
		else{
			viewHolder.imageView.setImageResource(R.drawable.file);
			
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

			workingView = inflater.inflate(itemsItemLayoutResource, null);
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
			viewHolder.dateView = (TextView) workingView.findViewById(R.id.date);
			viewHolder.sizeView = (TextView) workingView.findViewById(R.id.size);
			viewHolder.imageView = (ImageView) workingView.findViewById(R.id.image);
			
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
		public TextView dateView;
		public TextView sizeView;
		public ImageView imageView;
	
	}


}
