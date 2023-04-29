package kw.soft.gourmet.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kw.soft.gourmet.domain.menu.exception.MenuException;
import kw.soft.gourmet.domain.menu.exception.Code;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Description {
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    @Column(name = "description")
    private String value;

    public Description(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        checkEmpty(value);
        checkProperLength(value);
    }

    private void checkEmpty(final String value) {
        if (value == null || value.isBlank()) {
            throw new MenuException(Code.EMPTY_DESCRIPTION);
        }
    }

    private void checkProperLength(final String value) {
        if (value.length() > MAX_DESCRIPTION_LENGTH) {
            throw new MenuException(Code.INVALID_DESCRIPTION_LENGTH);
        }
    }
}
