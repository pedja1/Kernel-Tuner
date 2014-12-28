package rs.pedjaapps.kerneltuner.appmanager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.adapter.SystemInfoPagerAdapter;
import rs.pedjaapps.kerneltuner.ui.AbsActivity;

/**
 * Created by pedja on 28.12.14..
 * <p/>
 * This file is part of Kernel-Tuner
 * Copyright Predrag Čokulov 2014
 */
public class AppDetailsActivity extends AbsActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager mPager = (ViewPager)findViewById(R.id.pager);
        AppDetailsPagerAdapter mPagerAdapter = new AppDetailsPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(mPager);
    }
}
