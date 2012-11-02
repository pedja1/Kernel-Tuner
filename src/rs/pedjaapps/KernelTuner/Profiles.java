package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

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
