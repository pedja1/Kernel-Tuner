package rs.pedjaapps.KernelTuner;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.apache.commons.io.FileUtils;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;


@ReportsCrashes(formKey = "dDlUVUxrRnNKeU40X3VmejBFenF0Z0E6MQ",
// formUri = "http://www.bugsense.com/api/acra?api_key=f605d6c7",
				includeDropBoxSystemTags = true,
				mode = ReportingInteractionMode.NOTIFICATION, 
				resNotifTickerText = R.string.crash_notif_ticker_text, 
				resNotifTitle = R.string.crash_notif_title, 
				resNotifText = R.string.crash_notif_text, 
				resNotifIcon = android.R.drawable.stat_notify_error,
				resDialogText = R.string.crash_dialog_text, 
				resDialogIcon = android.R.drawable.ic_dialog_info, 
				resDialogTitle = R.string.crash_dialog_title, 
				resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, 
				resDialogOkToast = R.string.crash_dialog_ok_toast)

public class MyApplication extends Application {

	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		/*try {
			ACRA.getErrorReporter().putCustomData("freq_table",
					FileUtils.readFileToString(new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies")));
			ACRA.getErrorReporter().putCustomData("cpu_max",
					FileUtils.readFileToString(new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq")));
			ACRA.getErrorReporter().putCustomData("cpu_min",
					FileUtils.readFileToString(new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq")));
			ACRA.getErrorReporter().putCustomData("gpu3d_max",
					FileUtils.readFileToString(new File(IOHelper.GPU_2D)));
			ACRA.getErrorReporter().putCustomData("gpu3d_max",
					FileUtils.readFileToString(new File(IOHelper.GPU_3D)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
