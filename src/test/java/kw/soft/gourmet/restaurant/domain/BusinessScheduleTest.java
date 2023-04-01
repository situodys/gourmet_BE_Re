package kw.soft.gourmet.restaurant.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Stream;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BusinessScheduleTest {
    private static final DayOfWeek DEFAULT_DAY = DayOfWeek.MONDAY;

    private static BusinessHour createTodayBusinessHour(int startHour, int startMinute, int endHour, int endMinute,
                                                        boolean isStartAtTomorrow) {
        return new TodayBusinessHour(
                LocalTime.of(startHour, startMinute),
                LocalTime.of(endHour, endMinute),
                isStartAtTomorrow
        );
    }

    @Test
    @DisplayName("영업 시간 시작이 익일인 경우 예외를 반환한다.")
    public void throwExceptionWhenStartOfRunTimeIsTomorrow() {
        //given
        BusinessHour runTime = createTodayBusinessHour(1, 0, 2, 0, true);
        BusinessHour breakTime = createTodayBusinessHour(1, 15, 1, 30, true);

        //then
        Assertions.assertThatThrownBy(() -> {
                    new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);
                })
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_START_OF_RUNTIME);

    }

    @ParameterizedTest
    @MethodSource({"provideRunTimeAndBreakTimeWhenBothEndToday", "provideRunTimeAndBreakTimeWhenBothEndTomorrow"})
    @DisplayName("휴게 시간이 운영 시간을 벗어날 경우 예외를 반환한다")
    public void throwExceptionWhenBreakTimeIsNotInRunTime(
            BusinessHour runTime, BusinessHour breakTime) throws Exception {
        //then
        Assertions.assertThatThrownBy(() -> {
                    new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);
                })
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_BUSINESS_SCHEDULE);
    }

    private static Stream<Arguments> provideRunTimeAndBreakTimeWhenBothEndToday() {
        BusinessHour runTime = createTodayBusinessHour(13, 0, 14, 0, false);

        BusinessHour breakTimeWhoseEndIsBeforeRunTime = createTodayBusinessHour(11, 0, 12, 0, false);
        BusinessHour breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime = createTodayBusinessHour(12, 0, 13, 30,
                false);
        BusinessHour breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTime = createTodayBusinessHour(13, 30, 14, 30,
                false);
        BusinessHour breakTimeAfterRunTime = createTodayBusinessHour(15, 0, 16, 30, false);

        return Stream.of(
                Arguments.of(runTime, breakTimeWhoseEndIsBeforeRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTime),
                Arguments.of(runTime, breakTimeAfterRunTime)
        );
    }

    private static Stream<Arguments> provideRunTimeAndBreakTimeWhenBothEndTomorrow() {
        BusinessHour runTime = createTodayBusinessHour(17, 0, 2, 0, false);

        BusinessHour breakTimeWhoseEndIsBeforeRunTime = createTodayBusinessHour(15, 0, 16, 0, false);
        BusinessHour breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime = createTodayBusinessHour(15, 0, 17, 30,
                false);
        BusinessHour breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTimeAndStartAtToday = createTodayBusinessHour(17, 30,
                3, 0, false);
        BusinessHour breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTimeAndStartAtTomorrow = createTodayBusinessHour(1,
                30, 3, 0, false);
        BusinessHour breakTimeAfterRunTime = createTodayBusinessHour(3, 0, 4, 0, true);

        return Stream.of(
                Arguments.of(runTime, breakTimeWhoseEndIsBeforeRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsBeforeRunTimeAndEndIsInRunTime),
                Arguments.of(runTime, breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTimeAndStartAtToday),
                Arguments.of(runTime, breakTimeWhoseStartIsInRunTimeAndEndIsAfterRunTimeAndStartAtTomorrow),
                Arguments.of(runTime, breakTimeAfterRunTime)
        );
    }

    @ParameterizedTest
    @MethodSource({"provideTimeAndAnswerWhenBothBreakAndRunTimeEndTomorrowAndBreakTimeStartAtTomorrow"})
    @DisplayName("휴게 시간, 운영 시간의 마감이 모두 익일이고 휴게 시간의 시작이 다음날인 경우 주어진 시간에 영업 상태를 정상적으로 반환한다.")
    public void calculateBusinessStatusWhenBothBreakTimeAndRunTimeEndTomorrowAndBreakTimeStartAtTomorrow(
            LocalTime time, BusinessStatus answer) {
        //given
        BusinessHour runTime = createTodayBusinessHour(17, 0, 3, 0, false);
        BusinessHour breakTime = createTodayBusinessHour(0, 0, 1, 0, true);
        BusinessSchedule businessSchedule = new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);

        //when
        BusinessStatus result = businessSchedule.calculateBusinessStatus(time);

        //then
        Assertions.assertThat(result).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenBothBreakAndRunTimeEndTomorrowAndBreakTimeStartAtTomorrow() {
        return Stream.of(
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(2, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(3, 0), BusinessStatus.OPEN),

                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(1, 0), BusinessStatus.BREAK),

                Arguments.of(LocalTime.of(16, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(5, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource({"provideTimeAndAnswerWhenBothBreakAndRunTimeEndTomorrowAndBreakTimeStartAtToday"})
    @DisplayName("휴게 시간, 운영 시간의 마감이 모두 익일이고 휴게 시간의 시작이 오늘인 경우 주어진 시간에 영업 상태를 정상적으로 반환한다.")
    public void calculateBusinessStatusWhenBothBreakAndRunTimeEndTomorrowAndBreakTimeStartAtToday(
            LocalTime time, BusinessStatus answer) {
        //given
        BusinessHour runTime = createTodayBusinessHour(17, 0, 3, 0, false);
        BusinessHour breakTime = createTodayBusinessHour(23, 0, 1, 0, false);
        BusinessSchedule businessSchedule = new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);

        //when
        BusinessStatus result = businessSchedule.calculateBusinessStatus(time);

        //then
        Assertions.assertThat(result).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenBothBreakAndRunTimeEndTomorrowAndBreakTimeStartAtToday() {
        return Stream.of(
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(2, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(3, 0), BusinessStatus.OPEN),

                Arguments.of(LocalTime.of(23, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(1, 0), BusinessStatus.BREAK),

                Arguments.of(LocalTime.of(16, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(5, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenBreakTimeEndTodayAndRunTimeEndTomorrow")
    @DisplayName("휴게 시간 마감은 금일이고 운영 시간 마감이 익일인 경우 주어진 시간에 영업 상태를 정상적으로 반환한다.")
    public void calculateBusinessStatusWhenBreakTimeEndTodayAndRunTimeEndTomorrow(
            LocalTime time, BusinessStatus answer) {
        //given
        BusinessHour runTime = createTodayBusinessHour(17, 0, 3, 0, false);
        BusinessHour breakTime = createTodayBusinessHour(22, 0, 23, 0, false);
        BusinessSchedule businessSchedule = new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);

        //when
        BusinessStatus result = businessSchedule.calculateBusinessStatus(time);

        //then
        Assertions.assertThat(result).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenBreakTimeEndTodayAndRunTimeEndTomorrow() {
        return Stream.of(
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(23, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(3, 0), BusinessStatus.OPEN),

                Arguments.of(LocalTime.of(22, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(22, 30), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.BREAK),

                Arguments.of(LocalTime.of(16, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(5, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenBothBreakAndRunTimeEndToday")
    @DisplayName("휴게 시간, 운영 시간의 마감이 모두 금일인 경우 주어진 시간에 영업 상태를 정상적으로 반환한다.")
    public void calculateBusinessStatusWhenBothBreakTimeAndRunTimeEndToday(
            LocalTime time, BusinessStatus answer) {
        //given
        BusinessHour runTime = createTodayBusinessHour(9, 0, 21, 0, false);
        BusinessHour breakTime = createTodayBusinessHour(15, 0, 17, 0, false);
        BusinessSchedule businessSchedule = new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);

        //when
        BusinessStatus result = businessSchedule.calculateBusinessStatus(time);

        //then
        Assertions.assertThat(result).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenBothBreakAndRunTimeEndToday() {
        return Stream.of(
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(12, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(21, 0), BusinessStatus.OPEN),

                Arguments.of(LocalTime.of(15, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(16, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.BREAK),

                Arguments.of(LocalTime.of(0, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(1, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenBreakTimeIsUnset")
    @DisplayName("휴게 시간이 없는 경우 주어진 시간에 영업 상태를 정상적으로 반환한다.")
    public void calculateBusinessStatusWhenBreakTimeIsUnset(
            LocalTime time, BusinessStatus answer) {
        //given
        BusinessHour runTime = createTodayBusinessHour(9, 0, 21, 0, false);
        BusinessHour breakTime = createTodayBusinessHour(0, 0, 0, 0, false);
        BusinessSchedule businessSchedule = new BusinessSchedule(DEFAULT_DAY, runTime, breakTime);

        //when
        BusinessStatus result = businessSchedule.calculateBusinessStatus(time);

        //then
        Assertions.assertThat(result).isEqualTo(answer);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenBreakTimeIsUnset() {
        return Stream.of(
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(12, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(21, 0), BusinessStatus.OPEN),

                Arguments.of(LocalTime.of(0, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(1, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.CLOSE)
        );
    }
}