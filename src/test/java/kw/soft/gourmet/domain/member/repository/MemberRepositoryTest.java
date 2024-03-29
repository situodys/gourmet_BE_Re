package kw.soft.gourmet.domain.member.repository;

import static org.assertj.core.api.Assertions.*;

import kw.soft.gourmet.common.fixtures.MemberFixtures;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

@RepositoryTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버를 저장한다.")
    public void saveMember() throws Exception {
        //given
        Member memberWithHighPasswordPolicy = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();

        //when
        Member saved = memberRepository.save(memberWithHighPasswordPolicy);

        //then
        assertThat(saved.getId()).isNotNull();
    }
    @Test
    @DisplayName("이메일로 회원을 확인한다")
    public void validateExistByEmail() throws Exception{
        //given
        Email email = MemberFixtures.createEmail();

        //then
        memberRepository.validateExistByEmail(email);
    }

    @Test
    @DisplayName("이미 저장된 이메일인 경우 예외를 발생시킨다.")
    public void throwsExceptionWhenDuplicatedEmailExists() throws Exception{
        //given
        Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();
        memberRepository.save(member);

        //when
        Email email = MemberFixtures.createEmail();

        //then
        assertThatThrownBy(() -> memberRepository.validateExistByEmail(email))
                .isInstanceOf(MemberException.class)
                .extracting("code").isEqualTo(Code.ALREADY_EXIST_EMAIL);
    }

    @Test
    @DisplayName("이메일로 회원을 찾을 수 있을 때 해당 회원을 반환한다.")
    public void findByEmailHandleException() throws Exception{
        //given
        Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();
        Email email = MemberFixtures.createEmail();
        memberRepository.save(member);

        //when
        Member saved = memberRepository.findByEmailHandleException(email);

        //then
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("이메일로 회원을 찾을 수 없을 때 예외를 발생시킨다.")
    public void throwsExceptionWhenMemberNotFoundByEmail() throws Exception{
        //given
        Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();
        memberRepository.save(member);

        //when
        Email hun = MemberFixtures.createHunEmail();

        //then
        assertThatThrownBy(() -> memberRepository.findByEmailHandleException(hun))
                .isInstanceOf(JpaObjectRetrievalFailureException.class);
    }
}