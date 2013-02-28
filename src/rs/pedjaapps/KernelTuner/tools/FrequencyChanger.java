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
package rs.pedjaapps.KernelTuner.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class FrequencyChanger extends AsyncTask<String, Void, String>
{
	Context context;

	public FrequencyChanger(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	final SharedPreferences preferences;
	@Override
	protected String doInBackground(String... args)
	{
	RootExecuter.exec(new String[]{
            "chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n",
            "echo " + args[2] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n",
            "chmod 444 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n",
            "chown system /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n"});
          
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[0] + args[1], args[2]);
		editor.commit();
		return "";
	}
}	

