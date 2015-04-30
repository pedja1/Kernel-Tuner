package rs.pedjaapps.kerneltuner.fragments;

import android.content.*;
import android.hardware.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import rs.pedjaapps.kerneltuner.*;

public class SensorsFragment extends Fragment
{
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


    public static SensorsFragment newInstance()
    {
        SensorsFragment fragment = new SensorsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		m_sensormgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		m_sensorlist = m_sensormgr.getSensorList(Sensor.TYPE_ALL);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_system_info_sensors, container, false);
		oriHead = (TextView) view.findViewById(R.id.TextView_oriHead);
        accHead = (TextView) view.findViewById(R.id.TextView_accHead);
        magHead = (TextView) view.findViewById(R.id.TextView_magHead);
        ligHead = (TextView) view.findViewById(R.id.TextView_ligHead);
        proxHead = (TextView) view.findViewById(R.id.TextView_proxHead);
        presHead = (TextView) view.findViewById(R.id.TextView_presHead);
        tempHead = (TextView) view.findViewById(R.id.TextView_tempHead);
        gravHead = (TextView) view.findViewById(R.id.TextView_gravHead);
        gyroHead = (TextView) view.findViewById(R.id.TextView_gyrHead);
        humHead = (TextView) view.findViewById(R.id.TextView_humHead);

        oriAccu = (TextView) view.findViewById(R.id.oriAccuracy);
        accAccu = (TextView) view.findViewById(R.id.accAccuracy);
        magAccu = (TextView) view.findViewById(R.id.magAccuracy);
        ligAccu = (TextView) view.findViewById(R.id.ligAccuracy);
        proxAccu = (TextView) view.findViewById(R.id.proxAccuracy);
        presAccu = (TextView) view.findViewById(R.id.presAccuracy);
        tempAccu = (TextView) view.findViewById(R.id.tempAccuracy);
        gravAccu = (TextView) view.findViewById(R.id.gravAccuracy);
        gyroAccu = (TextView) view.findViewById(R.id.gyrAccuracy);
        humAccu = (TextView) view.findViewById(R.id.humAccuracy);

        tv_orientationA = (TextView) view.findViewById(R.id.TextView_oriA);
        pb_orientationA = (ProgressBar) view
			.findViewById(R.id.ProgressBar_oriA);
        tv_orientationB = (TextView) view.findViewById(R.id.TextView_oriB);
        pb_orientationB = (ProgressBar) view
			.findViewById(R.id.ProgressBar_oriB);
        tv_orientationC = (TextView) view.findViewById(R.id.TextView_oriC);
        pb_orientationC = (ProgressBar) view
			.findViewById(R.id.ProgressBar_oriC);
        tv_accelA = (TextView) view.findViewById(R.id.TextView_accA);
        pb_accelA = (ProgressBar) view.findViewById(R.id.ProgressBar_accA);
        tv_accelB = (TextView) view.findViewById(R.id.TextView_accB);
        pb_accelB = (ProgressBar) view.findViewById(R.id.ProgressBar_accB);
        tv_accelC = (TextView) view.findViewById(R.id.TextView_accC);
        pb_accelC = (ProgressBar) view.findViewById(R.id.ProgressBar_accC);
        tv_magneticA = (TextView) view.findViewById(R.id.TextView_magA);
        pb_magneticA = (ProgressBar) view.findViewById(R.id.ProgressBar_magA);
        tv_magneticB = (TextView) view.findViewById(R.id.TextView_magB);
        pb_magneticB = (ProgressBar) view.findViewById(R.id.ProgressBar_magB);
        tv_magneticC = (TextView) view.findViewById(R.id.TextView_magC);
        pb_magneticC = (ProgressBar) view.findViewById(R.id.ProgressBar_magC);
        tv_lightA = (TextView) view.findViewById(R.id.TextView_ligA);
        pb_lightA = (ProgressBar) view.findViewById(R.id.ProgressBar_ligA);
        tv_proxA = (TextView) view.findViewById(R.id.TextView_proxA);
        pb_proxA = (ProgressBar) view.findViewById(R.id.ProgressBar_proxA);
        tv_presA = (TextView) view.findViewById(R.id.TextView_presA);
        pb_presA = (ProgressBar) view.findViewById(R.id.ProgressBar_presA);
        tv_tempA = (TextView) view.findViewById(R.id.TextView_tempA);
        pb_tempA = (ProgressBar) view.findViewById(R.id.ProgressBar_tempA);

        tv_gravityA = (TextView) view.findViewById(R.id.TextView_gravA);
        pb_gravityA = (ProgressBar) view.findViewById(R.id.ProgressBar_gravA);
        tv_gravityB = (TextView) view.findViewById(R.id.TextView_gravB);
        pb_gravityB = (ProgressBar) view.findViewById(R.id.ProgressBar_gravB);
        tv_gravityC = (TextView) view.findViewById(R.id.TextView_gravC);
        pb_gravityC = (ProgressBar) view.findViewById(R.id.ProgressBar_gravC);
        tv_gyroscopeA = (TextView) view.findViewById(R.id.TextView_gyrA);
        pb_gyroscopeA = (ProgressBar) view.findViewById(R.id.ProgressBar_gyrA);
        tv_gyroscopeB = (TextView) view.findViewById(R.id.TextView_gyrB);
        pb_gyroscopeB = (ProgressBar) view.findViewById(R.id.ProgressBar_gyrB);
        tv_gyroscopeC = (TextView) view.findViewById(R.id.TextView_gyrC);
        pb_gyroscopeC = (ProgressBar) view.findViewById(R.id.ProgressBar_gyrC);
        tv_humidity_A = (TextView) view.findViewById(R.id.TextView_humA);
        pb_humidity_A = (ProgressBar) view.findViewById(R.id.ProgressBar_humA);

        oriLayout = (LinearLayout) view.findViewById(R.id.oriLayout);
        accLayout = (LinearLayout) view.findViewById(R.id.accLayout);
        magLayout = (LinearLayout) view.findViewById(R.id.magLayout);
        ligLayout = (LinearLayout) view.findViewById(R.id.ligLayout);
        proxLayout = (LinearLayout) view.findViewById(R.id.proxLayout);
        presLayout = (LinearLayout) view.findViewById(R.id.pressLayout);
        tempLayout = (LinearLayout) view.findViewById(R.id.tempLayout);
        connectSensors();
		return view;
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
                    accuracy = getString(R.string.unknown);
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
    public void onDestroy()
    {
        if(m_sensormgr != null)m_sensormgr.unregisterListener(senseventListener);
        super.onDestroy();
    }

}
