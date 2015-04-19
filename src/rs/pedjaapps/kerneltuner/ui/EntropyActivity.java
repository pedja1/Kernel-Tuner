package rs.pedjaapps.kerneltuner.ui;

import android.os.Bundle;

import com.devadvance.circularseekbar.CircularSeekBar;

import rs.pedjaapps.kerneltuner.R;

/**
 * Created by pedja on 24.1.15..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2015
 */
public class EntropyActivity extends AbsActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entropy);

        CircularSeekBar sbRead = (CircularSeekBar)findViewById(R.id.sbRead);
        CircularSeekBar sbWrite = (CircularSeekBar)findViewById(R.id.sbWrite);
    }
}
