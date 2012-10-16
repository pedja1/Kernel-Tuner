package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CPUTuner extends FragmentActivity {

	SharedPreferences sharedPrefs;
	private ArrayList<String> mTitles = new ArrayList<String>();
	String anthrax;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_cputuner);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mTitles.add(getString(R.string.title_cpu).toUpperCase());
		if(CPUInfo.voltageExists()==true){
		mTitles.add(getString(R.string.title_voltage).toUpperCase());
		}
		if(CPUInfo.TISExists()==true){
		mTitles.add(getString(R.string.title_tis).toUpperCase());
		}
		mTitles.add(getString(R.string.title_governor).toUpperCase());
	
		
	
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			
			int index = mTitles.indexOf(extras.getString("item"));
			mViewPager.setCurrentItem(index);
			
		}
	}

	@Override
	public void onResume() {
	

		super.onResume();
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;

			Bundle args = new Bundle();

			if (mTitles.get(i).equals("CPU TWEAKS")) {

				fragment = new CPUFragment();
				args.putInt(CPUFragment.ARG_SECTION_NUMBER, i + 1);
			} else if (mTitles.get(i).equals("GOVERNOR SETTINGS")) {

				fragment = new GovernorFragment();
				args.putInt(GovernorFragment.ARG_SECTION_NUMBER, i + 1);
			} else if (mTitles.get(i).equals("TIMES IN STATE")) {

				fragment = new TISFragment();
				args.putInt(TISFragment.ARG_SECTION_NUMBER, i + 1);
			} else if (mTitles.get(i).equals("VOLTAGE")) {

				fragment = new VoltageFragment();
				args.putInt(VoltageFragment.ARG_SECTION_NUMBER, i + 1);
			} 
			
			fragment.setArguments(args);
			return fragment;

		}

		@Override
		public int getCount() {
			return mTitles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			return mTitles.get(position);
		}

	}

	
}