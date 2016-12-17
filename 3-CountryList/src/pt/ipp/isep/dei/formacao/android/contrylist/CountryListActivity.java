package pt.ipp.isep.dei.formacao.android.contrylist;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CountryListActivity extends ListActivity {

    private final static String TAG = "COUNTRYLIST_ACTIVITY";

    private ArrayList<String[]> countries = new ArrayList<String[]>();
    private CountryListAdapter listAdapter = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called.");

        setContentView(R.layout.main);

        // cria o adapter que irá controlar a lista e indica qual o array
        // que contém a informação (countries)
        listAdapter = new CountryListAdapter(CountryListActivity.this,
                countries);
        setListAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume called.");

        // actualiza o array de países com base no filtro por continente
        loadCountriesFromDatabase(getContinentFilter());

        // notifica o adapter que é necessário actualizar a interface
        listAdapter.notifyDataSetChanged();
    }

    protected void loadCountriesFromDatabase(String continentFilter) {
        Database db = new Database(this);
        db.open();

        countries.clear();

        Cursor c = db.getAllEntriesRawQuery("SELECT * FROM " 
                + Database.TBL_COUNTRY
                + (continentFilter != null ? " WHERE continent='" 
                        + continentFilter + "'" : "") + " ORDER BY " 
                + Database.FLD_NAME + " ASC");

        if (c.moveToFirst()) {
            do {
                String[] country = new String[2];
                country[0] = c.getString(1);
                country[1] = c.getString(2);
                countries.add(country);
            } while (c.moveToNext());
        }
        c.close();

        db.close();
        db = null;
    }

    // invocado quando o utilizador clica num país da ListView
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Toast t = Toast.makeText(this,
                "You clicked on " + countries.get(position)[0],
                Toast.LENGTH_LONG);
        t.show();

        super.onListItemClick(l, v, position, id);
    }
    
    protected String getContinentFilter() {
        // acesso às preferências do utilizador para esta aplicação
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        // obtém o valor definido para o filtro por continente
        // guardado nas preferências
        String continentFilter = prefs.getString(
                CountryPreferenceActivity.CONTINENT_PREF,
                CountryPreferenceActivity.DEFAULT_CONTINENT);
        return continentFilter
                .compareToIgnoreCase(CountryPreferenceActivity.DEFAULT_CONTINENT) == 0 ? null
                : continentFilter;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called.");

        menu.setQwertyMode(true);

        // Menu.add(int groupId, int itemId, int order, int titleRes)

        // opção "refresh" 
        MenuItem mnu1 = menu.add(0, 0, 0, R.string.refresh_label);
        mnu1.setAlphabeticShortcut('r');
        mnu1.setIcon(R.drawable.ic_menu_refresh);

        // opção "preferências" 
        MenuItem mnu2 = menu.add(0, 1, 0, R.string.preferences_label);
        mnu2.setAlphabeticShortcut('m');
        mnu2.setIcon(R.drawable.ic_menu_preferences);

        return super.onCreateOptionsMenu(menu);
    }

    // executado quando o utilizador selecciona um item no menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0: // refresh
            loadCountriesFromDatabase(getContinentFilter());
            listAdapter.notifyDataSetChanged();
        return true;

        case 1: // preferências (lança intent para avançar para PreferenceActivity)
            Intent newIntent = new Intent(CountryListActivity.this,
                    CountryPreferenceActivity.class);
            startActivity(newIntent);
        return true;
        }

        return false;
    }
}
