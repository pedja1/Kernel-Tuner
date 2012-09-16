package rs.pedjaapps.KernelTuner;
 
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
 
public class notFoundActivity extends Activity {
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.not_supported);
		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText("Problem processing time-in-states file");
		
	}
 
}
