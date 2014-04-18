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

public class KernelTuner extends Activity implements Runnable
{

	private List<String> voltages = new ArrayList<String>();
	private long mLastBackPressTime = 0;
	private Toast mToast;

	private AlertDialog alert;

	private Button gpu;
	private Button cpu;
	private Button tis;
	private Button voltage;
	private Button mp;
	private Button thermal;

	MainApp app;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/**
		 * Declare buttons and set onClickListener for each
		 */

		initialCheck();
		if (preferences.getBoolean("notificationService", false)
				&& !isNotificationServiceRunning())
		{
			startService(new Intent(c, NotificationService.class));
		}
		else if (!preferences.getBoolean("notificationService", false)
				&& isNotificationServiceRunning())
		{
			stopService(new Intent(c, NotificationService.class));
		}
	}

	@Override
	protected void onResume()
	{

		/**
		 * I init.d is selected for restore settings on boot make inid.d files
		 * else remove them
		 */
		String boot = preferences.getString("boot", "init.d");
		if (boot.equals("init.d"))
		{
			Tools.exportInitdScripts(c, voltages);
		}
		else
		{
			new Initd(this).execute("rm");
		}

		super.onResume();

	}

	private void initialCheck()
	{

		String dOpt = preferences
				.getString("unsupported_items_display", "hide");
		/**
		 * Show/hide certain Views depending on number of cpus
		 */
		if (!minimal)
		{
			RelativeLayout cpu1ProgLayout = (RelativeLayout) findViewById(R.id.cpu1ProgLayout);
			RelativeLayout cpu2ProgLayout = (RelativeLayout) findViewById(R.id.cpu2ProgLayout);
			RelativeLayout cpu3ProgLayout = (RelativeLayout) findViewById(R.id.cpu3ProgLayout);
			if (IOHelper.cpu1Exists())
			{

				if (dOpt.equals("hide"))
					cpu1toggle.setVisibility(View.VISIBLE);
				else
					cpu1toggle.setEnabled(true);

				cpu1ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{

				if (dOpt.equals("hide"))
					cpu1toggle.setVisibility(View.GONE);
				else
					cpu1toggle.setEnabled(false);

				cpu1ProgLayout.setVisibility(View.GONE);
			}
			if (IOHelper.cpu2Exists())
			{

				if (dOpt.equals("hide"))
					cpu2toggle.setVisibility(View.VISIBLE);
				else
					cpu2toggle.setEnabled(true);

				cpu2ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{

				if (dOpt.equals("hide"))
					cpu2toggle.setVisibility(View.GONE);
				else
					cpu2toggle.setEnabled(false);

				cpu2ProgLayout.setVisibility(View.GONE);
			}
			if (IOHelper.cpu3Exists())
			{

				if (dOpt.equals("hide"))
					cpu3toggle.setVisibility(View.VISIBLE);
				else
					cpu3toggle.setEnabled(true);

				cpu3ProgLayout.setVisibility(View.VISIBLE);

			}
			else
			{
				if (dOpt.equals("hide"))
					cpu3toggle.setVisibility(View.GONE);
				else
					cpu3toggle.setEnabled(false);

				cpu3ProgLayout.setVisibility(View.GONE);
			}

		}
		/**
		 * Check for certain files in sysfs and if they doesnt exists hide
		 * depending views
		 */
		if (!(new File(Constants.CPU0_FREQS).exists()))
		{
			if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
			{
				if (dOpt.equals("hide"))
				{
					cpu.setVisibility(View.GONE);
				}
				else
				{
					cpu.setEnabled(false);
				}
			}
		}
		if (!(new File(Constants.VOLTAGE_PATH).exists()))
		{
			if (!(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()))
			{
				if (dOpt.equals("hide"))
				{
					voltage.setVisibility(View.GONE);
				}
				else
				{
					voltage.setEnabled(false);
				}
			}
		}
		if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
		{
			if (dOpt.equals("hide"))
			{
				tis.setVisibility(View.GONE);
			}
			else
			{
				tis.setEnabled(false);
			}
		}
		if (!(new File(Constants.MPDECISION).exists()))
		{
			if (dOpt.equals("hide"))
			{
				mp.setVisibility(View.GONE);
			}
			else
			{
				mp.setEnabled(false);
			}
		}
		if (!(new File(Constants.THERMALD).exists()))
		{
			if (dOpt.equals("hide"))
			{
				thermal.setVisibility(View.GONE);
			}
			else
			{
				thermal.setEnabled(false);
			}
		}
		if (!(new File(Constants.GPU_3D).exists()))
		{
			if (!(new File(Constants.GPU_SGX540).exists()))
			{
			if (dOpt.equals("hide"))
			{
				gpu.setVisibility(View.GONE);
			}
			else
			{
				gpu.setEnabled(false);
			}
			}
		}

	}

	/**
	 * Update UI with current frequency
	 */


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(1, 1, 1, "Settings")
				.setIcon(R.drawable.settings_dark)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(2, 2, 2, "Compatibility Check").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(3, 3, 3, "Swap")
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getItemId() == 1)
		{

			startActivity(new Intent(c, Preferences.class));

		}
		else if (item.getItemId() == 2)
		{
			startActivity(new Intent(c, CompatibilityCheck.class));
		}
		else if (item.getItemId() == 3)
		{
			Intent myIntent = new Intent(c, Swap.class);
			startActivity(myIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Press back button twice to exit application
	 */
	@Override
	public void onBackPressed()
	{
		if (!minimal)
		{
			if (mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000)
			{
				mToast = Toast.makeText(c,
						getResources().getString(R.string.press_again_to_exit),
						Toast.LENGTH_SHORT);
				mToast.show();
				mLastBackPressTime = java.lang.System.currentTimeMillis();
			}
			else
			{
				if (mToast != null)
					mToast.cancel();
				KernelTuner.this.finish();
				// java.lang.System.exit(0);
				mLastBackPressTime = 0;
			}
		}
		else
		{
			KernelTuner.this.finish();
			// java.lang.System.exit(0);
		}
	}

	private boolean isNotificationServiceRunning()
	{
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE))
		{
			if (NotificationService.class.getName().equals(
					service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context arg0, Intent intent)
		{

			double temperature = intent.getIntExtra(
					BatteryManager.EXTRA_TEMPERATURE, 0) / 10;

			if (tempPref.equals("fahrenheit"))
			{
				temperature = (temperature * 1.8) + 32;
				batteryTemp.setText(((int) temperature) + "°F");
				if (temperature <= 104)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 104 && temperature < 131)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 131 && temperature < 140)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 140)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}

			else if (tempPref.equals("celsius"))
			{
				batteryTemp.setText(temperature + "°C");
				if (temperature < 45)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 45 && temperature < 55)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 55 && temperature < 60)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 60)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}
			else if (tempPref.equals("kelvin"))
			{
				temperature = temperature + 273.15;
				batteryTemp.setText(temperature + "°K");
				if (temperature < 318.15)
				{
					batteryTemp.setTextColor(Color.GREEN);

				}
				else if (temperature > 318.15 && temperature < 328.15)
				{
					batteryTemp.setTextColor(Color.YELLOW);

				}
				else if (temperature >= 328.15 && temperature < 333.15)
				{
					batteryTemp.setTextColor(Color.RED);

				}
				else if (temperature >= 333.15)
				{

					batteryTemp.setTextColor(Color.RED);

				}
			}
			// /F = (C x 1.8) + 32

		}
	};
}
