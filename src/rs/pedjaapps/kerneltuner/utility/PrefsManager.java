package rs.pedjaapps.kerneltuner.utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rs.pedjaapps.kerneltuner.MainApp;
import rs.pedjaapps.kerneltuner.constants.TempUnit;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;

/**
 * Created by pedja on 17.4.14..
 */
public class PrefsManager
{
    public static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApp.getContext());

    enum Key
    {
        cpu_refresh_interval, is_first_launch, kernel_info, app_version, main_show_temp, main_show_cpu,
        main_show_toggles, main_show_buttons, temp_unit, show_ads, show_cpu_options_dialog, cpu_enable_all,
        cpu_disable_all, hide_unsupported_items, notification_service, cpu_show_all_cores
    }

    public static long getCpuRefreshInterval()
    {
        return prefs.getLong(Key.cpu_refresh_interval.toString(), 1000);
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

    public static boolean showAds()
    {
        return prefs.getBoolean(Key.show_ads.toString(), true);
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

    public static boolean adsRemoved()
    {
        return false;//TODO implement this
    }
}
