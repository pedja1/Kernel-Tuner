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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.Frequency;
import rs.pedjaapps.kerneltuner.model.FrequencyCollection;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.Tools;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

public class MpdecisionNew extends AbsActivity
{


    private List<Frequency> freqs = FrequencyCollection.getInstance().getFrequencies();

    private String mpscroff;
    private SharedPreferences preferences;
    private String delay;
    private String pause;
    private String[] thr = new String[8];
    private String[] tim = new String[8];

    private int idle;
    private int scroff;
    private int scroff_single;

    private Switch mp_switch;
    private Spinner idleSpinner;
    private Spinner scroffSpinner;

    int enabled;

    EditText[] thrTxt = new EditText[12];
    int[] thrIds;
    EditText maxCpus;
    EditText minCpus;
    String max_cpus;
    String min_cpus;
    Switch swMpEnabled;
    EditText etDelay;
    EditText etPause;

    private ProgressDialog pd = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mpdecision_new);
        mp_switch = (Switch) findViewById(R.id.mp_switch);
        swMpEnabled = (Switch)findViewById(R.id.swMpEnabled);
        idleSpinner = (Spinner) findViewById(R.id.bg);
        scroffSpinner = (Spinner) findViewById(R.id.spinner2);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maxCpus = (EditText) findViewById(R.id.max_cpus);
        minCpus = (EditText) findViewById(R.id.min_cpus);
        thrIds = new int[]{R.id.one_cpu_hotplug,
                R.id.one_cpu_hotplug_time,
                R.id.two_cpus_hotplug,
                R.id.two_cpu_hotplug_time,
                R.id.two_cpus_unplug,
                R.id.two_cpu_unplug_time,
                R.id.three_cpus_hotplug,
                R.id.three_cpu_hotplug_time,
                R.id.three_cpus_unplug,
                R.id.three_cpu_unplug_time,
                R.id.four_cpus_unplug,
                R.id.four_cpu_unplug_time};
        for (int i = 0; i < thrIds.length; i++)
        {
            thrTxt[i] = (EditText) findViewById(thrIds[i]);
        }
        etDelay = (EditText) findViewById(R.id.ed1);
        etPause = (EditText) findViewById(R.id.ed2);
        readMpdec();
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


    private void setCheckBoxes()
    {
        etDelay.setText(delay.trim());
        etPause.setText(pause.trim());

        thrTxt[0].setText(thr[0]);
        thrTxt[1].setText(tim[0]);
        thrTxt[2].setText(thr[2]);
        thrTxt[3].setText(tim[2]);
        thrTxt[4].setText(thr[3]);
        thrTxt[5].setText(tim[3]);
        thrTxt[6].setText(thr[4]);
        thrTxt[7].setText(tim[4]);
        thrTxt[8].setText(thr[5]);
        thrTxt[9].setText(tim[5]);
        thrTxt[10].setText(thr[7]);
        thrTxt[11].setText(tim[7]);
        maxCpus.setText(max_cpus);
        minCpus.setText(min_cpus);
        mp_switch.setChecked(scroff_single == 1);
        swMpEnabled.setChecked(enabled == 1);
        mp_switch.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1)
            {
                scroff_single = arg1 ? 1 : 0;
            }
        });

        swMpEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1)
            {
                enabled = arg1 ? 1 : 0;
            }
        });


        ArrayAdapter<Frequency> freqsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, freqs);
        freqsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scroffSpinner.setAdapter(freqsArrayAdapter);

        scroffSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                scroff = freqs.get(pos).getFrequencyValue() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

        int scroffPosition = FrequencyCollection.getPositionFromFreq(scroff - 1, freqs);
        scroffSpinner.setSelection(scroffPosition);
        idleSpinner.setAdapter(freqsArrayAdapter);

        idleSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                idle = freqs.get(pos).getFrequencyValue() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

        int idlePosition = FrequencyCollection.getPositionFromFreq(idle - 1, freqs);
        idleSpinner.setSelection(idlePosition);
    }

    private void readMpdec()
    {
        try
        {
            delay = FileUtils.readFileToString(new File(Constants.MPDEC_DELAY));
        }
        catch (Exception e)
        {
            delay = "err";
            etDelay.setEnabled(false);
        }

        try
        {
            pause = FileUtils.readFileToString(new File(Constants.MPDEC_PAUSE));
        }
        catch (Exception e)
        {
            pause = "err";
            etPause.setEnabled(false);
        }

        for (int i = 0; i < 8; i++)
        {
            try
            {
                thr[i] = FileUtils.readFileToString(
                        new File("/sys/kernel/msm_mpdecision/conf/nwns_threshold_" + i));
            }
            catch (Exception e)
            {
                thr[i] = "err";
            }
        }
        for (int i = 0; i < 8; i++)
        {
            try
            {
                tim[i] = FileUtils.readFileToString(new File("/sys/kernel/msm_mpdecision/conf/twts_threshold_" + i));
            }
            catch (Exception e)
            {
                tim[i] = "err";
            }
        }


        try
        {
            idle = Tools.parseInt(FileUtils.readFileToString(new File(Constants.MPDEC_IDLE_FREQ)).trim(), -1);
        }
        catch (Exception e)
        {
            idle = -1;
            idleSpinner.setEnabled(false);
        }

        try
        {
            enabled = Tools.parseInt(FileUtils.readFileToString(new File(Constants.MPDEC_ENABLED)).trim(), -1);
        }
        catch (Exception e)
        {
            enabled = -1;
            swMpEnabled.setEnabled(false);
        }

        try
        {
            scroff = Tools.parseInt(FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_FREQ)).trim(), -1);
        }
        catch (Exception e)
        {
            scroff = -1;
            scroffSpinner.setEnabled(false);
        }

        try
        {
            scroff_single = Tools.parseInt(FileUtils.readFileToString(new File(Constants.MPDEC_SCROFF_SINGLE)).trim(), -1);
        }
        catch (Exception e)
        {
            scroff_single = -1;
            mp_switch.setEnabled(false);

        }
        try
        {
            max_cpus = FileUtils.readFileToString(new File(Constants.MPDEC_MAX_CPUS));
        }
        catch (Exception e)
        {
            max_cpus = "err";
            maxCpus.setEnabled(false);
        }
        try
        {
            min_cpus = FileUtils.readFileToString(new File(Constants.MPDEC_MIN_CPUS));
        }
        catch (Exception e)
        {
            min_cpus = "err";
            minCpus.setEnabled(false);
        }
        setCheckBoxes();
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
        MpdecisionNew.this.pd = ProgressDialog.show(MpdecisionNew.this, null, getResources().getString(R.string.applying_settings), true, true);
        List<String> cmds = new ArrayList<>();
        for (int i = 0; i < 8; i++)
        {
            cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/nwns_threshold_" + i);
            cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/twts_threshold_" + i);
        }

        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/enabled");
        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core");
        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_freq");
        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/idle_freq");
        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/delay");
        cmds.add("chmod 777 /sys/kernel/msm_mpdecision/conf/pause");
        cmds.add("echo " + enabled + " > /sys/kernel/msm_mpdecision/conf/enabled");
        cmds.add("echo " + thrTxt[0].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_0");
        cmds.add("echo " + thrTxt[2].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_2");
        cmds.add("echo " + thrTxt[4].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_3");
        cmds.add("echo " + thrTxt[6].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_4");
        cmds.add("echo " + thrTxt[8].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_5");
        cmds.add("echo " + thrTxt[10].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/nwns_threshold_7");
        cmds.add("echo " + thrTxt[1].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_0");
        cmds.add("echo " + thrTxt[3].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_2");
        cmds.add("echo " + thrTxt[5].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_3");
        cmds.add("echo " + thrTxt[7].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_4");
        cmds.add("echo " + thrTxt[9].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_5");
        cmds.add("echo " + thrTxt[11].getText().toString() + " > /sys/kernel/msm_mpdecision/conf/twts_threshold_7");
        cmds.add("echo " + maxCpus.getText().toString() + " > /sys/kernel/msm_mpdecision/conf/max_cpus");
        cmds.add("echo " + minCpus.getText().toString() + " > /sys/kernel/msm_mpdecision/conf/min_cpus");
        cmds.add("echo " + mpscroff + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core");
        cmds.add("echo " + idle + " > /sys/kernel/msm_mpdecision/conf/idle_freq");
        //cmds.add("echo " + onoff + " > /sys/kernel/msm_mpdecision/conf/scroff_profile");
        cmds.add("echo " + scroff + " > /sys/kernel/msm_mpdecision/conf/scroff_freq");
        cmds.add("echo " + scroff_single + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core");
        cmds.add("echo " + etDelay.getText().toString() + " > /sys/kernel/msm_mpdecision/conf/delay");
        cmds.add("echo  " + etPause.getText().toString() + " > /sys/kernel/msm_mpdecision/conf/pause");
        new RootUtils().exec(new RootUtils.CommandCallbackImpl()
        {
            @Override
            public void onComplete(RootUtils.Status status, String output)
            {
                preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                /*SharedPreferences.Editor editor = preferences.edit();
                editor.putString("onoff", onoff);
                editor.putString("idle_freq", idleNew + "");
                editor.putString("scroff", scroffNew + "");
                editor.putString("scroff_single", scroff_singleNew);
                editor.putString("max_cpus", maxCpus.getText().toString());
                editor.putString("min_cpus", minCpus.getText().toString());

                editor.putString("thr0", thrTxt[0].getText().toString());
                editor.putString("thr2", thrTxt[1].getText().toString());
                editor.putString("thr3", thrTxt[2].getText().toString());
                editor.putString("thr4", thrTxt[3].getText().toString());
                editor.putString("thr5", thrTxt[4].getText().toString());
                editor.putString("thr7", thrTxt[5].getText().toString());
                editor.putString("tim0", thrTxt[0].getText().toString());
                editor.putString("tim2", thrTxt[1].getText().toString());
                editor.putString("tim3", thrTxt[2].getText().toString());
                editor.putString("tim4", thrTxt[3].getText().toString());
                editor.putString("tim5", thrTxt[4].getText().toString());
                editor.putString("tim7", thrTxt[5].getText().toString());
                editor.commit();*/
                MpdecisionNew.this.pd.dismiss();
                finish();
            }
        }, cmds.toArray(new String[cmds.size()]));

    }

}
