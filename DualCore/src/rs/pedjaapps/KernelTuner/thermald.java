package rs.pedjaapps.KernelTuner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import java.util.List;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import rs.pedjaapps.KernelTuner.R;
import android.widget.*; 

public class thermald extends Activity {

public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);


setContentView(R.layout.thermald);

}


public void onPause() {
    super.onPause();
}


protected void onResume()
{
    
    
    super.onResume();
    
}




	}
	

