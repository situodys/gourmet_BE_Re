package kw.soft.gourmet.restaurant.domain;

import java.time.LocalTime;

public class YesterdayBusinessHour extends BusinessHour {

    public YesterdayBusinessHour(LocalTime start, LocalTime end, boolean isStartAtTomorrow) {
        super(start, end,isStartAtTomorrow);
    }

    @Override
    protected boolean isWithinBusinessHourWhenEndTomorrow(LocalTime timeOfTomorrow) {
        return isBeforeOrEqualToEnd(timeOfTomorrow);
    }

    @Override
    protected boolean isWithinBusinessHourWhenEndToday(LocalTime now) {
        if (super.isStartAtTomorrow) {
            return isAfterOrEqualToStart(now) && isBeforeOrEqualToEnd(now);
        }
        return false;
    }

    @Override
    public BusinessHour convertToYesterdayBusinessHour() {
        return this;
    }
}
