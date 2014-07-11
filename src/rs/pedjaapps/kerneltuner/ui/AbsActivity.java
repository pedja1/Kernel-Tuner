package rs.pedjaapps.kerneltuner.ui;

import android.app.*;
import android.os.*;
import android.support.v4.app.*;
import rs.pedjaapps.kerneltuner.*;

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
