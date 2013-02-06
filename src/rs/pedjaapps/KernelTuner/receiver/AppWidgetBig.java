package rs.pedjaapps.KernelTuner.receiver;

import android.appwidget.*;
import android.content.*;
import rs.pedjaapps.KernelTuner.services.*;

public class AppWidgetBig extends AppWidgetProvider
{

	

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds)
	{

		
		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
													 AppWidgetBig.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),
								   WidgetUpdateServiceBig.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		// Update the widgets via the service
		context.startService(intent);
	}
}
