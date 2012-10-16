package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.IOException;

import android.os.AsyncTask;

public class Initd extends AsyncTask<String, Void, String> {
	
	
	@Override
	protected String doInBackground(String... args) {
      
		
		Process localProcess;
		
		if(args[0].equals("apply")){
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("busybox mount -o remount,rw /system\n");
				localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99ktcputweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktcputweaks\n");
				localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99ktgputweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktgputweaks\n");
				localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99ktmisctweaks /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktmisctweaks\n");
				localDataOutputStream.writeBytes("cp /data/data/rs.pedjaapps.KernelTuner/files/99ktvoltage /system/etc/init.d\n");
				localDataOutputStream.writeBytes("chmod 777 /system/etc/init.d/99ktvoltage\n");
					
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else if(args[0].equals("rm")){
			try {
				localProcess = Runtime.getRuntime().exec("su");

				DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
				localDataOutputStream.writeBytes("busybox mount -o remount,rw /system\n");

				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktcputweaks\n");
				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktgputweaks\n");
				localDataOutputStream.writeBytes("rm /system/etc/init.d/99ktmisctweaks\n");
				localDataOutputStream.writeBytes("exit\n");
				localDataOutputStream.flush();
				localDataOutputStream.close();
				localProcess.waitFor();
				localProcess.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
         return "";
     }

	}


