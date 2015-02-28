package rs.pedjaapps.kerneltuner.utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;

import rs.pedjaapps.kerneltuner.BuildConfig;
import rs.pedjaapps.kerneltuner.MainApp;
import rs.pedjaapps.kerneltuner.constants.TempUnit;

/**
 * Created by pedja on 17.4.14..
 */
public class PrefsManager
{
    public static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());

    enum Key
    {
        cpu_refresh_interval, is_first_launch, kernel_info, app_version, main_show_temp, main_show_cpu,
        main_show_toggles, main_show_buttons, temp_unit, is_pro_version, pro_shown, show_cpu_options_dialog, cpu_enable_all,
        cpu_disable_all, hide_unsupported_items, notification_service, cpu_show_all_cores, gpu_3d, gpu_2d,
        gpu_governor, mpdec_idle_freq, mpdec_enabled, mpdec_max_cpus, mpdec_min_cpus, mpdec_sosc,
        mpdec_sof, mpdec_pause, mpdec_delay, scheduler, readahead, dt2w, s2w, otg, vsync, fastcharge,
        tcp_congestion, se, DEBUG
    }

    public static boolean DEBUG()
    {
        return prefs.getBoolean(Key.DEBUG.toString(), BuildConfig.DEBUG);
    }

    public static boolean DEBUG(boolean debug)
    {
        return prefs.getBoolean(Key.DEBUG.toString(), debug);
    }

    public static long getCpuRefreshInterval()
    {
        return Utility.parseLong(prefs.getString(Key.cpu_refresh_interval.toString(), "1000"), 1000);
    }

    public static boolean isFirstLaunch()
    {
        return prefs.getBoolean(Key.is_first_launch.toString(), true);
    }

    public static void setKernelInfo()
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.kernel_info.toString(), IOHelper.kernel());
        editor.apply();
    }

    public static int getAppVersion()
    {
        return prefs.getInt(Key.app_version.toString(), 0);
    }

    public static void setAppVersion(int version)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.app_version.toString(), version);
        editor.apply();
    }
	
	public static int getSeLinux()
    {
        return prefs.getInt(Key.se.toString(), -1);
    }

    public static void setSeLinux(int se)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.se.toString(), se);
        editor.apply();
    }

    public static boolean getMainShowTemp()
    {
        return prefs.getBoolean(Key.main_show_temp.toString(), true);
    }

    public static boolean getMainShowCpu()
    {
        return prefs.getBoolean(Key.main_show_cpu.toString(), true);
    }

    public static boolean getMainShowToggles()
    {
        return prefs.getBoolean(Key.main_show_toggles.toString(), true);
    }

    public static boolean getMainShowButtons()
    {
        return prefs.getBoolean(Key.main_show_buttons.toString(), true);
    }

    public static TempUnit getTempUnit()
    {
        String tempUnit = prefs.getString(Key.temp_unit.toString(), TempUnit.celsius.toString());
        return TempUnit.fromString(tempUnit);
    }

    public static boolean isProVersion()
    {
        return prefs.getBoolean(Key.is_pro_version.toString(), false);
    }

    public static void setPro(boolean pro)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.is_pro_version.toString(), pro);
        editor.apply();
    }

    public static boolean isProDialogShown()
    {
        return prefs.getBoolean(Key.pro_shown.toString(), false);
    }

    public static void setProDialogShown(boolean pro)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.pro_shown.toString(), pro);
        editor.apply();
    }

    public static boolean showCpuOptionDialog()
    {
        return prefs.getBoolean(Key.show_cpu_options_dialog.toString(), true);
    }

    public static boolean getCpuEnableAll()
    {
        return prefs.getBoolean(Key.cpu_enable_all.toString(), false);
    }

    public static boolean getCpuDisableAll()
    {
        return prefs.getBoolean(Key.cpu_enable_all.toString(), false);
    }

    public static void setCpuEnableAll(boolean enableAll)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.cpu_enable_all.toString(), enableAll);
        editor.apply();
    }

    public static void setCpuDisableAll(boolean disableAll)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.cpu_disable_all.toString(), disableAll);
        editor.apply();
    }

    public static void setShowCpuOptionsDialog(boolean show)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.show_cpu_options_dialog.toString(), show);
        editor.apply();
    }

    public static boolean hideUnsupportedItems()
    {
        return prefs.getBoolean(Key.hide_unsupported_items.toString(), false);
    }

    public static boolean getNotificationService()
    {
        return prefs.getBoolean(Key.notification_service.toString(), false);
    }

    public static boolean cpuShowAllCores()
    {
        return prefs.getBoolean(Key.cpu_show_all_cores.toString(), false);
    }

    public static void setCpuShowAllCores(boolean show)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Key.cpu_show_all_cores.toString(), show);
        editor.apply();
    }

    public static void setCpuMaxFreq(int core, int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cpu_" + core + "_max", freq);
        editor.apply();
    }

    public static void setCpuMinFreq(int core, int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cpu_" + core + "_min", freq);
        editor.apply();
    }

    public static void setCpuScroffFreq(int core, int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cpu_" + core + "_scroff", freq);
        editor.apply();
    }

    public static void setCpuGovernor(int core, String governor)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cpu_" + core + "_governor", governor);
        editor.apply();
    }

    public static int getCpuMaxFreq(int core)
    {
        return prefs.getInt("cpu_" + core + "_max", -1);
    }

    public static int getCpuMinFreq(int core)
    {
        return prefs.getInt("cpu_" + core + "_min", -1);
    }

    public static int getCpuScroffFreq(int core)
    {
        return prefs.getInt("cpu_" + core + "_scroff", -1);
    }

    public static String getCpuGovernor(int core)
    {
        try
        {
            //i accidentally put this as int, so this is a workaround
            return prefs.getString("cpu_" + core + "_governor", null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Crashlytics.logException(e);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("cpu_" + core + "_governor");
            editor.apply();
        }
        return null;
    }

    public static void setGpu3d(int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.gpu_3d.toString(), freq);
        editor.apply();
    }

    public static void setGpu2d(int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.gpu_2d.toString(), freq);
        editor.apply();
    }

    public static void setGpuGovernor(String gov)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.gpu_governor.toString(), gov);
        editor.apply();
    }

    public static String getGpuGovernor()
    {
        return prefs.getString(Key.gpu_governor.toString(), null);
    }

    public static int getGpu3d()
    {
        return prefs.getInt(Key.gpu_3d.toString(), -1);
    }

    public static int getGpu2d()
    {
        return prefs.getInt(Key.gpu_2d.toString(), -1);
    }

    public static void setMpdecIdleFreq(int freq)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_idle_freq.toString(), freq);
        editor.apply();
    }

    public static int getMpdecIdleFreq()
    {
        return prefs.getInt(Key.mpdec_idle_freq.toString(), -1);
    }

    public static void setMpdecEnabled(int enabled)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_enabled.toString(), enabled);
        editor.apply();
    }

    public static int getMpdecEnabled()
    {
        return prefs.getInt(Key.mpdec_enabled.toString(), -1);
    }

    public static void setMpdecThreshold(int num, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("mpdec_thr_" + num, value);
        editor.apply();
    }

    public static int getMpdecThreshold(int num)
    {
        return prefs.getInt("mpdec_thr_" + num, -1);
    }

    public static void setMpdecTime(int num, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("mpdec_time_" + num, value);
        editor.apply();
    }

    public static int getMpdecTime(int num)
    {
        return prefs.getInt("mpdec_time_" + num, -1);
    }

    public static void setMpdecMaxCpus(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_max_cpus.toString(), value);
        editor.apply();
    }

    public static int getMpdecMaxCpus()
    {
        return prefs.getInt(Key.mpdec_max_cpus.toString(), -1);
    }

    public static void setMpdecMinCpus(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_min_cpus.toString(), value);
        editor.apply();
    }

    public static int getMpdecMinCpus()
    {
        return prefs.getInt(Key.mpdec_min_cpus.toString(), -1);
    }

    public static void setMpdecSoSc(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_sosc.toString(), value);
        editor.apply();
    }

    public static int getMpdecSoSc()
    {
        return prefs.getInt(Key.mpdec_sosc.toString(), -1);
    }

    public static void setMpdecSoF(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_sof.toString(), value);
        editor.apply();
    }

    public static int getMpdecSoF()
    {
        return prefs.getInt(Key.mpdec_sof.toString(), -1);
    }

    public static void setMpdecDelay(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_delay.toString(), value);
        editor.apply();
    }

    public static int getMpdecDelay()
    {
        return prefs.getInt(Key.mpdec_delay.toString(), -1);
    }

    public static void setMpdecPause(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.mpdec_pause.toString(), value);
        editor.apply();
    }

    public static int getMpdecPause()
    {
        return prefs.getInt(Key.mpdec_pause.toString(), -1);
    }

    public static void setThermalFreq(int num, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("thermal_freq_" + num, value);
        editor.apply();
    }

    public static int getThremalFreq(int num)
    {
        return prefs.getInt("thermal_freq_" + num, -1);
    }

    public static void setThermalLow(int num, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("thermal_tmp_low_" + num, value);
        editor.apply();
    }

    public static int getThremalLow(int num)
    {
        return prefs.getInt("thermal_tmp_low_" + num, -1);
    }

    public static void setThermalHigh(int num, int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("thermal_tmp_high_" + num, value);
        editor.apply();
    }

    public static int getThremalHigh(int num)
    {
        return prefs.getInt("thermal_tmp_high_" + num, -1);
    }

    public static void setScheduler(String scheduler)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.scheduler.toString(), scheduler);
        editor.apply();
    }

    public static String getScheduer()
    {
        return prefs.getString(Key.scheduler.toString(), null);
    }

    public static void setReadAhead(String readahead)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.readahead.toString(), readahead);
        editor.apply();
    }

    public static String getReadAhead()
    {
        return prefs.getString(Key.readahead.toString(), null);
    }

    public static void setDT2W(int enabled)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.dt2w.toString(), enabled);
        editor.apply();
    }

    public static int getDT2W()
    {
        return prefs.getInt(Key.dt2w.toString(), -1);
    }

    public static void setS2W(int enabled)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.s2w.toString(), enabled);
        editor.apply();
    }

    public static int getS2W()
    {
        return prefs.getInt(Key.s2w.toString(), -1);
    }

    public static void setFastcharge(int enabled)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.fastcharge.toString(), enabled);
        editor.apply();
    }

    public static int getFastcharge()
    {
        return prefs.getInt(Key.fastcharge.toString(), -1);
    }

    public static void setVsync(int enabled)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.vsync.toString(), enabled);
        editor.apply();
    }

    public static int getVsync()
    {
        return prefs.getInt(Key.vsync.toString(), -1);
    }

    public static void setOtg(int value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Key.otg.toString(), value);
        editor.apply();
    }

    public static int getOtg()
    {
        return prefs.getInt(Key.otg.toString(), -1);
    }

    public static void setTcpCongestion(String tcp)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key.tcp_congestion.toString(), tcp);
        editor.apply();
    }

    public static String getTcpCongestion()
    {
        return prefs.getString(Key.tcp_congestion.toString(), null);
    }
}
