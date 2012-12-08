package rs.pedjaapps.KernelTuner;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class ChangeVoltage extends AsyncTask<String, Void, String>
{

	Context context;

	public ChangeVoltage(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}
	

	SharedPreferences preferences;

	@Override
	protected String doInBackground(String... args)
	{

		List<CPUInfo.VoltageList> voltageList = CPUInfo.voltages();
		List<Integer> voltages = new ArrayList<Integer>();
		List<String> voltageFreqs =  new ArrayList<String>();
		
		for(CPUInfo.VoltageList v: voltageList){
			voltageFreqs.add((v.getFreq()));
		}
		for(CPUInfo.VoltageList v: voltageList){
			voltages.add(v.getVoltage());
		}
		
		Process localProcess;
		System.out.println("ChangeVoltage: Changing voltage");
		if (new File(CPUInfo.VOLTAGE_PATH).exists())
		{
			if (args[0].equals("minus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");

					for (int i = 0; i < voltageFreqs.size(); i++)
					{
						int volt = voltages.get(i) - 12500;
						if (volt >= 700000 && volt <= 1400000)
						{
							localDataOutputStream
								.writeBytes("echo "
											+ voltageFreqs.get(i)
											+ " "
											+ volt
											+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + volt);
							editor.commit();
						}
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("plus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");

					for (int i = 0; i < voltageFreqs.size(); i++)
					{
						int volt = voltages.get(i) + 12500;
						if (volt >= 700000 && volt <= 1400000)
						{
							localDataOutputStream
								.writeBytes("echo "
											+ voltageFreqs.get(i)
											+ " "
											+ volt
											+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + volt);
							editor.commit();
						}
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleplus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");


					int volt = voltages.get(Integer.parseInt(args[1])) + 12500;
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ voltageFreqs.get(Integer.parseInt(args[1]))
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
						SharedPreferences.Editor editor = preferences.edit();
					    editor.putString("voltage_" + voltageFreqs.get(Integer.parseInt(args[1])), voltageFreqs.get(Integer.parseInt(args[1])) + " " + volt);
					    editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleminus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");


					int volt = voltages.get(Integer.parseInt(args[1])) - 12500;
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ voltageFreqs.get(Integer.parseInt(args[1]))
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
						SharedPreferences.Editor editor = preferences.edit();
					    editor.putString("voltage_" + voltageFreqs.get(Integer.parseInt(args[1])), voltageFreqs.get(Integer.parseInt(args[1])) + " " + volt);
					    editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleseek"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");


					int volt = Integer.parseInt(args[1]);
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ args[2]
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
						SharedPreferences.Editor editor = preferences.edit();
					    editor.putString("voltage_" + args[2], args[2] + " " + volt);
					    editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			
			else if (args[0].equals("profile"))
			{
				
				String[] values = args[1].split("\\s");
						try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");

					for (int i = 0; i < voltageFreqs.size(); i++)
					{
						//int volt = voltages.get(i) + 12500;
						
							localDataOutputStream
								.writeBytes("echo "
											+ voltageFreqs.get(i)
											+ " "
											+ values[i]
											+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + values[i]);
							editor.commit();
						
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			
		}
		else if (new File(CPUInfo.VOLTAGE_PATH_TEGRA_3).exists())
		{
			if (args[0].equals("minus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");

					for (int i = 0; i < voltageFreqs.size(); i++)
					{
						int volt = voltages.get(i) - 12500;
						if (volt >= 700000 && volt <= 1400000)
						{
							localDataOutputStream
								.writeBytes("echo "
											+ voltageFreqs.get(i)
											+ " "
											+ volt
											+ " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + volt);
							editor.commit();
						}
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("plus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");

					for (int i = 0; i < voltageFreqs.size(); i++)
					{
						int volt = voltages.get(i) + 12500;
						if (volt >= 700000 && volt <= 1400000)
						{
							localDataOutputStream
								.writeBytes("echo "
											+ voltageFreqs.get(i)
											+ " "
											+ volt
											+ " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");
							SharedPreferences.Editor editor = preferences.edit();
						    editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + volt);
						    editor.commit();
						}
					}
					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleplus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");


					int volt = voltages.get(Integer.parseInt(args[1])) + 12500;
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ voltageFreqs.get(Integer.parseInt(args[1]))
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("voltage_" + voltageFreqs.get(Integer.parseInt(args[1])), voltageFreqs.get(Integer.parseInt(args[1])) + " " + volt);
						editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleminus"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");


					int volt = voltages.get(Integer.parseInt(args[1])) - 12500;
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ voltageFreqs.get(Integer.parseInt(args[1]))
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("voltage_" + voltageFreqs.get(Integer.parseInt(args[1])), voltageFreqs.get(Integer.parseInt(args[1])) + " " + volt);
						editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
			else if (args[0].equals("singleseek"))
			{
				try
				{
					localProcess = Runtime.getRuntime().exec("su");

					DataOutputStream localDataOutputStream = new DataOutputStream(
						localProcess.getOutputStream());
					localDataOutputStream
						.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");


					int volt = Integer.parseInt(args[1]);
					if (volt >= 700000 && volt <= 1400000)
					{
						localDataOutputStream
							.writeBytes("echo "
										+ args[2]
										+ " "
										+ volt
										+ " > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table\n");
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("voltage_" + args[2], args[2] + " " + volt);
						editor.commit();
					}

					localDataOutputStream.writeBytes("exit\n");
					localDataOutputStream.flush();
					localDataOutputStream.close();
					localProcess.waitFor();
					localProcess.destroy();

				}
				catch (IOException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
				catch (InterruptedException e1)
				{
					new LogWriter().execute(new String[] {getClass().getName(), e1.getMessage()});
				}
			}
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result)
	{
		VoltageActivity.notifyChanges();
		VoltageAdapter.pd.dismiss();
	}
}	

