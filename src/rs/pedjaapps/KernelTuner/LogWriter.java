package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.IOException;

import android.os.AsyncTask;
import android.os.Environment;


public class LogWriter extends AsyncTask<String, Void, String>
{

	
	
	@Override
	protected String doInBackground(String... args)
	{

		Process localProcess;
		try
		{
			localProcess = Runtime.getRuntime().exec("su");

			DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes("echo \"" + args[0] + "\n" + args[1] + "\" >> " + Environment.getExternalStorageDirectory() + "/ktuner_error_log.txt\n");
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

		/**SharedPreferences.Editor editor = preferences.edit();
		editor.putString(args[0] + "gov", args[1]);
		editor.commit();
*/
		return "";
	}
}	

