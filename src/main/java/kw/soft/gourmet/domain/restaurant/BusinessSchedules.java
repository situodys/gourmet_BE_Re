package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class BusinessSchedules {
    private final static int ONE_DAY = 1;
    private final static int ONE_WEEK = 7;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "dayOfWeek")
    private Map<DayOfWeek, BusinessSchedule> businessSchedules = new HashMap<>();

    public BusinessSchedules(final Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        checkFullDayOfWeek(businessSchedules);
        checkTimeIntervalAmongBusinessSchedules(businessSchedules);
        this.businessSchedules = new HashMap<>(businessSchedules);
    }

    private void checkFullDayOfWeek(final Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        if (businessSchedules == null || businessSchedules.size() != ONE_WEEK) {
            throw new RestaurantException(Code.NOT_ENOUGH_BUSINESS_SCHEDULES);
        }
    }

    private void checkTimeIntervalAmongBusinessSchedules(final Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        for (DayOfWeek today : DayOfWeek.values()) {
            BusinessSchedule yesterdayBs = businessSchedules.get(today.minus(ONE_DAY));
            BusinessSchedule todayBs = businessSchedules.get(today);
            if (yesterdayBs.isEndAfterStartOf(todayBs)) {
                throw new RestaurantException(Code.INVALID_BUSINESS_SCHEDULE_INTERVAL);
            }
        }
    }

    public BusinessStatus calculateBusinessStatus(final DayOfWeek todayDayOfWeek, final LocalTime currentTime) {
        BusinessStatus yesterdayStatus = calculateAtYesterday(todayDayOfWeek, currentTime);
        BusinessStatus todayStatus = calculateAtToday(todayDayOfWeek, currentTime);
        return yesterdayStatus.determineStatus(todayStatus);
    }

    private BusinessStatus calculateAtYesterday(final DayOfWeek todayDayOfWeek, final LocalTime currentTime) {
        BusinessSchedule yesterday = getYesterdayBusinessSchedule(todayDayOfWeek);
        return yesterday.calculateBusinessStatus(currentTime);
    }

    private BusinessSchedule getYesterdayBusinessSchedule(final DayOfWeek today) {
        return businessSchedules.get(today.minus(ONE_DAY)).convertToYesterdayBusinessSchedule();
    }

    private BusinessStatus calculateAtToday(final DayOfWeek todayDayOfWeek, final LocalTime currentTime) {
        BusinessSchedule today = businessSchedules.get(todayDayOfWeek);
        return today.calculateBusinessStatus(currentTime);
    }

    public void updateRestaurant(final Restaurant restaurant) {
        for (BusinessSchedule businessSchedule : businessSchedules.values()) {
            businessSchedule.updateRestaurant(restaurant);
        }
    }
}
