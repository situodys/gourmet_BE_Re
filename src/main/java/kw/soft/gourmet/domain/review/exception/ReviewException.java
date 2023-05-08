package kw.soft.gourmet.domain.review.exception;

import kw.soft.gourmet.domain.review.exception.Code;
import kw.soft.gourmet.exception.BusinessLogicException;

public class ReviewException extends BusinessLogicException {
    private final Code code;

    public ReviewException(Code code) {
        super(code);
        this.code = code;
    }
}
