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

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.adapter.CPUAdapter;
import rs.pedjaapps.kerneltuner.model.*;
import rs.pedjaapps.kerneltuner.root.*;
import rs.pedjaapps.kerneltuner.utility.IOHelper;


public class GpuActivityQNew extends AbsActivity implements AdapterView.OnItemClickListener, RootUtils.CommandCallback
{

    ListView mList;
    CPUAdapter mListAdapter;
    ProgressBar pbLoading;
    
	Handler uiHandler;
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		uiHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu_q_new);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mListAdapter = new CPUAdapter(this, new ArrayList<CPU>());
        mList.setAdapter(mListAdapter);
        new ATPopulateCpuList().execute();
    }

    private List<CPU> populateList()
    {
        List<CPU> list = new ArrayList<>();
        CPU cpu = new CPU();
        cpu.setType(CPU.TYPE_HEADER);
        cpu.setTitle(getString(R.string.gpu));
        list.add(cpu);

		if(Constants.GPU_3D_2.exists())
		{
        cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_MAX);
        cpu.setTitle(getString(R.string.maximum_gpu_frequency));
        cpu.setValue(IOHelper.gpu3d() / 1000000 + getString(R.string.mhz));
        list.add(cpu);
		}

		if(Constants.GPU_3D_2_GOV.exists())
		{
        cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_GOV);
        cpu.setTitle(getString(R.string.gpu_governor));
        cpu.setValue(IOHelper.gpu3dGovernor());
        list.add(cpu);
		}
		
		cpu = new CPU();
        cpu.setType(CPU.TYPE_HEADER);
        cpu.setTitle(getString(R.string.warning));
        list.add(cpu);
		
		cpu = new CPU();
        cpu.setType(CPU.TYPE_ITEM);
        cpu.setItemType(CPU.ITEM_TYPE_INFO);
        cpu.setTitle(getString(R.string.warning));
        cpu.setValue(getString(R.string.gpu_warning));
        list.add(cpu);
		
        return list;
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
                showSelectFreqDialog(cpu);
                break;
            case CPU.ITEM_TYPE_GOV:
                showSelectGovDialog(cpu);
                break;
        }
    }

    private void showSelectFreqDialog(final CPU cpu)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_freq));
        builder.setNegativeButton(R.string.cancel, null);

        final List<Frequency> freqs = IOHelper.gpu3dFrequenciesAsList();
		String[] items = new String[freqs.size()];
		for(int i = 0; i < items.length; i++)
		{
			items[i] = freqs.get(i).getFrequencyString();
		}
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Frequency frequency = FrequencyCollection.getFrequencyForIndex(i, freqs);
                if (frequency != null) switch (cpu.getItemType())
                {
                    case CPU.ITEM_TYPE_MAX:
                        RCommand.setGpuMaxFreq(frequency.getFrequencyValue(), GpuActivityQNew.this);
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

        final String[] items = {"performance", "ondemand", "simple", "interactive"};//IOHelper.governors();
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                RCommand.setGpuGov(items[i], GpuActivityQNew.this);
            }
        });

        builder.show();
    }


    private class ATPopulateCpuList extends AsyncTask<Void, Void, List<CPU>>
    {
        @Override
        protected List<CPU> doInBackground(Void... voids)
        {
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
    }
	
	@Override
	public void onComplete(RootUtils.Status status, String output)
	{
		new ATPopulateCpuList().execute();
	}

	@Override
	public void out(String line)
	{
		// TODO: Implement this method
	}
}
