/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.receiver;

import android.appwidget.*;
import android.content.*;
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
