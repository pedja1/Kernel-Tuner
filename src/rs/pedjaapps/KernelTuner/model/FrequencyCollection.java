package rs.pedjaapps.KernelTuner.model;

import java.util.List;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;

public class FrequencyCollection
{
    private List<Frequency> frequencies;
    private static FrequencyCollection frequency = null;

    public static synchronized FrequencyCollection getInstance()
    {
        if (frequency == null)
        {
            frequency = new FrequencyCollection();
        }
        return frequency;
    }

    public FrequencyCollection()
    {
        frequencies = IOHelper.frequencies();
    }

    public List<Frequency> getFrequencies()
    {
        return frequencies;
    }

    public int getMaxProgress(String maxFreq)
    {
        int offset = 0;
        for(Frequency f : frequencies)
        {
            if((f.getFrequencyValue() + "").equals(maxFreq))
            {
                return offset + 1;
            }
            offset++;
        }
        return 0;
    }

    public int getProgress(String freq)
    {
        int offset = 0;
        for(Frequency f : frequencies)
        {
            if((f.getFrequencyValue() + "").equals(freq))
            {
                return offset + 1;
            }
            offset++;
        }
        return 0;
    }
}
