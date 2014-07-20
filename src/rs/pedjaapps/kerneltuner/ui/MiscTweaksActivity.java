package rs.pedjaapps.kerneltuner.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.adapter.MiscAdapter;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.helpers.IOHelper;
import rs.pedjaapps.kerneltuner.model.Misc;
import rs.pedjaapps.kerneltuner.root.RCommand;
import rs.pedjaapps.kerneltuner.root.RootUtils;

public class MiscTweaksActivity extends AbsActivity implements AdapterView.OnItemClickListener, RootUtils.CommandCallback
{
    private enum S2WMehod
    {
        std, alt, err
    }
    ListView mList;
    MiscAdapter mListAdapter;
    ProgressBar pbLoading;
    Handler uiHandler;
    S2WMehod s2wMethod = S2WMehod.err;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        uiHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mListAdapter = new MiscAdapter(this, new ArrayList<Misc>());
        mList.setAdapter(mListAdapter);
        refreshView();
    }

    private void refreshView()
    {
        onComplete(null, null);
    }

    private List<Misc> populateList()
    {
        List<Misc> list = new ArrayList<>();
        Misc misc = new Misc();
        misc.setType(Misc.TYPE_HEADER);
        misc.setTitle(getString(R.string.io));
        list.add(misc);

        misc = new Misc();
        misc.setType(Misc.TYPE_ITEM);
        misc.setItemType(Misc.ITEM_TYPE_SCHEDULER);
        misc.setTitle(getString(R.string.io_scheduler));
        misc.setValue(IOHelper.scheduler());
        list.add(misc);

        if (IOHelper.sdcacheExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_ITEM);
            misc.setItemType(Misc.ITEM_TYPE_SD_READ_AHEAD);
            misc.setTitle(getString(R.string.read_ahead_cache_size));
            misc.setValue(IOHelper.sdCache() + "KB");
            list.add(misc);
        }

        if (IOHelper.s2wExists() || IOHelper.dt2wExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_HEADER);
            misc.setTitle(getString(R.string.s2w_dt2w));
            list.add(misc);

            if(IOHelper.dt2wExists())
            {
                misc = new Misc();
                misc.setType(Misc.TYPE_ITEM);
                misc.setItemType(Misc.ITEM_TYPE_DT2W);
                misc.setTitle(getString(R.string.doubletap2wake));
                misc.setValue(getDescForDt2w());
                list.add(misc);
            }
            if(IOHelper.s2wExists())
            {
                misc = new Misc();
                misc.setType(Misc.TYPE_ITEM);
                misc.setItemType(Misc.ITEM_TYPE_S2W);
                misc.setTitle(getString(R.string.sweep2wake));
                misc.setValue(getDescForS2w());
                list.add(misc);
            }
        }
        misc = new Misc();
        misc.setType(Misc.TYPE_HEADER);
        misc.setTitle(getString(R.string.other));
        list.add(misc);

        if (IOHelper.fchargeExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_ITEM);
            misc.setItemType(Misc.ITEM_TYPE_FASTCHARGE);
            misc.setTitle(getString(R.string.fastcharge));
            misc.setValue(getDescForFastcharge());
            list.add(misc);
        }

        if (IOHelper.vsyncExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_ITEM);
            misc.setItemType(Misc.ITEM_TYPE_VSYNC);
            misc.setTitle(getString(R.string.vsync));
            misc.setValue(getDescForVsync());
            list.add(misc);
        }

        if (IOHelper.otgExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_ITEM);
            misc.setItemType(Misc.ITEM_TYPE_OTG);
            misc.setTitle(getString(R.string.otg_with_standard_usb));
            misc.setValue(getDescForOtg());
            list.add(misc);
        }
        /*if (IOHelper.cdExists())
        {
            misc = new Misc();
            misc.setType(Misc.TYPE_ITEM);
            misc.setItemType(Misc.ITEM_TYPE_CD);
            misc.setTitle(getString(R.string.color_depth));
            misc.setValue(getDescForColorDepth());
            list.add(misc);
        }*/
        return list;
    }

    private String getDescForDt2w()
    {
        int dt2w = IOHelper.dt2w();
        switch (dt2w)
        {
            case 0:
                return getString(R.string.disabled);
            case 1:
                return getString(R.string.enabled);
            default:
                return getString(R.string.unknown);
        }
    }

    private String getDescForColorDepth()
    {
        String cDepth = IOHelper.cDepth();
        switch (cDepth)
        {
            case "16":
            case "24":
            case "32":
                return cDepth + "-bit";
            default:
                return getString(R.string.unknown);
        }
    }

    private String getDescForOtg()
    {
        int otg = IOHelper.readOTG();
        switch (otg)
        {
            case 0:
                return getString(R.string.disabled);
            case 1:
                return getString(R.string.enabled);
            default:
                return getString(R.string.unknown);
        }
    }

    private String getDescForFastcharge()
    {
        int fcharge = IOHelper.fcharge();
        switch (fcharge)
        {
            case 0:
                return getString(R.string.disabled);
            case 1:
                return getString(R.string.enabled);
            default:
                return getString(R.string.unknown);
        }
    }

    private String getDescForVsync()
    {
        int vsync = IOHelper.vsync();
        switch (vsync)
        {
            case 0:
                return getString(R.string.disabled);
            case 1:
                return getString(R.string.enabled);
            default:
                return getString(R.string.unknown);
        }
    }


    private String getDescForS2w()
    {
        if(new File(Constants.S2W).exists())
        {
            s2wMethod = S2WMehod.std;
        }
        else if(new File(Constants.S2W_ALT).exists())
        {
            s2wMethod = S2WMehod.alt;
        }
        int s2w = IOHelper.s2w();
        switch (s2w)
        {
            case 0:
                return getString(R.string.disabled);
            case 1:
                return getString(R.string.enabled);
            default:
                return getString(R.string.unknown);
        }

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
        Misc misc = mListAdapter.getItem(i);
        switch (misc.getItemType())
        {
            case Misc.ITEM_TYPE_SCHEDULER:
                showSelectSchedulerDialog();
                break;
            case Misc.ITEM_TYPE_SD_READ_AHEAD:
                showSetReadAheadDialog(misc);
                break;
            case Misc.ITEM_TYPE_DT2W:
            case Misc.ITEM_TYPE_S2W:
            case Misc.ITEM_TYPE_FASTCHARGE:
            case Misc.ITEM_TYPE_VSYNC:
            case Misc.ITEM_TYPE_OTG:
                showToggleDialog(misc);
                break;
        }
    }

    private void showSetReadAheadDialog(Misc misc)
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
                RCommand.setReadAhead(input.getText().toString(), MiscTweaksActivity.this);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.setView(input);
        builder.show();
    }


    private void showSelectSchedulerDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_scheduler));
        builder.setNegativeButton(R.string.cancel, null);

        final String[] items = IOHelper.schedulersAsArray();
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                RCommand.setScheduler(items[i], MiscTweaksActivity.this);
            }
        });

        builder.show();
    }

    private void showToggleDialog(final Misc misc)
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
                    case Misc.ITEM_TYPE_S2W:
                        switch (s2wMethod)
                        {
                            case std:
                                RCommand.setS2w(i, Constants.S2W, MiscTweaksActivity.this);
                                break;
                            case alt:
                                RCommand.setS2w(i, Constants.S2W_ALT, MiscTweaksActivity.this);
                                break;
                            case err:
                                break;
                        }
                        break;
                    case Misc.ITEM_TYPE_DT2W:
                        RCommand.setDt2w(i, MiscTweaksActivity.this);
                        break;
                    case Misc.ITEM_TYPE_FASTCHARGE:
                        RCommand.setFastcharge(i, MiscTweaksActivity.this);
                        break;
                    case Misc.ITEM_TYPE_VSYNC:
                        RCommand.setVsync(i, MiscTweaksActivity.this);
                        break;
                    case Misc.ITEM_TYPE_OTG:
                        RCommand.setOtg(i, MiscTweaksActivity.this);
                        break;
                }

            }
        });

        builder.show();
    }


    private class ATPopulateCpuList extends AsyncTask<Void, Void, List<Misc>>
    {

        @Override
        protected List<Misc> doInBackground(Void... voids)
        {
            return populateList();
        }

        @Override
        protected void onPreExecute()
        {
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Misc> list)
        {
            mListAdapter.clear();
            mListAdapter.addAll(list);
            mListAdapter.notifyDataSetChanged();
            pbLoading.setVisibility(View.GONE);
        }
    }
}
