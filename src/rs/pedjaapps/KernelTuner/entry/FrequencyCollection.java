package rs.pedjaapps.KernelTuner.entry;

import java.util.List;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;

public class FrequencyCollection
{
	private List<Frequency> frequencies;
	private static FrequencyCollection frequency = null;
	
	public static synchronized FrequencyCollection getInstance(){
		if(frequency == null){
			frequency = new FrequencyCollection();
		}
		return frequency;
	}

	public List<Frequency> getFrequencies()
	{
		return frequencies;
	}

	public void setFrequencies(List<Frequency> frequencies)
	{
		this.frequencies = frequencies;
	}
	
	public boolean getAllFrequencies(){
		frequencies = IOHelper.frequencies();
		if(frequencies.size() == 0)
		{
			return false;
		}
		return true;
	}
	
}
