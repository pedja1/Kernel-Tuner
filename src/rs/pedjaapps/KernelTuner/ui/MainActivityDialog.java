package rs.pedjaapps.KernelTuner.ui;

import android.widget.Button;

import rs.pedjaapps.KernelTuner.R;

/**
 * Created by pedja on 17.4.14..
 */
public class MainActivityDialog extends MainActivity
{
    @Override
    public int getThemeRes()
    {
        return R.style.Theme_Translucent_NoTitleBar_Light;
    }

    @Override
    public int getLayoutRes()
    {
        return R.layout.activity_main_popup;
    }

    @Override
    public void setupView()
    {
        Button info = (Button) findViewById(R.id.btn_info);
        if (minimal)
        {
            info.setText("Settings");
            info.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
                    .getDrawable(R.drawable.settings), null, null);
        }
    }
}
