/*
 * This file is part of the Kernel Tuner.
 *
 * Copyright Predrag Čokulov <predragcokulov@gmail.com>
 *
 * Kernel Tuner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Tuner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
 */
package rs.pedjaapps.kerneltuner.model;

import java.util.Date;

import rs.pedjaapps.kerneltuner.ui.FMActivity;

public class FMEntry
{

	private String name;
	private Date date;
    private String dateHr;
	private String size;
	private String sizeHr;
	private String path;
	private int permissions;
	private int type;
    private String link;

	public FMEntry()
	{
		
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public void setSizeHr(String sizeHr)
	{
		this.sizeHr = sizeHr;
	}

	public String getSizeHr()
	{
		return sizeHr;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public String getSize()
	{
		return size;
	}

	public Date getDate()
	{
		return date;
	}

	public String getPath()
	{
		return path;
	}

	public boolean isFolder()
	{
		return type == FMActivity.TYPE_DIRECTORY || type == FMActivity.TYPE_DIRECTORY_LINK;
	}

    public int getPermissions()
    {
        return permissions;
    }

    public void setPermissions(int permissions)
    {
        this.permissions = permissions;
    }

    public String getDateHr()
    {
        return dateHr;
    }

    public void setDateHr(String dateHr)
    {
        this.dateHr = dateHr;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
