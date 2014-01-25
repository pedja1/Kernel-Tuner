package rs.pedjaapps.KernelTuner.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.model.Frequency;
import rs.pedjaapps.KernelTuner.ui.CPUActivity;
import rs.pedjaapps.KernelTuner.utility.FSHelper;
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
    static LinearLayout cpuInfo, llScrollContainer;
    static TextView tvLoadPerc;
    static ProgressBar loadProgress;
    CpuData cpuData = null;
    View root;
    LayoutInflater inflater;

    public static final int HANDLER_UPDATE_CPU = 101;
    public static final int HANDLER_UPDATE_CPU_LOAD = 102;
    public static final String BUNDLE_KEY_CPU_MAX = "cpu_max";
    public static final String BUNDLE_KEY_CPU_PROGRESS = "cpu_progress";
    public static final String BUNDLE_KEY_CPU_FREQ = "cpu_freq";
    public static final String BUNDLE_KEY_CPU_NUM = "cpu_num";
    public static final String BUNDLE_KEY_CPU_LOAD = "cpu_load";

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
        this.inflater= inflater;
        app = MainApp.getInstance();
        root = inflater.inflate(R.layout.fragment_layout_cpu, null);

        llScrollContainer = (LinearLayout)root.findViewById(R.id.llScrollContainer);
        tvLoadPerc = (TextView)root.findViewById(R.id.tvLoadPerc);
        loadProgress = (ProgressBar)root.findViewById(R.id.loadProgress);

        cpuInfo = (LinearLayout)root.findViewById(R.id.cpuInfoLayout);

        for(int i = 0; i < app.getCpuCount(); i++)
        {
            //CPU INFO
            View cpu = inflater.inflate(R.layout.cpu_info_row, null);

            TextView tvCpuText = (TextView)cpu.findViewById(R.id.tvCpuText);
            tvCpuText.setText("CPU" + i);

            TextView tvCpuFreq = (TextView)cpu.findViewById(R.id.tvCpuFreq);
            tvCpuFreq.setTag("tvCpu" + i + "Freq");

            ProgressBar cpu0Progress = (ProgressBar)cpu.findViewById(R.id.cpuProgress);
            cpu0Progress.setTag("cpu" + i + "Progress");

            cpuInfo.addView(cpu);
        }

        new ATLoadCpuData().execute();

        return root;
    }

    private void setupCpuControls()
    {
        for(int i = 0; i < app.getCpuCount(); i++)
        {
            //CPU CONTROL
            View cpuCtrl = inflater.inflate(R.layout.cpu_ctrl_row, null);
            TextView tvCpuText = (TextView)cpuCtrl.findViewById(R.id.tvCpuText);
            tvCpuText.setText("CPU" + i);

            TextView tvCpuMinFreq = (TextView) cpuCtrl.findViewById(R.id.tvCpuMinFreq);
            TextView tvCpuMaxFreq = (TextView) cpuCtrl.findViewById(R.id.tvCpuMaxFreq);
            SeekBar sbCpuMaxFreq = (SeekBar)cpuCtrl.findViewById(R.id.cpuMaxSeekbar);
            SeekBar sbCpuMinFreq = (SeekBar)cpuCtrl.findViewById(R.id.cpuMinSeekbar);

            tvCpuMinFreq.setText(cpuData.getCpuMinFreq(i).getFrequencyString());
            tvCpuMaxFreq.setText(cpuData.getCpuMaxFreq(i).getFrequencyString());

            int maxSpinnerProgress = app.getCpuFreqs().size() - 1;
            int maxSelected = Frequency.getIndex(cpuData.getCpuMaxFreq(i).getFrequencyValue(), MainApp.getInstance().getCpuFreqs());
            int minSelected = Frequency.getIndex(cpuData.getCpuMinFreq(i).getFrequencyValue(), MainApp.getInstance().getCpuFreqs());

            sbCpuMaxFreq.setMax(maxSpinnerProgress);
            sbCpuMinFreq.setMax(maxSpinnerProgress);

            sbCpuMaxFreq.setProgress(maxSelected);
            sbCpuMinFreq.setProgress(minSelected);

            sbCpuMaxFreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {

                }
            });

            llScrollContainer.addView(cpuCtrl);
        }
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
        scheduler.addScheduleCpuLoad(cpuRefreshInterval, cpuHandler);
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
                    updateCpu(msg.getData());
                    break;
                case HANDLER_UPDATE_CPU_LOAD:
                    updateCpuLoad(msg.getData());
                    break;
            }
        }
    }

    private static void updateCpu(Bundle data)
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

    private static void updateCpuLoad(Bundle data)
    {
        int load = data.getInt(BUNDLE_KEY_CPU_LOAD);
        loadProgress.setMax(1000);
        loadProgress.setProgress(load);
        double text = ((double)load / 10);
        tvLoadPerc.setText(text + "%");
    }

    class ATLoadCpuData extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            cpuData = new CpuData(app.getCpuCount());
            for(int i = 0; i < app.getCpuCount(); i++)
            {
                cpuData.setCpuMaxFreq(i, FSHelper.readFrequency(Constants.PATH_CPU_PRE + i + Constants.PATH_CPU_MAX_FREQ));
                cpuData.setCpuMinFreq(i, FSHelper.readFrequency(Constants.PATH_CPU_PRE + i + Constants.PATH_CPU_MIN_FREQ));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            setupCpuControls();
        }
    }

    class CpuData
    {
        Frequency[] cpuMaxFreq;
        Frequency[] cpuMinFreq;

        CpuData(int cpuCount)
        {
            cpuMaxFreq = new Frequency[cpuCount];
            cpuMinFreq = new Frequency[cpuCount];
        }

        void setCpuMaxFreq(int cpu, Frequency freq)
        {
            cpuMaxFreq[cpu] = freq;
        }

        void setCpuMinFreq(int cpu, Frequency freq)
        {
            cpuMinFreq[cpu] = freq;
        }

        Frequency getCpuMaxFreq(int cpu)
        {
            return cpuMaxFreq[cpu];
        }

        Frequency getCpuMinFreq(int cpu)
        {
            return cpuMinFreq[cpu];
        }
    }

}
