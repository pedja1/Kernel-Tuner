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
		boolean notificationService = sharedPrefs.getBoolean("notificationService", false);
		if (applyonboot.equals("boot"))
		{

			Intent serviceIntent = new Intent();
			serviceIntent.setAction("rs.pedjaapps.KernelTuner.StartupService");
			context.startService(serviceIntent);

		}
		
		if (notificationService==true)
		{

			Intent serviceIntent = new Intent();
			serviceIntent.setAction("rs.pedjaapps.KernelTuner.NotificationService");
			context.startService(serviceIntent);

		}
	
	}
}
