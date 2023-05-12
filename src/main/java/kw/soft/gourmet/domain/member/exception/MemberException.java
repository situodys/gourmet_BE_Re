package kw.soft.gourmet.domain.member.exception;

import kw.soft.gourmet.exception.BusinessLogicException;

public class MemberException extends BusinessLogicException {
    private final Code code;

    public MemberException(final Code code) {
        super(code);
        this.code= code;
    }
}
