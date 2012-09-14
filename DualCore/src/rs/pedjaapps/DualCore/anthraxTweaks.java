package rs.pedjaapps.DualCore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class anthraxTweaks extends Activity{


    	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.anthrax_tweaks);
	
  
	Button mpdecision = (Button)this.findViewById(R.id.button1);
	mpdecision.setOnClickListener(new OnClickListener(){
		
		public void onClick(View v) {
			
			Intent myIntent = new Intent(anthraxTweaks.this, mpdecision.class);
			anthraxTweaks.this.startActivity(myIntent);
			
		}
	});
	
	Button thermald = (Button)findViewById(R.id.button2);
	thermald.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
				AlertDialog alertDialog = new AlertDialog.Builder(
                        anthraxTweaks.this).create();

        alertDialog.setTitle("thermald");
 
        alertDialog.setMessage("Comming Soon");
 
        alertDialog.setIcon(R.drawable.icon);
 
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              
                }
        });
 
        alertDialog.show();
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
			}
			
	
	});
	
	Button other = (Button)findViewById(R.id.button3);
	other.setOnClickListener(new OnClickListener(){
		
		
		public void onClick(View v) {
			
				AlertDialog alertDialog = new AlertDialog.Builder(
                        anthraxTweaks.this).create();

        alertDialog.setTitle("Other");
 
        alertDialog.setMessage("Comming Soon");
 
        alertDialog.setIcon(R.drawable.icon);
 
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              
                }
        });
 
        alertDialog.show();
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
			}
			
	
	});
	
	
    	}
}
