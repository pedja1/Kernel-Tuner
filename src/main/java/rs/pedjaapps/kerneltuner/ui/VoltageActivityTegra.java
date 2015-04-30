package rs.pedjaapps.kerneltuner.ui;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.model.VoltageCollection;

public class VoltageActivityTegra extends AbsVoltageActivity
{

	@Override
	public String driverName()
	{
		return "/UV_mV_table";
	}


	@Override
	public void load(Voltage voltage)
	{
		List<String> cmds = new ArrayList<>();
		cmds.add("chmod 777 " + Constants.VOLTAGE_PATH_TEGRA_3);
		
		String voltages = voltage.getDbValues().trim();
		cmds.add("echo '" + voltages + "' > " + Constants.VOLTAGE_PATH_TEGRA_3);
		
		new RootUtils().exec(this, cmds.toArray(new String[cmds.size()]));
	}

	@Override
	public void decreaseAll()
	{
		List<Voltage> voltages = VoltageCollection.getInstance().getVoltages();
		List<String> cmds = new ArrayList<>();
		cmds.add("chmod 777 " + Constants.VOLTAGE_PATH_TEGRA_3);
		StringBuilder builder = new StringBuilder();
		for(Voltage voltage : voltages)
		{
			builder.append(voltage.getValue() - 12).append(" ");
		}
		cmds.add("echo '" + builder.toString() + "' > " + Constants.VOLTAGE_PATH_TEGRA_3);
		new RootUtils().exec(this, cmds.toArray(new String[cmds.size()]));
	}

	@Override
	public void increaseAll()
	{
		List<Voltage> voltages = VoltageCollection.getInstance().getVoltages();
		List<String> cmds = new ArrayList<>();
		cmds.add("chmod 777 " + Constants.VOLTAGE_PATH_TEGRA_3);
		StringBuilder builder = new StringBuilder();
		for(Voltage voltage : voltages)
		{
			builder.append(voltage.getValue() + 12).append(" ");
		}
		cmds.add("echo '" + builder.toString() + "' > " + Constants.VOLTAGE_PATH_TEGRA_3);
		new RootUtils().exec(this, cmds.toArray(new String[cmds.size()]));
	}

	@Override
	public void changeSingle(Voltage voltage)
	{
		List<Voltage> voltages = VoltageCollection.getInstance().getVoltages();
		StringBuilder builder = new StringBuilder();
		for(Voltage v : voltages)
		{
			if(voltage.getFreqValue() == v.getFreqValue())
			{
				builder.append(voltage.getValue() / voltage.getMultiplier()).append(" ");
			}
			else
			{
				builder.append(v.getValue()).append(" ");
			}
		}
		new RootUtils().exec(this,
							 "chmod 777 " + Constants.VOLTAGE_PATH,
							 "echo '" + builder.toString() + "' > " + Constants.VOLTAGE_PATH_TEGRA_3
							 );
	}
	
}
