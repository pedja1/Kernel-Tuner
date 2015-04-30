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

public final class SysCtl
{

	private int id;
	private String key;
	private String value;
	
	public SysCtl()
	{
		
	}

	public SysCtl(int id, String key, String value)
	{
		this.id = id;
		this.key = key;
		this.value = value;
	}

	public SysCtl(final String name, final String value)
	{
		this.key = name;
		this.value = value;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
