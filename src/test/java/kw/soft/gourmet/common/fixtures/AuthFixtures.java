package kw.soft.gourmet.common.fixtures;

import kw.soft.gourmet.application.auth.JwtTokenProvider;
import kw.soft.gourmet.application.dto.auth.TokenPayload;
import kw.soft.gourmet.application.dto.auth.request.LoginRequest;
import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import kw.soft.gourmet.domain.auth.AccessToken;
import kw.soft.gourmet.domain.auth.AuthTokens;
import kw.soft.gourmet.domain.auth.RefreshToken;
import kw.soft.gourmet.domain.auth.TokenProvider;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.Password;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthFixtures {
    public static final PasswordPolicy highPasswordPolicy = new HighPasswordPolicy();
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static final MemberFactory memberFactory = new MemberFactory(highPasswordPolicy, passwordEncoder);
    public static final TokenProvider jwtTokenProvider = new JwtTokenProvider(
            "A".repeat(32),
            36000L,
            72000L
    );

    public static final Long MEMBER_ID = 12345L;
    public static final String MEMBER_EMAIL_STRING = "test@kw.ac.kr";
    public static final Email MEMBER_EMAIL = memberFactory.createEmail(MEMBER_EMAIL_STRING);
    public static final String MEMBER_HIGH_POLICY_PASSWORD_STRING = "test12@#";
    public static final Password MEMBER_HIGH_POLICY_PASSWORD = memberFactory.createPassword(
            MEMBER_HIGH_POLICY_PASSWORD_STRING);
    public static final Member MEMBER_WITH_ID = memberFactory.createMemberWithRoleUser()
            .id(MEMBER_ID)
            .email(memberFactory.createEmail(MEMBER_EMAIL_STRING))
            .password(memberFactory.createPassword(MEMBER_HIGH_POLICY_PASSWORD_STRING))
            .build();

    public static final String WRONG_PASSWORD_STRING = "wrongPassword";

    public static final LoginRequest LOGIN_REQUEST = new LoginRequest(MEMBER_EMAIL_STRING,
            MEMBER_HIGH_POLICY_PASSWORD_STRING);
    public static final SignUpRequest SIGN_UP_REQUEST = new SignUpRequest(MEMBER_EMAIL_STRING,
            MEMBER_HIGH_POLICY_PASSWORD_STRING);
    public static final AccessToken ACCESS_TOKEN = jwtTokenProvider.createAccessToken(
            TokenPayload.from(MEMBER_WITH_ID));
    public static final RefreshToken REFRESH_TOKEN = jwtTokenProvider.createRefreshToken(
            TokenPayload.from(MEMBER_WITH_ID));
    public static final AuthTokens AUTH_TOKENS = new AuthTokens(ACCESS_TOKEN, REFRESH_TOKEN);

    private AuthFixtures() {
    }

}
