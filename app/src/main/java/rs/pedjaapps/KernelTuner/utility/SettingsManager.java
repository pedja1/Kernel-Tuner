package rs.pedjaapps.KernelTuner.utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rs.pedjaapps.KernelTuner.App;
import rs.pedjaapps.KernelTuner.BuildConfig;

/**
 * Created by pedja on 2/12/14.
 * Handles all reads and writes to SharedPreferences
 * @author Predrag Čokulov
 */
public class SettingsManager
{
    private static final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.get());

    public enum KEY
    {
        DEBUG, FIRST_LAUNCH
    }

    public static boolean isFirstLaunch()
    {
        return prefs.getBoolean(KEY.FIRST_LAUNCH.toString(), true);
    }

    public static void setFirstLaunch(boolean firstLaunch)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY.FIRST_LAUNCH.toString(), firstLaunch);
        editor.apply();
    }

    public static boolean DEBUG()
    {
        return prefs.getBoolean(KEY.DEBUG.toString(), BuildConfig.DEBUG);
    }

    public static boolean DEBUG(boolean debug)
    {
        return prefs.getBoolean(KEY.DEBUG.toString(), debug);
    }

    public static void clearAllPrefs()
    {
        SharedPreferences.Editor editor = prefs.edit();
        for(KEY key : KEY.values())
        {
            editor.remove(key.toString());
        }
        editor.apply();
    }
}
