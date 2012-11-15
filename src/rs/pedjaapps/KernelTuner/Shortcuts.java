package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class Shortcuts extends Activity
{

ShortcutAdapter shortcutAdapter;
ListView shortcutListView;
	String[] titles = {"Reboot",
		"Reboot - Recovery",
		"Reboot - Bootloader",
		"CPU Tweaks",
		"Times in State", 
		"Voltage",
		"Governor Settings",
		"mpdecision",
		"thermald",
		"GPU",
		"Misc Tweaks",
		"Profiles",
		"Swap",
		"System Info",
		"Settings"};
	String[] descs ={"Normal Reboot", 
		"Reboot device in recovery mode",
		"Reboot device in bootloader", 
		"Start CPU Tweaks",
		"View CPU Times in State", 
		"Change CPU Voltage Setting",
		"Change Governor Settings",
		"Manage mpdecision",
		"Manage thermald",
		"Overclock GPU",
		"Start Misc Tweaks",
		"Manage Settings Profiles",
		"Create and manage Swap",
		"View System Information",
		"Change app Settings" };

	int[] icons = {R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher, 
		R.drawable.times, 
		R.drawable.voltage, 
		R.drawable.dual,
		R.drawable.dual,
		R.drawable.temp,
		R.drawable.gpu,
		R.drawable.misc,
		R.drawable.profile,
		R.drawable.swap,
		R.drawable.info,
		R.drawable.misc};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shortcuts_list);
	shortcutListView = (ListView) findViewById(R.id.list);
		shortcutAdapter = new ShortcutAdapter(this, R.layout.shortcut_list_item);
		shortcutListView.setAdapter(shortcutAdapter);

		for (final ShortcutEntry entry : getShortcutEntries())
		{
			shortcutAdapter.add(entry);
		}
		
	
		
		shortcutListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
				{

				/*	
*/                 if(position==0){
	Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//repeat to create is forbidden
	shortcutintent.putExtra("duplicate", false);
	//set the name of shortCut
	shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, titles[position]);
	//set icon
	Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), icons[position]);
	shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	//set the application to lunch when you click the icon
	shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Shortcuts.this , CPUActivity.class));
	//sendBroadcast,done
	sendBroadcast(shortcutintent);
	//Toast.makeText(Shortcuts.this, "Shortcut "+titles[position] + "created", Toast.LENGTH_SHORT);
				/*	Intent shortcutIntent = new Intent();

					shortcutIntent.setClassName(Shortcuts.this, this.getClass().getName());
					shortcutIntent.setAction(Intent.ACTION_MAIN);
					shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);      
					shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				/*	if (toAddress != null) shortcutIntent.putExtra("toAddress", toAddress);
					if (fromAddress != null) shortcutIntent.putExtra("fromAddress", fromAddress);

					if (toCoords != null) shortcutIntent.putExtra("toCoords", toCoords);
					if (fromCoords != null) shortcutIntent.putExtra("fromCoords", fromCoords);
*/
				/*	Intent intent = new Intent();
					intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
					intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "name");

//    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					//      Intent.ShortcutIconResource.fromContext(context,
					//        R.drawable.icon));

					intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
					Shortcuts.this.sendBroadcast(intent); 
				*/	}
					if(position==1){
						Intent myIntent = new Intent(Shortcuts.this, test.class);
						Shortcuts.this.startActivity(myIntent);
					}
				} 
			});
	}
	
	private List<ShortcutEntry> getShortcutEntries()
	{

		final List<ShortcutEntry> entries = new ArrayList<ShortcutEntry>();
		
		
		for(int i =0; i < titles.length; i++){
			entries.add(new ShortcutEntry(titles[i],descs[i],icons[i]));
		}
		    		
		    		


		return entries;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.shortcuts_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		

		if (item.getItemId() == R.id.done)
		{
			finish();
		}

		return super.onOptionsItemSelected(item);
		
		}
}
