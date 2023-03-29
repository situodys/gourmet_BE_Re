package kw.soft.gourmet.restaurant.domain;

import java.time.LocalTime;

public class TodayBusinessHour extends BusinessHour {

    public TodayBusinessHour(LocalTime start, LocalTime end, boolean isStartAtTomorrow) {
        super(start, end, isStartAtTomorrow);
    }

    @Override
    protected boolean isWithinBusinessHourWhenEndTomorrow(LocalTime now) {
        return isAfterOrEqualToStart(now) || isBeforeOrEqualToEnd(now);
    }

    @Override
    protected boolean isWithinBusinessHourWhenEndToday(LocalTime now) {
        return isAfterOrEqualToStart(now) && isBeforeOrEqualToEnd(now);
    }

    @Override
    public BusinessHour convertToYesterdayBusinessHour() {
        return new YesterdayBusinessHour(start, end, isStartAtTomorrow);
    }
}
