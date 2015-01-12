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

import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.Frequency;
import rs.pedjaapps.kerneltuner.model.FrequencyCollection;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.Tools;

import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Thermald extends AbsActivity
{
    private List<Frequency> freqs = FrequencyCollection.getInstance().getFrequencies();
    private int p1freq;
    private int p2freq;
    private int p3freq;
    private int p1freqnew;
    private int p2freqnew;
    private int p3freqnew;
    private String p1lownew;
    private String p1highnew;
    private String p2lownew;
    private String p2highnew;
    private String p3lownew;
    private String p3highnew;

    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    private EditText ed6;

    private SharedPreferences preferences;
    private ProgressDialog pd = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        setContentView(R.layout.thermald);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        p1freq = Tools.parseInt(IOHelper.thermalLowFreq(), -1);
        p2freq = Tools.parseInt(IOHelper.thermalMidFreq(), -1);
        p3freq = Tools.parseInt(IOHelper.thermalMaxFreq(), -1);
        String p1low = convertTemp(IOHelper.thermalLowLow());
        String p2low = convertTemp(IOHelper.thermalMidLow());
        String p3low = convertTemp(IOHelper.thermalMaxLow());
        String p1high = convertTemp(IOHelper.thermalLowHigh());
        String p2high = convertTemp(IOHelper.thermalMidHigh());
        String p3high = convertTemp(IOHelper.thermalMaxHigh());

        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText3);
        ed4 = (EditText) findViewById(R.id.editText4);
        ed5 = (EditText) findViewById(R.id.editText5);
        ed6 = (EditText) findViewById(R.id.editText6);
        ed1.setText(p1low);
        ed2.setText(p1high);
        ed3.setText(p2low);
        ed4.setText(p2high);
        ed5.setText(p3low);
        ed6.setText(p3high);
        createSpinnerp1();
        createSpinnerp2();
        createSpinnerp3();

        String tempPref = preferences.getString("temp", "celsius");
        int[] ids = {R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.c5, R.id.c6};
        if (tempPref.equals("fahrenheit"))
        {
            for (int i : ids)
            {
                TextView tv = (TextView) findViewById(i);
                tv.setText("°F");
            }
        }
        if (tempPref.equals("kelvin"))
        {
            for (int i : ids)
            {
                TextView tv = (TextView) findViewById(i);
                tv.setText("°K");
            }
        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {


        super.onResume();

    }

    private void createSpinnerp1()
    {
        final Spinner spinner = (Spinner) findViewById(R.id.bg);
        ArrayAdapter<Frequency> spinnerArrayAdapter = new ArrayAdapter<Frequency>(this, android.R.layout.simple_spinner_item, freqs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                p1freqnew = freqs.get(pos).getFrequencyValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

        try
        {
            int spinnerPosition = FrequencyCollection.getPositionFromFreq(p1freq, freqs);
            //set the default according to value
            spinner.setSelection(spinnerPosition);
        }
        catch (Exception e)
        {
            //idleSpinner.set
            System.out.println("err" + e.getMessage());
        }
    }

    private void createSpinnerp2()
    {


        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<Frequency> spinnerArrayAdapter = new ArrayAdapter<Frequency>(this, android.R.layout.simple_spinner_item, freqs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                p2freqnew = freqs.get(pos).getFrequencyValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

        try
        {
            int spinnerPosition = FrequencyCollection.getPositionFromFreq(p2freq, freqs);

            //set the default according to value
            spinner.setSelection(spinnerPosition);
        }
        catch (Exception e)
        {
            //idleSpinner.set
            System.out.println("err" + e.getMessage());
        }
    }

    private void createSpinnerp3()
    {

        final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<Frequency> spinnerArrayAdapter = new ArrayAdapter<Frequency>(this, android.R.layout.simple_spinner_item, freqs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                p3freqnew = freqs.get(pos).getFrequencyValue();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

        try
        {
            int spinnerPosition = FrequencyCollection.getPositionFromFreq(p3freq, freqs);

            //set the default according to value
            spinner.setSelection(spinnerPosition);
        }
        catch (Exception e)
        {
            System.out.println("err" + e.getMessage());
        }
    }


    private String convertTemp(String temp)
    {
        String tempUnit = preferences.getString("temp", "celsius");
        switch (tempUnit)
        {
            case "celsius":
                return temp;
            case "fahrenheit":
                if (!temp.equals(""))
                {
                    return ((int) (Double.parseDouble(temp) * 1.8 + 32)) + "";
                }
                else
                {
                    return "";
                }
            case "kelvin":
                return ((int) (Double.parseDouble(temp) + 273.15)) + "";
            default:
                return "";
        }


    }

    private String convertTempBack(String temp)
    {
        String tempUnit = preferences.getString("temp", "celsius");
        switch (tempUnit)
        {
            case "celsius":
                return temp;
            case "fahrenheit":
                if (!temp.equals(""))
                {
                    return ((int) ((Double.parseDouble(temp) - 32) / 1.8)) + "";
                }
                else
                {
                    return "";
                }
            case "kelvin":
                return ((int) (Double.parseDouble(temp) - 273.15)) + "";
            default:
                return "";
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.misc_tweaks_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.apply:
                apply();
                return true;
            case R.id.cancel:
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void apply()
    {
        Thermald.this.pd = ProgressDialog.show(Thermald.this, null, getResources().getString(R.string.applying_settings), true, false);
        p1lownew = convertTempBack(ed1.getText().toString());
        p1highnew = convertTempBack(ed2.getText().toString());
        p2lownew = convertTempBack(ed3.getText().toString());
        p2highnew = convertTempBack(ed4.getText().toString());
        p3lownew = convertTempBack(ed5.getText().toString());
        p3highnew = convertTempBack(ed6.getText().toString());
        String[] cmds = new String[] {
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_freq",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_freq",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_freq",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_low",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_low_high",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_low",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_mid_high",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_low",
                "chmod 777 /sys/kernel/msm_thermal/conf/allowed_max_high",
                "echo " + p1freqnew + " > /sys/kernel/msm_thermal/conf/allowed_low_freq",
                "echo " + p2freqnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_freq",
                "echo " + p3freqnew + " > /sys/kernel/msm_thermal/conf/allowed_max_freq",
                "echo " + p1lownew + " > /sys/kernel/msm_thermal/conf/allowed_low_low",
                "echo " + p1highnew + " > /sys/kernel/msm_thermal/conf/allowed_low_high",
                "echo " + p2lownew + " > /sys/kernel/msm_thermal/conf/allowed_mid_low",
                "echo " + p2highnew + " > /sys/kernel/msm_thermal/conf/allowed_mid_high",
                "echo " + p3lownew + " > /sys/kernel/msm_thermal/conf/allowed_max_low",
                "echo " + p3highnew + " > /sys/kernel/msm_thermal/conf/allowed_max_high"};
        new RootUtils().exec(new RootUtils.CommandCallbackImpl()
        {
            @Override
            public void onComplete(RootUtils.Status status, String output)
            {
                if (status == RootUtils.Status.success)
                {
                    PrefsManager.setThermalFreq(1, p1freqnew);
                    PrefsManager.setThermalFreq(2, p2freqnew);
                    PrefsManager.setThermalFreq(3, p3freqnew);
                    PrefsManager.setThermalLow(1, Tools.parseInt(p1lownew, -1));
                    PrefsManager.setThermalLow(2, Tools.parseInt(p2lownew, -1));
                    PrefsManager.setThermalLow(3, Tools.parseInt(p3lownew, -1));
                    PrefsManager.setThermalHigh(1, Tools.parseInt(p1highnew, -1));
                    PrefsManager.setThermalHigh(2, Tools.parseInt(p2highnew, -1));
                    PrefsManager.setThermalHigh(3, Tools.parseInt(p3highnew, -1));
                }

                Thermald.this.pd.dismiss();
                Thermald.this.finish();
            }
        }, cmds);
    }

}


