package rs.pedjaapps.KernelTuner.model;

/**
 * Created by pedja on 9/28/13.
 */
public class MainOption
{
    private int title;
    private int description;
    private int icon;
    private boolean selected;

    public MainOption(int title, int description, int icon, boolean selected)
    {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.selected = selected;
    }

    /*

    public MainOption()
    {

    }*/

    public int getTitle()
    {
        return title;
    }

    public void setTitle(int title)
    {
        this.title = title;
    }

    public int getDescription()
    {
        return description;
    }

    public void setDescription(int description)
    {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
