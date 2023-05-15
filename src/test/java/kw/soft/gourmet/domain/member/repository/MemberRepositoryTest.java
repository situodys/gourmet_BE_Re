package kw.soft.gourmet.domain.member.repository;

import kw.soft.gourmet.common.factory.MemberFactory;
import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.domain.member.Member;
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
        Member memberWithHighPasswordPolicy = MemberFactory.createMemberWithHighPasswordPolicy();

        //when
        Member saved = memberRepository.save(memberWithHighPasswordPolicy);

        //then
        Assertions.assertThat(saved.getId()).isNotNull();
    }
}