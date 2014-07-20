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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.DatabaseHandler;
import rs.pedjaapps.kerneltuner.helpers.VoltageAdapter;
import rs.pedjaapps.kerneltuner.model.Voltage;
import rs.pedjaapps.kerneltuner.model.VoltageCollection;
import rs.pedjaapps.kerneltuner.root.RootUtils;

public abstract class AbsVoltageActivity extends AbsActivity implements AdapterView.OnItemClickListener, RootUtils.CommandCallback
{

    private static VoltageAdapter voltageAdapter;
    private ListView voltageListView;
    private DatabaseHandler db;

    private static List<Voltage> voltages = VoltageCollection.getInstance().getVoltages();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.voltage);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(driverName());

        db = new DatabaseHandler(this);

        voltageListView = (ListView) findViewById(R.id.list);
        voltageAdapter = new VoltageAdapter(this);
        voltageListView.setOnItemClickListener(this);

        voltageListView.setAdapter(voltageAdapter);

        Button minus = (Button) findViewById(R.id.button1);
        minus.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                getProgressDialog().show();
                decreaseAll();
            }
        });

        Button plus = (Button) findViewById(R.id.button2);
        plus.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                getProgressDialog().show();
                increaseAll();
            }
        });

        Button save = (Button) findViewById(R.id.button3);
        save.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle(getResources().getString(R.string.voltage_profile_name));
                builder.setMessage(getResources().getString(R.string.enter_voltage_profile_name));
                builder.setIcon(R.drawable.ic_menu_cc);
                final EditText input = new EditText(arg0.getContext());
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String name = input.getText().toString();
                        save(name);

                    }
                });
                builder.setView(input);

                AlertDialog alert = builder.create();

                alert.show();

            }

        });

        Button load = (Button) findViewById(R.id.button4);

        load.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                List<Voltage> voltages = db.getAllVoltages();
                List<CharSequence> items = new ArrayList<CharSequence>();
                for (Voltage v : voltages)
                {
                    if (!"boot".equals(v.getName())) items.add(v.getName());
                }
                final CharSequence[] items2;
                items2 = items.toArray(new String[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle(getResources().getString(R.string.select_profile));
                builder.setItems(items2, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        Voltage voltage = db.getVoltageByName(items2[item].toString());
                        getProgressDialog().show();
                        load(voltage);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button clear = (Button) findViewById(R.id.button5);
        clear.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle(getResources().getString(R.string.clear_voltage_profiles));
                builder.setMessage(getResources().getString(R.string.clear_voltage_profiles_confirm));
                builder.setIcon(R.drawable.delete_light);

                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.clearVoltage();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no), null);

                AlertDialog alert = builder.create();

                alert.show();

            }
        });

        Button delete = (Button) findViewById(R.id.button6);
        delete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                final List<Voltage> voltages = db.getAllVoltages();
                List<CharSequence> items = new ArrayList<CharSequence>();
                for (Voltage v : voltages)
                {
                    if (!"boot".equals(v.getName())) items.add(v.getName());
                }
                final CharSequence[] items2;
                items2 = items.toArray(new String[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
                builder.setTitle(getResources().getString(R.string.select_profile_to_delte));
                builder.setItems(items2, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        db.deleteVoltageByName(voltages.get(item));
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        notifyChanges();
    }

    private void save(String name)
    {
        StringBuilder freqBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        int offset = 0;
        for (Voltage v : voltages)
        {
            if (offset != 0)
            {
                freqBuilder.append(",");
                valueBuilder.append(",");
            }
            freqBuilder.append(v.getFreqValue());
            valueBuilder.append(v.getValue());
            offset++;
        }

        db.addVoltage(new Voltage(name, freqBuilder.toString(), valueBuilder.toString()));
    }

    public static void notifyChanges()
    {
        voltageAdapter.clear();
        VoltageCollection.getInstance().readVoltages();
        for (Voltage entry : VoltageCollection.getInstance().getVoltages())
        {
            voltageAdapter.add(entry);
        }
        voltageAdapter.notifyDataSetChanged();
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
    public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
    {
        showSelectVoltageDialog(voltageAdapter.getItem(p3));
    }

    private void showSelectVoltageDialog(final Voltage voltage)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(voltage.getFreq());
        builder.setNegativeButton(R.string.cancel, null);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_change_voltage, null);

        final TextView tvVoltage = (TextView) view.findViewById(R.id.tvFreqVolt);
        final SeekBar sbVoltage = (SeekBar) view.findViewById(R.id.sbVoltage);
        Button btnMinus = (Button) view.findViewById(R.id.btnMinus);
        Button btnPlus = (Button) view.findViewById(R.id.btnPlus);

        tvVoltage.setText(voltage.getHRValue());
        int seekMax = 56;
        sbVoltage.setMax(seekMax);
        sbVoltage.setProgress((voltage.getValueMultiplied() - 700000) / 12500);
        btnMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                voltage.decreaseVoltage();
                tvVoltage.setText(voltage.getHRValue());
                sbVoltage.setProgress((voltage.getValueMultiplied() - 700000) / 12500);
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                voltage.increaseVoltage();
                tvVoltage.setText(voltage.getHRValue());
                sbVoltage.setProgress((voltage.getValueMultiplied() - 700000) / 12500);
            }
        });

        sbVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3)
            {
                voltage.setValue(p2 * 12500 + 700000);
                tvVoltage.setText(voltage.getHRValue());
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1)
            {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1)
            {
                // TODO: Implement this method
            }
        });

        builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface p1, int p2)
            {
                getProgressDialog().show();
                changeSingle(voltage);
            }
        });
        builder.setView(view);
        builder.show();
    }

    public void onComplete(RootUtils.Status status, String out)
    {
        notifyChanges();
        if (progressDialog != null) progressDialog.dismiss();
        save("boot");
    }

    public void out(String line)
    {
        //stub
    }

    public abstract void load(Voltage voltage);

    public abstract void decreaseAll();

    public abstract void increaseAll();

    public abstract void changeSingle(Voltage voltage);

    public abstract String driverName();

}
