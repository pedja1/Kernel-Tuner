package rs.pedjaapps.KernelTuner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * Created by pedja on 27.6.15..
 */
public abstract class AbsFragment extends Fragment
{
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(getTitle()).toUpperCase());
    }

    @StringRes
    public abstract int getTitle();
}
