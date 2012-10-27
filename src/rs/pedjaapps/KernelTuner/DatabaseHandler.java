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
    private static final String DATABASE_NAME = "profileManager";

    // Contacts table name
    private static final String TABLE_PROFILES = "profiles";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_CPU0MIN = "cpu0min";
    private static final String KEY_CPU0MAX = "cpu0max";
    private static final String KEY_CPU1MAX = "cpu1max";
    private static final String KEY_CPU1MIN = "cpu1min";
    private static final String KEY_CPU0GOV = "cpu0gov";
    private static final String KEY_CPU1GOV = "cpu1gov";
    private static final String KEY_GPU2D = "gpu2d";
    private static final String KEY_GPU3D = "gpu3d";
    private static final String KEY_VSYNC = "vsync";
    private static final String KEY_NOC = "number_of_cores";
    private static final String KEY_MTD = "mtd";
    private static final String KEY_MTU = "mtu";

    public DatabaseHandler(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
	{
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_CPU0MIN + " TEXT,"
			+ KEY_CPU0MAX + " TEXT," + KEY_CPU1MAX + " TEXT,"
			+ KEY_CPU1MIN + " TEXT,"
			+ KEY_CPU0GOV + " TEXT,"
			+ KEY_CPU1GOV + " TEXT,"
			+ KEY_GPU2D + " TEXT,"
			+ KEY_GPU3D + " TEXT,"
			+ KEY_VSYNC + " TEXT,"
			+ KEY_NOC + " TEXT,"
			+ KEY_MTD + " TEXT,"
			+ KEY_MTU + " TEXT"
			+
			")";
        db.execSQL(CREATE_PROFILES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, profile.getName());
        values.put(KEY_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_CPU1MAX, profile.getCpu1max());
        values.put(KEY_CPU1MIN, profile.getCpu1min());
        values.put(KEY_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_CPU1GOV, profile.getCpu1gov());
        values.put(KEY_GPU2D, profile.getGpu2d());
        values.put(KEY_GPU3D, profile.getGpu3d());
        values.put(KEY_VSYNC, profile.getVsync());
        values.put(KEY_NOC, profile.getNOC());
        values.put(KEY_MTD, profile.getMtd());
        values.put(KEY_MTU, profile.getMtu());

        // Inserting Row
        db.insert(TABLE_PROFILES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Profile getProfile(int id)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_ID,
									 KEY_NAME,
									 KEY_CPU0MIN,
									 KEY_CPU0MAX,
									 KEY_CPU1MIN,
									 KEY_CPU1MAX,
									 KEY_CPU0GOV,
									 KEY_CPU1GOV,
									 KEY_GPU2D,
									 KEY_GPU3D,
									 KEY_VSYNC,
									 KEY_NOC,
									 KEY_MTD,
									 KEY_MTU }, KEY_ID + "=?",
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
									  cursor.getString(13));
        // return contact
        return profile;
    }

    Profile getProfileByName(String name)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_ID,
									 KEY_NAME,
									 KEY_CPU0MIN,
									 KEY_CPU0MAX,
									 KEY_CPU1MIN,
									 KEY_CPU1MAX,
									 KEY_CPU0GOV,
									 KEY_CPU1GOV,
									 KEY_GPU2D,
									 KEY_GPU3D,
									 KEY_VSYNC,
									 KEY_NOC,
									 KEY_MTD,
									 KEY_MTU }, KEY_NAME + "=?",
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
									  cursor.getString(13));
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
                profile.setCpu0gov(cursor.getString(6));
                profile.setCpu1gov(cursor.getString(7));
                profile.setGpu2d(cursor.getString(8));
                profile.setGpu3d(cursor.getString(9));
                profile.setVsync(cursor.getString(10));
                profile.setNOC(cursor.getString(11));
                profile.setMtd(cursor.getString(12));
                profile.setMtu(cursor.getString(13));
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
        values.put(KEY_NAME, profile.getName());
        values.put(KEY_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_CPU1MAX, profile.getCpu1max());
        values.put(KEY_CPU1MIN, profile.getCpu1min());
        values.put(KEY_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_CPU1GOV, profile.getCpu1gov());
        values.put(KEY_GPU2D, profile.getGpu2d());
        values.put(KEY_GPU3D, profile.getGpu3d());
        values.put(KEY_VSYNC, profile.getVsync());
        values.put(KEY_NOC, profile.getNOC());
        values.put(KEY_MTD, profile.getMtd());
        values.put(KEY_MTU, profile.getMtu());

        // updating row
        return db.update(TABLE_PROFILES, values, KEY_ID + " = ?",
						 new String[] { String.valueOf(profile.getID()) });
    }

    // Deleting single contact
    public void deleteProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_ID + " = ?",
				  new String[] { String.valueOf(profile.getID()) });
        db.close();
    }

    public void deleteProfileByName(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_NAME + " = ?",
				  new String[] { String.valueOf(profile.getName()) });
        db.close();
    }

    // Getting contacts Count
    public int getProfileCount()
	{
        String countQuery = "SELECT  * FROM " + TABLE_PROFILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
