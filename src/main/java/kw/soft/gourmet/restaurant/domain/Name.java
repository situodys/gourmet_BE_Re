package kw.soft.gourmet.restaurant.domain;

import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Name {
    private static final int MAX_NAME_LENGTH=50;

    private String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new RestaurantException(Code.EMPTY_NAME);
        }
    }

    private void checkProperLength(String value) {
        if (value.length() > MAX_NAME_LENGTH) {
            throw new RestaurantException(Code.INVALID_NAME_LENGTH);
        }
    }
}
