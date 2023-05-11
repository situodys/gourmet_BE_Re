package kw.soft.gourmet.domain.proposal.exception;

import kw.soft.gourmet.exception.BusinessLogicException;

public class ProposalException extends BusinessLogicException {
    private final Code code;

    public ProposalException(Code code) {
        super(code);
        this.code = code;
    }
}
