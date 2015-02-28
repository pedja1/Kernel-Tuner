package rs.pedjaapps.kerneltuner.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.adapter.SystemInfoAdapter;
import rs.pedjaapps.kerneltuner.constants.TempUnit;
import rs.pedjaapps.kerneltuner.utility.IOHelper;
import rs.pedjaapps.kerneltuner.model.Frequency;
import rs.pedjaapps.kerneltuner.model.SystemInfo;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.Utility;

/**
 * Created by pedja on 13.7.14..
 */
public class SystemInfoFragment extends Fragment
{
    public static final String TYPE = "type";

    public enum Type
    {
        battery, memory, cpu, device
    }

    ListView mListView;
    SystemInfoAdapter mAdapter;
    ProgressBar pbLoading;
    Type type;
	TempUnit tempUnit;
	private ATGetData atGetData;

    public static Fragment newInstance(Type type)
    {
        SystemInfoFragment fragment = new SystemInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		tempUnit = PrefsManager.getTempUnit();
        Bundle args = getArguments();
        type = (Type) args.getSerializable(TYPE);

        View view = inflater.inflate(R.layout.layout_fragment_system_info_overview, container, false);
        mListView = (ListView)view.findViewById(R.id.list);
        pbLoading = (ProgressBar)view.findViewById(R.id.pbLoading);
        mAdapter = new SystemInfoAdapter(getActivity(), new ArrayList<SystemInfo>());
        mListView.setAdapter(mAdapter);

        atGetData = new ATGetData(type);
		atGetData.execute();

        return view;
    }

	@Override
	public void onDestroy()
	{
		if(atGetData != null)atGetData.cancel(true);
		super.onDestroy();
	}
	

    private class ATGetData extends AsyncTask<Void, Void, List<SystemInfo>>
    {
        Type type;

        public ATGetData(Type type)
        {
            this.type = type;
        }

        @Override
        protected void onPreExecute()
        {
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<SystemInfo> doInBackground(Void... voids)
        {
            switch (type)
            {
                case battery:
                    return getBatteryData();
                case memory:
                    return getMemoryData();
                case cpu:
                    return getCpuData();
                case device:
                    return getDeviceData();
                default:
                    return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<SystemInfo> systemInfos)
        {
            mAdapter.clear();
            mAdapter.addAll(systemInfos);
            mAdapter.notifyDataSetChanged();
            pbLoading.setVisibility(View.GONE);
        }
    }

    private List<SystemInfo> getBatteryData()
    {
        List<SystemInfo> systemInfos = new ArrayList<>();
        SystemInfo/* info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.battery));
        systemInfos.add(info);*/
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.level));
        info.setValue(IOHelper.batteryLevel() + "%");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.temperature));
		double tmp = IOHelper.batteryTemp();
		if (tempUnit == TempUnit.fahrenheit)
		{
			info.setValue((tmp * 1.8 + 32)  + getString(R.string.fahrenheit_unit));
		}
		else if (tempUnit == TempUnit.celsius)
		{
			info.setValue(tmp + getString(R.string.celsius_unit));
		}
		else if (tempUnit == TempUnit.kelvin)
		{
			info.setValue((tmp + 273.15)  + getString(R.string.kelvin_unit));
		}
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.flow));
        info.setValue(IOHelper.batteryDrain());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.capacity));
        info.setValue(IOHelper.batteryCapacity());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.health));
        info.setValue(IOHelper.batteryHealth());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.technology));
        info.setValue(IOHelper.batteryTechnology());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.voltage));
        info.setValue(IOHelper.batteryVoltage() + "mV");
        systemInfos.add(info);
        return systemInfos;
    }

    private List<SystemInfo> getMemoryData()
    {
        int freeRAM = getFreeRAM();
        int totalRAM = getTotalRAM();
        int usedRAM = totalRAM - freeRAM;
        String freeInternal = Utility.byteToHumanReadableSize(Utility.getAvailableSpaceInBytesOnInternalStorage());
        String usedInternal = Utility.byteToHumanReadableSize(Utility.getUsedSpaceInBytesOnInternalStorage());
        String totalInternal = Utility.byteToHumanReadableSize(Utility.getTotalSpaceInBytesOnInternalStorage());
        String freeExternal = Utility.byteToHumanReadableSize(Utility.getAvailableSpaceInBytesOnExternalStorage());
        String usedExternal = Utility.byteToHumanReadableSize(Utility.getUsedSpaceInBytesOnExternalStorage());
        String totalExternal = Utility.byteToHumanReadableSize(Utility.getTotalSpaceInBytesOnExternalStorage());

        List<SystemInfo> systemInfos = new ArrayList<>();
        SystemInfo info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.ram));
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.maximum));
        info.setValue(totalRAM + "MB");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.available));
        info.setValue(freeRAM + "MB");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.used));
        info.setValue(usedRAM + "MB");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(totalExternal.equals(totalInternal) ? R.string.disk : R.string.internal));
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.maximum));
        info.setValue(totalInternal);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.available));
        info.setValue(freeInternal);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.used));
        info.setValue(usedInternal);
        systemInfos.add(info);
        if(!totalExternal.equals(totalInternal))
        {
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_HEADER);
            info.setTitle(getString(R.string.external));
            systemInfos.add(info);
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_ITEM);
            info.setTitle(getString(R.string.maximum));
            info.setValue(totalExternal);
            systemInfos.add(info);
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_ITEM);
            info.setTitle(getString(R.string.available));
            info.setValue(freeExternal);
            systemInfos.add(info);
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_ITEM);
            info.setTitle(getString(R.string.used));
            info.setValue(usedExternal);
            systemInfos.add(info);
        }
        return systemInfos;
    }

    public static int getTotalRAM()
    {
        RandomAccessFile reader = null;
        String load;
        int mem = 0;
        try
        {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            mem = Utility.parseInt(load.substring(load.indexOf(":") + 1,
                    load.lastIndexOf(" ")).trim(), 0) / 1024;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {

                e.printStackTrace();
            }
        }
        return mem;
    }

    public int getFreeRAM()
    {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return (int) (mi.availMem / 1048576L);
    }

    private List<SystemInfo> getCpuData()
    {
        List<SystemInfo> systemInfos = new ArrayList<>();
        SystemInfo info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.cpu));
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.available_frequencies));
        StringBuilder builder = new StringBuilder();
        List<Frequency> frequencies = IOHelper.frequencies();
        int i = 0;
        for(Frequency frequency : frequencies)
        {
            if(i != 0)builder.append(", ");
            builder.append(frequency.getFrequencyString());
            i++;
        }
        info.setValue(builder.toString());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.maximum_frequency));
        info.setValue(IOHelper.cpu0MaxFreq() / 1000 + "MHz");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.minimum_frequncy));
        info.setValue(IOHelper.cpu0MinFreq() / 1000 + "MHz");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.available_governors));
        builder = new StringBuilder();
        List<String> governors = IOHelper.governorsAsList();
        i = 0;
        for(String s : governors)
        {
            if(i != 0)builder.append(", ");
            builder.append(s);
            i++;
        }
        info.setValue(builder.toString());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.governor));
        info.setValue(IOHelper.cpu0CurGov());
        systemInfos.add(info);
        if(IOHelper.gpuExists())
        {
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_HEADER);
            info.setTitle(getString(R.string.gpu));
            systemInfos.add(info);
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_ITEM);
            info.setTitle(getString(R.string.available_frequencies));
            info.setValue(IOHelper.getGpu3dFrequenciesAsString());
            systemInfos.add(info);
            info = new SystemInfo();
            info.setType(SystemInfo.TYPE_ITEM);
            info.setTitle(getString(R.string.maximum_gpu_frequency));
            info.setValue(IOHelper.gpu3d() / 1000000 + "MHz");
            systemInfos.add(info);
        }
        return systemInfos;
    }

    private List<SystemInfo> getDeviceData()
    {
        List<SystemInfo> systemInfos = new ArrayList<>();
        SystemInfo info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.device));
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.android_version));
        info.setValue(Build.VERSION.RELEASE);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.api_level));
        info.setValue(Build.VERSION.SDK_INT + "");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.api_level));
        info.setValue(Build.CPU_ABI);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.manufacturer));
        info.setValue(Build.MANUFACTURER);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.bootloader));
        info.setValue(Build.BOOTLOADER);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.hardware));
        info.setValue(Build.HARDWARE);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.radio));
        info.setValue(Build.getRadioVersion());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.board));
        info.setValue(Build.BOARD);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.brand));
        info.setValue(Build.BRAND);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.device));
        info.setValue(Build.DEVICE);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.display));
        info.setValue(Build.DISPLAY);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.fingerprint));
        info.setValue(Build.FINGERPRINT);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.host));
        info.setValue(Build.HOST);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.id));
        info.setValue(Build.ID);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.model));
        info.setValue(Build.MODEL);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.product));
        info.setValue(Build.PRODUCT);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.tags));
        info.setValue(Build.TAGS);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.type));
        info.setValue(Build.TYPE);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.user));
        info.setValue(Build.USER);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.applications));
        systemInfos.add(info);
		//todo fix transactiontoolongexception here
        List<PackageInfo> apps = new ArrayList<>(); 
		try
		{
			apps = getActivity().getPackageManager().getInstalledPackages(0);
		}
		catch (Exception e)
		{
			
		}
        int system = 0;
        int user = 0;
        for (PackageInfo packageInfo : apps)
        {
            if (isSystemPackage(packageInfo))
            {
                system++;
            }
            else
            {
                user++;
            }
        }
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.installed_applications));
        info.setValue(user + "");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.system_applications));
        info.setValue(system + "");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.kernel));
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.kernel_version));
        info.setValue(IOHelper.kernel());
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_HEADER);
        info.setTitle(getString(R.string.display));
        systemInfos.add(info);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();

        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.resolution));
        info.setValue(size.x + "x"  + size.y);
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.screen_refresh_rate));
        info.setValue(display.getRefreshRate()+ "Hz");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.screen_density));
        info.setValue(dm.densityDpi + "dpi");
        systemInfos.add(info);
        info = new SystemInfo();
        info.setType(SystemInfo.TYPE_ITEM);
        info.setTitle(getString(R.string.screen_ppi));
        info.setValue("X: " + dm.xdpi + ", Y " + dm.ydpi);
        systemInfos.add(info);
        //TODO sweep2wake vsync, fastcharge, color_depth, schedulers, uptime ...
        return systemInfos;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo)
    {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

}
