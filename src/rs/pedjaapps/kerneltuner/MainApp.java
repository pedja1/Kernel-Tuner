package rs.pedjaapps.kerneltuner;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MainApp extends Application
{
	private static MainApp app = null;
    private static Context context;

    public enum TrackerName
    {
        APP_TRACKER, // Tracker used only in this app.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

	@Override
	public void onCreate()
	{
		super.onCreate();
        context = getApplicationContext();
        Crashlytics.start(this);
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

    public synchronized Tracker getTracker(TrackerName trackerId)
    {
        if (!mTrackers.containsKey(trackerId))
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
}
