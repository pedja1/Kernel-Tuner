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
import android.app.ProgressDialog;
import android.content.*;
import android.content.DialogInterface.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

import rs.pedjaapps.kerneltuner.*;
import rs.pedjaapps.kerneltuner.model.*;
import rs.pedjaapps.kerneltuner.helpers.SDAdapter;

import rs.pedjaapps.kerneltuner.utility.Tools;

public class SDScannerActivity extends AbsActivity
{
    private ProgressDialog pd;
    SDAdapter sDAdapter;
    HashMap<String, List<SDScannerEntry>> backstack = new HashMap<>();
    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    String path = DEFAULT_PATH;

    @Override
    protected void onRestoreInstanceState(Bundle savedState)
    {
        super.onRestoreInstanceState(savedState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sd_analyzer_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView sDListView = (ListView) findViewById(R.id.list);
        sDAdapter = new SDAdapter(this, R.layout.sd_list_row);
        sDListView.setAdapter(sDAdapter);
        sDListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int pos,long arg3)
            {
                final SDScannerEntry entry = sDAdapter.getItem(pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle(entry.getFileName());
                builder.setMessage(getResources().getString(R.string.sd_alert_location) + " " + entry.getPath() +
                        "\n" + getResources().getString(R.string.sd_alert_size) + " " + entry.getHRsize());

                builder.setIcon(R.drawable.ic_menu_cc);

                if (new File(entry.getPath()).isDirectory())
                {
                    builder.setPositiveButton(getResources().getString(R.string.sd_alert_scan), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            path = entry.getPath();
                            new ScanSDCard().execute(path);
                        }
                    });
                }
                builder.setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        new File(entry.getPath()).delete();
                        sDAdapter.remove(sDAdapter.getItem(pos));
                        sDAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        new ScanSDCard().execute(path);
    }

    private class ScanSDCard extends AsyncTask<String, String, List<SDScannerEntry>>
    {
        int max;
        boolean cancel = false;
        @Override
        protected List<SDScannerEntry> doInBackground(String... args)
        {
            List<SDScannerEntry> fms = new ArrayList<>();
            File[] files = new File(args[0]).listFiles();
            max = files.length;
            publishProgress("set_max");
            int i = 0;
            for(File file : files)
            {
                if(cancel)return null;
                publishProgress("set_progress", file.getName(), i + "");
                i++;
                if(file.isFile() && file.length() == 0 && !file.exists())continue;
                SDScannerEntry entry = new SDScannerEntry();
                long size = FileUtils.sizeOf(file);
                entry.setFileName(file.getName());
                entry.setFolder(!file.isFile());
                entry.setHRsize(Tools.byteToHumanReadableSize(size));
                entry.setPath(file.getAbsolutePath());
                entry.setSize(size);
                fms.add(entry);
            }
            Collections.sort(fms, new MyComparator());
            return fms;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            if("set_max".equals(values[0]))
            {
                pd.setMax(max);
            }
            else if("set_progress".equals(values[0]))
            {
                pd.setMessage(values[1]);
                pd.setProgress(Tools.parseInt(values[2], 0));
            }
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(List<SDScannerEntry> list)
        {
            pd.dismiss();
            sDAdapter.clear();
            sDAdapter.addAll(list);
            sDAdapter.notifyDataSetChanged();
            backstack.put(path, list);
        }

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(SDScannerActivity.this);
            pd.setMessage(getString(R.string.scanning_sd_card));
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setOnCancelListener(new OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface arg0)
                {
                    ScanSDCard.this.cancel(true);
                    finish();
                    cancel = true;
                }
            });
            pd.show();
        }
    }

    class MyComparator implements Comparator<SDScannerEntry>
    {
        public int compare(SDScannerEntry ob1, SDScannerEntry ob2)
        {
            if(ob2.getSize() < ob1.getSize()) return -1;
            else if(ob2.getSize() > ob1.getSize()) return 1;
            return 0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(DEFAULT_PATH.equals(path))
        {
            super.onBackPressed();
        }
        else
        {
            String tmpPath = path.substring(0, path.lastIndexOf("/"));
            if(backstack.containsKey(tmpPath))
            {
                sDAdapter.clear();
                sDAdapter.addAll(backstack.get(tmpPath));
                sDAdapter.notifyDataSetChanged();
                path = tmpPath;
            }
            else
            {
                super.onBackPressed();
            }
        }
    }
}
