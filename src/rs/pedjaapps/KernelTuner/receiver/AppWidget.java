package rs.pedjaapps.KernelTuner.receiver;

import android.appwidget.*;
import android.content.*;
import rs.pedjaapps.KernelTuner.*;
import rs.pedjaapps.KernelTuner.services.*;

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
