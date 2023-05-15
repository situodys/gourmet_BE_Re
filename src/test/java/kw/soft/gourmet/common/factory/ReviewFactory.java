package kw.soft.gourmet.common.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.review.ReviewedMenu;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.review.Review;
import kw.soft.gourmet.domain.review.ReviewedMenus;

public class ReviewFactory {

    public static Review createReview(final Restaurant restaurant, final Member member, final Set<Menu> menus) {
        return Review.builder()
                .title("title")
                .content("content")
                .rating(3)
                .restaurant(restaurant)
                .member(member)
                .reviewedMenus(createReviewedMenus(menus))
                .build();
    }

    public static ReviewedMenu createReviewedMenu(final Menu menu) {
        return ReviewedMenu.builder()
                .name("reviewedMenuName")
                .menu(menu)
                .build();
    }

    public static ReviewedMenus createReviewedMenus(final Set<Menu> menus) {
        List<ReviewedMenu> reviewedMenus = menus.stream()
                .map(menu -> createReviewedMenu(menu))
                .collect(Collectors.toUnmodifiableList());
        return new ReviewedMenus(reviewedMenus);
    }

    private ReviewFactory() {
    }
}
