package rs.pedjaapps.kerneltuner.ui;

import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.devadvance.circularseekbar.CircularSeekBar;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
import rs.pedjaapps.kerneltuner.utility.Utility;


public class EntropyActivity extends AbsActivity implements CircularSeekBar.OnCircularSeekBarChangeListener, Runnable
{
    public static final int REFRESH_INTERVAL = 1000;
    private TextView tvRead;
    private TextView tvWrite;
    TextView tvAvailable;
    TextView tvPoolSize;
    private int poolSize;
    
    private static final int[] opts = new int[]{64, 128, 256, 512, 1024, 2048, 3072, 4096};
    private Handler handler;
    private CircularSeekBar sbRead;
    private CircularSeekBar sbWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Crashlytics.start(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entropy);
        
        sbRead = (CircularSeekBar) findViewById(R.id.sbRead);
        sbWrite = (CircularSeekBar) findViewById(R.id.sbWrite);

        tvAvailable = (TextView) findViewById(R.id.tvAvailable);
        tvPoolSize = (TextView) findViewById(R.id.tvPoolSize);
        tvRead = (TextView) findViewById(R.id.tvRead);
        tvWrite = (TextView) findViewById(R.id.tvWrite);

        poolSize = IOHelper.getEntropyPoolSize();
        tvPoolSize.setText(poolSize + "");
        if (!Constants.ENTROPY_READ_THRESHOLD.exists() || !Constants.ENTROPY_WRITE_THRESHOLD.exists())
        {
            sbRead.setEnabled(false);
            sbWrite.setEnabled(false);

        }
        else
        {
            sbRead.setMax(opts.length - 1);
            sbWrite.setMax(opts.length - 1);
            int read = IOHelper.getEntropyReadThreshold();
            int write = IOHelper.getEntropyWriteThreshold();
            tvRead.setText(read + "");
            tvWrite.setText(write + "");
            sbRead.setProgress(Utility.getIndexForValue(opts, read));
            sbWrite.setProgress(Utility.getIndexForValue(opts, write));
            sbRead.setOnSeekBarChangeListener(this);
            sbWrite.setOnSeekBarChangeListener(this);
        }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (handler != null) handler.removeCallbacks(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (Constants.ENTROPY_AVAILABLE.exists())
        {
            if (handler == null)
            {
                handler = new Handler();
            }
            else
            {
                handler.removeCallbacks(this);
            }
            handler.postDelayed(this, REFRESH_INTERVAL);
        }
    }

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser)
    {
        switch (circularSeekBar.getId())
        {
            case R.id.sbRead:
                tvRead.setText(opts[progress] + "");
                break;
            case R.id.sbWrite:
                tvWrite.setText(opts[progress] + "");
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar)
    {
        switch (seekBar.getId())
        {
            case R.id.sbRead:
                RCommand.setEntropyReadThreshold(opts[seekBar.getProgress()]);
                break;
            case R.id.sbWrite:
                RCommand.setEntropyWriteThreshold(opts[seekBar.getProgress()]);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar)
    {

    }

    @Override
    public void run()
    {
        int available = IOHelper.getEntropyAvailable();
        tvAvailable.setText(Utility.calculatePercent(available, poolSize) + "% (" + available + ")");
        sbWrite.setProgress(Utility.getIndexForValue(opts, IOHelper.getEntropyWriteThreshold()));
        sbRead.setProgress(Utility.getIndexForValue(opts, IOHelper.getEntropyReadThreshold()));
        handler.postDelayed(this, REFRESH_INTERVAL);
    }
}
