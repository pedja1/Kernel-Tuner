package rs.pedjaapps.kerneltuner.model;

public class Misc
{
	public static final int TYPE_HEADER = 0;
	public static final int TYPE_ITEM = 1;
	
	public static final int ITEM_TYPE_SCHEDULER = -1;
    public static final int ITEM_TYPE_SD_READ_AHEAD = -2;
    public static final int ITEM_TYPE_DT2W = -3;
    public static final int ITEM_TYPE_S2W = -4;
    public static final int ITEM_TYPE_FASTCHARGE = -5;
    public static final int ITEM_TYPE_VSYNC = -6;
    public static final int ITEM_TYPE_OTG = -7;
    public static final int ITEM_TYPE_CD = -8;
    public static final int ITEM_TYPE_INFO = -12;
	
	private int type, itemType;
	private String title, value;
	private boolean enabled = true;

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}

	public int getItemType()
	{
		return itemType;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}}
