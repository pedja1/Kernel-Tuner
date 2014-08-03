package rs.pedjaapps.kerneltuner.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.adapter.NMAdapter;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.model.NM;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.root.RootUtils;
import siir.es.adbWireless.ADBWirelessActivity;

public class NetworkManagerActivity extends AbsActivity implements AdapterView.OnItemClickListener, RootUtils.CommandCallback
{
    ListView mList;
    NMAdapter mListAdapter;
    ProgressBar pbLoading;
    Handler uiHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        uiHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mListAdapter = new NMAdapter(this, new ArrayList<NM>());
        mList.setAdapter(mListAdapter);
        refreshView();
    }

    private void refreshView()
    {
        onComplete(null, null);
    }

    private List<NM> populateList()
    {
        List<NM> list = new ArrayList<>();
        NM nm;
        if (IOHelper.tcpCongestionControlAvailable())
        {
            nm = new NM();
            nm.setType(NM.TYPE_HEADER);
            nm.setTitle(getString(R.string.tcp));
            list.add(nm);

            nm = new NM();
            nm.setType(NM.TYPE_ITEM);
            nm.setItemType(NM.ITEM_TYPE_TCP_CONGESTION);
            nm.setTitle(getString(R.string.tcp_congestion));
            nm.setValue(IOHelper.getTcpCongestion());
            list.add(nm);
        }
        nm = new NM();
        nm.setType(NM.TYPE_HEADER);
        nm.setTitle(getString(R.string.adb));
        list.add(nm);

        nm = new NM();
        nm.setType(NM.TYPE_ITEM);
        nm.setItemType(NM.ITEM_TYPE_ADB_WIRELESS);
        nm.setTitle(getString(R.string.adb_wireless));
        nm.setValue(getString(R.string.adb_wireless_desc));
        list.add(nm);

        nm = new NM();
        nm.setType(NM.TYPE_HEADER);
        nm.setTitle(getString(R.string.nm_info));
        list.add(nm);

        nm = new NM();
        nm.setType(NM.TYPE_ITEM);
        nm.setItemType(NM.ITEM_TYPE_PHONE_INFO);
        nm.setTitle(getString(R.string.phone_info));
        nm.setValue(getString(R.string.phone_info_desc));
        list.add(nm);

        return list;
    }

    @Override
    public void onComplete(RootUtils.Status status, String output)
    {
        new ATPopulateCpuList().execute();
    }

    @Override
    public void out(String line)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //we don't check whether its a header or item because header cant be clicked
        //(isEnabled returns false for header in adapter)
        NM nm = mListAdapter.getItem(i);
        switch (nm.getItemType())
        {
            case NM.ITEM_TYPE_TCP_CONGESTION:
                showSelectTcpCongestionDialog();
                break;
            case NM.ITEM_TYPE_ADB_WIRELESS:
                startActivity(new Intent(this, ADBWirelessActivity.class));
                break;
            case NM.ITEM_TYPE_PHONE_INFO:
                Intent intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.RadioInfo");
                startActivity(intent);
                break;
        }
    }

    private void showSetReadAheadDialog(NM misc)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.read_ahead_cache_size);

        final EditText input = new EditText(this);
        input.setText(misc.getValue().substring(0, misc.getValue().length() - 2));
        input.setSelectAllOnFocus(true);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.requestFocus();

        builder.setPositiveButton(getResources().getString(R.string.set), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                RCommand.setReadAhead(input.getText().toString(), NetworkManagerActivity.this);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.setView(input);
        builder.show();
    }


    private void showSelectTcpCongestionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_tcp_congestion));
        builder.setNegativeButton(R.string.cancel, null);

        final String[] items = IOHelper.getTcpAvailableCongestion();
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                RCommand.setTcpCongestion(items[i], NetworkManagerActivity.this);
            }
        });

        builder.show();
    }

    private void showToggleDialog(final NM misc)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(misc.getTitle());
        builder.setNegativeButton(R.string.cancel, null);

        final String[] items = new String[]{getString(R.string.disabled), getString(R.string.enabled)};
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                switch (misc.getItemType())
                {
                    /*case NM.ITEM_TYPE_S2W:
                        switch (s2wMethod)
                        {
                            case std:
                                RCommand.setS2w(i, Constants.S2W, NetworkManagerActivity.this);
                                break;
                            case alt:
                                RCommand.setS2w(i, Constants.S2W_ALT, NetworkManagerActivity.this);
                                break;
                            case err:
                                break;
                        }
                        break;
                    case NM.ITEM_TYPE_DT2W:
                        RCommand.setDt2w(i, NetworkManagerActivity.this);
                        break;
                    case NM.ITEM_TYPE_FASTCHARGE:
                        RCommand.setFastcharge(i, NetworkManagerActivity.this);
                        break;
                    case NM.ITEM_TYPE_VSYNC:
                        RCommand.setVsync(i, NetworkManagerActivity.this);
                        break;
                    case NM.ITEM_TYPE_OTG:
                        RCommand.setOtg(i, NetworkManagerActivity.this);
                        break;*/
                }

            }
        });

        builder.show();
    }


    private class ATPopulateCpuList extends AsyncTask<Void, Void, List<NM>>
    {

        @Override
        protected List<NM> doInBackground(Void... voids)
        {
            return populateList();
        }

        @Override
        protected void onPreExecute()
        {
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<NM> list)
        {
            mListAdapter.clear();
            mListAdapter.addAll(list);
            mListAdapter.notifyDataSetChanged();
            pbLoading.setVisibility(View.GONE);
        }
    }
}
