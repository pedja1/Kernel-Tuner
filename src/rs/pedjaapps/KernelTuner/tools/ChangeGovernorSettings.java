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


public class ChangeGovernorSettings extends AsyncTask<String, Void, String>
{

	final Context context;
	
	public ChangeGovernorSettings(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}


	final SharedPreferences preferences;

	@Override
	protected String doInBackground(String... args)
	{

	
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n").getBytes());
            stdin.write(("echo " + args[0].trim() + " > /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n").getBytes());
            
            stdin.flush();

            stdin.close();
            BufferedReader brCleanUp =
                    new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                Log.d("[KernelTuner ChangeGovernorSettings Output]", line);
            }
            brCleanUp.close();
            brCleanUp =
                    new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
            	Log.e("[KernelTuner ChangeGovernorSettings Error]", line);
            }
            brCleanUp.close();

        } catch (IOException ex) {
        }
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[2] + "_" + args[1], args[0]);
		editor.commit();

		return "";
	}
}	

