package rs.pedjaapps.KernelTuner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.pedjaapps.KernelTuner.MainActivity;
import rs.pedjaapps.KernelTuner.R;

/**
 * Created by pedja on 20.6.15..
 */
public class FragmentOverview extends Fragment
{
    public static FragmentOverview newInstance()
    {
        FragmentOverview fragment = new FragmentOverview();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //((MainActivity)getActivity()).setToolbar(getString(R.string.overview), MainActivity.getToolbarHeight());
        ((MainActivity)getActivity()).setTitle(getString(R.string.overview).toUpperCase());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_overview, container, false);
        return view;
    }
}
