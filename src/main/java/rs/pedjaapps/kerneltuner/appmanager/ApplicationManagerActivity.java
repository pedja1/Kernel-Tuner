package rs.pedjaapps.kerneltuner.appmanager;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.appmanager.AppAdapter;
import rs.pedjaapps.kerneltuner.appmanager.App;
import rs.pedjaapps.kerneltuner.ui.AbsActivity;

/**
 * Created by pedja on 27.12.14..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2014
 */
public class ApplicationManagerActivity extends AbsActivity
{
    ListView lvApps;
    AppAdapter mAdapter;

    ProgressBar pbLoading;
    LinearLayout llLoading;

    ATLoadApps atLoadApps;

    public static final List<App> cache = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_manager);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);

        lvApps = (ListView) findViewById(R.id.lvApps);
        mAdapter = new AppAdapter(this, new ArrayList<App>());
        lvApps.setAdapter(mAdapter);

        atLoadApps = new ATLoadApps();
        atLoadApps.execute();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(atLoadApps != null)
        {
            atLoadApps.cancel(true);
            atLoadApps.canceled = true;
        }
    }

    class ATLoadApps extends AsyncTask<String, Integer, Void>
    {
        Filter filter;

        ArrayList<App> apps;

        public static final int PROGRESS_SET_MAX = 0;
        public static final int PROGRESS_SET_PROGRESS = 1;
        public static final int PROGRESS_SHOW = 2;
        public static final int PROGRESS_UPDATE = 3;

        public ATLoadApps(Filter filter)
        {
            this.filter = filter;
        }

        public ATLoadApps()
        {
        }

        public boolean canceled = false;

        @Override
        protected Void doInBackground(String... params)
        {
            if(!cache.isEmpty())
            {
                apps = new ArrayList<>(cache);
                return null;
            }
            apps = new ArrayList<>();
            PackageManager pm = getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(/*GET_APPS_FLAGS*/0);
            publishProgress(PROGRESS_SET_MAX, packs.size());
            int offset = 0;
            for (PackageInfo pi : packs)
            {
                if(canceled)return null;
                if (filter == null || filter.matches(pi))
                {
                    App app = new App();
                    app.appLabel = pi.applicationInfo.loadLabel(pm).toString();
                    app.packageName = pi.packageName;
                    app.versionCode = pi.versionCode;
                    app.icon = pi.applicationInfo.loadIcon(pm);
                    app.versionName = pi.versionName;
                    app.pi = pi;
                    apps.add(app);
                }
                publishProgress(PROGRESS_SET_PROGRESS, offset);
                offset++;
            }
            publishProgress(PROGRESS_UPDATE, 0);

            //calculate size for each app
            for(App app : apps)
            {
                if(canceled)return null;
                AppManagerUtility.getAppSize(app, true);
                publishProgress(PROGRESS_UPDATE, 0);
            }
            cache.clear();
            cache.addAll(apps);
            return null;
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(Void res)
        {
            Collections.sort(apps, new AppManagerUtility.AppSorter(AppManagerUtility.AppSorter.SortBy.name));
            mAdapter.clear();
            mAdapter.addAll(apps);
            mAdapter.notifyDataSetChanged();
            llLoading.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            if (values == null || values.length < 2) return;
            switch (values[0])
            {
                case PROGRESS_SET_MAX:
                    pbLoading.setMax(values[1]);
                    break;
                case PROGRESS_SET_PROGRESS:
                    pbLoading.setProgress(values[1]);
                    break;
                case PROGRESS_UPDATE:
                    mAdapter.clear();
                    mAdapter.addAll(apps);
                    mAdapter.notifyDataSetChanged();
                case PROGRESS_SHOW:
                    llLoading.setVisibility(View.GONE);
                    mAdapter.clear();
                    mAdapter.addAll(apps);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    static class Filter
    {
        enum InstallStatus
        {
            installed, uninstalled, all
        }

        enum Type
        {
            system, user, all
        }

        Activity activity;
        //String packageName, name;
        Type type;
        InstallStatus installStatus;

        public Filter(Activity activity)
        {
            this.activity = activity;
        }

        public boolean matches(PackageInfo pi)
        {
            //check type first
            boolean isSystem = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if (type == Type.system && !isSystem)
                return false;
            if (type == Type.user && isSystem)
                return false;

            //check install status
            boolean installed = isAppInstalled(pi);
            if(installStatus == InstallStatus.installed && !installed)
                return false;
            if(installStatus == InstallStatus.uninstalled && installed)
                return false;

            return true;
        }

        private boolean isAppInstalled(PackageInfo pi)
        {
            PackageManager pm = activity.getPackageManager();
            try
            {
                pm.getPackageInfo(pi.packageName, PackageManager.GET_ACTIVITIES);
                return true;
            }
            catch (PackageManager.NameNotFoundException e)
            {
                return false;
            }
        }
    }
}
