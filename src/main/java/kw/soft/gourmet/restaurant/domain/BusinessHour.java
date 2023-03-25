package kw.soft.gourmet.restaurant.domain;

import java.time.LocalTime;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public abstract class BusinessHour {
    public static final LocalTime UNSET = LocalTime.of(0, 0);

    protected final LocalTime start;
    protected final LocalTime end;

    protected BusinessHour(LocalTime start, LocalTime end) {
        checkNull(start, end);
        this.start = start;
        this.end = end;
    }

    private void checkNull(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new RestaurantException(Code.INVALID_BUSINESS_HOUR);
        }
    }

    public boolean isExist() {
        return !(this.start.equals(UNSET) && this.end.equals(UNSET));
    }

    public boolean isIn(BusinessHour outer) {
        if (outer.isEndTomorrow() == this.isEndTomorrow()) {
            return outer.isAfterOrEqualToStart(this.start) && outer.isBeforeOrEqualToEnd(this.end);
        }
        if (this.isEndTomorrow() && !outer.isEndTomorrow()) {
            return false;
        }
        return outer.isAfterOrEqualToStart(this.start);
    }

    public boolean isWithinRange(LocalTime now) {
        if (isEndTomorrow()) {
            return isWithinRangeWhenEndTomorrow(now);
        }
        return isWithinRangeWhenEndToday(now);
    }

    public abstract BusinessHour convertToYesterdayBusinessHour();

    private boolean isEndTomorrow() {
        return this.start.isAfter(this.end);
    }

    protected abstract boolean isWithinRangeWhenEndToday(LocalTime now);

    protected abstract boolean isWithinRangeWhenEndTomorrow(LocalTime now);

    protected boolean isAfterOrEqualToStart(LocalTime time) {
        return !time.isBefore(this.start);
    }

    protected boolean isBeforeOrEqualToEnd(LocalTime time) {
        return !time.isAfter(this.end);
    }

    @Override
    public String toString() {
        return "BusinessHour{" +
                "start=" + this.start +
                ", end=" + this.end +
                '}';
    }
}
