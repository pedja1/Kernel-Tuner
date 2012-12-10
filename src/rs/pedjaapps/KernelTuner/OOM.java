package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.lang.Process;

public class OOM extends Activity {

	SeekBar foregroundSeek;
	SeekBar visibleSeek;
	SeekBar secondarySeek;
	SeekBar hiddenSeek;
	SeekBar contentSeek;
	SeekBar emptySeek;
	Button foregroundText;
	Button visibleText;
	Button secondaryText;
	Button hiddenText;
	Button contentText;
	Button emptyText;

	int foreground;
	int visible;
	int secondary;
	int hidden;
	int content;
	int empty;
	List<String> oom;

	ProgressDialog pd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oom);
		oom = CPUInfo.oom();
		

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
				Dialog("Foreground Application", String.valueOf(foreground), 0);
			}
			
		});
		visibleText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Visible Application", String.valueOf(foreground), 1);
			}
			
		});
		secondaryText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Secondary Server", String.valueOf(foreground), 2);
			}
			
		});
		hiddenText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Hidden Application", String.valueOf(foreground), 3);
			}
			
		});
		contentText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Content Provider", String.valueOf(foreground), 4);
			}
			
		});
		emptyText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Dialog("Empty Application", String.valueOf(foreground), 5);
			}
			
		});
		
		
		foregroundSeek
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar p1, int pos,
							boolean p3) {
						foregroundText.setText(String.valueOf(pos) + "MB");

					}

					public void onStartTrackingTouch(SeekBar p1) {

					}

					public void onStopTrackingTouch(SeekBar p1) {
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(contentSeek.getProgress()),
								mbToPages(emptySeek.getProgress()) });

					}

				});

		visibleSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				visibleText.setText(String.valueOf(pos) + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						mbToPages(foregroundSeek.getProgress()),
						mbToPages(visibleSeek.getProgress()),
						mbToPages(secondarySeek.getProgress()),
						mbToPages(hiddenSeek.getProgress()),
						mbToPages(contentSeek.getProgress()),
						mbToPages(emptySeek.getProgress()) });
			}

		});

		secondarySeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				secondaryText.setText(String.valueOf(pos) + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						mbToPages(foregroundSeek.getProgress()),
						mbToPages(visibleSeek.getProgress()),
						mbToPages(secondarySeek.getProgress()),
						mbToPages(hiddenSeek.getProgress()),
						mbToPages(contentSeek.getProgress()),
						mbToPages(emptySeek.getProgress()) });
			}

		});

		hiddenSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				hiddenText.setText(String.valueOf(pos) + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						mbToPages(foregroundSeek.getProgress()),
						mbToPages(visibleSeek.getProgress()),
						mbToPages(secondarySeek.getProgress()),
						mbToPages(hiddenSeek.getProgress()),
						mbToPages(contentSeek.getProgress()),
						mbToPages(emptySeek.getProgress()) });
			}

		});

		contentSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				contentText.setText(String.valueOf(pos) + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						mbToPages(foregroundSeek.getProgress()),
						mbToPages(visibleSeek.getProgress()),
						mbToPages(secondarySeek.getProgress()),
						mbToPages(hiddenSeek.getProgress()),
						mbToPages(contentSeek.getProgress()),
						mbToPages(emptySeek.getProgress()) });
			}

		});

		emptySeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar p1, int pos, boolean p3) {
				emptyText.setText(String.valueOf(pos) + "MB");

			}

			public void onStartTrackingTouch(SeekBar p1) {

			}

			public void onStopTrackingTouch(SeekBar p1) {
				new setOOM().execute(new String[] {
						mbToPages(foregroundSeek.getProgress()),
						mbToPages(visibleSeek.getProgress()),
						mbToPages(secondarySeek.getProgress()),
						mbToPages(hiddenSeek.getProgress()),
						mbToPages(contentSeek.getProgress()),
						mbToPages(emptySeek.getProgress()) });
			}

		});

	}

	public void updateUI() {
		try{
		foreground = Integer.parseInt(oom.get(0).trim()) * 4 / 1024;
		visible = Integer.parseInt(oom.get(1).trim()) * 4 / 1024;
		secondary = Integer.parseInt(oom.get(2).trim()) * 4 / 1024;
		hidden = Integer.parseInt(oom.get(3).trim()) * 4 / 1024;
		content = Integer.parseInt(oom.get(4).trim()) * 4 / 1024;
		empty = Integer.parseInt(oom.get(5).trim()) * 4 / 1024;
		}
		catch(NumberFormatException e){
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

		foregroundText.setText(String.valueOf(foreground) + "MB");
		visibleText.setText(String.valueOf(visible) + "MB");
		secondaryText.setText(String.valueOf(secondary) + "MB");
		hiddenText.setText(String.valueOf(hidden) + "MB");
		contentText.setText(String.valueOf(content) + "MB");
		emptyText.setText(String.valueOf(empty) + "MB");
	}

	private class setOOM extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... args) {
			Process localProcess;
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
				localDataOutputStream
						.writeBytes("echo "
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
								+ " > /sys/module/lowmemorykiller/parameters/minfree\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
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
				oom = CPUInfo.oom();
				System.out.println("OOM: Changing oom");
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
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

	public String mbToPages(int progress) {
		String prog = String.valueOf(progress * 1024 / 4);
		return prog;
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

		}
		return false;
	}
	
	public void Dialog(String dialogTitle, String currentValue, final int option){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(dialogTitle);

		builder.setMessage(getResources().getString(R.string.gov_new_value));

		builder.setIcon(R.drawable.ic_menu_edit);


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
								mbToPages(Integer.parseInt(input.getText().toString())),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(contentSeek.getProgress()),
								mbToPages(emptySeek.getProgress()) });
						break;
					case 1:
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(Integer.parseInt(input.getText().toString())),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(contentSeek.getProgress()),
								mbToPages(emptySeek.getProgress()) });
						break;
					case 2:
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(Integer.parseInt(input.getText().toString())),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(contentSeek.getProgress()),
								mbToPages(emptySeek.getProgress()) });
						break;
					case 3:
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(Integer.parseInt(input.getText().toString())),
								mbToPages(contentSeek.getProgress()),
								mbToPages(emptySeek.getProgress()) });
						break;
					case 4:
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(Integer.parseInt(input.getText().toString())),
								mbToPages(emptySeek.getProgress()) });
						break;
					case 5:
						new setOOM().execute(new String[] {
								mbToPages(foregroundSeek.getProgress()),
								mbToPages(visibleSeek.getProgress()),
								mbToPages(secondarySeek.getProgress()),
								mbToPages(hiddenSeek.getProgress()),
								mbToPages(contentSeek.getProgress()),
								mbToPages(Integer.parseInt(input.getText().toString())) });
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
