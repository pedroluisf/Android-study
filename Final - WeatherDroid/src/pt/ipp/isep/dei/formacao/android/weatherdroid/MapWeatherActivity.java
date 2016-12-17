package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapWeatherActivity extends MapActivity {

	private ArrayList<Place> cities2Show;
	private List<Overlay> overlayList;
	private MapView mapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_weather);

		mapView = (MapView) findViewById(R.id.myMapView);

		// Centra o Mapa (Default em PT)
		float lati = (float) 39.399872;
		float longi = (float) -8.224454;
		Database db = new Database(MapWeatherActivity.this);
		db.open();
		// First get Country
		Long countryId = Long.valueOf(getIntent().getExtras().getLong(
				"countryId"));
		Country country = Country.get(countryId, db);
		// Then Get Cities
		if (country != null) {
			lati = country.getLatitude();
			longi = country.getLongitude();
			cities2Show = Place.getAllByCountry(countryId, db);
		}
		db.close();
		db = null;
		// Now center
		GeoPoint myGeoPoint = new GeoPoint((int) (lati * 1E6),
				(int) (longi * 1E6));
		mapView.getController().animateTo(myGeoPoint);

		// nível de zoom
		mapView.getController().setZoom(countryId==1 ? 8 : 7); // Portugal is small... What can I say. Let's just make an hard coded exception here :S

		// controlos de zoom na MapView
		mapView.setBuiltInZoomControls(true);

		// Satelite View
		mapView.setSatellite(true);

		overlayList = mapView.getOverlays();
	}

	@Override
	protected void onResume() {
		showWeatherIcons();
		super.onResume();
	}

	private void showWeatherIcons() {
		overlayList.clear();

		for (Place city : cities2Show) {
			
			// Get icon
			Drawable iconDrawable;
			try {
				int iconId = getResources().getIdentifier(city.getCurrentCondition().getIcon(), "drawable",
						getPackageName());
				iconDrawable = getResources().getDrawable(iconId);
			} catch (Exception e) {
				iconDrawable = getResources().getDrawable(R.drawable.nodata_icon);
			}
			
			//Get an custom overlay for each Icon to show
			List<Place> temp = new ArrayList<Place>();
			temp.add(city);
			
			//Should Have on ItemOverlay for each diferent Weather Icon and not each diferent Place... Must be improved
			ItemOverlay overlay = new ItemOverlay(this, temp, iconDrawable);

			//Add to my overlay List
			overlayList.add(overlay);
			
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
