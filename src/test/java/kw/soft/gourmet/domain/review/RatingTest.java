package kw.soft.gourmet.domain.review;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.review.exception.Code;
import kw.soft.gourmet.domain.review.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RatingTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    @DisplayName("평점이 유효한 범위 내 값이 아닐 경우 예외를 발생시킨다")
    public void throwsExceptionWhenValueIsOutOfValidRange(int value) throws Exception {
        //then
        assertThatThrownBy(() -> new Rating(value))
                .isInstanceOf(ReviewException.class)
                .extracting("code").isEqualTo(Code.INVALID_RATING_RANGE);
    }
}
