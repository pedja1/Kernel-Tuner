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
package rs.pedjaapps.kerneltuner.shortcuts;

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
