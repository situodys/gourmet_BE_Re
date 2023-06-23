package kw.soft.gourmet.application.auth;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import kw.soft.gourmet.application.dto.auth.TokenPayload;
import kw.soft.gourmet.application.dto.auth.request.LoginRequest;
import kw.soft.gourmet.common.fixtures.AuthFixtures;
import kw.soft.gourmet.domain.auth.AuthTokens;
import kw.soft.gourmet.domain.auth.TokenProvider;
import kw.soft.gourmet.domain.auth.exception.AuthException;
import kw.soft.gourmet.domain.auth.exception.Code;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberFactory memberFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private TokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("로그인 성공 시 access,refresh token을 발급한다.")
    public void login() throws Exception {
        //given
        LoginRequest loginRequest = AuthFixtures.LOGIN_REQUEST;
        Member member = AuthFixtures.MEMBER_WITH_ID;
        AuthTokens expect = AuthFixtures.AUTH_TOKENS;

        given(memberFactory.createEmail(any(String.class))).willReturn(AuthFixtures.MEMBER_EMAIL);
        given(memberRepository.findByEmailHandleException(any(Email.class))).willReturn(member);
        given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

        //when
        AuthTokens authTokens = loginService.login(loginRequest);

        //then
        verify(jwtTokenProvider, times(1)).createAuthTokens(any(TokenPayload.class));
    }

    @Test
    @DisplayName("이메일로 회원을 찾을 수 없을 때 예외를 발생시킨다.")
    public void throwsExceptionWhenMemberNotFoundByEmail() throws Exception {
        //given
        LoginRequest loginRequest = AuthFixtures.LOGIN_REQUEST;
        given(memberFactory.createEmail(any(String.class))).willReturn(AuthFixtures.MEMBER_EMAIL);
        given(memberRepository.findByEmailHandleException(any(Email.class))).willThrow(
                JpaObjectRetrievalFailureException.class);

        //when,then
        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(JpaObjectRetrievalFailureException.class);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않은 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenPasswordIsWrong() throws Exception {
        //given
        final String wrongPassword = "wrongPassword12!@";
        LoginRequest loginRequest = AuthFixtures.LOGIN_REQUEST;
        given(memberFactory.createEmail(any(String.class))).willReturn(AuthFixtures.MEMBER_EMAIL);
        given(memberRepository.findByEmailHandleException(any(Email.class)))
                .willReturn(AuthFixtures.MEMBER_WITH_ID);

        //when,then
        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(AuthException.class)
                .extracting("code")
                .usingRecursiveComparison()
                .isEqualTo(Code.INCORRECT_PASSWORD);
    }
}
