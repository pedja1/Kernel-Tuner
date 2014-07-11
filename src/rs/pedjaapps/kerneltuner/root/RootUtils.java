package rs.pedjaapps.kerneltuner.root;
import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.*;
import com.stericson.RootTools.execution.*;
import java.io.*;
import java.util.concurrent.*;
import rs.pedjaapps.kerneltuner.utility.*;

import rs.pedjaapps.kerneltuner.utility.Executor;

public class RootUtils
{
	private CommandCallback callback;
	private StringBuilder output;
	private boolean commandExecuted = false;

	public RootUtils(CommandCallback callback)
	{
		this.callback = callback;
		reset();
	}
	
	public RootUtils()
	{
		reset();
	}

	public void reset()
	{
		output = new StringBuilder();
	}
	
	public static interface CommandCallback
	{
		public void onComplete(Status status, String output);
	}
	
	public enum Status
	{
		success, no_root, timeout, io_exception
	}
	
	public void exec(CommandCallback commandCallback, String... commands)
	{
		if(commandExecuted) throw new IllegalArgumentException("You can only execute one command with one instance of RootUtils");
		commandExecuted = true;
		callback = commandCallback;
		final CommandCapture command = new CommandCapture(0, commands)
		{
			@Override
			public void output(int id, String line)
			{
				output.append(line).append("\n");
			}
			
			@Override
			public void commandCompleted(int id, int exitCode)
			{
				if(callback != null)callback.onComplete(Status.success, output.toString());
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
					if(callback != null)callback.onComplete(Status.timeout, output.toString());
					reset();
				}
				catch (IOException e)
				{
					if(callback != null)callback.onComplete(Status.io_exception, output.toString());
					reset();
				}
				catch (RootDeniedException e)
				{
					if(callback != null)callback.onComplete(Status.no_root, output.toString());
					reset();
				}
			}
		};
		Executor.getInstance().executeSingleTask(runnable);
	}
	
	public void exec(String... commands)
	{
		exec(null, commands);
	}
}
