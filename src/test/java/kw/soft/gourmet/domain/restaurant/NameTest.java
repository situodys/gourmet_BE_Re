package kw.soft.gourmet.domain.restaurant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.restaurant.Name;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    @DisplayName("이름이 빈 값 이거나 공백인 경우 예외를 발생시킨다.")
    public void if_value_is_null_or_empty_then_throw_exception(String value) throws Exception {
        //then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.EMPTY_NAME);
    }

    @Test
    @DisplayName("이름이 50자보다 긴 경우 예외를 발생시킨다")
    public void if_value_length_is_longer_than_MAX_SIZE_then_throw_exception() throws Exception{
        //given
        String overMaxLengthValue = "가".repeat(51);
        //then
        assertThatThrownBy(() -> new Name(overMaxLengthValue))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_NAME_LENGTH);
    }

}
