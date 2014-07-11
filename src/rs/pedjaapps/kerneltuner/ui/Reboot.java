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
package rs.pedjaapps.kerneltuner.ui;


import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



public class Reboot extends Activity
{
	private String reboot;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		reboot = intent.getExtras().getString("reboot");
		
		CommandCapture command = new CommandCapture(0,
            getFilesDir().getPath()+"/reboot " + reboot);
		try{
			RootTools.getShell(true).add(command);
		}
		catch(Exception e){

		}  
          
	}
}
