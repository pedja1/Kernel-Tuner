package rs.pedjaapps.kerneltuner.model;

public class Frequency
{
	private String frequencyString;
	private int frequencyValue;

    public Frequency()
    {
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

	@Override
	public String toString()
	{
		return frequencyString;
	}
	
}
