package kw.soft.gourmet.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Email {
    private static final String EMAIL_REGEX = "^[0-9a-zA-Z+_.-]+(@kw.ac.kr)|(gmail.com)";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    Email(final String value) {
        checkValidEmail(value);
        this.value = value;
    }

    private void checkValidEmail(final String email) {
        if (email == null || !pattern.matcher(email).matches()) {
            throw new MemberException(Code.INVALID_EMAIL);
        }
    }
}
