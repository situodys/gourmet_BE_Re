package kw.soft.gourmet.domain.restaurant;

import java.time.LocalTime;

public class TimeCheckerBasedYesterday implements TimeChecker {

    @Override
    public boolean isWithinBusinessHour(final BusinessHour businessHour, final LocalTime now) {
        if (businessHour.isEndAtTomorrow() && !businessHour.isStartAtTomorrow()) {
            return isWithinBusinessHourWhenEndTomorrow(businessHour, now);
        }
        return isWithinBusinessHourWhenEndToday(businessHour, now);
    }

    private boolean isWithinBusinessHourWhenEndTomorrow(final BusinessHour businessHour,
                                                        final LocalTime timeOfTomorrow) {
        return businessHour.isBeforeOrEqualToEnd(timeOfTomorrow);
    }


    private boolean isWithinBusinessHourWhenEndToday(final BusinessHour businessHour, final LocalTime now) {
        if (businessHour.isStartAtTomorrow()) {
            return businessHour.isAfterOrEqualToStart(now) && businessHour.isBeforeOrEqualToEnd(now);
        }
        return false;
    }
}
