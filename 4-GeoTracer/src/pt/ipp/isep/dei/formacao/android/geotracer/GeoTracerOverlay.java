package pt.ipp.isep.dei.formacao.android.geotracer;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class GeoTracerOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

    public GeoTracerOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }

    public void addOverlay(OverlayItem overlay) {
        overlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlays.get(i);
    }

    @Override
    public int size() {
        return overlays.size();
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        // TODO onTap MapView
        return super.onTap(p, mapView);
    }
}
