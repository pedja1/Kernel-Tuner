/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.R;


public class CompatibilityCheck extends Activity
{

	private int count = 0;
	private boolean saf;
	private boolean mp;
	private boolean td;
	private boolean vs;
	private boolean fs;
	private boolean fc;
	private boolean tis;
	private boolean uv;
	private boolean cd;
	private boolean lt;
	private boolean bl;
	private boolean g3d;
	private boolean g2d;
	private boolean s2w;
	private boolean sdc;
	private boolean sh;

	SharedPreferences sharedPrefs;

	private class exec extends AsyncTask<Void, Integer, Void>
	{


		@Override
		protected Void doInBackground(Void... args)
		{
				if(new File(Constants.CPU0_FREQS).exists())
				{
				count = count + 1;
				saf = true;
				}
				else{
					saf = false;
				}
				publishProgress(3);
				
		       if(new File(Constants.VOLTAGE_PATH).exists())
			   {
				count = count + 1;
				uv = true;
			   }
			   else if(new File(Constants.VOLTAGE_PATH_TEGRA_3).exists()){
				   count = count + 1;
				   uv = true;
			   }
			   else{
				   uv = false;
			   }
		    	publishProgress(4);
				
	        if(new File(Constants.TIMES_IN_STATE_CPU0).exists())
			{
				count = count + 1;
				tis = true;
			}
			else{
				tis = false;
			}
			publishProgress(5);
		

				if(new File(Constants.NOTIF_LED).exists())
			    {
				count = count + 1;
				lt = true;
				}
				else{
					lt = false;
				}
			
			publishProgress(6);
		
			if(new File(Constants.BUTTONS_LIGHT).exists())
			{
				count = count + 1;
				bl = true;
			}
			else if(new File(Constants.BUTTONS_LIGHT_2).exists()){
				count = count + 1;
				bl = true;
			}
			else{
				bl = false;
			}
			publishProgress(7);
		
				if( new File(Constants.GPU_3D).exists())
			    {
				count = count + 1;
				g3d = true;
			    }
				else{
					g3d = false;
				}
			publishProgress(8);
				if(new File(Constants.GPU_2D).exists())
			    {
				count = count + 1;
				g2d = true;
			    }
				else{
					g2d = false;
				}
			publishProgress(9);
			

				if(new File(Constants.FCHARGE).exists())
			    {
				count = count + 1;
				fc = true;
			    }
				else{
					fc = false;
				}
		
			publishProgress(10);
			
				if(new File(Constants.VSYNC).exists())
				{
				count = count + 1;
				vs = true;
			    }
				else
				{
				vs = false;
				}
			publishProgress(11);

		
				 if( new File(Constants.CDEPTH).exists())
				 {
				count = count + 1;
				cd = true;
			}
			else{
				cd = false;
			}
			publishProgress(12);
		

			
			try
			{

				File myFile = new File("/sys/android_touch/sweep2wake");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				s2w = true;
				fIn.close();
			}
			catch (IOException e)
			{
				s2w = false;
			}
			publishProgress(13);
			

				if(new File(Constants.THERMAL_LOW_FREQ).exists())
				{
				count = count + 1;
				td = true;
			}
			else{
				td = false;
			}
			publishProgress(14);
			
				if( new File(Constants.MPDECISION).exists())
				{
				count = count + 1;
				mp = true;
			}
			else{
				mp = false;
			}
			publishProgress(15);
			

				if(new File(Constants.READ_AHEAD_KB).exists())
				{
				count = count + 1;
				sdc = true;
			}
			else{
				sdc = false;
			}
			publishProgress(16);

		

				if(new File(Constants.SCHEDULER).exists())
				{
				count = count + 1;
				sh = true;
				}
				else{
					sh = false;
				}

			publishProgress(17);

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{

			super.onProgressUpdate();
			
			LinearLayout ll1 = (LinearLayout) findViewById(R.id.ll1);
			LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll2);
			LinearLayout ll3 = (LinearLayout) findViewById(R.id.ll3);
			LinearLayout ll4 = (LinearLayout) findViewById(R.id.ll4);
			LinearLayout ll5 = (LinearLayout) findViewById(R.id.ll5);
			LinearLayout ll6 = (LinearLayout) findViewById(R.id.ll6);
			LinearLayout ll7 = (LinearLayout) findViewById(R.id.ll7);
			LinearLayout ll8 = (LinearLayout) findViewById(R.id.ll8);
			LinearLayout ll9 = (LinearLayout) findViewById(R.id.ll9);
			LinearLayout ll10 = (LinearLayout) findViewById(R.id.ll10);
		//	LinearLayout ll11 = (LinearLayout) findViewById(R.id.ll11);
			LinearLayout ll12 = (LinearLayout) findViewById(R.id.ll12);
			LinearLayout ll13 = (LinearLayout) findViewById(R.id.ll13);
			LinearLayout ll14 = (LinearLayout) findViewById(R.id.ll14);
			LinearLayout ll15 = (LinearLayout) findViewById(R.id.ll15);
			LinearLayout ll16 = (LinearLayout) findViewById(R.id.ll16);
			TextView saftv = (TextView) findViewById(R.id.textView2);
			TextView uvtv = (TextView) findViewById(R.id.textView4);
			TextView tistv = (TextView) findViewById(R.id.textView6);
			TextView lttv = (TextView) findViewById(R.id.textView8);
			TextView bltv = (TextView) findViewById(R.id.textView10);
			TextView g3dtv = (TextView) findViewById(R.id.textView12);
			TextView g2dtv = (TextView) findViewById(R.id.textView14);
			TextView fctv = (TextView) findViewById(R.id.textView16);
			TextView vstv = (TextView) findViewById(R.id.textView18);
			TextView cdtv = (TextView) findViewById(R.id.textView20);
		//	TextView fstv = (TextView) findViewById(R.id.textView22);
			TextView s2wtv = (TextView) findViewById(R.id.textView24);
			TextView tdtv = (TextView) findViewById(R.id.textView26);
			TextView mptv = (TextView) findViewById(R.id.textView28);
			TextView sdctv = (TextView) findViewById(R.id.textView30);
			TextView shtv = (TextView) findViewById(R.id.textView32);

			if (values[0] == 3)
			{
				ll1.setVisibility(View.VISIBLE);
				if (saf == true)
				{
					saftv.setText("[OK]");
					saftv.setTextColor(Color.GREEN);
				}
				else
				{
					saftv.setText("[Not Found]");
					saftv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 4)
			{
				ll2.setVisibility(View.VISIBLE);
				if (uv == true)
				{
					uvtv.setText("[OK]");
					uvtv.setTextColor(Color.GREEN);
				}
				else
				{
					uvtv.setText("[Not Found]");
					uvtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 5)
			{
				ll3.setVisibility(View.VISIBLE);
				if (tis == true)
				{
					tistv.setText("[OK]");
					tistv.setTextColor(Color.GREEN);
				}
				else
				{
					tistv.setText("[Not Found]");
					tistv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 6)
			{
				ll4.setVisibility(View.VISIBLE);
				if (lt == true)
				{
					lttv.setText("[OK]");
					lttv.setTextColor(Color.GREEN);
				}
				else
				{
					lttv.setText("[Not Found]");
					lttv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 7)
			{
				ll5.setVisibility(View.VISIBLE);
				if (bl == true)
				{
					bltv.setText("[OK]");
					bltv.setTextColor(Color.GREEN);
				}
				else
				{
					bltv.setText("[Not Found]");
					bltv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 8)
			{
				ll6.setVisibility(View.VISIBLE);
				if (g3d == true)
				{
					g3dtv.setText("[OK]");
					g3dtv.setTextColor(Color.GREEN);
				}
				else
				{
					g3dtv.setText("[Not Found]");
					g3dtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 9)
			{
				ll7.setVisibility(View.VISIBLE);
				if (g2d == true)
				{
					g2dtv.setText("[OK]");
					g2dtv.setTextColor(Color.GREEN);
				}
				else
				{
					g2dtv.setText("[Not Found]");
					g2dtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 10)
			{
				ll8.setVisibility(View.VISIBLE);
				if (fc == true)
				{
					fctv.setText("[OK]");
					fctv.setTextColor(Color.GREEN);
				}
				else
				{
					fctv.setText("[Not Found]");
					fctv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 11)
			{
				ll9.setVisibility(View.VISIBLE);
				if (vs == true)
				{
					vstv.setText("[OK]");
					vstv.setTextColor(Color.GREEN);
				}
				else
				{
					vstv.setText("[Not Found]");
					vstv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 12)
			{
				ll10.setVisibility(View.VISIBLE);
				if (cd == true)
				{
					cdtv.setText("[OK]");
					cdtv.setTextColor(Color.GREEN);
				}
				else
				{
					cdtv.setText("[Not Found]");
					cdtv.setTextColor(Color.RED);
				}
			}
		/*	if (values[0] == 13)
			{
				ll11.setVisibility(View.VISIBLE);
				if (fs == true)
				{
					fstv.setText("[OK]");
					fstv.setTextColor(Color.GREEN);
				}
				else
				{
					fstv.setText("[Not Found]");
					fstv.setTextColor(Color.RED);
				}
			}*/
			if (values[0] == 13)
			{
				ll12.setVisibility(View.VISIBLE);
				if (s2w == true)
				{
					s2wtv.setText("[OK]");
					s2wtv.setTextColor(Color.GREEN);
				}
				else
				{
					s2wtv.setText("[Not Found]");
					s2wtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 14)
			{
				ll13.setVisibility(View.VISIBLE);
				if (td == true)
				{
					tdtv.setText("[OK]");
					tdtv.setTextColor(Color.GREEN);
				}
				else
				{
					tdtv.setText("[Not Found]");
					tdtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 15)
			{
				ll14.setVisibility(View.VISIBLE);
				if (mp == true)
				{
					mptv.setText("[OK]");
					mptv.setTextColor(Color.GREEN);
				}
				else
				{
					mptv.setText("[Not Found]");
					mptv.setTextColor(Color.RED);
				}
			}

			if (values[0] == 16)
			{
				ll15.setVisibility(View.VISIBLE);
				if (sdc == true)
				{
					sdctv.setText("[OK]");
					sdctv.setTextColor(Color.GREEN);
				}
				else
				{
					sdctv.setText("[Not Found]");
					sdctv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 17)
			{
				ll16.setVisibility(View.VISIBLE);
				if (sh == true)
				{
					shtv.setText("[OK]");
					shtv.setTextColor(Color.GREEN);
				}
				else
				{
					shtv.setText("[Not Found]");
					shtv.setTextColor(Color.RED);
				}
			}

		}

		@Override
		protected void onPostExecute(Void result)
		{
			TextView res = (TextView) findViewById(R.id.textView34);
			int cn = count * 100 / 15;
			res.setText(String.valueOf(cn) + "%");
			if (cn < 30)
			{
				res.setTextColor(Color.RED);
			}
			else if (cn > 30 && cn < 50)
			{
				res.setTextColor(Color.MAGENTA);
			}
			else if (cn > 50 && cn < 70)
			{
				res.setTextColor(Color.YELLOW);
			}
			else if (cn > 70 && cn < 90)
			{
				res.setTextColor(Color.BLUE);
			}
			else if (cn > 90)
			{
				res.setTextColor(Color.GREEN);
			}
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
			LinearLayout resultll = (LinearLayout) findViewById(R.id.resultll);
			resultll.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
			Button ok = (Button) findViewById(R.id.button1);
			ok.setVisibility(View.VISIBLE);


		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.check);
		new exec().execute();
		final Button ok = (Button) findViewById(R.id.button1);
		ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0)
				{
					
					finish();

				}
			});

	}
}
