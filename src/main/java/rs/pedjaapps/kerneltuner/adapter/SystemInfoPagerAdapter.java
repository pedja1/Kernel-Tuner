package rs.pedjaapps.kerneltuner.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.fragments.SensorsFragment;
import rs.pedjaapps.kerneltuner.fragments.SystemInfoFragment;

/**
 * Created by pedja on 13.7.14..
 */
public class SystemInfoPagerAdapter extends FragmentPagerAdapter
{
    Context context;

    public SystemInfoPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return SystemInfoFragment.newInstance(SystemInfoFragment.Type.device);
            case 1:
                return SystemInfoFragment.newInstance(SystemInfoFragment.Type.battery);
            case 2:
                return SystemInfoFragment.newInstance(SystemInfoFragment.Type.memory);
            case 3:
                return SystemInfoFragment.newInstance(SystemInfoFragment.Type.cpu);
            case 4:
                return SensorsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return context.getString(R.string.device).toUpperCase();
            case 1:
                return context.getString(R.string.battery).toUpperCase();
            case 2:
                return context.getString(R.string.memory).toUpperCase();
            case 3:
                return context.getString(R.string.cpu_gpu).toUpperCase();
            case 4:
                return context.getString(R.string.sensors).toUpperCase();
        }
        return "";
    }
}
