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

import android.app.AlertDialog;
import android.content.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.*;

import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.adapter.CPUAdapter;
import rs.pedjaapps.kerneltuner.model.*;
import rs.pedjaapps.kerneltuner.root.*;
import rs.pedjaapps.kerneltuner.utility.*;
import rs.pedjaapps.linpack.LinpackMainActivity;


public class CPUActivity extends AbsActivity implements RootUtils.CommandCallback, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, View.OnTouchListener
{

    ListView mList;
    CPUAdapter mListAdapter;
    ProgressBar pbLoading;
    boolean showAllCores = false;
    Handler uiHandler;
    boolean mcWarningShown = false;
    Switch mSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        uiHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        showAllCores = PrefsManager.cpuShowAllCores();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View view = getLayoutInflater().inflate(R.layout.layout_cpu_ab_switch, null);
        mSwitch = (Switch) view.findViewById(R.id.swShowAllCores);
        mSwitch.setChecked(showAllCores);
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setOnTouchListener(this);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setCustomView(view, lp);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mListAdapter = new CPUAdapter(this, new ArrayList<CPU>());
        mList.setAdapter(mListAdapter);
        refreshView();

        if (new File(Constants.MPDECISION).exists()/* || new File(Constants.MPDECISION_BINARY).exists()*/)
        {
            Button mp = (Button) findViewById(R.id.btn_mpdecision);
            mp.setOnClickListener(new SimpleStartActivityListener(Mpdecision.class));
            mp.setOnLongClickListener(new InfoListener(R.drawable.main_mp,
                    getResources().getString(R.string.info_mpd_title),
                    getResources().getString(R.string.info_mpd_text),
                    Constants.G_S_URL_PREFIX + "mp-decision", true));
            mp.setVisibility(View.VISIBLE);
        }
        if (new File(Constants.THERMALD).exists()/* || new File(Constants.THERMALD_BINARY).exists()*/)
        {
            Button thermal = (Button) findViewById(R.id.btn_thermal);
            thermal.setOnClickListener(new SimpleStartActivityListener(Thermald.class));

            thermal.setOnLongClickListener(new InfoListener(R.drawable.main_thermal,
                    getResources().getString(R.string.info_thermal_title),
                    getResources().getString(R.string.info_thermal_text), "", false));
            thermal.setVisibility(View.VISIBLE);
        }

    }

    private void refreshView()
    {
        if (showAllCores) RCommand.toggleAllCpu(this, true);
        else onComplete(null, null);
    }

    private List<CPU> populateList()
    {
        List<CPU> list = new ArrayList<>();
        CPU cpu = new CPU();
        cpu.setType(CPU.TYPE_HEADER);
        cpu.setTitle(getString(showAllCores ? R.string.cpu0 : R.string.cpu));
        list.add(cpu);

        cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_MAX);
        cpu.setCpuNum(0);
        cpu.setTitle(getString(R.string.maximum_frequency));
        cpu.setValue(IOHelper.cpu0MaxFreq() / 1000 + getString(R.string.mhz));
        list.add(cpu);

        cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_MIN);
        cpu.setCpuNum(0);
        cpu.setTitle(getString(R.string.minimum_frequncy));
        cpu.setValue(IOHelper.cpu0MinFreq() / 1000 + getString(R.string.mhz));
        list.add(cpu);

        cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_GOV);
        cpu.setCpuNum(0);
        cpu.setTitle(getString(R.string.cpu_governor));
        cpu.setValue(IOHelper.cpu0CurGov());
        list.add(cpu);

        if (showAllCores)
        {
            if (IOHelper.cpu1Exists() && IOHelper.cpuOnline(1))
            {
                cpu = new CPU();
                cpu.setType(CPU.TYPE_HEADER);
                cpu.setTitle(getString(R.string.cpu1));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MAX);
                cpu.setCpuNum(1);
                cpu.setTitle(getString(R.string.maximum_frequency));
                cpu.setValue(IOHelper.cpu1MaxFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MIN);
                cpu.setCpuNum(1);
                cpu.setTitle(getString(R.string.minimum_frequncy));
                cpu.setValue(IOHelper.cpu1MinFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_GOV);
                cpu.setCpuNum(1);
                cpu.setTitle(getString(R.string.cpu_governor));
                cpu.setValue(IOHelper.cpu1CurGov());
                list.add(cpu);
            }
            if (IOHelper.cpu2Exists() && IOHelper.cpuOnline(2))
            {
                cpu = new CPU();
                cpu.setType(CPU.TYPE_HEADER);
                cpu.setTitle(getString(R.string.cpu2));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MAX);
                cpu.setCpuNum(2);
                cpu.setTitle(getString(R.string.maximum_frequency));
                cpu.setValue(IOHelper.cpu2MaxFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MIN);
                cpu.setCpuNum(2);
                cpu.setTitle(getString(R.string.minimum_frequncy));
                cpu.setValue(IOHelper.cpu2MinFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_GOV);
                cpu.setCpuNum(2);
                cpu.setTitle(getString(R.string.cpu_governor));
                cpu.setValue(IOHelper.cpu2CurGov());
                list.add(cpu);
            }
            if (IOHelper.cpu3Exists() && IOHelper.cpuOnline(3))
            {
                cpu = new CPU();
                cpu.setType(CPU.TYPE_HEADER);
                cpu.setTitle(getString(R.string.cpu3));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MAX);
                cpu.setCpuNum(3);
                cpu.setTitle(getString(R.string.maximum_frequency));
                cpu.setValue(IOHelper.cpu3MaxFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_MIN);
                cpu.setCpuNum(3);
                cpu.setTitle(getString(R.string.minimum_frequncy));
                cpu.setValue(IOHelper.cpu3MinFreq() / 1000 + getString(R.string.mhz));
                list.add(cpu);

                cpu = new CPU();
                cpu.setType(CPU.TYPE_ITEM);
                cpu.setItemType(CPU.ITEM_TYPE_GOV);
                cpu.setCpuNum(3);
                cpu.setTitle(getString(R.string.cpu_governor));
                cpu.setValue(IOHelper.cpu3CurGov());
                list.add(cpu);
            }
        }
        if (IOHelper.cpuScreenOff())
        {
            cpu = new CPU();
            cpu.setType(CPU.TYPE_HEADER);
            cpu.setTitle(getString(R.string.screen_off_frequency));
            list.add(cpu);

            cpu = new CPU();
            cpu.setType(CPU.TYPE_ITEM);
            cpu.setItemType(CPU.ITEM_TYPE_SCRN);
            cpu.setTitle(getString(R.string.maximum_screen_off_frequency));
            cpu.setValue(IOHelper.cpuScreenOffMaxFreq() / 1000 + getString(R.string.mhz));
            list.add(cpu);
        }
		
		cpu = new CPU();
		cpu.setType(CPU.TYPE_HEADER);
		cpu.setTitle(getString(R.string.linpack));
		list.add(cpu);

		cpu = new CPU();
		cpu.setType(CPU.TYPE_ITEM);
		cpu.setItemType(CPU.ITEM_TYPE_LINPACK);
		cpu.setTitle(getString(R.string.linpack));
		cpu.setValue(getString(R.string.start_single));
		list.add(cpu);
        return list;
    }

    @Override
    public void onComplete(RootUtils.Status status, String output)
    {
        new ATPopulateCpuList().execute();
    }

    @Override
    public void out(String line)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //we don't check whether its a header or item because header cant be clicked
        //(isEnabled returns false for header in adapter)
        CPU cpu = mListAdapter.getItem(i);
        switch (cpu.getItemType())
        {
            case CPU.ITEM_TYPE_MAX:
            case CPU.ITEM_TYPE_MIN:
            case CPU.ITEM_TYPE_SCRN:
                showSelectFreqDialog(cpu);
                break;
            case CPU.ITEM_TYPE_GOV:
                showSelectGovDialog(cpu);
                break;
			case CPU.ITEM_TYPE_LINPACK:
				startActivity(new Intent(this, LinpackMainActivity.class));
				break;
        }
    }

    private void showSelectFreqDialog(final CPU cpu)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_freq));
        builder.setNegativeButton(R.string.cancel, null);

        String[] items = FrequencyCollection.getInstance().getFrequenciesAsStringArray();
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Frequency frequency = FrequencyCollection.getFrequencyForIndex(i, FrequencyCollection.getInstance().getFrequencies());
                if (frequency != null) switch (cpu.getItemType())
                {
                    case CPU.ITEM_TYPE_MAX:
                        RCommand.setMaxFreq(cpu.getCpuNum(), frequency.getFrequencyValue(), CPUActivity.this);
                        break;
                    case CPU.ITEM_TYPE_MIN:
                        RCommand.setMinFreq(cpu.getCpuNum(), frequency.getFrequencyValue(), CPUActivity.this);
                        break;
                    case CPU.ITEM_TYPE_SCRN:
                        RCommand.setMaxScroffFreq(cpu.getCpuNum(), frequency.getFrequencyValue(), CPUActivity.this);
                        break;
                }
            }
        });

        builder.show();
    }

    private void showSelectGovDialog(final CPU cpu)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_governor));
        builder.setNegativeButton(R.string.cancel, null);

        final String[] items = IOHelper.governors();
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                RCommand.setGovernor(cpu.getCpuNum(), items[i], CPUActivity.this);
            }
        });

        builder.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        showAllCores = b;
        PrefsManager.setCpuShowAllCores(showAllCores);
        refreshView();
    }


    private class ATPopulateCpuList extends AsyncTask<Void, Void, List<CPU>>
    {

        @Override
        protected List<CPU> doInBackground(Void... voids)
        {
            //allow cpu to settle
            try
            {
                if (showAllCores) Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return populateList();
        }

        @Override
        protected void onPreExecute()
        {
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<CPU> list)
        {
            mListAdapter.clear();
            mListAdapter.addAll(list);
            mListAdapter.notifyDataSetChanged();
            pbLoading.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //RCommand.toggleAllCpu(null, false);
        if(PrefsManager.getMpdecEnabled() == -1 || PrefsManager.getMpdecEnabled() == 0)RCommand.toggleMpDecision(null, true);
    }

    @Override
    public boolean onTouch(View p1, MotionEvent p2)
    {
        if (p2.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (!showAllCores && !mcWarningShown)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.cpu_multicore_warning);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface p1, int p2)
                    {
                        mcWarningShown = true;
                        mSwitch.performClick();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();
                return true;
            }
        }
        return false;
    }
}
