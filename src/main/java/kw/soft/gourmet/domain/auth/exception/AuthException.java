package kw.soft.gourmet.domain.auth.exception;

import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class AuthException extends BusinessLogicException {
    private final ErrorCode code;

    public AuthException(final ErrorCode code) {
        super(code);
        this.code= code;
    }
}
