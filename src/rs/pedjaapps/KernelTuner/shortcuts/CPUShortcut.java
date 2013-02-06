package rs.pedjaapps.KernelTuner.shortcuts;

import rs.pedjaapps.KernelTuner.ui.CPUActivity;
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import rs.pedjaapps.KernelTuner.helpers.*;

public class CPUShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(CPUInfo.freqsExists() || CPUInfo.TISExists()){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//repeat to create is forbidden
		shortcutintent.putExtra("duplicate", false);
		//set the name of shortCut
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "CPUTweaks");
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		//set the application to lunch when you click the icon
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(CPUShortcut.this , CPUActivity.class));
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(CPUShortcut.this, "Shortcut CPUTweaks created", Toast.LENGTH_SHORT).show();
		finish();
		}
		else{
			Toast.makeText(CPUShortcut.this, "Your kernel doesnt support CPU Overclocking\nShortcut not created", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
