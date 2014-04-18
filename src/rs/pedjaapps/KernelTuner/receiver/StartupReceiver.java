/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.utility.AppReset;


public class StartupReceiver extends BroadcastReceiver
{
	SharedPreferences sharedPrefs;
	private boolean isNewKernel(){
		boolean newKernel = false;
		String savedKernel = sharedPrefs.getString("kernel", "");
		if(!savedKernel.equals("")){
			if(!(savedKernel.equals(IOHelper.kernel()))){
				
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
