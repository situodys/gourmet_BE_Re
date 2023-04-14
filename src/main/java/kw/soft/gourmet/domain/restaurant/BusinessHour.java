package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Transient;
import java.time.LocalTime;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessHour {
    private static final LocalTime UNSET = LocalTime.of(0, 0);
    private static final TimeChecker timeCheckerBasedToday = new TimeCheckerBasedToday();
    private static final TimeChecker timeCheckerBasedYesterday = new TimeCheckerBasedYesterday();

    private LocalTime start;
    private LocalTime end;
    private Boolean isStartAtTomorrow;
    @Transient
    private TimeChecker timeChecker = timeCheckerBasedToday;

    public BusinessHour(final LocalTime start, final LocalTime end, final boolean isStartAtTomorrow) {
        checkNull(start, end);
        this.start = start;
        this.end = end;
        this.isStartAtTomorrow = isStartAtTomorrow;
    }

    private void checkNull(final LocalTime start, final LocalTime end) {
        if (start == null || end == null) {
            throw new RestaurantException(Code.INVALID_BUSINESS_HOUR);
        }
    }

    public boolean isUnset() {
        return this.start.equals(UNSET) && this.end.equals(UNSET);
    }

    public boolean isIn(final BusinessHour outer) {
        if (isStartAndEndSameDayWith(outer)) {
            return outer.isAfterOrEqualToStart(this.start) && outer.isBeforeOrEqualToEnd(this.end);
        }
        if (isEndSameDayWith(outer)) {
            return outer.isBeforeOrEqualToEnd(this.end);
        }
        return outer.isAfterOrEqualToStart(this.start);
    }

    private boolean isStartAndEndSameDayWith(final BusinessHour outer) {
        return isStartSameDayWith(outer) && isEndSameDayWith(outer);
    }

    private boolean isStartSameDayWith(final BusinessHour outer) {
        return outer.isStartAtTomorrow() == this.isStartAtTomorrow();
    }

    private boolean isEndSameDayWith(final BusinessHour outer) {
        return outer.isEndAtTomorrow() == this.isEndAtTomorrow();
    }

    public boolean isEndAtTomorrow() {
        return this.isStartAtTomorrow || this.start.isAfter(this.end);
    }

    public boolean isStartAtTomorrow() {
        return this.isStartAtTomorrow;
    }

    public boolean isAfterOrEqualToStart(final LocalTime time) {
        return !time.isBefore(this.start);
    }

    protected boolean isBeforeOrEqualToEnd(final LocalTime time) {
        return !time.isAfter(this.end);
    }

    public boolean isWithinBusinessHour(final LocalTime now) {
        return timeChecker.isWithinBusinessHour(this, now);
    }

    public void convertToYesterdayBusinessHour() {
        this.timeChecker = timeCheckerBasedYesterday;
    }

    public LocalTime getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "BusinessHour{" +
                "start=" + this.start +
                ", end=" + this.end +
                '}';
    }
}
