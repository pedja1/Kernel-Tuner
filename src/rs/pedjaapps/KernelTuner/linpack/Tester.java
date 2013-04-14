/*
 * Copyright (C) 2010 0xlab - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rs.pedjaapps.KernelTuner.linpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class Tester extends Activity {

    TextView mTextView;
    Bundle mInfo[];
    public final static String MFLOPS = "MFLOPS";
    public final static String RESIDN = "RESIDN";
    public final static String TIME   = "TIME";
    public final static String EPS    = "EPS";

	private String TAG;
    public final static String PACKAGE = "org.zeroxlab.zeroxbenchmark";
    int mRound;
    int mNow;
    int mIndex;

    protected long mTesterStart = 0;
    protected long mTesterEnd   = 0;

    protected String mSourceTag = "unknown";
    private boolean mNextRound = true;

    protected boolean mDropTouchEvent     = true;
    protected boolean mDropTrackballEvent = true;
	
    protected String getTag() {
        return "Arithmetic";
    }

    protected int sleepBeforeStart() {
        return 1000;
    }

    protected int sleepBetweenRound() {
        return 200;
    }

    protected void oneRound() {
        LinpackLoop.main(mInfo[mNow - 1]);
        decreaseCounter();
    }

    
    protected boolean saveResult(Intent intent) {
        final Bundle result = new Bundle();
        average(result, mInfo);
    
        //intent.putExtra(CaseArithmetic.LIN_RESULT, result);
		runOnUiThread(new Runnable(){
				public void run() {
		mTextView.setText(bundleToString(result));
		}});
        return true;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		TAG = getTag();


		mRound = 80;
		mIndex = -1;

        mNow   = mRound;
        int length = mRound;
        mInfo = new Bundle[length];
        for (int i = 0; i < length; i++) {
            mInfo[i] = new Bundle();
        }

        mTextView = new TextView(this);
        mTextView.setText("Running benchmark....");
        //mTextView.setTextSize(mTextView.getTextSize() + 5);
        setContentView(mTextView);
        startTester();
    }

    public static void average(Bundle result, Bundle[] list) {

        if (result == null) {
            result = new Bundle();
        }

        if (list == null) {
            Log.i("Arithmetic", "Array is null");
            return;
        }

        int length = list.length;
        double mflops_total  = 0.0;
        double residn_total  = 0.0;
        double time_total    = 0.0;
        double eps_total     = 0.0;

        for (int i = 0; i < length; i ++) {
            Bundle info = list[i];

            if (info == null) {
                Log.i("Arithmetic", "one item of array is null!");
                return;
            }

            mflops_total  += info.getDouble(MFLOPS);
            residn_total  += info.getDouble(RESIDN);
            time_total    += info.getDouble(TIME);
            eps_total     += info.getDouble(EPS);
        }

        result.putDouble(MFLOPS, mflops_total / length);
        result.putDouble(RESIDN, residn_total / length);
        result.putDouble(TIME, time_total / length);
        result.putDouble(EPS, eps_total  / length);
    }

    public static String bundleToString(Bundle bundle) {
        String result = "";
        result += "Mflops/s :" + bundle.getDouble(MFLOPS, 0.0);
        /* the time result is too small to calculate average. (0.0 ~ 0.1), drop it*/
        //result += "\nTime     :" + bundle.getDouble(TIME, 0.0);
        result += "\nNorm Res :" + bundle.getDouble(RESIDN, 0.0);
        result += "\nPrecision:" + bundle.getDouble(EPS, 0.0);

        return result;
    }
	
	@Override
    protected void onPause() {
        super.onPause();
		if(isTesterFinished()==false){
        interruptTester();
		}
    }

    /* drop the annoying event */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDropTouchEvent) {
            return false;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        if (mDropTrackballEvent) {
            return false;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    protected void startTester() {
        TesterThread thread = new TesterThread(sleepBeforeStart(), sleepBetweenRound());
        thread.start();
    }

    public void interruptTester() {
        mNow = 0;
        finish();
    }

    /**
     * Call this method if you finish your testing.
     *
     * @param start The starting time of testing round
     * @param end The ending time of testing round
     */
    public void finishTester(long start, long end) {
        mTesterStart = start;
        mTesterEnd   = end;
        Intent intent = new Intent();
        if (mSourceTag == null || mSourceTag.equals("")) {
			// Case.putSource(intent, "unknown");
        } else {
            //Case.putSource(intent, mSourceTag);
        }

		// Case.putIndex(intent, mIndex);
        saveResult(intent);

		// setResult(0, intent);
		//  finish();
    }

   

    public void resetCounter() {
        mNow = mRound;
    }

    public void decreaseCounter() {
        /*
		 if (mNow == mRound) {
		 mTesterStart = SystemClock.uptimeMillis();
		 } else if (mNow == 1) {
		 mTesterEnd = SystemClock.uptimeMillis();
		 }
		 */
        mNow = mNow - 1;
        mNextRound = true;
    }

    public boolean isTesterFinished() {
        return (mNow <= 0);
    }

    class TesterThread extends Thread {
        int mSleepingStart;
        int mSleepingTime;
        TesterThread(int sleepStart, int sleepPeriod) {
            mSleepingStart = sleepStart;
            mSleepingTime  = sleepPeriod;
        }

        private void lazyLoop() throws Exception {
            while (!isTesterFinished()) {
                if (mNextRound) {
                    mNextRound = false;
                    oneRound();
                } else {
                    sleep(mSleepingTime);
                    // TODO: 
                    // Benchmarks that calculates frequencies (e.g. fps) should be time,
                    // for example, GL cases should run for a fixed time, and calculate 
                    // # of frames rendered, instead of periodically checking if fixed 
                    // # of frames had been rendered (which can hurt performance).
                }
            }
        }

        private void nervousLoop() throws Exception {
            while (!isTesterFinished()) {
                oneRound();
            }
        }

        private void sleepLoop() throws Exception {
            while (!isTesterFinished()) {
                oneRound();
                sleep(mSleepingTime);
            }
        }

        public void run() {
            try {
                sleep(mSleepingStart);

                long start = SystemClock.uptimeMillis();

                lazyLoop();

                long end = SystemClock.uptimeMillis();
                finishTester(start, end);
            } catch (Exception e) {
				e.printStackTrace();
            }
        }
    }

}
