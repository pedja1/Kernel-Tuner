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
package rs.pedjaapps.KernelTuner.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.ChangelogEntry;
import rs.pedjaapps.KernelTuner.helpers.ChangelogAdapter;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.net.Uri;

public class Changelog extends SherlockActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = prefs.getString("theme", "light");
		setTheme(Tools.getPreferedTheme(theme));
		setContentView(R.layout.changelog);

		ListView mListView = (ListView) findViewById(R.id.list);
		ChangelogAdapter mAdapter = new ChangelogAdapter(this, R.layout.changelog_row);
		mListView.setAdapter(mAdapter);

		for (final ChangelogEntry entry : getEntries())
		{
			mAdapter.add(entry);
		}
	}

	private static List<ChangelogEntry> getEntries()
	{

		final List<ChangelogEntry> entries = new ArrayList<ChangelogEntry>();

		entries.add(new ChangelogEntry(true, null, 0, "4.4.2"));
		entries.add(new ChangelogEntry(false, "Main Screen as popup option(Like in new Android Tuner)", 0, ""));
		entries.add(new ChangelogEntry(false, "Option to disable toast notifications when applying profiles (requested by user)", 0, ""));
		entries.add(new ChangelogEntry(false, "Option to hide or disable unsupported options", 0, ""));
		entries.add(new ChangelogEntry(false, "Option to hide \"panels\" in main screen (toggles, temp, CPU info)", 0, ""));
		entries.add(new ChangelogEntry(false, "New theme: MIUI", 0, ""));
		entries.add(new ChangelogEntry(false, "New theme: Sense 5", 0, ""));
		entries.add(new ChangelogEntry(false, "Option to select custom refresh rate of CPU load and frequency", 0, ""));
		entries.add(new ChangelogEntry(false, "Fixed BuildProp Editor FC when there are no backups", 1, ""));
		entries.add(new ChangelogEntry(false, "New Changelog screen", 0, ""));
		entries.add(new ChangelogEntry(false, "Superuser permission for new Superuser app(ClockworkMod)", 0, ""));
		
		
		return entries;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,0,0,"Full Changelog").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0,1,1,"OK").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
		com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, KernelTuner.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

				return true;
			case 0:
				Uri uri = Uri.parse("http://forum.xda-developers.com/showpost.php?p=27603190&postcount=2");
				Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent2);
				return true;
			case 1:
				finish();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}
	
}
