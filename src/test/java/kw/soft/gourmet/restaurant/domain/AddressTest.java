package kw.soft.gourmet.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.restaurant.Address;
import kw.soft.gourmet.domain.restaurant.GeoPoint;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class AddressTest {

    @ParameterizedTest
    @ValueSource(strings = {""," ","       "})
    @NullSource
    @DisplayName("주소값이 존재하지 않는 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenAddressIsInvalid(String address) {
        //given
        GeoPoint geoPoint = new GeoPoint(90.0,90.0);
        //then
        assertThatThrownBy(() -> new Address(address, geoPoint))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_ADDRESS);
    }

}