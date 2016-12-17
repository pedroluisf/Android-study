package pt.ipp.isep.dei.formacao.android.weatherdroid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.RemoteViews;

public class WeatherWidget extends AppWidgetProvider {

	private Place mPlace = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName thisWidget = new ComponentName(context, WeatherWidget.class);
	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

	    //Get the user preferences
	    UserPreferencesHelper myPreferencesHelper = new UserPreferencesHelper(context);
	    LocationHelper myLocationHelper = new LocationHelper(context);
	    CommunicationsHelper myCommHelper = new CommunicationsHelper(context);

	    boolean mlocation = myPreferencesHelper.getLocationFilter();
        long myPlaceId = Long.parseLong(myPreferencesHelper.getCityFilter());
        long myCountryId = Long.parseLong(myPreferencesHelper.getCountryFilter());
        myPreferencesHelper = null;
        
	    //must we get geolocation?
	    if (mlocation){
	    	Location myLocation = myLocationHelper.getCurrentLocation();
	    	myCommHelper.translateLocation(myLocation);
	    	String cityId = myCommHelper.getCityId();
	    	if (cityId != "") //If we got it use it, otherwise keep values on pref
	    		myPlaceId = Long.parseLong(cityId);
	    }
	    
        //load the data 
        Database db = Database.getInstance(context);
	    db.open();

	    if (myPlaceId != 0){
	    	//If we have a selected city
		    mPlace = Place.get(myPlaceId, db);
	    } else {
	    	//If we don't have a selected city, we go to the city capital
		    mPlace = Place.getCountryCapital(myCountryId, db);
	    }
	    
	    db.close();
	    db = null;
	    
	    // executa onUpdate sempre que recebe um Intent neste caso trata-se de um Intents do tipo
	    // pt.ipp.isep.dei.formacao.android.weatherdroid.WEATHER_DISPLAY
	    // de acordo com o intent filter deste broadcast receiver
	    onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisWidget));

	    super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    // update a cada instância da widget:
	    final int N = appWidgetIds.length;
	    for (int i = 0; i < N; i++) {
	        int appWidgetId = appWidgetIds[i];
	        updateAppWidget(context, appWidgetManager, appWidgetId);
	    }
	}
	
	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
	    // cria um objecto RemoteViews com o layout da widget
	    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

	    if (mPlace != null){
		    views.setTextViewText(R.id.wid_txtCityName, mPlace.getName());
        	try	{
            	int iconId = context.getResources().getIdentifier(mPlace.getCurrentCondition().getIcon(), "drawable", context.getPackageName());
        		views.setImageViewResource(R.id.wid_imgWeather, iconId);
        	} catch (Exception e){        		
        		views.setImageViewResource(R.id.wid_imgWeather, R.drawable.nodata_icon);
        	}
        	if (mPlace.getCurrentCondition() != null){
            	views.setTextViewText(R.id.wid_txtTempMax, mPlace.getCurrentCondition().getMaxTemperature() != 0 ? context.getString(R.string.max_temperature) + ": " + mPlace.getCurrentCondition().getMaxTemperature() + "ºC" : "");
            	views.setTextViewText(R.id.wid_txtTempMin, mPlace.getCurrentCondition().getMinTemperature() != 0 ? context.getString(R.string.min_temperature) + ": " + mPlace.getCurrentCondition().getMinTemperature() + "ºC" : "");
    		    views.setTextViewText(R.id.wid_txtWeather, mPlace.getCurrentCondition().getDescription());
    		    views.setTextViewText(R.id.wid_txtHumidity, context.getString(R.string.humidity) + ": " + mPlace.getCurrentCondition().getHumidity() + "%");
    		    views.setTextViewText(R.id.wid_txtPrecipitation, context.getString(R.string.precipitation) + ": " + mPlace.getCurrentCondition().getPrecipitationMM() + "mm");
        	} else {
    		    views.setTextViewText(R.id.wid_txtWeather, context.getString(R.string.no_data_found));
    		    views.setTextViewText(R.id.wid_txtHumidity, "");
    		    views.setTextViewText(R.id.wid_txtPrecipitation, "");
        	}
	    }else{
		    views.setTextViewText(R.id.txtCityName, context.getString(R.string.no_data_found));
    		views.setImageViewResource(R.id.wid_imgWeather, R.drawable.nodata_icon);
		    views.setTextViewText(R.id.wid_txtWeather, "");
		    views.setTextViewText(R.id.wid_txtHumidity, "");
		    views.setTextViewText(R.id.wid_txtPrecipitation, "");
	    }

	    // When we click the widget, we want to open our list activity.
	    Intent launchActivity = new Intent(context, CityListActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
	    views.setOnClickPendingIntent(R.id.wid_imgWeather, pendingIntent);;
	    views.setOnClickPendingIntent(R.id.wid_txtCityName, pendingIntent);;

	    appWidgetManager.updateAppWidget(appWidgetId, views);
        
	}	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
	}

	@Override
	public void onEnabled(Context context) {
	}

	@Override
	public void onDisabled(Context context) {
	}
	
}
