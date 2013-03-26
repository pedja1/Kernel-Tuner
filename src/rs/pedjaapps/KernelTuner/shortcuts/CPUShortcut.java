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
package rs.pedjaapps.KernelTuner.shortcuts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.ui.CPUActivity;
import rs.pedjaapps.KernelTuner.ui.CPUActivityOld;

public class CPUShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Context c = getApplicationContext();
		
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
	    String cpuPref = prefs.getString("show_cpu_as","full");
	    Class cpuActivity= null;
    	if(cpuPref.equals("full")){
			cpuActivity = CPUActivity.class;
		}
		else{
			cpuActivity = CPUActivityOld.class;
		}
		if(IOHelper.freqsExists() || IOHelper.TISExists()){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT")
		        .putExtra("duplicate", false)
				.putExtra(Intent.EXTRA_SHORTCUT_NAME, "CPUTweaks")
				.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(c, R.drawable.ic_launcher))
				.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(c, cpuActivity));
		sendBroadcast(shortcutintent);
		Toast.makeText(CPUShortcut.this, "Shortcut CPUTweaks created", Toast.LENGTH_SHORT).show();
		finish();
		}
		else{
			Toast.makeText(CPUShortcut.this, "Your kernel doesnt support CPU Overclocking\nShortcut not created", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
