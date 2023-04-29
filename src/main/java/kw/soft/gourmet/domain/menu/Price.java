package kw.soft.gourmet.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.menu.exception.Code;
import kw.soft.gourmet.domain.menu.exception.MenuException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Price {
    private static final String UNIT = "원";

    @Column(name = "price", nullable = false)
    private int value;

    @Column(name = "is_market_price", nullable = false)
    private boolean isMarketPrice;

    public Price(final int value, final boolean isMarketPrice) {
        checkValidMarketPlace(value, isMarketPrice);
        checkRange(value);
        this.value = value;
        this.isMarketPrice = isMarketPrice;
    }

    private void checkRange(final int value) {
        if (value < 0) {
            throw new MenuException(Code.INVALID_MENU_PRICE);
        }
    }

    private void checkValidMarketPlace(final int price, final boolean isMarketPrice) {
        if (isMarketPrice && price != 0) {
            throw new MenuException(Code.INVALID_MARKET_PRICE);
        }
    }
}
