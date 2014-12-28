package rs.pedjaapps.kerneltuner.appmanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.appmanager.fragment.AppDetailsFragment;
import rs.pedjaapps.kerneltuner.fragments.SensorsFragment;
import rs.pedjaapps.kerneltuner.fragments.SystemInfoFragment;

/**
 * Created by pedja on 13.7.14..
 */
public class AppDetailsPagerAdapter extends FragmentPagerAdapter
{
    Context context;

    public AppDetailsPagerAdapter(FragmentManager fm, Context context)
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
                return AppDetailsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return context.getString(R.string.details).toUpperCase();
        }
        return "";
    }
}
