package kw.soft.gourmet.restaurant.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public class BusinessSchedules {
    private final static int ONE_DAY = 1;
    private final static int ONE_WEEK = 7;

    private Map<DayOfWeek, BusinessSchedule> businessSchedules;

    public BusinessSchedules(Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        checkFullDayOfWeek(businessSchedules);
        checkTimeIntervalAmongBusinessSchedules(businessSchedules);
        this.businessSchedules = businessSchedules;
    }

    private void checkFullDayOfWeek(Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        if (businessSchedules == null || businessSchedules.size() != ONE_WEEK) {
            throw new RestaurantException(Code.NOT_ENOUGH_BUSINESS_SCHEDULES);
        }
    }

    private void checkTimeIntervalAmongBusinessSchedules(Map<DayOfWeek, BusinessSchedule> businessSchedules) {
        for (DayOfWeek today : DayOfWeek.values()) {
            BusinessSchedule yesterdayBs = businessSchedules.get(today.minus(ONE_DAY));
            BusinessSchedule todayBs = businessSchedules.get(today);
            if (!yesterdayBs.isEndBefore(todayBs)) {
                throw new RestaurantException(Code.INVALID_BUSINESS_SCHEDULE_INTERVAL);
            }
        }
    }

    public BusinessStatus calculateBusinessStatus(DayOfWeek todayDayOfWeek, LocalTime currentTime) {
        BusinessStatus yesterdayStatus = calculateAtYesterday(todayDayOfWeek, currentTime);
        BusinessStatus todayStatus = calculateAtToday(todayDayOfWeek, currentTime);
        return yesterdayStatus.determineStatus(todayStatus);
    }

    private BusinessStatus calculateAtYesterday(DayOfWeek todayDayOfWeek, LocalTime currentTime) {
        BusinessSchedule yesterday = getYesterdayBusinessSchedule(todayDayOfWeek);
        return yesterday.calculateBusinessStatus(currentTime);
    }

    private BusinessSchedule getYesterdayBusinessSchedule(DayOfWeek today) {
        return businessSchedules.get(today.minus(ONE_DAY)).convertToYesterdayBusinessSchedule();
    }

    private BusinessStatus calculateAtToday(DayOfWeek todayDayOfWeek, LocalTime currentTime) {
        BusinessSchedule today = businessSchedules.get(todayDayOfWeek);
        return today.calculateBusinessStatus(currentTime);
    }
}
