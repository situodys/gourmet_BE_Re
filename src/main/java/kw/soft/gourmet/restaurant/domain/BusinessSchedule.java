package kw.soft.gourmet.restaurant.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public class BusinessSchedule {
    private final DayOfWeek dayOfWeek;
    private final BusinessHour runTime;
    private final BusinessHour breakTime;

    public BusinessSchedule(DayOfWeek dayOfWeek, BusinessHour runTime, BusinessHour breakTime) {
        validate(runTime, breakTime);
        this.dayOfWeek = dayOfWeek;
        this.runTime = runTime;
        this.breakTime = breakTime;
    }

    private void validate(BusinessHour runTime, BusinessHour breakTime) {
        checkValidRunTime(runTime);
        checkBreakTimeIsInRunTime(runTime, breakTime);
    }

    private void checkValidRunTime(BusinessHour runTime) {
        if (runTime.isUnset()) {
            throw new RestaurantException(Code.INVALID_RUNTIME);
        }
    }

    private void checkBreakTimeIsInRunTime(BusinessHour runTime, BusinessHour breakTime) {
        if (!breakTime.isIn(runTime)) {
            throw new RestaurantException(Code.INVALID_BUSINESS_SCHEDULE);
        }
    }

    public BusinessStatus calculateBusinessStatus(LocalTime now) {
        if (isClosedAt(now)) {
            return BusinessStatus.CLOSE;
        }
        if (isBreakTimeAt(now)) {
            return BusinessStatus.BREAK;
        }
        return BusinessStatus.OPEN;
    }

    private boolean isClosedAt(LocalTime now) {
        return !runTime.isWithinBusinessHour(now);
    }

    private boolean isBreakTimeAt(LocalTime now) {
        return breakTime.isWithinBusinessHour(now);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public BusinessSchedule convertToYesterdayBusinessSchedule() {
        return new BusinessSchedule(
                this.dayOfWeek,
                runTime.convertToYesterdayBusinessHour(),
                breakTime.convertToYesterdayBusinessHour()
        );
    }
}
