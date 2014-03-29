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

public class Voltage
{

	//private variables
    int _id;
    String _Name;
    String _freq;
    String _value;
    

    // Empty constructor
    public Voltage()
	{

    }
    // constructor
    public Voltage(int id, String Name, String freq, String value)
	{
        this._id = id;
        this._Name = Name;
        this._freq = freq;
        this._value = value;
       ;


    }

    // constructor
    public Voltage(String Name, String freq, String value)
	{
    	this._Name = Name;
    	this._freq = freq;
        this._value = value;
        
    }
    // getting ID
    public int getID()
	{
        return this._id;
    }

    // setting id
    public void setID(int id)
	{
        this._id = id;
    }

    public String getName()
	{
        return this._Name;
    }

    
    public void setName(String Name)
	{
        this._Name = Name;
    }
	
    public String getFreq()
	{
        return this._freq;
    }

    
    public void setFreq(String freq)
	{
        this._freq = freq;
    }

 
    public String getValue()
	{
        return this._value;
    }

   
    public void setValue(String value)
	{
        this._value = value;
    }

    
}
