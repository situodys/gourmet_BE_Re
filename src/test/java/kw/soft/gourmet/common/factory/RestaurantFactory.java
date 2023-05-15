package kw.soft.gourmet.common.factory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import kw.soft.gourmet.domain.restaurant.Address;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.BusinessSchedule;
import kw.soft.gourmet.domain.restaurant.BusinessSchedules;
import kw.soft.gourmet.domain.restaurant.Description;
import kw.soft.gourmet.domain.restaurant.GeoPoint;
import kw.soft.gourmet.domain.restaurant.Name;
import kw.soft.gourmet.domain.restaurant.PhoneNumber;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.restaurant.RestaurantType;

public class RestaurantFactory {
    public static Restaurant createRestaurant() {
        return Restaurant.builder()
                .name(new Name("name"))
                .description(new Description("description"))
                .imageUrl("imageUrl")
                .phoneNumber(new PhoneNumber("010-123-1234"))
                .address(new Address("address", new GeoPoint(50.0, 50.0)))
                .restaurantType(RestaurantType.KOREAN)
                .businessSchedules(createDefaultBusinessSchedules())
                .build();
    }

    private static BusinessSchedules createDefaultBusinessSchedules() {
        Map<DayOfWeek, BusinessSchedule> schedules = new HashMap<>();
        BusinessHour runtTime = new BusinessHour(LocalTime.of(9, 0), LocalTime.of(22, 0), false);
        BusinessHour breakTime = new BusinessHour(LocalTime.of(16, 00), LocalTime.of(17, 0), false);

        for (DayOfWeek day : DayOfWeek.values()) {
            schedules.put(day, new BusinessSchedule(day, runtTime, breakTime));
        }
        return new BusinessSchedules(schedules);
    }

    public static BusinessHour createBusinessHour(LocalTime start, LocalTime end, boolean isStartAtTomorrow) {
        return BusinessHour.builder()
                .start(start)
                .end(end)
                .isStartAtTomorrow(isStartAtTomorrow)
                .build();
    }

    private RestaurantFactory() {
    }
}
