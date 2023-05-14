package kw.soft.gourmet.domain.restaurant.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kw.soft.gourmet.common.annotation.RepositoryTest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@RepositoryTest
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private static BusinessSchedules createDefaultBusinessSchedules() {
        Map<DayOfWeek, BusinessSchedule> schedules = new HashMap<>();
        BusinessHour runtTime = new BusinessHour(LocalTime.of(1, 1), LocalTime.of(2, 2), false);
        BusinessHour breakTime = new BusinessHour(LocalTime.of(1, 10), LocalTime.of(1, 20), false);

        for (DayOfWeek day : DayOfWeek.values()) {
            schedules.put(day, new BusinessSchedule(day, runtTime, breakTime));
        }
        return new BusinessSchedules(schedules);
    }

    @Test
    @DisplayName("음식점을 저장한다.")
    public void saveRestaurant() throws Exception {
        //given
        Restaurant restaurant = Restaurant.builder()
                .name(new Name("name"))
                .description(new Description("description"))
                .imageUrl("imageUrl")
                .phoneNumber(new PhoneNumber("010-123-1234"))
                .address(new Address("address", new GeoPoint(0.0, 0.0)))
                .restaurantType(RestaurantType.KOREAN)
                .businessSchedules(createDefaultBusinessSchedules())
                .build();
        //when
        Restaurant saved = restaurantRepository.save(restaurant);

        //then
        assertThat(saved.getId()).isNotNull();
    }
}
