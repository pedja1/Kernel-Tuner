package rs.pedjaapps.KernelTuner.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rs.pedjaapps.KernelTuner.BuildConfig;


public class Executor
{
    private final ExecutorService executorService;
	private static Executor executor = null;
    private static List<Future<Runnable>> tasks;

    private Executor()
    {
        executorService = Executors.newCachedThreadPool();
        tasks = new ArrayList<>();
    }

	public static Executor getInstance()
	{
		if(executor == null)
		{
			executor = new Executor();
		}
		return executor;
	}

    /**
     * Adds task to the que
     * @param runnable - runnable to be executed*/
    public Executor addTask(Runnable runnable)
    {
        Future f = executorService.submit(runnable);
        tasks.add(f);
        return executor;
    }

    /**
     * execute single runnable
     * @param runnable - runnable to be executed*/
    public void executeSingleTask(Runnable runnable)
    {
		executorService.execute(runnable);
    }

    /**
     * Executes all tasks currently in que and waits for all of them to finish before returning
     * */
    public void executeAll()
    {
        for (Future<Runnable> f : tasks)
        {
            try
            {
                f.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                if(BuildConfig.DEBUG)e.printStackTrace();
                //Crashlytics.logException(e);
            }
        }
    }


    public void shutdown()
    {
        executorService.shutdownNow();
    }
}
