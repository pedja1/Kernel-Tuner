package rs.pedjaapps.KernelTuner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.model.Frequency;
import rs.pedjaapps.KernelTuner.utility.FSHelper;

public class MainApp extends Application
{
	private static MainApp app = null;
	SharedPreferences prefs;

    private static Context context;
    private List<Frequency> cpuFreqs;
    private int cpuCount;

    public MainApp()
    {
        cpuFreqs = FSHelper.getAllCpuFreqs(true);
        cpuCount = FSHelper.getCpuCount(FSHelper.CPU_COUNT_METHOD_DYNAMIC);//TODO should chose method based on prefs
    }

    public void setCpuFreqs(List<Frequency> cpuFreqs)
    {
        this.cpuFreqs = cpuFreqs;
    }

    public List<Frequency> getCpuFreqs()
    {
        return cpuFreqs;
    }


    @Override
	public void onCreate()
	{
		super.onCreate();
		app = this;
        context = getApplicationContext();
        cpuFreqs = new ArrayList<Frequency>();
        Crashlytics.start(this);
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

    public static Context getContext()
    {
        return context;
    }

    public int getCpuCount()
    {
        return cpuCount;
    }

    public void setCpuCount(int cpuCount)
    {
        this.cpuCount = cpuCount;
    }

}
