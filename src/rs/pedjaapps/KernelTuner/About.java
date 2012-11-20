package rs.pedjaapps.KernelTuner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity
{
	
	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		
		TextView	official = (TextView) findViewById(R.id.textView8);
		TextView	xda = (TextView) findViewById(R.id.textView6);
		TextView versiontext = (TextView)findViewById(R.id.textView3);
		
		
		if (xda != null)
		{
			xda.setMovementMethod(LinkMovementMethod.getInstance());
		}

		
		if (official != null)
		{
			official.setMovementMethod(LinkMovementMethod.getInstance());
		}

		
		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String	version = pInfo.versionName;
			
			versiontext.setText(getResources().getString(R.string.version) + version);
		}
		catch (PackageManager.NameNotFoundException e)
		{}


	}

	
	
}
