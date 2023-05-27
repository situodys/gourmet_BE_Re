package kw.soft.gourmet.domain.restaurant.exception;

import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class RestaurantException extends BusinessLogicException {
    private final ErrorCode code;

    public RestaurantException(ErrorCode code) {
        super(code);
        this.code = code;
    }
}
