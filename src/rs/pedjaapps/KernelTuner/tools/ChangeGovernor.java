package rs.pedjaapps.KernelTuner.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class ChangeGovernor extends AsyncTask<String, Void, String>
{

	final Context context;

	public ChangeGovernor(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}


	final SharedPreferences preferences;
	
	@Override
	protected String doInBackground(String... args)
	{

		 try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor\n").getBytes());
	            stdin.write(("echo " + args[1] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner ChangeGovernor Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner ChangeGovernor Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }
		 SharedPreferences.Editor editor = preferences.edit();
			editor.putString(args[0] + "gov", args[1]);
			editor.commit();
		return "";
	}
}	

