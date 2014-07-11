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

public final class ChangelogEntry
{

	private final boolean version;
	private final String changelog;
	private final int type;
	private final String versionCode;
	
	public ChangelogEntry(boolean version, String changelog, int type, String versionCode) {
		this.version = version;
		this.changelog = changelog;
		this.type = type;
		this.versionCode = versionCode;
	}

	public boolean isVersion() {
		return version;
	}

	public String getChangelog() {
		return changelog;
	}
	
	public String getVersionCode() {
		return versionCode;
	}

	public int getType() {
		return type;
	}


	
	

}
