package kw.soft.gourmet.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.menu.exception.Code;
import kw.soft.gourmet.domain.menu.exception.MenuException;
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
    public void throwsExceptionWhenValueIsNullOrEmpty(String value) throws Exception {
        //then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(MenuException.class)
                .extracting("code").isEqualTo(Code.EMPTY_NAME);
    }

    @Test
    @DisplayName("이름이 100자보다 긴 경우 예외를 발생시킨다")
    public void throwsExceptionWhenValueIsOverMaxSize() throws Exception{
        //given
        String overMaxLengthValue = "가".repeat(101);

        //then
        assertThatThrownBy(() -> new Name(overMaxLengthValue))
                .isInstanceOf(MenuException.class)
                .extracting("code").isEqualTo(Code.INVALID_NAME_LENGTH);
    }
}
