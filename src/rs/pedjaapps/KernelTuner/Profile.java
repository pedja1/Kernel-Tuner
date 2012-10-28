package rs.pedjaapps.KernelTuner;

public class Profile
{

	//private variables
    int _id;
    String _Name;
    String _cpu0min;
    String _cpu0max;
    String _cpu1min;
    String _cpu1max;
    String _cpu0gov;
    String _cpu1gov;
    String _gpu2d;
    String _gpu3d;
    String _vsync;
    String _number_of_cores;
    String _mtu;
    String _mtd;

    // Empty constructor
    public Profile()
	{

    }
    // constructor
    public Profile(int id, String Name, String cpu0min, String cpu0max, String cpu1min,
				   String cpu1max,
				   String cpu0gov,
				   String cpu1gov,
				   String gpu2d,
				   String gpu3d,
				   String vsync,
				   String number_of_cores,
				   String mtu,
				   String mtd)
	{
        this._id = id;
        this._Name = Name;
        this._cpu0min = cpu0min;
        this._cpu0max = cpu0max;
        this._cpu1min = cpu1min;
        this._cpu1max = cpu1max;
        this._cpu0gov = cpu0gov;
        this._cpu1gov = cpu1gov;
        this._gpu2d = gpu2d;
        this._gpu3d = gpu3d;
        this._vsync = vsync;
        this._number_of_cores = number_of_cores;
        this._mtd = mtd;
        this._mtu = mtu;


    }

    // constructor
    public Profile(String Name, String cpu0min, String cpu0max, String cpu1min,
				   String cpu1max,
				   String cpu0gov,
				   String cpu1gov,
				   String gpu2d,
				   String gpu3d,
				   String vsync,
				   String number_of_cores,
				   String mtu,
				   String mtd)
	{

    	this._Name = Name;
        this._cpu0min = cpu0min;
        this._cpu0max = cpu0max;
        this._cpu1min = cpu1min;
        this._cpu1max = cpu1max;
        this._cpu0gov = cpu0gov;
        this._cpu1gov = cpu1gov;
        this._gpu2d = gpu2d;
        this._gpu3d = gpu3d;
        this._vsync = vsync;
        this._number_of_cores = number_of_cores;
        this._mtd = mtd;
        this._mtu = mtu;
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

	// getting Name
    public String getName()
	{
        return this._Name;
    }

    // setting Name
    public void setName(String Name)
	{
        this._Name = Name;
    }

    // getting cpu0min
    public String getCpu0min()
	{
        return this._cpu0min;
    }

    // setting cpu0min
    public void setCpu0min(String cpu0min)
	{
        this._cpu0min = cpu0min;
    }

    // getting phone cpu0max
    public String getCpu0max()
	{
        return this._cpu0max;
    }

    // setting phone cpu0max
    public void setCpu0max(String cpu0max)
	{
        this._cpu0max = cpu0max;
    }

	// getting phone cpu1min
    public String getCpu1min()
	{
        return this._cpu1min;
    }

    // setting phone cpu1min
    public void setCpu1min(String cpu1min)
	{
        this._cpu1min = cpu1min;
    }

	// getting phone cpu1max
    public String getCpu1max()
	{
        return this._cpu1max;
    }

    // setting phone cpu1max
    public void setCpu1max(String cpu1max)
	{
        this._cpu1max = cpu1max;
    }

	// getting phone cpu0gov
    public String getCpu0gov()
	{
        return this._cpu0gov;
    }

    // setting phone cpu0gov
    public void setCpu0gov(String cpu0gov)
	{
        this._cpu0gov = cpu0gov;
    }

	// getting phone cpu1gov
    public String getCpu1gov()
	{
        return this._cpu1gov;
    }

    // setting phone cpu1gov
    public void setCpu1gov(String cpu1gov)
	{
        this._cpu1gov = cpu1gov;
    }

	// getting phone gpu2d
    public String getGpu2d()
	{
        return this._gpu2d;
    }

    // setting phone gpu2d
    public void setGpu2d(String gpu2d)
	{
        this._gpu2d = gpu2d;
    }

	// getting phone gpu3d
    public String getGpu3d()
	{
        return this._gpu3d;
    }

    // setting phone gpu3d
    public void setGpu3d(String gpu3d)
	{
        this._gpu3d = gpu3d;
    }

	// getting phone vsync
    public String getVsync()
	{
        return this._vsync;
    }

    // setting phone vsync
    public void setVsync(String vsync)
	{
        this._vsync = vsync;
    }

	// getting phone noc
    public String getNOC()
	{
        return this._number_of_cores;
    }

    // setting phone noc
    public void setNOC(String number_of_cores)
	{
        this._number_of_cores = number_of_cores;
    }

	// getting phone mtd
    public String getMtd()
	{
        return this._mtd;
    }

    // setting phone mtd
    public void setMtd(String mtd)
	{
        this._mtd = mtd;
    }

	// getting phone mtu
    public String getMtu()
	{
        return this._mtu;
    }

    // setting phone mtu
    public void setMtu(String mtu)
	{
        this._mtu = mtu;
    }


}
