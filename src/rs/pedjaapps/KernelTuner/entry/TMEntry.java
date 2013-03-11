package rs.pedjaapps.KernelTuner.entry;

import android.graphics.drawable.Drawable;

public class TMEntry
{

    int pid;
	String name;
	Drawable icon;
	int rss;
	int type;

	public TMEntry(String name, int pid, Drawable icon, int rss, int type)
	{
		this.name = name;
		this.pid = pid;
		this.icon = icon;
		this.rss = rss;
		this.type = type;
	}

	public int getPid()
	{
		return pid;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getRss()
	{
		return rss;
	}
	
	public Drawable getIcon()
	{
		return icon;
	}

	public String getName()
	{
		return name;
	}

}
