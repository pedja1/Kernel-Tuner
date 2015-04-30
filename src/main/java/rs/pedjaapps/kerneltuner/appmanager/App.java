package rs.pedjaapps.kerneltuner.appmanager;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by pedja on 27.12.14..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2014
 */
public class App
{
    public String appLabel, packageName, versionName;
    public int versionCode;
    public Drawable icon;
    public long size;
    public PackageInfo pi;
}
