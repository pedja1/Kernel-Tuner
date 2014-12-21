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
package rs.pedjaapps.kerneltuner.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.adapter.SystemInfoPagerAdapter;
import rs.pedjaapps.kerneltuner.model.Frequency;
import rs.pedjaapps.kerneltuner.model.FrequencyCollection;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.utility.Tools;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SystemInfoActivity extends AbsActivity
{
    private Integer vsync;
    private Integer fastcharge;
    private Integer cdepth;
    private String schedulers;
    private String scheduler;
    private Integer mpdec;
    private Integer s2w;

    private class info extends AsyncTask<String, Void, Object>
    {

        @Override
        protected Object doInBackground(String... args)
        {
            fastcharge = IOHelper.fcharge();
            vsync = IOHelper.vsync();
            try
            {
                cdepth = Tools.parseInt(IOHelper.cDepth(), 0);
            }
            catch (Exception e)
            {

            }

            try
            {

                File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
                FileInputStream fIn = new FileInputStream(myFile);

                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null)
                {
                    aBuffer += aDataRow + "\n";
                }

                schedulers = aBuffer;
                myReader.close();
                fIn.close();
                scheduler = schedulers.substring(schedulers.indexOf("[") + 1,
                        schedulers.indexOf("]"));
                scheduler.trim();
                schedulers = schedulers.replace("[", "");
                schedulers = schedulers.replace("]", "");

            }
            catch (Exception e)
            {
                schedulers = "err";
                scheduler = "err";
            }


            try
            {

                File myFile = new File(
                        "/sys/kernel/msm_mpdecision/conf/enabled");
                FileInputStream fIn = new FileInputStream(myFile);

                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null)
                {
                    aBuffer += aDataRow + "\n";
                }

                mpdec = Tools.parseInt(aBuffer.trim(), -1);
                myReader.close();
                fIn.close();
            }
            catch (Exception e)
            {

            }


            s2w = IOHelper.s2w();


            return "";
        }

        @Override
        protected void onPostExecute(Object result)
        {



        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);

        ViewPager mPager = (ViewPager)findViewById(R.id.pager);
        SystemInfoPagerAdapter mPagerAdapter = new SystemInfoPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
