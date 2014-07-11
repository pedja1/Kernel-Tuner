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
package rs.pedjaapps.kerneltuner.shortcuts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.ui.GovernorActivity;
import rs.pedjaapps.kerneltuner.ui.TaskManager;

public class TmShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Context c = getApplicationContext();
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT")
		         .putExtra("duplicate", false)
				 .putExtra(Intent.EXTRA_SHORTCUT_NAME, "Task Manager")
				 .putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(c, R.drawable.tm))
			.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent("rs.pedjaapps.KernelTuner.TM"));
		sendBroadcast(shortcutintent);
		Toast.makeText(this, "Shortcut Task Manager created", Toast.LENGTH_SHORT).show();
		finish();
	}
}
