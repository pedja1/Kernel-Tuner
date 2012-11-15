package rs.pedjaapps.KernelTuner.shortcuts;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.Toast;
import rs.pedjaapps.KernelTuner.*;

public class RebootShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String reboot = intent.getExtras().getString("reboot");
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//repeat to create is forbidden
		shortcutintent.putExtra("duplicate", false);
		//set the name of shortCut
		if(reboot.equals("recovery")){
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Recovery");
		}
		else if(reboot.equals("bootloader")){
			shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Bootloader");
		}
		else{
			shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Reboot");
		}
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.reboot);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		
		
		
		
		//set the application to lunch when you click the icon
		Intent intent2 = new Intent(RebootShortcut.this , Reboot.class);
		intent2.putExtra("reboot", reboot);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(RebootShortcut.this, "Shortcut Reboot created", Toast.LENGTH_SHORT).show();
		finish();
	}
}
