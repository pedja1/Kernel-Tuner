package rs.pedjaapps.KernelTuner.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.c;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.constants.TempUnit;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.model.FrequencyCollection;
import rs.pedjaapps.KernelTuner.utility.PrefsManager;

/**
 * Created by pedja on 17.4.14..
 */
public class MainActivity extends AbsActivity implements Runnable, View.OnClickListener
{
    private long cpuRefreshInterval;
    private boolean firstLaunch;
    final int cpuTempPath = IOHelper.getCpuTempPath();

    private LinearLayout tempPanel;
    private LinearLayout cpuPanel;
    private LinearLayout togglesPanel;
    private LinearLayout mainPanel;

    private TextView tvCpu0prog;
    private TextView tvCpu1prog;
    private TextView tvCpu2prog;
    private TextView tvCpu3prog;

    private ProgressBar pbCpu0progbar;
    private ProgressBar pbCpu1progbar;
    private ProgressBar pbCpu2progbar;
    private ProgressBar pbCpu3progbar;

    private TextView tvBatteryTemp;
    private TextView tvCputemptxt;

    private LinearLayout llCpuTemp;

    Button cpu1toggle;
    Button cpu2toggle;
    Button cpu3toggle;

    private TextView tvCpuLoad;
    private ProgressBar pbCpuLoad;
    Button info;

    private TempUnit tempUnit;
    Handler cpuRefreshHandler;
    Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(getThemeRes());
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        cpuRefreshInterval = PrefsManager.getCpuRefreshInterval();
        firstLaunch = PrefsManager.isFirstLaunch();
        tempUnit = PrefsManager.getTempUnit();

        mountDebugFileSystem();
        enableTmemperatureMonitor();
        if (firstLaunch)
        {
            logKernelInfo();
        }

        setupView();
        setupViewPost();
        showChangelog();
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
    }

    private void setupViewPost()
    {
        if (PrefsManager.showAds())
            ((AdView) findViewById(R.id.ad)).loadAd(new AdRequest());
        Button gpu = (Button) findViewById(R.id.btn_gpu);

        gpu.setOnClickListener(new StartActivityListener((new File(Constants.GPU_SGX540).exists()) ? GpuSGX540.class : Gpu.class));
        gpu.setOnLongClickListener(new InfoListener(R.drawable.gpu,
                getResources().getString(R.string.info_gpu_title),
                getResources().getString(R.string.info_gpu_text),
                Constants.G_S_URL_PREFIX + "GPU", true));

        Button voltage = (Button) findViewById(R.id.btn_voltage);
        voltage.setOnClickListener(new StartActivityListener(
                VoltageActivity.class));
        voltage.setOnLongClickListener(new InfoListener(R.drawable.voltage,
                getResources().getString(R.string.info_voltage_title),
                getResources().getString(R.string.info_voltage_text),
                Constants.G_S_URL_PREFIX + "undervolting cpu", true));

        Button cpu = (Button) findViewById(R.id.btn_cpu);
        cpu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(PrefsManager.showCpuOptionDialog())
                {
                    cpuOptionsDialog();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, CPUActivity.class);
                    startActivity(intent);
                }
            }
        });
        cpu.setOnLongClickListener(new InfoListener(R.drawable.ic_launcher,
                getResources().getString(R.string.info_cpu_title),
                getResources().getString(R.string.info_cpu_text),
                Constants.G_S_URL_PREFIX + "CPU", true));

        Button tis = (Button) findViewById(R.id.btn_times);
        tis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MainActivity.this, TISActivity.class);
                startActivity(myIntent);
            }
        });
        tis.setOnLongClickListener(new InfoListener(R.drawable.times,
                getResources().getString(R.string.info_tis_title),
                getResources().getString(R.string.info_tis_text),
                Constants.G_S_URL_PREFIX + "cpu times_in_state", true));

        Button mp = (Button) findViewById(R.id.btn_mpdecision);
        mp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Intent myIntent = null;
                if (new File(Constants.MPDEC_THR_DOWN).exists())
                {
                    myIntent = new Intent(MainActivity.this, Mpdecision.class);
                }
                else if (new File(Constants.MPDEC_THR_0).exists())
                {
                    myIntent = new Intent(MainActivity.this, MpdecisionNew.class);
                }
                startActivity(myIntent);

            }
        });
        mp.setOnLongClickListener(new InfoListener(R.drawable.dual,
                getResources().getString(R.string.info_mpd_title),
                getResources().getString(R.string.info_mpd_text),
                Constants.G_S_URL_PREFIX + "mp-decision", true));

        Button misc = (Button) findViewById(R.id.btn_misc);
        misc.setOnClickListener(new StartActivityListener(MiscTweaks.class));
        misc.setOnLongClickListener(new InfoListener(R.drawable.misc,
                getResources().getString(R.string.info_misc_title),
                getResources().getString(R.string.info_misc_text), "", false));

        Button governor = (Button) findViewById(R.id.btn_governor);
        governor.setOnClickListener(new StartActivityListener(
                GovernorActivity.class));
        governor.setOnLongClickListener(new InfoListener(
                R.drawable.main_governor, getResources().getString(
                R.string.info_gov_title), getResources().getString(
                R.string.info_gov_text), Constants.G_S_URL_PREFIX
                + "linux governors", true));

        Button oom = (Button) findViewById(R.id.btn_oom);
        oom.setOnClickListener(new StartActivityListener(OOM.class));
        oom.setOnLongClickListener(new InfoListener(R.drawable.oom,
                getResources().getString(R.string.info_oom_title),
                getResources().getString(R.string.info_oom_text),
                Constants.G_S_URL_PREFIX + "oom", true));

        Button profiles = (Button) findViewById(R.id.btn_profiles);
        profiles.setOnClickListener(new StartActivityListener(Profiles.class));
        profiles.setOnLongClickListener(new InfoListener(R.drawable.profile,
                getResources().getString(R.string.info_profiles_title),
                getResources().getString(R.string.info_profiles_text), "",
                false));

        Button thermal = (Button) findViewById(R.id.btn_thermal);
        thermal.setOnClickListener(new StartActivityListener(Thermald.class));

        thermal.setOnLongClickListener(new InfoListener(R.drawable.temp,
                getResources().getString(R.string.info_thermal_title),
                getResources().getString(R.string.info_thermal_text), "", false));

        Button sd = (Button) findViewById(R.id.btn_sd);
        sd.setOnClickListener(new StartActivityListener(
                SDScannerConfigActivity.class));
        sd.setOnLongClickListener(new InfoListener(R.drawable.sd,
                getResources().getString(R.string.info_sd_title),
                getResources().getString(R.string.info_sd_text), "", false));



        info.setOnLongClickListener(new InfoListener(R.drawable.info,
                getResources().getString(R.string.info_sys_info_title),
                getResources().getString(R.string.info_sys_info_text), "",
                false));

        Button tm = (Button) findViewById(R.id.btn_task_manager);
        tm.setOnClickListener(new StartActivityListener(TaskManager.class));
        tm.setOnLongClickListener(new InfoListener(R.drawable.tm,
                getResources().getString(R.string.info_tm_title),
                getResources().getString(R.string.info_tm_text),
                Constants.G_S_URL_PREFIX + "task manager", true));

        Button build = (Button) findViewById(R.id.btn_build);
        build.setOnClickListener(new StartActivityListener(
                BuildpropEditor.class));
        build.setOnLongClickListener(new InfoListener(R.drawable.build,
                getResources().getString(R.string.info_build_title),
                getResources().getString(R.string.info_build_text),
                Constants.G_S_URL_PREFIX + "build.prop", true));

        Button sys = (Button) findViewById(R.id.btn_sysctl);
        sys.setOnClickListener(new StartActivityListener(SysCtl.class));
        sys.setOnLongClickListener(new InfoListener(R.drawable.sysctl,
                getResources().getString(R.string.info_sysctl_title),
                getResources().getString(R.string.info_sysctl_text),
                Constants.G_S_URL_PREFIX + "sysctl", true));

        Button log = (Button) findViewById(R.id.btn_logcat);
        log.setOnClickListener(new StartActivityListener(LogCat.class));
        log.setOnLongClickListener(new InfoListener(R.drawable.swap,
                getResources().getString(R.string.info_logs_title),
                getResources().getString(R.string.info_logs_text),
                Constants.G_S_URL_PREFIX + "swap", true));
    }

    private void showChangelog()
    {
        int appVersion = PrefsManager.getAppVersion();

        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int version = pInfo.versionCode;
            if (appVersion != version)
            {
                Intent myIntent = new Intent(this, Changelog.class);
                startActivity(myIntent);
            }

            PrefsManager.setAppVersion(version);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e(Constants.LOG_TAG, e.getMessage());
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    private void logKernelInfo()
    {
        PrefsManager.setKernelInfo();
    }

    private void enableTmemperatureMonitor()
    {
        if (!IOHelper.isTempEnabled())
        {
            new ATEnableTempMon().execute();
        }
    }

    private void mountDebugFileSystem()
    {
        File file = new File("/sys/kernel/debug");

        if (!file.exists() && file.list().length >= 0)
        {
            new ATMountDebugFS().execute();
        }
    }

    @Override
    public int getThemeRes()
    {
        return R.style.Theme_Kerneltuner;
    }

    public int getLayoutRes()
    {
        return R.layout.activity_main;
    }

    public void setupView()
    {
        tempPanel = (LinearLayout) findViewById(R.id.temperature_layout);
        mainPanel = (LinearLayout) findViewById(R.id.buttons_layout);
        cpuPanel = (LinearLayout) findViewById(R.id.cpu_info_layout);
        togglesPanel = (LinearLayout) findViewById(R.id.toggles_layout);

        if (!PrefsManager.getMainShowTemp())
        {
            tempPanel.setVisibility(View.GONE);
        }
        if (!PrefsManager.getMainShowCpu())
        {
            cpuPanel.setVisibility(View.GONE);
        }
        if (!PrefsManager.getMainShowToggles())
        {
            togglesPanel.setVisibility(View.GONE);
        }
        if (!PrefsManager.getMainShowButtons())
        {
            mainPanel.setVisibility(View.GONE);
        }

        tvCpu0prog = (TextView) findViewById(R.id.txtCpu0Freq);
        tvCpu1prog = (TextView) findViewById(R.id.txtCpu1Freq);
        tvCpu2prog = (TextView) findViewById(R.id.txtCpu2Freq);
        tvCpu3prog = (TextView) findViewById(R.id.txtCpu3Freq);

        pbCpu0progbar = (ProgressBar) findViewById(R.id.prgCpu0);
        pbCpu1progbar = (ProgressBar) findViewById(R.id.prgCpu1);
        pbCpu2progbar = (ProgressBar) findViewById(R.id.prgCpu2);
        pbCpu3progbar = (ProgressBar) findViewById(R.id.prgCpu3);

        tvBatteryTemp = (TextView) findViewById(R.id.txtBatteryTemp);
        llCpuTemp = (LinearLayout) findViewById(R.id.temp_cpu_layout);

        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle("Various kernel and system tuning");
        actionBar.setHomeButtonEnabled(false);

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
        tvCpuLoad = (TextView) findViewById(R.id.txtCpuLoad);
        pbCpuLoad = (ProgressBar) findViewById(R.id.prgCpuLoad);

        info = (Button) findViewById(R.id.btn_info);
        info.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MainActivity.this, SystemInfo.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_cpu1_toggle:
                getProgressDialog().show();
                new ATToggleCPU().execute("1");
                break;
            case R.id.btn_cpu2_toggle:
                getProgressDialog().show();
                new ATToggleCPU().execute("2");
                break;
            case R.id.btn_cpu3_toggle:
                getProgressDialog().show();
                new ATToggleCPU().execute("3");
                break;
        }
    }

    private class ATMountDebugFS extends AsyncTask<String, Void, Object>
    {
        @Override
        protected Object doInBackground(String... args)
        {
            CommandCapture command = new CommandCapture(0,
                    "mount -t debugfs debugfs /sys/kernel/debug");
            try
            {
                RootTools.getShell(true).add(command).waitForFinish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
    }

    private class ATEnableTempMon extends AsyncTask<String, Void, Object>
    {
        @Override
        protected Object doInBackground(String... args)
        {
            CommandCapture command = new CommandCapture(
                    0,
                    "chmod 777 /sys/devices/virtual/thermal/thermal_zone1/mode",
                    "chmod 777 /sys/devices/virtual/thermal/thermal_zone0/mode",
                    "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone1/mode",
                    "echo -n enabled > /sys/devices/virtual/thermal/thermal_zone0/mode");
            try
            {
                RootTools.getShell(true).add(command).waitForFinish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return null;
        }
    }

    private class ATToggleCPU extends AsyncTask<String, Void, Object>
    {
        @Override
        protected Object doInBackground(String... args)
        {
            File file = new File("/sys/devices/system/cpu/cpu" + args[0]
                    + "/cpufreq/scaling_governor");
            if (file.exists())
            {
                CommandCapture command = new CommandCapture(0,
                        "echo 1 > /sys/kernel/msm_mpdecision/conf/enabled",
                        "chmod 777 /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "echo 0 > /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "chown system /sys/devices/system/cpu/cpu" + args[0]
                                + "/online");
                try
                {
                    RootTools.getShell(true).add(command).waitForFinish();
                }
                catch (Exception e)
                {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }

            else
            {
                CommandCapture command = new CommandCapture(0,
                        "echo 0 > /sys/kernel/msm_mpdecision/conf/enabled",
                        "chmod 666 /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "echo 1 > /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "chmod 444 /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "chown system /sys/devices/system/cpu/cpu" + args[0]
                                + "/online",
                        "chmod 777 /sys/devices/system/cpu/cpu" + args[0]
                                + "/cpufreq/scaling_max_freq",
                        "chmod 777 /sys/devices/system/cpu/cpu" + args[0]
                                + "/cpufreq/scaling_min_freq",
                        "chmod 777 /sys/devices/system/cpu/cpu" + args[0]
                                + "/cpufreq/scaling_cur_freq",
                        "chmod 777 /sys/devices/system/cpu/cpu" + args[0]
                                + "/cpufreq/scaling_governor");
                try
                {
                    RootTools.getShell(true).add(command).waitForFinish();
                }
                catch (Exception e)
                {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(Object result)
        {
            if(progressDialog != null)progressDialog.dismiss();
        }

    }

    @Override
    public void run()
    {
        try
        {
            String tmp = IOHelper.cpuTemp(cpuTempPath);
            cpuTemp(tmp);
            cpu0update(IOHelper.cpu0CurFreq(), IOHelper.cpu0MaxFreq());

            if (IOHelper.cpu1Exists())
            {
                cpu1update(IOHelper.cpu1CurFreq(), IOHelper.cpu1MaxFreq());
            }
            if (IOHelper.cpu2Exists())
            {
                cpu2update(IOHelper.cpu2CurFreq(), IOHelper.cpu2MaxFreq());
            }
            if (IOHelper.cpu3Exists())
            {
                cpu3update(IOHelper.cpu3CurFreq(), IOHelper.cpu3MaxFreq());
            }
            setCpuLoad(getCpuLoad());
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
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

    private void cpu0update(final String freq, final String max)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu0prog.setText(freq.trim().substring(0, freq.length() - 3) + "MHz");
                }
                else
                {
                    tvCpu0prog.setText("offline");
                }
                pbCpu0progbar.setMax(FrequencyCollection.getInstance().getMaxProgress(max.trim()));
                pbCpu0progbar.setProgress(FrequencyCollection.getInstance().getProgress(freq.trim()));
            }
        });
    }

    private void cpu1update(final String freq, final String max)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu1prog.setText(freq.trim().substring(0, freq.length() - 3) + "MHz");
                }
                else
                {
                    tvCpu1prog.setText("offline");
                }
                pbCpu1progbar.setMax(FrequencyCollection.getInstance().getMaxProgress(max.trim()));
                pbCpu1progbar.setProgress(FrequencyCollection.getInstance().getProgress(freq.trim()));
            }
        });
    }

    private void cpu2update(final String freq, final String max)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu2prog.setText(freq.trim().substring(0, freq.length() - 3) + "MHz");
                }
                else
                {
                    tvCpu2prog.setText("offline");
                }
                pbCpu2progbar.setMax(FrequencyCollection.getInstance().getMaxProgress(max.trim()));
                pbCpu2progbar.setProgress(FrequencyCollection.getInstance().getProgress(freq.trim()));
            }
        });
    }

    private void cpu3update(final String freq, final String max)
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!freq.equals("offline") && freq.length() > 3)
                {
                    tvCpu3prog.setText(freq.trim().substring(0, freq.length() - 3) + "MHz");
                }
                else
                {
                    tvCpu3prog.setText("offline");
                }
                pbCpu3progbar.setMax(FrequencyCollection.getInstance().getMaxProgress(max.trim()));
                pbCpu3progbar.setProgress(FrequencyCollection.getInstance().getProgress(freq.trim()));
            }
        });
    }

    private void setCpuLoad(int load)
    {
        pbCpuLoad.setProgress(load);
        tvCpuLoad.setText(load + "%");
    }

    /**
     * CPU Temperature
     */

    private void cpuTemp(String cputemp)
    {
        llCpuTemp.setVisibility(View.VISIBLE);

        if (!cputemp.equals("") || cputemp.length() != 0)
        {
            if (tempUnit == TempUnit.fahrenheit)
            {
                cputemp = String.valueOf((int) (Double.parseDouble(cputemp) * 1.8) + 32);
                tvCputemptxt.setText(cputemp + "°F");
                int temp = Integer.parseInt(cputemp);

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
                tvCputemptxt.setText(cputemp + "°C");
                int temp = Integer.parseInt(cputemp);
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
                cputemp = String.valueOf((int) (Double.parseDouble(cputemp) + 273.15));

                tvCputemptxt.setText(cputemp + "°K");
                int temp = Integer.parseInt(cputemp);
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

    class StartActivityListener implements View.OnClickListener
    {
        Class<?> cls;

        public StartActivityListener(Class<?> cls)
        {
            this.cls = cls;
        }

        @Override
        public void onClick(View v)
        {
            startActivity(new Intent(MainActivity.this, cls));
        }
    }

    class InfoListener implements View.OnLongClickListener
    {

        int icon;
        String title;
        String text;
        String url;
        boolean more;

        public InfoListener(int icon, String title, String text, String url, boolean more)
        {
            this.icon = icon;
            this.title = title;
            this.text = text;
            this.url = url;
            this.more = more;
        }

        @Override
        public boolean onLongClick(View v)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(title);

            builder.setIcon(icon);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.text_view_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(text);

            builder.setPositiveButton(getResources().getString(R.string.info_ok), null);
            if (more)
            {
                builder.setNeutralButton(R.string.info_more, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
            builder.setView(view);
            AlertDialog alert = builder.create();

            alert.show();
            return true;
        }
    }

    private void cpuOptionsDialog()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.cpu_options_dialog, null);
        final CheckBox cbEnableAll = (CheckBox)v.findViewById(R.id.cbEnableAll);
        final CheckBox cbDisableAll = (CheckBox)v.findViewById(R.id.cbDisableAllOnExit);
        final CheckBox cbSave = (CheckBox)v.findViewById(R.id.cbSave);
        cbEnableAll.setChecked(PrefsManager.getCpuEnableAll());
        cbDisableAll.setChecked(PrefsManager.getCpuDisableAll());
        b.setView(v);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface p1, int p2)
            {
                startCpuSettings(cbEnableAll.isChecked(), cbDisableAll.isChecked(), cbSave.isChecked());
            }
        });
        b.setNegativeButton("Cancel", null);

        b.show();
    }

    private void startCpuSettings(boolean enableAll, boolean disableAll, boolean save)
    {
        PrefsManager.setCpuEnableAll(enableAll);
        PrefsManager.setCpuDisableAll(disableAll);
        PrefsManager.setShowCpuOptionsDialog(save);
        Intent intent = new Intent(this, CPUActivity.class);
        startActivity(intent);
    }


}
