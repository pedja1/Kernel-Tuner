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
package rs.pedjaapps.KernelTuner.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class Reboot extends Activity
{
	private String reboot;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		reboot = intent.getExtras().getString("reboot");
		
		
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write((getFilesDir().getPath()+"/reboot " + reboot + "\n").getBytes());
            stdin.write("exit\n".getBytes());
            stdin.flush();

            stdin.close();
            BufferedReader brCleanUp =
                    new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                Log.d("[KernelTuner ChangeGovernor Output]", line);
            }
            brCleanUp.close();
            brCleanUp =
                    new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
            	Log.e("[KernelTuner ChangeGovernor Error]", line);
            }
            brCleanUp.close();
			process.waitFor();
			process.destroy();

        } catch (Exception ex) {
        }
	}
}
