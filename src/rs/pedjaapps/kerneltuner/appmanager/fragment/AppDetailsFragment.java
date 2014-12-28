package rs.pedjaapps.kerneltuner.appmanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.pedjaapps.kerneltuner.R;

/**
 * Created by pedja on 28.12.14..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2014
 */
public class AppDetailsFragment extends Fragment
{
    public static AppDetailsFragment newInstance()
    {
        AppDetailsFragment fragment = new AppDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_layout_app_details, container, false);
        return view;
    }
}
