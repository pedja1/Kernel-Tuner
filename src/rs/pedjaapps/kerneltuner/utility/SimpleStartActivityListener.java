package rs.pedjaapps.kerneltuner.utility;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class SimpleStartActivityListener implements View.OnClickListener
{
    Class<?> cls;

    public SimpleStartActivityListener(Class<?> cls)
    {
        this.cls = cls;
    }

    @Override
    public void onClick(View v)
    {
        v.getContext().startActivity(new Intent(v.getContext(), cls));
    }
}