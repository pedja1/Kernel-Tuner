package rs.pedjaapps.KernelTuner;

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
