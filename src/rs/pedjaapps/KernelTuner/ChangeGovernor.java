package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class ChangeGovernor extends AsyncTask<String, Void, String>
{

	Context context;

	public ChangeGovernor(Context context)
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
			localDataOutputStream.writeBytes("chmod 777 /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor\n");
			localDataOutputStream.writeBytes("echo " + args[1] + " > /sys/devices/system/cpu/" + args[0] + "/cpufreq/scaling_governor\n");
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
		editor.putString(args[0] + "gov", args[1]);
		editor.commit();

		return "";
	}
}	

