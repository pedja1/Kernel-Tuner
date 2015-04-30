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

import android.graphics.drawable.*;
import android.os.*;

public class Task implements Parcelable
{
    int pid;
	String name;
	Drawable icon;
	int rss;
	int type;
	boolean killDisabled;

	public Task(String name, int pid, Drawable icon, int rss, int type)
	{
		this.name = name;
		this.pid = pid;
		this.icon = icon;
		this.rss = rss;
		this.type = type;
	}

	public void setKillDisabled(boolean killDisabled)
	{
		this.killDisabled = killDisabled;
	}

	public boolean isKillDisabled()
	{
		return killDisabled;
	}

	public int getPid()
	{
		return pid;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getRss()
	{
		return rss;
	}
	
	public Drawable getIcon()
	{
		return icon;
	}

	public String getName()
	{
		return name;
	}
	
	 @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.pid);
        dest.writeInt(this.rss);
        dest.writeString(this.name);
        dest.writeInt(this.type);
    }

    private Task(Parcel in)
    {
        this.pid = in.readInt();
        this.rss = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
    }

    public static Creator<Task> CREATOR = new Creator<Task>()
    {
        public Task createFromParcel(Parcel source)
        {
            return new Task(source);
        }

        public Task[] newArray(int size)
        {
            return new Task[size];
        }
    };

}
