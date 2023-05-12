package kw.soft.gourmet.domain.proposal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.Price;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("menuPrice")
public class MenuPriceProposal extends Proposal {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "before_price")),
            @AttributeOverride(name = "isMarketPrice", column = @Column(name = "before_is_market_price"))
    })
    private Price beforePrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "requested_price")),
            @AttributeOverride(name = "isMarketPrice", column = @Column(name = "requested_is_market_price"))
    })
    private Price requestedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    public MenuPriceProposal(final Long id, final String title, final State state,
                             final Menu menu, final int beforePrice, final boolean beforeIsMarketPrice,
                             final int requestedPrice, final boolean requestedIsMarketPrice, final Member member) {
        super(id, title, state, member);
        this.beforePrice = new Price(beforePrice, beforeIsMarketPrice);
        this.requestedPrice = new Price(requestedPrice, requestedIsMarketPrice);
        this.menu = menu;
    }
}
