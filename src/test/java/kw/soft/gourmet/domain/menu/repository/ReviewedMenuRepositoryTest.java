package kw.soft.gourmet.domain.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.MenuCategory;
import kw.soft.gourmet.domain.menu.ReviewedMenu;
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
import kw.soft.gourmet.domain.restaurant.repository.RestaurantRepository;
import kw.soft.gourmet.domain.review.Review;
import kw.soft.gourmet.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@RepositoryTest
public class ReviewedMenuRepositoryTest {

    @Autowired
    private ReviewedMenuRepository reviewedMenuRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;

    private Review review = Review.builder()
            .title("title")
            .content("content")
            .rating(3)
            .build();

    private Restaurant restaurant = Restaurant.builder()
            .name(new Name("name"))
            .description(new Description("description"))
            .imageUrl("imageUrl")
            .phoneNumber(new PhoneNumber("010-123-1234"))
            .address(new Address("address", new GeoPoint(0.0, 0.0)))
            .restaurantType(RestaurantType.KOREAN)
            .businessSchedules(createDefaultBusinessSchedules())
            .build();

    private Menu menu = Menu.builder()
            .name("menu")
            .description("description")
            .price(1000)
            .isMarketPrice(false)
            .menuCategory(MenuCategory.MAIN)
            .restaurant(restaurant)
            .build();


    private BusinessSchedules createDefaultBusinessSchedules() {
        Map<DayOfWeek, BusinessSchedule> schedules = new HashMap<>();
        BusinessHour runtTime = new BusinessHour(LocalTime.of(1, 1), LocalTime.of(2, 2), false);
        BusinessHour breakTime = new BusinessHour(LocalTime.of(1, 10), LocalTime.of(1, 20), false);

        for (DayOfWeek day : DayOfWeek.values()) {
            schedules.put(day, new BusinessSchedule(day, runtTime, breakTime));
        }
        return new BusinessSchedules(schedules);
    }

    @BeforeEach
    public void init() {
        restaurantRepository.save(restaurant);
        menuRepository.save(menu);
        reviewRepository.save(review);
    }

    @Test
    @DisplayName("리뷰된 메뉴를 저장한다.")
    public void saveReviewedMenu() throws Exception{
        //given
        ReviewedMenu reviewedMenu = createReviewedMenu();

        //when
        ReviewedMenu saved = reviewedMenuRepository.save(reviewedMenu);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    private ReviewedMenu createReviewedMenu() {
        return ReviewedMenu.builder()
                .name("name")
                .review(review)
                .menu(menu)
                .build();
    }
}
