package rs.pedjaapps.KernelTuner;


import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHandler extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KTDatabase.db";

    // table names
    private static final String TABLE_PROFILES = "profiles";
    private static final String TABLE_VOLTAGE = "voltage";

    // Table Columns names
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_NAME = "Name";
    private static final String KEY_PROFILE_CPU0MIN = "cpu0min";
    private static final String KEY_PROFILE_CPU0MAX = "cpu0max";
    private static final String KEY_PROFILE_CPU1MAX = "cpu1max";
    private static final String KEY_PROFILE_CPU1MIN = "cpu1min";

    private static final String KEY_PROFILE_CPU2MIN = "cpu2min";
    private static final String KEY_PROFILE_CPU2MAX = "cpu2max";
    private static final String KEY_PROFILE_CPU3MAX = "cpu3max";
    private static final String KEY_PROFILE_CPU3MIN = "cpu3min";
    private static final String KEY_PROFILE_CPU0GOV = "cpu0gov";
    private static final String KEY_PROFILE_CPU1GOV = "cpu1gov";
    private static final String KEY_PROFILE_CPU2GOV = "cpu2gov";
    private static final String KEY_PROFILE_CPU3GOV = "cpu3gov";
	private static final String KEY_PROFILE_VOLTAGE = "voltageProfile";
    private static final String KEY_PROFILE_MTD = "mtd";
    private static final String KEY_PROFILE_MTU = "mtu";
    private static final String KEY_PROFILE_GPU2D = "gpu2d";
    private static final String KEY_PROFILE_GPU3D = "gpu3d";
	private static final String KEY_PROFILE_BUTTONS_BACKLIGHT = "buttonsLight";
    private static final String KEY_PROFILE_VSYNC = "vsync";
	private static final String KEY_PROFILE_F_CHARGE = "fastcharge";
	private static final String KEY_PROFILE_CDEPTH = "cdepth";
	private static final String KEY_PROFILE_IOSCHEDULER = "IOScheduler";
	private static final String KEY_PROFILE_SDCACHE = "sdCache";
	private static final String KEY_PROFILE_NOTIFICATION_LED = "notificationLed";
	private static final String KEY_PROFILE_SWEEP2WAKE = "sweep2wake";
  /*  private static final String KEY_PROFILE_CPU = "cpu";
    private static final String KEY_PROFILE_VT = "vt";
    private static final String KEY_PROFILE_MD = "md";
    private static final String KEY_PROFILE_GPU = "gpu";
    private static final String KEY_PROFILE_CBL = "cbl";
    private static final String KEY_PROFILE_VS = "vs";
    private static final String KEY_PROFILE_FC = "fc";
    private static final String KEY_PROFILE_CD = "cd";
    private static final String KEY_PROFILE_IO = "io";
    private static final String KEY_PROFILE_SD = "sd";
    private static final String KEY_PROFILE_NLT = "nlt";
    private static final String KEY_PROFILE_S2W = "s2w";*/
	
    
    private static final String KEY_VOLTAGE_ID = "id";
    private static final String KEY_VOLTAGE_NAME = "Name";
    private static final String KEY_VOLTAGE_FREQ = "freq";
    private static final String KEY_VOLTAGE_VALUE = "value";

    public DatabaseHandler(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
	{
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
			+ KEY_PROFILE_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PROFILE_NAME + " TEXT,"
			+ KEY_PROFILE_CPU0MIN + " TEXT,"
			+ KEY_PROFILE_CPU0MAX + " TEXT," 
			+ KEY_PROFILE_CPU1MAX + " TEXT,"
			+ KEY_PROFILE_CPU1MIN + " TEXT,"
			+ KEY_PROFILE_CPU2MIN + " TEXT,"
			+ KEY_PROFILE_CPU2MAX + " TEXT," 
			+ KEY_PROFILE_CPU3MAX + " TEXT,"
			+ KEY_PROFILE_CPU3MIN + " TEXT,"
			+ KEY_PROFILE_CPU0GOV + " TEXT,"
			+ KEY_PROFILE_CPU1GOV + " TEXT,"
			+ KEY_PROFILE_CPU2GOV + " TEXT,"
			+ KEY_PROFILE_CPU3GOV + " TEXT,"
			+ KEY_PROFILE_VOLTAGE + " TEXT,"
			+ KEY_PROFILE_MTD + " TEXT,"
			+ KEY_PROFILE_MTU + " TEXT,"
			+ KEY_PROFILE_GPU2D + " TEXT,"
			+ KEY_PROFILE_GPU3D + " TEXT,"
			+ KEY_PROFILE_BUTTONS_BACKLIGHT + " TEXT,"
			+ KEY_PROFILE_VSYNC + " INTEGER,"
			+ KEY_PROFILE_F_CHARGE + " INTEGER,"
			+ KEY_PROFILE_CDEPTH + " TEXT,"
			+ KEY_PROFILE_IOSCHEDULER + " TEXT,"
			+ KEY_PROFILE_SDCACHE + " INTEGER,"
			+ KEY_PROFILE_NOTIFICATION_LED + " TEXT,"
			+ KEY_PROFILE_SWEEP2WAKE + " INTEGER"
			/*+ KEY_PROFILE_CPU + " INTEGER,"
			+ KEY_PROFILE_VT + " INTEGER,"
			+ KEY_PROFILE_MD + " INTEGER,"
			+ KEY_PROFILE_GPU + " INTEGER,"
			+ KEY_PROFILE_CBL + " INTEGER,"
			+ KEY_PROFILE_VS + " INTEGER,"
			+ KEY_PROFILE_FC + " INTEGER,"
			+ KEY_PROFILE_CD + " INTEGER,"
			+ KEY_PROFILE_IO + " INTEGER,"
			+ KEY_PROFILE_SD + " INTEGER,"
			+ KEY_PROFILE_NLT + " INTEGER,"
			+ KEY_PROFILE_S2W + " INTEGER"*/
			+
			")";
        String CREATE_VOLTAGE_TABLE = "CREATE TABLE " + TABLE_VOLTAGE + "("
    			+ KEY_VOLTAGE_ID + " INTEGER PRIMARY KEY," + KEY_VOLTAGE_NAME + " TEXT," + KEY_VOLTAGE_FREQ + " TEXT," + KEY_VOLTAGE_VALUE + " TEXT"
    			+
    			")";
        db.execSQL(CREATE_PROFILES_TABLE);
        db.execSQL(CREATE_VOLTAGE_TABLE);
    }

    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLTAGE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

  
    void addProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_NAME, profile.getName());
        values.put(KEY_PROFILE_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_PROFILE_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_PROFILE_CPU1MAX, profile.getCpu1max());
        values.put(KEY_PROFILE_CPU1MIN, profile.getCpu1min());
        values.put(KEY_PROFILE_CPU2MIN, profile.getCpu2min()); 
        values.put(KEY_PROFILE_CPU2MAX, profile.getCpu2max()); 
        values.put(KEY_PROFILE_CPU3MAX, profile.getCpu3max());
        values.put(KEY_PROFILE_CPU3MIN, profile.getCpu3min());
        values.put(KEY_PROFILE_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_PROFILE_CPU1GOV, profile.getCpu1gov());
        values.put(KEY_PROFILE_CPU2GOV, profile.getCpu2gov());
        values.put(KEY_PROFILE_CPU3GOV, profile.getCpu3gov());
		values.put(KEY_PROFILE_VOLTAGE,profile.getVoltage());
        values.put(KEY_PROFILE_MTD, profile.getMtd());
        values.put(KEY_PROFILE_MTU, profile.getMtu());
        values.put(KEY_PROFILE_GPU2D, profile.getGpu2d());
        values.put(KEY_PROFILE_GPU3D, profile.getGpu3d());
		values.put(KEY_PROFILE_BUTTONS_BACKLIGHT, profile.getButtonsLight());
        values.put(KEY_PROFILE_VSYNC, profile.getVsync());
        values.put(KEY_PROFILE_F_CHARGE, profile.getFcharge());
        values.put(KEY_PROFILE_CDEPTH, profile.getCdepth());
        values.put(KEY_PROFILE_IOSCHEDULER, profile.getIoScheduler());
        values.put(KEY_PROFILE_SDCACHE, profile.getSdcache());
        values.put(KEY_PROFILE_NOTIFICATION_LED, profile.getNotificationLed());
        values.put(KEY_PROFILE_SWEEP2WAKE, profile.getSweep2wake());
        /*values.put(KEY_PROFILE_CPU, profile.getCpu());
        values.put(KEY_PROFILE_VT, profile.getVt());
        values.put(KEY_PROFILE_MD, profile.getMd());
        values.put(KEY_PROFILE_GPU, profile.getGpu());
        values.put(KEY_PROFILE_CBL, profile.getCbl());
        values.put(KEY_PROFILE_VS, profile.getVs());
        values.put(KEY_PROFILE_FC, profile.getFc());
        values.put(KEY_PROFILE_CD, profile.getCd());
        values.put(KEY_PROFILE_IO, profile.getIo());
        values.put(KEY_PROFILE_SD, profile.getSd());
        values.put(KEY_PROFILE_NLT, profile.getNlt());
        values.put(KEY_PROFILE_S2W, profile.getS2w());*/

        // Inserting Row
        db.insert(TABLE_PROFILES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single profile
    Profile getProfile(int id)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_PROFILE_ID,
									 KEY_PROFILE_NAME,
									 KEY_PROFILE_CPU0MIN,
									 KEY_PROFILE_CPU0MAX,
									 KEY_PROFILE_CPU1MIN,
									 KEY_PROFILE_CPU1MAX,
									 KEY_PROFILE_CPU2MIN,
									 KEY_PROFILE_CPU2MAX,
									 KEY_PROFILE_CPU3MIN,
									 KEY_PROFILE_CPU3MAX,
									 KEY_PROFILE_CPU0GOV,
									 KEY_PROFILE_CPU1GOV,
									 KEY_PROFILE_CPU2GOV,
									 KEY_PROFILE_CPU3GOV,
									 KEY_PROFILE_VOLTAGE,
									 KEY_PROFILE_MTD,
									 KEY_PROFILE_MTU,
									 KEY_PROFILE_GPU2D,
									 KEY_PROFILE_GPU3D,
									 KEY_PROFILE_BUTTONS_BACKLIGHT,
									 KEY_PROFILE_VSYNC,
									 KEY_PROFILE_F_CHARGE,
									 KEY_PROFILE_CDEPTH,
									 KEY_PROFILE_IOSCHEDULER,
									 KEY_PROFILE_SDCACHE,
									 KEY_PROFILE_NOTIFICATION_LED,
									 KEY_PROFILE_SWEEP2WAKE
									/* KEY_PROFILE_CPU,
									 KEY_PROFILE_VT,
									 KEY_PROFILE_MD,
									 KEY_PROFILE_GPU,
									 KEY_PROFILE_CBL,
									 KEY_PROFILE_VS,
									 KEY_PROFILE_FC,
									 KEY_PROFILE_CD,
									 KEY_PROFILE_IO,
									 KEY_PROFILE_SD,
								 KEY_PROFILE_NLT,
									KEY_PROFILE_S2W*/}, KEY_PROFILE_ID + "=?",
								 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)),
									  cursor.getString(1),
									  cursor.getString(2),
									  cursor.getString(3), 
									  cursor.getString(4), 
									  cursor.getString(5),
									  cursor.getString(6),
									  cursor.getString(7),
									  cursor.getString(8),
									  cursor.getString(9),
									  cursor.getString(10),
									  cursor.getString(11),
									  cursor.getString(12),
									  cursor.getString(13),
									  cursor.getString(14),
									  cursor.getString(15),
									  cursor.getString(16), 
									  cursor.getString(17), 
									  cursor.getString(18),
									  cursor.getString(19),
									  cursor.getInt(20),
									  cursor.getInt(21),
									  cursor.getString(22),
									  cursor.getString(23),
									  cursor.getInt(24),
									  cursor.getString(25),
									  cursor.getInt(26)
									 /* cursor.getInt(27),
									  cursor.getInt(28),
									  cursor.getInt(29),
									  cursor.getInt(30),
									  cursor.getInt(31),
									  cursor.getInt(32),
									  cursor.getInt(33),
									  cursor.getInt(34),
									  cursor.getInt(35),
									  cursor.getInt(36),
									  cursor.getInt(37),
									  cursor.getInt(38)*/
									  
									  );
        // return contact
        return profile;
    }

    Profile getProfileByName(String name)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_PROFILE_ID,
									 KEY_PROFILE_NAME,
									 KEY_PROFILE_CPU0MIN,
									 KEY_PROFILE_CPU0MAX,
									 KEY_PROFILE_CPU1MIN,
									 KEY_PROFILE_CPU1MAX,
									 KEY_PROFILE_CPU2MIN,
									 KEY_PROFILE_CPU2MAX,
									 KEY_PROFILE_CPU3MIN,
									 KEY_PROFILE_CPU3MAX,
									 KEY_PROFILE_CPU0GOV,
									 KEY_PROFILE_CPU1GOV,
									 KEY_PROFILE_CPU2GOV,
									 KEY_PROFILE_CPU3GOV,
									 KEY_PROFILE_VOLTAGE,
									 KEY_PROFILE_MTD,
									 KEY_PROFILE_MTU,
									 KEY_PROFILE_GPU2D,
									 KEY_PROFILE_GPU3D,
									 KEY_PROFILE_BUTTONS_BACKLIGHT,
									 KEY_PROFILE_VSYNC,
									 KEY_PROFILE_F_CHARGE,
									 KEY_PROFILE_CDEPTH,
									 KEY_PROFILE_IOSCHEDULER,
									 KEY_PROFILE_SDCACHE,
									 KEY_PROFILE_NOTIFICATION_LED,
									 KEY_PROFILE_SWEEP2WAKE
									/* KEY_PROFILE_CPU,
									 KEY_PROFILE_VT,
									 KEY_PROFILE_MD,
									 KEY_PROFILE_GPU,
									 KEY_PROFILE_CBL,
									 KEY_PROFILE_VS,
									 KEY_PROFILE_FC,
									 KEY_PROFILE_CD,
									 KEY_PROFILE_IO,
									 KEY_PROFILE_SD,
									 KEY_PROFILE_NLT,
									 KEY_PROFILE_S2W*/}, KEY_PROFILE_NAME + "=?",
								 new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)),
									  cursor.getString(1),
									  cursor.getString(2),
									  cursor.getString(3), 
									  cursor.getString(4), 
									  cursor.getString(5),
									  cursor.getString(6),
									  cursor.getString(7),
									  cursor.getString(8),
									  cursor.getString(9),
									  cursor.getString(10),
									  cursor.getString(11),
									  cursor.getString(12),
									  cursor.getString(13),
									  cursor.getString(14),
									  cursor.getString(15),
									  cursor.getString(16), 
									  cursor.getString(17), 
									  cursor.getString(18),
									  cursor.getString(19),
									  cursor.getInt(20),
									  cursor.getInt(21),
									  cursor.getString(22),
									  cursor.getString(23),
									  cursor.getInt(24),
									  cursor.getString(25),
									  cursor.getInt(26)
									/*  cursor.getInt(27),
									  cursor.getInt(28),
									  cursor.getInt(29),
									  cursor.getInt(30),
									  cursor.getInt(31),
									  cursor.getInt(32),
									  cursor.getInt(33),
									  cursor.getInt(34),
									  cursor.getInt(35),
									  cursor.getInt(36),
									  cursor.getInt(37),
									  cursor.getInt(38)*/);
        // return contact
        return profile;
    }

    // Getting All Contacts
    public List<Profile> getAllProfiles()
	{
        List<Profile> profileList = new ArrayList<Profile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
		{
            do {
				Profile profile = new Profile();
                profile.setID(Integer.parseInt(cursor.getString(0)));
                profile.setName(cursor.getString(1));
                profile.setCpu0min(cursor.getString(2));
                profile.setCpu0max(cursor.getString(3));
                profile.setCpu1min(cursor.getString(4));
                profile.setCpu1max(cursor.getString(5));
                profile.setCpu2min(cursor.getString(6));
                profile.setCpu2max(cursor.getString(7));
                profile.setCpu3min(cursor.getString(8));
                profile.setCpu3max(cursor.getString(9));
                profile.setCpu0gov(cursor.getString(10));
                profile.setCpu1gov(cursor.getString(11));
                profile.setCpu2gov(cursor.getString(12));
                profile.setCpu3gov(cursor.getString(13));
				profile.setVoltage(cursor.getString(14));
                profile.setMtd(cursor.getString(15));
                profile.setMtu(cursor.getString(16));
                profile.setGpu2d(cursor.getString(17));
                profile.setGpu3d(cursor.getString(8));
				profile.setButtonsLight(cursor.getString(19));
                profile.setVsync(cursor.getInt(20));
                profile.setFcharge(cursor.getInt(21));
                profile.setCdepth(cursor.getString(22));
                profile.setIoScheduler(cursor.getString(23));
                profile.setSdcache(cursor.getInt(24));
                profile.setNotificationLed(cursor.getString(25));
                profile.setSweep2wake(cursor.getInt(26));
              /*  profile.setCpu(cursor.getInt(27));
                profile.setVt(cursor.getInt(28));
                profile.setMd(cursor.getInt(29));
                profile.setGpu(cursor.getInt(30));
                profile.setCbl(cursor.getInt(31));
                profile.setVs(cursor.getInt(32));
                profile.setFc(cursor.getInt(33));
                profile.setCd(cursor.getInt(34));
                profile.setIo(cursor.getInt(35));
                profile.setSd(cursor.getInt(36));
                profile.setNlt(cursor.getInt(37));
                profile.setS2w(cursor.getInt(38)*/
                
                // Adding contact to list
                profileList.add(profile);
            } while (cursor.moveToNext());
        }

        // return contact list
        return profileList;
    }

    // Updating single contact
    public int updateProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_NAME, profile.getName());
        values.put(KEY_PROFILE_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_PROFILE_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_PROFILE_CPU1MAX, profile.getCpu1max());
        values.put(KEY_PROFILE_CPU1MIN, profile.getCpu1min());
        values.put(KEY_PROFILE_CPU2MIN, profile.getCpu2min()); 
        values.put(KEY_PROFILE_CPU2MAX, profile.getCpu2max()); 
        values.put(KEY_PROFILE_CPU3MAX, profile.getCpu3max());
        values.put(KEY_PROFILE_CPU3MIN, profile.getCpu3min());
        values.put(KEY_PROFILE_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_PROFILE_CPU1GOV, profile.getCpu1gov());
        values.put(KEY_PROFILE_CPU2GOV, profile.getCpu2gov());
        values.put(KEY_PROFILE_CPU3GOV, profile.getCpu3gov());
		values.put(KEY_PROFILE_VOLTAGE,profile.getVoltage());
        values.put(KEY_PROFILE_MTD, profile.getMtd());
        values.put(KEY_PROFILE_MTU, profile.getMtu());
        values.put(KEY_PROFILE_GPU2D, profile.getGpu2d());
        values.put(KEY_PROFILE_GPU3D, profile.getGpu3d());
		values.put(KEY_PROFILE_BUTTONS_BACKLIGHT, profile.getButtonsLight());
        values.put(KEY_PROFILE_VSYNC, profile.getVsync());
        values.put(KEY_PROFILE_F_CHARGE, profile.getFcharge());
        values.put(KEY_PROFILE_CDEPTH, profile.getCdepth());
        values.put(KEY_PROFILE_IOSCHEDULER, profile.getIoScheduler());
        values.put(KEY_PROFILE_SDCACHE, profile.getSdcache());
        values.put(KEY_PROFILE_NOTIFICATION_LED, profile.getNotificationLed());
        values.put(KEY_PROFILE_SWEEP2WAKE, profile.getSweep2wake());
        /*values.put(KEY_PROFILE_CPU, profile.getCpu());
        values.put(KEY_PROFILE_VT, profile.getVt());
        values.put(KEY_PROFILE_MD, profile.getMd());
        values.put(KEY_PROFILE_GPU, profile.getGpu());
        values.put(KEY_PROFILE_CBL, profile.getCbl());
        values.put(KEY_PROFILE_VS, profile.getVs());
        values.put(KEY_PROFILE_FC, profile.getFc());
        values.put(KEY_PROFILE_CD, profile.getCd());
        values.put(KEY_PROFILE_IO, profile.getIo());
        values.put(KEY_PROFILE_SD, profile.getSd());
        values.put(KEY_PROFILE_NLT, profile.getNlt());
        values.put(KEY_PROFILE_S2W, profile.getS2w());*/
        
        // updating row
        return db.update(TABLE_PROFILES, values, KEY_PROFILE_ID + " = ?",
						 new String[] { String.valueOf(profile.getID()) });
    }

    // Deleting single profile
    public void deleteProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_PROFILE_ID + " = ?",
				  new String[] { String.valueOf(profile.getID()) });
        db.close();
    }

    public void deleteProfileByName(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_PROFILE_NAME + " = ?",
				  new String[] { String.valueOf(profile.getName()) });
        db.close();
    }

    // Getting profile Count
    public int getProfileCount()
	{
        String countQuery = "SELECT  * FROM " + TABLE_PROFILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    
    void addVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();

           ContentValues values = new ContentValues();
           values.put(KEY_VOLTAGE_NAME, voltage.getName());
           values.put(KEY_VOLTAGE_FREQ, voltage.getFreq());
           values.put(KEY_VOLTAGE_VALUE, voltage.getValue()); 
         

           // Inserting Row
           db.insert(TABLE_VOLTAGE, null, values);
           db.close(); // Closing database connection
       }

       // Getting single 
       Voltage getVoltage(int id)
   	{
           SQLiteDatabase db = this.getReadableDatabase();

           Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
   									 KEY_VOLTAGE_NAME,
   									 KEY_VOLTAGE_FREQ,
   									 KEY_VOLTAGE_VALUE}, KEY_VOLTAGE_ID + "=?",
   								 new String[] { String.valueOf(id) }, null, null, null, null);
           if (cursor != null)
               cursor.moveToFirst();

           Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
   									  cursor.getString(1),
   									  cursor.getString(2),
   									cursor.getString(3));
           // return 
           return voltage;
       }

       Voltage getVoltageByName(String Name)
   	{
           SQLiteDatabase db = this.getReadableDatabase();

           Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
        		   						KEY_VOLTAGE_NAME,
        		   						KEY_VOLTAGE_FREQ,
   									 KEY_VOLTAGE_VALUE }, KEY_VOLTAGE_NAME + "=?",
   								 new String[] { Name }, null, null, null, null);
           if (cursor != null)
               cursor.moveToFirst();

           Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
   									  cursor.getString(1),
   									  cursor.getString(2),
   									cursor.getString(3));
           // return 
           return voltage;
       }
       
       Voltage getVoltageByFreq(String freq)
      	{
              SQLiteDatabase db = this.getReadableDatabase();

              Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
           		   						KEY_VOLTAGE_NAME,
           		   						KEY_VOLTAGE_FREQ,
      									 KEY_VOLTAGE_VALUE }, KEY_VOLTAGE_FREQ + "=?",
      								 new String[] { freq }, null, null, null, null);
              if (cursor != null)
                  cursor.moveToFirst();

              Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
      									  cursor.getString(1),
      									  cursor.getString(2),
      									cursor.getString(3));
              // return 
              return voltage;
          }

       // Getting All 
       public List<Voltage> getAllVoltages()
   	{
           List<Voltage> voltageList = new ArrayList<Voltage>();
           // Select All Query
           String selectQuery = "SELECT  * FROM " + TABLE_VOLTAGE;

           SQLiteDatabase db = this.getWritableDatabase();
           Cursor cursor = db.rawQuery(selectQuery, null);

           // looping through all rows and adding to list
           if (cursor.moveToFirst())
   		{
               do {
   				Voltage voltage = new Voltage();
                   voltage.setID(Integer.parseInt(cursor.getString(0)));
                   voltage.setName(cursor.getString(1));
                   voltage.setFreq(cursor.getString(2));
                   voltage.setValue(cursor.getString(3));
                   
                   // Adding  to list
                   voltageList.add(voltage);
               } while (cursor.moveToNext());
           }

           // return  list
           return voltageList;
       }

       // Updating single 
       public int updateVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();

           ContentValues values = new ContentValues();
           values.put(KEY_VOLTAGE_NAME, voltage.getFreq());
           values.put(KEY_VOLTAGE_FREQ, voltage.getFreq());
           values.put(KEY_VOLTAGE_VALUE, voltage.getValue());

           // updating row
           return db.update(TABLE_VOLTAGE, values, KEY_VOLTAGE_ID + " = ?",
   						 new String[] { String.valueOf(voltage.getID()) });
       }

       // Deleting single 
       public void deleteVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();
           db.delete(TABLE_VOLTAGE, KEY_VOLTAGE_ID + " = ?",
   				  new String[] { String.valueOf(voltage.getID()) });
           db.close();
       }

       public void deleteVoltageByName(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();
           db.delete(TABLE_VOLTAGE, KEY_VOLTAGE_NAME + " = ?",
   				  new String[] { String.valueOf(voltage.getName()) });
           db.close();
       }

       // Getting  Count
       public int getVoltageCount()
   	{
           String countQuery = "SELECT  * FROM " + TABLE_VOLTAGE;
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor cursor = db.rawQuery(countQuery, null);
           cursor.close();

           // return count
           return cursor.getCount();
       }

}
