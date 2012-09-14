package rs.pedjaapps.DualCore;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class AppWidget extends AppWidgetProvider {

  private static final String LOG = "d";

  Handler mHandler = new Handler();

  @Override
  public void onUpdate(final Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {

    Log.w(LOG, "onUpdate method called");
    // Get all ids
    ComponentName thisWidget = new ComponentName(context,
        AppWidget.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

    // Build the intent to call the service
    final Intent intent = new Intent(context.getApplicationContext(),
        WidgetUpdateService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
           	
                        	context.startService(intent);

  }
  
} 
