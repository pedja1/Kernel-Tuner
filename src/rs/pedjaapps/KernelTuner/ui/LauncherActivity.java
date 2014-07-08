package rs.pedjaapps.KernelTuner.ui;

import android.content.Intent;
import android.os.Bundle;

import rs.pedjaapps.KernelTuner.R;

/**
 * Created by pedja on 18.4.14..
 */
public class LauncherActivity extends AbsActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if()
        {

        }*/
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public int getThemeRes()
    {
        return R.style.Theme_Translucent_NoTitleBar_Light;
    }
}
