package rs.pedjaapps.KernelTuner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import rs.pedjaapps.KernelTuner.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class changelog extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changelog);

		TextView anthrax = (TextView) findViewById(R.id.textView12);
		if (anthrax != null) {
			anthrax.setMovementMethod(LinkMovementMethod.getInstance());
		}
		checkAnthrax();

	}

	public void checkAnthrax() {
		String anthrax = null;
		try {
			File myFile = new File("/proc/version");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(
					fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			anthrax = aBuffer;
			myReader.close();

		} catch (Exception e) {
			anthrax = "notfound";
		}

		TextView anth = (TextView) findViewById(R.id.textView12);
		int intIndex = anthrax.indexOf("anthrax");
		if (intIndex == -1) {
			System.out.println("not found");

			anth.setVisibility(View.GONE);
		} else {
			System.out.println("Found anthrax at index " + intIndex);
			anth.setVisibility(View.VISIBLE);

		}
	}
}
