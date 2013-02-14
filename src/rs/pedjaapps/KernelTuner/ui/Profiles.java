package rs.pedjaapps.KernelTuner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.actionbarsherlock.app.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.entry.*;
import rs.pedjaapps.KernelTuner.helpers.*;
import rs.pedjaapps.KernelTuner.tools.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import java.lang.Process;
import rs.pedjaapps.KernelTuner.R;



public class Profiles extends SherlockActivity
{
	private String _cpu0max;
	private String _cpu1max;
	private String _cpu0min;
	private String _cpu1min;
	private String _cpu2max;
	private String _cpu3max;
	private String _cpu2min;
	private String _cpu3min;
	private String _cpu0gov;
	private String _cpu1gov;
	private  String _cpu2gov;
	private String _cpu3gov;
	private String _mpup;
	private String _mpdown;
	private String _gpu2d;
	private String _gpu3d;
	private  String _cbb;
	private  int _fcharge;
	private int _vsync;
	private String _cdepth;
	private  String _scheduler;
	private  int _sdcache ;
	private int _s2w;
	private String _name;
	 
	private ProgressDialog pd;
	 
	private class SaveCurrentAsProfile extends AsyncTask<String, Void, Object>
	{

		@Override
		protected Object doInBackground(String... args)
		{
			Process localProcess;
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				
				if (CPUInfo.cpu1Online() == true)
						{
							localDataOutputStream.writeBytes("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n");
							localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu1/online\n");
							localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu1/online\n");
							localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu1/online\n");
							localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
						}
						if (CPUInfo.cpu2Online() == true)
						{
							localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu2/online\n");
							localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu2/online\n");
							localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu2/online\n");
							localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
						}
						if (CPUInfo.cpu3Online() == true)
						{
							localDataOutputStream.writeBytes("chmod 666 /sys/devices/system/cpu/cpu3/online\n");
							localDataOutputStream.writeBytes("echo 1 > /sys/devices/system/cpu/cpu3/online\n");
							localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/cpu3/online\n");
							localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
						}
						localDataOutputStream.writeBytes("exit\n");
						localDataOutputStream.flush();
						localDataOutputStream.close();
						localProcess.waitFor();
						localProcess.destroy();
			}
			catch (IOException e1)
			{
				
				e1.printStackTrace();
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
						
			_cpu0min = CPUInfo.cpu0MinFreq();
			_cpu0max = CPUInfo.cpu0MaxFreq();
			_cpu0gov = CPUInfo.cpu0CurGov();
			if(CPUInfo.cpu1Online()){
			_cpu1min = CPUInfo.cpu1MinFreq();
			_cpu1max = CPUInfo.cpu1MaxFreq();
			_cpu1gov = CPUInfo.cpu1CurGov();
			}
			if(CPUInfo.cpu2Online()){
			_cpu2min = CPUInfo.cpu2MinFreq();
			_cpu2max = CPUInfo.cpu2MaxFreq();
			_cpu2gov = CPUInfo.cpu2CurGov();
			}
			if(CPUInfo.cpu3Online()){
			_cpu3min = CPUInfo.cpu3MinFreq();
			_cpu3max = CPUInfo.cpu3MaxFreq();
			_cpu3gov = CPUInfo.cpu3CurGov();
			}
			_mpup = CPUInfo.mpup();
			_mpdown = CPUInfo.mpdown();
			_gpu2d = CPUInfo.gpu2d();
			_gpu3d = CPUInfo.gpu3d();
			_cbb = CPUInfo.cbb();
			_vsync = CPUInfo.vsync();
			_fcharge = CPUInfo.fcharge();
			_cdepth = CPUInfo.cDepth();
			_scheduler = CPUInfo.scheduler();
			_sdcache = CPUInfo.sdCache();
			_s2w = CPUInfo.s2w();
			
try{
	localProcess = Runtime.getRuntime().exec("su");

	DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
	
			if (CPUInfo.cpu1Online() == true)
			{
				localDataOutputStream.writeBytes("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n");
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu1/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu1/online\n");
			}
			if (CPUInfo.cpu2Online() == true)
			{
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu2/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu2/online\n");
			}
			if (CPUInfo.cpu3Online() == true)
			{
				localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/online\n");
				localDataOutputStream.writeBytes("echo 0 > /sys/devices/system/cpu/cpu3/online\n");
				localDataOutputStream.writeBytes("chown system /sys/devices/system/cpu/cpu3/online\n");
			}

			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
}
catch (IOException e1)
{
	e1.printStackTrace();
}
catch (InterruptedException e1)
{
	e1.printStackTrace();
}



			
			
			
			
			

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			

			db.addProfile(new Profile(_name,
					 _cpu0min, 
					  _cpu0max, 
			    		_cpu1min,
			    		_cpu1max,
			    		_cpu2min, 
			  		 _cpu2max, 
			      		_cpu3min,
			      		_cpu3max,
			    		_cpu0gov,
			    		_cpu1gov,
			    		_cpu2gov,
			    		_cpu3gov,
			    		"Unchanged",
			    		_mpdown,
			    		_mpup,
			    		_gpu2d,
			    		_gpu3d,
			    		_cbb,
			    		_vsync,
			    		_fcharge,
			    		_cdepth,
			    		_scheduler,
			    		_sdcache,
			    		_s2w
			    		));
				   profilesAdapter.clear();
					for (final ProfilesEntry entry : getProfilesEntries())
					{
						profilesAdapter.add(entry);
					}
					profilesListView.invalidate();
					profilesAdapter.notifyDataSetChanged();
					profiles = db.getAllProfiles();
					setUI();

					pd.dismiss();
		}

	}

	private DatabaseHandler db;
	private ProfilesAdapter profilesAdapter ;
	private ListView profilesListView;
	private List<Profile> profiles;
	private static final int GET_CODE = 0;
	String theme;
	boolean isLight;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.IndicatorLight);
			isLight = true;
		}
		else if(theme.equals("dark")){
			setTheme(R.style.IndicatorDark);
			isLight = false;
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.IndicatorLightDark);
			isLight = true;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiles);
		
		ImageView add = (ImageView)findViewById(R.id.add);
		add.setImageResource(isLight ? R.drawable.add_light : R.drawable.add_dark);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		db = new DatabaseHandler(this);
		profiles = db.getAllProfiles();
		
		profilesListView = (ListView) findViewById(R.id.list);
		profilesAdapter = new ProfilesAdapter(this, R.layout.profile_list_item);
		profilesListView.setAdapter(profilesAdapter);

		registerForContextMenu(profilesListView);
		
		for (final ProfilesEntry entry : getProfilesEntries())
		{
			profilesAdapter.add(entry);
		}
		
		profilesListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
						Profiles.this);

						
					final Profile profile = profiles.get(p3);
						
                     String cpu0min = profile.getCpu0min();
					String cpu0max = profile.getCpu0max();
					String cpu1min = profile.getCpu1min();
					String cpu1max = profile.getCpu1max();
					String cpu2min = profile.getCpu2min();
					String cpu2max = profile.getCpu2max();
					String cpu3min = profile.getCpu3min();
					String cpu3max = profile.getCpu3max();
					
					 String cpu0gov = profile.getCpu0gov();
					 String cpu1gov = profile.getCpu1gov();
					 String cpu2gov = profile.getCpu2gov();
					 String cpu3gov = profile.getCpu3gov();
					 
					 String mpup = profile.getMtu();
					 String mpdown = profile.getMtd();
					 
					 String gpu2d = profile.getGpu2d();
					 String gpu3d = profile.getGpu3d();
					
					 
					 String voltage = profile.getVoltage();

					 String buttons = profile.getButtonsLight();
					 int vsync = profile.getVsync();
					 int fcharge = profile.getFcharge();
					 String cdepth = profile.getCdepth();
					 String io = profile.getIoScheduler();
					 Integer sdcache = profile.getSdcache();

					 Integer s2w = profile.getSweep2wake();
					
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
					
					LinearLayout cpu0govll = (LinearLayout)view.findViewById(R.id.cpu0gov);
					LinearLayout cpu1govll = (LinearLayout)view.findViewById(R.id.cpu1gov);
					LinearLayout cpu2govll = (LinearLayout)view.findViewById(R.id.cpu2gov);
					LinearLayout cpu3govll = (LinearLayout)view.findViewById(R.id.cpu3gov);
					
					LinearLayout mpupll = (LinearLayout)view.findViewById(R.id.mpup);
					LinearLayout mpdownll = (LinearLayout)view.findViewById(R.id.mpdown);
					
					LinearLayout gpu2dll = (LinearLayout)view.findViewById(R.id.gpu2d);
					LinearLayout gpu3dll = (LinearLayout)view.findViewById(R.id.gpu3d);
					
					LinearLayout buttonsll = (LinearLayout)view.findViewById(R.id.buttons);

					LinearLayout vsyncBox = (LinearLayout)view.findViewById(R.id.vsync);
					LinearLayout fchargeBox = (LinearLayout)view.findViewById(R.id.fcharge);
					
					LinearLayout cdepthll = (LinearLayout)view.findViewById(R.id.cdepth);
					LinearLayout ioll = (LinearLayout)view.findViewById(R.id.io);
					LinearLayout sdll = (LinearLayout)view.findViewById(R.id.sd);
					LinearLayout voltagell = (LinearLayout)view.findViewById(R.id.voltage);
					LinearLayout s2wll = (LinearLayout)view.findViewById(R.id.s2w);

					
					//cpu0 min
					if( cpu0min != null && !cpu0min.equals(getResources().getString(R.string.unchanged)) && !cpu0min.equals("") ){
						tv1.setText(cpu0min);
					}
					else{
					 cpu0minll.setVisibility(View.GONE);
					
					}
				//cpu0 max
					if( cpu0max != null && !cpu0max.equals(getResources().getString(R.string.unchanged)) && !cpu0max.equals("") ){
						tv2.setText(cpu0max);
					}
					else{
						cpu0maxll.setVisibility(View.GONE);

					}
					//cpu1min
					if( cpu1min != null && !cpu1min.equals(getResources().getString(R.string.unchanged)) && !cpu1min.equals("") ){
						tv3.setText(cpu1min);
					}
					else{
						cpu1minll.setVisibility(View.GONE);

					}
					//cpu1max
					if( cpu1max != null && !cpu1max.equals(getResources().getString(R.string.unchanged)) && !cpu1max.equals("") ){
						tv4.setText(cpu1max);
					}
					else{
						cpu1maxll.setVisibility(View.GONE);

					}
					//cpu2min
					if( cpu2min != null && !cpu2min.equals(getResources().getString(R.string.unchanged)) && !cpu2min.equals("") ){
						tv5.setText(cpu2min);
					}
					else{
						cpu2minll.setVisibility(View.GONE);

					}
					//cpu2max
					if( cpu2max != null && !cpu2max.equals(getResources().getString(R.string.unchanged)) && !cpu2max.equals("") ){
						tv6.setText(cpu2max);
					}
					else{
						cpu2maxll.setVisibility(View.GONE);

					}
					//cpu3min
					if( cpu3min != null && !cpu3min.equals(getResources().getString(R.string.unchanged)) && !cpu3min.equals("") ){
						tv7.setText(cpu3min);
					}
					else{
						cpu3minll.setVisibility(View.GONE);

					}
					//cpu3max
					if( cpu3max != null && !cpu3max.equals(getResources().getString(R.string.unchanged)) && !cpu3max.equals("") ){
						tv8.setText(cpu3max);
					}
					else{
						cpu3maxll.setVisibility(View.GONE);

					}
					//cpu0governor
					if( cpu0gov != null && !cpu0gov.equals(getResources().getString(R.string.unchanged)) && !cpu0gov.equals("") ){
						tv9.setText(cpu0gov);
					}
					else{
						cpu0govll.setVisibility(View.GONE);

					}
					//cpu1governor
					if( cpu1gov != null && !cpu1gov.equals(getResources().getString(R.string.unchanged)) && !cpu1gov.equals("") ){
						tv10.setText(cpu1gov);
					}
					else{
						cpu1govll.setVisibility(View.GONE);

					}
					//cpu2governor
					if( cpu2gov != null && !cpu2gov.equals(getResources().getString(R.string.unchanged)) && !cpu2gov.equals("") ){
						tv11.setText(cpu2gov);
					}
					else{
						cpu2govll.setVisibility(View.GONE);

					}
					
					//cpu3governor
					if( cpu3gov != null && !cpu3gov.equals(getResources().getString(R.string.unchanged)) && !cpu3gov.equals("") ){
						tv12.setText(cpu3gov);
					}
					else{
						cpu3govll.setVisibility(View.GONE);

					}
					//voltage
					if( voltage != null && !voltage.equals(getResources().getString(R.string.unchanged)) && !voltage.equals("") ){
						tv13.setText(voltage);
					}
					else{
						voltagell.setVisibility(View.GONE);

					}
					//mpdecision down
					if( mpdown != null && !mpdown.equals("") ){
						tv14.setText(mpdown);
					}
					else{
						mpdownll.setVisibility(View.GONE);

					}
					//mpdecision up
					if( mpup != null && !mpup.equals("") ){
						tv15.setText(mpup);
					}
					else{
						mpupll.setVisibility(View.GONE);

					}
					//gpu2d
					if( gpu2d != null && !gpu2d.equals(getResources().getString(R.string.unchanged)) && !gpu2d.equals("") ){
						tv16.setText(gpu2d);
					}
					else{
						gpu2dll.setVisibility(View.GONE);
					}
					//gpu3d
					if( gpu3d != null && !gpu3d.equals(getResources().getString(R.string.unchanged)) && !gpu3d.equals("") ){
						tv17.setText(gpu3d);
					}
					else{
						gpu3dll.setVisibility(View.GONE);
					}
					//buttons
					if( buttons != null && !buttons.equals("") ){
						tv18.setText(buttons);
					}
					else{
						buttonsll.setVisibility(View.GONE);
					}
					//vsync
					if( vsync==0 ){
						tv19.setText("OFF");
					}
					else if(vsync == 1){
						tv19.setText("ON");
					}
					else{
						vsyncBox.setVisibility(View.GONE);
					}
					//fcharge
					if( fcharge==0 ){
						tv20.setText("OFF");
					}
					else if(fcharge == 1){
						tv20.setText("ON");
					}
					else{
						fchargeBox.setVisibility(View.GONE);
					}
					//cdepth
					if( cdepth != null && !cdepth.equals(getResources().getString(R.string.unchanged)) && !cdepth.equals("") ){
						tv21.setText(cdepth);
					}
					else{
						cdepthll.setVisibility(View.GONE);
					}
					//io
					if( io != null && !io.equals(getResources().getString(R.string.unchanged)) && !io.equals("") ){
						tv22.setText(io);
					}
					else{
						ioll.setVisibility(View.GONE);
					}
					//sd
					if( sdcache != null && sdcache!=0){
						tv23.setText(sdcache+"");
					}
					else{
						sdll.setVisibility(View.GONE);

					}
					//s2w
					if( s2w==0 ){
						tv24.setText("OFF");
					}
					else if(s2w==1){
						tv24.setText("On with no backlight");
					}
					else if(s2w==2){
						tv24.setText("On with backlight");
					}
					else{
						s2wll.setVisibility(View.GONE);
					}
					builder.setIcon(R.drawable.ic_menu_cc);

					
					builder.setView(view);
					AlertDialog alert = builder.create();

					alert.show();
				}

			
		});
		
		setUI();
		
	}
	
	private  void setUI(){
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
		


		return entries;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//MenuInflater inflater = getSupportMenuInflater();
		//inflater.inflate(R.menu.profiles_options_menu, menu);
		if(theme.equals("light")){
			isLight = true;
			}
			else if(theme.equals("dark")){
				isLight = false;
			}
			else if(theme.equals("light_dark_action_bar")){
				isLight = false;
			}
		menu.add(1, 1, 1, "Add")
        .setIcon(isLight ? R.drawable.add_light : R.drawable.add_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		menu.add(2, 2, 2, "Delete All")
        .setIcon(isLight ? R.drawable.delete_light : R.drawable.delete_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		menu.add(3, 3, 3, "Save")
        .setIcon(isLight ? R.drawable.apply_light : R.drawable.apply_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return super.onCreateOptionsMenu(menu);
}





@Override
public boolean onOptionsItemSelected(MenuItem item) {

	if (item.getItemId() == 1)
	{
    	Intent intent = new Intent();
        intent.setClass(this,ProfileEditor.class);
        intent.putExtra("profileName","");
        startActivityForResult(intent,GET_CODE);
	}	
		
	if (item.getItemId() == 2)
	{
    	AlertDialog.Builder builder = new AlertDialog.Builder(
                Profiles.this);

    	if(theme.equals("light")){
			isLight = true;
			}
			else if(theme.equals("dark")){
				isLight = false;
			}
			else if(theme.equals("light_dark_action_bar")){
				isLight = true;
			}
			builder.setTitle(getResources().getString(R.string.delete_all_profiles));

			builder.setMessage(getResources().getString(R.string.delete_all_profiles_confirm));

			builder.setIcon(isLight ? R.drawable.delete_light : R.drawable.delete_dark);

			builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
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

						profilesAdapter.notifyDataSetChanged();
						profiles = db.getAllProfiles();
						setUI();
					}
				});
			builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
			AlertDialog alert = builder.create();

			alert.show();
	}
	
	if (item.getItemId() == 3)
	{
	AlertDialog.Builder builder2 = new AlertDialog.Builder(
            Profiles.this);

		builder2.setTitle(getResources().getString(R.string.save_current_settings));

		if(theme.equals("light")){
			isLight = true;
			}
			else if(theme.equals("dark")){
				isLight = false;
			}
			else if(theme.equals("light_dark_action_bar")){
				isLight = true;
			}

		builder2.setIcon(isLight ? R.drawable.save_light : R.drawable.save_dark);
		final EditText ed2 = new EditText(Profiles.this);
		ed2.setHint("Profile Name");
		builder2.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if(ed2.getText().toString().length()<1 || ed2.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_profile_name), Toast.LENGTH_LONG).show();
						
					}
					else{
					_name = ed2.getText().toString();
					Profiles.this.pd = ProgressDialog.show(Profiles.this, null,
							getResources().getString(R.string.gathering_system_info), true, false);
					new SaveCurrentAsProfile().execute();
					}
				}
			});
		builder2.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});
		builder2.setView(ed2);
		AlertDialog alert2 = builder2.create();

		alert2.show();	
}
	if(item.getItemId()==android.R.id.home || item.getItemId()==0){
        // app icon in action bar clicked; go home
        Intent intent = new Intent(this, KernelTuner.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);  
		return true;
	}
return super.onOptionsItemSelected(item);

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
    		data.getStringExtra("mtd"),
    		data.getStringExtra("mtu"),
    		data.getStringExtra("gpu2d"),
    		data.getStringExtra("gpu3d"),
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
		profilesAdapter.notifyDataSetChanged();
		profiles = db.getAllProfiles();
		setUI();
 
	
   }
   
   else{
    //text.setText("Cancelled");
   }
  }
 }
	
@Override
public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    super.onCreateContextMenu(menu, v, menuInfo);
	final Profile profile = profiles.get(info.position);
    menu.setHeaderTitle(profile.getName());
    menu.setHeaderIcon(R.drawable.ic_menu_cc);
    android.view.MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.profiles_context_menu, menu);
}

@Override
public boolean onContextItemSelected(android.view.MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
            .getMenuInfo();
    final Profile profile = profiles.get(info.position);
    switch (item.getItemId()) {
    case R.id.apply:
    	
    	new ProfileApplier(Profiles.this).execute(new String[] {profile.getName()});
	      
        return true;
    
	case R.id.delete:
		db.deleteProfile(profile);
		profilesAdapter.clear();
		for (final ProfilesEntry entry : getProfilesEntries())
		{
			profilesAdapter.add(entry);
		}
		profilesListView.invalidate();
		profilesAdapter.notifyDataSetChanged();
		profiles = db.getAllProfiles();
		setUI();
      return true;
	case R.id.edit:
		Intent intent = new Intent();
        intent.setClass(this,ProfileEditor.class);
        intent.putExtra("profileName", profile.getName());
        startActivityForResult(intent,GET_CODE);
	      return true;
	      
	case R.id.copy:
		AlertDialog.Builder builder = new AlertDialog.Builder(
                Profiles.this);

			builder.setTitle(getResources().getString(R.string.copy_profile));

			if(theme.equals("light")){
				isLight = true;
				}
				else if(theme.equals("dark")){
					isLight = false;
				}
				else if(theme.equals("light_dark_action_bar")){
					isLight = true;
				}

			builder.setIcon(isLight ? R.drawable.copy_light : R.drawable.copy_dark);
			final EditText ed = new EditText(Profiles.this);
			ed.setHint(getResources().getString(R.string.profile_name));
			builder.setPositiveButton(getResources().getString(R.string.copy), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
						String name = ed.getText().toString();
						 String cpu0min = profile.getCpu0min();
							String cpu0max = profile.getCpu0max();
							String cpu1min = profile.getCpu1min();
							String cpu1max = profile.getCpu1max();
							String cpu2min = profile.getCpu2min();
							String cpu2max = profile.getCpu2max();
							String cpu3min = profile.getCpu3min();
							String cpu3max = profile.getCpu3max();
							
							 String cpu0gov = profile.getCpu0gov();
							 String cpu1gov = profile.getCpu1gov();
							 String cpu2gov = profile.getCpu2gov();
							 String cpu3gov = profile.getCpu3gov();
							 
							 String mpup = profile.getMtu();
							 String mpdown = profile.getMtd();
							 
							 String gpu2d = profile.getGpu2d();
							 String gpu3d = profile.getGpu3d();
						
							 
							 String voltage = profile.getVoltage();

							 String buttons = profile.getButtonsLight();
							 int vsync = profile.getVsync();
							 int fcharge = profile.getFcharge();
							 String cdepth = profile.getCdepth();
							 String io = profile.getIoScheduler();
							 Integer sdcache = profile.getSdcache();

							 Integer s2w = profile.getSweep2wake();
						 db.addProfile(new Profile(name,
								 cpu0min, 
								  cpu0max, 
						    		cpu1min,
						    		cpu1max,
						    		cpu2min, 
						  		 cpu2max, 
						      		cpu3min,
						      		cpu3max,
						    		cpu0gov,
						    		cpu1gov,
						    		cpu2gov,
						    		cpu3gov,
						    		voltage,
						    		mpdown,
						    		mpup,
						    		gpu2d,
						    		gpu3d,
						    		buttons,
						    		vsync,
						    		fcharge,
						    		cdepth,
						    		io,
						    		sdcache,
						    		s2w
						    		));
							   profilesAdapter.clear();
								for (final ProfilesEntry entry : getProfilesEntries())
								{
									profilesAdapter.add(entry);
								}
								profilesListView.invalidate();
								profilesAdapter.notifyDataSetChanged();
								profiles = db.getAllProfiles();
								setUI();
					}
				});
			builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
			builder.setView(ed);
			AlertDialog alert = builder.create();

			alert.show();
		
	      return true;
	      
	
  }
    return false;
}

}
