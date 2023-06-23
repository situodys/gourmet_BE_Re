package kw.soft.gourmet.domain.restaurant.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.common.fixtures.RestaurantFixtures;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("음식점을 저장한다.")
    public void saveRestaurant() throws Exception {
        //given
        Restaurant restaurant = RestaurantFixtures.createRestaurant();

        //when
        Restaurant saved = restaurantRepository.save(restaurant);

        //then
        assertThat(saved.getId()).isNotNull();
    }
}
