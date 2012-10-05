package rs.pedjaapps.KernelTuner;

import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class anthraxTweaks extends Activity {
	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = sharedPrefs.getString("theme", "system");
		if (theme.equals("system")) {
			setTheme(android.R.style.Theme_DeviceDefault);
		} else if (theme.equals("holo")) {
			setTheme(android.R.style.Theme_Holo);
		} else if (theme.equals("holo_light")) {
			setTheme(android.R.style.Theme_Holo_Light);
		} else if (theme.equals("dark")) {
			setTheme(android.R.style.Theme_Black);
		} else if (theme.equals("light")) {
			setTheme(android.R.style.Theme_Light);
		} else if (theme.equals("holo_no_ab")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar);
		} else if (theme.equals("holo_wp")) {
			setTheme(android.R.style.Theme_Holo_Wallpaper);
		} else if (theme.equals("holo_fs")) {
			setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		} else if (theme.equals("holo_light_dark_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
		} else if (theme.equals("holo_light_no_ab")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		} else if (theme.equals("holo_light_fs")) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		}
		setContentView(R.layout.anthrax_tweaks);

		Button mpdecision = (Button) this.findViewById(R.id.button1);
		mpdecision.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(anthraxTweaks.this,
						mpdecision.class);
				anthraxTweaks.this.startActivity(myIntent);

			}
		});

		Button thermald = (Button) findViewById(R.id.button2);
		thermald.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent myIntent = new Intent(anthraxTweaks.this,
						thermald.class);
				anthraxTweaks.this.startActivity(myIntent);
			}

		});

		Button other = (Button) findViewById(R.id.button3);
		other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						anthraxTweaks.this);

				builder.setTitle("Other");

				builder.setMessage("Coming soon");

				builder.setIcon(R.drawable.icon);

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				AlertDialog alert = builder.create();

				alert.show();
			}

		});

	}
}
