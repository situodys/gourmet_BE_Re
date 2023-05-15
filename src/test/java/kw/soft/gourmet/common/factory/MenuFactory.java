package kw.soft.gourmet.common.factory;

import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.MenuCategory;
import kw.soft.gourmet.domain.restaurant.Restaurant;

public class MenuFactory {
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

    private MenuFactory() {
    }
}
