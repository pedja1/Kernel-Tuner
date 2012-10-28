package rs.pedjaapps.KernelTuner;


import android.os.*;
import android.preference.*;
import rs.pedjaapps.KernelTuner.*;


public class Preferences extends PreferenceActivity
{


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{        
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);        

		
	}

}
