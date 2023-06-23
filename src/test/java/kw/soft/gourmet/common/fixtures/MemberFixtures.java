package kw.soft.gourmet.common.fixtures;

import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.Password;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFixtures {
    public static final PasswordPolicy highPasswordPolicy = new HighPasswordPolicy();
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static final MemberFactory memberFactory = new MemberFactory(highPasswordPolicy,passwordEncoder);

    public static final Long USER_ID = 12345L;

    public static final String USER_EMAIL = "test@kw.ac.kr";
    public static final String HUN_EMAIL = "situodys@gmail.com";
    public static final String USER_HIGH_POLICY_PASSWORD = "test12@#";

    private MemberFixtures(){
    }

    public static Member createMemberWithHighPasswordPolicyBcryptEncoded() {
        return memberFactory.createMemberWithRoleUser()
                .email(memberFactory.createEmail(USER_EMAIL))
                .password(memberFactory.createPassword(USER_HIGH_POLICY_PASSWORD))
                .build();
    }

    public static Member createMemberWithHighPasswordPolicyBcryptEncodedIdExist() {
        return memberFactory.createMemberWithRoleUser()
                .id(USER_ID)
                .email(memberFactory.createEmail(USER_EMAIL))
                .password(memberFactory.createPassword(USER_HIGH_POLICY_PASSWORD))
                .build();
    }

    public static Email createEmail() {
        return memberFactory.createEmail(USER_EMAIL);
    }

    public static Email createHunEmail() {
        return memberFactory.createEmail(HUN_EMAIL);
    }

    public static Password createPasswordWithHighPasswordPolicyBcryptEncoded() {
        return memberFactory.createPassword(USER_HIGH_POLICY_PASSWORD);
    }
}
