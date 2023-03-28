package kw.soft.gourmet.restaurant.domain;

import java.time.LocalTime;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public abstract class BusinessHour {
    private static final LocalTime UNSET = LocalTime.of(0, 0);

    protected final LocalTime start;
    protected final LocalTime end;
    protected final Boolean isStartAtTomorrow;

    protected BusinessHour(LocalTime start, LocalTime end, boolean isStartAtTomorrow) {
        checkNull(start, end);
        this.start = start;
        this.end = end;
        this.isStartAtTomorrow = isStartAtTomorrow;
    }

    private void checkNull(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new RestaurantException(Code.INVALID_BUSINESS_HOUR);
        }
    }

    public boolean isUnset() {
        return this.start.equals(UNSET) && this.end.equals(UNSET);
    }

    public boolean isIn(BusinessHour outer) {
        if (isStartAndEndSameDayWith(outer)) {
            return outer.isAfterOrEqualToStart(this.start) && outer.isBeforeOrEqualToEnd(this.end);
        }
        if (isEndSameDayWith(outer)) {
            return outer.isBeforeOrEqualToEnd(this.end);
        }
        return outer.isAfterOrEqualToStart(this.start);
    }

    private boolean isStartAndEndSameDayWith(BusinessHour outer) {
        return isStartSameDayWith(outer) && isEndSameDayWith(outer);
    }

    private boolean isStartSameDayWith(BusinessHour outer) {
        return outer.isStartAtTomorrow() == this.isStartAtTomorrow();
    }

    private boolean isEndSameDayWith(BusinessHour outer) {
        return outer.isEndAtTomorrow() == this.isEndAtTomorrow();
    }

    private boolean isEndAtTomorrow() {
        return this.isStartAtTomorrow || this.start.isAfter(this.end);
    }

    private boolean isStartAtTomorrow() {
        return this.isStartAtTomorrow;
    }

    protected boolean isAfterOrEqualToStart(LocalTime time) {
        return !time.isBefore(this.start);
    }

    protected boolean isBeforeOrEqualToEnd(LocalTime time) {
        return !time.isAfter(this.end);
    }

    public boolean isWithinBusinessHour(LocalTime now) {
        if (isEndAtTomorrow() && !isStartAtTomorrow) {
            return isWithinBusinessHourWhenEndTomorrow(now);
        }
        return isWithinBusinessHourWhenEndToday(now);
    }

    protected abstract boolean isWithinBusinessHourWhenEndToday(LocalTime now);

    protected abstract boolean isWithinBusinessHourWhenEndTomorrow(LocalTime now);

    public abstract BusinessHour convertToYesterdayBusinessHour();

    @Override
    public String toString() {
        return "BusinessHour{" +
                "start=" + this.start +
                ", end=" + this.end +
                '}';
    }
}
