package kw.soft.gourmet.domain.proposal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.proposal.exception.Code;
import kw.soft.gourmet.domain.proposal.exception.ProposalException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Title {
    private static final int MAX_TITLE_LENGTH = 100;

    @Column(name = "title", nullable = false)
    private String value;

    public Title(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(final String value) {
        if (value == null || value.isBlank()) {
            throw new ProposalException(Code.EMPTY_TITLE);
        }
    }

    private void checkProperLength(final String value) {
        if (value.length() > MAX_TITLE_LENGTH) {
            throw new ProposalException(Code.INVALID_TITLE_LENGTH);
        }
    }
}
