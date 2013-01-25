package rs.pedjaapps.KernelTuner;

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


public class ChangeGovernorSettings extends AsyncTask<String, Void, String>
{

	Context context;
	
	public ChangeGovernorSettings(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}


	SharedPreferences preferences;

	@Override
	protected String doInBackground(String... args)
	{

	
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n").getBytes());
            stdin.write(("echo " + args[0].trim() + " > /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n").getBytes());
            
            stdin.flush();

            stdin.close();
            BufferedReader brCleanUp =
                    new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                Log.d("[KernelTuner ChangeGovernorSettings Output]", line);
            }
            brCleanUp.close();
            brCleanUp =
                    new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
            	Log.e("[KernelTuner ChangeGovernorSettings Error]", line);
            }
            brCleanUp.close();

        } catch (IOException ex) {
        }
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[2] + "_" + args[1], args[0]);
		editor.commit();

		return "";
	}
}	

