package rs.pedjaapps.kerneltuner.root;

import android.os.*;
import android.util.*;
import com.crashlytics.android.*;
import com.stericson.RootShell.*;
import com.stericson.RootShell.exceptions.*;
import com.stericson.RootShell.execution.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.utility.*;

import rs.pedjaapps.kerneltuner.utility.Executor;

public class RootUtils
{
    private StringBuilder output;
    private boolean commandExecuted = false;
    private Handler handler;

    public RootUtils()
    {
        reset();
        handler = new Handler(Looper.getMainLooper());
    }

    public void reset()
    {
        output = new StringBuilder();
    }

    public static class CommandCallbackImpl implements CommandCallback
    {
        public void onComplete(Status status, String output)
        {
        }

        public void out(String line)
        {
        }
    }

    public interface CommandCallback
    {
        void onComplete(Status status, String output);

        void out(String line);
    }

    public enum Status
    {
        success, no_root, timeout, io_exception, terminated, unknown_error
    }

    public void exec(final CommandCallback callback, String... commands)
    {
        if (commandExecuted)
            throw new IllegalArgumentException("You can only execute one command with one instance of RootUtils");
        commandExecuted = true;
        final Command command = new Command(0, commands)
        {
            @Override
            public void commandOutput(int id, String line)
            {
                output.append(line).append("\n");
                if (callback != null) callback.out(line);
                super.commandOutput(id, line);
            }

            @Override
            public void commandCompleted(int id, int exitCode)
            {
                if (callback != null) callback.onComplete(Status.success, output.toString());
                reset();
            }

            @Override
            public void commandTerminated(int id, String reason)
            {
                if (callback != null) callback.onComplete(Status.terminated, output.toString());
                reset();
            }
        };
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    RootShell.getShell(true).add(command);
                }
                catch (TimeoutException e)
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (callback != null)
                                callback.onComplete(Status.timeout, output.toString());
                            reset();
                        }
                    });
                }
                catch (IOException e)
                {
                    handler.post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            if (callback != null)
                                callback.onComplete(Status.io_exception, output.toString());
                            reset();
                        }
                    });
                }
                catch (RootDeniedException e)
                {
                    handler.post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            if (callback != null)
                                callback.onComplete(Status.no_root, output.toString());
                            reset();
                        }
                    });
                }
                catch (Exception e)
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (callback != null)
                                callback.onComplete(Status.unknown_error, output.toString());
                            reset();
                        }
                    });
                }
            }
        };
        Executor.getInstance().executeSingleTask(runnable);
    }

    public void exec(String... commands)
    {
        exec(null, commands);
    }

    public String execAndWait(final String... commands) throws IOException
    {
        if (commandExecuted)
            throw new IllegalArgumentException("You can only execute one command with one instance of RootUtils");
        commandExecuted = true;
        final Command command = new Command(0, commands)
        {
            @Override
            public void commandOutput(int id, String line)
            {
                output.append(line).append("\n");
                super.commandOutput(id, line);
				Log.d(Constants.LOG_TAG, "execAndWait " + Arrays.toString(commands));
            }
        };
        try
        {
            RootShell.getShell(true).add(command);
            commandWait(command);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IOException(e);
        }
        if(command.getExitCode() != 0) throw new IOException("Command failed");
        return output.toString();
    }

    private void commandWait(Command cmd) throws Exception
    {
		if(Looper.myLooper() == Looper.getMainLooper())System.out.println("Command wait start");
        int waitTill = 50;
        int waitTillMultiplier = 2;
        int waitTillLimit = 3200; //7 tries, 6350 msec

		long start = System.currentTimeMillis();
		
        while (!cmd.isFinished() && waitTill <= waitTillLimit)
        {
			//todo this sync is broken...
            synchronized (cmd)
            {
                try
                {
                    if (!cmd.isFinished())
                    {
                        cmd.wait(waitTill);
                        waitTill *= waitTillMultiplier;
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (!cmd.isFinished())
        {
            Log.e(Constants.LOG_TAG, "Could not finish root command in " + (waitTill / waitTillMultiplier));
        }
		if(Looper.getMainLooper() == Looper.myLooper())System.out.println("command wait finished in " + (System.currentTimeMillis() - start));
    }

    public static void closeAllShells()
    {
		Executor.getInstance().executeSingleTask(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					RootShell.closeAllShells();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					Crashlytics.logException(e);
				}
			}
		});
    }
}
