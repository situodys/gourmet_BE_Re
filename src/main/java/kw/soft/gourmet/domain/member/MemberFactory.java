package kw.soft.gourmet.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MemberFactory {
    private final MemberGenerator memberGenerator = new MemberGenerator();

    public MemberGenerator.MemberBuilder createMemberWithRoleUser() {
        return memberGenerator.generateMemberWithRoleUserBuilder();
    }

    public Email createEmail(final String email) {
        return new Email(email);
    }

    public Password createPassword(final String password, final PasswordPolicy passwordPolicy,
                                   final PasswordEncoder passwordEncoder) {
        return new Password(password, passwordPolicy, passwordEncoder);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class MemberGenerator {
        @Builder(builderMethodName = "generateMemberWithRoleUserBuilder")
        private Member generateMemberWithRoleUser(final Long id, final Email email, final Password password) {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .authority(Authority.USER)
                    .build();
        }
    }
}
