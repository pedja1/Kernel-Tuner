package rs.pedjaapps.DualCore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class anthraxTweaks extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anthrax_tweaks);

		Button mpdecision = (Button) this.findViewById(R.id.button1);
		mpdecision.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent myIntent = new Intent(anthraxTweaks.this,
						mpdecision.class);
				anthraxTweaks.this.startActivity(myIntent);

			}
		});

		Button thermald = (Button) findViewById(R.id.button2);
		thermald.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						anthraxTweaks.this);

				builder.setTitle("thermald");

				builder.setMessage("Coming soon");

				builder.setIcon(R.drawable.icon);

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				AlertDialog alert = builder.create();

				alert.show();
			}

		});

		Button other = (Button) findViewById(R.id.button3);
		other.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						anthraxTweaks.this);

				builder.setTitle("Other");

				builder.setMessage("Coming soon");

				builder.setIcon(R.drawable.icon);

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
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
