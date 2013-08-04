package rs.pedjaapps.KernelTuner.ui;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.MainApp;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FrequencyCollection;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashActivity extends Activity
{

	boolean isPreLoadFinished = false;
	boolean isActualPreLoadFinished = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Log.e(Constants.LOG_TAG, ">>> APP START");
		PreLoad preLoad = new PreLoad();
		preLoad.execute();
		ActualPreLoad actualPreLoad = new ActualPreLoad();
		actualPreLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v(Constants.LOG_TAG, "SplashActivity :: onResume");

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(Constants.LOG_TAG, "SplashActivity :: onPause");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}

	private class PreLoad extends AsyncTask<String, Integer, Void>
	{
		ProgressBar pb;

		@Override
		protected Void doInBackground(String... params)
		{
			long splashStartTime = System.currentTimeMillis();
			double currentProgress = 0;
			long splashElapsedTime = 0;
			int minimumStayTime = 3000;
			while ((splashElapsedTime) < minimumStayTime)
			{
				splashElapsedTime = System.currentTimeMillis()
						- splashStartTime;
				currentProgress = 100.0 * splashElapsedTime / minimumStayTime;
				publishProgress((int) currentProgress);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			isPreLoadFinished = true;
			if (isPreLoadFinished && isActualPreLoadFinished)
			{
				Intent intent = new Intent(SplashActivity.this,
						KernelTuner.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);

				finish();
			}
		}

		@Override
		protected void onPreExecute()
		{
			pb = (ProgressBar) findViewById(R.id.progressBar1);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			if (pb != null)
				pb.setProgress(progress[0]);
		}

	}

	private class ActualPreLoad extends AsyncTask<String, Integer, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			FrequencyCollection collection = FrequencyCollection.getInstance();
			collection.getAllFrequencies();
			MainApp.getInstance().setPrefs(
					PreferenceManager
							.getDefaultSharedPreferences(SplashActivity.this));
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			isActualPreLoadFinished = true;
			if (isPreLoadFinished && isActualPreLoadFinished)
			{
				Intent intent = new Intent(SplashActivity.this,
						KernelTuner.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);

				finish();
			}
		}

	}

}
