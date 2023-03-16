package kw.soft.gourmet.restaurant.exception;

import kw.soft.gourmet.exception.BusinessLogicException;

public class RestaurantException extends BusinessLogicException {
    private final Code code;

    public RestaurantException(Code code) {
        super(code);
        this.code = code;
    }
}
