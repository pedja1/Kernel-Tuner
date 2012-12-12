package rs.pedjaapps.KernelTuner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class StartupReceiver extends BroadcastReceiver
{
	SharedPreferences sharedPrefs;
	private boolean isNewKernel(){
		boolean newKernel = false;
		String savedKernel = sharedPrefs.getString("kernel", "");
		if(!savedKernel.equals("")){
			if(!(savedKernel.equals(CPUInfo.kernel()))){
				
			}
		}
		return newKernel;
	}
	@Override
	public void onReceive(Context context, Intent intent)
	{

	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String applyonboot = sharedPrefs.getString("boot", "boot");
		boolean resetPref = sharedPrefs.getBoolean("reset", false);
		boolean notificationService = sharedPrefs.getBoolean("notificationService", false);
		if(isNewKernel() && resetPref){
			AppReset reset = new AppReset(context);
			reset.reset();
		}
		else{
		if (applyonboot.equals("boot"))
		{

			Intent serviceIntent = new Intent();
			serviceIntent.setAction("rs.pedjaapps.KernelTuner.StartupService");
			context.startService(serviceIntent);

		}
		}
		if (notificationService==true)
		{

			Intent serviceIntent = new Intent();
			serviceIntent.setAction("rs.pedjaapps.KernelTuner.NotificationService");
			context.startService(serviceIntent);

		}
	
	}
}
