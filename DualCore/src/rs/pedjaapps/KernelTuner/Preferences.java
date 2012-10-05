package rs.pedjaapps.KernelTuner;


import rs.pedjaapps.KernelTuner.R;
import android.os.Bundle;

import android.preference.PreferenceActivity;


public class Preferences extends PreferenceActivity {

	    
	    @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {        
	        super.onCreate(savedInstanceState);
	        
	        addPreferencesFromResource(R.xml.preferences);        
	    
	     
	    }
	    
	}
