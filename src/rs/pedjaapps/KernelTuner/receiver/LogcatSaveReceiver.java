package rs.pedjaapps.KernelTuner.receiver;

import rs.pedjaapps.KernelTuner.helpers.Lock;
import rs.pedjaapps.KernelTuner.services.LogcatSaveService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LogcatSaveReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d("alogcat", "received intent for save");

		rs.pedjaapps.KernelTuner.intents.Intent.handleExtras(context, intent);

		Lock.acquire(context);

		Intent svcIntent = new Intent(context, LogcatSaveService.class);
		context.startService(svcIntent);
	}
}
