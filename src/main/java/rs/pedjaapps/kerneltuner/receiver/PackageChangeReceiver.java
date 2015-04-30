package rs.pedjaapps.kerneltuner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import rs.pedjaapps.kerneltuner.ui.AbsActivity;
import rs.pedjaapps.kerneltuner.appmanager.ApplicationManagerActivity;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;

/**
 * Created by pedja on 11.4.14..
 *
 * This file is part of ${PROJECT_NAME}
 * Copyright Predrag Čokulov 2014
 */
public class PackageChangeReceiver extends BroadcastReceiver
{
    public static final String PRO_PACKAGE_NAME = "rs.pedjaapps.kerneltunerpro.app";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent == null || intent.getAction() == null /*|| intent.getExtras() == null*/)
        {
            return;
        }
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)
                || intent.getAction().equals(Intent.ACTION_PACKAGE_DATA_CLEARED)
                || intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)
                || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED))
        {
            ApplicationManagerActivity.cache.clear();
        }
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED))
        {
            boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
            String pn = intent.getData().toString().split(":")[1];
            if(!replacing && PRO_PACKAGE_NAME.equalsIgnoreCase(pn))
            {
                PrefsManager.setPro(false);
                Intent removeAdsIntent = new Intent(AbsActivity.ACTION_TOGGLE_PRO_VERSION);
                LocalBroadcastManager.getInstance(context).sendBroadcast(removeAdsIntent);
            }
            ApplicationManagerActivity.cache.clear();
        }
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
        {
            boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
            String pn = intent.getData().toString().split(":")[1];
            if(!replacing && PRO_PACKAGE_NAME.equalsIgnoreCase(pn))
            {
                PrefsManager.setPro(true);
                Intent removeAdsIntent = new Intent(AbsActivity.ACTION_TOGGLE_PRO_VERSION);
                LocalBroadcastManager.getInstance(context).sendBroadcast(removeAdsIntent);
            }
            ApplicationManagerActivity.cache.clear();
        }
    }
}
