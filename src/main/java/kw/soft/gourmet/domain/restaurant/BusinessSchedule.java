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
        if (runTime.isUnset()) {
            throw new RestaurantException(Code.INVALID_RUNTIME);
        }
        if (runTime.isStartAtTomorrow()) {
            throw new RestaurantException(Code.INVALID_START_OF_RUNTIME);
        }
    }

    private void checkBreakTimeIsInRunTime(final BusinessHour runTime, final BusinessHour breakTime) {
        if (breakTime.isUnset()) {
            return;
        }
        if (!breakTime.isIn(runTime)) {
            throw new RestaurantException(Code.INVALID_BUSINESS_SCHEDULE);
        }
    }

    public BusinessStatus calculateBusinessStatus(final LocalTime now) {
        if (isClosedAt(now)) {
            return BusinessStatus.CLOSE;
        }
        if (isBreakTimeAt(now)) {
            return BusinessStatus.BREAK;
        }
        return BusinessStatus.OPEN;
    }

    private boolean isClosedAt(final LocalTime now) {
        return !runTime.isWithinBusinessHour(now);
    }

    private boolean isBreakTimeAt(final LocalTime now) {

        return breakTime.isWithinBusinessHour(now);
    }

    public boolean isEndBefore(final BusinessSchedule tomorrowBusinessSchedule) {
        if (!this.runTime.isEndAtTomorrow()) {
            return true;
        }
        return !tomorrowBusinessSchedule.isStartAfter(this.runTime.getEnd());
    }

    public boolean isStartAfter(final LocalTime time) {
        return runTime.isAfterOrEqualToStart(time);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public BusinessSchedule convertToYesterdayBusinessSchedule() {
        runTime.convertToYesterdayBusinessHour();
        breakTime.convertToYesterdayBusinessHour();
        return this;
    }

    public void updateRestaurant(final Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
