package rs.pedjaapps.KernelTuner;

import android.content.*;
import android.os.*;
import android.preference.*;
import java.io.*;

import java.lang.Process;


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

		Process localProcess;
		try
		{
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n");
			localDataOutputStream.writeBytes("echo " + args[0].trim() + " > /sys/devices/system/cpu/cpufreq/" + args[2] + "/" + args[1].trim() + "\n");
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
		editor.putString(args[2] + "_" + args[1], args[0]);
		editor.commit();

		return "";
	}
}	

