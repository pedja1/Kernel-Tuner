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


import android.widget.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.model.SysCtl;
import rs.pedjaapps.kerneltuner.helpers.DatabaseHandler;
import rs.pedjaapps.kerneltuner.helpers.SysCtlAdapter;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import rs.pedjaapps.kerneltuner.utility.Tools;


public class SysCtlActivity extends AbsActivity
{
    ListView sysListView;
    SysCtlAdapter sysAdapter;
    List<SysCtl> entries;
    ProgressDialog pd;
    CheckBox kernel, vm, fs, net;
    SharedPreferences preferences;
    DatabaseHandler db = new DatabaseHandler(this);
    ProgressBar loading;
    String arch;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysctl);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        arch = Tools.getAbi();
        loading = (ProgressBar) findViewById(R.id.loading);
        kernel = (CheckBox) findViewById(R.id.kernel);
        vm = (CheckBox) findViewById(R.id.vm);
        fs = (CheckBox) findViewById(R.id.fs);
        net = (CheckBox) findViewById(R.id.net);

        kernel.setChecked(preferences.getBoolean("sysctl_kernel", true));
        vm.setChecked(preferences.getBoolean("sysctl_vm", true));
        fs.setChecked(preferences.getBoolean("sysctl_fs", true));
        net.setChecked(preferences.getBoolean("sysctl_net", false));

        kernel.setOnCheckedChangeListener(new Listener());
        net.setOnCheckedChangeListener(new Listener());
        vm.setOnCheckedChangeListener(new Listener());
        fs.setOnCheckedChangeListener(new Listener());


        sysListView = (ListView) findViewById(R.id.list);
        sysAdapter = new SysCtlAdapter(this, R.layout.sysctl_row);
        sysListView.setAdapter(sysAdapter);

        sysListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, final int pos,
                                    long is)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final SysCtl tmpEntry = sysAdapter.getItem(pos);
                builder.setTitle(tmpEntry.getKey());

                builder.setMessage(getResources().getString(R.string.set_new_value));

                builder.setIcon(R.drawable.sysctl);


                final EditText input = new EditText(v.getContext());
                input.setText(tmpEntry.getValue());
                input.setSelectAllOnFocus(true);
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.requestFocus();
                builder.setPositiveButton(R.string.Change, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        new RootUtils().exec(new RootUtils.CommandCallbackImpl()
                        {
                            @Override
                            public void onComplete(RootUtils.Status status, String output)
                            {
                                sysAdapter.remove(tmpEntry);
                                sysAdapter.insert(new SysCtl(tmpEntry.getKey(), input.getText().toString()), pos);
                                sysAdapter.notifyDataSetChanged();

                                if (db.sysEntryExists(tmpEntry.getKey()))
                                {
                                    db.updateSysEntry(new SysCtl(tmpEntry.getKey(), input.getText().toString()));
                                }
                                else
                                {
                                    db.addSysCtlEntry(new SysCtl(tmpEntry.getKey(), input.getText().toString()));
                                }
                            }
                        }, "sysctl -w " + tmpEntry.getKey().trim() + "=" + input.getText().toString().trim());
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), null);
                builder.setView(input);

                AlertDialog alert = builder.create();

                alert.show();
            }
        });
        new GetSysCtlEntries().execute();
    }

    private class Listener implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1)
        {
            new GetSysCtlEntries().execute();
        }
    }

    private class GetSysCtlEntries extends AsyncTask<String, Void, List<SysCtl>>
    {
        String line;

        @Override
        protected List<SysCtl> doInBackground(String... args)
        {
            entries = new ArrayList<>();
            Process proc;
            try
            {
                proc = Runtime.getRuntime().exec("sysctl -a\n");
                InputStream inputStream = proc.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while ((line = bufferedReader.readLine()) != null)
                {
                    if (!line.startsWith("sysctl:"))
                    {
                        //line = line.replaceAll("\\s", "");
                        String[] temp = line.split("=");
                        List<String> tmp = Arrays.asList(temp);

                        //System.out.println(line);
                        //	System.out.println(tmp.get(0));
                        SysCtl tmpEntry = new SysCtl(tmp.get(0), tmp.get(1));
                        entries.add(tmpEntry);
                        //	publishProgress(tmpEntry);
                    }

                }
                proc.waitFor();
                proc.destroy();
            }
            catch (Exception e)
            {
                Log.e("syscl", "error " + e.getMessage());
            }
            return entries;
        }

        @Override
        protected void onPostExecute(List<SysCtl> res)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("sysctl_kernel", kernel.isChecked())
                    .putBoolean("sysctl_vm", vm.isChecked())
                    .putBoolean("sysctl_fs", fs.isChecked())
                    .putBoolean("sysctl_net", net.isChecked())
                    .apply();
            for (SysCtl e : res)
            {
                if (e.getKey().startsWith("kernel"))
                {
                    if (kernel.isChecked())
                    {
                        sysAdapter.add(e);
                    }
                }
                else if (e.getKey().startsWith("vm"))
                {
                    if (vm.isChecked())
                    {
                        sysAdapter.add(e);
                    }
                }
                else if (e.getKey().startsWith("fs"))
                {
                    if (fs.isChecked())
                    {
                        sysAdapter.add(e);
                    }
                }
                else if (e.getKey().startsWith("net"))
                {
                    if (net.isChecked())
                    {
                        sysAdapter.add(e);
                    }
                }
                else
                {
                    sysAdapter.add(e);
                }
            }
            sysAdapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            //setProgressBarIndeterminateVisibility(false);

        }

        @Override
        protected void onPreExecute()
        {
            sysAdapter.clear();
            loading.setVisibility(View.VISIBLE);
        }

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

        }
        return super.onOptionsItemSelected(item);
    }


}
