package rs.pedjaapps.KernelTuner.ui;

import rs.pedjaapps.KernelTuner.tools.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class PreferencesFragmentActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = preferences.getString("theme", "light");
		setTheme(Tools.getPreferedTheme(theme));
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PreferencesFragment())
				.commit();
	}

}
