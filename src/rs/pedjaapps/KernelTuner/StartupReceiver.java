package rs.pedjaapps.KernelTuner;
import android.content.*;
import android.preference.*;


public class StartupReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{

	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String applyonboot = sharedPrefs.getString("boot", "");
		if (applyonboot.equals("boot"))
		{

			Intent serviceIntent = new Intent();
			serviceIntent.setAction("rs.pedjaapps.KernelTuner.StartupService");
			context.startService(serviceIntent);

		}
	
	}
}
