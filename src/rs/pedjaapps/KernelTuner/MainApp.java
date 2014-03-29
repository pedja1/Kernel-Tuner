package rs.pedjaapps.KernelTuner;

import android.app.Application;
import android.content.SharedPreferences;

public class MainApp extends Application
{
	private static MainApp app = null;
	SharedPreferences prefs;

	@Override
	public void onCreate()
	{
		super.onCreate();
		app = this;
	}

	public static synchronized MainApp getInstance()
	{
		if(app == null){
			app = new MainApp();
		}
		return app;
	}

	public SharedPreferences getPrefs()
	{
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs)
	{
		this.prefs = prefs;
	}

}
