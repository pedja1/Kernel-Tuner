package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.IOException;

import android.os.AsyncTask;

public class Initd extends AsyncTask<String, Void, String>
{


	@Override
	protected String doInBackground(String... args)
	{


		Process localProcess;

		if (args[0].equals("apply"))
		{
			try
			{
				localProcess = Runtime.getRuntime().exec("su");
				System.out.println("Exporting init.d scripts");
				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
				localDataOutputStream.writeBytes("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktcputweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktcputweaks\n");
				localDataOutputStream.writeBytes("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktgputweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktgputweaks\n");
				localDataOutputStream.writeBytes("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktmisctweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktmisctweaks\n");
				localDataOutputStream.writeBytes("/data/data/rs.pedjaapps.KernelTuner/files/cp /data/data/rs.pedjaapps.KernelTuner/files/99ktvoltage /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktvoltage\n");

				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
				System.out.println("Exporting init.d scripts finished");
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
		else if (args[0].equals("rm"))
		{
			try
			{
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("mount -o remount,rw /system\n");

				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktcputweaks\n");
				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktgputweaks\n");
				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktmisctweaks\n");
				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktvoltage\n");
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
		return "";
	}

}


