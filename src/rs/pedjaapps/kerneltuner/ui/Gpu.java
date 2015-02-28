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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import java.util.Arrays;
import java.util.List;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.Utility;

import android.widget.SeekBar;

public class Gpu extends Activity
{

	private int gpu2dcurent;
	private int gpu3dcurent ;
	private int gpu2dmax;
	private int gpu3dmax;
	private static String board;
	private List<Integer> gpu2d;
	private List<Integer> gpu3d;
	private SharedPreferences preferences;
	Context c;
	TextView cur2dTxt;
	TextView cur3dTxt;
	TextView max2dTxt;
	TextView max3dTxt;
	String mhz;
	String current;
	String max;
	SeekBar seek2d;
	SeekBar seek3d;


    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		c = this;
    	board = android.os.Build.DEVICE;
    	preferences = PreferenceManager.getDefaultSharedPreferences(c);
	
		super.onCreate(savedInstanceState);
		   
		setContentView(R.layout.gpu);
		gpu2dmax = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
		gpu3dmax = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
		gpu2dcurent = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpuclk");
		gpu3dcurent = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpuclk");
		seek2d = (SeekBar)findViewById(R.id.seek_2d);
		seek3d = (SeekBar)findViewById(R.id.seek_3d);
		List<String> adreno220 = Arrays.asList("shooter", "shooteru", "pyramid", "tenderloin", "vigor", "rider", "nozomi", "LT26i", "hikari", "doubleshot", "su640","SHV-E160S","SHV-E160L", "SHV-E120L", "holiday");
		List<String> adreno225 = Arrays.asList("evita", "ville", "jewel", "d2spr", "d2tmo");
		List<String> adreno320 = Arrays.asList("mako","dlx");
		if (adreno220.contains(board))
		{
			gpu2d = Arrays.asList(160000000, 200000000, 228571000, 266667000);
			gpu3d = Arrays.asList(200000000, 228571000, 266667000, 300000000, 320000000);
		    seekBar(gpu2d.size()-1, gpu3d.size()-1, gpu2d.indexOf(gpu2dmax), gpu3d.indexOf(gpu3dmax));
		}
		else if (adreno225.contains(board))
		{
			gpu2d = Arrays.asList(320000000, 266667000, 228571000, 200000000, 160000000, 96000000, 27000000);
			gpu3d = Arrays.asList(512000000, 400000000, 320000000, 300000000, 266667000, 228571000, 200000000, 177778000, 27000000);
			seekBar(gpu2d.size()-1, gpu3d.size()-1, gpu2d.indexOf(gpu2dmax), gpu3d.indexOf(gpu3dmax));
		}
		else if (adreno320.contains(board))
		{
			
			gpu2d = Arrays.asList(27000000, 48000000, 54857000, 64000000,
                    76800000,
                    96000000,
                    128000000,
                    145455000,
                    160000000,
                    177778000,
                    200000000,
                    266667000,
                    300000000);
			gpu3d = Arrays.asList(27000000,
                    48000000,
                    54857000,
                    64000000,
                    76800000,
                    96000000,
                    128000000,
                    145455000,
                    160000000,
                    177778000,
                    200000000,
                    228571000,
                    266667000,
                    300000000,
                    320000000,
                    400000000,
                    450000000,
                    500000000);
			seekBar(gpu2d.size()-1, gpu3d.size()-1, gpu2d.indexOf(gpu2dmax), gpu3d.indexOf(gpu3dmax));
		}
		else{
		seek2d.setEnabled(false);
		seek3d.setEnabled(false);
		TextView ns = (TextView)findViewById(R.id.not_supported);
		ns.setVisibility(View.VISIBLE);
		}
		preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		cur2dTxt = (TextView)findViewById(R.id.current_2d);
		cur3dTxt = (TextView)findViewById(R.id.current_3d);
		max2dTxt = (TextView)findViewById(R.id.max_2d);
		max3dTxt = (TextView)findViewById(R.id.max_3d);
		mhz = getResources().getString(R.string.mhz);
		current = getResources().getString(R.string.current);
		max = getResources().getString(R.string._max);
		cur3dTxt.setText(current+": "+(gpu3dcurent/1000000) + mhz);
		cur2dTxt.setText(current+": "+(gpu2dcurent/1000000) + mhz);
		
		max3dTxt.setText(max+": "+(gpu3dmax/1000000) + mhz);
		max2dTxt.setText(max+": "+(gpu2dmax/1000000) + mhz);
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
			finish();
			}
			
		});

	}

    private void seekBar(int max2d, int max3d, int current2d, int current3d)
	{
		
		seek2d.setMax(max2d);
		seek2d.setProgress(current2d);
		seek3d.setMax(max3d);
		seek3d.setProgress(current3d);
		seek2d.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			int selected2d;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				max2dTxt.setText(max+": "+(gpu2d.get(progress)/1000000)+mhz);
				selected2d = gpu2d.get(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				CommandCapture command = new CommandCapture(0, 
			            "chmod 777 /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk",
			            "chmod 777 /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk",
			            "echo " + selected2d + " > /sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk",
			            "echo " + selected2d + " > /sys/devices/platform/kgsl-2d1.1/kgsl/kgsl-2d1/max_gpuclk");
				try{
					RootTools.getShell(true).add(command);
				}
				catch(Exception e){

				}
				gpu2dmax = readFile("/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
				max2dTxt.setText(max+": "+(gpu2dmax/1000000) + mhz);
				seekBar.setProgress(gpu2d.indexOf(gpu2dmax));
                PrefsManager.setGpu2d(gpu2dmax);
				
			}});
		seek3d.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			int selected3d;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				max3dTxt.setText(max+": "+(gpu3d.get(progress)/1000000)+mhz);
				selected3d = gpu3d.get(progress);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				CommandCapture command = new CommandCapture(0,  
						"chmod 777 /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk",
			            "echo " + selected3d + " > /sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
				try{
					RootTools.getShell(true).add(command);
				}
				catch(Exception e){

				}
				gpu3dmax = readFile("/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
				max3dTxt.setText(max+": "+(gpu3dmax/1000000) + mhz);
				seekBar.setProgress(gpu3d.indexOf(gpu3dmax));
                PrefsManager.setGpu3d(gpu3dmax);
				
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
