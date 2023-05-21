package kw.soft.gourmet.common.factory;

import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.Password;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFixtures {
    private static final PasswordPolicy highPasswordPolicy = new HighPasswordPolicy();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final MemberFactory memberFactory = new MemberFactory();

    public static final String USER_EMAIL = "test@kw.ac.kr";
    public static final String USER_HIGH_POLICY_PASSWORD = "test12@#";

    private MemberFixtures(){
    }

    public static Member createMemberWithHighPasswordPolicyBcryptEncoded() {
        return memberFactory.createMemberWithRoleUser()
                .email(memberFactory.createEmail(USER_EMAIL))
                .password(memberFactory.createPassword(USER_HIGH_POLICY_PASSWORD, highPasswordPolicy, passwordEncoder))
                .build();
    }

    public static Email createEmail() {
        return memberFactory.createEmail(USER_EMAIL);
    }

    public static Password createPasswordWithHighPasswordPolicyBcryptEncoded() {
        return memberFactory.createPassword(USER_HIGH_POLICY_PASSWORD, highPasswordPolicy, passwordEncoder);
    }
}
