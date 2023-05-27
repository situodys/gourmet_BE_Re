package kw.soft.gourmet.domain.review.exception;

import kw.soft.gourmet.domain.review.exception.Code;
import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class ReviewException extends BusinessLogicException {
    private final ErrorCode code;

    public ReviewException(ErrorCode code) {
        super(code);
        this.code = code;
    }
}
