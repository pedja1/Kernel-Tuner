package rs.pedjaapps.KernelTuner.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.utility.Scheduler;


/**
 * Created by pedja on 9/29/13.
 */
public class TimesInStateFragment extends Fragment
{

    int timesRefreshInterval = 30;//s //TODO should be based on user prefs
    TisHandler tisHandler;
    Scheduler scheduler;
    MainApp app;
    static LinearLayout cpuInfo;

    public static final int HANDLER_UPDATE_TIS = 102;
    public static final String BUNDLE_KEY_TIS_TIMES = "times_list";

    //static TextView tvCpu0Freq, tvCpu1Freq, tvCpu2Freq, tvCpu3Freq;
    //static ProgressBar cpu0Progress, cpu1Progress, cpu2Progress, cpu3Progress;
	
 
	static TimesInStateFragment newInstance(int htmlFile)
	{
        TimesInStateFragment f = new TimesInStateFragment();


        /*Bundle args = new Bundle();
        args.putInt(ARG_LISTENER, htmlFile);
        f.setArguments(args);*/

        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        app = MainApp.getInstance();
        View v = inflater.inflate(R.layout.fragment_layout_times_in_state, null);



        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        tisHandler = new TisHandler();
    }

    @Override
    public void onResume()
    {
        scheduler = new Scheduler();
        //scheduler.addScheduleTimes(timesRefreshInterval, tisHandler);
        
        super.onResume();
    }

    @Override
    public void onPause()
    {
        scheduler.shutdownNow();
        scheduler = null;
        super.onStop();
    }

    private static class TisHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case HANDLER_UPDATE_TIS:
                    update(msg.getData());
                    break;
            }
        }
    }

    private static void update(Bundle data)
    {
        /*int cpu = data.getInt(BUNDLE_KEY_CPU_NUM);
        int max = data.getInt(BUNDLE_KEY_CPU_MAX);
        int progress = data.getInt(BUNDLE_KEY_CPU_PROGRESS);
        String freq = data.getString(BUNDLE_KEY_CPU_FREQ);
        ProgressBar progressBar = (ProgressBar)cpuInfo.findViewWithTag("cpu" + cpu + "Progress");
        if(progressBar != null)
        {
            progressBar.setMax(max);
            progressBar.setProgress(progress);
        }
        TextView tv = (TextView)cpuInfo.findViewWithTag("tvCpu" + cpu + "Freq");
        if(tv != null)tv.setText(freq);*/



    }
}
