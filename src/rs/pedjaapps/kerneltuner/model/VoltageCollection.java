package rs.pedjaapps.kerneltuner.model;

import java.util.List;

import rs.pedjaapps.kerneltuner.utility.IOHelper;

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

    public VoltageCollection readVoltages()
    {
        voltages = IOHelper.voltages();
        return this;
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
