package rs.pedjaapps.KernelTuner.entry;

public class Frequency
{
	private String frequencyString;
	private int frequencyValue;

	public Frequency(String frequencyString, int frequencyValue)
	{
		this.frequencyString = frequencyString;
		this.frequencyValue = frequencyValue;
	}

	public String getFrequencyString()
	{
		return frequencyString;
	}

	public void setFrequencyString(String frequencyString)
	{
		this.frequencyString = frequencyString;
	}

	public int getFrequencyValue()
	{
		return frequencyValue;
	}

	public void setFrequencyValue(int frequencyValue)
	{
		this.frequencyValue = frequencyValue;
	}
}
