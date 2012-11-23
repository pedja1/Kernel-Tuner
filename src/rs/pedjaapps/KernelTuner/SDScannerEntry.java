package rs.pedjaapps.KernelTuner;

public final class SDScannerEntry
{

	private final String fileName;
	private final int size;
	private final String HRsize;



	public SDScannerEntry(final String name, final int size, final String HRsize)
	{
		this.fileName = name;
		this.size = size;
		this.HRsize = HRsize;
		

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


}
