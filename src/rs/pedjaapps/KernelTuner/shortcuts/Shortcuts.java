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
package rs.pedjaapps.KernelTuner.shortcuts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.KernelTuner.R;

public class Shortcuts extends Activity
{

ShortcutAdapter shortcutAdapter;
ListView shortcutListView;
        String[] titles = {"Reboot",
                "Reboot - Recovery",
                "Reboot - Bootloader",
                "CPU Tweaks",
                "Times in State", 
                "Voltage",
                "Governor Settings",
                "mpdecision",
                "thermald",
                "GPU",
                "Misc Tweaks",
                "Profiles",
                "Swap",
                "System Info",
                "Settings",
                "OOM",
				"SD Analyzer",
				"build.prop Editor",
				"SysCtl",
				"Task Manager"};
        String[] descs ={"Normal Reboot", 
                "Reboot device in recovery mode",
                "Reboot device in bootloader", 
                "Start CPU Tweaks",
                "View CPU Times in State", 
                "Change CPU Voltage Setting",
                "Change Governor Settings",
                "Manage mpdecision",
                "Manage thermald",
                "Overclock GPU",
                "Start Misc Tweaks",
                "Manage Settings Profiles",
                "Create and Manage Swap",
                "View System Information",
                "Change app Settings",
                "Out Of Memory Settings",
				"Analyze SD Card Content",
				"Edit Build properties",
				"System Control",
				"Manage Running Processes"};

        int[] icons = {R.drawable.reboot,
                R.drawable.reboot,
                R.drawable.reboot,
                R.drawable.ic_launcher, 
                R.drawable.times, 
                R.drawable.voltage, 
                R.drawable.dual,
                R.drawable.dual,
                R.drawable.temp,
                R.drawable.gpu,
                R.drawable.misc,
                R.drawable.profile,
                R.drawable.swap,
                R.drawable.info,
                R.drawable.misc,
                R.drawable.swap,
				R.drawable.sd,
				R.drawable.build,
				R.drawable.sysctl,
				R.drawable.tm};
        Class<?>[] classes = {RebootShortcut.class, 
                        RebootShortcut.class, 
                        RebootShortcut.class, 
                        CPUShortcut.class,
                        TISShortcut.class,
                        VoltageShortcut.class,
                        GovernorShortcut.class,
                        MpdecisionShortcut.class,
                        ThermaldShortcut.class,
                        GPUShortcut.class,
                        MiscShortcut.class,
                        ProfilesShortcut.class,
                        SwapShortcut.class,
                        InfoShortcut.class,
                        SettingsShortcut.class,
                        OOMShortcut.class,
						SDShortcut.class,
						BuildShortcut.class,
						SysShortcut.class,
						TmShortcut.class};

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
				final Context c = this;
                setContentView(R.layout.shortcuts_list);
                shortcutListView = (ListView) findViewById(R.id.list);
                shortcutAdapter = new ShortcutAdapter(c, R.layout.shortcut_list_item);
                shortcutListView.setAdapter(shortcutAdapter);

                for (final ShortcutEntry entry : getShortcutEntries())
                {
                        shortcutAdapter.add(entry);
                }
                shortcutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
                                {
                                                Intent myIntent = new Intent(c, classes[position]);
                                                switch(position){
                                                case 0:
                                                        myIntent.putExtra("reboot", "");
                                                        break;
                                                case 1:
                                                        myIntent.putExtra("reboot", "recovery");
                                                        break;
                                                case 2:
                                                        myIntent.putExtra("reboot", "bootloader");
                                                }
                                                Shortcuts.this.startActivity(myIntent);
                                        
                                } 
                        });
        }
        
        private List<ShortcutEntry> getShortcutEntries()
        {
                final List<ShortcutEntry> entries = new ArrayList<ShortcutEntry>();
                for(int i =0; i < titles.length; i++){
                        entries.add(new ShortcutEntry(titles[i],descs[i],icons[i]));
                }
                return entries;
        }
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.shortcuts_options_menu, menu);
                return super.onCreateOptionsMenu(menu);
        }
        @Override
        public boolean onPrepareOptionsMenu (Menu menu) {

                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.done)
                {
                        finish();
                }
                return super.onOptionsItemSelected(item);
                }
}
