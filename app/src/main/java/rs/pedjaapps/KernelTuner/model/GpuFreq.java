package rs.pedjaapps.KernelTuner.model;

/**
 * Created by pedja on 27.6.15..
 */
public class GpuFreq
{
    public final int freq;
    public final String formatted;

    public GpuFreq(int freq)
    {
        this.freq = freq;
        formatted = freq / 1000000 + "MHz";
    }
}
