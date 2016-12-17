package pt.ipp.isep.dei.formacao.android.contrylist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CountryListAdapter extends ArrayAdapter<String[]> {

    // array com os elementos da lista
    private ArrayList<String[]> countries;
    // contexto (que neste caso é a WeatherListActivity)
    private Context context;

    public CountryListAdapter(Context context, ArrayList<String[]> objects) {
        super(context, R.layout.row_layout, objects);

        this.countries = objects;
        this.context = context;
    }

    /**
     * Executado para cada país sempre que o mesmo necessita de ser actualizado
     * na ListView.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // preenche a view do país com os elementos do layout "row_layout" 
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_layout, null);
        }

        String[] country = countries.get(position);

        if (country != null) {
            // obtem os elementos da view que contêm o nome e continente...
            TextView txtName = (TextView) v.findViewById(R.id.txtCountryName);
            TextView txtContinent = (TextView) v
                    .findViewById(R.id.txtContinentName);

            // ... e define os seus valores
            txtName.setText(country[0]);
            txtContinent.setText(country[1]);
        }

        return v;
    }
}
