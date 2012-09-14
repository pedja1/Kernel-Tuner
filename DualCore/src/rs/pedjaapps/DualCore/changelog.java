package rs.pedjaapps.DualCore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.*;

import java.util.*;
import android.widget.*;

public class changelog extends Activity{


    	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.changelog);
	
	TextView	anthrax = (TextView) findViewById(R.id.textView12);
	if (anthrax != null) {
		anthrax.setMovementMethod(LinkMovementMethod.getInstance());
	}
	checkAnthrax();
  
		
}
	public void checkAnthrax(){
		String anthrax = null;
		try {
			File myFile = new File("/proc/version");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			anthrax = aBuffer;
			myReader.close();

		} catch (Exception e) {
			anthrax="notfound";
		}


		TextView anth = (TextView)findViewById(R.id.textView12);
		int intIndex = anthrax.indexOf("anthrax");
		if(intIndex == - 1){
			System.out.println("not found");

			anth.setVisibility(View.GONE);
		}else{
			System.out.println("Found anthrax at index "
							   + intIndex);
			anth.setVisibility(View.VISIBLE);

		}
	}
}
