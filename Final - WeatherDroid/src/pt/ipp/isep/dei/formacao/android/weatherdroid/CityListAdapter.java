package pt.ipp.isep.dei.formacao.android.weatherdroid;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityListAdapter extends ArrayAdapter<Place> {

    // array com os elementos da lista
    private ArrayList<Place> cities;
    // contexto (que neste caso é a CountryListActivity)
    private Context context;

	public CityListAdapter(Context context, ArrayList<Place> objects) {
        super(context, R.layout.row_layout, objects);

        this.cities = objects;
        this.context = context;
    }

    /**
     * Executado para cada cidade sempre que o mesmo necessita de ser actualizado
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

        Place city = cities.get(position);

        if (city != null) {
        	ImageView imgWeather = (ImageView) v.findViewById(R.id.imgWeather);
            TextView txtCityName = (TextView) v.findViewById(R.id.txtCityName);
            TextView txtTempCurr = (TextView) v.findViewById(R.id.txtTempCurr);
            TextView txtTempMaxMin = (TextView) v.findViewById(R.id.txtTempMaxMin);
            TextView txtWeather = (TextView) v.findViewById(R.id.txtWeather);
            TextView txtHumidity = (TextView) v.findViewById(R.id.txtHumidity);
            TextView txtPrecipitation = (TextView) v.findViewById(R.id.txtPrecipitation);

            txtCityName.setText(city.getName());
            if (city.getCurrentCondition() != null){
            	try{
                	Drawable icon = context.getResources().getDrawable(context.getResources().getIdentifier(city.getCurrentCondition().getIcon(), "drawable", context.getPackageName()));
                	if (icon != null)
                		imgWeather.setImageDrawable(icon);
            	}catch (Exception e){
                	Drawable icon = context.getResources().getDrawable(context.getResources().getIdentifier("nodata_icon", "drawable", context.getPackageName()));
                	if (icon != null)
                		imgWeather.setImageDrawable(icon);
            	}            	
                txtWeather.setText(city.getCurrentCondition().getDescription());
            	String temp = context.getString(R.string.temperature) + ": ";
            	temp += city.getCurrentCondition().getCurrentTemperature() != 0 ? city.getCurrentCondition().getCurrentTemperature() + "ºC" : "";
                txtTempCurr.setText(temp);
            	temp = city.getCurrentCondition().getMaxTemperature() != 0 ? context.getString(R.string.max_temperature) + ": " + city.getCurrentCondition().getMaxTemperature() + "ºC" : "";
            	temp += city.getCurrentCondition().getMinTemperature() != 0 ? "\n" + context.getString(R.string.min_temperature) + ": " + city.getCurrentCondition().getMinTemperature() + "ºC" : "";
                txtTempMaxMin.setText(temp);
                txtHumidity.setText(context.getString(R.string.humidity) + ": " + city.getCurrentCondition().getHumidity() + "%");
                txtPrecipitation.setText(context.getString(R.string.precipitation) + ": " + city.getCurrentCondition().getPrecipitationMM() + "mm");
            }else{
            	Drawable icon = context.getResources().getDrawable(context.getResources().getIdentifier("nodata_icon", "drawable", context.getPackageName()));
            	if (icon != null)
            		imgWeather.setImageDrawable(icon);
                txtWeather.setText(context.getString(R.string.no_data_found));
                txtHumidity.setText("");
                txtPrecipitation.setText("");
            }
        }

        return v;
    }
}
