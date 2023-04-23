package kw.soft.gourmet.domain.restaurant;

import java.time.LocalTime;

public class TimeCheckerBasedYesterday implements TimeChecker {

    @Override
    public boolean isWithinBusinessHour(final BusinessHour businessHour, final LocalTime now) {
        if (businessHour.isUnset()) {
            return false;
        }
        if (businessHour.isEndAtTomorrow() && !businessHour.isStartAtTomorrow()) {
            return isWithinBusinessHourWhenEndTomorrow(businessHour, now);
        }
        return isWithinBusinessHourWhenEndToday(businessHour, now);
    }

    private boolean isWithinBusinessHourWhenEndTomorrow(final BusinessHour businessHour,
                                                        final LocalTime timeOfTomorrow) {
        return businessHour.isTimeBeforeEnd(timeOfTomorrow);
    }


    private boolean isWithinBusinessHourWhenEndToday(final BusinessHour businessHour, final LocalTime now) {
        if (businessHour.isStartAtTomorrow()) {
            return businessHour.isTimeAfterStart(now) && businessHour.isTimeBeforeEnd(now);
        }
        return false;
    }
}
