package rs.pedjaapps.KernelTuner.entry;

public final class VoltageEntry
{


	private final int voltage;
	private final String freq;



	public VoltageEntry(final String freq, final int voltage)
	{
		this.voltage = voltage;
		this.freq = freq;

	}


	public int getVoltage()
	{
		return voltage;
	}

	public String getFreq()
	{
		return freq;
	}

}
