package rs.pedjaapps.kerneltuner.model;

public class NM
{
	public static final int TYPE_HEADER = 0;
	public static final int TYPE_ITEM = 1;
    public static final int TYPE_ITEM_SWITCH = 2;
	
	public static final int ITEM_TYPE_TCP_CONGESTION = -1;
    public static final int ITEM_TYPE_INFO = -12;
    public static final int ITEM_TYPE_ADB_WIRELESS = -2;
    public static final int ITEM_TYPE_PHONE_INFO = -3;

    private int type, itemType;
	private String title, value;

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
