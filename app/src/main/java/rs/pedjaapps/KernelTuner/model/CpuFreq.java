package rs.pedjaapps.KernelTuner.model;

/**
 * Created by pedja on 27.6.15..
 */
public class CpuFreq
{
    public final int freq;
    public final String formatted;

    public CpuFreq(int freq)
    {
        this.freq = freq;
        formatted = freq / 1000 + "MHz";
    }
}
