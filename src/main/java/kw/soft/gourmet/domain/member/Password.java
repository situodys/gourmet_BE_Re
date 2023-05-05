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
public class Password {
    @Column(name = "password", nullable = false)
    private String value;

    public Password(final String value, final PasswordPolicy passwordPolicy) {
        checkValidPassword(value, passwordPolicy);
        this.value = value;
    }

    private void checkValidPassword(final String password, final PasswordPolicy passwordPolicy) {
        passwordPolicy.validate(password);
    }

}
