package com.golovin.googlemapmask;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

final class MapHelper {
    static final LatLng LA_LOCATION = new LatLng(34.052235, -118.243683);

    /**
     * In kilometers.
     */
    private static final int EARTH_RADIUS = 6371;

    private MapHelper() {
        //no instance
    }

    static PolygonOptions createPolygonWithCircle(Context context, LatLng center, int radius) {

        return new PolygonOptions()
                .fillColor(ContextCompat.getColor(context, R.color.grey_500_transparent))
                .addAll(createOuterBounds())
                .addHole(createHole(center, radius))
                .strokeWidth(0);
    }

    private static List<LatLng> createOuterBounds() {
        float delta = 0.01f;

        return new ArrayList<LatLng>() {{
            add(new LatLng(90 - delta, -180 + delta));
            add(new LatLng(0, -180 + delta));
            add(new LatLng(-90 + delta, -180 + delta));
            add(new LatLng(-90 + delta, 0));
            add(new LatLng(-90 + delta, 180 - delta));
            add(new LatLng(0, 180 - delta));
            add(new LatLng(90 - delta, 180 - delta));
            add(new LatLng(90 - delta, 0));
            add(new LatLng(90 - delta, -180 + delta));
        }};
    }

    private static Iterable<LatLng> createHole(LatLng center, int radius) {
        int points = 50; // number of corners of inscribed polygon

        double radiusLatitude = Math.toDegrees(radius / (float) EARTH_RADIUS);
        double radiusLongitude = radiusLatitude / Math.cos(Math.toRadians(center.latitude));

        List<LatLng> result = new ArrayList<>(points);

        double anglePerCircleRegion = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double theta = i * anglePerCircleRegion;
            double latitude = center.latitude + (radiusLatitude * Math.sin(theta));
            double longitude = center.longitude + (radiusLongitude * Math.cos(theta));

            result.add(new LatLng(latitude, longitude));
        }

        return result;
    }
}
