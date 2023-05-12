package kw.soft.gourmet.domain.review;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.review.exception.Code;
import kw.soft.gourmet.domain.review.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContentTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    @DisplayName("내용이 빈 값 이거나 공백인 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenValueIsNullOrEmpty(String value) throws Exception {
        //then
        assertThatThrownBy(() -> new Content(value))
                .isInstanceOf(ReviewException.class)
                .extracting("code").isEqualTo(Code.EMPTY_CONTENT);
    }

    @Test
    @DisplayName("내용이 1000자보다 긴 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenValueIsOverMaxSize() throws Exception {
        //given
        String overMaxLengthValue = "가".repeat(1001);

        //then
        assertThatThrownBy(() -> new Content(overMaxLengthValue))
                .isInstanceOf(ReviewException.class)
                .extracting("code").isEqualTo(Code.INVALID_CONTENT_LENGTH);
    }
}
