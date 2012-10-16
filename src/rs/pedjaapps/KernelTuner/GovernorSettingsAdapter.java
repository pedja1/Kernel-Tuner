package rs.pedjaapps.KernelTuner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class GovernorSettingsAdapter extends ArrayAdapter<GovEntry> {

	private final int govItemLayoutResource;

	public GovernorSettingsAdapter(final Context context, final int govItemLayoutResource) {
	super(context, 0);
	this.govItemLayoutResource = govItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

	final View view = getWorkingView(convertView);
	final ViewHolder viewHolder = getViewHolder(view);
	final GovEntry entry = getItem(position);

	viewHolder.nameView.setText(entry.getName());

	

	viewHolder.valueView.setText(entry.getValue());


	return view;
	}

	private View getWorkingView(final View convertView) {
	View workingView = null;

	if(null == convertView) {
	final Context context = getContext();
	final LayoutInflater inflater = (LayoutInflater)context.getSystemService
	(Context.LAYOUT_INFLATER_SERVICE);

	workingView = inflater.inflate(govItemLayoutResource, null);
	} else {
	workingView = convertView;
	}

	return workingView;
	}

	private ViewHolder getViewHolder(final View workingView) {
	final Object tag = workingView.getTag();
	ViewHolder viewHolder = null;


	if(null == tag || !(tag instanceof ViewHolder)) {
	viewHolder = new ViewHolder();

	viewHolder.nameView = (TextView) workingView.findViewById(R.id.name);
	viewHolder.valueView = (TextView) workingView.findViewById(R.id.value);
	
	workingView.setTag(viewHolder);

	} else {
	viewHolder = (ViewHolder) tag;
	}

	return viewHolder;
	}

	private class ViewHolder {
	public TextView nameView;
	public TextView valueView;
	
	}


	}