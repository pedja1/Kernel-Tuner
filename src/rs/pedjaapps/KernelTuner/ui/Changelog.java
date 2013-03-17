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

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.ChangelogEntry;
import rs.pedjaapps.KernelTuner.helpers.ChangelogAdapter;

public class Changelog extends SherlockActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = prefs.getString("theme", "light");
		if (theme.equals("light")) 
		{
			setTheme(R.style.Theme_Sherlock_Light);
		} 
		else if (theme.equals("dark")) 
		{
			setTheme(R.style.Theme_Sherlock);
		} 
		else if (theme.equals("light_dark_action_bar")) 
		{
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		}
		else if (theme.equals("miui_light")) 
		{
			setTheme(R.style.Theme_Miui_Light);
		} 
		else if (theme.equals("miui_dark")) 
		{
			setTheme(R.style.Theme_Miui_Dark);
		} 
		else if (theme.equals("sense5")) 
		{
			setTheme(R.style.Theme_Sense5);
		}
		else if (theme.equals("sense5_light")) 
		{
			setTheme(R.style.Theme_Light_Sense5);
		}
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
		
		
		return entries;
	}
	
}
