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
import android.app.ActionBar;
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


    private String screenRezolution;
    private String screenRefreshRate;
    private String screenDensity;
    private String screenPpi;
    TextView oriHead, accHead, magHead, ligHead, proxHead, presHead, tempHead,
            gyroHead, gravHead, humHead, oriAccu, accAccu, magAccu, ligAccu,
            proxAccu, presAccu, tempAccu, gyroAccu, gravAccu, humAccu,
            tv_orientationA, tv_orientationB, tv_orientationC, tv_accelA,
            tv_accelB, tv_accelC, tv_magneticA, tv_magneticB, tv_magneticC,
            tv_lightA, tv_proxA, tv_presA, tv_tempA, tv_gravityA, tv_gravityB,
            tv_gravityC, tv_gyroscopeA, tv_gyroscopeB, tv_gyroscopeC,
            tv_humidity_A;
    ProgressBar pb_orientationA, pb_orientationB, pb_orientationC, pb_accelA,
            pb_accelB, pb_accelC, pb_magneticA, pb_magneticB, pb_magneticC,
            pb_lightA, pb_proxA, pb_presA, pb_tempA, pb_gravityA, pb_gravityB,
            pb_gravityC, pb_gyroscopeA, pb_gyroscopeB, pb_gyroscopeC,
            pb_humidity_A;
    LinearLayout oriLayout, accLayout, magLayout, ligLayout, proxLayout,
            tempLayout, presLayout;
    SensorManager m_sensormgr;
    List<Sensor> m_sensorlist;
    static final int FLOATTOINTPRECISION = 100;

    Boolean isSDPresent;
    String unknown;

    private class info extends AsyncTask<String, Void, Object>
    {

        private boolean isSystemPackage(PackageInfo pkgInfo)
        {
            return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }

        @Override
        protected Object doInBackground(String... args)
        {
            isSDPresent = android.os.Environment.getExternalStorageState()
                    .equals(android.os.Environment.MEDIA_MOUNTED);
            List<String> govs = IOHelper.governorsAsList();
            StringBuilder builder = new StringBuilder();
            for (String s : govs)
            {
                builder.append(s + ", ");
            }


            fastcharge = IOHelper.fcharge();
            vsync = IOHelper.vsync();
            try
            {
                cdepth = Integer.parseInt(IOHelper.cDepth());
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

                mpdec = Integer.parseInt(aBuffer.trim());
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

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current tab position.
     */
    private List<String> tabTitles = new ArrayList<String>();
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);

        ViewPager mPager = (ViewPager)findViewById(R.id.pager);
        SystemInfoPagerAdapter mPagerAdapter = new SystemInfoPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(mPagerAdapter);

    }

    public void Sensors(LayoutInflater inflater, ViewGroup container)
    {
        inflater.inflate(R.layout.system_info_sensors, container);

        oriHead = (TextView) container.findViewById(R.id.TextView_oriHead);
        accHead = (TextView) container.findViewById(R.id.TextView_accHead);
        magHead = (TextView) container.findViewById(R.id.TextView_magHead);
        ligHead = (TextView) container.findViewById(R.id.TextView_ligHead);
        proxHead = (TextView) container.findViewById(R.id.TextView_proxHead);
        presHead = (TextView) container.findViewById(R.id.TextView_presHead);
        tempHead = (TextView) container.findViewById(R.id.TextView_tempHead);
        gravHead = (TextView) container.findViewById(R.id.TextView_gravHead);
        gyroHead = (TextView) container.findViewById(R.id.TextView_gyrHead);
        humHead = (TextView) container.findViewById(R.id.TextView_humHead);

        oriAccu = (TextView) container.findViewById(R.id.oriAccuracy);
        accAccu = (TextView) container.findViewById(R.id.accAccuracy);
        magAccu = (TextView) container.findViewById(R.id.magAccuracy);
        ligAccu = (TextView) container.findViewById(R.id.ligAccuracy);
        proxAccu = (TextView) container.findViewById(R.id.proxAccuracy);
        presAccu = (TextView) container.findViewById(R.id.presAccuracy);
        tempAccu = (TextView) container.findViewById(R.id.tempAccuracy);
        gravAccu = (TextView) container.findViewById(R.id.gravAccuracy);
        gyroAccu = (TextView) container.findViewById(R.id.gyrAccuracy);
        humAccu = (TextView) container.findViewById(R.id.humAccuracy);

        tv_orientationA = (TextView) container.findViewById(R.id.TextView_oriA);
        pb_orientationA = (ProgressBar) this
                .findViewById(R.id.ProgressBar_oriA);
        tv_orientationB = (TextView) container.findViewById(R.id.TextView_oriB);
        pb_orientationB = (ProgressBar) this
                .findViewById(R.id.ProgressBar_oriB);
        tv_orientationC = (TextView) container.findViewById(R.id.TextView_oriC);
        pb_orientationC = (ProgressBar) this
                .findViewById(R.id.ProgressBar_oriC);
        tv_accelA = (TextView) container.findViewById(R.id.TextView_accA);
        pb_accelA = (ProgressBar) this.findViewById(R.id.ProgressBar_accA);
        tv_accelB = (TextView) container.findViewById(R.id.TextView_accB);
        pb_accelB = (ProgressBar) this.findViewById(R.id.ProgressBar_accB);
        tv_accelC = (TextView) container.findViewById(R.id.TextView_accC);
        pb_accelC = (ProgressBar) this.findViewById(R.id.ProgressBar_accC);
        tv_magneticA = (TextView) container.findViewById(R.id.TextView_magA);
        pb_magneticA = (ProgressBar) this.findViewById(R.id.ProgressBar_magA);
        tv_magneticB = (TextView) container.findViewById(R.id.TextView_magB);
        pb_magneticB = (ProgressBar) this.findViewById(R.id.ProgressBar_magB);
        tv_magneticC = (TextView) container.findViewById(R.id.TextView_magC);
        pb_magneticC = (ProgressBar) this.findViewById(R.id.ProgressBar_magC);
        tv_lightA = (TextView) container.findViewById(R.id.TextView_ligA);
        pb_lightA = (ProgressBar) this.findViewById(R.id.ProgressBar_ligA);
        tv_proxA = (TextView) container.findViewById(R.id.TextView_proxA);
        pb_proxA = (ProgressBar) this.findViewById(R.id.ProgressBar_proxA);
        tv_presA = (TextView) container.findViewById(R.id.TextView_presA);
        pb_presA = (ProgressBar) this.findViewById(R.id.ProgressBar_presA);
        tv_tempA = (TextView) container.findViewById(R.id.TextView_tempA);
        pb_tempA = (ProgressBar) this.findViewById(R.id.ProgressBar_tempA);

        tv_gravityA = (TextView) container.findViewById(R.id.TextView_gravA);
        pb_gravityA = (ProgressBar) this.findViewById(R.id.ProgressBar_gravA);
        tv_gravityB = (TextView) container.findViewById(R.id.TextView_gravB);
        pb_gravityB = (ProgressBar) this.findViewById(R.id.ProgressBar_gravB);
        tv_gravityC = (TextView) container.findViewById(R.id.TextView_gravC);
        pb_gravityC = (ProgressBar) this.findViewById(R.id.ProgressBar_gravC);
        tv_gyroscopeA = (TextView) container.findViewById(R.id.TextView_gyrA);
        pb_gyroscopeA = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrA);
        tv_gyroscopeB = (TextView) container.findViewById(R.id.TextView_gyrB);
        pb_gyroscopeB = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrB);
        tv_gyroscopeC = (TextView) container.findViewById(R.id.TextView_gyrC);
        pb_gyroscopeC = (ProgressBar) this.findViewById(R.id.ProgressBar_gyrC);
        tv_humidity_A = (TextView) container.findViewById(R.id.TextView_humA);
        pb_humidity_A = (ProgressBar) this.findViewById(R.id.ProgressBar_humA);

        oriLayout = (LinearLayout) container.findViewById(R.id.oriLayout);
        accLayout = (LinearLayout) container.findViewById(R.id.accLayout);
        magLayout = (LinearLayout) container.findViewById(R.id.magLayout);
        ligLayout = (LinearLayout) container.findViewById(R.id.ligLayout);
        proxLayout = (LinearLayout) container.findViewById(R.id.proxLayout);
        presLayout = (LinearLayout) container.findViewById(R.id.pressLayout);
        tempLayout = (LinearLayout) container.findViewById(R.id.tempLayout);
        connectSensors();

    }

    protected String getSensorInfo(Sensor sen)
    {
        String sensorInfo = "INVALID";
        String snsType;

        switch (sen.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                snsType = "TYPE_ACCELEROMETER";
                break;
            case Sensor.TYPE_ALL:
                snsType = "TYPE_ALL";
                break;
            case Sensor.TYPE_GYROSCOPE:
                snsType = "TYPE_GYROSCOPE";
                break;
            case Sensor.TYPE_LIGHT:
                snsType = "TYPE_LIGHT";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                snsType = "TYPE_MAGNETIC_FIELD";
                break;
            case Sensor.TYPE_ORIENTATION:
                snsType = "TYPE_ORIENTATION";
                break;
            case Sensor.TYPE_PRESSURE:
                snsType = "TYPE_PRESSURE";
                break;
            case Sensor.TYPE_PROXIMITY:
                snsType = "TYPE_PROXIMITY";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                snsType = "TYPE_TEMPERATURE";
                break;
            default:
                snsType = "UNKNOWN_TYPE " + sen.getType();
                break;
        }

        sensorInfo = sen.getName() + "\n";
        sensorInfo += "Version: " + sen.getVersion() + "\n";
        sensorInfo += "Vendor: " + sen.getVendor() + "\n";
        sensorInfo += "Type: " + snsType + "\n";
        sensorInfo += "MaxRange: " + sen.getMaximumRange() + "\n";
        sensorInfo += "Resolution: "
                + String.format("%.5f", sen.getResolution()) + "\n";
        sensorInfo += "Power: " + sen.getPower() + " mA\n";
        return sensorInfo;
    }

    SensorEventListener senseventListener = new SensorEventListener()
    {

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            String accuracy;

            switch (event.accuracy)
            {
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                    accuracy = "SENSOR_STATUS_ACCURACY_HIGH";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                    accuracy = "SENSOR_STATUS_ACCURACY_MEDIUM";
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                    accuracy = "SENSOR_STATUS_ACCURACY_LOW";
                    break;
                case SensorManager.SENSOR_STATUS_UNRELIABLE:
                    accuracy = "SENSOR_STATUS_UNRELIABLE";
                    break;
                default:
                    accuracy = unknown;
            }

            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            {
                oriAccu.setText(accuracy);
                pb_orientationA.setProgress((int) event.values[0]);
                pb_orientationB.setProgress(Math.abs((int) event.values[1]));
                pb_orientationC.setProgress(Math.abs((int) event.values[2]));
                tv_orientationA.setText(String.format("%.1f", event.values[0]));
                tv_orientationB.setText(String.format("%.1f", event.values[1]));
                tv_orientationC.setText(String.format("%.1f", event.values[2]));
            }
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                accAccu.setText(accuracy);
                pb_accelA.setProgress(Math.abs((int) event.values[0]
                        * FLOATTOINTPRECISION));
                pb_accelB.setProgress(Math.abs((int) event.values[1]
                        * FLOATTOINTPRECISION));
                pb_accelC.setProgress(Math.abs((int) event.values[2]
                        * FLOATTOINTPRECISION));
                tv_accelA.setText(String.format("%.2f", event.values[0]));
                tv_accelB.setText(String.format("%.2f", event.values[1]));
                tv_accelC.setText(String.format("%.2f", event.values[2]));
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                magAccu.setText(accuracy);
                pb_magneticA.setProgress(Math.abs((int) event.values[0]
                        * FLOATTOINTPRECISION));
                pb_magneticB.setProgress(Math.abs((int) event.values[1]
                        * FLOATTOINTPRECISION));
                pb_magneticC.setProgress(Math.abs((int) event.values[2]
                        * FLOATTOINTPRECISION));
                tv_magneticA.setText(String.format("%.2f", event.values[0]));
                tv_magneticB.setText(String.format("%.2f", event.values[1]));
                tv_magneticC.setText(String.format("%.2f", event.values[2]));
            }
            if (event.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                ligAccu.setText(accuracy);
                pb_lightA.setProgress(Math.abs((int) event.values[0]));
                tv_lightA.setText(String.format("%.2f", event.values[0]));
            }
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
            {
                proxAccu.setText(accuracy);
                pb_proxA.setProgress(Math.abs((int) event.values[0]));
                tv_proxA.setText(String.format("%.2f", event.values[0]));
            }
            if (event.sensor.getType() == Sensor.TYPE_PRESSURE)
            {
                presAccu.setText(accuracy);
                pb_presA.setProgress(Math.abs((int) event.values[0]));
                tv_presA.setText(String.format("%.2f", event.values[0]));
            }
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
            {
                tempAccu.setText(accuracy);
                pb_tempA.setProgress(Math.abs((int) event.values[0]));
                tv_tempA.setText(String.format("%.2f", event.values[0]));
            }
            if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
            {
                gravAccu.setText(accuracy);
                pb_gravityA.setProgress(Math.abs((int) event.values[0]
                        * FLOATTOINTPRECISION));
                pb_gravityB.setProgress(Math.abs((int) event.values[1]
                        * FLOATTOINTPRECISION));
                pb_gravityC.setProgress(Math.abs((int) event.values[2]
                        * FLOATTOINTPRECISION));
                tv_gravityA.setText(String.format("%.2f", event.values[0]));
                tv_gravityB.setText(String.format("%.2f", event.values[1]));
                tv_gravityC.setText(String.format("%.2f", event.values[2]));
            }
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            {
                gyroAccu.setText(accuracy);
                pb_gyroscopeA.setProgress(Math.abs((int) event.values[0]
                        * FLOATTOINTPRECISION));
                pb_gyroscopeB.setProgress(Math.abs((int) event.values[1]
                        * FLOATTOINTPRECISION));
                pb_gyroscopeC.setProgress(Math.abs((int) event.values[2]
                        * FLOATTOINTPRECISION));
                tv_gyroscopeA.setText(String.format("%.2f", event.values[0]));
                tv_gyroscopeB.setText(String.format("%.2f", event.values[1]));
                tv_gyroscopeC.setText(String.format("%.2f", event.values[2]));
            }
            if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY)
            {
                humAccu.setText(accuracy);
                pb_humidity_A.setProgress(Math.abs((int) event.values[0]));
                tv_humidity_A.setText(String.format("%.2f", event.values[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }

    };

    protected void connectSensors()
    {
        m_sensormgr.unregisterListener(senseventListener);
        if (!m_sensorlist.isEmpty())
        {
            Sensor snsr;
            int m_sensorListSize = m_sensorlist.size();
            for (int i = 0; i < m_sensorListSize; i++)
            {
                snsr = m_sensorlist.get(i);

                if (snsr.getType() == Sensor.TYPE_ORIENTATION)
                {
                    oriHead.setText(getSensorInfo(snsr));
                    pb_orientationA.setMax((int) snsr.getMaximumRange());
                    pb_orientationB.setMax((int) snsr.getMaximumRange());
                    pb_orientationC.setMax((int) snsr.getMaximumRange());
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_ACCELEROMETER)
                {
                    accHead.setText(getSensorInfo(snsr));
                    pb_accelA
                            .setMax((int) (snsr.getMaximumRange()
                                    * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
                    pb_accelB
                            .setMax((int) (snsr.getMaximumRange()
                                    * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
                    pb_accelC
                            .setMax((int) (snsr.getMaximumRange()
                                    * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));

                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                {
                    magHead.setText(getSensorInfo(snsr));
                    pb_magneticA
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
                    pb_magneticB
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
                    pb_magneticC
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));

                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_LIGHT)
                {
                    ligHead.setText(getSensorInfo(snsr));
                    pb_lightA.setMax((int) (snsr.getMaximumRange()));
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_PROXIMITY)
                {

                    proxHead.setText(getSensorInfo(snsr));
                    pb_proxA.setMax((int) (snsr.getMaximumRange()));
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_PRESSURE)
                {
                    presHead.setText(getSensorInfo(snsr));
                    pb_presA.setMax((int) (snsr.getMaximumRange()));
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
                {

                    tempHead.setText(getSensorInfo(snsr));
                    pb_tempA.setMax((int) (snsr.getMaximumRange()));
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_GYROSCOPE)
                {
                    gyroHead.setText(getSensorInfo(snsr));
                    pb_gyroscopeA
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
                    pb_gyroscopeB
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));
                    pb_gyroscopeC
                            .setMax((int) (snsr.getMaximumRange() * FLOATTOINTPRECISION));

                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_GRAVITY)
                {
                    gravHead.setText(getSensorInfo(snsr));
                    pb_gravityA
                            .setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
                    pb_gravityB
                            .setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));
                    pb_gravityC
                            .setMax((int) (snsr.getMaximumRange() * SensorManager.GRAVITY_EARTH * FLOATTOINTPRECISION));

                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (snsr.getType() == Sensor.TYPE_RELATIVE_HUMIDITY)
                {

                    humHead.setText(getSensorInfo(snsr));
                    pb_humidity_A.setMax((int) (snsr.getMaximumRange()));
                    m_sensormgr.registerListener(senseventListener, snsr,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }

            }
        }
    }

    @Override
    protected void onDestroy()
    {
        m_sensormgr.unregisterListener(senseventListener);
        super.onPause();
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
