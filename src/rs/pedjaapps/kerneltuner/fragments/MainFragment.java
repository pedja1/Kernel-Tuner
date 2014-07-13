package rs.pedjaapps.kerneltuner.fragments;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

//import com.google.android.gms.internal.*;
import java.io.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.ui.*;
import rs.pedjaapps.kerneltuner.utility.*;

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

        gpu.setOnClickListener(new StartActivityListener((new File(Constants.GPU_SGX540).exists()) ? GpuSGX540.class : Gpu.class));
        gpu.setOnLongClickListener(new InfoListener(R.drawable.gpu,
                getResources().getString(R.string.info_gpu_title),
                getResources().getString(R.string.info_gpu_text),
                Constants.G_S_URL_PREFIX + "GPU", true));

        Button voltage = (Button) view.findViewById(R.id.btn_voltage);
        //voltage.setOnClickListener(new StartActivityListener(VoltageActivity.class));
        voltage.setOnLongClickListener(new InfoListener(R.drawable.voltage,
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
        cpu.setOnLongClickListener(new InfoListener(R.drawable.ic_launcher,
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
        tis.setOnLongClickListener(new InfoListener(R.drawable.times,
                getResources().getString(R.string.info_tis_title),
                getResources().getString(R.string.info_tis_text),
                Constants.G_S_URL_PREFIX + "cpu times_in_state", true));

        Button mp = (Button) view.findViewById(R.id.btn_mpdecision);
        mp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Intent myIntent = null;
                if (new File(Constants.MPDEC_THR_DOWN).exists())
                {
                    myIntent = new Intent(getActivity(), Mpdecision.class);
                }
                else if (new File(Constants.MPDEC_THR_0).exists())
                {
                    myIntent = new Intent(getActivity(), MpdecisionNew.class);
                }
                startActivity(myIntent);

            }
        });
        mp.setOnLongClickListener(new InfoListener(R.drawable.dual,
                getResources().getString(R.string.info_mpd_title),
                getResources().getString(R.string.info_mpd_text),
                Constants.G_S_URL_PREFIX + "mp-decision", true));

        Button misc = (Button) view.findViewById(R.id.btn_misc);
        misc.setOnClickListener(new StartActivityListener(MiscTweaks.class));
        misc.setOnLongClickListener(new InfoListener(R.drawable.misc,
                getResources().getString(R.string.info_misc_title),
                getResources().getString(R.string.info_misc_text), "", false));

        Button governor = (Button) view.findViewById(R.id.btn_governor);
        governor.setOnClickListener(new StartActivityListener(
                GovernorActivity.class));
        governor.setOnLongClickListener(new InfoListener(
                R.drawable.main_governor, getResources().getString(
                R.string.info_gov_title), getResources().getString(
                R.string.info_gov_text), Constants.G_S_URL_PREFIX
                + "linux governors", true));

        Button oom = (Button) view.findViewById(R.id.btn_oom);
        oom.setOnClickListener(new StartActivityListener(OOM.class));
        oom.setOnLongClickListener(new InfoListener(R.drawable.oom,
                getResources().getString(R.string.info_oom_title),
                getResources().getString(R.string.info_oom_text),
                Constants.G_S_URL_PREFIX + "oom", true));

        Button profiles = (Button) view.findViewById(R.id.btn_profiles);
        profiles.setOnClickListener(new StartActivityListener(Profiles.class));
        profiles.setOnLongClickListener(new InfoListener(R.drawable.profile,
                getResources().getString(R.string.info_profiles_title),
                getResources().getString(R.string.info_profiles_text), "",
                false));

        Button thermal = (Button) view.findViewById(R.id.btn_thermal);
        thermal.setOnClickListener(new StartActivityListener(Thermald.class));

        thermal.setOnLongClickListener(new InfoListener(R.drawable.temp,
                getResources().getString(R.string.info_thermal_title),
                getResources().getString(R.string.info_thermal_text), "", false));

        Button sd = (Button) view.findViewById(R.id.btn_sd);
        sd.setOnClickListener(new StartActivityListener(
                SDScannerActivity.class));
        sd.setOnLongClickListener(new InfoListener(R.drawable.sd,
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
        info.setOnLongClickListener(new InfoListener(R.drawable.info,
                getResources().getString(R.string.info_sys_info_title),
                getResources().getString(R.string.info_sys_info_text), "",
                false));

        Button tm = (Button) view.findViewById(R.id.btn_task_manager);
        tm.setOnClickListener(new StartActivityListener(TaskManager.class));
        tm.setOnLongClickListener(new InfoListener(R.drawable.tm,
                getResources().getString(R.string.info_tm_title),
                getResources().getString(R.string.info_tm_text),
                Constants.G_S_URL_PREFIX + "task manager", true));

        Button build = (Button) view.findViewById(R.id.btn_build);
        build.setOnClickListener(new StartActivityListener(
                BuildpropEditor.class));
        build.setOnLongClickListener(new InfoListener(R.drawable.build,
                getResources().getString(R.string.info_build_title),
                getResources().getString(R.string.info_build_text),
                Constants.G_S_URL_PREFIX + "build.prop", true));

        Button sys = (Button) view.findViewById(R.id.btn_sysctl);
        sys.setOnClickListener(new StartActivityListener(SysCtl.class));
        sys.setOnLongClickListener(new InfoListener(R.drawable.sysctl,
                getResources().getString(R.string.info_sysctl_title),
                getResources().getString(R.string.info_sysctl_text),
                Constants.G_S_URL_PREFIX + "sysctl", true));

        Button log = (Button) view.findViewById(R.id.btn_logcat);
        log.setOnClickListener(new StartActivityListener(LogCat.class));
        log.setOnLongClickListener(new InfoListener(R.drawable.swap,
                getResources().getString(R.string.info_logs_title),
                getResources().getString(R.string.info_logs_text),
                Constants.G_S_URL_PREFIX + "swap", true));

        if (!(new File(Constants.CPU0_FREQS).exists()))
        {
            if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
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
        if (!(new File(Constants.VOLTAGE_PATH).exists()))
        {
            if (!(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()))
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
        if (!(new File(Constants.TIMES_IN_STATE_CPU0).exists()))
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
        if (!(new File(Constants.MPDECISION).exists()))
        {
            if (hideUnsupportedItems)
            {
                mp.setVisibility(View.GONE);
            }
            else
            {
                mp.setEnabled(false);
            }
        }
        if (!(new File(Constants.THERMALD).exists()))
        {
            if (hideUnsupportedItems)
            {
                thermal.setVisibility(View.GONE);
            }
            else
            {
                thermal.setEnabled(false);
            }
        }
        if (!(new File(Constants.GPU_3D).exists()))
        {
            if (!(new File(Constants.GPU_SGX540).exists()))
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

    class StartActivityListener implements View.OnClickListener
    {
        Class<?> cls;

        public StartActivityListener(Class<?> cls)
        {
            this.cls = cls;
        }

        @Override
        public void onClick(View v)
        {
            startActivity(new Intent(getActivity(), cls));
        }
    }

    class InfoListener implements View.OnLongClickListener
    {

        int icon;
        String title;
        String text;
        String url;
        boolean more;

        public InfoListener(int icon, String title, String text, String url, boolean more)
        {
            this.icon = icon;
            this.title = title;
            this.text = text;
            this.url = url;
            this.more = more;
        }

        @Override
        public boolean onLongClick(View v)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(title);

            builder.setIcon(icon);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.text_view_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(text);

            builder.setPositiveButton(getResources().getString(R.string.info_ok), null);
            if (more)
            {
                builder.setNeutralButton(R.string.info_more, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
            builder.setView(view);
            AlertDialog alert = builder.create();

            alert.show();
            return true;
        }
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
