package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferencesHelper {

	private Context context;
    private SharedPreferences prefs;

	public UserPreferencesHelper(Context context){
		this.context = context;
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	}
	
    public boolean getLocationFilter() {
        boolean locationFilter = prefs.getBoolean(UserPreferencesActivity.LOCATION_PREF, false);
        return locationFilter;
    } 	
	
    public String getCountryFilter() {
        String countryFilter = prefs.getString(UserPreferencesActivity.COUNTRY_PREF, "1");
        return countryFilter;
    } 	
	
    public String getCityFilter() {
        String cityFilter = prefs.getString(UserPreferencesActivity.CITY_PREF, "0");
        return cityFilter;
    } 	
	
    public int getUpdateFilter() {
        int updateFilter = Integer.parseInt(prefs.getString(UserPreferencesActivity.UPDATE_PREF, "24"));
        return updateFilter;
    } 	
    
    public DateContainer getLastUpdate(){
    	DateContainer dataUltUp;

    	String lastUpdate = prefs.getString(UserPreferencesActivity.LAST_UPDATE, "");
    	if (lastUpdate.equalsIgnoreCase("")){
    		dataUltUp = new DateContainer(DateContainer.DateContainerType.DATETIME);
    		dataUltUp.getCalendar().add(Calendar.HOUR_OF_DAY, -getUpdateFilter());
    	} else {
        	dataUltUp = new DateContainer(DateContainer.DateContainerType.DATETIME, lastUpdate);
    	}
    	return dataUltUp;
    }
	
    public void setLastUpdate(){
    	DateContainer dataUltUp = new DateContainer(DateContainer.DateContainerType.DATETIME);
		prefs.edit().putString(UserPreferencesActivity.LAST_UPDATE, dataUltUp.getDateTimeString()).commit();
    }
    
    public boolean getNeedsUpdate(){
    	return prefs.getBoolean(UserPreferencesActivity.NEEDS_UPDATE, false);
    }
    
    public void setNeedsUpdate(boolean needsUpdate){
    	prefs.edit().putBoolean(UserPreferencesActivity.NEEDS_UPDATE, needsUpdate).commit();
    }
    
}
