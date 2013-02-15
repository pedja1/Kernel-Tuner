package rs.pedjaapps.KernelTuner.ui;



import android.os.Bundle;
import android.webkit.WebView;
import org.holoeverywhere.app.Activity;
import rs.pedjaapps.KernelTuner.R;

public class Changelog extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.changelog);

		WebView myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.loadUrl("http://kerneltuner.pedjaapps.in.rs/ktuner/changelog.html");
	}


}
