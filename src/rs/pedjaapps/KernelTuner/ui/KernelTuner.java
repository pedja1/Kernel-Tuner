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

import android.app.*;
import android.app.ActivityManager.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.ads.*;
import com.stericson.RootTools.*;
import com.stericson.RootTools.execution.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.model.*;
import rs.pedjaapps.KernelTuner.helpers.*;
import rs.pedjaapps.KernelTuner.services.*;
import rs.pedjaapps.KernelTuner.utility.*;

import rs.pedjaapps.KernelTuner.Constants;

public class KernelTuner extends Activity
{
	@Override
	protected void onResume()
	{
		/**
		 * I init.d is selected for restore settings on boot make inid.d files
		 * else remove them
		 */
		/*String boot = preferences.getString("boot", "init.d");
		if (boot.equals("init.d"))
		{
			Tools.exportInitdScripts(c, voltages);
		}
		else
		{
			new Initd(this).execute("rm");
		}*/
		super.onResume();
	}
}
