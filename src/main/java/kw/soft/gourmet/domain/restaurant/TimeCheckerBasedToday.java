package kw.soft.gourmet.domain.restaurant;

import java.time.LocalTime;

public class TimeCheckerBasedToday implements TimeChecker {

    @Override
    public boolean isWithinBusinessHour(final BusinessHour businessHour, final LocalTime now) {
        if (businessHour.isEndAtTomorrow() && !businessHour.isStartAtTomorrow()) {
            return isWithinBusinessHourWhenEndTomorrow(businessHour, now);
        }
        return isWithinBusinessHourWhenEndToday(businessHour, now);
    }

    private boolean isWithinBusinessHourWhenEndTomorrow(final BusinessHour businessHour, final LocalTime now) {
        return businessHour.isAfterOrEqualToStart(now) || businessHour.isBeforeOrEqualToEnd(now);
    }

    private boolean isWithinBusinessHourWhenEndToday(final BusinessHour businessHour, final LocalTime now) {
        return businessHour.isAfterOrEqualToStart(now) && businessHour.isBeforeOrEqualToEnd(now);
    }
}
