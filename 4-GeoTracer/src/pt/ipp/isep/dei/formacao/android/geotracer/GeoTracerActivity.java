package pt.ipp.isep.dei.formacao.android.geotracer;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GeoTracerActivity extends MapActivity {

    // "camadas" a apresentar sobre o mapa
    // neste caso, apenas uma para o utilizador
    private List<Overlay> overlayList;
    private MapView mapView;

    private LocationManager mLocationManager;

    // dialog para alertar o utilizador da falta de ligação à internet
    private AlertDialog onlineDialog = null;
        // e para a indisponibilidade do GPS
    private AlertDialog gpsDialog = null;

    private final static String TAG = "GEOTRACER_ACTIVITY";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (!isInternetActive()) {
            // apresenta uma notificação caso não exista
            // ligação à internet
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.noconnection_label)
                    .setCancelable(false)
                    // acção do botão Ok
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // TODO somehow handle lack of connection
                                    // e.g.
                                    // GeoTracerActivity.this.finish();
                                }
                            });
            onlineDialog = builder.create();
            onlineDialog.show();
        }

        mapView = (MapView) findViewById(R.id.myMapView);

        // centra o mapa em Portugal
        GeoPoint myGeoPoint = new GeoPoint((int) (39.399872 * 1E6),
                (int) (-8.224454 * 1E6));
        mapView.getController().animateTo(myGeoPoint);

        // nível de zoom
        mapView.getController().setZoom(14); // 1-15

        // controlos de zoom na MapView
        mapView.setBuiltInZoomControls(true);

        overlayList = mapView.getOverlays();
    }
    
    @Override
    protected void onResume() {
        // Localização
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location mLocation = null;

        // Verificar se o GPS está Activo
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // 2 seg ou 10m
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2000, 10, mLocationListener);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.nogps_label)
                    .setCancelable(false)
                    // acção do botão Ok
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // TODO somehow handle lack of gps
                                }
                            });
            gpsDialog = builder.create();
            gpsDialog.show();
        }

        mLocation = mLocationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (mLocation == null)
            mLocation = mLocationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        drawUserLocation(mLocation);

        super.onResume();
    }

    @Override
    protected void onPause() {

        mLocationManager.removeUpdates(mLocationListener);

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        // destrói a dialog de alerta, se existir
        if (onlineDialog != null) {
            onlineDialog.dismiss();
            onlineDialog = null;
        }
        if (gpsDialog != null) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }

        Log.d(TAG, "onDestroy called.");

        super.onDestroy();
    }

    private boolean isInternetActive() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // 0 - representa Internet Móvel; 1 - Representa Wi-Fi
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                && connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
            return false;
        else
            return true;

        // como alternativa pode-se utilizar
        // return cm.getActiveNetworkInfo() != null &&
        // cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    protected void drawUserLocation(Location l) {

        if (l == null)
            return;

        GeoPoint gp = new GeoPoint((int) (l.getLatitude() * 1E6),
                (int) (l.getLongitude() * 1E6));

        overlayList.clear();

        OverlayItem overlayItem = new OverlayItem(gp, "User Location", null);

        int iconId = getResources().getIdentifier("ic_person", "drawable",
                getPackageName());

        Drawable iconDrawable = getResources().getDrawable(iconId);

        // adiciona o overlay à MapView geralmente cada overlay
        // possui um marker associado
        // neste caso está a ser criado um overlay para a posição do utilizador
        // em que o marker é o ícone
        GeoTracerOverlay overlay = new GeoTracerOverlay(iconDrawable);
        overlay.addOverlay(overlayItem);
        overlayList.add(overlay);

        mapView.getController().animateTo(gp);
    }
    
    private final LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            drawUserLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
}
