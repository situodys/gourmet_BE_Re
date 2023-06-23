package kw.soft.gourmet.application.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import kw.soft.gourmet.common.fixtures.AuthFixtures;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberFactory memberFactory;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("회원가입한다.")
    public void signUp() throws Exception {
        //given
        Member member = AuthFixtures.MEMBER_WITH_ID;
        SignUpRequest signUpRequest = AuthFixtures.SIGN_UP_REQUEST;
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberFactory.createEmail(any(String.class))).willReturn(AuthFixtures.MEMBER_EMAIL);
        given(memberFactory.createMemberWithRoleUser()).willReturn(
                AuthFixtures.memberFactory.createMemberWithRoleUser());
        willDoNothing().given(memberRepository).validateExistByEmail(any());

        //when
        signUpService.signUp(signUpRequest);

        //then
        then(memberRepository).should(times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일인 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenDuplicatedEmailExists() throws Exception {
        //given
        SignUpRequest signUpRequest = AuthFixtures.SIGN_UP_REQUEST;
        given(memberFactory.createEmail(any(String.class))).willReturn(AuthFixtures.MEMBER_EMAIL);
        willThrow(new MemberException(Code.ALREADY_EXIST_EMAIL)).given(memberRepository)
                .validateExistByEmail(any(Email.class));

        //then
        assertThatThrownBy(() -> signUpService.signUp(signUpRequest))
                .isInstanceOf(MemberException.class)
                .extracting("code").usingRecursiveComparison().isEqualTo(Code.ALREADY_EXIST_EMAIL);
    }
}
