package kw.soft.gourmet.domain.menu.repository;

import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.common.factory.MenuFactory;
import kw.soft.gourmet.common.factory.RestaurantFactory;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.restaurant.repository.RestaurantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant = RestaurantFactory.createRestaurant();

    @BeforeEach
    public void setUp() {
        restaurantRepository.save(restaurant);
    }

    @Test
    @DisplayName("메뉴를 저장한다.")
    public void saveMenu() throws Exception{
        //given
        Menu menu = MenuFactory.createMenu(restaurant);

        //when
        Menu saved = menuRepository.save(menu);

        //then
        Assertions.assertThat(saved.getId()).isNotNull();
    }
}
