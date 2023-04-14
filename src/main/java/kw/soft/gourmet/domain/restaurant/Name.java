package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Name {
    private static final int MAX_NAME_LENGTH = 50;

    @Column(name = "name", nullable = false)
    private String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(final String value) {
        if (value == null || value.isBlank()) {
            throw new RestaurantException(Code.EMPTY_NAME);
        }
    }

    private void checkProperLength(final String value) {
        if (value.length() > MAX_NAME_LENGTH) {
            throw new RestaurantException(Code.INVALID_NAME_LENGTH);
        }
    }
}
