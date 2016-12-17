package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CommunicationsHelper {

	private Context context;
	private String countryId;
	private String cityId;
	
	public String getCountryId() {
		return countryId;
	}

	public String getCityId() {
		return cityId;
	}

	public CommunicationsHelper(Context context) {
		this.context = context;
	}

	public boolean isInternetActive() {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 0 - representa Internet Móvel; 1 - Representa Wi-Fi
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				&& connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
			return false;
		else
			return true;

	}

	public void translateLocation(Location location){

		countryId = "";
		cityId = "";

		// If we don't have internet we won't be able to reverse geocode the
		// coordenates and therefore we can't get the correct city/country.
		if (!isInternetActive()) {
			return;
		}

		String countryReceivedIso = "";
		String cityReceivedLong = "";
		String cityReceivedSmall = "";
		
		try {

			// Prepare Request
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(
					"http://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ location.getLatitude() + ","
							+ location.getLongitude() + "&sensor=true"));

			// Perform Request
			HttpResponse response = client.execute(request);

			// Treat Response
			InputStream inputStream = response.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;
			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}

			// Get The Data we need from response
			JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
			//Test response
			if (!jsonResponse.getString("status").equalsIgnoreCase("OK"))
				return;
			//Read response
			JSONArray addresses = jsonResponse.getJSONArray("results"); 
			for (int i = 0 ; i < addresses.length(); i++ ){
				if (addresses.getJSONObject(i).getJSONArray("types").getString(0).equalsIgnoreCase("administrative_area_level_1")){
					JSONArray addressesComponents = addresses.getJSONObject(i).getJSONArray("address_components");
					for (int j = 0 ; j < addressesComponents.length(); j++ ){
						if (addressesComponents.getJSONObject(j).getJSONArray("types").getString(0).equalsIgnoreCase("country")){
							countryReceivedIso = addressesComponents.getJSONObject(j).getString("short_name");
						}
						if (addressesComponents.getJSONObject(j).getJSONArray("types").getString(0).equalsIgnoreCase("administrative_area_level_1")){
							cityReceivedLong = addressesComponents.getJSONObject(j).getString("long_name");
							cityReceivedSmall = addressesComponents.getJSONObject(j).getString("short_name");
						}
					}
					break;
				}
			}
			
		} catch (Exception e) {
			Log.e("CommunicationsHelper",
					"An Exception occured while reverse geocoding :" + e.getMessage());
			e.printStackTrace();
			return;
		}

		
		// Now look for the country / City on Database
		Database db = new Database(context);
		db.open();
		Country country = null;
		Place city = null; 
		//Get the country first
		if (!countryReceivedIso.equals("")){
			country = Country.getByISO(countryReceivedIso, db);
			if (country != null)
				countryId = String.valueOf(country.getId());
		}
		//Now go for the city
		if (!cityReceivedLong.equals("")){
			city = Place.getByName(cityReceivedLong, db);
			if (city != null)
				cityId = String.valueOf(city.getId());
		}
		//If it misses try the short one
		if (city == null && !cityReceivedSmall.equals("")){
			city = Place.getByName(cityReceivedSmall, db);
			if (city != null)
				cityId = String.valueOf(city.getId());
		}
		
		db.close();
		db = null;
		
	}
	
}
