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

    String arch = Tools.getAbi();
	@Override
	protected String doInBackground(String... args)
	{
		if (args[0].equals("apply"))
		{
			System.out.println("Init.d: Writing init.d");
		RootExecuter.exec(new String[]{
	            "mount -o remount,rw /system\n",
	            "/data/data/rs.pedjaapps.KernelTuner/files/"+arch+"/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktcputweaks /system/etc/init.d\n",
	            "chmod 777 /system/etc/init.d/99ktcputweaks\n",
				"/data/data/rs.pedjaapps.KernelTuner/files/"+arch+"/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktgputweaks /system/etc/init.d\n",
	            "chmod 777 /system/etc/init.d/99ktgputweaks\n",
				"/data/data/rs.pedjaapps.KernelTuner/files/"+arch+"/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktmisctweaks /system/etc/init.d\n",
	            "chmod 777 /system/etc/init.d/99ktmisctweaks\n",
				"/data/data/rs.pedjaapps.KernelTuner/files/"+arch+"/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktvoltage /system/etc/init.d\n",
	            "chmod 777 /system/etc/init.d/99ktvoltage\n",
				"/data/data/rs.pedjaapps.KernelTuner/files/"+arch+"/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktsysctl /system/etc/init.d\n",
				"chmod 777 /system/etc/init.d/99ktsysctl\n"});
	           
		}
		else if (args[0].equals("rm"))
		{
			System.out.println("Init.d: Deleting init.d");
		RootExecuter.exec(new String[]{
	            "mount -o remount,rw /system\n",
	            "rm /system/etc/init.d/99ktcputweaks\n",
	            "rm /system/etc/init.d/99ktgputweaks\n",
	            "rm /system/etc/init.d/99ktmisctweaks\n",
	            "rm /system/etc/init.d/99ktvoltage\n",
				"rm /system/etc/init.d/99ktsysctl\n"});
		}
		return "";
	}

}


