package rs.pedjaapps.KernelTuner;

import android.graphics.drawable.Drawable;

public final class TaskManagerEntry
{


	private final String name;
	private final String pid;
	private final int status;
	private final Drawable icon;
	private final String memory;
	private final String cpu;
	//private final 



	public TaskManagerEntry(final String name, final String pid, final int status, final Drawable icon, final String memory, final String cpu)
	{
		this.name = name;
		this.pid = pid;
		this.status = status;
		this.icon = icon;
		this.memory = memory;
		this.cpu = cpu;
	}


	public String getName()
	{
		return name;
	}

	public String getPid()
	{
		return pid;
	}
	
	public int getStatus(){
		return status;
	}
	public Drawable getIcon(){
		return icon;
	}
	
	public String getMemory()
	{
		return memory;
	}

	public String getCpu()
	{
		return cpu;
	}

}
