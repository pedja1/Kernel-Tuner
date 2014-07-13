package rs.pedjaapps.kerneltuner.model;

public class SystemInfo
{
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int type, icon;
    private String title, value;


    public void setType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
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
    }

    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }
}
