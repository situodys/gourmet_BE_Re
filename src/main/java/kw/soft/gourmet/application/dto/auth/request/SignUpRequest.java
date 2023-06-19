package kw.soft.gourmet.application.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpRequest(@NotBlank String email, @NotBlank String password) {
    public Member toMemberWithRoleUser(final MemberFactory memberFactory) {
        return memberFactory.createMemberWithRoleUser()
                .email(memberFactory.createEmail(email))
                .password(memberFactory.createPassword(password))
                .build();
    }
}
