package rs.pedjaapps.KernelTuner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VerticalSeekBar;

import rs.pedjaapps.KernelTuner.BuildConfig;
import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.model.FrequencyCollection;

/**
 * Created by pedja on 31.5.14..
 */
public class CpuFragment extends Fragment implements SeekBar.OnSeekBarChangeListener
{
    private SeekBar sbCpu0Max, sbCpu0Min, sbCpu1Max, sbCpu1Min, sbCpu2Max, sbCpu2Min, sbCpu3Max, sbCpu3Min;
    private TextView tvCpu0Max, tvCpu0Min, tvCpu1Max, tvCpu3Max, tvCpu3Min, tvCpu2Min, tvCpu2Max, tvCpu1Min;
    private LinearLayout llCpu0, llCpu1, llCpu2, llCpu3;
    private boolean viewsCreated = false;
    private Handler uiHandler;

    public static CpuFragment newInstance()
    {
        CpuFragment cpuFragment = new CpuFragment();
        return cpuFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_fragment_cpu, container, false);
        sbCpu0Max = (SeekBar)view.findViewById(R.id.sbCpu0Max);
        sbCpu0Min = (SeekBar)view.findViewById(R.id.sbCpu0Min);
        sbCpu1Max = (SeekBar)view.findViewById(R.id.sbCpu1Max);
        sbCpu1Min = (SeekBar)view.findViewById(R.id.sbCpu1Min);
        sbCpu2Max = (SeekBar)view.findViewById(R.id.sbCpu2Max);
        sbCpu2Min = (SeekBar)view.findViewById(R.id.sbCpu2Min);
        sbCpu3Max = (SeekBar)view.findViewById(R.id.sbCpu3Max);
        sbCpu3Min = (SeekBar)view.findViewById(R.id.sbCpu3Min);
        sbCpu0Max.setOnSeekBarChangeListener(this);
        sbCpu0Min.setOnSeekBarChangeListener(this);
        sbCpu1Max.setOnSeekBarChangeListener(this);
        sbCpu1Min.setOnSeekBarChangeListener(this);
        sbCpu2Max.setOnSeekBarChangeListener(this);
        sbCpu2Min.setOnSeekBarChangeListener(this);
        sbCpu3Max.setOnSeekBarChangeListener(this);
        sbCpu3Min.setOnSeekBarChangeListener(this);

        tvCpu0Max = (TextView)view.findViewById(R.id.tvCpu0Max);
        tvCpu0Min = (TextView)view.findViewById(R.id.tvCpu0Min);
        tvCpu1Max = (TextView)view.findViewById(R.id.tvCpu1Max);
        tvCpu1Min = (TextView)view.findViewById(R.id.tvCpu1Min);
        tvCpu2Max = (TextView)view.findViewById(R.id.tvCpu2Max);
        tvCpu2Min = (TextView)view.findViewById(R.id.tvCpu2Min);
        tvCpu3Max = (TextView)view.findViewById(R.id.tvCpu3Max);
        tvCpu3Min = (TextView)view.findViewById(R.id.tvCpu3Min);

        llCpu0 = (LinearLayout)view.findViewById(R.id.llCpu0);
        llCpu1 = (LinearLayout)view.findViewById(R.id.llCpu1);
        llCpu2 = (LinearLayout)view.findViewById(R.id.llCpu2);
        llCpu3 = (LinearLayout)view.findViewById(R.id.llCpu3);
        viewsCreated = true;
        uiHandler = new Handler();
        return view;
    }

    public void updateCpu(final int cpuNum, final int cpuMin, final int cpuMax, final boolean online)
    {
        if(!viewsCreated)
        {
            if(BuildConfig.DEBUG) Log.w(Constants.LOG_TAG, "Trying to update views, but views weren't created");
            return;
        }
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                switch (cpuNum)
                {
                    case 0:
                        updateCpu0(cpuMin, cpuMax, online);
                        break;
                    case 2:
                        updateCpu2(cpuMin, cpuMax, online);
                        break;
                    case 1:
                        updateCpu1(cpuMin, cpuMax, online);
                        break;
                    case 3:
                        updateCpu3(cpuMin, cpuMax, online);
                        break;
                }
            }
        });
    }

    private void updateCpu0(int min, int max, boolean online)
    {
        if(!online || max == Constants.CPU_OFFLINE_CODE || min == Constants.CPU_OFFLINE_CODE)
        {
            llCpu0.setVisibility(View.GONE);
            return;
        }
        tvCpu0Min.setText(FrequencyCollection.getHrStringFromFreq(min));
        tvCpu0Max.setText(FrequencyCollection.getHrStringFromFreq(max));
        sbCpu0Max.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu0Min.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu0Max.setProgress(FrequencyCollection.getProgress(max, FrequencyCollection.getInstance().getFrequencies()));
        sbCpu0Min.setProgress(FrequencyCollection.getProgress(min, FrequencyCollection.getInstance().getFrequencies()));
    }

    private void updateCpu1(int min, int max, boolean online)
    {
        if(!online || max == Constants.CPU_OFFLINE_CODE || min == Constants.CPU_OFFLINE_CODE)
        {
            llCpu1.setVisibility(View.GONE);
            return;
        }
        tvCpu1Min.setText(FrequencyCollection.getHrStringFromFreq(min));
        tvCpu1Max.setText(FrequencyCollection.getHrStringFromFreq(max));
        sbCpu1Max.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu1Min.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu1Max.setProgress(FrequencyCollection.getProgress(max, FrequencyCollection.getInstance().getFrequencies()));
        sbCpu1Min.setProgress(FrequencyCollection.getProgress(min, FrequencyCollection.getInstance().getFrequencies()));
    }

    private void updateCpu2(int min, int max, boolean online)
    {
        if(!online || max == Constants.CPU_OFFLINE_CODE || min == Constants.CPU_OFFLINE_CODE)
        {
            llCpu2.setVisibility(View.GONE);
            return;
        }
        tvCpu2Min.setText(FrequencyCollection.getHrStringFromFreq(min));
        tvCpu2Max.setText(FrequencyCollection.getHrStringFromFreq(max));
        sbCpu2Max.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu2Min.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu2Max.setProgress(FrequencyCollection.getProgress(max, FrequencyCollection.getInstance().getFrequencies()));
        sbCpu2Min.setProgress(FrequencyCollection.getProgress(min, FrequencyCollection.getInstance().getFrequencies()));
    }

    private void updateCpu3(int min, int max, boolean online)
    {
        if(!online || max == Constants.CPU_OFFLINE_CODE || min == Constants.CPU_OFFLINE_CODE)
        {
            llCpu3.setVisibility(View.GONE);
            return;
        }
        tvCpu3Min.setText(FrequencyCollection.getHrStringFromFreq(min));
        tvCpu3Max.setText(FrequencyCollection.getHrStringFromFreq(max));
        sbCpu3Max.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu3Min.setMax(FrequencyCollection.getInstance().getFrequencies().size() - 1);
        sbCpu3Max.setProgress(FrequencyCollection.getProgress(max, FrequencyCollection.getInstance().getFrequencies()));
        sbCpu3Min.setProgress(FrequencyCollection.getProgress(min, FrequencyCollection.getInstance().getFrequencies()));
    }

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
        //TODO change frequency
    }
}
