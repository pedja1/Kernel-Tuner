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
package rs.pedjaapps.kerneltuner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.BuildAdapter;
import rs.pedjaapps.kerneltuner.model.Build;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.Tools;


public class BuildpropEditor extends AbsActivity
{

    ListView bListView;
    BuildAdapter bAdapter;
    SharedPreferences preferences;
    List<Build> entries;

    Pattern buildPropertyLine = Pattern.compile("(\\S+)=(\\S+)");

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        entries = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.build);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bListView = (ListView) findViewById(R.id.list);
        bAdapter = new BuildAdapter(this, new ArrayList<Build>());
        bListView.setAdapter(bAdapter);

        bListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, final int pos, long is)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final Build build = bAdapter.getItem(pos);
                builder.setTitle(build.key);

                builder.setIcon(R.drawable.build);

                final EditText input = new EditText(v.getContext());
                input.setText(build.value);
                input.selectAll();
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.requestFocus();

                builder.setPositiveButton(getString(R.string.Change), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        build.value = input.getText().toString().trim();
                        saveBuildProp(build);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), null);
                builder.setView(input);

                AlertDialog alert = builder.create();

                alert.show();
            }
        });
        new GetBuildEntries().execute();
    }

    private void saveBuildProp(Build build)
    {
        try
        {
            Build buildFromAll = Build.getBuildByKey(entries, build.key);
            buildFromAll.value = build.value;
            StringBuilder builder = new StringBuilder();
            for(Build bld : entries)
            {
                if(bld.isProperty)
                {
                    builder.append(bld.key).append("=").append(bld.value);
                }
                else
                {
                    builder.append(bld.value);
                }
                builder.append("\n");
            }
            FileOutputStream fOut = openFileOutput("build.prop", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(builder.toString());
            osw.flush();
            osw.close();

            new RootUtils().exec(new RootUtils.CommandCallbackImpl()
             {
                 @Override
                 public void onComplete(RootUtils.Status status, String output)
                 {
                     System.out.println(output);
                     Toast.makeText(BuildpropEditor.this, getString(R.string.bprop_saved), Toast.LENGTH_LONG).show();
                     bAdapter.notifyDataSetChanged();
                 }
             }, "mount -o remount,rw /system",
                    "busybox cp -f /system/build.prop /system/build.prop.bk",
                    "busybox cp -f " + getFilesDir().getPath() + "/build.prop /system/build.prop",
                    "chmod 644 /system/build.prop", "mount -o remount,ro /system");

        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            Toast.makeText(BuildpropEditor.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private class GetBuildEntries extends AsyncTask<String, Build, Void>
    {
        String line;

        @Override
        protected Void doInBackground(String... args)
        {
            entries.clear();
            try
            {
                File myFile = new File("/system/build.prop");
                FileInputStream fIn = new FileInputStream(myFile);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn));
                while ((line = bufferedReader.readLine()) != null)
                {
                    Matcher matcher = buildPropertyLine.matcher(line);
                    Build build = new Build();
                    if (matcher.matches())
                    {
                        build.key = matcher.group(1);
                        build.value = matcher.group(2);
                        build.isProperty = true;
                    }
                    else
                    {
                        build.value = line;
                        build.isProperty = false;
                    }
                    entries.add(build);
                }
                bufferedReader.close();
            }
            catch (Exception e)
            {
                Log.e("Get build prop", "error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res)
        {
            for (Build build : entries)
            {
                if (build.isProperty) bAdapter.add(build);
            }
            bAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute()
        {
            bAdapter.clear();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.add)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 1, getString(R.string.backup)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, 2, getString(R.string.restore)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
            case 0:
                addDialog();
                return true;
            case 1:
                backup();
                return true;
            case 2:
                restore();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.add_new_entry));

        //builder.setMessage("Set new value!");

        builder.setIcon(R.drawable.build);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.build_add_layout, null);
        final EditText tvKey = (EditText) view.findViewById(R.id.key);
        final EditText tvValue = (EditText) view.findViewById(R.id.value);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new RootUtils().exec(new RootUtils.CommandCallbackImpl()
                {
                    @Override
                    public void onComplete(RootUtils.Status status, String output)
                    {
                        Build build = new Build();
                        build.key = tvKey.getText().toString().trim();
                        build.value = tvValue.getText().toString().trim();
                        build.isProperty = true;
                        entries.add(build);
                        bAdapter.clear();
                        for (Build bld : entries)
                        {
                            if (bld.isProperty) bAdapter.add(bld);
                        }
                        bAdapter.notifyDataSetChanged();
                    }
                }, "mount -o remount,rw /system",
                    "echo " + tvKey.getText().toString() + "=" + tvValue.getText().toString() + " >> /system/build.prop",
                    "mount -o remount,ro /system");

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {


            }

        });
        builder.setView(view);

        AlertDialog alert = builder.create();

        alert.show();
    }

    private void backup()
    {
        try
        {
            //RootExecuter.exec(new String[]{"cp /system/build.prop "+Environment.getExternalStorageDirectory().toString()+"/KernelTuner/build.prop"});
            FileUtils.copyFile(new File("/system/build.prop"), new File(Environment.getExternalStorageDirectory().toString() + "/KernelTuner/build/build.prop-" + Tools.msToDateSimple(System.currentTimeMillis())));
            Toast.makeText(this, getString(R.string.build_prop_backedup) + Environment.getExternalStorageDirectory().toString() + "/KernelTuner/build/", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void restore()
    {
        File backupDir = new File(Environment.getExternalStorageDirectory().toString() + "/KernelTuner/build/");
        File[] backups = backupDir.listFiles();
        List<CharSequence> items = new ArrayList<>();
        if (backups != null)
        {
            for (File f : backups)
            {
                items.add(f.getName());
            }
        }
        final CharSequence[] items2;
        items2 = items.toArray(new CharSequence[items.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_backup));
        builder.setItems(items2, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                new RootUtils().exec(new RootUtils.CommandCallbackImpl()
                                     {
                                         @Override
                                         public void onComplete(RootUtils.Status status, String output)
                                         {
                                             Toast.makeText(BuildpropEditor.this, getString(R.string.build_prop_restored), Toast.LENGTH_LONG).show();
                                             new GetBuildEntries().execute();
                                         }
                                     }, "busybox cp -f " + Environment.getExternalStorageDirectory().toString() + "/KernelTuner/build/" + items2[item] + " /system/build.prop",
                        "chmod 644 /system/build.prop");


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
