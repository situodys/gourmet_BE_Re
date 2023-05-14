package kw.soft.gourmet.domain.menu.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.MenuCategory;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.BusinessSchedule;
import kw.soft.gourmet.domain.restaurant.BusinessSchedules;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@RepositoryTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    private Menu createMenu() {
        return Menu.builder()
                .name("menu")
                .description("description")
                .price(1000)
                .isMarketPrice(false)
                .menuCategory(MenuCategory.MAIN)
                .restaurant(Restaurant.builder().businessSchedules(createDefaultBusinessSchedules()).build())
                .build();
    }

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
    @DisplayName("메뉴를 저장한다.")
    public void saveMenu() throws Exception{
        //given
        Menu menu = createMenu();

        //when
        Menu saved = menuRepository.save(menu);

        //then
        Assertions.assertThat(saved.getId()).isNotNull();
    }
}
