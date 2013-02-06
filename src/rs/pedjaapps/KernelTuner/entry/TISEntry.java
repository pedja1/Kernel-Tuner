package rs.pedjaapps.KernelTuner.entry;

public final class TISEntry
{

	private final String name;
	private final String time;
	private final String percent;
	private final int progress;



	public TISEntry(final String name, final String time, final String percent, final int progress)
	{
		this.name = name;
		this.time = time;
		this.percent = percent;
		this.progress = progress;
		

	}


	public String getName()
	{
		return name;
	}


	public String getTime()
	{
		return time;
	}

	public String getPercent()
	{
		return percent;
	}

	public int getProgress()
	{
		return progress;
	}

}
