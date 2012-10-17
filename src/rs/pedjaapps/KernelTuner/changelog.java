package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import rs.pedjaapps.KernelTuner.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class changelog extends Activity {
	
	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.changelog);

		TextView changelog = (TextView) findViewById(R.id.textView12);
		
			changelog.setMovementMethod(LinkMovementMethod.getInstance());
		

	}


}
