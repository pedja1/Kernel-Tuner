package rs.pedjaapps.KernelTuner.entry;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;

public class FrequencyCollection
{
	private List<Frequency> frequencies;
	private List<String> frequencyStrings;
	private List<String> frequencyValues;
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
	
	public boolean getAllFrequencies(){
		frequencies = IOHelper.frequencies();
		frequencyStrings = new ArrayList<String>();
		frequencyValues = new ArrayList<String>();
		for(Frequency f: frequencies){
			frequencyStrings.add(f.getFrequencyString());
		}
		for(Frequency f: frequencies){
			frequencyValues.add(f.getFrequencyValue()+"");
		}
		if(frequencies.size() == 0)
		{
			return false;
		}
		return true;
	}

	public List<String> getFrequencyStrings()
	{
		return frequencyStrings;
	}

	public List<String> getFrequencyValues()
	{
		return frequencyValues;
	}
	
}
