package rs.pedjaapps.KernelTuner;


import android.content.*;
import android.view.*;
import android.widget.*;

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
		/*	viewHolder.vtView = (ImageView)workingView.findViewById(R.id.vt);
			viewHolder.mdView = (ImageView)workingView.findViewById(R.id.md);
			viewHolder.gpuView = (ImageView)workingView.findViewById(R.id.gpu);
			viewHolder.cblView = (ImageView)workingView.findViewById(R.id.cbl);
			viewHolder.vsView = (ImageView)workingView.findViewById(R.id.vs);
			viewHolder.fcView = (ImageView)workingView.findViewById(R.id.fc);
			viewHolder.cdView = (ImageView)workingView.findViewById(R.id.cd);
			viewHolder.ioView = (ImageView)workingView.findViewById(R.id.io);
			viewHolder.sdView = (ImageView)workingView.findViewById(R.id.sd);
			viewHolder.nltView = (ImageView)workingView.findViewById(R.id.nlt);
			viewHolder.s2wView = (ImageView)workingView.findViewById(R.id.s2w);*/

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
	/*	public ImageView vtView;
		public ImageView mdView;
		public ImageView gpuView;
		public ImageView cblView;
		public ImageView vsView;
		public ImageView fcView;
		public ImageView cdView;
		public ImageView ioView;
		public ImageView sdView;
		public ImageView nltView;
		public ImageView s2wView;*/

	}


}
