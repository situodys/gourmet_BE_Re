package kw.soft.gourmet.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.menu.exception.Code;
import kw.soft.gourmet.domain.menu.exception.MenuException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    @DisplayName("가격이 음수인 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenPriceIsLessThan0() throws Exception {
        //given
        int price = -100;
        boolean isMarketPrice = false;

        //then
        assertThatThrownBy(() -> new Price(price, isMarketPrice))
                .isInstanceOf(MenuException.class)
                .extracting("code").isEqualTo(Code.INVALID_MENU_PRICE);
    }

    @Test
    @DisplayName("싯가이면서 가격값이 0이 아닌 경우 예외를 발생시킨다")
    public void throwsExceptionWhenPriceIsMarketPriceAndPriceValueIsNot0() throws Exception {
        //given
        int price = 100;
        boolean isMarketPrice = true;

        //then
        assertThatThrownBy(() -> new Price(price, isMarketPrice))
                .isInstanceOf(MenuException.class)
                .extracting("code").isEqualTo(Code.INVALID_MARKET_PRICE);
    }
}
