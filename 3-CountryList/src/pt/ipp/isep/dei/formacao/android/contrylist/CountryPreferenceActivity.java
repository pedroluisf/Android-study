package pt.ipp.isep.dei.formacao.android.contrylist;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class CountryPreferenceActivity extends PreferenceActivity {

    protected ArrayList<String> continents = new ArrayList<String>();

    // chave da preferência que permite definir o filtro por continente
    public static final String CONTINENT_PREF = "continent_pref";
    // opção por omissão do filtro por continente (mostrar todos)
    public static final String DEFAULT_CONTINENT = "Show All";

    private ListPreference continentPref;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // XML que define as preferências a serem apresentadas na UI da activity
        addPreferencesFromResource(R.xml.preferences);

        // carrega todos os "distinct" continentes da base de dados
        loadContinentsFromDatabase();

        String[] continentsList = continents.toArray(new String[continents
                .size()]);

        // obtém a instância da preferência
        continentPref = (ListPreference) findPreference(CONTINENT_PREF);

        // define o conjunto de key/value a partir do qual o utilizador
        // poderá escolher uma opção
        // neste caso as keys são as mesmas que os values
        continentPref.setEntries(continentsList);
        continentPref.setEntryValues(continentsList);
    }

    protected void loadContinentsFromDatabase() {
        Database db = new Database(this);
        db.open();

        continents.clear();
        // adicionar a opção por omissão que não se encontra na base de dados
        continents.add(DEFAULT_CONTINENT);

        Cursor c = db.getAllEntriesRawQuery("SELECT DISTINCT " 
                + Database.FLD_CONTINENT + " FROM " + Database.TBL_COUNTRY
                + " ORDER BY " + Database.FLD_CONTINENT + " ASC");

        if (c.moveToFirst()) {
            do {
                continents.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();

        db.close();
        db = null;
    }
}
