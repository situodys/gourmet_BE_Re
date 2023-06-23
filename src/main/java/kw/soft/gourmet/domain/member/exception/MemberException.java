package kw.soft.gourmet.domain.member.exception;

import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class MemberException extends BusinessLogicException {
    private final ErrorCode code;

    public MemberException(final ErrorCode code) {
        super(code);
        this.code= code;
    }
}
