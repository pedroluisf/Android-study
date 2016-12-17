package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

public class Place {

	private long id;
	private long id_country;
	private int country_capital;
	private String name;
	private float latitude, longitude;
	private long population;

	private ArrayList<WeatherCondition> conditions;
	private WeatherCondition condition;

	public final static String TBL_NAME = "tbl_place";
	public final static String ID = "id_place";
	public final static String ID_COUNTRY = "id_country";
	public final static String COUNTRY_CAPITAL = "country_capital";
	public final static String NAME = "name";
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";
	public final static String POPULATION = "population";
	public final static String[] FIELDS = { ID, ID_COUNTRY, COUNTRY_CAPITAL, NAME, LATITUDE, LONGITUDE, POPULATION };

	public static ArrayList<Place> getAll(IDatabase db) {
		ArrayList<Place> myPlaces = new ArrayList<Place>();
		Cursor c = db.getEntry(TBL_NAME, FIELDS, null, NAME);
        if (c.moveToFirst()) {
            do {
                Place p = new Place(c.getLong(0), c.getLong(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getLong(6));
                myPlaces.add(p);
            } while (c.moveToNext());
        }
        c.close();        
        return myPlaces;
	}

	public static ArrayList<Place> getAllByCountry(long id_country, IDatabase db) {
		ArrayList<Place> myPlaces = new ArrayList<Place>();
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID_COUNTRY);
		whereClause.append(" = ");
		whereClause.append(id_country);
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), NAME);
        if (c.moveToFirst()) {
            do {
                Place p = new Place(c.getLong(0), c.getLong(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getLong(6));
                p.conditions = WeatherCondition.getForPlace(p.getId(), db);
                p.condition = WeatherCondition.getCurrentForPlace(p.getId(), db);
                myPlaces.add(p);
            } while (c.moveToNext());
        }
        c.close();        
        return myPlaces;
	}

	public static Place get(long id, IDatabase db) {
		Place p = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID);
		whereClause.append(" = ");
		whereClause.append(id);
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), null);
        if (c.moveToFirst()) {
            p = new Place(c.getLong(0), c.getLong(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getLong(6));
        }
        c.close();        
        if (p != null){
        	p.conditions = WeatherCondition.getForPlace(id, db);
        	p.condition = WeatherCondition.getCurrentForPlace(id, db);
        }
        return p;
	}

	public static Place getByName(String name, IDatabase db) {
		Place p = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(NAME);
		whereClause.append(" = '");
		whereClause.append(name);
		whereClause.append("'");
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), null);
        if (c.moveToFirst()) {
            p = new Place(c.getLong(0), c.getLong(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getLong(6));
        }
        c.close();        
        if (p != null){
        	p.conditions = WeatherCondition.getForPlace(p.getId(), db);
        	p.condition = WeatherCondition.getCurrentForPlace(p.getId(), db);
        }
        return p;
	}

	public static Place getCountryCapital(long id_country, IDatabase db) {
		Place p = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID_COUNTRY);
		whereClause.append(" = ");
		whereClause.append(id_country);
		whereClause.append(" AND ");
		whereClause.append(COUNTRY_CAPITAL);
		whereClause.append(" = 1");
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), null);
        if (c.moveToFirst()) {
            p = new Place(c.getLong(0), c.getLong(1), c.getInt(2), c.getString(3), c.getFloat(4), c.getFloat(5), c.getLong(6));
        }
        c.close();       
        if (p != null){
            p.conditions = WeatherCondition.getForPlace(p.getId(), db);
            p.condition = WeatherCondition.getCurrentForPlace(p.getId(), db);
        }
        return p;
	}

	public static void create(long id_country, boolean isCapital, String name, float lat, float lon, long population, IDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(ID_COUNTRY, id_country);
		cv.put(COUNTRY_CAPITAL, isCapital ? 1 : 0);
		cv.put(NAME, name);
		cv.put(LATITUDE, lat);
		cv.put(LONGITUDE, lon);
		cv.put(POPULATION, population);
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

	public Place(long id, long id_country, int country_capital, String name, float latitude, float longitude, long population) {
		this.id = id;
		this.id_country = id_country;
		this.country_capital = country_capital;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.population = population;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCountry() {
		return id_country;
	}

	public void setIdCountry(long id_country) {
		this.id_country = id_country;
	}

	public boolean isCountryCapital() {
		return country_capital == 1;
	}

	public void setCountryCapital(boolean capital) {
		this.country_capital = capital ? 1 : 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public ArrayList<WeatherCondition> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<WeatherCondition> conditions) {
		this.conditions = conditions;
	}

	public WeatherCondition getCurrentCondition() {
		return condition;
	}

	public void setCurrentCondition(WeatherCondition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "#" + id + " " + name;
	}

}
