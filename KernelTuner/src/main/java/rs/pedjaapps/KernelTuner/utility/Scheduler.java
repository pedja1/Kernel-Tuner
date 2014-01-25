package rs.pedjaapps.KernelTuner.utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import java.io.IOException;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.fragments.CpuFragment;
import rs.pedjaapps.KernelTuner.model.Frequency;


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

                int maxProgress = Frequency.getIndex(max, MainApp.getInstance().getCpuFreqs());
                int progress = Frequency.getIndex(cur, MainApp.getInstance().getCpuFreqs());
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

    public boolean addScheduleCpuLoad(final long delay, final Handler handler)
    {
        Runnable scheduledTask = new Runnable()
        {
            public void run()
            {
                float fLoad = 0;
                try {
                    RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
                    String load = reader.readLine();

                    String[] toks = load.split(" ");

                    long idle1 = Long.parseLong(toks[5]);
                    long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

                    try
                    {
                        Thread.sleep(360);
                    }
                    catch (Exception e)
                    {

                    }

                    reader.seek(0);
                    load = reader.readLine();
                    reader.close();

                    toks = load.split(" ");

                    long idle2 = Long.parseLong(toks[5]);
                    long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

                    fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                int load = (int)((fLoad * 100) * 10);

                Bundle data = new Bundle();
                data.putInt(CpuFragment.BUNDLE_KEY_CPU_LOAD, load);

                Message message = Message.obtain(handler, CpuFragment.HANDLER_UPDATE_CPU_LOAD);
                message.setData(data);

                handler.sendMessage(message);
                addScheduleCpuLoad(delay, handler);
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
