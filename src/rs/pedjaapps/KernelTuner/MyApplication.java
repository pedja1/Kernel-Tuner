/*
 * This file is part of the Kernel Tuner.
 *
 * Copyright Predrag Čokulov <predragcokulov@gmail.com>
 *
 * Kernel Tuner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Tuner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
 */
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
formUri = "http://www.bugsense.com/api/acra?api_key=f605d6c7",
//	formUri = "http://kerneltuner.pedjaapps.in.rs/ktuner/crash_reports/submit.php?key=XPlrFdDPHQWpDRMGbqCpsFJFGvpJqMjn",
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
