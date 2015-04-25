package rs.pedjaapps.kerneltuner.ui;


import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.io.*;

import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.constants.*;
import rs.pedjaapps.kerneltuner.fragments.*;
import rs.pedjaapps.kerneltuner.receiver.*;
import rs.pedjaapps.kerneltuner.root.*;
import rs.pedjaapps.kerneltuner.utility.*;

import android.support.v7.app.ActionBar;
import android.app.*;

/**
 * Created by pedja on 17.4.14..
 *
 * This file is part of Kernel Tuner
 * Copyright Predrag Čokulov 2014
 */
public class MainActivity extends AbsActivity implements Runnable, View.OnClickListener
{
    private long cpuRefreshInterval;
    int cpuTempPath;
    boolean hideUnsupportedItems;

    private long mLastBackPressTime = 0;
    private Toast mToast;

    private TextView tvCpu0prog;
    private TextView tvCpu1prog;
    private TextView tvCpu2prog;
    private TextView tvCpu3prog;

    private TextView tvBatteryTemp;
    private TextView tvCputemptxt;

    private LinearLayout llCpuTemp;

    Button cpu1toggle;
    Button cpu2toggle;
    Button cpu3toggle;

    private TextView tvCpuLoad;

    private TempUnit tempUnit;
    Handler cpuRefreshHandler;
    Handler uiHandler;
    Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		System.out.println("Main activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);
		
		uiHandler = new Handler();
		cpuTempPath = IOHelper.getCpuTempPath();
		
        cpuRefreshInterval = PrefsManager.getCpuRefreshInterval();
        final boolean firstLaunch = PrefsManager.isFirstLaunch();
        tempUnit = PrefsManager.getTempUnit();
        hideUnsupportedItems = PrefsManager.hideUnsupportedItems();

		Executor.getInstance().executeSingleTask(new Runnable()
		{
			@Override
			public void run()
			{
				mountDebugFileSystem();
				enableTmemperatureMonitor();
				if (firstLaunch)
				{
					logKernelInfo();
				}
			}
		});
        
        checkProVersion();

        setupView();
		
		System.out.println("Main activity onCreate end");

        //if(getSupportActionBar() != null)getSupportActionBar().setSubtitle(Build.MANUFACTURER + " " + Build.MODEL);
    }

    private void showByProDialog()
    {
        if(PrefsManager.isProDialogShown() || PrefsManager.isProVersion())return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.go_pro);
        builder.setMessage(R.string.go_pro_summary);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Utility.goPro(MainActivity.this);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(false);
        builder.show();
        PrefsManager.setProDialogShown(true);
    }

    private void checkProVersion()
    {
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... params)
            {
                if(Utility.isPackageInstalled(PackageChangeReceiver.PRO_PACKAGE_NAME, MainActivity.this))
                {
                    PrefsManager.setPro(true);
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent(ACTION_TOGGLE_PRO_VERSION));
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s)
            {
                showByProDialog();
            }
        }.execute();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause()
    {
        super.onStop();
        if (mBatInfoReceiver != null)
        {
            unregisterReceiver(mBatInfoReceiver);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (cpuRefreshHandler != null)
        {
            cpuRefreshHandler.removeCallbacks(this);
        }
        RootUtils.closeAllShells();
    }

    private void logKernelInfo()
    {
		PrefsManager.setKernelInfo();
    }

    private void enableTmemperatureMonitor()
    {
        if (!IOHelper.isTempEnabled())
        {
            new RootUtils().exec("chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode",
								 "chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode",
								 "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode",
								 "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode");
        }
    }

    private void mountDebugFileSystem()
    {
        File file = new File("/sys/kernel/debug");

        if (!file.exists() && file.list() != null && file.list().length == 0)
        {
            new RootUtils().exec("mount -t debugfs debugfs /sys/kernel/debug");
        }
    }

    public void setupView()
    {

        tvCpu0prog = (TextView) findViewById(R.id.txtCpu0);
        tvCpu1prog = (TextView) findViewById(R.id.txtCpu1);
        tvCpu2prog = (TextView) findViewById(R.id.txtCpu2);
        tvCpu3prog = (TextView) findViewById(R.id.txtCpu3);

        tvBatteryTemp = (TextView) findViewById(R.id.txtBatteryTemp);
        llCpuTemp = (LinearLayout) findViewById(R.id.temp_cpu_layout);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setSubtitle("Various kernel and system tuning");
        if(actionBar != null )actionBar.setHomeButtonEnabled(false);

        HandlerThread cpuRefreshThread = new HandlerThread("cpu_refresh_thread");
        cpuRefreshThread.start();
        cpuRefreshHandler = new Handler(cpuRefreshThread.getLooper());
        cpuRefreshHandler.postDelayed(this, cpuRefreshInterval);

        cpu1toggle = (Button) findViewById(R.id.btn_cpu1_toggle);
        cpu1toggle.setOnClickListener(this);

        cpu2toggle = (Button) findViewById(R.id.btn_cpu2_toggle);
        cpu2toggle.setOnClickListener(this);

        cpu3toggle = (Button) findViewById(R.id.btn_cpu3_toggle);
        cpu3toggle.setOnClickListener(this);

        tvCputemptxt = (TextView) findViewById(R.id.txtCpuTemp);
        tvCpuLoad = (TextView) findViewById(R.id.txtCpuLoadText);


        if (IOHelper.cpu1Exists())
        {
            if (hideUnsupportedItems)
                cpu1toggle.setVisibility(View.VISIBLE);
            else
                cpu1toggle.setEnabled(true);

            tvCpu1prog.setVisibility(View.VISIBLE);
        }
        else
        {
            if (hideUnsupportedItems)
                cpu1toggle.setVisibility(View.GONE);
            else
                cpu1toggle.setEnabled(false);

            tvCpu1prog.setVisibility(View.GONE);
        }
        if (IOHelper.cpu2Exists())
        {
            if (hideUnsupportedItems)
                cpu2toggle.setVisibility(View.VISIBLE);
            else
                cpu2toggle.setEnabled(true);

            tvCpu2prog.setVisibility(View.VISIBLE);
        }
        else
        {
            if (hideUnsupportedItems)
                cpu2toggle.setVisibility(View.GONE);
            else
                cpu2toggle.setEnabled(false);

            tvCpu2prog.setVisibility(View.GONE);
        }
        if (IOHelper.cpu3Exists())
        {
            if (hideUnsupportedItems)
                cpu3toggle.setVisibility(View.VISIBLE);
            else
                cpu3toggle.setEnabled(true);

            tvCpu3prog.setVisibility(View.VISIBLE);
        }
        else
        {
            if (hideUnsupportedItems)
                cpu3toggle.setVisibility(View.GONE);
            else
                cpu3toggle.setEnabled(false);

            tvCpu3prog.setVisibility(View.GONE);
        }
        currentFragment = MainFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.flFragmentContainer, currentFragment).commit();
		System.out.println("setupview");
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_cpu1_toggle:
                toggleCpu(view, 1);
                break;
            case R.id.btn_cpu2_toggle:
                toggleCpu(view, 2);
                break;
            case R.id.btn_cpu3_toggle:
                toggleCpu(view, 3);
                break;
        }
    }
	
	private void toggleCpu(final View view, int coreNum)
	{
		view.setEnabled(false);
		RCommand.toggleCpu(coreNum, new RootUtils.CommandCallbackImpl(){

				@Override
				public void onComplete(RootUtils.Status status, String output)
				{
					view.setEnabled(true);
				}
			});
	}

    @Override
    public void run()
    {
		System.out.println("refresh ...");
        String tmp = IOHelper.cpuTemp(cpuTempPath);
        cpuTemp(tmp);
        cpu0update(IOHelper.cpuCurFreq(0));
        

        if (IOHelper.cpu1Exists())
        {
            cpu1update(IOHelper.cpuCurFreq(1));
            
        }
        if (IOHelper.cpu2Exists())
        {
            cpu2update(IOHelper.cpuCurFreq(2));
            
        }
        if (IOHelper.cpu3Exists())
        {
            cpu3update(IOHelper.cpuCurFreq(3));
            
        }
        try
        {
            setCpuLoad(getCpuLoad());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        cpuRefreshHandler.postDelayed(this, cpuRefreshInterval);
    }

    private int getCpuLoad() throws IOException
    {
        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
        String sLoad = reader.readLine();

        String[] toks = sLoad.split(" ");

        long idle1 = Long.parseLong(toks[5]);
        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
                + Long.parseLong(toks[4]) + Long.parseLong(toks[6])
                + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

        try
        {
            Thread.sleep(360);
        }
        catch (Exception ignored)
        {
        }

        reader.seek(0);
        sLoad = reader.readLine();
        reader.close();

        toks = sLoad.split(" ");

        long idle2 = Long.parseLong(toks[5]);
        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
                + Long.parseLong(toks[4]) + Long.parseLong(toks[6])
                + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

        float fLoad = (float) (cpu2 - cpu1)
                / ((cpu2 + idle2) - (cpu1 + idle1));
        return (int) (fLoad * 100);
    }

    private void cpu0update(final String freq)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu0prog.setText(getString(R.string.txt_cpu0, freq.trim().substring(0, freq.length() - 3) + "MHz"));
                }
                else
                {
                    tvCpu0prog.setText(getString(R.string.txt_cpu0, "offline"));
                }
            }
        });
    }

    private void cpu1update(final String freq)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu1prog.setText(getString(R.string.txtCpu1, freq.trim().substring(0, freq.length() - 3) + "MHz"));
                }
                else
                {
                    tvCpu1prog.setText(getString(R.string.txtCpu1, "offline"));
                }
            }
        });
    }

    private void cpu2update(final String freq)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu2prog.setText(getString(R.string.txtCpu2, freq.trim().substring(0, freq.length() - 3) + "MHz"));
                }
                else
                {
                    tvCpu2prog.setText(getString(R.string.txtCpu2, "offline"));
                }
            }
        });
    }

    private void cpu3update(final String freq)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu3prog.setText(getString(R.string.txtCpu3, freq.trim().substring(0, freq.length() - 3) + "MHz"));
                }
                else
                {
                    tvCpu3prog.setText(getString(R.string.txtCpu3, "offline"));
                }
            }
        });
    }

    private void setCpuLoad(final int load)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                tvCpuLoad.setText(getString(R.string.txt_cpu_load, load + "%"));
            }
        });
    }

    /**
     * CPU Temperature
     */

    private void cpuTemp(final String cputemp)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                String tmpCputemp = cputemp;
                llCpuTemp.setVisibility(View.VISIBLE);

                if (!cputemp.equals("") || cputemp.length() != 0)
                {
                    if (tempUnit == TempUnit.fahrenheit)
                    {
                        tmpCputemp = String.valueOf((int) (Utility.parseDouble(tmpCputemp, 0) * 1.8) + 32);
                        tvCputemptxt.setText(tmpCputemp + "°F");
                        int temp = Utility.parseInt(tmpCputemp, 0);

                        if (temp < 113)
                        {
                            tvCputemptxt.setTextColor(Color.GREEN);
                        }
                        else if (temp >= 113 && temp < 138)
                        {
                            tvCputemptxt.setTextColor(Color.YELLOW);
                        }
                        else if (temp >= 138)
                        {
                            tvCputemptxt.setTextColor(Color.RED);
                        }
                    }

                    else if (tempUnit == TempUnit.celsius)
                    {
                        tvCputemptxt.setText(tmpCputemp + "°C");
                        int temp = Utility.parseInt(tmpCputemp, 0);
                        if (temp < 45)
                        {
                            tvCputemptxt.setTextColor(Color.GREEN);
                        }
                        else if (temp >= 45 && temp <= 59)
                        {
                            tvCputemptxt.setTextColor(Color.YELLOW);
                        }
                        else if (temp > 59)
                        {
                            tvCputemptxt.setTextColor(Color.RED);
                        }
                    }

                    else if (tempUnit == TempUnit.kelvin)
                    {
                        tmpCputemp = String.valueOf((int) (Utility.parseDouble(tmpCputemp, 0) + 273.15));

                        tvCputemptxt.setText(tmpCputemp + "°K");
                        int temp = Utility.parseInt(tmpCputemp, 0);
                        if (temp < 318)
                        {
                            tvCputemptxt.setTextColor(Color.GREEN);
                        }
                        else if (temp >= 318 && temp <= 332)
                        {
                            tvCputemptxt.setTextColor(Color.YELLOW);
                        }
                        else if (temp > 332)
                        {
                            tvCputemptxt.setTextColor(Color.RED);
                        }
                    }
                }
                else
                {
                    llCpuTemp.setVisibility(View.GONE);
                }
            }
        });

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context arg0, Intent intent)
        {
            double temperature = intent.getIntExtra(
                    BatteryManager.EXTRA_TEMPERATURE, 0) / 10;

            if (tempUnit == TempUnit.fahrenheit)
            {
                temperature = (temperature * 1.8) + 32;
                tvBatteryTemp.setText(((int) temperature) + "°F");
                if (temperature <= 104)
                {
                    tvBatteryTemp.setTextColor(Color.GREEN);
                }
                else if (temperature > 104 && temperature < 131)
                {
                    tvBatteryTemp.setTextColor(Color.YELLOW);
                }
                else if (temperature >= 131 && temperature < 140)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
                else if (temperature >= 140)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
            }
            else if (tempUnit == TempUnit.celsius)
            {
                tvBatteryTemp.setText(temperature + "°C");
                if (temperature < 45)
                {
                    tvBatteryTemp.setTextColor(Color.GREEN);

                }
                else if (temperature > 45 && temperature < 55)
                {
                    tvBatteryTemp.setTextColor(Color.YELLOW);
                }
                else if (temperature >= 55 && temperature < 60)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
                else if (temperature >= 60)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
            }
            else if (tempUnit == TempUnit.kelvin)
            {
                temperature = temperature + 273.15;
                tvBatteryTemp.setText(temperature + "°K");
                if (temperature < 318.15)
                {
                    tvBatteryTemp.setTextColor(Color.GREEN);
                }
                else if (temperature > 318.15 && temperature < 328.15)
                {
                    tvBatteryTemp.setTextColor(Color.YELLOW);
                }
                else if (temperature >= 328.15 && temperature < 333.15)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
                else if (temperature >= 333.15)
                {
                    tvBatteryTemp.setTextColor(Color.RED);
                }
            }
            // /F = (C x 1.8) + 32
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1, 1, 1, getString(R.string.settings))
                .setIcon(R.drawable.settings_dark)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == 1)
        {
            startActivity(new Intent(this, Preferences.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (mLastBackPressTime < java.lang.System.currentTimeMillis() - 4000)
        {
            mToast = Toast.makeText(this, Html.fromHtml(getResources().getString(R.string.press_again_to_exit)), Toast.LENGTH_SHORT);
            mToast.show();
            mLastBackPressTime = java.lang.System.currentTimeMillis();
        }
        else
        {
            if (mToast != null)
                mToast.cancel();
            finish();
            mLastBackPressTime = 0;
        }
    }

    public void setCurrentFragment(Fragment currentFragment)
    {
        this.currentFragment = currentFragment;
    }

    public Fragment getCurrentFragment()
    {
        return currentFragment;
    }
}
