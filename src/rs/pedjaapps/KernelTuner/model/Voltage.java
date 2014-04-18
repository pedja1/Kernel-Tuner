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
package rs.pedjaapps.KernelTuner.model;

public class Voltage
{

	//private variables
    String name;
    String freq;
    int value;
    

    // Empty constructor
    public Voltage()
	{

    }

    public String getName()
	{
        return this.name;
    }

    
    public void setName(String Name)
	{
        this.name = Name;
    }
	
    public String getFreq()
	{
        return this.freq;
    }

    
    public void setFreq(String freq)
	{
        this.freq = freq;
    }

 
    public int getValue()
	{
        return this.value;
    }

    public void setValue(int value)
	{
        this.value = value;
    }
}
