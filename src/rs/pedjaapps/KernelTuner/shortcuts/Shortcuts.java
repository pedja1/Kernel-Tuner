package rs.pedjaapps.KernelTuner.shortcuts;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.ui.OOM;
import rs.pedjaapps.KernelTuner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
                "OOM"};
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
                "Create and manage Swap",
                "View System Information",
                "Change app Settings",
                "Out Of Memory Settings"};

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
                R.drawable.swap};
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
                        OOMShortcut.class};

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.shortcuts_list);
        shortcutListView = (ListView) findViewById(R.id.list);
                shortcutAdapter = new ShortcutAdapter(this, R.layout.shortcut_list_item);
                shortcutListView.setAdapter(shortcutAdapter);

                for (final ShortcutEntry entry : getShortcutEntries())
                {
                        shortcutAdapter.add(entry);
                }
                
        
                
                shortcutListView.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
                                {

                                        
                
                                                Intent myIntent = new Intent(Shortcuts.this, classes[position]);
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
