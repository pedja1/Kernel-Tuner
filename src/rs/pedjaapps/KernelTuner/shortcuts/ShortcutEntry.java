package rs.pedjaapps.KernelTuner.shortcuts;

public final class ShortcutEntry
{

	private final String title;
	private final String desc;
	public final int icon;



	public ShortcutEntry(final String title, final String desc, final int icon)
	{
		this.title = title;
		this.desc = desc;
		this.icon = icon;


	}


	public String getTitle()
	{
		return title;
	}


	public String getDesc()
	{
		return desc;
	}

	public int getIcon()
	{
		return icon;
	}
	
}
