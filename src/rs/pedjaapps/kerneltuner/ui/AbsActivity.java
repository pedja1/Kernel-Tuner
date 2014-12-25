package rs.pedjaapps.kerneltuner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.content.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.analytics.*;
import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.utility.*;

import android.support.v7.widget.Toolbar;

/**
 * Created by pedja on 17.4.14..
 */
public abstract class AbsActivity extends ActionBarActivity
{
    public static final String ACTION_TOGGLE_PRO_VERSION = "action_toggle_pro_version";
    AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		overridePendingTransition(R.anim.zoom_in_right, R.anim.zoom_out_left);
		super.onCreate(savedInstanceState);
		/*ViewGroup root = (ViewGroup) getWindow().getDecorView();//.findViewById(android.R.id.content);
		//LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
		Toolbar toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar, root, false);
		root.addView(toolbar, 0); // insert at top
        
		setSupportActionBar(toolbar);*/
		
        Tracker t = MainApp.getInstance().getTracker(MainApp.TrackerName.APP_TRACKER);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TOGGLE_PRO_VERSION);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, filter);
        LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent(ACTION_TOGGLE_PRO_VERSION));
	}

    @Override
    protected void onStart()
    {
        super.onStart();
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5750ECFACEA6FCE685DE7A97D8C59A5F")
                .addTestDevice("05FBCDCAC44495595ACE7DC1AEC5C208")
                .addTestDevice("40AA974617D79A7A6C155B1A2F57D595")
                .build();
        if(!PrefsManager.isProVersion())adView.loadAd(adRequest);
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
    protected void onDestroy()
    {
        super.onDestroy();
        if(localReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
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

    BroadcastReceiver localReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(ACTION_TOGGLE_PRO_VERSION.equals(intent.getAction()))
            {
                if(PrefsManager.isProVersion())
                {
                    if (adView != null)
                    {
                        adView.destroy();
                        adView.setVisibility(View.GONE);
                    }
                    if(AbsActivity.this instanceof MainActivity)
                        getSupportActionBar().setTitle(Html.fromHtml(getString(R.string.app_name_pro_styled)));
                }
                else
                {
                    if(AbsActivity.this instanceof MainActivity)
                        getSupportActionBar().setTitle(R.string.app_name);
                }
            }
        }
    };
}
