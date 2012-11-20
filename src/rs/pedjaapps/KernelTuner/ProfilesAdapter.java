package rs.pedjaapps.KernelTuner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
