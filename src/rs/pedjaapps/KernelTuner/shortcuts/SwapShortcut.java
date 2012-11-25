package rs.pedjaapps.KernelTuner.shortcuts;

import rs.pedjaapps.KernelTuner.CPUInfo;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.Swap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

public class SwapShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(CPUInfo.swapsExists()){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//repeat to create is forbidden
		shortcutintent.putExtra("duplicate", false);
		//set the name of shortCut
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Swap");
		//set icon
		Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.swap);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		//set the application to lunch when you click the icon
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(SwapShortcut.this , Swap.class));
		//sendBroadcast,done
		sendBroadcast(shortcutintent);
		Toast.makeText(SwapShortcut.this, "Shortcut Swap created", Toast.LENGTH_SHORT).show();
		finish();
		}
		else{
			Toast.makeText(SwapShortcut.this, "Your kernel doesnt support Swaping\nShortcut not created", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
