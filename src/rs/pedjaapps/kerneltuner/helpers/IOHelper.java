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
package rs.pedjaapps.kerneltuner.helpers;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.model.Frequency;
import rs.pedjaapps.kerneltuner.model.TimesEntry;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.utility.Tools;

public class IOHelper
{

    public static boolean freqsExists()
    {
        boolean i = false;
        if (new File(Constants.CPU0_FREQS).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean oomExists()
    {
        boolean i = false;
        if (new File(Constants.OOM).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean thermaldExists()
    {
        boolean i = false;
        if (new File(Constants.THERMALD).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean swapsExists()
    {
        boolean i = false;
        if (new File(Constants.SWAPS).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean cpu0Exists()
    {
        return new File(Constants.cpu0online).exists();
    }

    public static boolean cpu1Exists()
    {
        return new File(Constants.cpu1online).exists();
    }

    public static boolean cpu2Exists()
    {
        return new File(Constants.cpu2online).exists();
    }

    public static boolean cpu3Exists()
    {
        return new File(Constants.cpu3online).exists();
    }

    public static boolean cpuScreenOff()
    {
        return new File(Constants.cpuScreenOff).exists();
    }

    public static boolean cpuOnline(int cpu)
    {
		return new File("/sys/devices/system/cpu/cpu" + cpu + "/cpufreq/scaling_governor").exists();
    }

    public static boolean gpuExists()
    {
        File file1 = new File(Constants.GPU_3D);
        File file2 = new File(Constants.GPU_3D_2);
        return file1.exists() || file2.exists();
    }

    public static boolean cdExists()
    {
        boolean i = false;
        if (new File(Constants.CDEPTH).exists())
        {
            i = true;
        }
        return i;
    }

    public static boolean tcpCongestionControlAvailable()
    {
        return new File(Constants.TCP_CONGESTION).exists() && new File(Constants.TCP_AVAILABLE_CONGESTION).exists();
    }

    public static boolean voltageExists()
    {
        boolean i = false;
        if (new File(Constants.VOLTAGE_PATH).exists())
        {
            i = true;
        }
        else if (new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean otgExists()
    {
        boolean i = false;
        if (new File(Constants.OTG).exists())
        {
            i = true;
        }
        else if (new File(Constants.OTG_2).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean s2wExists()
    {
        boolean i = false;
        if (new File(Constants.S2W).exists())
        {
            i = true;
        }
        else if (new File(Constants.S2W_ALT).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean dt2wExists()
    {
        return new File(Constants.DT2W).exists();
    }

    public static boolean TISExists()
    {
        boolean i = false;
        if (new File(Constants.TIMES_IN_STATE_CPU0).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean mpdecisionExists()
    {
        boolean i = false;
        if (new File(Constants.MPDECISION).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean buttonsExists()
    {
        boolean i = false;
        if (new File(Constants.BUTTONS_LIGHT).exists())
        {
            i = true;
        }
        else if (new File(Constants.BUTTONS_LIGHT_2).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean sdcacheExists()
    {
        boolean i = false;
        if (new File(Constants.SD_CACHE).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean vsyncExists()
    {
        boolean i = false;
        if (new File(Constants.VSYNC).exists())
        {
            i = true;
        }
        return i;

    }

    public static boolean fchargeExists()
    {
        boolean i = false;
        if (new File(Constants.FCHARGE).exists())
        {
            i = true;
        }
        return i;

    }

    public static List<Frequency> frequencies()
    {
        List<Frequency> entries = new ArrayList<Frequency>();
        try
        {
            File myFile = new File(Constants.CPU0_FREQS);
            FileInputStream fIn = new FileInputStream(myFile);

            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            while ((aDataRow = myReader.readLine()) != null)
            {
                String[] freqs = aDataRow.split(" ");
                for (String s : freqs)
                {
                    Frequency frequency = new Frequency();
                    int value = Tools.parseInt(s, Constants.CPU_OFFLINE_CODE);
                    if (value == Constants.CPU_OFFLINE_CODE) continue;
                    String string = null;
                    if (aDataRow.length() > 3)
                    {
                        string = s.trim().substring(0, s.trim().length() - 3) + "MHz";
                    }
                    if (TextUtils.isEmpty(string)) continue;
                    frequency.setFrequencyString(string);
                    frequency.setFrequencyValue(value);
                    entries.add(frequency);
                }
            }
            myReader.close();
            fIn.close();
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
                    String freq = delims[0];

                    Frequency frequency = new Frequency();
                    int value = Tools.parseInt(freq, Constants.CPU_OFFLINE_CODE);
                    if (value == Constants.CPU_OFFLINE_CODE) continue;
                    String string = null;
                    if (freq.length() > 3)
                    {
                        string = freq.trim().substring(0, freq.trim().length() - 3) + "MHz";
                    }
                    if (TextUtils.isEmpty(string)) continue;
                    frequency.setFrequencyString(string);
                    frequency.setFrequencyValue(value);
                    entries.add(frequency);
                }
                Collections.sort(entries, new MyComparator());
                in.close();
                fstream.close();
                br.close();
            }
            catch (Exception ee)
            {
                Crashlytics.logException(ee);
                e.printStackTrace();
            }
        }
        return entries;

    }

    public static String[] getTcpAvailableCongestion()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.TCP_AVAILABLE_CONGESTION)).trim().split(" ");
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
            return FileUtils.readFileToString(new File(Constants.TCP_CONGESTION)).trim();
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
            return Arrays.asList(FileUtils.readFileToString(new File(Constants.OOM)).split(","));
        }
        catch (Exception e)
        {
            return new ArrayList<String>();
        }

    }

    public static String leds()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.BUTTONS_LIGHT_2)).trim();
        }
        catch (Exception e)
        {
            try
            {
                return FileUtils.readFileToString(new File(Constants.BUTTONS_LIGHT)).trim();
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
            return FileUtils.readFileToString(new File(Constants.CPU0_GOVS)).split("\\s");
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
            return Arrays.asList(FileUtils.readFileToString(new File(Constants.CPU0_GOVS)).split("\\s"));
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
            return Tools.parseInt(/*FileUtils.readFileToString(new File(Constants.CPU0_MIN_FREQ)).trim()*/RCommand.readFileContent(Constants.CPU0_MIN_FREQ), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }

    }

    public static String cpuMin()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU_MIN)).trim();
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
            return FileUtils.readFileToString(new File(Constants.CPU_MAX)).trim();
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
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU0_MAX_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }

    }

    public static int cpu1MinFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU1_MIN_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }
    }

    public static int cpu1MaxFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU1_MAX_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }
    }

    public static int cpu2MinFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU2_MIN_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }

    }

    public static int cpu2MaxFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU2_MAX_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }
    }

    public static int cpu3MinFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU3_MIN_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }

    }

    public static int cpu3MaxFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.CPU3_MAX_FREQ)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }

    }

    public static String cpu0CurFreq()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU0_CURR_FREQ)).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpu1CurFreq()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU1_CURR_FREQ)).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }
    }

    public static String cpu2CurFreq()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU2_CURR_FREQ)).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }

    }

    public static String cpu3CurFreq()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU3_CURR_FREQ)).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }

    }

    public static int cpuScreenOffMaxFreq()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.cpuScreenOff)).trim(), Constants.CPU_OFFLINE_CODE);
        }
        catch (Exception e)
        {
            return Constants.CPU_OFFLINE_CODE;
        }
    }

    public static String cpu0CurGov()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU0_CURR_GOV)).trim();
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
            return FileUtils.readFileToString(new File(Constants.CPU1_CURR_GOV)).trim();
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
            return FileUtils.readFileToString(new File(Constants.CPU2_CURR_GOV)).trim();
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
            return FileUtils.readFileToString(new File(Constants.CPU3_CURR_GOV)).trim();
        }
        catch (Exception e)
        {
            return "offline";
        }

    }


    public static List<TimesEntry> getTis()
    {
        List<TimesEntry> times = new ArrayList<TimesEntry>();

        try
        {

            FileInputStream fstream = new FileInputStream(Constants.TIMES_IN_STATE_CPU0);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {
                String[] delims = strLine.split(" ");
                times.add(new TimesEntry(Integer.parseInt(delims[0]), Long.parseLong(delims[1])));
                //System.out.println(strLine);
            }
            in.close();
            fstream.close();
            br.close();
        }
        catch (Exception e)
        {
            Log.e("Error: ", e.getMessage());
        }

        return times;

    }


    public static List<Voltage> voltages()
    {
        List<Voltage> voltages = new ArrayList<Voltage>();
        if (new File(Constants.VOLTAGE_PATH).exists())
        {
            parseVoltageM1(voltages, Constants.VOLTAGE_PATH);
        }
        if (new File(Constants.VOLTAGE_PATH_2).exists())
        {
            parseVoltageM1(voltages, Constants.VOLTAGE_PATH_2);
        }
        else if (new File(Constants.VOLTAGE_PATH_TEGRA_3).exists())
        {
            parseVoltageM2(voltages, Constants.VOLTAGE_PATH_TEGRA_3);
        }
        return voltages;
    }

    private static void parseVoltageM2(List<Voltage> voltages, String voltagePathTegra3)
    {
        try
        {
            FileInputStream fstream = new FileInputStream(voltagePathTegra3);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {
                String[] delims = strLine.split("\\s");
                if (delims.length < 2) continue;
                Voltage voltage = new Voltage();
                String name, frequency = null;
                if (delims[0].length() > 4)
                {
                    frequency = delims[0].substring(0, delims[0].length() - 4).trim() + "MHz";
                }
                name = delims[0].substring(0, delims[0].length() - 4);
                int value = Tools.parseInt(delims[1], Constants.CPU_OFFLINE_CODE);
                if (name == null || frequency == null || value == Constants.CPU_OFFLINE_CODE)
                {
                    continue;
                }
                voltage.setFreq(frequency);
                voltage.setName(name);
                voltage.setValue(value);
                voltage.setDivider(1);
                voltage.setMultiplier(1000);
                voltages.add(voltage);
            }

            in.close();
            fstream.close();
            br.close();
        }
        catch (Exception ex)
        {

        }
    }

    private static void parseVoltageM1(List<Voltage> voltages, String path)
    {
        try
        {
            FileInputStream fstream = new FileInputStream(path);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {
                strLine = strLine.trim().replaceAll("\\s+", "");
                Voltage voltage = new Voltage();
                String name, frequency;
                String[] vf = strLine.split(":");
                if(vf.length != 2)continue;
                int frInt = Tools.parseInt(vf[0], -1);
                if(frInt < 0)continue;
                frequency = frInt / 1000 + "MHz";
                int value = Tools.parseInt(vf[1], -1);
                if(value < 0)continue;
                name = value / 1000 + "mV";

                voltage.setFreq(frequency);
                voltage.setName(name);
                voltage.setValue(value);
                voltage.setFreqValue(frInt);
                voltage.setDivider(1000);
                voltage.setMultiplier(1);
                voltages.add(voltage);
            }

            in.close();
            fstream.close();
            br.close();
        }
        catch (Exception e)
        {

        }
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

    public static final String deepSleep()
    {
        String deepSleep;

        int time = (int) (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());

        String s = ((int) ((time / 1000) % 60)) + "";
        String m = ((int) ((time / (1000 * 60)) % 60)) + "";
        String h = ((int) ((time / (1000 * 3600)) % 24)) + "";
        String d = ((int) (time / (1000 * 60 * 60 * 24))) + "";
        StringBuilder builder = new StringBuilder();
        if (!d.equals("0"))
        {
            builder.append(d + "d:");
        }
        if (!h.equals("0"))
        {
            builder.append(h + "h:");
        }
        if (!m.equals("0"))
        {
            builder.append(m + "m:");
        }

        builder.append(s + "s");
        deepSleep = builder.toString();

        return deepSleep;
    }

    public static int getCpuTempPath()
    {
        int ret = 10;
        for (int i = 0; i < Constants.CPU_TEMP_PATHS.length; i++)
        {
            if (new File(Constants.CPU_TEMP_PATHS[i]).exists())
            {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static final String cpuTemp(int path)
    {
        try
        {
            String temp = FileUtils.readFileToString(new File(Constants.CPU_TEMP_PATHS[path])).trim();
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

    public static final String cpuInfo()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.CPU_INFO)).trim();
        }
        catch (Exception e2)
        {
            return "";
        }
    }

    public static List<String> availableGovs()
    {
        File govs = new File("/sys/devices/system/cpu/cpufreq/");
        List<String> availableGovs = new ArrayList<>();

        if (govs.exists())
        {
            File[] files = govs.listFiles();

            for (File file : files)
            {
                availableGovs.add(file.getName());


            }
        }

        availableGovs.removeAll(Arrays.asList("vdd_table"));
        return availableGovs;

    }

    public static final List<String> govSettings()
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

        List<String> schedulers = new ArrayList<String>();

        try
        {

            File myFile = new File(Constants.SCHEDULER);
            FileInputStream fIn = new FileInputStream(myFile);

            BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null)
            {
                aBuffer += aDataRow + "\n";
            }

            String schedulersTemp = aBuffer;
            myReader.close();
            fIn.close();

            schedulersTemp = schedulersTemp.replace("[", "");
            schedulersTemp = schedulersTemp.replace("]", "");
            String[] temp = schedulersTemp.split("\\s");
            for (String s : temp)
            {
                schedulers.add(s);
            }
        }
        catch (Exception e)
        {

        }

        return schedulers;
    }

    public static final String mpup()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.MPDEC_THR_UP)).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static final String mpdown()
    {
        try
        {
            return FileUtils.readFileToString(new File(Constants.MPDEC_THR_DOWN)).trim();
        }
        catch (Exception e)
        {
            return "err";
        }
    }

    public static int gpu3d()
    {
        try
        {
            File file1 = new File(Constants.GPU_3D);
            File file2 = new File(Constants.GPU_3D_2);
            if (file1.exists())
            {
                return Tools.parseInt(FileUtils.readFileToString(file1).trim(), Constants.CPU_OFFLINE_CODE);
            }
            else if (file2.exists())
            {
                return Tools.parseInt(FileUtils.readFileToString(file2).trim(), Constants.GPU_OFFLINE_CODE);
            }
            else
            {
                return Constants.GPU_NOT_AVAILABLE;
            }
        }
        catch (Exception e)
        {
            return Constants.GPU_NOT_AVAILABLE;
        }
    }

	public static String gpu3dGovernor()
    {
        try
        {
            File file1 = new File(Constants.GPU_3D_2_GOV);
            if (file1.exists())
            {
                return FileUtils.readFileToString(file1).trim();
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
            return FileUtils.readFileToString(new File(Constants.GPU_2D)).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String getGpu3dFrequenciesAsString()
    {
        List<Frequency> frequencies = gpu3dFrequenciesAsList();
        StringBuilder builder = new StringBuilder();
        int i = 0;
		for (Frequency fr : frequencies)
        {
            if (i != 0)builder.append(", ");
            builder.append(fr.getFrequencyString());
            i++;
        }
        return builder.toString();
    }

    public static List<Frequency> gpu3dFrequenciesAsList()
    {
        try
        {
            List<Frequency> frequencies = new ArrayList<>();
            File file1 = new File(Constants.GPU_3D_AVAILABLE_FREQUENCIES);
            String[] frqs = FileUtils.readFileToString(file1).trim().split(" ");
			Set<Integer> values = new HashSet<>();
            for (String freq : frqs)
            {
                int frInt = Tools.parseInt(freq, -1);
                if (frInt == -1 || values.contains(frInt))continue;
				values.add(frInt);
                Frequency frequency = new Frequency();
                frequency.setFrequencyValue(frInt);
                frequency.setFrequencyString(frInt / 1000000 + "MHz");
                frequencies.add(frequency);
            }
            return frequencies;
        }
        catch (Exception e)
        {
            return new ArrayList<Frequency>();
        }
    }


    public static int fcharge()
    {
        try
        {
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.FCHARGE)).trim(), -1);
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
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.VSYNC)).trim(), -1);
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
            return FileUtils.readFileToString(new File(Constants.CDEPTH)).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static final String scheduler()
    {
        String scheduler = "";
        try
        {

            File myFile = new File(Constants.SCHEDULER);
            FileInputStream fIn = new FileInputStream(myFile);

            BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null)
            {
                aBuffer += aDataRow + "\n";
            }

            String schedulers = aBuffer;
            myReader.close();

            scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
            scheduler.trim();
            fIn.close();

        }
        catch (Exception e)
        {

        }
        return scheduler;
    }

    public static final int sdCache()
    {
        try
        {
            return Integer.parseInt(FileUtils.readFileToString(new File(Constants.SD_CACHE)).trim());
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
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.S2W)).trim(), -1);
        }
        catch (Exception e)
        {
            try
            {
                return Tools.parseInt(FileUtils.readFileToString(new File(Constants.S2W_ALT)).trim(), -1);
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
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.DT2W)).trim(), -1);
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
            return Tools.parseInt(FileUtils.readFileToString(new File(Constants.OTG)).trim(), -1);
        }
        catch (Exception e)
        {
            try
            {
                return Tools.parseInt(FileUtils.readFileToString(new File(Constants.OTG_2)).trim(), -1);
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
            return FileUtils.readFileToString(new File(Constants.KERNEL)).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    static class MyComparator implements Comparator<Frequency>
    {
        public int compare(Frequency ob1, Frequency ob2)
        {
            return ob1.getFrequencyValue() - ob2.getFrequencyValue();
        }
    }

    public static int batteryLevel()
    {
        try
        {
            return Integer.parseInt(FileUtils.readFileToString(new File(Constants.BATTERY_LEVEL)).trim());
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
            File battTempFile1 = new File(Constants.BATTERY_TEMP);
            File battTempFile2 = new File(Constants.BATTERY_TEMP2);
            if (battTempFile1.exists())
            {
                return Double.parseDouble(FileUtils.readFileToString(battTempFile1).trim());
            }
            else if (battTempFile2.exists())
            {
                return Double.parseDouble(FileUtils.readFileToString(battTempFile2).trim()) / 10;
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
            File file1 = new File(Constants.BATTERY_DRAIN);
            File file2 = new File(Constants.BATTERY_DRAIN2);
            if (file1.exists())
            {
                return FileUtils.readFileToString(file1).trim() + "mA";
            }
            else if (file2.exists())
            {
                return Tools.parseInt(FileUtils.readFileToString(file2).trim(), 1000) / 1000 + "mA";
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
            File file1 = new File(Constants.BATTERY_VOLTAGE);
            File file2 = new File(Constants.BATTERY_VOLTAGE2);
            if (file1.exists())
            {
                return Integer.parseInt(FileUtils.readFileToString(file1).trim());
            }
            else if (file2.exists())
            {
                return Integer.parseInt(FileUtils.readFileToString(file2).trim()) / 1000;
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
            return FileUtils.readFileToString(new File(Constants.BATTERY_TECH)).trim();
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
            return FileUtils.readFileToString(new File(Constants.BATTERY_HEALTH)).trim();
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
            File file1 = new File(Constants.BATTERY_CAPACITY);
            File file2 = new File(Constants.BATTERY_CAPACITY2);
            if (file1.exists())
            {
                return FileUtils.readFileToString(file1).trim() + "mAh";
            }
            else if (file2.exists())
            {
                return FileUtils.readFileToString(file2).trim() + "mAh";
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
            return Integer.parseInt(FileUtils.readFileToString(new File(Constants.BATTERY_CHARGING_SOURCE)).trim());
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
            if (FileUtils.readFileToString(new File(Constants.CPU_TEMP_ENABLED)).equals("enabled"))
            {
                return true;
            }
            else
            {
                return false;
            }
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_DELAY)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_PAUSE)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_TIME_UP)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_TIME_DOWN)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_IDLE_FREQ)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_FREQ)).trim();
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
            return FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_SINGLE)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_LOW)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_HIGH)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MID_LOW)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MID_HIGH)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_LOW)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_HIGH)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_LOW_FREQ)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MID_FREQ)).trim();
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
            return FileUtils.readFileToString(new File(Constants.THERMAL_MAX_FREQ)).trim();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    static int load;

    public static int cpuLoad()
    {
        // Do something long

        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                float fLoad = 0;
                try
                {
                    RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
                    String load = reader.readLine();

                    String[] toks = load.split(" ");

                    long idle1 = Long.parseLong(toks[5]);
                    long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
						+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

                    try
                    {
                        Thread.sleep(360);
                    }
                    catch (Exception e)
                    {
                    }

                    reader.seek(0);
                    load = reader.readLine();
                    reader.close();

                    toks = load.split(" ");

                    long idle2 = Long.parseLong(toks[5]);
                    long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
						+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

                    fLoad = (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                load = (int) (fLoad * 100);


            }
        };
        new Thread(runnable).start();
        return load;
    }

}
