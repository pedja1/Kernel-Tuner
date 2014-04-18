package rs.pedjaapps.KernelTuner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MainApp extends Application
{
	private static MainApp app = null;
    private static Context context;

	@Override
	public void onCreate()
	{
		super.onCreate();
        context = getApplicationContext();
        app = this;
	}

	public static synchronized MainApp getInstance()
	{
		if(app == null)
        {
			app = new MainApp();
		}
		return app;
	}

    public static Context getContext()
    {
        return context;
    }
}
