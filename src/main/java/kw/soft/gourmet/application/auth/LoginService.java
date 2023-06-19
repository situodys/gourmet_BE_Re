package kw.soft.gourmet.application.auth;

import kw.soft.gourmet.application.dto.auth.TokenPayload;
import kw.soft.gourmet.application.dto.auth.request.LoginRequest;
import kw.soft.gourmet.domain.auth.AuthTokens;
import kw.soft.gourmet.domain.auth.TokenProvider;
import kw.soft.gourmet.domain.auth.exception.AuthException;
import kw.soft.gourmet.domain.auth.exception.Code;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final TokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;
    private final PasswordEncoder passwordEncoder;

    public LoginService(@Qualifier("jwt") final TokenProvider tokenProvider,
                        final MemberRepository memberRepository,
                        final MemberFactory memberFactory,
                        final PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
        this.memberFactory = memberFactory;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthTokens login(final LoginRequest loginRequest) {
        Member member = findMemberByEmail(loginRequest);
        checkPassword(loginRequest, member);

        return createAuthTokens(member);
    }

    private Member findMemberByEmail(final LoginRequest loginRequest) {
        return memberRepository.findByEmailHandleException(convertToEmail(loginRequest));
    }

    private Email convertToEmail(LoginRequest loginRequest) {
        return memberFactory.createEmail(loginRequest.email());
    }

    private void checkPassword(final LoginRequest loginRequest, final Member member) {
        if (!member.isPasswordMatch(passwordEncoder, loginRequest.password())) {
            throw new AuthException(Code.INCORRECT_PASSWORD);
        }
    }

    private AuthTokens createAuthTokens(final Member member) {
        return jwtTokenProvider.createAuthTokens(convertToPayload(member));
    }

    private TokenPayload convertToPayload(Member member) {
        return TokenPayload.from(member);
    }
}
