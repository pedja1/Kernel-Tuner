package rs.pedjaapps.KernelTuner;

import android.app.Application;

import com.pedja1.fontwidget.lib.TypefaceHolder;

/**
 * Created by pedja on 14.6.15..
 */
public class App extends Application
{
    private static App app = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;

        //Fabric.with(this, new Crashlytics());

        TypefaceHolder.getInstance().setDefaultFont("Roboto-Regular.ttf", TypefaceHolder.Source.asset, getApplicationContext());
    }

    public static App get()
    {
        return app;
    }
}
