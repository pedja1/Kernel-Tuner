package rs.pedjaapps.kerneltuner.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.ui.CPUActivity;
import rs.pedjaapps.kerneltuner.utility.DisplayManager;

/**
 * Created by pedja on 31.5.14..
 */
public class MainFragmentNew extends Fragment
{
    OptionsAdapter mAdapter;

    public static MainFragmentNew newInstance()
    {
        MainFragmentNew fragment = new MainFragmentNew();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final GridView view = (GridView) inflater.inflate(R.layout.layout_fragment_main_new, container, false);
        mAdapter = new OptionsAdapter(getActivity(), generateOptionsList());
        view.post(new Runnable()
        {
            @Override
            public void run()
            {
                view.setColumnWidth(view.getWidth() / 3);
                mAdapter.notifyDataSetChanged();
            }
        });
        view.setAdapter(mAdapter);
        return view;
    }

    private List<Option> generateOptionsList()
    {
        List<Option> options = new ArrayList<>();

        Option option = new Option();
        option.activityClass = CPUActivity.class;
        option.iconRes = R.drawable.main_cpu;
        option.textRes = R.string.btn_cpu;
        option.id = Option.ID.cpu;
        option.infoMore = true;
        option.infoTitleRes = R.string.info_cpu_title;
        option.infoTextRes = R.string.info_cpu_text;
        option.infoUrl = Constants.G_S_URL_PREFIX + "CPU";
        options.add(option);

        option = new Option();
        option.activityClass = CPUActivity.class;
        option.iconRes = R.drawable.main_cpu;
        option.textRes = R.string.btn_cpu;
        option.id = Option.ID.cpu;
        option.infoMore = true;
        option.infoTitleRes = R.string.info_cpu_title;
        option.infoTextRes = R.string.info_cpu_text;
        option.infoUrl = Constants.G_S_URL_PREFIX + "CPU";
        options.add(option);
        return options;
    }

    public static class Option
    {
        public enum ID
        {
            cpu, times, voltage, gpu, governor, misc, tm, build, sysctl, logcat, network, oom, sd,
            sysinfo, app, fm
        }

        @NotNull public ID id;
        public int iconRes, textRes, infoTitleRes, infoTextRes;
        public String infoUrl;
        @Nullable public Class<? extends Activity> activityClass;
        public boolean infoMore;
    }

    private static class OptionsAdapter extends ArrayAdapter<Option>
    {
        public OptionsAdapter(Context context, List<Option> items)
        {
            super(context, 0, items);
        }

        @Override
        public boolean isEnabled(int position)
        {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Option option = getItem(position);

            if(convertView == null)
            {
                convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_main_option_button_item, parent, false);
            }
            Button btn = (Button)convertView;

            btn.setCompoundDrawablesWithIntrinsicBounds(option.iconRes, 0, 0, 0);
            btn.setText(option.textRes);

            return convertView;
        }
    }

}
