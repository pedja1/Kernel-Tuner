package rs.pedjaapps.KernelTuner.model;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.utility.FSHelper;

/**
 * Created by pedja on 9/28/13.
 */
public class MainOptionsCollection
{
    private static MainOptionsCollection mainOptionsCollection = null;
    private List<MainOption> mainOptions;

    public static MainOptionsCollection getInstance()
    {
        if(mainOptionsCollection == null)
            mainOptionsCollection = new MainOptionsCollection();
        return mainOptionsCollection;
    }

    private MainOptionsCollection()
    {
        mainOptions = new ArrayList<MainOption>();
    }

    public int generateOptions()
    {
        List<MainOption> options = new ArrayList<MainOption>();

        if(FSHelper.isCpuAvailable())
            options.add(new MainOption(R.string.main_cpu_title, R.string.main_cpu_text, R.drawable.main_cpu, true));
        if(FSHelper.isTimesInStateAvailable())
            options.add(new MainOption(R.string.main_tis_title, R.string.main_tis_text, R.drawable.main_times, false));
        options.add(new MainOption(R.string.main_gov_title, R.string.main_gov_text, R.drawable.main_governor, false));
        if(FSHelper.isGpuAvailable())
            options.add(new MainOption(R.string.main_gpu_title, R.string.main_gpu_text, R.drawable.main_gpu, false));
        options.add(new MainOption(R.string.main_misc_title, R.string.main_misc_text, R.drawable.main_misc, false));
        options.add(new MainOption(R.string.main_oom_title, R.string.main_oom_text, R.drawable.main_oom, false));

        options.add(new MainOption(R.string.main_sd_title, R.string.main_sd_text, R.drawable.main_sd, false));
        options.add(new MainOption(R.string.main_tm_title, R.string.main_tm_text, R.drawable.main_tm, false));
        options.add(new MainOption(R.string.main_build_title, R.string.main_build_text, R.drawable.main_build, false));
        options.add(new MainOption( R.string.main_sysctl_title, R.string.main_sysctl_text, R.drawable.main_sysctl, false));

        options.add(new MainOption(R.string.main_logs_title, R.string.main_logs_text, R.drawable.main_log, false));
        options.add(new MainOption( R.string.main_profiles_title, R.string.main_profiles_text, R.drawable.main_profiles, false));
        options.add(new MainOption(R.string.main_sys_info_title, R.string.main_sys_info_text, R.drawable.main_info, false));




        setMainOptions(options);

        return mainOptions.size();
    }

    public List<MainOption> getMainOptions()
    {
        if(mainOptions.size() == 0)
        {
            generateOptions();
        }
        return mainOptions;
    }

    public void setMainOptions(List<MainOption> mainOptions)
    {
        this.mainOptions = mainOptions;
    }
}
