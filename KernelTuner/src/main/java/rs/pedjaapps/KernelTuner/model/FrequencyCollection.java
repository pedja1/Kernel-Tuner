package rs.pedjaapps.KernelTuner.model;

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
		checkFreq(frequencies);
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
		checkFreq(frequencyStrings);
		return frequencyStrings;
	}

	public List<String> getFrequencyValues()
	{
		checkFreq(frequencyValues);
		return frequencyValues;
	}
	
	private void checkFreq(List freq)
	{
		if(freq == null || freq.size() == 0)
		{
			getAllFrequencies();
		}
	}
}
