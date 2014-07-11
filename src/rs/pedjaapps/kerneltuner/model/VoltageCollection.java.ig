package rs.pedjaapps.KernelTuner.model;

import java.util.List;

import rs.pedjaapps.KernelTuner.helpers.IOHelper;

public class VoltageCollection
{
    private List<Voltage> voltages;
    private static VoltageCollection voltageCollection = null;

    public static synchronized VoltageCollection getInstance()
    {
        if (voltageCollection == null)
        {
            voltageCollection = new VoltageCollection();
        }
        return voltageCollection;
    }

    public void readVoltages()
    {
        voltages = IOHelper.voltages();
    }

    public VoltageCollection()
    {
        voltages = IOHelper.voltages();
    }

    public List<Voltage> getVoltages()
    {
        return voltages;
    }
}
