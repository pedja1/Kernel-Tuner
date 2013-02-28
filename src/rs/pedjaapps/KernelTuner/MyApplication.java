package rs.pedjaapps.KernelTuner;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.apache.commons.io.FileUtils;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;


@ReportsCrashes(formKey = "",//"dDlUVUxrRnNKeU40X3VmejBFenF0Z0E6MQ",
// formUri = "http://www.bugsense.com/api/acra?api_key=f605d6c7",
	formUri = "http://kerneltuner.pedjaapps.in.rs/ktuner/crash_reports/submit.php?key=XPlrFdDPHQWpDRMGbqCpsFJFGvpJqMjn",
				includeDropBoxSystemTags = true,
				deleteUnapprovedReportsOnApplicationStart = true
				/*mode = ReportingInteractionMode.NOTIFICATION, 
				resNotifTickerText = R.string.crash_notif_ticker_text, 
				resNotifTitle = R.string.crash_notif_title, 
				resNotifText = R.string.crash_notif_text, 
				resNotifIcon = android.R.drawable.stat_notify_error,
				resDialogText = R.string.crash_dialog_text, 
				resDialogIcon = android.R.drawable.ic_dialog_info, 
				resDialogTitle = R.string.crash_dialog_title, 
				resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, 
				resDialogOkToast = R.string.crash_dialog_ok_toast*/)

public class MyApplication extends Application {

	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}

}
