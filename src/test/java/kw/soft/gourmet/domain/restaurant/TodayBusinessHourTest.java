package kw.soft.gourmet.domain.restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TodayBusinessHourTest {

    @Test
    @DisplayName("null을 포함하여 초기화 하는 경우 예외를 발생시킨다")
    public void throwExceptionWhenFieldIsNull() throws Exception {
        //then
        assertThatThrownBy(() -> new BusinessHour(LocalTime.MIDNIGHT, null, false))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_BUSINESS_HOUR);
        assertThatThrownBy(() -> new BusinessHour(null, LocalTime.MIDNIGHT, false))
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_BUSINESS_HOUR);
    }

    @ParameterizedTest
    @MethodSource("provideBusinessHourAndResult")
    @DisplayName("시작,끝 시간이 설정되었는지 확인한다.")
    public void isUnset(BusinessHour businessHour, boolean result) {
        //when
        boolean isExist = businessHour.isUnset();

        //then
        assertThat(isExist).isEqualTo(result);
    }

    private static Stream<Arguments> provideBusinessHourAndResult() {
        final LocalTime UNSET = LocalTime.of(0, 0);
        LocalTime start = LocalTime.of(1, 1);
        LocalTime end = LocalTime.of(2, 2);
        boolean isStartAtNextDay = false;

        return Stream.of(
                Arguments.of(new BusinessHour(UNSET, UNSET, isStartAtNextDay), true),

                Arguments.of(new BusinessHour(UNSET, end, isStartAtNextDay), false),
                Arguments.of(new BusinessHour(start, UNSET, isStartAtNextDay), false),
                Arguments.of(new BusinessHour(start, end, isStartAtNextDay), false)
        );
    }

    @ParameterizedTest
    @MethodSource({
            "provideInnerAndOuterBusinessHourWhenBothEndToday",
            "provideInnerAndOuterBusinessHourWhenBothEndTomorrow",
            "provideInnerAndOuterBusinessHourWhenEndDifferentDay"})
    @DisplayName("BusinessHour가 다른 BusinessHour 안에 포함되는지 확인한다.")
    public void isBusinessHourInGivenOuterBusinessHour(BusinessHour inner, BusinessHour outer, boolean result) {
        //when
        boolean isIn = inner.isIn(outer);

        //then
        assertThat(isIn).isEqualTo(result);
    }

    private static List<Arguments> provideInnerAndOuterBusinessHourWhenEndDifferentDay() {
        BusinessHour outerWhichEndToday = new BusinessHour(LocalTime.of(21, 0), LocalTime.of(23, 0), false);
        BusinessHour outerWhichEndAtTomorrow = new BusinessHour(LocalTime.of(21, 0), LocalTime.of(3, 0), false);
        BusinessHour outerWhichStartAtTomorrow = new BusinessHour(LocalTime.of(1, 0), LocalTime.of(3, 0), false);

        BusinessHour innerWhichEndToday = new BusinessHour(LocalTime.of(22, 0), LocalTime.of(23, 0), false);
        BusinessHour innerWhichStartAtTomorrowAndInOuter = new BusinessHour(LocalTime.of(1, 0), LocalTime.of(2, 0),
                true);
        BusinessHour innerWhichStartAtTomorrowAndNotInOuter = new BusinessHour(LocalTime.of(1, 0),
                LocalTime.of(4, 0),
                true);

        Arguments innerIsInOuterThenTrue = Arguments.of(innerWhichEndToday, outerWhichEndAtTomorrow, true);
        Arguments innerEndIsBeforeOuterStartThenFalse = Arguments.of(innerWhichEndToday, outerWhichStartAtTomorrow,
                false);
        Arguments innerWhichStartAtTomorrowAndInOuterThenTrue = Arguments.of(innerWhichStartAtTomorrowAndInOuter,
                outerWhichEndAtTomorrow, true);
        Arguments innerWhichStartAtTomorrowAndOuterEndTodayThenFalse = Arguments.of(
                innerWhichStartAtTomorrowAndNotInOuter,
                outerWhichEndToday, false);
        Arguments innerWhichStartAtTomorrowAndOuterThenFalse = Arguments.of(innerWhichStartAtTomorrowAndNotInOuter,
                outerWhichEndAtTomorrow, false);

        return List.of(
                innerIsInOuterThenTrue,
                innerEndIsBeforeOuterStartThenFalse,
                innerWhichStartAtTomorrowAndInOuterThenTrue,
                innerWhichStartAtTomorrowAndOuterEndTodayThenFalse,
                innerWhichStartAtTomorrowAndOuterThenFalse
        );
    }

    private static List<Arguments> provideInnerAndOuterBusinessHourWhenBothEndToday() {
        BusinessHour EndTodayOuter = new BusinessHour(
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                false);

        BusinessHour EndTodayInnerInOuter = new BusinessHour(
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                false);
        Arguments innerIsInOuterThenTrue = Arguments.of(EndTodayInnerInOuter, EndTodayOuter, true);

        BusinessHour EndTodayInnerWhoseEndIsBeforeOuterStart = new BusinessHour(
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                false);
        Arguments innerEndIsBeforeOuterStartThenFalse = Arguments.of(
                EndTodayInnerWhoseEndIsBeforeOuterStart, EndTodayOuter, false);

        BusinessHour EndTodayInnerWhoseStartIsBeforeOuterStartAndEndIsInOuter = new BusinessHour(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                false);
        Arguments innerStartIsBeforeOuterStartAndInnerEndIsInOuterThenFalse = Arguments.of(
                EndTodayInnerWhoseStartIsBeforeOuterStartAndEndIsInOuter, EndTodayOuter, false);

        BusinessHour EndTodayInnerWhoseStartIsInOuterAndEndIsAfterOuter = new BusinessHour(
                LocalTime.of(12, 0),
                LocalTime.of(15, 0),
                false);
        Arguments innerStartIsInOuterAndInnerEndIsAfterOuterEndThenFalse = Arguments.of(
                EndTodayInnerWhoseStartIsInOuterAndEndIsAfterOuter, EndTodayOuter, false);

        BusinessHour EndTodayInnerWhoseStartIsAfterOuterEnd = new BusinessHour(
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                false);
        Arguments innerStartIsAfterOuterEndThenFalse = Arguments.of(
                EndTodayInnerWhoseStartIsAfterOuterEnd, EndTodayOuter, false);

        return List.of(
                innerIsInOuterThenTrue,
                innerEndIsBeforeOuterStartThenFalse,
                innerStartIsBeforeOuterStartAndInnerEndIsInOuterThenFalse,
                innerStartIsInOuterAndInnerEndIsAfterOuterEndThenFalse,
                innerStartIsAfterOuterEndThenFalse
        );
    }

    private static List<Arguments> provideInnerAndOuterBusinessHourWhenBothEndTomorrow() {
        BusinessHour outer = new BusinessHour(LocalTime.of(22, 0), LocalTime.of(3, 0), true);

        BusinessHour innerInOuter = new BusinessHour(LocalTime.of(23, 0), LocalTime.of(2, 0), true);
        Arguments innerIsInOuterThenTrue = Arguments.of(innerInOuter, outer,
                true);

        BusinessHour innerWhoseStartIsBeforeOuterStartAndEndIsInOuter = new BusinessHour(
                LocalTime.of(21, 0), LocalTime.of(2, 0), true);
        Arguments innerWhoseStartIsBeforeOuterStartAndEndIsInOuterThenFalse = Arguments.of(
                innerWhoseStartIsBeforeOuterStartAndEndIsInOuter, outer, false);

        BusinessHour innerWhoseStartIsInOuterAndEndIsAfterOuter = new BusinessHour(LocalTime.of(23, 0),
                LocalTime.of(4, 0), true);
        Arguments innerWhoseStartIsInOuterAndEndIsAfterOuterThenFalse = Arguments.of(
                innerWhoseStartIsInOuterAndEndIsAfterOuter, outer, false);

        return List.of(innerIsInOuterThenTrue,
                innerWhoseStartIsBeforeOuterStartAndEndIsInOuterThenFalse,
                innerWhoseStartIsInOuterAndEndIsAfterOuterThenFalse);
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenEndToday")
    @DisplayName("당일 마감일 때 주어진 시간을 포함하는지 확인한다")
    public void isGivenTimeWithinBusinessHourWhenBusinessHourEndToday(LocalTime time, boolean answer) throws Exception {
        //given
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(18, 0);

        //when
        BusinessHour businessHour = new BusinessHour(open, close, false);
        boolean isWithinBusinessHour = businessHour.isWithinBusinessHour(time);

        //then
        assertThat(isWithinBusinessHour).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenEndToday() {
        return Stream.of(
                Arguments.of(LocalTime.of(10, 0), true),
                Arguments.of(LocalTime.of(18, 0), true),

                Arguments.of(LocalTime.of(8, 0), false),
                Arguments.of(LocalTime.of(18, 30), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenEndTomorrow")
    @DisplayName("익일 마감일 때 주어진 시간을 포함하는지 확인한다.")
    public void isGivenTimeWithinBusinessHourWhenBusinessHourEndTomorrow(LocalTime time, boolean answer)
            throws Exception {
        //given
        LocalTime open = LocalTime.of(18, 0);
        LocalTime close = LocalTime.of(2, 0);

        //when
        BusinessHour businessHour = new BusinessHour(open, close, false);
        boolean isWithinBusinessHour = businessHour.isWithinBusinessHour(time);

        //then
        assertThat(isWithinBusinessHour).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenEndTomorrow() {
        return Stream.of(
                Arguments.of(LocalTime.of(19, 0), true),
                Arguments.of(LocalTime.of(0, 0), true),
                Arguments.of(LocalTime.of(18, 0), true),

                Arguments.of(LocalTime.of(17, 0), false),
                Arguments.of(LocalTime.of(3, 0), false)
        );
    }
}
