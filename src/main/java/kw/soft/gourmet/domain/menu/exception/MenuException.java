package kw.soft.gourmet.domain.menu.exception;

import kw.soft.gourmet.exception.BusinessLogicException;

public class MenuException extends BusinessLogicException {
    private final Code code;

    public MenuException(Code code) {
        super(code);
        this.code = code;
    }
}
