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

package rs.pedjaapps.kerneltuner;

import android.content.Context;

import java.io.File;

/**
 * Class of constants used by this Locale plug-in.
 */
public final class Constants
{



    /**
     * Private constructor prevents instantiation
     * 
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Constants()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }

    /**
     * Log tag for logcat messages
     */
    // TODO: Change this to your application's own log tag
    public static final String LOG_TAG = "Kernel Tuner"; //$NON-NLS-1$
	
	/**
	 *Paths
	 */

    public static final int CPU_OFFLINE_CODE = -45;
    public static final int GPU_OFFLINE_CODE = -55;
    public static final int GPU_NOT_AVAILABLE = -55;
	
    public static final String G_S_URL_PREFIX = "https://www.google.com/search?q=";
	public static final String cpu0online = "/sys/devices/system/cpu/cpu0/online"; 
	public static final String cpu1online = "/sys/devices/system/cpu/cpu1/online"; 
	public static final String cpu2online = "/sys/devices/system/cpu/cpu2/online"; 
	public static final String cpu3online = "/sys/devices/system/cpu/cpu3/online";
    public static final String cpuScreenOff = "/sys/devices/system/cpu/cpu0/cpufreq/screen_off_max_freq";


	public static final String CPU0_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	public static final String SWAPS = "/proc/swaps";

	public static final String CPU0_CURR_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	public static final String CPU1_CURR_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
	public static final String CPU2_CURR_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
	public static final String CPU3_CURR_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";

	public static final String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static final String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	public static final String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	public static final String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	public static final String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	public static final String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	public static final String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	public static final String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	public static final String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static final String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	public static final String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	public static final String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";

	public static final String CPU0_GOVS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
	public static final String CPU1_GOVS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_governors";
	public static final String CPU2_GOVS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_governors";
	public static final String CPU3_GOVS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_governors";
	public static final String TIMES_IN_STATE_CPU0 = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public static final String TIMES_IN_STATE_CPU1 = "/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state";
	public static final String TIMES_IN_STATE_CPU2 = "/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state";
	public static final String TIMES_IN_STATE_CPU3 = "/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state";

	public static final String VOLTAGE_PATH = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
	public static final String VOLTAGE_PATH_TEGRA_3 = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	public static final String GPU_3D = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk";
    public static final String GPU_3D_2 = "/sys/class/kgsl/kgsl-3d0/max_gpuclk";
	public static final String GPU_3D_2_GOV = "/sys/class/kgsl/kgsl-3d0/pwrscale/trustzone/governor";
    public static final String GPU_2D_2 = "/sys/class/kgsl/kgsl-2d0/max_gpuclk";
    public static final String GPU_3D_AVAILABLE_FREQUENCIES = "/sys/class/kgsl/kgsl-3d0/gpu_available_frequencies";
	public static final String GPU_2D = "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk";
	public static final String CDEPTH = "/sys/kernel/debug/msm_fb/0/bpp";
	public static final String S2W = "/sys/android_touch/sweep2wake";
    public static final String S2W_VERSION = "/sys/android_touch/sweep2wake_version";
    public static final String DT2W = "/sys/android_touch/doubletap2wake";
	public static final String S2W_ALT = "/sys/android_touch/sweep2wake/s2w_switch";
	public static final String MPDECISION = "/sys/kernel/msm_mpdecision/conf/enabled";
	public static final String BUTTONS_LIGHT = "/sys/devices/platform/leds-pm8058/leds/button-backlight/currents";
	public static final String BUTTONS_LIGHT_2 = "/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents";
	public static final String SD_CACHE = "/sys/devices/virtual/bdi/179:0/read_ahead_kb";
	public static final String VSYNC = "/sys/kernel/debug/msm_fb/0/vsync_enable";
	public static final String FCHARGE = "/sys/kernel/fast_charge/force_fast_charge";
	public static final String OOM = "/sys/module/lowmemorykiller/parameters/minfree";
	public static final String THERMALD = "/sys/kernel/msm_thermal/conf/allowed_low_freq";
	public static final String SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
	public static final String OTG = "/sys/kernel/debug/msm_otg/mode";
	public static final String OTG_2= "/sys/kernel/debug/otg/mode";
	public static final String CPU_MIN= "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
	public static final String CPU_MAX= "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
	public static final String CPU_INFO = "/proc/cpuinfo";
    public static final String CPU_TEMP = "/sys/class/thermal/thermal_zone1/temp";
	public static final String MPDEC_THR_UP = "/sys/kernel/msm_mpdecision/conf/nwns_threshold_up";
	public static final String MPDEC_THR_DOWN = "/sys/kernel/msm_mpdecision/conf/nwns_threshold_down";
	public static final String KERNEL = "/proc/version";
	public static final String BATTERY_LEVEL = "/sys/class/power_supply/battery/capacity";
	public static final String BATTERY_TEMP = "/sys/class/power_supply/battery/batt_temp";
    public static final String BATTERY_TEMP2 = "/sys/class/power_supply/battery/temp";
	public static final String BATTERY_DRAIN = "/sys/class/power_supply/battery/batt_current";
    public static final String BATTERY_DRAIN2 = "/sys/class/power_supply/battery/current_now";
	public static final String BATTERY_VOLTAGE = "/sys/class/power_supply/battery/batt_vol";
    public static final String BATTERY_VOLTAGE2 = "/sys/class/power_supply/battery/voltage_now";
	public static final String BATTERY_TECH = "/sys/class/power_supply/battery/technology";
	public static final String BATTERY_HEALTH = "/sys/class/power_supply/battery/health";
	public static final String BATTERY_CAPACITY = "/sys/class/power_supply/battery/full_bat";
    public static final String BATTERY_CAPACITY2 = "/sys/class/power_supply/battery/charge_full_design";
	public static final String BATTERY_CHARGING_SOURCE = "/sys/class/power_supply/battery/charging_source";
	public static final String CPU_TEMP_ENABLED = "/sys/devices/virtual/thermal/thermal_zone1/mode";
	public static final String MPDEC_THR_0 = "/sys/kernel/msm_mpdecision/conf/nwns_threshold_0";
	public static final String NOTIF_LED = "/sys/kernel/notification_leds/off_timer_multiplier";
	public static final String THERMAL_LOW_FREQ = 	"/sys/kernel/msm_thermal/conf/allowed_low_freq";
	public static final String READ_AHEAD_KB = 	"/sys/devices/virtual/bdi/179:0/read_ahead_kb";
	public static final String NLT = "/sys/kernel/notification_leds/off_timer_multiplier";
	public static final String S2W_END = "/sys/android_touch/sweep2wake_endbutton";
	public static final String S2W_START = "/sys/android_touch/sweep2wake_startbutton";
	public static final String S2W_BUTTONS = "/sys/android_touch/sweep2wake_buttons";
	public static final String MPDEC_DELAY = "/sys/kernel/msm_mpdecision/conf/delay";
    public static final String MPDEC_ENABLED = "/sys/kernel/msm_mpdecision/conf/enabled";
	public static final String MPDEC_PAUSE = "/sys/kernel/msm_mpdecision/conf/pause";
	public static final String MPDEC_TIME_UP = "/sys/kernel/msm_mpdecision/conf/twts_threshold_up";
	public static final String MPDEC_TIME_DOWN = "/sys/kernel/msm_mpdecision/conf/twts_threshold_down";
	public static final String MPDEC_IDLE_FREQ = "/sys/kernel/msm_mpdecision/conf/idle_freq";
	public static final String MPDEC_SCROFF_FREQ = "/sys/kernel/msm_mpdecision/conf/scroff_freq";
	public static final String MPDEC_SCROFF_SINGLE = "/sys/kernel/msm_mpdecision/conf/scroff_single_core";
	public static final String MPDEC_MAX_CPUS = "/sys/kernel/msm_mpdecision/conf/max_cpus";
	public static final String MPDEC_MIN_CPUS = "/sys/kernel/msm_mpdecision/conf/min_cpus";
	public static final String THERMAL_MID_FREQ = "/sys/kernel/msm_thermal/conf/allowed_mid_freq";
	public static final String THERMAL_MAX_FREQ = "/sys/kernel/msm_thermal/conf/allowed_max_freq";
	public static final String THERMAL_LOW_LOW = "/sys/kernel/msm_thermal/conf/allowed_low_low";
	public static final String THERMAL_LOW_HIGH = "/sys/kernel/msm_thermal/conf/allowed_low_high";
	public static final String THERMAL_MID_LOW = "/sys/kernel/msm_thermal/conf/allowed_mid_low";
	public static final String THERMAL_MID_HIGH = "/sys/kernel/msm_thermal/conf/allowed_mid_high";
	public static final String THERMAL_MAX_LOW = "/sys/kernel/msm_thermal/conf/allowed_max_low";
	public static final String THERMAL_MAX_HIGH = "/sys/kernel/msm_thermal/conf/allowed_max_high";
	
	public static final String[] CPU_TEMP_PATHS = new String[]{
	"/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
	"/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
	"/sys/class/thermal/thermal_zone1/temp",
	"/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
	"/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
    "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
	"/sys/devices/platform/tegra_tmon/temp1_input",
	"/sys/kernel/debug/tegra_thermal/temp_tj",
	"/sys/devices/platform/s5p-tmu/temperature",
	"/sys/class/thermal/thermal_zone0/temp"
	};
	
	public static final String GPU_SGX540 = "/sys/devices/system/cpu/cpu0/cpufreq/gpu_max_freq";
	public static final String IVA = "/sys/devices/system/cpu/cpu0/cpufreq/iva_freq_oc";
	

    public static final int SPLASH_MINIMUM_STAY_TIME = 5; // seconds

    public static final int HANDLER_SPLASH_COMPLETE = 1001;
	
    /**
     * Flag to enable logcat messages.
     */
    public static final boolean IS_LOGGABLE = false;

    /**
     * Flag to enable runtime checking of method parameters
     */
    public static final boolean IS_PARAMETER_CHECKING_ENABLED = false;

    /**
     * Flag to enable runtime checking of whether a method is called on the correct thread
     */
    public static final boolean IS_CORRECT_THREAD_CHECKING_ENABLED = false;

    /**
     * Determines the "versionCode" in the {@code AndroidManifest}.
     * 
     * @param context to read the versionCode. Cannot be null.
     * @return versionCode of the app.
     * @throws IllegalArgumentException if {@code context} is null.
     */
    public static int getVersionCode(final Context context)
    {
        if (Constants.IS_PARAMETER_CHECKING_ENABLED)
        {
            if (null == context)
            {
                throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
            }
        }

        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (final UnsupportedOperationException e)
        {
            /*
             * This exception is thrown by test contexts
             */
            return 1;
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
