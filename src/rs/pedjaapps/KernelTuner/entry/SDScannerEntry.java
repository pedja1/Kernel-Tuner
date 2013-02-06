package rs.pedjaapps.KernelTuner.entry;

public final class SDScannerEntry
{

	private final String fileName;
	private final int size;
	private final String HRsize;
	private final String path;



	public SDScannerEntry(final String name, final int size, final String HRsize, final String path)
	{
		this.fileName = name;
		this.size = size;
		this.HRsize = HRsize;
		this.path = path;
		

	}


	public String getName()
	{
		return fileName;
	}


	public int getSize()
	{
		return size;
	}
	
	public String getHR()
	{
		return HRsize;
	}

	public String getPath()
	{
		return path;
	}

}
