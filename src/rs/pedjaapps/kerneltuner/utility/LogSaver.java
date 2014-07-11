package rs.pedjaapps.kerneltuner.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class LogSaver {
	public static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ssZ");
	private static final Executor EX = Executors.newSingleThreadExecutor();
	
	private Context mContext;
	private Prefs mPrefs;
	private LogDumper mLogDumper;

	public LogSaver(Context context) {
		mContext = context;
		mPrefs = new Prefs(mContext);

		mLogDumper = new LogDumper(mContext);
	}

	public File save() {
		final File path = new File(Environment.getExternalStorageDirectory(),
				"KernelTuner");
		final File file = new File(path + "/logcat."
				+ LOG_FILE_FORMAT.format(new Date()) + ".txt");

		String msg = "saving log to: " + file.toString();
		//Log.d("alogcat", msg);

		EX.execute(new Runnable() {
			public void run() {
				String dump = mLogDumper.dump(false);

				if (!path.exists()) {
					path.mkdir();
				}

				BufferedWriter bw = null;
				try {
					file.createNewFile();
					bw = new BufferedWriter(new FileWriter(file), 1024);
					bw.write(dump);
				} catch (IOException e) {
					Log.e("logcat", "error saving log", e);
				} finally {
					if (bw != null) {
						try {
							bw.close();
						} catch (IOException e) {
							Log.e("logcat", "error closing log", e);
						}
					}
				}
			}
		});

		return file;
	}

}
