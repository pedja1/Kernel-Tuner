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
import android.preference.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;

import java.util.*;

import rs.pedjaapps.kerneltuner.R;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.Utility;

public class GpuSGX540 extends Activity
{

	private int gpuCurrent;
	private List<Integer> gpu;
	private SharedPreferences preferences;
	Context c;
	TextView curGpuTxt, maxGpuTxt, minGpuTxt;
	String mhz;
	String current;
	String max;
	String min;
	SeekBar seekGpu;


    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		c = this;
    	preferences = PreferenceManager.getDefaultSharedPreferences(c);
	
		super.onCreate(savedInstanceState);
		   
		setContentView(R.layout.gpu_sgx540);
		
		gpuCurrent = readFile(Constants.GPU_SGX540);
	    seekGpu = (SeekBar)findViewById(R.id.seek_gpu);
		
			gpu = Arrays.asList(153, 307, 384);
			seekBar(gpu.size()-1, gpu.indexOf(gpuCurrent));
	
		/*else{
		seekGpu.setEnabled(false);
		seekIva.setEnabled(false);
		TextView ns = (TextView)findViewById(R.id.not_supported);
		ns.setVisibility(View.VISIBLE);
		}*/
		preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		curGpuTxt = (TextView)findViewById(R.id.current_gpu);
		maxGpuTxt = (TextView)findViewById(R.id.max_gpu);
		minGpuTxt = (TextView)findViewById(R.id.min_gpu);
		
		mhz = getResources().getString(R.string.mhz);
		current = getResources().getString(R.string.current);
		max = getResources().getString(R.string._max);
		min = getResources().getString(R.string._min);
		curGpuTxt.setText(current+": "+(gpuCurrent) + mhz);
		maxGpuTxt.setText(max+": "+gpu.get(2) + mhz);
		minGpuTxt.setText(min+": "+gpu.get(0) + mhz);
		
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
			    finish();
			}
			
		});

	}

    private void seekBar(int maxGpu, int currentGpu)
	{
		
		seekGpu.setMax(maxGpu);
		seekGpu.setProgress(currentGpu);
		seekGpu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			int selectedGpu;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				curGpuTxt.setText(current+": "+(gpu.get(progress))+mhz);
				selectedGpu = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Command command = new Command(0,
			            "chmod 777 " + Constants.GPU_SGX540,
			            "echo " + selectedGpu + " > "+ Constants.GPU_SGX540);
				try{
					RootShell.getShell(true).add(command);
				}
				catch(Exception e){

				}
				gpuCurrent = readFile(Constants.GPU_SGX540);
				curGpuTxt.setText(current+": " + gpuCurrent + mhz);
				seekBar.setProgress(gpu.indexOf(gpuCurrent));
                PrefsManager.setGpu2d(gpuCurrent);
				
			}});
	}

    private Integer readFile(String path)
	{
		try
		{
			return Utility.parseInt(RCommand.readFileContent(path).trim(), 0);
		}
		catch (Exception e)
		{
			return 0;
				}
	}
    
}
