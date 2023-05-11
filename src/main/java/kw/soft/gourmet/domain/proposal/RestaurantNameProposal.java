package kw.soft.gourmet.domain.proposal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.restaurant.Name;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("restaurantName")
public class RestaurantNameProposal extends Proposal {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "before_name"))
    private Name beforeName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "requested_name"))
    private Name requestedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public RestaurantNameProposal(final Long id, final String title, final State state,
                                  final Restaurant restaurant, final String beforeName, final String requestedName,
                                  final Member member) {
        super(id, title, state, member);
        this.beforeName = new Name(beforeName);
        this.requestedName = new Name(requestedName);
        this.restaurant = restaurant;
    }
}
