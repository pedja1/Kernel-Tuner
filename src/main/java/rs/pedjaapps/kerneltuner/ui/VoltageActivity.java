package rs.pedjaapps.kerneltuner.ui;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.root.RootUtils;

public class VoltageActivity extends AbsVoltageActivity
{
	@Override
	public String driverName()
	{
		return "/vdd_table/vdd_levels";
	}


	@Override
	public void load(Voltage voltage)
	{
		List<String> cmds = new ArrayList<>();
		cmds.add("chmod 777 " + Constants.VOLTAGE_PATH);
		String[] freqs = voltage.getDbFreqs().trim().split(",");
		String[] voltages = voltage.getDbValues().trim().split(",");
		if(freqs.length != voltages.length)return;
		
		for(int i = 0; i < freqs.length; i++)
		{
			cmds.add("echo '" + freqs[i] + " " + voltages[i] + "' > " + Constants.VOLTAGE_PATH);
		}
		new RootUtils().exec(this, cmds.toArray(new String[cmds.size()]));
	}

	@Override
	public void decreaseAll()
	{
		new RootUtils().exec(this,
							 "chmod 777 " + Constants.VOLTAGE_PATH,
							 "echo -12500 > " + Constants.VOLTAGE_PATH
							 );
	}

	@Override
	public void increaseAll()
	{
		new RootUtils().exec(this,
							 "chmod 777 " + Constants.VOLTAGE_PATH,
							 "echo +12500 > " + Constants.VOLTAGE_PATH
							 );
	}

	@Override
	public void changeSingle(Voltage voltage)
	{
		new RootUtils().exec(this,
							 "chmod 777 " + Constants.VOLTAGE_PATH,
							 "echo '" + voltage.getFreqValue() + " " + voltage.getValue() + "' > " + Constants.VOLTAGE_PATH
							 );
	}
	
}
