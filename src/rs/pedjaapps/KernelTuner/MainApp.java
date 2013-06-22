package rs.pedjaapps.KernelTuner;

import android.app.Application;

public class MainApp extends Application
{
	private static MainApp app = null;

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

}
