package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import pt.ipp.isep.dei.formacao.android.weatherdroid.DateContainer.DateContainerType;
import android.content.ContentValues;
import android.database.Cursor;

// representa uma previs�o meteorol�gica
public class WeatherCondition {

	private long placeId;
	private DateContainer date;

	private int currentTemperature;
	private int minTemperature;
	private int maxTemperature;

	private String description;
	private String observationTime;

	private int windSpeedMiles;
	private int windSpeedKmph;
	private String windDirection;

	private float precipitationMM;
	private int humidity;

	// URL do �cone da previs�o
	private String icon;

	public static String TBL_NAME = "tbl_weather_condition";
	public static String ID_PLACE = "id_place";
	public static String DATE_CONDITION = "date_condition";
	public static String TEMPERATURE = "temperature";
	public static String MIN_TEMPERATURE = "min_temperature";
	public static String MAX_TEMPERATURE = "max_temperature";
	public static String DESCRIPTION = "description";
	public static String OBSERVATION_TIME = "observation_time";
	public static String WINDSPEED_MILES = "windspeed_miles";
	public static String WINDSPEED_KMPH = "windspeed_kmph";
	public static String WIND_DIRECTION = "wind_direction";
	public static String PRECIPITATION = "precipitation";
	public static String HUMIDITY = "humidity";
	public static String ICON = "icon";
	public static String[] FIELDS = { ID_PLACE, DATE_CONDITION, TEMPERATURE,
			MIN_TEMPERATURE, MAX_TEMPERATURE, DESCRIPTION, OBSERVATION_TIME,
			WINDSPEED_MILES, WINDSPEED_KMPH, WIND_DIRECTION, PRECIPITATION,
			HUMIDITY, ICON };

	public static ArrayList<WeatherCondition> getForPlace(long id_place, IDatabase db) {
		ArrayList<WeatherCondition> myWeatherConds = new ArrayList<WeatherCondition>();
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID_PLACE);
		whereClause.append(" = ");
		whereClause.append(id_place);
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), DATE_CONDITION);
        if (c.moveToFirst()) {
            do {
            	WeatherCondition w = new WeatherCondition();
            	w.setPlaceId(c.getLong(0)); 
            	w.setDate(new DateContainer(DateContainerType.DATE, c.getString(1)));
            	w.setCurrentTemperature(c.getInt(2));
            	w.setMinTemperature(c.getInt(3));
            	w.setMaxTemperature(c.getInt(4));
            	w.setDescription(c.getString(5));
            	w.setObservationTime(c.getString(6));
            	w.setWindSpeedMiles(c.getInt(7));
            	w.setWindSpeedKmph(c.getInt(8));
            	w.setWindDirection(c.getString(9));
            	w.setPrecipitationMM(c.getFloat(10));
            	w.setHumidity(c.getInt(11));
                myWeatherConds.add(w);
            } while (c.moveToNext());
        }
        c.close();        
        return myWeatherConds;
	}

	public static WeatherCondition getCurrentForPlace(long id_place, IDatabase db) {
    	WeatherCondition w = null;
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(ID_PLACE);
		whereClause.append(" = ");
		whereClause.append(id_place);
		whereClause.append(" AND ");
		whereClause.append(DATE_CONDITION);
		whereClause.append(" = '");
		whereClause.append(new DateContainer(DateContainerType.DATE).getDateTimeString());
		whereClause.append("' ");
		Cursor c = db.getEntry(TBL_NAME, FIELDS, whereClause.toString(), DATE_CONDITION);
        if (c.moveToFirst()) {
        	w = new WeatherCondition();
        	w.setPlaceId(c.getLong(0)); 
        	w.setDate(new DateContainer(DateContainerType.DATE, c.getString(1)));
        	w.setCurrentTemperature(c.getInt(2));
        	w.setMinTemperature(c.getInt(3));
        	w.setMaxTemperature(c.getInt(4));
        	w.setDescription(c.getString(5));
        	w.setObservationTime(c.getString(6));
        	w.setWindSpeedMiles(c.getInt(7));
        	w.setWindSpeedKmph(c.getInt(8));
        	w.setWindDirection(c.getString(9));
        	w.setPrecipitationMM(c.getFloat(10));
        	w.setHumidity(c.getInt(11));
        	w.setIcon(c.getString(12));
        }
        c.close();        
        return w;
	}

	public static void create(long id_place, DateContainer date_condition,
			Integer temperature, Integer min_temperature,
			Integer max_temperature, String description,
			String observation_time, Integer windspeed_miles,
			Integer windspeed_kmph, String wind_direction, Float precipitation,
			Integer humidity, String icon, IDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(ID_PLACE, id_place);
		cv.put(DATE_CONDITION, date_condition.getDateTimeString());
		cv.put(TEMPERATURE, temperature);
		cv.put(MIN_TEMPERATURE, min_temperature);
		cv.put(MAX_TEMPERATURE, max_temperature);
		cv.put(DESCRIPTION, description);
		cv.put(OBSERVATION_TIME, observation_time);
		cv.put(WINDSPEED_MILES, windspeed_miles);
		cv.put(WINDSPEED_KMPH, windspeed_kmph);
		cv.put(WIND_DIRECTION, wind_direction);
		cv.put(PRECIPITATION, precipitation);
		cv.put(HUMIDITY, humidity);
		cv.put(ICON, icon);
		create(cv, db);
		cv = null;
	}

	public static void create(ContentValues cv, IDatabase db) {
		db.insertEntry(TBL_NAME, cv);
	}

	public static void deleteAllForPlace(long id_place, IDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append(ID_PLACE);
		sb.append(" = ");
		sb.append(id_place);
		db.removeEntry(TBL_NAME, sb.toString());
	}

	public WeatherCondition() {
	}

	public WeatherCondition(long id_place, DateContainer date_condition,
			Integer temperature, Integer min_temperature,
			Integer max_temperature, String description,
			String observation_time, Integer windspeed_miles,
			Integer windspeed_kmph, String wind_direction, Float precipitation,
			Integer humidity, String icon) {

		this.placeId = id_place;
		this.date = date_condition;
		this.currentTemperature = temperature;
		this.minTemperature = min_temperature;
		this.maxTemperature = max_temperature;
		this.description = description;
		this.observationTime = observation_time;
		this.windSpeedMiles = windspeed_miles;
		this.windSpeedKmph = windspeed_kmph;
		this.windDirection = wind_direction;
		this.precipitationMM = precipitation;
		this.humidity = humidity;
		this.icon = icon;
	}

	public DateContainer getDate() {
		return date;
	}

	public void setDate(DateContainer date) {
		this.date = date;
	}

	public long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(long placeId) {
		this.placeId = placeId;
	}

	public int getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(int currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	public int getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
	}

	public int getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}

	public int getWindSpeedMiles() {
		return windSpeedMiles;
	}

	public void setWindSpeedMiles(int windSpeedMiles) {
		this.windSpeedMiles = windSpeedMiles;
	}

	public int getWindSpeedKmph() {
		return windSpeedKmph;
	}

	public void setWindSpeedKmph(int windSpeedKmph) {
		this.windSpeedKmph = windSpeedKmph;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public float getPrecipitationMM() {
		return precipitationMM;
	}

	public void setPrecipitationMM(float precipitationMM) {
		this.precipitationMM = precipitationMM;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
