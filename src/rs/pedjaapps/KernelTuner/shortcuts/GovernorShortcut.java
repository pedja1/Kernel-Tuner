package rs.pedjaapps.KernelTuner.shortcuts;

import rs.pedjaapps.KernelTuner.GovernorActivity;
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

public class GovernorShortcut extends Activity
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
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Governor Settings");
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.dual);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		//set the application to lunch when you click the icon
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(GovernorShortcut.this , GovernorActivity.class));
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(GovernorShortcut.this, "Shortcut GovernorSettings created", Toast.LENGTH_SHORT).show();
		finish();
	}
}
