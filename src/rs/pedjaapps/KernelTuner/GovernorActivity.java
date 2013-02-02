package rs.pedjaapps.KernelTuner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.slidingmenu.lib.SlidingMenu;

public class GovernorActivity extends SherlockActivity
{

	private GovernorSettingsAdapter govAdapter ;
	private ListView govListView;
	private List<String> fileList;
	private List<String> availableGovs = CPUInfo.availableGovs();
	private List<String> govValues;
	private List<String> governors;
	private List<String> temp;
	boolean isLight;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
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

		setContentView(R.layout.governor_settings);
		final SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.side);
		
		GridView sideView = (GridView) menu.findViewById(R.id.grid);
		SideMenuAdapter sideAdapter = new SideMenuAdapter(this, R.layout.side_item);
		System.out.println("check "+sideView+" "+sideAdapter);
		sideView.setAdapter(sideAdapter);

		
		sideView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				List<SideMenuEntry> entries =  SideItems.getEntries();
				Intent intent = new Intent();
				intent.setClass(GovernorActivity.this, entries.get(position).getActivity());
				startActivity(intent);
				menu.showContent();
			}
			
		});
		List<SideMenuEntry> entries =  SideItems.getEntries();
		for(SideMenuEntry e: entries){
			sideAdapter.add(e);
		}
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		govListView = (ListView) findViewById(R.id.list);
		if (!availableGovs.isEmpty())
		{

			govAdapter = new GovernorSettingsAdapter(this, R.layout.governor_list_item);
			govListView.setAdapter(govAdapter);

			for (final GovEntry entry : getGovEntries())
			{
				govAdapter.add(entry);
			}
		}
		else
		{
			TextView tv = (TextView)findViewById(R.id.textView1); 
			tv.setVisibility(View.VISIBLE);
			tv.setText(getResources().getString(R.string.gov_not_supported));
		}
		govListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
				{

					String[] valuess = govValues.toArray(new String[0]);
					AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

					builder.setTitle(fileList.get(position));

					builder.setMessage(getResources().getString(R.string.gov_new_value));

					builder.setIcon(isLight ? R.drawable.edit_light: R.drawable.edit_dark);


					final EditText input = new EditText(view.getContext());
					input.setHint(valuess[position]);
					input.selectAll();
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					input.setGravity(Gravity.CENTER_HORIZONTAL);

					builder.setPositiveButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								new ChangeGovernorSettings(GovernorActivity.this).execute(new String[] {input.getText()+"", fileList.get(position), governors.get(position)});

								try
								{
									Thread.sleep(700);
								}
								catch (InterruptedException e)
								{
								}
								getGovEntries();

								govAdapter.clear();
								for (final GovEntry entry : getGovEntries())
								{
									govAdapter.add(entry);
								}
								govAdapter.notifyDataSetChanged();
								govListView.invalidate();
							}
						});
					builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								

							}

						});
					builder.setView(input);

					AlertDialog alert = builder.create();

					alert.show();


				} 
			});

	}

	

	private List<GovEntry> getGovEntries()
	{

		final List<GovEntry> entries = new ArrayList<GovEntry>();
		fileList = new ArrayList<String>();
		govValues = new ArrayList<String>();
		governors = new ArrayList<String>();
		temp = new ArrayList<String>();

		for (String s : availableGovs)
		{
			File gov = new File("/sys/devices/system/cpu/cpufreq/" + s + "/");

			if (gov.exists())
			{
				File[] files = gov.listFiles();
				if(files!=null){
				for (File file : files)
				{
					temp.add(file.getName());
					fileList.add(file.getName());

				}

				int tempSize = temp.size();
				for (int i = 0; i < tempSize; i++)
				{

					try
					{

		    			File myFile = new File("/sys/devices/system/cpu/cpufreq/" + s + "/" + temp.get(i).trim());
		    			FileInputStream fIn = new FileInputStream(myFile);
		    			BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
		    			String aDataRow = "";
		    			String aBuffer = "";
		    			while ((aDataRow = myReader.readLine()) != null)
						{
		    				aBuffer += aDataRow + "\n";
		    			}	    			


		    			myReader.close();

		    			entries.add(new GovEntry(temp.get(i), aBuffer));
		    			govValues.add(aBuffer);
		    			governors.add(s);


		    		}
					catch (Exception e)
					{
						}

				}
				}

			}

			temp.clear();


		}


		return entries;
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
}
