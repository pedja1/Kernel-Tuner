package rs.pedjaapps.KernelTuner.entry;

public final class SideMenuEntry
{

	private final int image;
	private final String  title;
	private final Class activity;

	public SideMenuEntry(final int image, final String title, final Class activity)
	{
		this.image = image;
		this.title = title;
		this.activity = activity;

	}


	public int getImage()
	{
		return image;
	}


	public String getTitle()
	{
		return title;
	}

	public Class getActivity()
	{
		return activity;
	}

}
