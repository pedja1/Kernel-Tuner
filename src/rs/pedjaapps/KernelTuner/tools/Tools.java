package rs.pedjaapps.KernelTuner.tools;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import android.os.StatFs;
import android.os.Environment;

public class Tools
{

    public static String byteToHumanReadableSize(long size){
		String hrSize = "";
		long b = size;
		double k = size/1024.0;
		double m = size/1048576.0;
		double g = size/1073741824.0;
		double t = size/1099511627776.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t>1)
		{
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
			hrSize = dec.format(k).concat("KB");
		}
		else if (b>1)
		{
			hrSize = dec.format(b).concat("B");
		}
		return hrSize;

	}
	
	public static String kByteToHumanReadableSize(int size){
		String hrSize = "";
		int k = size;
		double m = size/1024.0;
		double g = size/1048576.0;
		double t = size/1073741824.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t>1)
		{
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
			hrSize = dec.format(k).concat("KB");
		}

		return hrSize;

	}
	
	public static String msToDate(long ms){
		SimpleDateFormat f = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		return f.format(ms);
	}
	public static String msToDateSimple(long ms){
		SimpleDateFormat f = new SimpleDateFormat("ddMMyyHHmmss");
		return f.format(ms);
	}
	
	public static String mbToPages(int progress) {
		String prog = (progress * 1024 / 4)+"";
		return prog;
	}
	
	public static int pagesToMb(int pages) {
		return pages / 1024 * 4;
		
	}
	
	public static long getAvailableSpaceInBytesOnInternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		availableSpace = (long) stat.getAvailableBlocks()
			* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
			* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getAvailableSpaceInBytesOnExternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		availableSpace = (long) stat.getAvailableBlocks()
			* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
			* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
								 .getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}
	
	public static String tempConverter(String tempPref, double cTemp) {
		String tempNew = "";
		/**
		 * cTemp = temperature in celsius tempPreff = string from shared
		 * preferences with value fahrenheit, celsius or kelvin
		 */
		if (tempPref.equals("fahrenheit")) {
			tempNew = ((cTemp * 1.8) + 32) + "°F";

		} else if (tempPref.equals("celsius")) {
			tempNew = cTemp + "°C";

		} else if (tempPref.equals("kelvin")) {

			tempNew = (cTemp + 273.15) + "°C";

		}
		return tempNew;
	}
	
	public static String msToHumanReadableTime(long time)
	{

		String timeString;
		String s = ""+((int)((time / 1000) % 60));
		String m = ""+((int)((time / (1000 * 60)) % 60));
		String h = ""+((int)((time / (1000 * 3600)) % 24));
		String d = ""+((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");
		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");
		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");
		}

		builder.append(s + "s");
		timeString = builder.toString();
		return timeString;
	}

	public static String msToHumanReadableTime2(long time)
	{

		String timeString;
		String s = ""+((int)((time / 100) % 60));
		String m = ""+((int)((time / (100 * 60)) % 60));
		String h = ""+((int)((time / (100 * 3600)) % 24));
		String d = ""+((int)(time / (100 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");
		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");
		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");
		}

		builder.append(s + "s");
		timeString = builder.toString();
		return timeString;
	}
	
}
