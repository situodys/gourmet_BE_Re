package kw.soft.gourmet.domain.member.repository;

import static org.assertj.core.api.Assertions.*;

import kw.soft.gourmet.common.factory.MemberFixtures;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}