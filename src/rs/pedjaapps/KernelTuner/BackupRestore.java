package rs.pedjaapps.KernelTuner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Bundle;
import android.os.Environment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BackupRestore extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String them = sharedPrefs.getString("theme", "light");

		if (them.equals("light")) {
			setTheme(R.style.Theme_Sherlock_Light);
		} else if (them.equals("dark")) {
			setTheme(R.style.Theme_Sherlock);

		} else if (them.equals("light_dark_action_bar")) {
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		}
		setContentView(R.layout.backup_restore);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Button backup = (Button) findViewById(R.id.backup);
		Button restore = (Button) findViewById(R.id.restore);

		backup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				backup();
			}

		});

		restore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				restore();
			}

		});
	}

	private void backup() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String[] currentPaths = {
						"//data//rs.pedjaapps.KernelTuner//databases//KernelTuner.db",
						"//data//rs.pedjaapps.KernelTuner//shared_prefs" };
				String[] backupPaths = { "KernelTuner/KernelTuner.db",
					"KernelTuner/shared_prefs" };

				for (int i = 0; i < currentPaths.length; i++) {
					File currentDB = new File(data, currentPaths[i]);
					File backupDB = new File(sd, backupPaths[i]);

					copyDirectory(currentDB, backupDB);
					Toast.makeText(this, "Backup Successful", Toast.LENGTH_LONG)
							.show();

				}

			} else {
				Toast.makeText(this, "External Storage not Mounted",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void restore() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String[] currentPaths = {
					"//data//rs.pedjaapps.KernelTuner//databases//KernelTuner.db",
					"//data//rs.pedjaapps.KernelTuner//shared_prefs" };
				String[] backupPaths = { "KernelTuner/KernelTuner.db",
					"KernelTuner/shared_prefs" };

				for (int i = 0; i < currentPaths.length; i++) {
					File currentDB = new File(data, currentPaths[i]);
					File backupDB = new File(sd, backupPaths[i]);

					copyDirectory(backupDB, currentDB);
					Toast.makeText(this, "Restore Successful",
							Toast.LENGTH_LONG).show();

				}

			} else {
				Toast.makeText(this, "External Stirage not Mounted",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void copyDirectory(File sourceLocation, File targetLocation) {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists() && !targetLocation.mkdirs()) {
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			// make sure the directory we plan to store the recording in exists
			File directory = targetLocation.getParentFile();
			if (directory != null && !directory.exists() && !directory.mkdirs()) {
			}

			try {
				InputStream in = new FileInputStream(sourceLocation);
				OutputStream out = new FileOutputStream(targetLocation);

				// Copy the bits from instream to outstream
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, KernelTuner.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
	
}
