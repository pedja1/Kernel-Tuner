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

import android.os.AsyncTask;
import android.util.Log;

public class Initd extends AsyncTask<String, Void, String>
{


	@Override
	protected String doInBackground(String... args)
	{

		
		if (args[0].equals("apply"))
		{
			System.out.println("Init.d: Writing init.d");
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("mount -o remount,rw /system\n").getBytes());
	            stdin.write(("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktcputweaks /system/etc/init.d\n").getBytes());
	            stdin.write(("chmod 777 /system/etc/init.d/99ktcputweaks\n").getBytes());
	            stdin.write(("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktgputweaks /system/etc/init.d\n").getBytes());
	            stdin.write(("chmod 777 /system/etc/init.d/99ktgputweaks\n").getBytes());
	            stdin.write(("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktmisctweaks /system/etc/init.d\n").getBytes());
	            stdin.write(("chmod 777 /system/etc/init.d/99ktmisctweaks\n").getBytes());
	            stdin.write(("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktvoltage /system/etc/init.d\n").getBytes());
	            stdin.write(("chmod 777 /system/etc/init.d/99ktvoltage\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Init.d Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Init.d Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }
		}
		else if (args[0].equals("rm"))
		{
			System.out.println("Init.d: Deleting init.d");
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("mount -o remount,rw /system\n").getBytes());
	            stdin.write(("rm /system/etc/init.d/99ktcputweaks\n").getBytes());
	            stdin.write(("rm /system/etc/init.d/99ktgputweaks\n").getBytes());
	            stdin.write(("rm /system/etc/init.d/99ktmisctweaks\n").getBytes());
	            stdin.write(("rm /system/etc/init.d/99ktvoltage\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Init.d Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Init.d Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }
		}
		return "";
	}

}


