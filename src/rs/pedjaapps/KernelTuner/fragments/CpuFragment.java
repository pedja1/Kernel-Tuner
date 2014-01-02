package rs.pedjaapps.KernelTuner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class CpuFragment extends Fragment
{

    int cpuRefreshInterval = 1000;//ms //TODO should be based on user prefs
    CpuHandler cpuHandler;
    Scheduler scheduler;
    MainApp app;
    static LinearLayout cpuInfo;

    public static final int HANDLER_UPDATE_CPU = 101;
    public static final String BUNDLE_KEY_CPU_MAX = "cpu_max";
    public static final String BUNDLE_KEY_CPU_PROGRESS = "cpu_progress";
    public static final String BUNDLE_KEY_CPU_FREQ = "cpu_freq";
    public static final String BUNDLE_KEY_CPU_NUM = "cpu_num";

    //static TextView tvCpu0Freq, tvCpu1Freq, tvCpu2Freq, tvCpu3Freq;
    //static ProgressBar cpu0Progress, cpu1Progress, cpu2Progress, cpu3Progress;
	
 
	public static CpuFragment newInstance()
	{
        CpuFragment f = new CpuFragment();


        /*Bundle args = new Bundle();
        args.putInt(ARG_LISTENER, htmlFile);
        f.setArguments(args);*/

        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        app = MainApp.getInstance();
        View v = inflater.inflate(R.layout.fragment_layout_cpu, null);


        cpuInfo = (LinearLayout)v.findViewById(R.id.cpuInfoLayout);

        for(int i = 0; i < app.getCpuCount(); i++)
        {
            View cpu = inflater.inflate(R.layout.cpu_info_row, null);

            TextView tvCpuText = (TextView)cpu.findViewById(R.id.tvCpuText);
            tvCpuText.setText("CPU" + i);

            TextView tvCpuFreq = (TextView)cpu.findViewById(R.id.tvCpuFreq);
            tvCpuFreq.setTag("tvCpu" + i + "Freq");

            ProgressBar cpu0Progress = (ProgressBar)cpu.findViewById(R.id.cpuProgress);
            cpu0Progress.setTag("cpu" + i + "Progress");

            cpuInfo.addView(cpu);
        }


        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        cpuHandler = new CpuHandler();
    }

    @Override
    public void onResume()
    {
        scheduler = new Scheduler();
        for(int i = 0 ; i < app.getCpuCount(); i++)
        {
            scheduler.addScheduleCpu(i, cpuRefreshInterval, cpuHandler);
        }
        super.onResume();
    }

    @Override
    public void onPause()
    {
        scheduler.shutdownNow();
        scheduler = null;
        super.onStop();
    }

    private static class CpuHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case HANDLER_UPDATE_CPU:
                    update(msg.getData());
                    break;
            }
        }
    }

    private static void update(Bundle data)
    {
        int cpu = data.getInt(BUNDLE_KEY_CPU_NUM);
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
        if(tv != null)tv.setText(freq);



    }
}
