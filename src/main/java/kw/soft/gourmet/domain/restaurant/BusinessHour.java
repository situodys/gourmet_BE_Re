package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Transient;
import java.time.LocalTime;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessHour {
    private static final LocalTime UNSET_TIME = LocalTime.of(0, 0);
    private static final TimeChecker timeCheckerBasedToday = new TimeCheckerBasedToday();
    private static final TimeChecker timeCheckerBasedYesterday = new TimeCheckerBasedYesterday();
    public static final BusinessHour UNSET = new BusinessHour(UNSET_TIME, UNSET_TIME, false);

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
        return this.start.equals(UNSET_TIME) && this.end.equals(UNSET_TIME);
    }

    public boolean isIn(final BusinessHour outer) {
        if (isStartAndEndSameDayWith(outer)) {
            return outer.isTimeAfterStart(this.start) && outer.isTimeBeforeEnd(this.end);
        }
        if (isEndSameDayWith(outer) && !outer.isStartAtTomorrow()) {
            return outer.isTimeBeforeEnd(this.end);
        }
        if (isStartSameDayWith(outer) && outer.isEndAtTomorrow()) {
            return outer.isTimeAfterStart(this.start);
        }
        return false;
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

    public boolean isTimeAfterStart(final LocalTime time) {
        return !time.isBefore(this.start);
    }

    public boolean isTimeBeforeEnd(final LocalTime time) {
        return !time.isAfter(this.end);
    }

    public boolean isWithinBusinessHour(final LocalTime now) {
        return timeChecker.isWithinBusinessHour(this, now);
    }

    public boolean isStartBeforeEndOf(BusinessHour another) {
        return another.isTimeBeforeEnd(this.start);
    }

    public BusinessHour changeTimeCheckerBasedYesterday() {
        this.timeChecker = timeCheckerBasedYesterday;
        return this;
    }

    @Override
    public String toString() {
        return "BusinessHour{" +
                "start=" + this.start +
                ", end=" + this.end +
                '}';
    }
}
