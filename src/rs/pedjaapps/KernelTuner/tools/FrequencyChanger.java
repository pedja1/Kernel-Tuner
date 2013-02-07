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



public class FrequencyChanger extends AsyncTask<String, Void, String>
{

	Context context;

	public FrequencyChanger(Context context)
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

            stdin.write(("chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n").getBytes());
            stdin.write(("echo " + args[2] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n").getBytes());
            stdin.write(("chmod 444 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n").getBytes());
            stdin.write(("chown system /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n").getBytes());
            
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
		editor.putString(args[0] + args[1], args[2]);
		editor.commit();
		return "";
	}
}	

