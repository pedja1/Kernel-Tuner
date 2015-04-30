package rs.pedjaapps.kerneltuner.fragments;

import rs.pedjaapps.kerneltuner.ui.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.File;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
import rs.pedjaapps.kerneltuner.utility.InfoListener;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.SimpleStartActivityListener;

/**
 * Created by pedja on 31.5.14..
 */
public class MainFragment extends Fragment
{
    public static MainFragment newInstance()
    {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_fragment_main, container, false);

        boolean hideUnsupportedItems = PrefsManager.hideUnsupportedItems();
        Button gpu = (Button) view.findViewById(R.id.btn_gpu);

		Class<? extends Activity> gpuClass = null;
		if(Constants.GPU_SGX540.exists())
		{
			gpuClass = GpuSGX540.class;
		}
        else if(Constants.GPU_3D_2.exists())
        {
            gpuClass = GpuActivityQNew.class;
        }
		else if(Constants.GPU_3D.exists())
		{
			gpuClass = Gpu.class;
		}
        if(gpuClass != null)
		{
			gpu.setOnClickListener(new SimpleStartActivityListener(gpuClass));
		}
		else
		{
			gpu.setEnabled(false);
		}
        gpu.setOnLongClickListener(new InfoListener(R.drawable.main_gpu,
                getResources().getString(R.string.info_gpu_title),
                getResources().getString(R.string.info_gpu_text),
                Constants.G_S_URL_PREFIX + "GPU", true));
				
		Button voltage = (Button) view.findViewById(R.id.btn_voltage);
        
		Class<? extends Activity> voltageClass = null;
		if(Constants.VOLTAGE_PATH.exists())
		{
			voltageClass = VoltageActivity.class;
		}
		else if(Constants.VOLTAGE_PATH_2.exists())
		{
			voltageClass = VoltageActivity1.class;
		}
		else if(Constants.VOLTAGE_PATH_TEGRA_3.exists())
		{
			voltageClass = VoltageActivityTegra.class;
		}
		
        if(voltageClass != null)
		{
			voltage.setOnClickListener(new SimpleStartActivityListener(voltageClass));
		}
		else
		{
			voltage.setEnabled(false);
		}
        voltage.setOnClickListener(new SimpleStartActivityListener(VoltageActivity.class));
        voltage.setOnLongClickListener(new InfoListener(R.drawable.main_voltage,
                getResources().getString(R.string.info_voltage_title),
                getResources().getString(R.string.info_voltage_text),
                Constants.G_S_URL_PREFIX + "undervolting cpu", true));

        Button cpu = (Button) view.findViewById(R.id.btn_cpu);
        cpu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*if(PrefsManager.showCpuOptionDialog())
                {
                    cpuOptionsDialog();
                }
                else
                {*/
                    Intent intent = new Intent(getActivity(), CPUActivity.class);
                    startActivity(intent);
               /* }*/
                //((MainActivity)getActivity()).setCurrentFragment(CpuFragment.newInstance());
                //getFragmentManager().beginTransaction().replace(R.id.flFragmentContainer, ((MainActivity)getActivity()).getCurrentFragment()).commit();
				
				/*FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getActivity(), fragmentTransaction, MainFragment.this, ((MainActivity)getActivity()).getCurrentFragment(), R.id.flFragmentContainer);
				fragmentTransactionExtended.addTransition(FragmentTransactionExtended.GLIDE);
				fragmentTransaction.addToBackStack("main");
				fragmentTransactionExtended.commit();*/
            }
        });
        cpu.setOnLongClickListener(new InfoListener(R.drawable.main_cpu,
                getResources().getString(R.string.info_cpu_title),
                getResources().getString(R.string.info_cpu_text),
                Constants.G_S_URL_PREFIX + "CPU", true));

        Button tis = (Button) view.findViewById(R.id.btn_times);
        tis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getActivity(), TISActivity.class);
                startActivity(myIntent);
            }
        });
        tis.setOnLongClickListener(new InfoListener(R.drawable.main_times,
                getResources().getString(R.string.info_tis_title),
                getResources().getString(R.string.info_tis_text),
                Constants.G_S_URL_PREFIX + "cpu times_in_state", true));

        /*Button app = (Button) view.findViewById(R.id.btn_app_manager);
        app.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getActivity(), ApplicationManagerActivity.class);
                startActivity(myIntent);
            }
        });
        app.setOnLongClickListener(new InfoListener(R.drawable.main_app,
                getResources().getString(R.string.info_tis_title),
                getResources().getString(R.string.info_tis_text), null, true));*/

        Button misc = (Button) view.findViewById(R.id.btn_misc);
        misc.setOnClickListener(new SimpleStartActivityListener(MiscTweaksActivity.class));
        misc.setOnLongClickListener(new InfoListener(R.drawable.main_misc,
                getResources().getString(R.string.info_misc_title),
                getResources().getString(R.string.info_misc_text), "", false));

        Button governor = (Button) view.findViewById(R.id.btn_governor);
        governor.setOnClickListener(new SimpleStartActivityListener(
                GovernorActivity.class));
        governor.setOnLongClickListener(new InfoListener(
                R.drawable.main_governor, getResources().getString(
                R.string.info_gov_title), getResources().getString(
                R.string.info_gov_text), Constants.G_S_URL_PREFIX
                + "linux governors", true));

        Button oom = (Button) view.findViewById(R.id.btn_oom);
        oom.setOnClickListener(new SimpleStartActivityListener(OOM.class));
        oom.setOnLongClickListener(new InfoListener(R.drawable.main_oom,
                getResources().getString(R.string.info_oom_title),
                getResources().getString(R.string.info_oom_text),
                Constants.G_S_URL_PREFIX + "oom", true));

        /*Button profiles = (Button) view.findViewById(R.id.btn_profiles);
        profiles.setOnClickListener(new StartActivityListener(Profiles.class));
        profiles.setOnLongClickListener(new InfoListener(R.drawable.profile,
                getResources().getString(R.string.info_profiles_title),
                getResources().getString(R.string.info_profiles_text), "",
                false));*/

        Button network = (Button) view.findViewById(R.id.btnNetwork);
        network.setOnClickListener(new SimpleStartActivityListener(NetworkManagerActivity.class));
        network.setOnLongClickListener(new InfoListener(R.drawable.main_network,
                getResources().getString(R.string.info_network_title),
                getResources().getString(R.string.info_network_text), "",
                false));

        Button sd = (Button) view.findViewById(R.id.btn_sd);
        sd.setOnClickListener(new SimpleStartActivityListener(
                SDScannerActivity.class));
        sd.setOnLongClickListener(new InfoListener(R.drawable.main_sd,
                getResources().getString(R.string.info_sd_title),
                getResources().getString(R.string.info_sd_text), "", false));


        Button info = (Button) view.findViewById(R.id.btn_info);
        info.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getActivity(), SystemInfoActivity.class);
                startActivity(myIntent);
            }
        });
        info.setOnLongClickListener(new InfoListener(R.drawable.main_info,
                getResources().getString(R.string.info_sys_info_title),
                getResources().getString(R.string.info_sys_info_text), "",
                false));

        Button tm = (Button) view.findViewById(R.id.btn_task_manager);
        tm.setOnClickListener(new SimpleStartActivityListener(TaskManager.class));
        tm.setOnLongClickListener(new InfoListener(R.drawable.main_tm,
                getResources().getString(R.string.info_tm_title),
                getResources().getString(R.string.info_tm_text),
                Constants.G_S_URL_PREFIX + "task manager", true));

        Button build = (Button) view.findViewById(R.id.btn_build);
        build.setOnClickListener(new SimpleStartActivityListener(
                BuildpropEditor.class));
        build.setOnLongClickListener(new InfoListener(R.drawable.main_build,
                getResources().getString(R.string.info_build_title),
                getResources().getString(R.string.info_build_text),
                Constants.G_S_URL_PREFIX + "build.prop", true));

        Button sys = (Button) view.findViewById(R.id.btn_sysctl);
        sys.setOnClickListener(new SimpleStartActivityListener(SysCtlActivity.class));
        sys.setOnLongClickListener(new InfoListener(R.drawable.main_sysctl,
                getResources().getString(R.string.info_sysctl_title),
                getResources().getString(R.string.info_sysctl_text),
                Constants.G_S_URL_PREFIX + "sysctl", true));

        Button log = (Button) view.findViewById(R.id.btn_logcat);
        log.setOnClickListener(new SimpleStartActivityListener(LogCat.class));
        log.setOnLongClickListener(new InfoListener(R.drawable.main_log,
                getResources().getString(R.string.info_logs_title),
                getResources().getString(R.string.info_logs_text),
                Constants.G_S_URL_PREFIX + "swap", true));

		/*Button fm = (Button) view.findViewById(R.id.btn_fm);
        fm.setOnClickListener(new SimpleStartActivityListener(FMActivity.class));
        fm.setOnLongClickListener(new InfoListener(R.drawable.folder,
			getResources().getString(R.string.info_fm_title),
			getResources().getString(R.string.info_fm_text),
			"", false));*/

        if (!(Constants.CPU0_FREQS.exists()))
        {
            if (!(Constants.TIMES_IN_STATE_CPU0.exists()))
            {
                if (hideUnsupportedItems)
                {
                    cpu.setVisibility(View.GONE);
                }
                else
                {
                    cpu.setEnabled(false);
                }
            }
        }
        if (!(Constants.VOLTAGE_PATH.exists()))
        {
            if (!(Constants.VOLTAGE_PATH_TEGRA_3.exists()))
            {
				if (!(Constants.VOLTAGE_PATH_2.exists()))
				{
                if (hideUnsupportedItems)
                {
                    voltage.setVisibility(View.GONE);
                }
                else
                {
                    voltage.setEnabled(false);
                }
				}
            }
        }
        if (!(Constants.TIMES_IN_STATE_CPU0.exists()))
        {
            if (hideUnsupportedItems)
            {
                tis.setVisibility(View.GONE);
            }
            else
            {
                tis.setEnabled(false);
            }
        }
        if (!IOHelper.gpuExists())
        {
            if (!(Constants.GPU_SGX540.exists()))
            {
                if (hideUnsupportedItems)
                {
                    gpu.setVisibility(View.GONE);
                }
                else
                {
                    gpu.setEnabled(false);
                }
            }
        }
        return view;
    }

    private void cpuOptionsDialog()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.cpu_options_dialog, null);
        final CheckBox cbEnableAll = (CheckBox)v.findViewById(R.id.cbEnableAll);
        final CheckBox cbDisableAll = (CheckBox)v.findViewById(R.id.cbDisableAllOnExit);
        final CheckBox cbSave = (CheckBox)v.findViewById(R.id.cbSave);
        cbEnableAll.setChecked(PrefsManager.getCpuEnableAll());
        cbDisableAll.setChecked(PrefsManager.getCpuDisableAll());
        b.setView(v);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface p1, int p2)
            {
                startCpuSettings(cbEnableAll.isChecked(), cbDisableAll.isChecked(), cbSave.isChecked());
            }
        });
        b.setNegativeButton("Cancel", null);

        b.show();
    }

    private void startCpuSettings(boolean enableAll, boolean disableAll, boolean save)
    {
        PrefsManager.setCpuEnableAll(enableAll);
        PrefsManager.setCpuDisableAll(disableAll);
        PrefsManager.setShowCpuOptionsDialog(save);
        Intent intent = new Intent(getActivity(), CPUActivity.class);
        startActivity(intent);
    }
}
