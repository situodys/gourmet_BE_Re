package kw.soft.gourmet.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "", " ", "   ",
            "testtest",
            "12345678",
            "!@#$%^&*(",
            "test1234",
            "test!@#$",
            "1234!@#$",
            "test12#"
    })
    @NullSource
    @DisplayName("높은 수준의 비밀번호 정책에 대해 잘못된 이메일 형식인 경우 예외를 발생시킨다")
    public void throwsExceptionWhenInvalidPasswordWithHighPasswordPolicy(String value) {
        //given
        PasswordPolicy passwordPolicy = new HighPasswordPolicy();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //then
        assertThatThrownBy(() -> new Password(value, passwordPolicy,passwordEncoder))
                .isInstanceOf(MemberException.class)
                .extracting("code").isEqualTo(Code.INVALID_PASSWORD);
    }
}
