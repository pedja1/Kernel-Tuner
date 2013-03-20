package rs.pedjaapps.KernelTuner.receiver;

import rs.pedjaapps.KernelTuner.helpers.Lock;
import rs.pedjaapps.KernelTuner.services.LogcatShareService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LogcatShareReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d("alogcat", "received intent for share");

		rs.pedjaapps.KernelTuner.intents.Intent.handleExtras(context, intent);

		Lock.acquire(context);

		Intent svcIntent = new Intent(context, LogcatShareService.class);
		context.startService(svcIntent);
	}

}
