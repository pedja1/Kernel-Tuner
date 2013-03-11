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
package rs.pedjaapps.KernelTuner.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.BuildEntry;
import rs.pedjaapps.KernelTuner.helpers.BuildAdapter;
import rs.pedjaapps.KernelTuner.tools.RootExecuter;
import rs.pedjaapps.KernelTuner.tools.Tools;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;



public class BuildpropEditor extends SherlockActivity
{
	
	ListView bListView;
	BuildAdapter bAdapter;
	List<BuildEntry> entries;
	ProgressDialog pd;
	//CheckBox kernel, vm, fs, net;
	SharedPreferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = preferences.getString("theme", "light");

		if (theme.equals("light"))
		{
			setTheme(R.style.Theme_Sherlock_Light);
		}
		else if (theme.equals("dark"))
		{
			setTheme(R.style.Theme_Sherlock);

		}
		else if (theme.equals("light_dark_action_bar"))
		{
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.build);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		/**
		 * Load ads if enabled in settings*/
		final boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{
			AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());
		}
		
/*
		kernel = (CheckBox)findViewById(R.id.kernel);
		vm = (CheckBox)findViewById(R.id.vm);
		fs = (CheckBox)findViewById(R.id.fs);
		net = (CheckBox)findViewById(R.id.net);

		kernel.setChecked(preferences.getBoolean("sysctl_kernel", true));
		vm.setChecked(preferences.getBoolean("sysctl_vm", true));
		fs.setChecked(preferences.getBoolean("sysctl_fs", true));
		net.setChecked(preferences.getBoolean("sysctl_net", false));

		kernel.setOnCheckedChangeListener(new Listener());
		net.setOnCheckedChangeListener(new Listener());
		vm.setOnCheckedChangeListener(new Listener());
		fs.setOnCheckedChangeListener(new Listener());
*/


		bListView = (ListView) findViewById(R.id.list);
		bAdapter = new BuildAdapter(this, R.layout.build_row);
		bListView.setAdapter(bAdapter);

		bListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v, final int pos,
										long is)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					final BuildEntry tmpEntry = bAdapter.getItem(pos);
					builder.setTitle(tmpEntry.getName());

					builder.setMessage("Set new value!");

					builder.setIcon(R.drawable.build);


					final EditText input = new EditText(v.getContext());
					input.setText(tmpEntry.getValue());
					input.selectAll();
					input.setGravity(Gravity.CENTER_HORIZONTAL);
					input.requestFocus();

					builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								try{
								String bp = FileUtils.readFileToString(new File("/system/build.prop"));
								bp = bp.replace((CharSequence)tmpEntry.getName().trim()+"="+tmpEntry.getValue().trim(), (CharSequence)tmpEntry.getName().trim()+"="+input.getText().toString().trim());
								FileOutputStream fOut = openFileOutput("build.prop",
										MODE_PRIVATE);
								OutputStreamWriter osw = new OutputStreamWriter(fOut);
								osw.write(bp);
								osw.flush();
								osw.close();
								RootExecuter.exec(new String[]{"/data/data/rs.pedjaapps.KernelTuner/files/cp /system/build.prop /system/build.prop.bk\n",
										"/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/build.prop /system/build.prop\n",
										"chmod 644 /system/build.prop\n"});
								Toast.makeText(BuildpropEditor.this, "build.prop edited successfully", Toast.LENGTH_LONG).show();
								}
								catch(Exception e){
									Log.e("",e.getMessage());
									Toast.makeText(BuildpropEditor.this, "error ocured:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
								}
								
								bAdapter.remove(tmpEntry);
								bAdapter.insert(new BuildEntry(tmpEntry.getName(), input.getText().toString()), pos);
								bAdapter.notifyDataSetChanged();

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


		new GetBuildEntries().execute();

	}


	private class GetBuildEntries extends AsyncTask<String, BuildEntry, Void>
	{
		String line;
		@Override
		protected Void doInBackground(String... args)
		{
			entries = new ArrayList<BuildEntry>();
			//Process proc = null;
			try
			{
			//	proc = Runtime.getRuntime().exec(getFilesDir().getPath() + "/toolbox getprop");
			
				File myFile = new File("/system/build.prop");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(fIn));
				while ((line = bufferedReader.readLine()) != null)
				{
					
					if(!line.startsWith("#") && !line.startsWith(" ") && line.length()!=0){
						String[] temp = line.trim().split("=");
						List<String> tmp = Arrays.asList(temp);
						BuildEntry tmpEntry;
						if(tmp.size()==2){
							tmpEntry = new BuildEntry(tmp.get(0), tmp.get(1));
						}
						else{
					        tmpEntry = new BuildEntry(tmp.get(0), "");
					    }
						entries.add(tmpEntry);
						publishProgress(tmpEntry);
					}

				}
				bufferedReader.close();
			}
			catch (Exception e)
			{
				Log.e("Get build prop", "error: " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(BuildEntry... values)
		{
			
				bAdapter.add(values[0]);
				bAdapter.notifyDataSetChanged();
			
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res)
		{
			setProgressBarIndeterminateVisibility(false);
		}
		@Override
		protected void onPreExecute()
		{
			setProgressBarIndeterminateVisibility(true);
			bAdapter.clear();
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,0,0,"Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0,1,1,"Backup").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0,2,2,"Restore").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
		com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, KernelTuner.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

				return true;
			case 0:
				addDialog();
				return true;
			case 1:
				backup();
				return true;
			case 2:
				restore();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}
	
	private void addDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Add new entrie");

		//builder.setMessage("Set new value!");

		builder.setIcon(R.drawable.build);

		LayoutInflater inflater = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.build_add_layout, null);
	    final EditText key = (EditText)view.findViewById(R.id.key);
		final EditText value = (EditText)view.findViewById(R.id.value);

		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					RootExecuter.exec(new String[]{"echo "+key.getText().toString()+"="+value.getText().toString()+" >> /system/build.prop"});
					bAdapter.add(new BuildEntry(key.getText().toString(), value.getText().toString()));
					bAdapter.notifyDataSetChanged();
				}
			});
		builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{


				}

			});
		builder.setView(view);

		AlertDialog alert = builder.create();

		alert.show();
	}
	
	private void backup()
	{
		try{
		//RootExecuter.exec(new String[]{"cp /system/build.prop "+Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build.prop"});
		FileUtils.copyFile(new File("/system/build.prop"), new File(Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build/build.prop-"+Tools.msToDateSimple(System.currentTimeMillis())));
		Toast.makeText(this, "build.prop backed-up in "+Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build/", Toast.LENGTH_LONG).show();
		}
		catch(Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void restore()
	{
		File backupDir = new File(Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build/");
		File[] backups = backupDir.listFiles();
		List<CharSequence> items = new ArrayList<CharSequence>();
		for(File f : backups){
			items.add(f.getName());
		}
		final CharSequence[] items2;
		items2 = items.toArray(new String[0]);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Backup to Restore");
		builder.setItems(items2, new DialogInterface.OnClickListener() {
		    @Override
			public void onClick(DialogInterface dialog, int item) {
		    	RootExecuter.exec(new String[]{"/data/data/rs.pedjaapps.KernelTuner/files/cp "+ Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build/"+items2[item]+" /system/build.prop\n",
		    			"chmod 644 /system/build.prop\n"});
		    	Toast.makeText(BuildpropEditor.this, "build.prop restored", Toast.LENGTH_LONG).show();
		    	new GetBuildEntries().execute();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
