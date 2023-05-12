package kw.soft.gourmet.domain.proposal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import kw.soft.gourmet.domain.member.Authority;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.MenuCategory;
import kw.soft.gourmet.domain.menu.repository.MenuRepository;
import kw.soft.gourmet.domain.proposal.RestaurantBusinessHourProposal;
import kw.soft.gourmet.domain.proposal.State;
import kw.soft.gourmet.domain.restaurant.Address;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.BusinessHourType;
import kw.soft.gourmet.domain.restaurant.BusinessSchedule;
import kw.soft.gourmet.domain.restaurant.BusinessSchedules;
import kw.soft.gourmet.domain.restaurant.Description;
import kw.soft.gourmet.domain.restaurant.GeoPoint;
import kw.soft.gourmet.domain.restaurant.Name;
import kw.soft.gourmet.domain.restaurant.PhoneNumber;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.restaurant.RestaurantType;
import kw.soft.gourmet.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class RestaurantBusinessHourProposalRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantBusinessHourProposalRepository restaurantBusinessHourProposalRepository;
    @Autowired
    private MemberRepository memberRepository;

    private PasswordPolicy passwordPolicy = new HighPasswordPolicy();

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

    private Member member = Member.builder()
            .email("test123@kw.ac.kr")
            .password("test123!@#")
            .passwordPolicy(passwordPolicy)
            .authority(Authority.USER)
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

    @Test
    public void saveRestaurantBusinessScheduleProposal() throws Exception {
        //given
        RestaurantBusinessHourProposal proposal = RestaurantBusinessHourProposal.builder()
                .title("title")
                .state(State.PENDING)
                .dayOfWeek(DayOfWeek.MONDAY)
                .businessHourType(BusinessHourType.RUN_TIME)
                .beforeBusinessHour(createBusinessHour())
                .requestedBusinessHour(createBusinessHour())
                .restaurant(restaurant)
                .member(member)
                .build();
        restaurantRepository.save(restaurant);
        menuRepository.save(menu);
        memberRepository.save(member);

        //when
        RestaurantBusinessHourProposal saved = restaurantBusinessHourProposalRepository.save(proposal);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    private BusinessHour createBusinessHour() {
        return new BusinessHour(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                false);
    }
}
