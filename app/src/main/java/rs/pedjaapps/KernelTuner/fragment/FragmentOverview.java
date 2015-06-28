package rs.pedjaapps.KernelTuner.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedja1.fontwidget.widget.FTextView;

import rs.pedjaapps.KernelTuner.App;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.model.CpuFreq;
import rs.pedjaapps.KernelTuner.model.GpuFreq;
import rs.pedjaapps.KernelTuner.utility.AppColor;
import rs.pedjaapps.KernelTuner.utility.IOHelper;
import rs.pedjaapps.KernelTuner.utility.Utility;

/**
 * Created by pedja on 20.6.15..
 */
public class FragmentOverview extends AbsFragment
{

    private CpuGpuMonitor cpuGpuMonitor;
    private MemoryMonitor memoryMonitor;
    private BatteryMonitor batteryMonitor;

    public static FragmentOverview newInstance()
    {
        FragmentOverview fragment = new FragmentOverview();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_overview, container, false);

        cpuGpuMonitor = new CpuGpuMonitor(view);
        memoryMonitor = new MemoryMonitor(view);
        batteryMonitor = new BatteryMonitor(view);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(cpuGpuMonitor != null)
            cpuGpuMonitor.start();
        if(memoryMonitor != null)
            memoryMonitor.start();
        if(batteryMonitor != null)
            batteryMonitor.start();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(cpuGpuMonitor != null)
            cpuGpuMonitor.stop();
        if(memoryMonitor != null)
            memoryMonitor.stop();
        if(batteryMonitor != null)
            batteryMonitor.stop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        cpuGpuMonitor = null;
        memoryMonitor = null;
        batteryMonitor = null;
    }

    @Override
    public int getTitle()
    {
        return R.string.overview;
    }

    private static class CpuGpuMonitor implements Runnable
    {
        private static final long REFRESH_INTERVAL = 1000;
        FTextView tvCpuTemp, tvCpu0, tvCpu1, tvCpu2, tvCpu3, tvGpuF, tvGpuMax;
        private boolean running = false;
        private HandlerThread worker;
        private Handler workerHandler;
        private Handler uiHandler;

        public CpuGpuMonitor(View view)
        {
            tvCpuTemp = (FTextView)view.findViewById(R.id.tvCpuTemp);

            tvCpu0 = (FTextView)view.findViewById(R.id.tvCpu0);
            tvCpu1 = (FTextView)view.findViewById(R.id.tvCpu1);
            tvCpu2 = (FTextView)view.findViewById(R.id.tvCpu2);
            tvCpu3 = (FTextView)view.findViewById(R.id.tvCpu3);
            if(!IOHelper.cpu0Exists())tvCpu0.setVisibility(View.GONE);
            if(!IOHelper.cpu1Exists())tvCpu1.setVisibility(View.GONE);
            if(!IOHelper.cpu2Exists())tvCpu2.setVisibility(View.GONE);
            if(!IOHelper.cpu3Exists())tvCpu3.setVisibility(View.GONE);
            if(!IOHelper.cpu0Exists() && !IOHelper.cpu1Exists() && !IOHelper.cpu2Exists() && !IOHelper.cpu3Exists())
            {
                view.findViewById(R.id.llCpu).setVisibility(View.GONE);
            }

            tvGpuF = (FTextView)view.findViewById(R.id.tvGpuF);
            tvGpuMax = (FTextView)view.findViewById(R.id.tvGpuMax);
            if(!IOHelper.gpu3DMaxExists())tvGpuMax.setVisibility(View.GONE);
            if(!IOHelper.gpu3DCurExists())tvGpuF.setVisibility(View.GONE);
            if(!IOHelper.gpu3DMaxExists() && !IOHelper.gpu3DCurExists())
            {
                view.findViewById(R.id.llGpu).setVisibility(View.GONE);
            }

            uiHandler = new Handler(Looper.getMainLooper());
            worker = new HandlerThread("overview-monitor");
            worker.start();
            workerHandler = new Handler(worker.getLooper());
        }

        @Override
        public void run()
        {
            if(!running)return;

            final CpuFreq cpu0 = IOHelper.cpuCurFreq(0);
            final CpuFreq cpu1 = IOHelper.cpuCurFreq(1);
            final CpuFreq cpu2 = IOHelper.cpuCurFreq(2);
            final CpuFreq cpu3 = IOHelper.cpuCurFreq(3);
            final GpuFreq gpuFreq = IOHelper.gpu3DCurFreq();
            final GpuFreq gpuMaxFreq = IOHelper.gpu3DMaxFreq();

            uiHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    tvCpu0.setText(cpu0 != null ? cpu0.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvCpu1.setText(cpu1 != null ? cpu1.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvCpu2.setText(cpu2 != null ? cpu2.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvCpu3.setText(cpu3 != null ? cpu3.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvCpu0.setTextColor(cpu0 != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);
                    tvCpu1.setTextColor(cpu1 != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);
                    tvCpu2.setTextColor(cpu2 != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);
                    tvCpu3.setTextColor(cpu3 != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);

                    tvGpuF.setText(gpuFreq != null ? gpuFreq.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvGpuMax.setText(gpuMaxFreq != null ? gpuMaxFreq.formatted : tvCpu0.getContext().getString(R.string.offline));
                    tvGpuF.setTextColor(gpuFreq != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);
                    tvGpuMax.setTextColor(gpuMaxFreq != null ? AppColor.PRIMARY_TEXT : AppColor.SECONDARY_TEXT);
                }
            });
            workerHandler.postDelayed(this, REFRESH_INTERVAL);
        }

        private void start()
        {
            if(running)return;
            running = true;
            workerHandler.postDelayed(this, REFRESH_INTERVAL);
        }

        private void stop()
        {
            running = false;
            workerHandler.removeCallbacks(this);
        }
    }

    private static class MemoryMonitor implements Runnable
    {
        private static final long REFRESH_INTERVAL = 60000;
        FTextView tvMemFree, tvMemMax;
        private boolean running = false;
        private HandlerThread worker;
        private Handler workerHandler;
        private Handler uiHandler;

        public MemoryMonitor(View view)
        {
            tvMemFree = (FTextView)view.findViewById(R.id.tvMemFree);
            tvMemMax = (FTextView)view.findViewById(R.id.tvMemMax);

            uiHandler = new Handler(Looper.getMainLooper());
            worker = new HandlerThread("overview-mem-monitor");
            worker.start();
            workerHandler = new Handler(worker.getLooper());
        }

        @Override
        public void run()
        {
            if(!running)return;

            final int freeRam = Utility.getFreeRAM();
            final int totalRam = Utility.getTotalRAM();

            uiHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    tvMemFree.setText(tvMemFree.getContext().getString(R.string.free_mb, freeRam));
                    tvMemMax.setText(tvMemFree.getContext().getString(R.string.max_mb, totalRam));
                }
            });
            workerHandler.postDelayed(this, REFRESH_INTERVAL);
        }

        private void start()
        {
            if(running)return;
            running = true;
            workerHandler.postDelayed(this, REFRESH_INTERVAL);
        }

        private void stop()
        {
            running = false;
            workerHandler.removeCallbacks(this);
        }
    }

    private static class BatteryMonitor extends BroadcastReceiver
    {
        FTextView  tvBattLevel, tvBattTemp;
        private Context context;

        public BatteryMonitor(View view)
        {
            tvBattLevel = (FTextView)view.findViewById(R.id.tvBattLevel);
            tvBattTemp = (FTextView)view.findViewById(R.id.tvBattTemp);
            context = App.get();
        }


        private void start()
        {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(this, ifilter);
            setFromIntent(batteryStatus);
        }

        private void stop()
        {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            setFromIntent(intent);
        }

        private void setFromIntent(Intent intent)
        {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            float temp = (float)intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10f;
            if(level >= 0)//can temperature be less than 0 degrees?
            {
                int color = battLevelColorFromLevel(level);
                tvBattLevel.setText(Html.fromHtml(context.getString(R.string.batt_level, (0xFFFFFF & color), level)));
            }

            if(temp > 0)
            {
                int color = battTempColorFromtemp(temp);
                tvBattTemp.setText(Html.fromHtml(context.getString(R.string.batt_temp, (0xFFFFFF & color), temp)));
            }
        }

        private int battLevelColorFromLevel(int level)
        {
            if(level == 100)
            {
                return AppColor.MATERIAL_BLUE;
            }
            else if(level <= 30)
            {
                return AppColor.MATERIAL_ORANGE;
            }
            else if(level <= 15)
            {
                return AppColor.MATERIAL_RED;
            }
            else //between 30 and 100 exclusive
            {
                return AppColor.MATERIAL_GREEN;
            }
        }

        private int battTempColorFromtemp(float temp)
        {
            if(temp >= 70)
            {
                return AppColor.MATERIAL_RED;
            }
            else if(temp >= 50)
            {
                return AppColor.MATERIAL_ORANGE;
            }
            else if(temp >= 30)
            {
                return AppColor.MATERIAL_GREEN;
            }
            else
            {
                return AppColor.MATERIAL_BLUE;
            }
        }
    }


}
