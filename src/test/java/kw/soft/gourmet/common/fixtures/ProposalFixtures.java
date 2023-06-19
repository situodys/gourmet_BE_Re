package kw.soft.gourmet.common.fixtures;

import java.time.DayOfWeek;
import java.time.LocalTime;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.proposal.MenuNameProposal;
import kw.soft.gourmet.domain.proposal.MenuPriceProposal;
import kw.soft.gourmet.domain.proposal.RestaurantBusinessHourProposal;
import kw.soft.gourmet.domain.proposal.RestaurantNameProposal;
import kw.soft.gourmet.domain.proposal.State;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.BusinessHourType;
import kw.soft.gourmet.domain.restaurant.Restaurant;

public class ProposalFixtures {
    public static MenuNameProposal createMenuNameProposal(final Menu menu, final Member member) {
        return MenuNameProposal.builder()
                .title("title")
                .state(State.PENDING)
                .menu(menu)
                .beforeName("beforeName")
                .requestedName("requestedName")
                .member(member)
                .build();
    }

    public static MenuPriceProposal createMenuPriceProposal(final Menu menu, final Member member) {
        return MenuPriceProposal.builder()
                .title("title")
                .state(State.PENDING)
                .menu(menu)
                .beforePrice(1000)
                .beforeIsMarketPrice(false)
                .requestedPrice(2000)
                .requestedIsMarketPrice(false)
                .member(member)
                .build();
    }

    public static RestaurantBusinessHourProposal createRestaurantBusinessHourProposal(final Restaurant restaurant,
                                                                                      final Member member) {
        BusinessHour beforeBusinessHour = RestaurantFixtures.createBusinessHour(
                LocalTime.of(9, 0), LocalTime.of(12, 0), false
        );
        BusinessHour requestedBusinessHour = RestaurantFixtures.createBusinessHour(
                LocalTime.of(10, 0), LocalTime.of(13, 0), false
        );

        return RestaurantBusinessHourProposal.builder()
                .title("title")
                .state(State.PENDING)
                .dayOfWeek(DayOfWeek.MONDAY)
                .businessHourType(BusinessHourType.RUN_TIME)
                .beforeBusinessHour(beforeBusinessHour)
                .requestedBusinessHour(requestedBusinessHour)
                .restaurant(restaurant)
                .member(member)
                .build();
    }

    public static RestaurantNameProposal createRestaurantNameProposal(final Restaurant restaurant, final Member member) {
        return RestaurantNameProposal.builder()
                .title("title")
                .state(State.PENDING)
                .restaurant(restaurant)
                .beforeName("beforeName")
                .requestedName("requestedName")
                .member(member)
                .build();
    }
}
