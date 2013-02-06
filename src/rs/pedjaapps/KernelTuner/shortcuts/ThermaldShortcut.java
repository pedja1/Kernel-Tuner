package rs.pedjaapps.KernelTuner.shortcuts;

import rs.pedjaapps.KernelTuner.helpers.CPUInfo;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.ui.Thermald;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

public class ThermaldShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(CPUInfo.thermaldExists()){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//repeat to create is forbidden
		shortcutintent.putExtra("duplicate", false);
		//set the name of shortCut
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Thermald");
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.temp);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		//set the application to lunch when you click the icon
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(ThermaldShortcut.this , Thermald.class));
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(ThermaldShortcut.this, "Shortcut Thermald created", Toast.LENGTH_SHORT).show();
		finish();
		}
		else{
			Toast.makeText(ThermaldShortcut.this, "Your kernel doesnt support Thermald\nShortcut not created", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
