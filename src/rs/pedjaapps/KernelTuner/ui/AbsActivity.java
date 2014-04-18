package rs.pedjaapps.KernelTuner.ui;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import rs.pedjaapps.KernelTuner.R;

/**
 * Created by pedja on 17.4.14..
 */
public abstract class AbsActivity extends FragmentActivity
{
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

    public abstract int getThemeRes();
}
