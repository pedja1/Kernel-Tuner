package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.*;

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
    	
    //	Intent intent = new Intent();
       // intent.setClass(this,ProfileEditor.class);
      //  startActivityForResult(intent,GET_CODE);
		
		db.addProfile(new Profile(0, "Battery", "192", 
								  "1188", 
								  "192", 
								  "1188", 
								  "1188", 
								  "1188", 
								  "1188", 
								  "1188", 
								  "ondemand",
								  "ondemand",
								  "ondemand",
								  "ondemand",
								  "low",	
								  "50",
								  "100",
								  "320",
								  "320",
								  "60",
								  1,
								  0,
								  "24",
								  "noop",
								  3072,
								  "null",
								  2));
			 profilesAdapter.clear();
		for (final ProfilesEntry entry : getProfilesEntries())
		{
			profilesAdapter.add(entry);
		}
		profilesListView.invalidate();
		setUI();
		
		return true;
    case 1:
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
		return true;
   
       
}
return false;

}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  
  if (requestCode == GET_CODE){
   if (resultCode == RESULT_OK) {
  
  /*db.addProfile(new Profile(data.getStringExtra("Name"),
		  data.getStringExtra("cpu0min"), 
		  data.getStringExtra("cpu0max"), 
    		data.getStringExtra("cpu1min"),
    		data.getStringExtra("cpu1max"),
    		data.getStringExtra("cpu0gov"),
    		data.getStringExtra("cpu1gov"),
    		data.getStringExtra("gpu2d"),
    		data.getStringExtra("gpu3d"),
    		data.getStringExtra("vsync"),
    		data.getStringExtra("noc"),
    		data.getStringExtra("mtd"),
    		data.getStringExtra("mtu")));*/
  //getprofiles();
  //spinnerProfiles(); 
 
	
   }
   
   else{
    //text.setText("Cancelled");
   }
  }
 }
	
}
