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
public class Content {
    private static final int MAX_CONTENT_LENGTH = 1000;

    @Column(name = "content", nullable = false)
    private String value;

    public Content(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(final String value) {
        if (value == null || value.isBlank()) {
            throw new ReviewException(Code.EMPTY_CONTENT);
        }
    }

    private void checkProperLength(final String value) {
        if (value.length() > MAX_CONTENT_LENGTH) {
            throw new ReviewException(Code.INVALID_CONTENT_LENGTH);
        }
    }
}
