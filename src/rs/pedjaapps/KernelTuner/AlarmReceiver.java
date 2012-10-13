package rs.pedjaapps.KernelTuner;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

 
public class AlarmReceiver extends BroadcastReceiver {
 
 @Override
 public void onReceive(Context context, Intent intent) {
   
   Intent UpdateService = new Intent(context, UpdateCheckService.class);
   context.startService(UpdateService);
 }
 
}