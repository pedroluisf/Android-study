package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class UserPreferencesActivity extends PreferenceActivity {

	protected ArrayList<Country> countries = new ArrayList<Country>();
	protected ArrayList<Place> cities = new ArrayList<Place>();

	// chave das preferências
	public static final String LOCATION_PREF = "use_location_pref";
	public static final String COUNTRY_PREF = "country_pref";
	public static final String CITY_PREF = "city_pref";
	public static final String UPDATE_PREF = "auto_freq_pref";
	public static final String LAST_UPDATE = "last_update";
	public static final String NEEDS_UPDATE = "needs_update";
	
	private CheckBoxPreference locationPref;
	private ListPreference countryPref;
	private ListPreference cityPref;
	private ListPreference updatePref;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// XML que define as preferências a serem apresentadas na UI da activity
		addPreferencesFromResource(R.xml.preferences);

		// obtém a instância das preferências
		locationPref = (CheckBoxPreference) findPreference(LOCATION_PREF);
		countryPref = (ListPreference) findPreference(COUNTRY_PREF);
		cityPref = (ListPreference) findPreference(CITY_PREF);
		updatePref = (ListPreference) findPreference(UPDATE_PREF);

		loadDataFromDatabase(true);
		countryPref.setEnabled(!locationPref.isChecked());
		cityPref.setEnabled(!locationPref.isChecked());
		
		//Implement listener to reload Cities whenever country is changed
		countryPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						//If we change country
						if (!preference.getSharedPreferences().getString(COUNTRY_PREF, "1").equals((String)newValue)) {
							new UserPreferencesHelper(UserPreferencesActivity.this).setNeedsUpdate(true);
						}
						loadDataFromDatabase(false);
						return true;
					}
				});

		locationPref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						// No Point letting user change settings when using
						// location
						countryPref.setEnabled(!locationPref.isChecked());
						cityPref.setEnabled(!locationPref.isChecked());
						return true;
					}
				});

	}

	protected void loadDataFromDatabase(boolean loadCountries) {
		Database db = new Database(this);
		db.open();

		// Let's get the countries available
		countries = Country.getAll(db);

		// Now for the cities (Default PT)
		int countryId = countryPref.getValue() != null ? Integer.parseInt(countryPref.getValue()) : 1;
		cities = Place.getAllByCountry(countryId , db);

		db.close();
		db = null;
		
		//Only if needed
		if (loadCountries) 
			setEntriesForCountries();

		//These are always needed
		setEntriesForCities();
		setEntriesForAutomaticUpdates();
		
	}

	protected void setEntriesForCountries() {

		int mySize = countries.size();
		String[] labelsList = new String[mySize];
		String[] idsList = new String[mySize];

		for (int i = 0; i < mySize; i++) {
			labelsList[i] = countries.get(i).getName();
			idsList[i] = String.valueOf(countries.get(i).getId());
		}

		// define o conjunto de key/value a partir do qual o utilizador
		// poderá escolher uma opção
		// neste caso as keys são as mesmas que os values
		countryPref.setEntries(labelsList); // Lista a mostrar ao user (Descriptions)
		countryPref.setEntryValues(idsList); // Lista com os valores respectivos (IDs)

	}

	protected void setEntriesForCities() {

		int mySize = cities.size();
		String[] labelsList = new String[mySize];
		String[] idsList = new String[mySize];

		for (int i = 0; i < mySize; i++) {
			labelsList[i] = cities.get(i).getName();
			idsList[i] = String.valueOf(cities.get(i).getId());
		}

		// define o conjunto de key/value a partir do qual o utilizador
		// poderá escolher uma opção
		// neste caso as keys são as mesmas que os values
		cityPref.setEntries(labelsList); // Lista a mostrar ao user (Descriptions)
		cityPref.setEntryValues(idsList); // Lista com os valores respectivos (IDs)

	}

	protected void setEntriesForAutomaticUpdates(){
		
		int mySize = 3;
		String[] labelsList = new String[mySize];
		String[] idsList = new String[mySize];

		labelsList[0] = "12 h";
		idsList[0] = "12";
		labelsList[1] = "24 h";
		idsList[1] = "24";
		labelsList[2] = "48 h";
		idsList[2] = "48";

		// define o conjunto de key/value a partir do qual o utilizador
		// poderá escolher uma opção
		// neste caso as keys são as mesmas que os values
		updatePref.setEntries(labelsList); // Lista a mostrar ao user (Descriptions)
		updatePref.setEntryValues(idsList); // Lista com os valores respectivos (IDs)

	}
}