package rs.pedjaapps.KernelTuner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.pedjaapps.KernelTuner.R;

/**
 * Created by pedja on 20.6.15..
 */
public class FragmentCPU extends Fragment
{
    public static FragmentCPU newInstance()
    {
        FragmentCPU fragment = new FragmentCPU();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_cpu, container, false);
        return view;
    }
}
