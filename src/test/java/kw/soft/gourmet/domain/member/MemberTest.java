package kw.soft.gourmet.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import kw.soft.gourmet.common.fixtures.MemberFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    @DisplayName("주어진 값과 비밀번호가 일치하지 않으면 false를 반환한다")
    public void ReturnFalseWhenPasswordIsNotMatch() throws Exception{
        //given
        Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();
        String another = "another";

        //when
        boolean result = member.isPasswordMatch(MemberFixtures.passwordEncoder, another);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 값과 비밀번호가 일치하면 true를 반환한다")
    public void ReturnTrueWhenPasswordIsMatch() throws Exception{
        //given
        Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();
        String another = MemberFixtures.USER_HIGH_POLICY_PASSWORD;

        //when
        boolean result = member.isPasswordMatch(MemberFixtures.passwordEncoder, another);

        //then
        assertThat(result).isTrue();
    }
}
