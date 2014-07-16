package rs.pedjaapps.kerneltuner.model;

public class CPU
{
	public static final int TYPE_HEADER = 0;
	public static final int TYPE_ITEM = 1;
	
	public static final int ITEM_TYPE_MAX = -1;
	public static final int ITEM_TYPE_MIN = -2;
	public static final int ITEM_TYPE_GOV = -3;
	public static final int ITEM_TYPE_SCRN = -5;
	public static final int ITEM_TYPE_INFO = -6;
	
	private int type, itemType, cpuNum;
	private String title, value;

	

	public void setCpuNum(int cpuNum)
	{
		this.cpuNum = cpuNum;
	}

	public int getCpuNum()
	{
		return cpuNum;
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
