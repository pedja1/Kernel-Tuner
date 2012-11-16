package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.*;

public class Changelog extends Activity
{

	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.changelog);

		WebView myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.loadUrl("http://kerneltuner.pedjaapps.in.rs/ktuner/changelog.html");
	}


}
