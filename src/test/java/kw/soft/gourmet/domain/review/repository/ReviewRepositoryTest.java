package kw.soft.gourmet.domain.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.common.factory.MemberFixtures;
import kw.soft.gourmet.common.factory.MenuFactory;
import kw.soft.gourmet.common.factory.RestaurantFactory;
import kw.soft.gourmet.common.factory.ReviewFactory;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.repository.MenuRepository;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.restaurant.repository.RestaurantRepository;
import kw.soft.gourmet.domain.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Restaurant restaurant = RestaurantFactory.createRestaurant();

    private Menu menu = MenuFactory.createMenu(restaurant);

    private Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();

    @BeforeEach
    public void setUp() {
        restaurantRepository.save(restaurant);
        memberRepository.save(member);
        menuRepository.save(menu);
    }

    @Test
    @DisplayName("리뷰를 저장한다.")
    public void saveReview() throws Exception {
        //given
        Review review = ReviewFactory.createReview(restaurant, member, Set.of(menu));

        //when
        Review saved = reviewRepository.save(review);

        //then
        assertThat(saved.getId()).isNotNull();
    }
}
