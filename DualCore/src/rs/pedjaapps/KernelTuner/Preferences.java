package rs.pedjaapps.KernelTuner;


import rs.pedjaapps.KernelTuner.R;
import android.os.Bundle;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


public class Preferences extends PreferenceActivity {

	    
	    @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {        
	        super.onCreate(savedInstanceState);
	        
	        addPreferencesFromResource(R.xml.preferences);        
	    
	     
	    }
	    
	}
