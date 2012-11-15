package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import rs.pedjaapps.KernelTuner.*;

public class test extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	Intent shortcutIntent = new Intent();

	shortcutIntent.setClassName(test.this, this.getClass().getName());
	shortcutIntent.setAction(Intent.ACTION_MAIN);
	shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);      
	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	/*	if (toAddress != null) shortcutIntent.putExtra("toAddress", toAddress);
	 if (fromAddress != null) shortcutIntent.putExtra("fromAddress", fromAddress);

	 if (toCoords != null) shortcutIntent.putExtra("toCoords", toCoords);
	 if (fromCoords != null) shortcutIntent.putExtra("fromCoords", fromCoords);
	 */
	Intent intent = new Intent();
	intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "name");

//    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
	//      Intent.ShortcutIconResource.fromContext(context,
	//        R.drawable.icon));

	intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	test.this.sendBroadcast(intent); 
	}
}
