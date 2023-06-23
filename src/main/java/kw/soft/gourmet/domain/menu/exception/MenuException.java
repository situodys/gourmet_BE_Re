package kw.soft.gourmet.domain.menu.exception;

import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class MenuException extends BusinessLogicException {
    private final ErrorCode code;

    public MenuException(ErrorCode code) {
        super(code);
        this.code = code;
    }
}
