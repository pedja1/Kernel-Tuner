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

    public static int getProgress(int freq, List<Frequency> frequencies)
    {
        int offset = 0;
        for(Frequency f : frequencies)
        {
            if(f.getFrequencyValue() == freq)
            {
                return offset + 1;
            }
            offset++;
        }
        return 0;
    }

    public static String getHrStringFromFreq(int freq)
    {
        //convert freq in hz to human readable format(eg. 500mhz, 2ghz)
        if(freq / 1000000 > 0)
        {
            return (double)freq / 1000000 + "Ghz";
        }
        else if(freq / 1000 > 0)
        {
            return (double)freq/1000 + "Mhz";
        }
        else
        {
            return freq + "Hz";//should never happen
        }
    }
}
