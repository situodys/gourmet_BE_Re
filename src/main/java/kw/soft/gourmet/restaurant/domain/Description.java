package kw.soft.gourmet.restaurant.domain;

import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;

public class Description {
    private final int MAX_DESCRIPTION_LENGTH = 500;

    private String value;

    public Description(String value) {
        validate(value);
        this.value =value;
    }

    private void validate(String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new RestaurantException(Code.EMPTY_DESCRIPTION);
        }
    }

    private void checkProperLength(String value) {
        if (value.length() > MAX_DESCRIPTION_LENGTH) {
            throw new RestaurantException(Code.INVALID_DESCRIPTION_LENGTH);
        }
    }


}
