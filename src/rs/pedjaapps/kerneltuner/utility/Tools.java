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
package rs.pedjaapps.kerneltuner.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import rs.pedjaapps.kerneltuner.R;

public class Tools
{

    public static String byteToHumanReadableSize(long size)
    {
        String hrSize = "0.00B";
        double k = size / 1024.0;
        double m = size / 1048576.0;
        double g = size / 1073741824.0;
        double t = size / 1099511627776.0;

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1)
        {
            hrSize = dec.format(t).concat("TB");
        }
        else if (g > 1)
        {
            hrSize = dec.format(g).concat("GB");
        }
        else if (m > 1)
        {
            hrSize = dec.format(m).concat("MB");
        }
        else if (k > 1)
        {
            hrSize = dec.format(k).concat("KB");
        }
        else if (size > 1)
        {
            hrSize = dec.format(size).concat("B");
        }
        return hrSize;

    }

    public static String kByteToHumanReadableSize(int size)
    {
        String hrSize = "";
        double m = size / 1024.0;
        double g = size / 1048576.0;
        double t = size / 1073741824.0;

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1)
        {
            hrSize = dec.format(t).concat("TB");
        }
        else if (g > 1)
        {
            hrSize = dec.format(g).concat("GB");
        }
        else if (m > 1)
        {
            hrSize = dec.format(m).concat("MB");
        }
        else if (size > 1)
        {
            hrSize = dec.format(size).concat("KB");
        }

        return hrSize;

    }

    public static String msToDate(long ms)
    {
        SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");
        return f.format(ms);
    }

    public static String msToDateSimple(long ms)
    {
        SimpleDateFormat f = new SimpleDateFormat("ddMMyyHHmmss");
        return f.format(ms);
    }

    public static String mbToPages(int progress)
    {
        return (progress * 1024 / 4) + "";
    }

    public static int pagesToMb(int pages)
    {
        return pages / 1024 * 4;

    }

    public static long getAvailableSpaceInBytesOnInternalStorage()
    {
        long availableSpace;
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        availableSpace = (long) stat.getAvailableBlocks()
                * (long) stat.getBlockSize();

        return availableSpace;
    }

    public static long getUsedSpaceInBytesOnInternalStorage()
    {
        long usedSpace;
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
                * (long) stat.getBlockSize();

        return usedSpace;
    }

    public static long getTotalSpaceInBytesOnInternalStorage()
    {
        long usedSpace;
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

        return usedSpace;
    }

    public static long getAvailableSpaceInBytesOnExternalStorage()
    {
        long availableSpace;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        availableSpace = (long) stat.getAvailableBlocks()
                * (long) stat.getBlockSize();

        return availableSpace;
    }

    public static long getUsedSpaceInBytesOnExternalStorage()
    {
        long usedSpace;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
                * (long) stat.getBlockSize();

        return usedSpace;
    }

    public static long getTotalSpaceInBytesOnExternalStorage()
    {
        long usedSpace;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

        return usedSpace;
    }

    /**
     * cTemp = temperature in celsius tempPreff = string from shared
     * preferences with value fahrenheit, celsius or kelvin
     */
    public static String tempConverter(String tempPref, double cTemp)
    {
        String tempNew = "";

        if (tempPref.equals("fahrenheit"))
        {
            tempNew = ((cTemp * 1.8) + 32) + "°F";

        }
        else if (tempPref.equals("celsius"))
        {
            tempNew = cTemp + "°C";

        }
        else if (tempPref.equals("kelvin"))
        {

            tempNew = (cTemp + 273.15) + "°C";

        }
        return tempNew;
    }

    public static String msToHumanReadableTime(long time)
    {

        String timeString;
        String s = "" + ((int) ((time / 1000) % 60));
        String m = "" + ((int) ((time / (1000 * 60)) % 60));
        String h = "" + ((int) ((time / (1000 * 3600)) % 24));
        String d = "" + ((int) (time / (1000 * 60 * 60 * 24)));
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
        timeString = builder.toString();
        return timeString;
    }

    public static String msToHumanReadableTime2(long time)
    {

        String timeString;
        String s = "" + ((int) ((time / 100) % 60));
        String m = "" + ((int) ((time / (100 * 60)) % 60));
        String h = "" + ((int) ((time / (100 * 3600)) % 24));
        String d = "" + ((int) (time / (100 * 60 * 60 * 24)));
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
        timeString = builder.toString();
        return timeString;
    }

    public static String getAbi()
    {
        String abi = android.os.Build.CPU_ABI;
        if (abi.contains("armeabi"))
        {
            return "arm";
        }
        else if (abi.contains("x86"))
        {
            return "x86";
        }
        else if (abi.contains("mips"))
        {
            return "mips";
        }
        else
        {
            return "arm";
        }
    }

    public static String getProcessStatus(String status)
    {
        switch (status)
        {
            case "S":
                return "Sleeping";
            case "D":
                return "Uninterruptible";
            case "R":
                return "Running";
            case "T":
                return "Stopped";
            case "X":
                return "Terminated";
            case "Z":
                return "Zombie";
            default:
                return "";
        }

    }


    public static int parseInt(String value, int def)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception e)
        {
            return def;
        }
    }

    public static long parseLong(String value, long def)
    {
        try
        {
            return Long.parseLong(value);
        }
        catch (Exception e)
        {
            return def;
        }
    }


    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, String message,
                                                     String title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title != null ? Html.fromHtml(title) : null);
        builder.setMessage(message != null ? Html.fromHtml(message) : null);
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * General Purpose AlertDialog
     */
    public static AlertDialog showMessageAlertDialog(Context context, int message,
                                                     int title, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title > 0) builder.setTitle(Html.fromHtml(context.getString(title)));
        builder.setMessage(Html.fromHtml(context.getString(message)));
        builder.setPositiveButton(android.R.string.ok, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static void paypalDonate(Activity activity)
    {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
        uriBuilder.appendQueryParameter("cmd", "_donations");

        uriBuilder.appendQueryParameter("business", "pcokulov@gmail.com");
        uriBuilder.appendQueryParameter("lc", "US");
        uriBuilder.appendQueryParameter("item_name", "Kernel Tuner Donate");
        uriBuilder.appendQueryParameter("no_note", "1");
        uriBuilder.appendQueryParameter("no_shipping", "1");
        uriBuilder.appendQueryParameter("currency_code", "USD");
        Uri payPalUri = uriBuilder.build();

        // Start your favorite browser
        try
        {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, payPalUri);
            activity.startActivity(viewIntent);
        }
        catch (ActivityNotFoundException e)
        {
            showMessageAlertDialog(activity,R.string.donations__alert_dialog_no_browser,
                    R.string.donations__alert_dialog_title, null);
        }
    }
}

