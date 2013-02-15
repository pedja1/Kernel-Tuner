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
package rs.pedjaapps.KernelTuner.entry;

public final class SDScannerEntry
{

	private final String fileName;
	private final int size;
	private final String HRsize;
	private final String path;



	public SDScannerEntry(final String name, final int size, final String HRsize, final String path)
	{
		this.fileName = name;
		this.size = size;
		this.HRsize = HRsize;
		this.path = path;
		

	}


	public String getName()
	{
		return fileName;
	}


	public int getSize()
	{
		return size;
	}
	
	public String getHR()
	{
		return HRsize;
	}

	public String getPath()
	{
		return path;
	}

}
