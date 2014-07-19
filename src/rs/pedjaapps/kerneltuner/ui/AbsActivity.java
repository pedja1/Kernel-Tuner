package rs.pedjaapps.kerneltuner.ui;

import android.app.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;

/**
 * Created by pedja on 17.4.14..
 */
public abstract class AbsActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		overridePendingTransition(R.anim.zoom_in_right, R.anim.zoom_out_left);
		super.onCreate(savedInstanceState);
        Tracker t = MainApp.getInstance().getTracker(MainApp.TrackerName.APP_TRACKER);
	}

    @Override
    protected void onStart()
    {
        super.onStart();
        final AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5750ECFACEA6FCE685DE7A97D8C59A5F")
                .addTestDevice("05FBCDCAC44495595ACE7DC1AEC5C208")
                .addTestDevice("40AA974617D79A7A6C155B1A2F57D595")
                .build();
        if(PrefsManager.showAds())adView.loadAd(adRequest);
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        //test interstitial ad
        // Create the interstitial.
        /*interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-6294976772687752/1839387229");
        if(!SettingsManager.adsRemoved() && SettingsManager.canDisplayAdds())interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                displayInterstitial();
            }
        });*/

        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.zoom_in_left, R.anim.zoom_out_right);
	}
	
    ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog()
    {
        return getProgressDialog(getString(R.string.please_wait));
    }

    public ProgressDialog getProgressDialog(int message)
    {
        return getProgressDialog(getString(message));
    }

    public ProgressDialog  getProgressDialog(String message)
    {
        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(message);
        return progressDialog;
    }
}
