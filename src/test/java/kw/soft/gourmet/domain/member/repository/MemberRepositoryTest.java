package kw.soft.gourmet.domain.member.repository;

import kw.soft.gourmet.domain.member.Authority;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member createMember() {
        PasswordPolicy passwordPolicy = new HighPasswordPolicy();

        return Member.builder()
                .email("test@kw.ac.kr")
                .passwordPolicy(passwordPolicy)
                .password("test12@#")
                .authority(Authority.USER)
                .build();
    }

    @Test
    @DisplayName("멤버를 저장한다.")
    public void saveMember() throws Exception {
        //given
        Member member = createMember();

        //when
        Member saved = memberRepository.save(member);

        //then
        Assertions.assertThat(saved.getId()).isNotNull();
    }
}