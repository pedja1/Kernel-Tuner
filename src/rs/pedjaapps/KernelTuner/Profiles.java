package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import android.widget.AdapterView.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Profiles extends Activity
{

	SharedPreferences sharedPrefs;
	DatabaseHandler db;
	ProfilesAdapter profilesAdapter ;
	ListView profilesListView;
	private static final int GET_CODE = 0;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiles);

		
		
		db = new DatabaseHandler(this);
	
		
		profilesListView = (ListView) findViewById(R.id.list);
		profilesAdapter = new ProfilesAdapter(this, R.layout.profile_list_item);
		profilesListView.setAdapter(profilesAdapter);

		for (final ProfilesEntry entry : getProfilesEntries())
		{
			profilesAdapter.add(entry);
		}
		
		profilesListView.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
						Profiles.this);

						DatabaseHandler db = new DatabaseHandler(Profiles.this);
						List<Profile> profiles = db.getAllProfiles();
					Profile profile = profiles.get(p3);
						
                     String cpu0min = profile.getCpu0min();
					String cpu0max = profile.getCpu0max();
					String cpu1min = profile.getCpu1min();
					String cpu1max = profile.getCpu1max();
					String cpu2min = profile.getCpu2min();
					String cpu2max = profile.getCpu2max();
					String cpu3min = profile.getCpu3min();
					String cpu3max = profile.getCpu3max();
					
				builder.setTitle(profile.getName());
                 	
					LinearLayout holder = new LinearLayout(Profiles.this);
					View view = getLayoutInflater().inflate(R.layout.profile_info, holder,false);
				//	List<TextView> tvList = new ArrayList<TextView>();
					TextView tv1 = (TextView)view.findViewById(R.id.tv1);
					TextView tv2 = (TextView)view.findViewById(R.id.tv2);
					TextView tv3 = (TextView)view.findViewById(R.id.tv3);
					TextView tv4 = (TextView)view.findViewById(R.id.tv4);
					TextView tv5 = (TextView)view.findViewById(R.id.tv5);
					TextView tv6 = (TextView)view.findViewById(R.id.tv6);
					TextView tv7 = (TextView)view.findViewById(R.id.tv7);
					TextView tv8 = (TextView)view.findViewById(R.id.tv8);
					TextView tv9 = (TextView)view.findViewById(R.id.tv9);
					TextView tv10 = (TextView)view.findViewById(R.id.tv10);
					TextView tv11 = (TextView)view.findViewById(R.id.tv11);
					TextView tv12 = (TextView)view.findViewById(R.id.tv12);
					TextView tv13 = (TextView)view.findViewById(R.id.tv13);
					TextView tv14 = (TextView)view.findViewById(R.id.tv14);
					TextView tv15 = (TextView)view.findViewById(R.id.tv15);
					TextView tv16 = (TextView)view.findViewById(R.id.tv16);
					TextView tv17 = (TextView)view.findViewById(R.id.tv17);
					TextView tv18 = (TextView)view.findViewById(R.id.tv18);
					TextView tv19 = (TextView)view.findViewById(R.id.tv19);
					TextView tv20 = (TextView)view.findViewById(R.id.tv20);
					TextView tv21 = (TextView)view.findViewById(R.id.tv21);
					TextView tv22 = (TextView)view.findViewById(R.id.tv22);
					TextView tv23 = (TextView)view.findViewById(R.id.tv23);
					TextView tv24 = (TextView)view.findViewById(R.id.tv24);
					LinearLayout cpu0minll = (LinearLayout)view.findViewById(R.id.cpu0min);
					LinearLayout cpu0maxll = (LinearLayout)view.findViewById(R.id.cpu0max);
					LinearLayout cpu1minll = (LinearLayout)view.findViewById(R.id.cpu1min);
					LinearLayout cpu1maxll = (LinearLayout)view.findViewById(R.id.cpu1max);
					LinearLayout cpu2minll = (LinearLayout)view.findViewById(R.id.cpu2min);
					LinearLayout cpu2maxll = (LinearLayout)view.findViewById(R.id.cpu2max);
					LinearLayout cpu3minll = (LinearLayout)view.findViewById(R.id.cpu3min);
					LinearLayout cpu3maxll = (LinearLayout)view.findViewById(R.id.cpu3max);
					if( cpu0min != null && !cpu0min.equals("Unchanged") && !cpu0min.equals("") ){
						tv1.setText(cpu0min);
					}
					else{
					 cpu0minll.setVisibility(View.GONE);
					
					}
				
					if( cpu0max != null && !cpu0max.equals("Unchanged") && !cpu0max.equals("") ){
						tv2.setText(cpu0max);
					}
					else{
						cpu0maxll.setVisibility(View.GONE);

					}
					
					if( cpu1min != null && !cpu1min.equals("Unchanged") && !cpu1min.equals("") ){
						tv3.setText(cpu1min);
					}
					else{
						cpu1minll.setVisibility(View.GONE);

					}

					if( cpu1max != null && !cpu1max.equals("Unchanged") && !cpu1max.equals("") ){
						tv4.setText(cpu1max);
					}
					else{
						cpu1maxll.setVisibility(View.GONE);

					}
					//
					if( cpu2min != null && !cpu2min.equals("Unchanged") && !cpu2min.equals("") ){
						tv5.setText(cpu2min);
					}
					else{
						cpu2minll.setVisibility(View.GONE);

					}

					if( cpu2max != null && !cpu2max.equals("Unchanged") && !cpu2max.equals("") ){
						tv6.setText(cpu2max);
					}
					else{
						cpu2maxll.setVisibility(View.GONE);

					}

					if( cpu3min != null && !cpu3min.equals("Unchanged") && !cpu3min.equals("") ){
						tv7.setText(cpu3min);
					}
					else{
						cpu3minll.setVisibility(View.GONE);

					}

					if( cpu3max != null && !cpu3max.equals("Unchanged") && !cpu3max.equals("") ){
						tv8.setText(cpu3max);
					}
					else{
						cpu3maxll.setVisibility(View.GONE);

					}
					builder.setIcon(R.drawable.ic_menu_cc);

					/*builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
							}
						});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{

							}
						});*/
					builder.setView(view);
					AlertDialog alert = builder.create();

					alert.show();
				}

			
		});
		
		setUI();
		
	}
	
	public void setUI(){
		TextView tv1 = (TextView)findViewById(R.id.tv1);
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll1);
		if(profilesAdapter.isEmpty()==false){
			tv1.setVisibility(View.GONE);
			ll.setVisibility(View.GONE);
		}
		else{
			tv1.setVisibility(View.VISIBLE);
			ll.setVisibility(View.VISIBLE);
		}
	}
	
	private List<ProfilesEntry> getProfilesEntries()
	{

		final List<ProfilesEntry> entries = new ArrayList<ProfilesEntry>();
        List<Profile> profiles = db.getAllProfiles();
		
		for(Profile p : profiles){
			entries.add(new ProfilesEntry(p.getName(), 0));
		}
		
	//	entries.add(new ProfilesEntry("Battery", 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
	//	entries.add(new ProfilesEntry("Performances", 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0));
	//	entries.add(new ProfilesEntry("test", 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0));

		

		return entries;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
menu.add(Menu.NONE, 0, 0, "Add ").setIcon(R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
menu.add(Menu.NONE, 1, 1, "Delete All").setIcon(R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onPrepareOptionsMenu (Menu menu) {

return true;
}




@Override
public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId()) {
    case 0:
    	
    	Intent intent = new Intent();
        intent.setClass(this,ProfileEditor.class);
        startActivityForResult(intent,GET_CODE);
		
		
			
		
		return true;
    case 1:
    	AlertDialog.Builder builder = new AlertDialog.Builder(
                Profiles.this);

			builder.setTitle("Delete all profiles");

			builder.setMessage("Are you sure you want to delete all profiles?");

			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton("Yes, do it.", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						List<Profile> profiles = db.getAllProfiles();
				        
						for(Profile p : profiles){
							db.deleteProfile(p);
						
						}
						profilesAdapter.clear();
						for (final ProfilesEntry entry : getProfilesEntries())
						{
							profilesAdapter.add(entry);
						}
						profilesListView.invalidate();
						setUI();
					}
				});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
			AlertDialog alert = builder.create();

			alert.show();
		
		return true;
   
       
}
return false;

}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  
  if (requestCode == GET_CODE){
   if (resultCode == RESULT_OK) {
  
  db.addProfile(new Profile(data.getStringExtra("Name"),
		  data.getStringExtra("cpu0min"), 
		  data.getStringExtra("cpu0max"), 
    		data.getStringExtra("cpu1min"),
    		data.getStringExtra("cpu1max"),
    		data.getStringExtra("cpu2min"), 
  		  data.getStringExtra("cpu2max"), 
      		data.getStringExtra("cpu3min"),
      		data.getStringExtra("cpu3max"),
    		data.getStringExtra("cpu0gov"),
    		data.getStringExtra("cpu1gov"),
    		data.getStringExtra("cpu2gov"),
    		data.getStringExtra("cpu3gov"),
    		data.getStringExtra("voltageProfile"),
    		data.getStringExtra("gpu2d"),
    		data.getStringExtra("gpu3d"),
    		data.getStringExtra("mtd"),
    		data.getStringExtra("mtu"),
    		data.getStringExtra("buttonsBacklight"),
    		data.getIntExtra("vsync", 0),
    		data.getIntExtra("fcharge", 0),
    		data.getStringExtra("cdepth"),
    		data.getStringExtra("io"),
    		data.getIntExtra("sdcache", 0),
    		data.getIntExtra("s2w", 0))
    		);
	   profilesAdapter.clear();
		for (final ProfilesEntry entry : getProfilesEntries())
		{
			profilesAdapter.add(entry);
		}
		profilesListView.invalidate();
		setUI();
 
	
   }
   
   else{
    //text.setText("Cancelled");
   }
  }
 }
	
}
