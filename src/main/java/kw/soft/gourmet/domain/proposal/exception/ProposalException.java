package kw.soft.gourmet.domain.proposal.exception;

import kw.soft.gourmet.exception.BusinessLogicException;
import kw.soft.gourmet.exception.ErrorCode;

public class ProposalException extends BusinessLogicException {
    private final ErrorCode code;

    public ProposalException(ErrorCode code) {
        super(code);
        this.code = code;
    }
}
