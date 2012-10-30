package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import rs.pedjaapps.KernelTuner.*;

public class about extends Activity
{
	
	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		checkAnthrax();
		TextView	anthrax = (TextView) findViewById(R.id.textView5);
		if (anthrax != null)
		{
			anthrax.setMovementMethod(LinkMovementMethod.getInstance());
		}

		TextView	xda = (TextView) findViewById(R.id.textView6);
		if (xda != null)
		{
			xda.setMovementMethod(LinkMovementMethod.getInstance());
		}

		TextView	official = (TextView) findViewById(R.id.textView8);
		if (official != null)
		{
			official.setMovementMethod(LinkMovementMethod.getInstance());
		}

		
		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String	version = pInfo.versionName;
			TextView versiontext = (TextView)findViewById(R.id.textView3);
			versiontext.setText("version: " + version);
		}
		catch (PackageManager.NameNotFoundException e)
		{}


	}

	public void checkAnthrax()
	{
		String anthrax = null;
		try
		{
			File myFile = new File("/proc/version");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			anthrax = aBuffer;
			myReader.close();

		}
		catch (Exception e)
		{
			anthrax = "notfound";
		}


		TextView anth = (TextView)findViewById(R.id.textView5);
		int intIndex = anthrax.indexOf("anthrax");
		if (intIndex == - 1)
		{
			//System.out.println("not found");

			anth.setVisibility(View.GONE);
		}
		else
		{
			//System.out.println("Found anthrax at index "
			//				   + intIndex);
			anth.setVisibility(View.VISIBLE);

		}
	}
}
