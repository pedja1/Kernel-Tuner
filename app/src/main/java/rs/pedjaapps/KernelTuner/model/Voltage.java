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
    String freq;
    int freqValue;
    int value;
    private int id;
    String name;
    private String dbFreqs;
    private String dbValues;
    private int divider = 1; //cannot be 0
    private int multiplier = 1; //cannot be 0

    public Voltage(int id, String name, String dbFreqs, String dbValues)
    {
        this.id = id;
        this.name = name;
        this.dbFreqs = dbFreqs;
        this.dbValues = dbValues;
    }

    public Voltage(String name, String dbFreqs, String dbValues)
    {
        this.name = name;
        this.dbFreqs = dbFreqs;
        this.dbValues = dbValues;
    }


    // Empty constructor
    public Voltage()
    {

    }

    public void setMultiplier(int multiplier)
    {
        this.multiplier = multiplier;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    public void setDivider(int divider)
    {
        this.divider = divider;
    }

    public int getDivider()
    {
        return divider;
    }

    public void setDbFreqs(String dbFreqs)
    {
        this.dbFreqs = dbFreqs;
    }

    public String getDbFreqs()
    {
        return dbFreqs;
    }

    public void setDbValues(String dbValues)
    {
        this.dbValues = dbValues;
    }

    public String getDbValues()
    {
        return dbValues;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setFreqValue(int freqValue)
    {
        this.freqValue = freqValue;
    }

    public int getFreqValue()
    {
        return freqValue;
    }

    public String getName()
    {
        return this.name;
    }

    public String getHRValue()
    {
        return value / divider + "mV";
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

    public int getValueMultiplied()
    {
        return value * multiplier;
    }

    public void decreaseVoltage()
    {
        value = value - (int) (12.5f * divider);
    }

    public void increaseVoltage()
    {
        value = value + (int) (12.5f * divider);
    }
}
