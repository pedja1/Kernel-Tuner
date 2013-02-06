package rs.pedjaapps.KernelTuner.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.tools.Initd;
import rs.pedjaapps.KernelTuner.helpers.CPUInfo;


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
		
		List<CPUInfo.VoltageList> voltageList = CPUInfo.voltages();
		
		List<String> voltageFreqs =  new ArrayList<String>();
		
		for(CPUInfo.VoltageList v: voltageList){
			voltageFreqs.add((v.getFreq()));
		}
		
		for (String s : voltageFreqs)
		{
			editor.remove("voltage_" + s);
	
		}

		List<String> govSettings = CPUInfo.govSettings();
		List<String> availableGovs = CPUInfo.availableGovs();

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

