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

import android.os.*;
import android.widget.*;
import java.util.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.helpers.*;
import rs.pedjaapps.kerneltuner.model.*;


public class CPUActivity extends AbsActivity
{

    ListView mList;
	CPUAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        
		mList = (ListView)findViewById(R.id.list);
		mListAdapter = new CPUAdapter(this, new ArrayList<CPU>());
		mList.setAdapter(mListAdapter);
		
		populateList();
    }

	private void populateList()
	{
		
		CPU cpu = new CPU();
		cpu.setType(CPU.TYPE_HEADER);
		cpu.setTitle(getString(R.string.cpu0));
		mListAdapter.add(cpu);
		
		cpu = new CPU();
		cpu.setType(CPU.TYPE_ITEM);
		cpu.setItemType(CPU.ITEM_TYPE_MAX);
		cpu.setCpuNum(0);
		cpu.setTitle("Maximum CPU frequency");
		cpu.setValue(IOHelper.cpu0MaxFreq() / 1000 + getString(R.string.mhz));
		mListAdapter.add(cpu);
		
		cpu = new CPU();
		cpu.setType(CPU.TYPE_ITEM);
		cpu.setItemType(CPU.ITEM_TYPE_MIN);
		cpu.setCpuNum(0);
		cpu.setTitle("Minimum CPU frequency");
		cpu.setValue(IOHelper.cpu0MinFreq() / 1000 + getString(R.string.mhz));
		mListAdapter.add(cpu);
		
		cpu = new CPU();
		cpu.setType(CPU.TYPE_ITEM);
		cpu.setItemType(CPU.ITEM_TYPE_GOV);
		cpu.setCpuNum(0);
		cpu.setTitle("CPU governor");
		cpu.setValue(IOHelper.cpu0CurGov());
		mListAdapter.add(cpu);
		
		if(IOHelper.cpu1Exists())
		{
			cpu = new CPU();
			cpu.setType(CPU.TYPE_HEADER);
			cpu.setTitle(getString(R.string.cpu1));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MAX);
			cpu.setCpuNum(1);
			cpu.setTitle("Maximum CPU frequency");
			cpu.setValue(IOHelper.cpu1MaxFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MIN);
			cpu.setCpuNum(1);
			cpu.setTitle("Minimum CPU frequency");
			cpu.setValue(IOHelper.cpu1MinFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_GOV);
			cpu.setCpuNum(1);
			cpu.setTitle("CPU governor");
			cpu.setValue(IOHelper.cpu1CurGov());
			mListAdapter.add(cpu);
		}
		if(IOHelper.cpu2Exists())
		{
			cpu = new CPU();
			cpu.setType(CPU.TYPE_HEADER);
			cpu.setTitle(getString(R.string.cpu2));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MAX);
			cpu.setCpuNum(2);
			cpu.setTitle("Maximum CPU frequency");
			cpu.setValue(IOHelper.cpu2MaxFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MIN);
			cpu.setCpuNum(2);
			cpu.setTitle("Minimum CPU frequency");
			cpu.setValue(IOHelper.cpu2MinFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_GOV);
			cpu.setCpuNum(2);
			cpu.setTitle("CPU governor");
			cpu.setValue(IOHelper.cpu2CurGov());
			mListAdapter.add(cpu);
		}
		if(IOHelper.cpu3Exists())
		{
			cpu = new CPU();
			cpu.setType(CPU.TYPE_HEADER);
			cpu.setTitle(getString(R.string.cpu3));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MAX);
			cpu.setCpuNum(3);
			cpu.setTitle("Maximum CPU frequency");
			cpu.setValue(IOHelper.cpu3MaxFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_MIN);
			cpu.setCpuNum(3);
			cpu.setTitle("Minimum CPU frequency");
			cpu.setValue(IOHelper.cpu3MinFreq() / 1000 + getString(R.string.mhz));
			mListAdapter.add(cpu);

			cpu = new CPU();
			cpu.setType(CPU.TYPE_ITEM);
			cpu.setItemType(CPU.ITEM_TYPE_GOV);
			cpu.setCpuNum(3);
			cpu.setTitle("CPU governor");
			cpu.setValue(IOHelper.cpu3CurGov());
			mListAdapter.add(cpu);
		}
		mListAdapter.notifyDataSetChanged();
	}

}
