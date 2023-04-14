package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(name = "address", nullable = false)
    private String address;

    @Embedded
    private GeoPoint geoPoint;

    public Address(final String address, final GeoPoint geoPoint) {
        checkIsNull(address);
        this.address = address;
        this.geoPoint = geoPoint;
    }

    private void checkIsNull(final String address) {
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
