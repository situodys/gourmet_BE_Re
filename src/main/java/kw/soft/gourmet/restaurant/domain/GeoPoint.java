package kw.soft.gourmet.restaurant.domain;

import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;
import lombok.Getter;

@Getter
public class GeoPoint {
    private static final double MAX_LAT = 90.0;
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LNG = 180.0;
    private static final double MIN_LNG = -180.0;

    private double latitude;
    private double longitude;

    public GeoPoint(double latitude, double longitude) {
        checkValidLatRange(latitude);
        checkValidLngRange(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void checkValidLatRange(double latitude) {
        if (latitude < MIN_LAT || latitude > MAX_LAT) {
            throw new RestaurantException(Code.INVALID_GEOPOINT);
        }
    }

    private void checkValidLngRange(double longitude) {
        if (longitude < MIN_LNG || longitude > MAX_LNG) {
            throw new RestaurantException(Code.INVALID_GEOPOINT);
        }
    }


}
