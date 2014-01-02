package rs.pedjaapps.KernelTuner.utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.fragments.CpuFragment;


public class Scheduler
{
    // the number of tasks that can run simultaneously
    int corePoolSize = 4;
    ScheduledThreadPoolExecutor executor;

    public Scheduler()
    {
        executor = new ScheduledThreadPoolExecutor(corePoolSize);
    }
	
	public Scheduler(int corePoolSize)
    {
		this.corePoolSize = corePoolSize;
        executor = new ScheduledThreadPoolExecutor(corePoolSize);
    }
	
    public boolean addScheduleCpu(final int cpu, final long delay, final Handler handler)
    {
		int coreCount = MainApp.getInstance().getCpuCount();
		if(cpu >= coreCount)
		{
		     Log.e(Constants.LOG_TAG, "Cpu doesn't exist. Number of cpu's: [" + coreCount + "]"
			                                         + " Requested cpu: [" + cpu + "]");
			return false;
		}

        Runnable scheduledTask = new Runnable()
        {
            public void run()
            {
				int max = Integer.parseInt(FSHelper.readCpu(Constants.PATH_CPU_PRE + cpu + Constants.PATH_CPU_MAX_FREQ).trim());
				int cur = Integer.parseInt(FSHelper.readCpu(Constants.PATH_CPU_PRE + cpu + Constants.PATH_CPU_CURR_FREQ).trim());

                int maxProgress = MainApp.getInstance().getCpuFreqs().indexOf(max);
                int progress = MainApp.getInstance().getCpuFreqs().indexOf(cur);
                String freq = (cur/1000) + "Mhz";


				Bundle data = new Bundle();
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_MAX, maxProgress);
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_PROGRESS, progress);
                data.putString(CpuFragment.BUNDLE_KEY_CPU_FREQ, freq);
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_NUM, cpu);

				Message message = Message.obtain(handler, CpuFragment.HANDLER_UPDATE_CPU);
				message.setData(data);
				
				handler.sendMessage(message);
                addScheduleCpu(cpu, delay, handler);
            }
        };

        executor.schedule(scheduledTask, delay, TimeUnit.MILLISECONDS);
		return true;
    }
	
	/*public boolean addScheduleTimes(final long delay, final Handler handler)
    {
		final int coreCount = MainApp.getInstance().getCpuCount();

        Runnable scheduledTask = new Runnable()
        {
            public void run()
            {
				List<TimesInState> times = new ArrayList<TimesInState>();
				//String[] timesFiles = new String[coreCount];
				String[] freqs = null;
				long[] time = null;
				
				for(int i = 0; i < coreCount; i++)
				{
					String file;
					try
					{
						file = FileUtils.readFileToString(new File(Constants.PATH_CPU_PRE + i + Constants.PATH_CPU_TIMES));
					}
					catch (IOException e)
					{
						file = null;
						e.printStackTrace();
						Log.e(Constants.LOG_TAG, "Scheduler : addSchedulerTimes : " + e.getMessage());
					}
					if(i == 0)
					{
						if(file != null)
						{
							String[] tmp = file.split("\n");
							freqs = new String[tmp.length];
							time = new long[tmp.length];
							for(int a = 0; i < tmp.length; a++)
							{
								String[] tmp2 = tmp[a].split(" ");
								freqs[a] = tmp2[0];
								time[a] = Integer.parseInt(tmp2[1].trim());
							}
						}
					}
					else
					{
						String[] tmp = file.split("\n");
						for(int a = 0; i < tmp.length; a++)
						{
							String[] tmp2 = tmp[a].split(" ");
							if(time != null)time[a] = time[a] + Integer.parseInt(tmp2[1].trim());
						}
					}
					if(freqs != null && time != null && time.length == freqs.length)
				    for(int s = 0; s < freqs.length; s++)
					{
						
					}
				}

				Bundle data = new Bundle();
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_MAX, maxProgress);
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_PROGRESS, progress);
                data.putString(CpuFragment.BUNDLE_KEY_CPU_FREQ, freq);
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_NUM, cpu);

				Message message = Message.obtain(handler, CpuFragment.HANDLER_UPDATE_CPU);
				message.setData(data);
				
				handler.sendMessage(message);
                addScheduleTimes(delay, handler);
            }
        };

        executor.schedule(scheduledTask, delay, TimeUnit.SECONDS);
		return true;
    }*/
    
    public void shutdownNow()
    {
        if (executor != null)
        {
            executor.shutdownNow();
            executor.shutdown();
        }
    }

    /**
     * Do Not submit new tasks after calling this method
     * */
    public void shutdownAfterCompleted()
    {
        if (executor != null)
        {
            executor.shutdown();
        }
    }
}
