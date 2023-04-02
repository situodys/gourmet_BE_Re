package kw.soft.gourmet.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BusinessSchedulesTest {
    private static final LocalTime DUMMY_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime DUMMY_END_TIME = LocalTime.of(18, 0);
    private static final BusinessHour DUMMY_BUSINESS_HOUR = new TodayBusinessHour(DUMMY_START_TIME, DUMMY_END_TIME, false);

    private static BusinessSchedules createBusinessSchedules(BusinessSchedule yesterday, BusinessSchedule today) {
        Map<DayOfWeek, BusinessSchedule> schedules = createDefaultBusinessSchedules();

        schedules.put(yesterday.getDayOfWeek(), yesterday);
        schedules.put(today.getDayOfWeek(), today);

        return new BusinessSchedules(schedules);
    }

    private static Map<DayOfWeek, BusinessSchedule> createDefaultBusinessSchedules() {
        Map<DayOfWeek, BusinessSchedule> schedules = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            schedules.put(day, createBusinessSchedule(day, DUMMY_BUSINESS_HOUR, DUMMY_BUSINESS_HOUR));
        }
        return schedules;
    }

    private static BusinessSchedule createBusinessSchedule(DayOfWeek day,
                                                           BusinessHour runTime,
                                                           BusinessHour breakTime) {
        return new BusinessSchedule(day, runTime, breakTime);
    }

    @Test
    @DisplayName("인자가 null이거나 모든 요일의 영업시간을 담지 않은 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenParamIsNullOrNotContainsFullDayOfWeek() throws Exception {
        //given
        Map<DayOfWeek, BusinessSchedule> nullParam = null;
        Map<DayOfWeek, BusinessSchedule> notEnoughParam = createDefaultBusinessSchedules();

        //when
        notEnoughParam.remove(DayOfWeek.MONDAY);

        //then
        Assertions.assertThatThrownBy(() -> {
                    new BusinessSchedules(nullParam);
                })
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.NOT_ENOUGH_BUSINESS_SCHEDULES);

        Assertions.assertThatThrownBy(() -> {
                    new BusinessSchedules(notEnoughParam);
                })
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.NOT_ENOUGH_BUSINESS_SCHEDULES);
    }

    @Test
    @DisplayName("전날 마감시간이 다음날 영업 시작 시간을 넘어설 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenYesterdayRunTimeEndAfterTodayRunTimeStart() throws Exception{
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(9, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 0), LocalTime.of(1, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(8, 0), LocalTime.of(21, 0), false),
                new TodayBusinessHour(LocalTime.of(14, 0), LocalTime.of(16, 0), true));

        //then
        Assertions.assertThatThrownBy(() -> {
                    createBusinessSchedules(yesterday, today);
                })
                .isInstanceOf(RestaurantException.class)
                .extracting("code").isEqualTo(Code.INVALID_BUSINESS_SCHEDULE_INTERVAL);
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartTomorrow")
    @DisplayName("어제 오늘 모두 익일 마감이고 어제의 휴게 시간 시작이 익일인 경우 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartTomorrow(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 0), LocalTime.of(1, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(19, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(0, 0), LocalTime.of(1, 0), true));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartTomorrow() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(19, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(20, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(18, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndTomorrow")
    @DisplayName("어제 오늘 모두 익일 마감이고 어제의 휴게 시간 시작이 익일이 아니고 마감이 익일인 경우 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndTomorrow(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 0), LocalTime.of(1, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(19, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 30), LocalTime.of(1, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndTomorrow() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(1, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(19, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(20, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(23, 30), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(18, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndYesterday")
    @DisplayName("어제 오늘 모두 익일 마감이고 어제의 휴게 시간 시작,마감 모두 당일인 경우 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndYesterday(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(22, 0), LocalTime.of(23, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(19, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 30), LocalTime.of(1, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayAndTodayEndTomorrowAndYesterdayBreakTimeStartYesterdayAndEndYesterday() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(1, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(19, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(20, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(22, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.OPEN),

                //break 케이스
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(23, 30), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(3, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(18, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartTomorrow")
    @DisplayName("어제는 익일 마감이고 휴게 시간 시작은 익일이며 오늘은 당일 마감일 때 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartTomorrow(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(0, 0), LocalTime.of(1, 0), true));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(9, 0), LocalTime.of(17, 30), false),
                new TodayBusinessHour(LocalTime.of(15, 0), LocalTime.of(17, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartTomorrow() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(1, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(17, 30), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(15, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(18, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(18, 30), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartYesterdayAndEndTomorrow")
    @DisplayName("어제는 익일 마감이고 휴게 시간 시작은 당일, 마감은 익일이며 오늘은 당일 마감일 때 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartYesterdayAndEndTomorrow(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(23, 0), LocalTime.of(1, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(9, 0), LocalTime.of(17, 30), false),
                new TodayBusinessHour(LocalTime.of(15, 0), LocalTime.of(17, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeStartYesterdayAndEndTomorrow() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(1, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(17, 30), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(0, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(15, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(18, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(18, 30), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(23, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeIsYesterday")
    @DisplayName("어제는 익일 마감이고 휴게 시간 시작,마감은 당일이며 오늘은 당일 마감일 때 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeIsYesterday(
            LocalTime currentTime, BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(18, 0), LocalTime.of(2, 0), false),
                new TodayBusinessHour(LocalTime.of(22, 0), LocalTime.of(23, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(9, 0), LocalTime.of(17, 30), false),
                new TodayBusinessHour(LocalTime.of(15, 0), LocalTime.of(17, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayEndTomorrowAndTodayEndTodayAndYesterdayBreakTimeIsYesterday() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(1, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(9, 0), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(17, 30), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(15, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(16, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(17, 0), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(18, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(22, 0), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(22, 30), BusinessStatus.CLOSE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTimeAndAnswerWhenYesterdayEndYesterdayAndTodayEndToday")
    @DisplayName("어제 오늘 모두 당일 마감인 경우 주어진 시간에 영업 상태를 반환한다.")
    public void calculateBusinessStatusWhenYesterdayEndYesterdayAndTodayEndToday(LocalTime currentTime,
                                                                                 BusinessStatus status)
            throws Exception {
        //given
        BusinessSchedule yesterday = createBusinessSchedule(
                DayOfWeek.MONDAY,
                new TodayBusinessHour(LocalTime.of(9, 0), LocalTime.of(12, 0), false),
                new TodayBusinessHour(LocalTime.of(10, 0), LocalTime.of(11, 0), false));
        BusinessSchedule today = createBusinessSchedule(
                DayOfWeek.TUESDAY,
                new TodayBusinessHour(LocalTime.of(13, 0), LocalTime.of(16, 0), false),
                new TodayBusinessHour(LocalTime.of(14, 0), LocalTime.of(15, 0), false));

        BusinessSchedules businessSchedules = createBusinessSchedules(yesterday, today);

        //when
        DayOfWeek todayDayOfWeek = DayOfWeek.TUESDAY;
        BusinessStatus result = businessSchedules.calculateBusinessStatus(todayDayOfWeek, currentTime);

        //then
        assertThat(result).isEqualTo(status);
    }

    private static Stream<Arguments> provideTimeAndAnswerWhenYesterdayEndYesterdayAndTodayEndToday() {
        return Stream.of(
                //open 케이스
                Arguments.of(LocalTime.of(13, 30), BusinessStatus.OPEN),
                Arguments.of(LocalTime.of(15, 30), BusinessStatus.OPEN),
                //break 케이스
                Arguments.of(LocalTime.of(14, 0), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(14, 30), BusinessStatus.BREAK),
                Arguments.of(LocalTime.of(15, 0), BusinessStatus.BREAK),
                //close 케이스
                Arguments.of(LocalTime.of(9, 30), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(10, 30), BusinessStatus.CLOSE),
                Arguments.of(LocalTime.of(11, 30), BusinessStatus.CLOSE)
        );
    }
}
