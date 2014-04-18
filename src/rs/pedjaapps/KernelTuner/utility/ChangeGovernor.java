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
package rs.pedjaapps.KernelTuner.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.stericson.RootTools.execution.CommandCapture;
import com.stericson.RootTools.RootTools;


public class ChangeGovernor extends AsyncTask<String, Void, String>
{

	final Context context;

	public ChangeGovernor(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	final SharedPreferences preferences;
	
	@Override
	protected String doInBackground(String... args)
	{
		CommandCapture command = new CommandCapture(0, 
			"chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor", 
		    "echo " + args[1] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor");
		try{
		RootTools.getShell(true).add(command).waitForFinish();
		}
		catch(Exception e){
			
		}
		 SharedPreferences.Editor editor = preferences.edit();
			editor.putString(args[0] + "gov", args[1]);
			editor.commit();
		return "";
	}
}	

