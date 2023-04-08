package kw.soft.gourmet.restaurant.domain;

import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public class Address {
    private String address;
    private GeoPoint geoPoint;

    public Address(String address, GeoPoint geoPoint) {
        checkIsNull(address);
        this.address=address;
        this.geoPoint = geoPoint;
    }

    private void checkIsNull(String address) {
        if (address == null || address.isBlank()) {
            throw new RestaurantException(Code.INVALID_ADDRESS);
        }
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return geoPoint.getLatitude();
    }

    public double getLongitude() {
        return geoPoint.getLongitude();
    }
}
