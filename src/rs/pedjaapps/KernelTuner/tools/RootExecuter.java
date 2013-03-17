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

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import rs.pedjaapps.KernelTuner.Constants;

public class RootExecuter
{
      public static void exec(String[] commands){
		  try {
			  String line;
			  Process process = Runtime.getRuntime().exec("su");
			  OutputStream stdin = process.getOutputStream();
			  InputStream stderr = process.getErrorStream();
			  InputStream stdout = process.getInputStream();
              for(String s : commands){
			  stdin.write((s).getBytes());
			  }
			  stdin.flush();

			  stdin.close();
			  BufferedReader brCleanUp =
				  new BufferedReader(new InputStreamReader(stdout));
			  while ((line = brCleanUp.readLine()) != null) {
				  Log.d(Constants.LOG_TAG, line);
			  }
			  brCleanUp.close();
			  brCleanUp =
				  new BufferedReader(new InputStreamReader(stderr));
			  while ((line = brCleanUp.readLine()) != null) {
				  Log.e(Constants.LOG_TAG, line);
			  }
			  brCleanUp.close();
			  if (process != null) {
				  process.getErrorStream().close();
				  process.getInputStream().close();
				  process.getOutputStream().close();
			  }

		  } catch (IOException ex) {
			  
		  }
	  }

}
