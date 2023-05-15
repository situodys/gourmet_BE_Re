package kw.soft.gourmet.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Embedded
    private Price price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "menu_category")
    private MenuCategory menuCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @Builder
    private Menu(final Long id, final String name, final String description, final int price,
                 final boolean isMarketPrice, final MenuCategory menuCategory, final Restaurant restaurant) {
        this.id = id;
        this.name = new Name(name);
        this.description = new Description(description);
        this.price = new Price(price, isMarketPrice);
        this.menuCategory = menuCategory;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }
}
