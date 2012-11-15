package rs.pedjaapps.KernelTuner.shortcuts;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.Toast;
import rs.pedjaapps.KernelTuner.*;

public class TISShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//repeat to create is forbidden
		shortcutintent.putExtra("duplicate", false);
		//set the name of shortCut
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Times in State");
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.times);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		//set the application to lunch when you click the icon
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(TISShortcut.this , TISActivity.class));
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(TISShortcut.this, "Shortcut Times In State created", Toast.LENGTH_SHORT).show();
		finish();
	}
}
