package rs.pedjaapps.kerneltuner.root;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.MainApp;
import rs.pedjaapps.kerneltuner.ui.MainActivity;
import rs.pedjaapps.kerneltuner.utility.Executor;

public class RootUtils
{
    private StringBuilder output;
    private boolean commandExecuted = false;
    private Handler handler;

    public RootUtils()
    {
        reset();
        handler = new Handler(MainApp.getInstance().getMainLooper());
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

    public static interface CommandCallback
    {
        public void onComplete(Status status, String output);

        public void out(String line);
    }

    public enum Status
    {
        success, no_root, timeout, io_exception
    }

    public void exec(final CommandCallback callback, String... commands)
    {
        if (commandExecuted)
            throw new IllegalArgumentException("You can only execute one command with one instance of RootUtils");
        commandExecuted = true;
        final CommandCapture command = new CommandCapture(0, commands)
        {
            @Override
            public void output(int id, String line)
            {
                output.append(line).append("\n");
                if (callback != null) callback.out(line);
            }

            @Override
            public void commandCompleted(int id, int exitCode)
            {
                if (callback != null) callback.onComplete(Status.success, output.toString());
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
                    RootTools.getShell(true).add(command);
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
            }
        };
        Executor.getInstance().executeSingleTask(runnable);
    }

    public void exec(String... commands)
    {
        exec(null, commands);
    }

    public String execAndWait(String... commands) throws IOException
    {
        if (commandExecuted)
            throw new IllegalArgumentException("You can only execute one command with one instance of RootUtils");
        commandExecuted = true;
        final CommandCapture command = new CommandCapture(0, commands)
        {
            @Override
            public void output(int id, String line)
            {
                output.append(line).append("\n");
            }
        };
        try
        {
            RootTools.getShell(true).add(command);
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
        int waitTill = 50;
        int waitTillMultiplier = 2;
        int waitTillLimit = 3200; //7 tries, 6350 msec

        while (!cmd.isFinished() && waitTill <= waitTillLimit)
        {
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
    }

    public void closeAllShells()
    {
        try
        {
            RootTools.closeAllShells();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
