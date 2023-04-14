package kw.soft.gourmet.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.restaurant.PhoneNumber;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PhoneNumberTest {

    @ParameterizedTest
    @ValueSource(strings = {""," ", "123-123-123","01234-123-123","0-0-0","02-12345-123","031-123-12345","abc-1234-1234" })
    @NullSource
    @DisplayName("전화번호가 유효하지 않은 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenPhoneNumberIsInvalid(String phoneNumber) throws Exception {
        //then
        assertThatThrownBy(() -> new PhoneNumber(phoneNumber))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_PHONE_NUMBER);
    }


}