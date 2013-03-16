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

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.ContextMenu.*;
import android.widget.*;
import android.widget.SeekBar.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.helpers.*;

import android.view.View.OnClickListener;
import java.lang.Process;
import rs.pedjaapps.KernelTuner.tools.Tools;

public class OOM extends Activity {

	private SeekBar foregroundSeek;
	private SeekBar visibleSeek;
	private SeekBar secondarySeek;
	private SeekBar hiddenSeek;
	private SeekBar contentSeek;
	private SeekBar emptySeek;
	private Button foregroundText;
	private Button visibleText;
	private Button secondaryText;
	private Button hiddenText;
	private Button contentText;
	private Button emptyText;

	private int foreground;
	private int visible;
	private int secondary;
	private int hidden;
	private int content;
	private int empty;
	private List<String> oom;

	private ProgressDialog pd;
	boolean isLight;
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String theme = preferences.getString("theme", "light");
		
		if (theme.equals("light")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Light);
		} 
		else if (theme.equals("dark")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar);
		} 
		else if (theme.equals("miui_light")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Miui_Light);
		} 
		else if (theme.equals("miui_dark")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Miui);
		} 
		else if (theme.equals("sense5")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Sense5);
		}
		else if (theme.equals("sense5_light")) 
		{
			setTheme(R.style.Theme_Translucent_NoTitleBar_Sense5_Light);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oom);
		oom = IOHelper.oom();
		

		foregroundSeek = (SeekBar) findViewById(R.id.foregroundSeek);
		foregroundText = (Button) findViewById(R.id.foregroundTest);
		visibleSeek = (SeekBar) findViewById(R.id.visibleSeek);
		visibleText = (Button) findViewById(R.id.visibleText);
		secondarySeek = (SeekBar) findViewById(R.id.secondarySeek);
		secondaryText = (Button) findViewById(R.id.secondaryText);
		hiddenSeek = (SeekBar) findViewById(R.id.hiddenSeek);
		hiddenText = (Button) findViewById(R.id.hiddenText);
		contentSeek = (SeekBar) findViewById(R.id.contentSeek);
		contentText = (Button) findViewById(R.id.contentText);
		emptySeek = (SeekBar) findViewById(R.id.emptySeek);
		emptyText = (Button) findViewById(R.id.emptyText);

		updateUI();

		Button  presets = (Button)findViewById(R.id.button1);
		presets.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				registerForContextMenu(arg0); 
			    openContextMenu(arg0);
			    unregisterForContextMenu(arg0);
				
			}
			
		});
		
		foregroundText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Foreground Application", foreground+"", 0);
			}
			
		});
		visibleText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Visible Application", foreground+"", 1);
			}
			
		});
		secondaryText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Secondary Server", foreground+"", 2);
			}
			
		});
		hiddenText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Hidden Application", foreground+"", 3);
			}
			
		});
		contentText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Content Provider", foreground+"", 4);
			}
			
		});
		emptyText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Empty Application", foreground+"", 5);
			}
			
		});
		
		
		foregroundSeek
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar p1, int pos,
							boolean p3) {
						foregroundText.setText(pos + "MB");
					}
					public void onStartTrackingTouch(SeekBar p1) {

					}

					public void onStopTrackingTouch(SeekBar p1) {
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(emptySeek.getProgress()) });

					}

				});

		visibleSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				visibleText.setText(pos + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						Tools.mbToPages(foregroundSeek.getProgress()),
						Tools.mbToPages(visibleSeek.getProgress()),
						Tools.mbToPages(secondarySeek.getProgress()),
						Tools.mbToPages(hiddenSeek.getProgress()),
						Tools.mbToPages(contentSeek.getProgress()),
						Tools.mbToPages(emptySeek.getProgress()) });
			}

		});

		secondarySeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				secondaryText.setText(pos + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						Tools.mbToPages(foregroundSeek.getProgress()),
						Tools.mbToPages(visibleSeek.getProgress()),
						Tools.mbToPages(secondarySeek.getProgress()),
						Tools.mbToPages(hiddenSeek.getProgress()),
						Tools.mbToPages(contentSeek.getProgress()),
						Tools.mbToPages(emptySeek.getProgress()) });
			}

		});

		hiddenSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				hiddenText.setText(pos + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						Tools.mbToPages(foregroundSeek.getProgress()),
						Tools.mbToPages(visibleSeek.getProgress()),
						Tools.mbToPages(secondarySeek.getProgress()),
						Tools.mbToPages(hiddenSeek.getProgress()),
						Tools.mbToPages(contentSeek.getProgress()),
						Tools.mbToPages(emptySeek.getProgress()) });
			}

		});

		contentSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				contentText.setText(pos + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						Tools.mbToPages(foregroundSeek.getProgress()),
						Tools.mbToPages(visibleSeek.getProgress()),
						Tools.mbToPages(secondarySeek.getProgress()),
						Tools.mbToPages(hiddenSeek.getProgress()),
						Tools.mbToPages(contentSeek.getProgress()),
						Tools.mbToPages(emptySeek.getProgress()) });
			}

		});

		emptySeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				emptyText.setText(pos + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						Tools.mbToPages(foregroundSeek.getProgress()),
						Tools.mbToPages(visibleSeek.getProgress()),
						Tools.mbToPages(secondarySeek.getProgress()),
						Tools.mbToPages(hiddenSeek.getProgress()),
						Tools.mbToPages(contentSeek.getProgress()),
						Tools.mbToPages(emptySeek.getProgress()) });
			}

		});

	}

	public final void updateUI(){
		try{
		foreground = Integer.parseInt(oom.get(0).trim()) * 4 / 1024;
		visible = Integer.parseInt(oom.get(1).trim()) * 4 / 1024;
		secondary = Integer.parseInt(oom.get(2).trim()) * 4 / 1024;
		hidden = Integer.parseInt(oom.get(3).trim()) * 4 / 1024;
		content = Integer.parseInt(oom.get(4).trim()) * 4 / 1024;
		empty = Integer.parseInt(oom.get(5).trim()) * 4 / 1024;
		}
		catch(Exception e){
			foreground=0;
			visible=0;
			secondary=0;
			hidden=0;
			content=0;
			empty=0;
		}
		foregroundSeek.setProgress(foreground);
		visibleSeek.setProgress(visible);
		secondarySeek.setProgress(secondary);
		hiddenSeek.setProgress(hidden);
		contentSeek.setProgress(content);
		emptySeek.setProgress(empty);

		foregroundText.setText(foreground + "MB");
		visibleText.setText(visible + "MB");
		secondaryText.setText(secondary + "MB");
		hiddenText.setText(hidden + "MB");
		contentText.setText(content + "MB");
		emptyText.setText(empty + "MB");
	}

	private class setOOM extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
		
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("echo "
						+ args[0]
						+ ","
						+ args[1]
						+ ","
						+ args[2]
						+ ","
						+ args[3]
						+ ","
						+ args[4]
						+ ","
						+ args[5]
						+ " > /sys/module/lowmemorykiller/parameters/minfree\n").getBytes());
		
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner ChangeGovernor Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner ChangeGovernor Error]", line);
	            }
	            brCleanUp.close();
	            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("oom", args[0]
						+ ","
						+ args[1]
						+ ","
						+ args[2]
						+ ","
						+ args[3]
						+ ","
						+ args[4]
						+ ","
						+ args[5]);
				editor.commit();
				oom = IOHelper.oom();
	        } catch (IOException ex) {
	        }
			
			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			
			updateUI();
			
			pd.dismiss();
		}

		@Override
		protected void onPreExecute() {
			
			pd = new ProgressDialog(OOM.this);
			pd.setMessage("Changing Out Of Memory values\nPlease wait...");
			pd.show();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Select Preset");
		menu.setHeaderIcon(R.drawable.swap);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.oom_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.very_light:
			new setOOM().execute(new String[] {
					"512",
					"1024",
					"1280",
					"2048",
					"3072",
					"4096" });
			return true;
		case R.id.light:
			new setOOM().execute(new String[] {
					"1024",
					"2048",
					"2560",
					"4096",
					"6144",
					"8192" });
			return true;
		case R.id.medium:
			new setOOM().execute(new String[] {
					"1024",
					"2048",
					"4096",
					"8192",
					"12288",
					"16384" });
			return true;

		case R.id.aggressive:
			new setOOM().execute(new String[] {
					"2048",
					"4096",
					"8192",
					"16384",
					"24576",
					"32768" });
			return true;

		case R.id.very_aggressive:
			new setOOM().execute(new String[] {
					"4096",
					"8192",
					"16384",
					"16384",
					"49152",
					"65536" });
			return true;
		case R.id._256_multitasking:
			new setOOM().execute(new String[] {
					"2048",
					"3072",
					"5632",
					"6144",
					"6656",
					"7168" });
			return true;
		case R.id._256_balanced:
			new setOOM().execute(new String[] {
					"2048",
					"3072",
					"6656",
					"7168",
					"7680",
					"8192" });
			return true;
		case R.id._256_aggressive:
			new setOOM().execute(new String[] {
					"2048",
					"3072",
					"7168",
					"7680",
					"8960",
					"12800" });
			return true;
			//512
		case R.id._512_multitasking:
			new setOOM().execute(new String[] {
					"2048",
					"3584",
					"10240",
					"12800",
					"15360",
					"19200" });
			return true;
		case R.id._512_balanced:
			new setOOM().execute(new String[] {
					"2048",
					"3584",
					"14080",
					"17920",
					"21760",
					"25600" });
			return true;
		case R.id._512_aggressive:
			new setOOM().execute(new String[] {
					"2048",
					"3584",
					"19200",
					"23040",
					"24320",
					"32000" });
			return true;
			
		case R.id._768_aggressive:
			new setOOM().execute(new String[] {
					"2048",
					"4096",
					"38400",
					"42240",
					"46080",
					"51200" });
			return true;
		case R.id._1000_aggressive:
			new setOOM().execute(new String[] {
					"2048",
					"4096",
					"51200",
					"56320",
					"61440",
					"65536" });
			return true;

		}
		return false;
	}
	
	private final void Dialog(String dialogTitle, String currentValue, final int option){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(dialogTitle);

		builder.setMessage(getResources().getString(R.string.gov_new_value));

		builder.setIcon(isLight ? R.drawable.edit_light : R.drawable.edit_dark);


		final EditText input = new EditText(this);
		input.setHint(currentValue);
		input.selectAll();
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setGravity(Gravity.CENTER_HORIZONTAL);

		builder.setPositiveButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					switch(option){
					case 0:
						new setOOM().execute(new String[] {
								Tools.mbToPages(Integer.parseInt(input.getText().toString())),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(emptySeek.getProgress()) });
						break;
					case 1:
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(Integer.parseInt(input.getText().toString())),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(emptySeek.getProgress()) });
						break;
					case 2:
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(Integer.parseInt(input.getText().toString())),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(emptySeek.getProgress()) });
						break;
					case 3:
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(Integer.parseInt(input.getText().toString())),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(emptySeek.getProgress()) });
						break;
					case 4:
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(Integer.parseInt(input.getText().toString())),
								Tools.mbToPages(emptySeek.getProgress()) });
						break;
					case 5:
						new setOOM().execute(new String[] {
								Tools.mbToPages(foregroundSeek.getProgress()),
								Tools.mbToPages(visibleSeek.getProgress()),
								Tools.mbToPages(secondarySeek.getProgress()),
								Tools.mbToPages(hiddenSeek.getProgress()),
								Tools.mbToPages(contentSeek.getProgress()),
								Tools.mbToPages(Integer.parseInt(input.getText().toString())) });
						break;
						
					}
					

					
					
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

}
