package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class GeoPoint {
    private static final double MAX_LAT = 90.0;
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LNG = 180.0;
    private static final double MIN_LNG = -180.0;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    public GeoPoint(final double latitude, final double longitude) {
        checkValidLatRange(latitude);
        checkValidLngRange(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void checkValidLatRange(final double latitude) {
        if (latitude < MIN_LAT || latitude > MAX_LAT) {
            throw new RestaurantException(Code.INVALID_GEOPOINT);
        }
    }

    private void checkValidLngRange(final double longitude) {
        if (longitude < MIN_LNG || longitude > MAX_LNG) {
            throw new RestaurantException(Code.INVALID_GEOPOINT);
        }
    }


}
