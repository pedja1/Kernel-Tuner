package rs.pedjaapps.KernelTuner.services;

import rs.pedjaapps.KernelTuner.helpers.Lock;
import rs.pedjaapps.KernelTuner.tools.LogSaver;
import android.app.IntentService;
import android.content.Intent;

public class LogcatSaveService extends IntentService {
	public LogcatSaveService() {
		super("saveService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//Log.d("alogcat", "handling intent");

		LogSaver saver = new LogSaver(this);
		saver.save();

		Lock.release();
	}
}
