/**
 * siir.es.adbWireless.adbWireless.java
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package siir.es.adbWireless;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.stericson.RootShell.RootShell;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.root.RootUtils;

public class adbWidgetProvider extends AppWidgetProvider
{

    private static String ACTION_CLICK = "siir.es.adbwireless.widget_update";
    
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		SharedPreferences settings = context.getSharedPreferences("wireless", 0);
        ADBWirelessActivity.mState = settings.getBoolean("mState", false);
        ADBWirelessActivity.wifiState = settings.getBoolean("wifiState", false);

        for (int appWidgetId : appWidgetIds)
        {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.adb_appwidget);
			
            Intent intent = new Intent(context, adbWidgetProvider.class);
            intent.setAction(ACTION_CLICK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
            if (ADBWirelessActivity.mState)
            {
                views.setImageViewResource(R.id.widgetButton, R.drawable.widgeton);
            }
            else
            {
                views.setImageViewResource(R.id.widgetButton, R.drawable.widget);
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_CLICK))
        {

            if (!RootShell.isAccessGiven())
            {
                Toast.makeText(context, R.string.no_root, Toast.LENGTH_LONG).show();
                return;
            }

            if (!Utils.checkWifiState(context))
            {
                ADBWirelessActivity.wifiState = false;
                Utils.saveWiFiState(context, ADBWirelessActivity.wifiState);

                if (Utils.prefsWiFiOn(context))
                {
                    Utils.enableWiFi(context, true);
                }
                else
                {
                    Toast.makeText(context, R.string.no_wifi, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            else
            {
                ADBWirelessActivity.wifiState = true;
                Utils.saveWiFiState(context, ADBWirelessActivity.wifiState);
            }

            SharedPreferences settings = context.getSharedPreferences("wireless", 0);
            ADBWirelessActivity.mState = settings.getBoolean("mState", false);
            ADBWirelessActivity.wifiState = settings.getBoolean("wifiState", false);

            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Utils.prefsHaptic(context))
            {
                vib.vibrate(45);
            }

            try
            {
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.adb_appwidget);
				
                if (ADBWirelessActivity.mState)
                {
                    views.setImageViewResource(R.id.widgetButton, R.drawable.widget);
                    ComponentName cn = new ComponentName(context, adbWidgetProvider.class);
                    AppWidgetManager.getInstance(context).updateAppWidget(cn, views);
                    Utils.adbStop(context);
                }
                else
                {
                    views.setImageViewResource(R.id.widgetButton, R.drawable.widgeton);
                    ComponentName cn = new ComponentName(context, adbWidgetProvider.class);
                    AppWidgetManager.getInstance(context).updateAppWidget(cn, views);
                    Toast.makeText(context, context.getString(R.string.widget_start) + " " + Utils.getWifiIp(context), Toast.LENGTH_LONG).show();
                    Utils.adbStart(context);
                }

            }
            catch (Exception e)
            {
				e.printStackTrace();
            }
        }
    }
}
