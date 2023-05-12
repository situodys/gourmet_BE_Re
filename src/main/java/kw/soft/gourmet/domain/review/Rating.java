package kw.soft.gourmet.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.review.exception.Code;
import kw.soft.gourmet.domain.review.exception.ReviewException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Rating {
    private static final int MAX_RATING_VALUE = 5;
    private static final int MIN_RATING_VALUE = 0;

    @Column(name = "rating")
    private int rating;

    public Rating(final int rating) {
        validate(rating);
        this.rating = rating;
    }

    private void validate(final int rating) {
        checkRange(rating);
    }

    private void checkRange(final int rating) {
        if (rating < MIN_RATING_VALUE || rating > MAX_RATING_VALUE) {
            throw new ReviewException(Code.INVALID_RATING_RANGE);
        }
    }
}
