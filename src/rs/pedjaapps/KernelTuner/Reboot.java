package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import java.io.*;
import java.lang.Process;



public class Reboot extends Activity
{
	String reboot;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		reboot = intent.getExtras().getString("reboot");
		
		Process localProcess;
		try
		{
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("/data/data/rs.pedjaapps.KernelTuner/files/reboot " + reboot + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localDataOutputStream.close();
			localProcess.waitFor();
			localProcess.destroy();
		}
		catch (IOException e)
		{
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
		}
		catch (InterruptedException e)
		{
			new LogWriter().execute(new String[] {getClass().getName(), e.getMessage()});
		}
	}
}
