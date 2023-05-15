package kw.soft.gourmet.domain.review;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Embedded
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Embedded
    private ReviewedMenus reviewedMenus = new ReviewedMenus();

    @Builder
    private Review(final Long id, final String title, final String content,
                   final Integer rating, final ReviewedMenus reviewedMenus, final Member member,
                   final Restaurant restaurant) {
        this.id = id;
        this.title = new Title(title);
        this.content = new Content(content);
        this.rating = new Rating(rating);
        this.restaurant = restaurant;
        this.member = member;
        this.reviewedMenus = reviewedMenus;

        reviewedMenus.updateReview(this);
    }

    public Long getId() {
        return id;
    }
}
