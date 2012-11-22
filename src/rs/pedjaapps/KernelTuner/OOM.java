package rs.pedjaapps.KernelTuner;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.util.*;
import rs.pedjaapps.KernelTuner.*;

public class OOM extends Activity
{

SeekBar foregroundSeek;
SeekBar visibleSeek;
SeekBar secondarySeek;
SeekBar hiddenSeek;
SeekBar contentSeek;
SeekBar emptySeek;
TextView foregroundText;
TextView visibleText;
TextView secondaryText;
TextView hiddenText;
TextView contentText;
TextView emptyText;
List<String> oom = CPUInfo.oom();


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oom);

		int foreground = Integer.parseInt(oom.get(0))*4/1024;
		
		foregroundSeek = (SeekBar)findViewById(R.id.foregroundSeek);
		foregroundText = (TextView)findViewById(R.id.foregroundTest);
		visibleSeek = (SeekBar)findViewById(R.id.visibleSeek);
		visibleText = (TextView)findViewById(R.id.visibleText);
		secondarySeek = (SeekBar)findViewById(R.id.secondarySeek);
		secondaryText = (TextView)findViewById(R.id.secondaryText);
		hiddenSeek = (SeekBar)findViewById(R.id.hiddenSeek);
		hiddenText = (TextView)findViewById(R.id.hiddenText);
		contentSeek = (SeekBar)findViewById(R.id.contentSeek);
		contentText = (TextView)findViewById(R.id.contentText);
		emptySeek = (SeekBar)findViewById(R.id.emptySeek);
		emptyText = (TextView)findViewById(R.id.emptyText);
		
		
		foregroundSeek.setProgress(foreground);
		foregroundText.setText(String.valueOf(foreground)+"MB");
	    foregroundSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				public void onProgressChanged(SeekBar p1, int pos, boolean p3)
				{
				      foregroundText.setText(String.valueOf(pos)+"MB");
					
				}

				public void onStartTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}

				public void onStopTrackingTouch(SeekBar p1)
				{
					// TODO: Implement this method
				}

			
		});

	}

	


	
}
