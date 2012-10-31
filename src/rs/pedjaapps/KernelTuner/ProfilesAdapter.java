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

		 int cpu = entry.getCpu();
		 int vt = entry.getVt();
		 int md = entry.getMd();
		 int gpu = entry.getGpu();
		 int cbl = entry.getCbl();
		 int vs = entry.getVs();
		 int fc = entry.getFc();
		 int cd = entry.getCd();
		 int io = entry.getIo();
		 int sd = entry.getSd();
		 int nlt = entry.getNlt();
		 int s2w = entry.getS2w();
		 
		 
		 if (cpu==1){
			 
			 viewHolder.cpuView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.cpuView.setVisibility(View.GONE);
		 }
		 if (vt==1){
			 viewHolder.vtView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.vtView.setVisibility(View.GONE);
		 }
		 if (md==1){
			 viewHolder.mdView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.mdView.setVisibility(View.GONE);
		 }
		 if (gpu==1){
			 viewHolder.gpuView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.gpuView.setVisibility(View.GONE);
		 }
		 if (cbl==1){
			 viewHolder.cblView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.cblView.setVisibility(View.GONE);
		 }
		 if (vs==1){
			 viewHolder.vsView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.vsView.setVisibility(View.GONE);
		 }
		 if (fc==1){
			 viewHolder.fcView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.fcView.setVisibility(View.GONE);
		 }
		 if (cd==1){
			 viewHolder.cdView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.cdView.setVisibility(View.GONE);
		 }
		 if (io==1){
			 viewHolder.ioView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.ioView.setVisibility(View.GONE);
		 }
		 if (sd==1){
			 viewHolder.sdView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.sdView.setVisibility(View.GONE);
		 }
		 if (nlt==1){
			 viewHolder.nltView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.nltView.setVisibility(View.GONE);
		 }
		 if (s2w==1){
			 viewHolder.s2wView.setVisibility(View.VISIBLE);
		 }
		 else{
			 viewHolder.s2wView.setVisibility(View.GONE);
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
			viewHolder.cpuView = (ImageView)workingView.findViewById(R.id.cpu);
			viewHolder.vtView = (ImageView)workingView.findViewById(R.id.vt);
			viewHolder.mdView = (ImageView)workingView.findViewById(R.id.md);
			viewHolder.gpuView = (ImageView)workingView.findViewById(R.id.gpu);
			viewHolder.cblView = (ImageView)workingView.findViewById(R.id.cbl);
			viewHolder.vsView = (ImageView)workingView.findViewById(R.id.vs);
			viewHolder.fcView = (ImageView)workingView.findViewById(R.id.fc);
			viewHolder.cdView = (ImageView)workingView.findViewById(R.id.cd);
			viewHolder.ioView = (ImageView)workingView.findViewById(R.id.io);
			viewHolder.sdView = (ImageView)workingView.findViewById(R.id.sd);
			viewHolder.nltView = (ImageView)workingView.findViewById(R.id.nlt);
			viewHolder.s2wView = (ImageView)workingView.findViewById(R.id.s2w);

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
		public ImageView cpuView;
		public ImageView vtView;
		public ImageView mdView;
		public ImageView gpuView;
		public ImageView cblView;
		public ImageView vsView;
		public ImageView fcView;
		public ImageView cdView;
		public ImageView ioView;
		public ImageView sdView;
		public ImageView nltView;
		public ImageView s2wView;

	}


}
