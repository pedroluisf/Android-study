package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CityListActivity extends ListActivity {

	public static final String WEATHER_DISPLAY = "pt.ipp.isep.dei.formacao.android.weatherdroid.WEATHER_DISPLAY";
	private ArrayList<Place> cities = new ArrayList<Place>();
	private CityListAdapter listAdapter = null;
	private UserPreferencesHelper myPreferencesHelper;
	private LocationHelper myLocationHelper;
	private ProgressDialog progressDialog;
	private Long countryFilter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_city_list);

		myPreferencesHelper = new UserPreferencesHelper(this);
		myLocationHelper = new LocationHelper(this);

		// cria o adapter que irá controlar a lista e indica qual o array
		// que contém a informação (countries)
		listAdapter = new CityListAdapter(CityListActivity.this, cities);
		setListAdapter(listAdapter);

		// Send the broadcast for Widget
		Intent mIntent = new Intent(WEATHER_DISPLAY);
		this.sendBroadcast(mIntent);

		loadCitiesFromDatabase();

	}

	@Override
	protected void onResume() {

		try {
			stopService(new Intent(this, DownloadService.class));
		} catch (Exception e) {
		}

		// Start the Service that will update Weather Conditions
		startService(new Intent(this, DownloadService.class));
		
		super.onResume();
	}

	protected void loadCitiesFromDatabase() {

		// Show progress dialog
		progressDialog = ProgressDialog.show(this, "",
				getString(R.string.loading));

		// Get location if we need it
		Location location = null;
		if (myPreferencesHelper.getLocationFilter()) {
			location = myLocationHelper.getCurrentLocation();
		}

		// Now load info
		LoadCitiesTask call = new LoadCitiesTask();
		call.execute(location);

	}

	private class LoadCitiesTask extends AsyncTask<Location, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(Location... params) {

			// Get Country from preferences
			countryFilter = Long.parseLong(myPreferencesHelper
					.getCountryFilter());

			// If we use location, update the Country
			if (myPreferencesHelper.getLocationFilter()) {

				CommunicationsHelper MyComHelper = new CommunicationsHelper(
						CityListActivity.this);

				MyComHelper.translateLocation(params[0]);

				if (!MyComHelper.getCountryId().equals("")) {
					countryFilter = Long.valueOf(MyComHelper.getCountryId());
				} else {
					publishProgress(false);
				}

			}

			// Load Cities of designated country
			Database db = new Database(CityListActivity.this);
			db.open();

			ArrayList<Place> newPlaces = Place.getAllByCountry(countryFilter,
					db);

			cities.clear();
			for (Place p : newPlaces) {
				cities.add(p);
			}

			db.close();
			db = null;

			return true;

		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			if (!values[0]){
				Toast.makeText(CityListActivity.this, R.string.location_failed,
					Toast.LENGTH_SHORT).show();
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			progressDialog.dismiss();
			listAdapter.notifyDataSetChanged();

		}

	}
	
	// invocado quando o utilizador clica numa cidade da ListView
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(
				this,
				cities.get(position).getName() + "\n"
						+ this.getString(R.string.population) + ": "
						+ String.format("%,d", cities.get(position).getPopulation()),
				Toast.LENGTH_LONG).show();

		super.onListItemClick(l, v, position, id);
	}

	// executado quando o utilizador pressiona a tecla "MENU"
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.setQwertyMode(true);

		// Menu.add(int groupId, int itemId, int order, int titleRes)

		// opção "refresh"
		MenuItem mnu1 = menu.add(0, 0, 0, R.string.refresh_label);
		mnu1.setAlphabeticShortcut('r');
		mnu1.setIcon(R.drawable.ic_menu_refresh);

		// opção "preferências"
		MenuItem mnu2 = menu.add(0, 1, 0, R.string.view_map_label);
		mnu2.setAlphabeticShortcut('m');
		mnu2.setIcon(R.drawable.ic_menu_mapmode);

		// opção "preferências"
		MenuItem mnu3 = menu.add(0, 2, 0, R.string.settings_label);
		mnu3.setAlphabeticShortcut('p');
		mnu3.setIcon(R.drawable.ic_menu_preferences);

		return super.onCreateOptionsMenu(menu);
	}

	// executado quando o utilizador selecciona um item no menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0: // refresh
			loadCitiesFromDatabase();
			return true;

		case 1: // preferências (lança intent para avançar para
				// PreferenceActivity)
			Intent newMapIntent = new Intent(CityListActivity.this,
					MapWeatherActivity.class);
			newMapIntent.putExtra("countryId", countryFilter);
			startActivity(newMapIntent);
			
			return true;

		case 2: // preferências (lança intent para avançar para
				// PreferenceActivity)
			Intent newPrefIntent = new Intent(CityListActivity.this,
					UserPreferencesActivity.class);
			startActivity(newPrefIntent);
			return true;

		}

		return false;
	}

}
