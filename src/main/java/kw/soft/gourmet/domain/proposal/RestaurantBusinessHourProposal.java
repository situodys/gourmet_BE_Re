package kw.soft.gourmet.domain.proposal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.DayOfWeek;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.restaurant.BusinessHour;
import kw.soft.gourmet.domain.restaurant.BusinessHourType;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("restaurantBusinessHour")
public class RestaurantBusinessHourProposal extends Proposal {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    private BusinessHourType businessHourType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "before_start")),
            @AttributeOverride(name = "end", column = @Column(name = "before_end")),
            @AttributeOverride(name = "isStartAtTomorrow", column = @Column(name = "before_is_start_at_tomorrow")),
    })
    private BusinessHour beforeBusinessHour;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "requested_start")),
            @AttributeOverride(name = "end", column = @Column(name = "requested_end")),
            @AttributeOverride(name = "isStartAtTomorrow", column = @Column(name = "requested_is_start_at_tomorrow")),
    })
    private BusinessHour requestedBusinessHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public RestaurantBusinessHourProposal(final Long id, final String title, final State state,
                                          final Member member, final DayOfWeek dayOfWeek,
                                          final BusinessHourType businessHourType,
                                          final BusinessHour beforeBusinessHour,
                                          final BusinessHour requestedBusinessHour, final Restaurant restaurant) {
        super(id, title, state, member);
        this.dayOfWeek = dayOfWeek;
        this.businessHourType = businessHourType;
        this.beforeBusinessHour = beforeBusinessHour;
        this.requestedBusinessHour = requestedBusinessHour;
        this.restaurant = restaurant;
    }
}
