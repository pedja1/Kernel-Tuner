/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.kerneltuner.ui;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.GovEntry;
import rs.pedjaapps.kerneltuner.helpers.GovernorSettingsAdapter;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.utility.ChangeGovernorSettings;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class GovernorActivity extends AbsActivity
{

	private GovernorSettingsAdapter govAdapter ;
	private ListView govListView;
	private List<String> fileList;
	private List<String> availableGovs;
	private List<String> govValues;
	private List<String> governors;
	private List<String> temp;
	boolean isLight;
	Context c;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		c = this;
		availableGovs = IOHelper.availableGovs();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.governor_settings);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		govListView = (ListView) findViewById(R.id.list);
		if (!availableGovs.isEmpty())
		{

			govAdapter = new GovernorSettingsAdapter(c, R.layout.governor_list_item);
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
								
								new ChangeGovernorSettings(c).execute(new String[] {input.getText()+"", fileList.get(position), governors.get(position)});

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

	

	private final List<GovEntry> getGovEntries()
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(c, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
}
