package rs.pedjaapps.kerneltuner.appmanager;

import java.io.IOException;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.Tools;

/**
 * Created by pedja on 27.12.14..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2014
 */
public class AppManagerUtility
{
    public static Pattern duSizePattern = Pattern.compile("(\\d+)\\s+.+");
    private AppManagerUtility()
    {
    }

    public static void getAppSize(App app, boolean fullSize)
    {
        long total = 0;

        try
        {
            String apkSizeString = new RootUtils().execAndWait("du -sk " + app.pi.applicationInfo.sourceDir);
            Matcher matcher = duSizePattern.matcher(apkSizeString);
            if(matcher.find())
            {
                total += Tools.parseLong(matcher.group(1), 0);
            }
            if(fullSize)
            {
                String dirSize = new RootUtils().execAndWait("du -sk " + app.pi.applicationInfo.dataDir);
                matcher = duSizePattern.matcher(dirSize);
                if(matcher.find())
                {
                    total += Tools.parseLong(matcher.group(1), 0);
                }
            }
            app.size = total;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static class AppSorter implements Comparator<App>
    {
        enum SortBy
        {
            name, size
        }

        SortBy sortBy;

        public AppSorter(SortBy sortBy)
        {
            this.sortBy = sortBy;
            if(sortBy == null)throw new IllegalArgumentException("sortBy cannot be null");
        }

        @Override
        public int compare(App lhs, App rhs)
        {
            if(sortBy == SortBy.name)
            {
                return lhs.appLabel.compareTo(rhs.appLabel);
            }
            else
            {
                if(lhs.size > rhs.size) return 1;
                if(lhs.size < rhs.size) return -1;
            }
            return 0;
        }
    }
}
