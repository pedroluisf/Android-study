package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

public class Country {

	private long id;
	private String name;
	private String iso;
	private float latitude, longitude;

	public final static String TBL_NAME = "tbl_country";
	public final static String ID = "id_country";
	public final static String ISO = "ISO";
	public final static String NAME = "name";
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";
	public final static String[] FIELDS = { ID, NAME, ISO, LATITUDE, LONGITUDE };

	public static ArrayList<Country> getAll(IDatabase db) {
		ArrayList<Country> myCountries = new ArrayList<Country>();
		Cursor c = db.getEntry(TBL_NAME, FIELDS, null, NAME);
        if (c.moveToFirst()) {
            do {
                Country cnt = new Country(c.getLong(0), c.getString(1), c.getString(2), c.getFloat(3), c.getFloat(4));
                myCountries.add(cnt);
            } while (c.moveToNext());
        }
        c.close();        
        return myCountries;
	}

	public static Country get(long id, IDatabase db) {
		Country cnt = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID);
		whereClause.append(" = ");
		whereClause.append(id);
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), NAME);
        if (c.moveToFirst()) {
            cnt = new Country(c.getLong(0), c.getString(1), c.getString(2), c.getFloat(3), c.getFloat(4));
        }
        c.close();        
        return cnt;
	}

	public static Country getByName(String name, IDatabase db) {
		Country cnt = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(NAME);
		whereClause.append(" = '");
		whereClause.append(name);
		whereClause.append("'");
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), NAME);
        if (c.moveToFirst()) {
            cnt = new Country(c.getLong(0), c.getString(1), c.getString(2), c.getFloat(3), c.getFloat(4));
        }
        c.close();        
        return cnt;
	}

	public static Country getByISO(String iso, IDatabase db) {
		Country cnt = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ISO);
		whereClause.append(" = '");
		whereClause.append(iso);
		whereClause.append("'");
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), NAME);
        if (c.moveToFirst()) {
            cnt = new Country(c.getLong(0), c.getString(1), c.getString(2), c.getFloat(3), c.getFloat(4));
        }
        c.close();        
        return cnt;
	}

	public static void create(String name, String iso, float lat, float lon, IDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(ISO, iso);
		cv.put(LATITUDE, lat);
		cv.put(LONGITUDE, lon);
		db.insertEntry(TBL_NAME, cv);
		cv = null;
	}

	public static void delete(long id, IDatabase db) {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID);
		whereClause.append(" = ");
		whereClause.append(id);
		db.removeEntry(TBL_NAME, whereClause.toString());
	}

	public Country(long id, String name, String iso, float latitude,
			float longitude) {
		super();
		this.id = id;
		this.name = name;
		this.iso = iso;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIso() {
		return iso;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

}
