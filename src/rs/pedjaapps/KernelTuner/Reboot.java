package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class Reboot extends Activity
{
	private String reboot;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		reboot = intent.getExtras().getString("reboot");
		
		
		try {
            String line;
            Process process = Runtime.getRuntime().exec("su");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();

            stdin.write(("/data/data/rs.pedjaapps.KernelTuner/files/reboot " + reboot + "\n").getBytes());
            
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
	}
}
