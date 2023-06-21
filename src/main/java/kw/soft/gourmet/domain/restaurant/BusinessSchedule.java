package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalTime;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_schedule_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "run_time_start")),
            @AttributeOverride(name = "end", column = @Column(name = "run_time_end")),
            @AttributeOverride(name = "isStartAtTomorrow", column = @Column(name = "is_run_time_start_at_tomorrow")),
    })
    private BusinessHour runTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "break_time_start")),
            @AttributeOverride(name = "end", column = @Column(name = "break_time_end")),
            @AttributeOverride(name = "isStartAtTomorrow", column = @Column(name = "is_break_time_start_at_tomorrow")),
    })
    private BusinessHour breakTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public BusinessSchedule(final DayOfWeek dayOfWeek, final BusinessHour runTime, final BusinessHour breakTime) {
        validate(runTime, breakTime);
        this.dayOfWeek = dayOfWeek;
        this.runTime = runTime;
        this.breakTime = breakTime;
    }

    private void validate(final BusinessHour runTime, final BusinessHour breakTime) {
        checkValidRunTime(runTime);
        checkBreakTimeIsInRunTime(runTime, breakTime);
    }

    private void checkValidRunTime(final BusinessHour runTime) {
        if (runTime.isStartAtTomorrow()) {
            throw new RestaurantException(Code.INVALID_START_OF_RUNTIME);
        }
    }

    private void checkBreakTimeIsInRunTime(final BusinessHour runTime, final BusinessHour breakTime) {
        checkBreakTimeWhenRunTimeIsUnset(runTime, breakTime);
        if (breakTime.isUnset()) {
            return;
        }
        if (!breakTime.isIn(runTime)) {
            throw new RestaurantException(Code.INVALID_BUSINESS_SCHEDULE);
        }
    }

    private void checkBreakTimeWhenRunTimeIsUnset(BusinessHour runTime, BusinessHour breakTime) {
        if (runTime.isUnset() && !breakTime.isUnset()) {
            throw new RestaurantException(Code.INVALID_BREAK_TIME);
        }
    }

    public BusinessStatus calculateBusinessStatus(final LocalTime now) {
        if (isDayOff() || isClosedAt(now)) {
            return BusinessStatus.CLOSE;
        }
        if (isBreakTimeAt(now)) {
            return BusinessStatus.BREAK;
        }
        return BusinessStatus.OPEN;
    }

    private boolean isDayOff() {
        return runTime.isUnset();
    }

    private boolean isClosedAt(final LocalTime now) {
        return !runTime.isWithinBusinessHour(now);
    }

    private boolean isBreakTimeAt(final LocalTime now) {
        return breakTime.isWithinBusinessHour(now);
    }

    public boolean isEndAfterStartOf(final BusinessSchedule tomorrowBusinessSchedule) {
        if (!this.runTime.isEndAtTomorrow()) {
            return false;
        }
        return tomorrowBusinessSchedule.isStartBeforeEndOf(this.runTime);
    }

    public boolean isStartBeforeEndOf(final BusinessHour another) {
        return this.runTime.isStartBeforeEndOf(another);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public BusinessSchedule convertToYesterdayBusinessSchedule() {
        runTime.changeTimeCheckerBasedYesterday();
        breakTime.changeTimeCheckerBasedYesterday();
        return this;
    }

    public void updateRestaurant(final Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
