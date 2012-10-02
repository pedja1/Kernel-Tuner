package rs.pedjaapps.KernelTuner;
 
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
 
public class notFoundActivity extends Activity {
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
		setContentView(R.layout.not_supported);
		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText("Problem processing time-in-states file");
		
	}
 
}
