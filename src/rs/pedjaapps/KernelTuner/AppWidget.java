package rs.pedjaapps.KernelTuner;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AppWidget extends AppWidgetProvider
{

	@Override
	public void onUpdate(final Context context,
						 AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{

	
		ComponentName thisWidget = new ComponentName(context, AppWidget.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		final Intent intent = new Intent(context.getApplicationContext(),
										 WidgetUpdateService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		context.startService(intent);

	}

}
