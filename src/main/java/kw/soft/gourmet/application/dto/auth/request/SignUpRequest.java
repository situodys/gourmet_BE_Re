package kw.soft.gourmet.application.dto.auth.request;

import jakarta.validation.constraints.NotEmpty;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpRequest(@NotEmpty String email, @NotEmpty String password) {
    public Member toMemberWithRoleUser(final MemberFactory memberFactory, final PasswordPolicy passwordPolicy,
                                       final PasswordEncoder passwordEncoder) {
        return memberFactory.createMemberWithRoleUser()
                .email(memberFactory.createEmail(email))
                .password(memberFactory.createPassword(password, passwordPolicy, passwordEncoder))
                .build();
    }
}
