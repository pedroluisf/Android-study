package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemOverlay extends ItemizedOverlay<OverlayItem> {

	private List<Place> cities = new ArrayList<Place>();
	private Context context;

	public ItemOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public ItemOverlay(Context context, List<Place> cities, Drawable marker) {
		super(boundCenterBottom(marker));
		this.context = context;
		if (cities == null) {
			this.cities = new ArrayList<Place>();
		} else {
			this.cities = cities;
		}
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		Place city = cities.get(i);
		GeoPoint geopoint = new GeoPoint((int) (city.getLatitude() * 1e6),
				(int) (city.getLongitude() * 1e6));
		String description = "";
		try {
			description = city.getCurrentCondition().getDescription();
		} catch (Exception e) {
		}
		return new OverlayItem(geopoint, city.getName(), description);
	}

	@Override
	public int size() {
		return cities.size();
	}

	@Override
	public boolean onTap(final int index) {
		Place city = cities.get(index);

		StringBuilder myMessage = new StringBuilder();
		myMessage.append(context.getString(R.string.population) + ": "
				+ String.format("%,d", city.getPopulation()) + "\n");
		WeatherCondition wc = city.getCurrentCondition();
		if (wc != null) {
			myMessage.append(wc.getDescription());
			myMessage.append(context.getString(R.string.temperature) + " "
					+ context.getString(R.string.max_temperature)
					+ Integer.valueOf(wc.getMaxTemperature()) + "º C" + " "
					+ context.getString(R.string.min_temperature)
					+ Integer.valueOf(wc.getMinTemperature()) + "º C");
			myMessage.append(context.getString(R.string.humidity) + ": "
					+ city.getCurrentCondition().getHumidity() + "%");
			myMessage.append(context.getString(R.string.precipitation) + ": "
					+ city.getCurrentCondition().getPrecipitationMM() + "mm");
		} else {
			myMessage.append(context.getString(R.string.no_data_found));
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(city.getName());
		builder.setMessage(myMessage);
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();

		return true; // we're finished handling the event
	}
}
