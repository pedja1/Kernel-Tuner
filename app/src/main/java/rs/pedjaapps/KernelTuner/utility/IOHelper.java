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
package rs.pedjaapps.KernelTuner.utility;

import android.os.SystemClock;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.pedjaapps.KernelTuner.model.CpuFreq;
import rs.pedjaapps.KernelTuner.model.CpuTime;
import rs.pedjaapps.KernelTuner.model.GpuFreq;
import rs.pedjaapps.KernelTuner.model.Voltage;
import rs.pedjaapps.KernelTuner.root.RCommand;
import rs.pedjaapps.KernelTuner.root.RootUtils;

public class IOHelper
{

    public static final Pattern voltagePattern1 = Pattern.compile("\\s*(\\d+):\\s*(\\d+)\\s*");
    public static final Pattern voltagePattern2 = Pattern.compile("\\s*(\\d+)mhz:\\s*(\\d+)\\s*mV\\s*");
    public static Matcher voltageMatcher1 = null;
    public static Matcher voltageMatcher2 = null;

    public static boolean freqsExists()
    {
        return Constants.CPU0_FREQS.exists();
    }

    public static boolean oomExists()
    {
        return Constants.OOM.exists();
    }

    public static boolean thermaldExists()
    {
        return Constants.THERMALD.exists();
    }

    public static boolean swapsExists()
    {
        return Constants.SWAPS.exists();
    }

    public static boolean cpu0Exists()
    {
        return Constants.cpu0online.exists();
    }

    public static boolean cpu1Exists()
    {
        return Constants.cpu1online.exists();
    }

    public static boolean cpu2Exists()
    {
        return Constants.cpu2online.exists();
    }

    public static boolean cpu3Exists()
    {
        return Constants.cpu3online.exists();
    }

    public static boolean cpuScreenOff()
    {
        return Constants.cpuScreenOff.exists();
    }

    public static boolean cpuOnline(int cpu)
    {
        switch (cpu)
        {
            case 0:
                return Constants.CPU0_CURR_GOV.exists();
            case 1:
                return Constants.CPU1_CURR_GOV.exists();
            case 2:
                return Constants.CPU2_CURR_GOV.exists();
            case 3:
                return Constants.CPU3_CURR_GOV.exists();
            default:
                return false;
        }
    }

    public static boolean gpu3DMaxExists()
    {
        File file1 = Constants.GPU_3D;
        File file2 = Constants.GPU_3D_2;
        return file1.exists() || file2.exists();
    }

    public static boolean gpu3DCurExists()
    {
        File file1 = Constants.GPU_3D_CURRENT;
        File file2 = Constants.GPU_3D_2_CURRENT;
        return file1.exists() || file2.exists();
    }

    public static boolean cdExists()
    {
        return Constants.CDEPTH.exists();
    }

    public static boolean tcpCongestionControlAvailable()
    {
        return Constants.TCP_CONGESTION.exists() && Constants.TCP_AVAILABLE_CONGESTION.exists();
    }

    public static boolean voltageExists()
    {
        boolean i = false;
        if (Constants.VOLTAGE_PATH.exists())
        {
            i = true;
        }
        else if (Constants.VOLTAGE_PATH_TEGRA_3.exists())
        {
            i = true;
        }
		else if (Constants.VOLTAGE_PATH_2.exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean otgExists()
    {
        boolean i = false;
        if (Constants.OTG.exists())
        {
            i = true;
        }
        else if (Constants.OTG_2.exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean s2wExists()
    {
        boolean i = false;
        if (Constants.S2W.exists())
        {
            i = true;
        }
        else if (Constants.S2W_ALT.exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean dt2wExists()
    {
        return Constants.DT2W.exists();
    }

    public static boolean TISExists()
    {
        return Constants.TIMES_IN_STATE_CPU0.exists();
    }

    public static boolean mpdecisionExists()
    {
        return Constants.MPDECISION.exists();
    }

    public static boolean buttonsExists()
    {
        boolean i = false;
        if (Constants.BUTTONS_LIGHT.exists())
        {
            i = true;
        }
        else if (Constants.BUTTONS_LIGHT_2.exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean sdcacheExists()
    {
        return Constants.SD_CACHE.exists();
    }

    public static boolean vsyncExists()
    {
        return Constants.VSYNC.exists();
    }

    public static boolean fchargeExists()
    {
        return Constants.FCHARGE.exists();
    }

    public static List<CpuFreq> frequencies()
    {
        List<CpuFreq> frequencies = new ArrayList<>();
        try
        {
            String data = RCommand.readFileContent(Constants.CPU0_FREQS);
            String[] freqs = data.split(" ");
            for (String s : freqs)
            {
                int freq = Utility.parseInt(s, -1);
                CpuFreq frequency = new CpuFreq(freq);
                frequencies.add(frequency);
            }
        }
        catch (Exception e)
        {
            try
            {
                FileInputStream fstream = new FileInputStream(Constants.TIMES_IN_STATE_CPU0);

                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                while ((strLine = br.readLine()) != null)
                {
                    String[] delims = strLine.split(" ");
                    String freqS = delims[0];

                    int freq = Utility.parseInt(freqS, -1);
                    CpuFreq frequency = new CpuFreq(freq);
                    frequencies.add(frequency);
                }
                Collections.sort(frequencies, new CpuFreqSorter());
                in.close();
                fstream.close();
                br.close();
            }
            catch (Exception ee)
            {
                //Crashlytics.logException(ee);
                e.printStackTrace();
            }
        }
        return frequencies;

    }

    public static String[] getTcpAvailableCongestion()
    {
        try
        {
            return RCommand.readFileContent(Constants.TCP_AVAILABLE_CONGESTION).trim().split(" ");
        }
        catch (Exception e)
        {
            return new String[0];
        }
    }

    public static List<String> getTcpAvailableCongestionAsList()
    {
        return Arrays.asList(getTcpAvailableCongestion());
    }

    public static String getTcpCongestion()
    {
        try
        {
            return RCommand.readFileContent(Constants.TCP_CONGESTION).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static List<String> oom()
    {
        try
        {
            if(!Constants.OOM.canRead())
            {
                new RootUtils().execAndWait("chmod 664 " + Constants.OOM);
            }
            return Arrays.asList(RCommand.readFileContent(Constants.OOM).split(","));
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }

    }

    public static String leds()
    {
        try
        {
            return RCommand.readFileContent(Constants.BUTTONS_LIGHT_2).trim();
        }
        catch (Exception e)
        {
            try
            {
                return RCommand.readFileContent(Constants.BUTTONS_LIGHT).trim();
            }
            catch (Exception ee)
            {
                return "";
            }
        }

    }

    public static String[] governors()
    {
        try
        {
            //return RCommand.readFileContent(Constants.CPU0_GOVS)).split("\\s");
            return RCommand.readFileContent(Constants.CPU0_GOVS).trim().split("\\s");
        }
        catch (Exception e)
        {
            return new String[0];
        }

    }

    public static List<String> governorsAsList()
    {
        try
        {
            return Arrays.asList(RCommand.readFileContent(Constants.CPU0_GOVS).split("\\s"));
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }

    }

    public static int cpu0MinFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU0_MIN_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public static String cpuMin()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU_MIN).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpuMax()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU_MAX).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static int cpu0MaxFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU0_MAX_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    public static int cpu1MinFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU1_MIN_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static int cpu1MaxFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU1_MAX_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static int cpu2MinFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU2_MIN_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    public static int cpu2MaxFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU2_MAX_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static int cpu3MinFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU3_MIN_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    public static int cpu3MaxFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.CPU3_MAX_FREQ).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }

    }

    public static CpuFreq cpuCurFreq(int core)
    {
        File path;
        switch (core)
        {
            case 0:
                path = Constants.CPU0_CURR_FREQ;
                break;
            case 1:
                path = Constants.CPU1_CURR_FREQ;
                break;
            case 2:
                path = Constants.CPU2_CURR_FREQ;
                break;
            case 3:
                path = Constants.CPU3_CURR_FREQ;
                break;
            default:
                path = Constants.CPU0_CURR_FREQ;
                break;
        }
        if(!path.exists())return null;
        try
        {
            int freq = Utility.parseInt(RCommand.readFileContent(path).trim(), -1);
            if(freq > 0)
            {
                return new CpuFreq(freq);
            }
        }
        catch (Exception ignore)
        {
        }
        return null;
    }

    public static int cpuScreenOffMaxFreq()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.cpuScreenOff).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static String cpu0CurGov()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU0_CURR_GOV).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpu1CurGov()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU1_CURR_GOV).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpu2CurGov()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU2_CURR_GOV).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpu3CurGov()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU3_CURR_GOV).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }


    public static List<CpuTime> getTis()
    {
        List<CpuTime> times = new ArrayList<>();

        String[] lines = null;
        try
        {
            lines = RCommand.readFileContentAsLineArray(Constants.TIMES_IN_STATE_CPU0);

            for (String strLine : lines)
            {
                String[] delims = strLine.split(" ");
                times.add(new CpuTime(Utility.parseInt(delims[0], 0), Long.parseLong(delims[1])));
            }
        }
        catch (Exception ex)
        {
            if(SettingsManager.DEBUG())Log.d(Constants.LOG_TAG, "parseVoltageM2: " + ex.getMessage());
            //Crashlytics.setString("time values: ", Arrays.toString(lines));
            //Crashlytics.logException(ex);
        }

        return times;

    }


    public static List<Voltage> voltages()
    {
        List<Voltage> voltages = new ArrayList<>();
        if (Constants.VOLTAGE_PATH.exists())
        {
            parseVoltageM1(voltages, Constants.VOLTAGE_PATH);
        }
        if (Constants.VOLTAGE_PATH_2.exists())
        {
            parseVoltageM1(voltages, Constants.VOLTAGE_PATH_2);
        }
        else if (Constants.VOLTAGE_PATH_TEGRA_3.exists())
        {
            parseVoltageM2(voltages, Constants.VOLTAGE_PATH_TEGRA_3);
        }
        return voltages;
    }

    private static void parseVoltageM2(List<Voltage> voltages, File voltagePathTegra3)
    {
        /*
        300mhz: 775 mV
        422mhz: 775 mV
        652mhz: 775 mV
        729mhz: 780 mV
        883mhz: 800 mV
        960mhz: 810 mV
        1036mhz: 820 mV
        1190mhz: 840 mV
        1267mhz: 850 mV
        1497mhz: 880 mV
        1574mhz: 890 mV
        1728mhz: 920 mV
        1958mhz: 965 mV
        2265mhz: 1025 mV
        2457mhz: 1060 mV
        */
        String[] lines = null;
        try
        {
            lines = RCommand.readFileContentAsLineArray(voltagePathTegra3);
            for (String strLine : lines)
            {
                voltageMatcher2 = resetMatcher(voltageMatcher2, strLine, voltagePattern2);
                if(!voltageMatcher2.matches())continue;
                Voltage voltage = new Voltage();
                String name = voltageMatcher2.group(2), frequency = voltageMatcher2.group(1);
                int value = Utility.parseInt(name, -1);
                if (frequency == null || value == -1)
                {
                    continue;
                }
                voltage.setFreq(frequency + "MHz");
                voltage.setName(name + "mV");
                voltage.setValue(value);
                voltage.setDivider(1);
                voltage.setMultiplier(1000);
                voltages.add(voltage);
            }
        }
        catch (Exception ex)
        {
            if(SettingsManager.DEBUG())Log.d(Constants.LOG_TAG, "parseVoltageM2: " + ex.getMessage());
            //Crashlytics.setString("volt values: ", Arrays.toString(lines));
            //Crashlytics.logException(ex);
        }
    }

    private static void parseVoltageM1(List<Voltage> voltages, File path)
    {
        String[] lines = null;
        try
        {
            lines = RCommand.readFileContentAsLineArray(path);
            for (String strLine : lines)
            {
                voltageMatcher1 = resetMatcher(voltageMatcher1, strLine, voltagePattern1);
                if(!voltageMatcher1.matches())continue;
                Voltage voltage = new Voltage();
                String name = voltageMatcher1.group(2), frequency = voltageMatcher1.group(1);
                int frInt = Utility.parseInt(frequency, -1);
                if (frInt < 0) continue;
                frequency = frInt / 1000 + "MHz";
                int value = Utility.parseInt(name, -1);
                if (value < 0) continue;
                name = value / 1000 + "mV";

                voltage.setFreq(frequency);
                voltage.setName(name);
                voltage.setValue(value);
                voltage.setFreqValue(frInt);
                voltage.setDivider(1000);
                voltage.setMultiplier(1);
                voltages.add(voltage);
            }
        }
        catch (Exception ex)
        {
            if(SettingsManager.DEBUG())Log.d(Constants.LOG_TAG, "parseVoltageM2: " + ex.getMessage());
            //Crashlytics.setString("volt values: ", Arrays.toString(lines));
            //Crashlytics.logException(ex);
        }
    }

    private static Matcher resetMatcher(Matcher matcher, String input, Pattern pattern)
    {
        if(matcher == null)return pattern.matcher(input);
        else return matcher.reset(input);
    }


    public static String uptime()
    {
        String uptime;

        int time = (int) SystemClock.elapsedRealtime();

        String s = (time / 1000) % 60 + "";
        String m = (time / (1000 * 60)) % 60 + "";
        String h = (time / (1000 * 3600)) % 24 + "";
        String d = time / (1000 * 60 * 60 * 24) + "";
        StringBuilder builder = new StringBuilder();
        if (!d.equals("0"))
        {
            builder.append(d).append("d:");
        }
        if (!h.equals("0"))
        {
            builder.append(h).append("h:");
        }
        if (!m.equals("0"))
        {
            builder.append(m).append("m:");
        }
        builder.append(s).append("s");
        uptime = builder.toString();

        return uptime;

    }

    public static String deepSleep()
    {
        String deepSleep;

        int time = (int) (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());

        String s = (time / 1000) % 60 + "";
        String m = (time / (1000 * 60)) % 60 + "";
        String h = (time / (1000 * 3600)) % 24 + "";
        String d = time / (1000 * 60 * 60 * 24) + "";
        StringBuilder builder = new StringBuilder();
        if (!d.equals("0"))
        {
            builder.append(d).append("d:");
        }
        if (!h.equals("0"))
        {
            builder.append(h).append("h:");
        }
        if (!m.equals("0"))
        {
            builder.append(m).append("m:");
        }

        builder.append(s).append("s");
        deepSleep = builder.toString();

        return deepSleep;
    }

    public static int getCpuTempPath()
    {
        int ret = 10;
        for (int i = 0; i < Constants.CPU_TEMP_PATHS.length; i++)
        {
            if (Constants.CPU_TEMP_PATHS[i].exists())
            {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static String cpuTemp(int path)
    {
        try
        {
            String temp = RCommand.readFileContent(Constants.CPU_TEMP_PATHS[path]).trim();
            if (temp.length() > 2)
            {
                return temp.substring(0, temp.length() - (temp.length() - 2));
            }
            else
            {
                return temp;
            }
        }
        catch (Exception e2)
        {
            return "0";
        }
    }

    public static String cpuInfo()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU_INFO).trim();
        }
        catch (Exception e2)
        {
            return "";
        }
    }

    public static List<String> availableGovs()
    {
        List<String> availableGovs = new ArrayList<>();

        if (Constants.GOVERNOR_SETTINGS.exists())
        {
            File[] files = Constants.GOVERNOR_SETTINGS.listFiles();

            for (File file : files)
            {
                availableGovs.add(file.getName());
            }
        }

        availableGovs.removeAll(Collections.singletonList("vdd_table"));
        return availableGovs;

    }

    public static List<String> govSettings()
    {

        List<String> govSettings = new ArrayList<String>();

        for (String s : availableGovs())
        {
            File gov = new File("/sys/devices/system/cpu/cpufreq/" + s + "/");

            if (gov.exists())
            {
                File[] files = gov.listFiles();
                if (files != null)
                {
                    for (File file : files)
                    {
                        govSettings.add(file.getName());
                    }
                }
            }
        }
        return govSettings;
    }

    public static String[] schedulersAsArray()
    {
        List<String> schedulers = schedulers();
        return schedulers.toArray(new String[schedulers.size()]);
    }

    public static List<String> schedulers()
    {
        List<String> schedulers = new ArrayList<>();
        try
        {
            String schedulersTemp = RCommand.readFileContent(Constants.SCHEDULER);
            String[] temp = schedulersTemp.replace("[", "").replace("]", "").split("\\s");
            Collections.addAll(schedulers, temp);
        }
        catch (Exception e)
        {

        }

        return schedulers;
    }

    public static String mpup()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_THR_UP).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpdown()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_THR_DOWN).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static GpuFreq gpu3DMaxFreq()
    {
        int freq = -1;
        try
        {
            File file1 = Constants.GPU_3D;
            File file2 = Constants.GPU_3D_2;
            if (file1.exists())
            {
                freq = Utility.parseInt(RCommand.readFileContent(file1).trim(), -1);
            }
            else if (file2.exists())
            {
                freq =  Utility.parseInt(RCommand.readFileContent(file2).trim(), -1);
            }
        }
        catch (Exception e)
        {
            freq = -1;
        }
        if(freq > 0)
            return new GpuFreq(freq);
        return null;
    }

    public static GpuFreq gpu3DCurFreq()
    {
        int freq = -1;
        try
        {
            File file1 = Constants.GPU_3D_CURRENT;
            File file2 = Constants.GPU_3D_2_CURRENT;
            if (file1.exists())
            {
                freq = Utility.parseInt(RCommand.readFileContent(file1).trim(), -1);
            }
            else if (file2.exists())
            {
                freq =  Utility.parseInt(RCommand.readFileContent(file2).trim(), -1);
            }
        }
        catch (Exception e)
        {
            freq = -1;
        }
        if(freq > 0)
            return new GpuFreq(freq);
        return null;
    }


    public static String gpu3dGovernor()
    {
        try
        {
            File file1 = Constants.GPU_3D_2_GOV;
            if (file1.exists())
            {
                return RCommand.readFileContent(file1).trim();
            }
            else
            {
                return "n/a";
            }
        }
        catch (Exception e)
        {
            return "n/a";
        }
    }

    public static String gpu2d()
    {
        try
        {
            return RCommand.readFileContent(Constants.GPU_2D).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String getGpu3dFrequenciesAsString()
    {
        List<GpuFreq> frequencies = gpu3dFrequenciesAsList();
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (GpuFreq fr : frequencies)
        {
            if (i != 0) builder.append(", ");
            builder.append(fr.formatted);
            i++;
        }
        return builder.toString();
    }

    public static List<GpuFreq> gpu3dFrequenciesAsList()
    {
        try
        {
            List<GpuFreq> frequencies = new ArrayList<>();
            File file1 = Constants.GPU_3D_2_AVAILABLE_FREQUENCIES;
            String[] frqs = RCommand.readFileContent(file1).trim().split(" ");
            Set<Integer> values = new HashSet<>();
            for (String freq : frqs)
            {
                int frInt = Utility.parseInt(freq, -1);
                if (frInt == -1 || values.contains(frInt)) continue;
                values.add(frInt);
                GpuFreq frequency = new GpuFreq(frInt);
                frequencies.add(frequency);
            }
            return frequencies;
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }


    public static int fcharge()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.FCHARGE).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static int vsync()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.VSYNC).trim(), -1);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static String cDepth()
    {
        try
        {
            return RCommand.readFileContent(Constants.CDEPTH).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String scheduler()
    {
        String scheduler = "";
        try
        {
            String schedulers = RCommand.readFileContent(Constants.SCHEDULER);
            scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]")).trim();
        }
        catch (Exception e)
        {

        }
        return scheduler;
    }

    public static int sdCache()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.SD_CACHE).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int s2w()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.S2W).trim(), -1);
        }
        catch (Exception e)
        {
            try
            {
                return Utility.parseInt(RCommand.readFileContent(Constants.S2W_ALT).trim(), -1);
            }
            catch (Exception e2)
            {
                return -1;
            }
        }
    }

    public static int dt2w()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.DT2W).trim(), -1);
        }
        catch (IOException e)
        {
            return -1;
        }
    }

    public static int readOTG()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.OTG).trim(), -1);
        }
        catch (Exception e)
        {
            try
            {
                return Utility.parseInt(RCommand.readFileContent(Constants.OTG_2).trim(), -1);
            }
            catch (Exception e2)
            {
                return -1;
            }
        }
    }

    public static String kernel()
    {
        try
        {
            return RCommand.readFileContent(Constants.KERNEL).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    static class CpuFreqSorter implements Comparator<CpuFreq>
    {
        public int compare(CpuFreq ob1, CpuFreq ob2)
        {
            return ob1.freq - ob2.freq;
        }
    }

    public static int batteryLevel()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.BATTERY_LEVEL).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static double batteryTemp()
    {
        try
        {
            File battTempFile1 = Constants.BATTERY_TEMP;
            File battTempFile2 = Constants.BATTERY_TEMP2;
            if (battTempFile1.exists())
            {
                return Double.parseDouble(RCommand.readFileContent(battTempFile1).trim());
            }
            else if (battTempFile2.exists())
            {
                return Double.parseDouble(RCommand.readFileContent(battTempFile2).trim()) / 10;
            }
            else
            {
                return 0.0;
            }
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }

    public static String batteryDrain()
    {
        try
        {
            File file1 = Constants.BATTERY_DRAIN;
            File file2 = Constants.BATTERY_DRAIN2;
            if (file1.exists())
            {
                return RCommand.readFileContent(file1).trim() + "mA";
            }
            else if (file2.exists())
            {
                return Utility.parseInt(RCommand.readFileContent(file2).trim(), 1000) / 1000 + "mA";
            }
            else
            {
                return "n/a";
            }

        }
        catch (Exception e)
        {
            return "n/a";
        }
    }

    public static int batteryVoltage()
    {
        try
        {
            File file1 = Constants.BATTERY_VOLTAGE;
            File file2 = Constants.BATTERY_VOLTAGE2;
            if (file1.exists())
            {
                return Utility.parseInt(RCommand.readFileContent(file1).trim(), 0);
            }
            else if (file2.exists())
            {
                return Utility.parseInt(RCommand.readFileContent(file2).trim(), 0) / 1000;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static String batteryTechnology()
    {
        try
        {
            return RCommand.readFileContent(Constants.BATTERY_TECH).trim();
        }
        catch (Exception e)
        {
            return "n/a";
        }
    }

    public static String batteryHealth()
    {
        try
        {
            return RCommand.readFileContent(Constants.BATTERY_HEALTH).trim();
        }
        catch (Exception e)
        {
            return "n/a";
        }
    }

    public static String batteryCapacity()
    {
        try
        {
            File file1 = Constants.BATTERY_CAPACITY;
            File file2 = Constants.BATTERY_CAPACITY2;
            if (file1.exists())
            {
                return RCommand.readFileContent(file1).trim() + "mAh";
            }
            else if (file2.exists())
            {
                return RCommand.readFileContent(file2).trim() + "mAh";
            }
            else
            {
                return "n/a";
            }
        }
        catch (Exception e)
        {
            return "n/a";
        }
    }

    /**
     * @return 2 if charging from AC
     */
    public static int batteryChargingSource()
    {
        try
        {
            return Utility.parseInt(RCommand.readFileContent(Constants.BATTERY_CHARGING_SOURCE).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static boolean isTempEnabled()
    {
        try
        {
            return RCommand.readFileContent(Constants.CPU_TEMP_ENABLED).equals("enabled");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static String mpDelay()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_DELAY).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpPause()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_PAUSE).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpTimeUp()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_TIME_UP).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpTimeDown()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_TIME_DOWN).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpIdleFreq()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_IDLE_FREQ).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpScroffFreq()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_SCROFF_FREQ).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String mpScroffSingleCore()
    {
        try
        {
            return RCommand.readFileContent(Constants.MPDEC_SCROFF_SINGLE).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static String thermalLowLow()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_LOW_LOW).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalLowHigh()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_LOW_HIGH).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMidLow()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MID_LOW).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMidHigh()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MID_HIGH).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMaxLow()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MAX_LOW).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMaxHigh()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MAX_HIGH).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalLowFreq()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_LOW_FREQ).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMidFreq()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MID_FREQ).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String thermalMaxFreq()
    {
        try
        {
            return RCommand.readFileContent(Constants.THERMAL_MAX_FREQ).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }
    public static long sizeOf(File file)
    {
        try
        {
            return FileUtils.sizeOf(file);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getEntropyAvailable()
    {
        try
        {
            return Utility.parseInt(FileUtils.readFileToString(Constants.ENTROPY_AVAILABLE).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getEntropyPoolSize()
    {
        try
        {
            return Utility.parseInt(FileUtils.readFileToString(Constants.ENTROPY_POOL_SIZE).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getEntropyReadThreshold()
    {
        try
        {
            return Utility.parseInt(FileUtils.readFileToString(Constants.ENTROPY_READ_THRESHOLD).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getEntropyWriteThreshold()
    {
        try
        {
            return Utility.parseInt(FileUtils.readFileToString(Constants.ENTROPY_WRITE_THRESHOLD).trim(), 0);
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}
