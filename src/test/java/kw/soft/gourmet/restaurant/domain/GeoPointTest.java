package kw.soft.gourmet.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.restaurant.GeoPoint;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GeoPointTest {

    @ParameterizedTest
    @ValueSource(doubles = {-90.1,90.1})
    @DisplayName("위도값이 유효한 범위를 벗어난 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenLatitudeIsInvalidRange(double latitude) {
        //given
        double validLongitude = 90.0;
        //then
        assertThatThrownBy(() -> new GeoPoint(latitude, validLongitude))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_GEOPOINT);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-180.1,180.1})
    @DisplayName("경도값이 유효한 범위를 벗어난 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenLongitudeIsInvalidRange(double longitude) {
        //given
        double validLatitude = 45.0;
        //then
        assertThatThrownBy(() -> new GeoPoint(validLatitude, longitude))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_GEOPOINT);
    }

}