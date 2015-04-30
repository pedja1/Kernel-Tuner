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

public class SDSummaryEntry {

	private String name;
	private String hrSize;
	private Long size;
	private int percent;
	private int icon;
	
	public SDSummaryEntry(String name, String hrSize, long size, int percent, int icon) {
		super();
		this.name = name;
		this.hrSize = hrSize;
		this.size = size;
		this.percent = percent;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getHrSize() {
		return hrSize;
	}

	public Long getSize() {
		return size;
	}

	public int getPercent() {
		return percent;
	}
	
	public int getIcon() {
		return icon;
	}


}
