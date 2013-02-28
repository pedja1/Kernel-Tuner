package rs.pedjaapps.KernelTuner.tools;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import rs.pedjaapps.KernelTuner.Constants;

public class RootExecuter
{
      public static void exec(String[] commands){
		  try {
			  String line;
			  Process process = Runtime.getRuntime().exec("su");
			  OutputStream stdin = process.getOutputStream();
			  InputStream stderr = process.getErrorStream();
			  InputStream stdout = process.getInputStream();
              for(String s : commands){
			  stdin.write((s).getBytes());
			  }
			  stdin.flush();

			  stdin.close();
			  BufferedReader brCleanUp =
				  new BufferedReader(new InputStreamReader(stdout));
			  while ((line = brCleanUp.readLine()) != null) {
				  Log.d(Constants.LOG_TAG, line);
			  }
			  brCleanUp.close();
			  brCleanUp =
				  new BufferedReader(new InputStreamReader(stderr));
			  while ((line = brCleanUp.readLine()) != null) {
				  Log.e(Constants.LOG_TAG, line);
			  }
			  brCleanUp.close();
			  if (process != null) {
				  process.getErrorStream().close();
				  process.getInputStream().close();
				  process.getOutputStream().close();
			  }

		  } catch (IOException ex) {
			  
		  }
	  }

}
