package rs.pedjaapps.linpack;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.ui.AbsActivity;


public class LinpackMainActivity extends AbsActivity implements Runnable
{

    TextView mflopsTextView;
    TextView nresTextView;
    TextView timeTextView;
    TextView precisionTextView;
    ListView resultsList;
    ResultsListAdapter adapter;
    DatabaseHandler db;
    Button start_single;
    Handler linpackHandler;
    Handler uiHandler;

    private long startTime;

    static final DecimalFormat mflopsFormat = new DecimalFormat("0.000");
    static final DecimalFormat nResFormat = new DecimalFormat("0.00");
	static final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    static
	{
		dfs.setDecimalSeparator('.');
		mflopsFormat.setDecimalFormatSymbols(dfs);
		nResFormat.setDecimalFormatSymbols(dfs);
	}
    SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        HandlerThread thread = new HandlerThread("linpack");
        thread.start();
        linpackHandler = new Handler(thread.getLooper());
        uiHandler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.linpack_main);

        mflopsTextView = (TextView) findViewById(R.id.mflops);
        nresTextView = (TextView) findViewById(R.id.nres);
        timeTextView = (TextView) findViewById(R.id.time);
        precisionTextView = (TextView) findViewById(R.id.precision);

        start_single = (Button) findViewById(R.id.start_single);
        start_single.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View p1)
            {
                mflopsTextView.setText(R.string.running_benchmark);
                nresTextView.setText("0");
                timeTextView.setText("0");
                precisionTextView.setText("0");
                start_single.setEnabled(false);
                startLinpack();
            }
        });
        resultsList = (ListView) findViewById(R.id.list);
        adapter = new ResultsListAdapter(this, R.layout.linpack_results_row);
        resultsList.setAdapter(adapter);
        db = new DatabaseHandler(this);
        populateList();
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(LinpackMainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.linpack_result_dialog_layout, null);
                TextView mflopsTextView = (TextView) view.findViewById(R.id.mflops);
                TextView nresTextView = (TextView) view.findViewById(R.id.nres);
                TextView timeTextView = (TextView) view.findViewById(R.id.time);
                TextView precisionTextView = (TextView) view.findViewById(R.id.precision);
                Result e = db.getResultByDate(adapter.getItem(position).date);

                mflopsTextView.setText(e.mflops + "");
                if (e.mflops < 30)
                {
                    mflopsTextView.setTextColor(Color.RED);
                }
                else
                {
                    mflopsTextView.setTextColor(Color.GREEN);
                }
                nresTextView.setText(e.nres + "");
                if (e.nres > 5)
                {
                    nresTextView.setTextColor(Color.YELLOW);
                }
                else if (e.nres > 10)
                {
                    nresTextView.setTextColor(Color.RED);
                }
                else
                {
                    nresTextView.setTextColor(Color.GREEN);
                }
                timeTextView.setText(e.time + "s");
                precisionTextView.setText("" + e.precision);

                builder.setNegativeButton(getResources().getString(R.string.close), null);

                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private void startLinpack()
    {
        linpackHandler.post(this);
    }

    private void populateList()
    {
        adapter.clear();
        List<Result> results = db.getAllResults();
        Collections.reverse(results);
        for(Result r : results)
        {
            adapter.add(r);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void run()
    {
        startTime = System.currentTimeMillis();
        final Result result = runLinpack(Result.class);
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                result.time = System.currentTimeMillis() - startTime;
                startTime = 0;
                result.date = new Date();
                result.mflops = Double.parseDouble(mflopsFormat.format(result.mflops));
                result.nres = Double.parseDouble(nResFormat.format(result.nres));

                db.addResult(result);

                mflopsTextView.setText(result.mflops + "");
                mflopsTextView.setTextColor(result.mflops < 200 ? Color.RED : Color.GREEN);
                nresTextView.setText(result.nres + "");
                if(result.nres > 5)
                {
                    nresTextView.setTextColor(Color.YELLOW);
                }
                else if(result.nres > 10)
                {
                    nresTextView.setTextColor(Color.RED);
                }
                else
                {
                    nresTextView.setTextColor(Color.GREEN);
                }
                timeTextView.setText(result.time / 1000 + "s");
                precisionTextView.setText(result.precision + "");
                populateList();
                start_single.setEnabled(true);
            }
        });
    }

    public native Result runLinpack(Class mClass);

    static
    {
        System.loadLibrary("linpack-jni");
    }
}
