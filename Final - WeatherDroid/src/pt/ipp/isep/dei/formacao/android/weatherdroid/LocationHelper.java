package pt.ipp.isep.dei.formacao.android.weatherdroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {

	private Context context;
	private Location myLocation;
	private final LocationListener mLocationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			myLocation = location;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private static LocationManager mLocationManager;

	public LocationHelper(Context context) {
		this.context = context;
	}

	public Location getCurrentLocation() {

		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// Try GPS
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			// 2 seg ou 10m
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 2000, 10, mLocationListener);

			myLocation = mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		}

		// Now Go Network
		if (myLocation == null) {

			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

			myLocation = mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}

		// Remove Updates (must save batery!!!)
		mLocationManager.removeUpdates(mLocationListener);

		return myLocation;
		
	}

}
