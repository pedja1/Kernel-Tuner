package rs.pedjaapps.KernelTuner;

import android.content.*;
import android.os.*;
import android.preference.*;
import java.io.*;

import java.lang.Process;



public class FrequencyChanger extends AsyncTask<String, Void, String>
{

	Context context;

	public FrequencyChanger(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}
	

	SharedPreferences preferences;
	@Override
	protected String doInBackground(String... args)
	{


		Process localProcess;
		try
		{
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n");
			localDataOutputStream.writeBytes("echo " + args[2] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n");
			localDataOutputStream.writeBytes("chmod 444 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_" + args[1] + "_freq\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();

		}
		catch (IOException e1)
		{
			new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
		}
		catch (InterruptedException e1)
		{
			new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[0] + args[1], args[2]);
		editor.commit();
		return "";
	}
}	

