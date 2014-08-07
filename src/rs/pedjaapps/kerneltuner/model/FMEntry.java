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

public class FMEntry
{

	private String name;
	private String date;
	private String size;
	private String sizeHr;
	private int folder;
	private String path;
	private String info;
	private String permissions;
	private String time;
	private String owner;
	private String group;
	private int type;

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

	public void setDate(String date)
	{
		this.date = date;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public String getInfo()
	{
		return info;
	}

	public void setPermissions(String permissions)
	{
		this.permissions = permissions;
	}

	public String getPermissions()
	{
		return permissions;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getTime()
	{
		return time;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getGroup()
	{
		return group;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setFolder(int folder)
	{
		this.folder = folder;
	}

	public int getFolder()
	{
		return folder;
	}

	public String getName()
	{
		return name;
	}

	public String getSize()
	{
		return size;
	}

	public String getDate()
	{
		return date;
	}

	public String getPath()
	{
		return path;
	}

	public boolean isFolder()
	{
		return folder == 1;
	}

}
