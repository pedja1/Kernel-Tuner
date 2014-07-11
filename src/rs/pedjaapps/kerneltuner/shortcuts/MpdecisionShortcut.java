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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.Toast;
import java.io.File;
import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.ui.Mpdecision;
import rs.pedjaapps.kerneltuner.ui.TISActivity;
import rs.pedjaapps.kerneltuner.ui.TISActivityChart;

public class MpdecisionShortcut extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Context c = getApplicationContext();
	    String mActivity= "";
    	if(new File(Constants.MPDEC_THR_UP).exists()){
			mActivity = "rs.pedjaapps.KernelTuner.MPDECISION";
		}
		else if (new File(Constants.MPDEC_THR_0).exists()){
			mActivity = "rs.pedjaapps.KernelTuner.MPDECISION_NEW";
		}
		if(IOHelper.mpdecisionExists()){
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT")
		         .putExtra("duplicate", false)
				 .putExtra(Intent.EXTRA_SHORTCUT_NAME, "Mpdecision")
				 .putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(c, R.drawable.dual))
				 .putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(mActivity));
		sendBroadcast(shortcutintent);
		Toast.makeText(MpdecisionShortcut.this, "Shortcut Mpdecision created", Toast.LENGTH_SHORT).show();
		finish();
		}
		else{
			Toast.makeText(MpdecisionShortcut.this, "Your kernel doesnt support Mpdecision\nShortcut not created", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
