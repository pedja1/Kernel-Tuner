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
package rs.pedjaapps.KernelTuner.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.tools.Initd;
import rs.pedjaapps.KernelTuner.helpers.IOHelper;


public class AppReset
{

	final Context context;
	final SharedPreferences sharedPrefs;
	final SharedPreferences.Editor editor;
	public AppReset(Context context)
	{
		this.context = context;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sharedPrefs.edit();
	}

	
	
	final public void reset(){
		
		List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
		
		List<String> voltageFreqs =  new ArrayList<String>();
		
		for(IOHelper.VoltageList v: voltageList){
			voltageFreqs.add((v.getFreq()));
		}
		
		for (String s : voltageFreqs)
		{
			editor.remove("voltage_" + s);
	
		}

		List<String> govSettings = IOHelper.govSettings();
		List<String> availableGovs = IOHelper.availableGovs();

		for (String s : availableGovs)
		{
			for (String st : govSettings)
			{
				editor.remove(s + "_" + st);
			}
		}
		editor.remove("gpu3d");
		editor.remove("gpu2d");
		editor.remove("led");
		editor.remove("cpu0gov");
		editor.remove("cpu1gov");
		editor.remove("cpu2gov");
		editor.remove("cpu3gov");
		editor.remove("cpu0min");
		editor.remove("cpu1min");
		editor.remove("cpu2min");
		editor.remove("cpu3min");
		editor.remove("cpu0max");
		editor.remove("cpu1max");
		editor.remove("cpu2max");
		editor.remove("cpu3max");
		editor.remove("fastcharge");
		editor.remove("vsync");
		editor.remove("hw");
		editor.remove("backbuf");
		editor.remove("cdepth");
		editor.remove("io");
		editor.remove("cdcache");
		editor.remove("dalaynew");
		editor.remove("pausenew");
		editor.remove("thruploadnew");
		editor.remove("thrdownloadnew");
		editor.remove("thrupmsnew");
		editor.remove("thrdownmsnew");
		editor.remove("ldt");
		editor.remove("s2w");
		editor.remove("p1freq");
		editor.remove("p2freq");
		editor.remove("p3freq");
		editor.remove("p1low");
		editor.remove("p1high");
		editor.remove("p2low");
		editor.remove("p2high");
		editor.remove("p3low");
		editor.remove("p3high");
		editor.remove("s2wStart");
		editor.remove("s2wEnd");
		editor.remove("swap");
		editor.remove("swap_location");
		editor.remove("swappiness");
		editor.remove("oom");
		editor.remove("otg");
		editor.remove("idle_freq");
		editor.remove("scroff");
		editor.remove("scroff_single");
		editor.commit();
		new Initd().execute(new String[]{"rm"});
	}
	
	
	
}	

