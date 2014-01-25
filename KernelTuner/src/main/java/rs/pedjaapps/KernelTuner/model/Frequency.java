package rs.pedjaapps.KernelTuner.model;

import java.util.List;

import rs.pedjaapps.KernelTuner.MainApp;

public class Frequency
{
	private String frequencyString;
	private int frequencyValue;

	public Frequency(String frequencyString, int frequencyValue)
	{
		this.frequencyString = frequencyString;
		this.frequencyValue = frequencyValue;
	}

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

    public static int getIndex(int freq, List<Frequency> freqs)
    {
        for(int i = 0; i < freqs.size(); i++)
        {
            if(freq == freqs.get(i).getFrequencyValue())
            {
                return i;
            }
        }
        return -1;
    }
}
