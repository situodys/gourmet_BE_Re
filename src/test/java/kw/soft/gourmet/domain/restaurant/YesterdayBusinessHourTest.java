package kw.soft.gourmet.domain.restaurant;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class YesterdayBusinessHourTest {

    @ParameterizedTest
    @MethodSource("provideCurrentTimeAndAnswerWhenEndTomorrow")
    @DisplayName("익일 마감일 때 주어진 시간을 포함하는지 확인한다.")
    public void isGivenTimeInBusinessHourWhenBusinessHourEndTomorrow(LocalTime now, boolean answer)
            throws Exception {
        //given
        LocalTime open = LocalTime.of(18, 0);
        LocalTime close = LocalTime.of(2, 0);

        //when
        BusinessHour businessHour = new BusinessHour(open, close,false);
        businessHour.changeTimeCheckerBasedYesterday();
        boolean hasContained = businessHour.isWithinBusinessHour(now);
        //then
        assertThat(hasContained).isEqualTo(answer);
    }

    private static Stream<Arguments> provideCurrentTimeAndAnswerWhenEndTomorrow() {
        return Stream.of(
                Arguments.of(LocalTime.of(3, 0), false),
                Arguments.of(LocalTime.of(17, 0), false),
                Arguments.of(LocalTime.of(18, 0), false),
                Arguments.of(LocalTime.of(19, 0), false),
                Arguments.of(LocalTime.of(0, 0), true),
                Arguments.of(LocalTime.of(1, 0), true)
        );
    }

    @Test
    @DisplayName("시간이 설정되지 않았을 경우 주어진 시간을 포함하지 않는다")
    public void isWithinBusinessHourReturnFalseWhenUnset() throws Exception{
        //given
        BusinessHour businessHour = BusinessHour.UNSET;
        LocalTime now = LocalTime.of(0, 0);
        //when
        boolean result = businessHour.isWithinBusinessHour(now);

        //then
        assertThat(result).isEqualTo(false);
    }
}
