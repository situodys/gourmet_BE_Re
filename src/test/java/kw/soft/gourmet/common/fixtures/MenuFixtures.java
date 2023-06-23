package kw.soft.gourmet.common.fixtures;

import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.MenuCategory;
import kw.soft.gourmet.domain.restaurant.Restaurant;

public class MenuFixtures {
    public static Menu createMenu(final Restaurant restaurant) {
        return Menu.builder()
                .name("menu")
                .description("description")
                .price(1000)
                .isMarketPrice(false)
                .menuCategory(MenuCategory.MAIN)
                .restaurant(restaurant)
                .build();
    }

    private MenuFixtures() {
    }
}
