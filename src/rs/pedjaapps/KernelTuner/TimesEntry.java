package rs.pedjaapps.KernelTuner;

public final class TimesEntry
{

	private final int freq;
	private final long time;

	public TimesEntry(final int freq, final long time)
	{
		this.freq = freq;
		this.time = time;
		

	}


	public int getFreq()
	{
		return freq;
	}


	public long getTime()
	{
		return time;
	}

	

}