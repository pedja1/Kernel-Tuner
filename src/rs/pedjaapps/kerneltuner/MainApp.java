package rs.pedjaapps.kerneltuner;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;

import java.util.HashMap;
import com.google.android.gms.analytics.*;

public class MainApp extends Application
{
	private static MainApp app = null;
    private static Context context;
    private DisplayImageOptions defaultDisplayImageOptions;

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
        //Crashlytics.start(this);
        app = this;
        defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.showImageOnLoading(R.drawable.ic_action_app)
                //.showImageOnFail(R.drawable.ic_action_app)
                //.showImageForEmptyUri(R.drawable.ic_action_app)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSizePercentage(13) // default
                .writeDebugLogs()
                //.imageDownloader(new SqliteImageLoader(this))
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);
		System.out.println("App created");
	}

	public static MainApp getInstance()
	{
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
			analytics.setDryRun(false);
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
			analytics.setLocalDispatchPeriod(30);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public DisplayImageOptions getDefaultDisplayImageOptions()
    {
        return defaultDisplayImageOptions;
    }
}
