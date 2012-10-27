package rs.pedjaapps.KernelTuner;

import android.content.*;
import android.os.*;
import android.preference.*;
import java.io.*;

import java.lang.Process;


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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[0] + "gov", args[1]);
		editor.commit();

		return "";
	}
}	

