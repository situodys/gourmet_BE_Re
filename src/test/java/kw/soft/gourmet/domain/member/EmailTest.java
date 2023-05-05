package kw.soft.gourmet.domain.member;

import static org.assertj.core.api.Assertions.*;

import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "", " ", "   ",
            "test@naver.com",
            "test@nate.com",
            "@kw.ac.kr",
            "a!@kw.ac.kr",
            "test@gol.er.kr",
            "가나다@kw.ac.kr"
    })
    @NullSource
    @DisplayName("잘못된 이메일 형식인 경우 예외를 발생시킨다")
    public void throwsExceptionWhenInvalidEmail(String value) {
        //then
        assertThatThrownBy(() -> new Email(value))
                .isInstanceOf(MemberException.class)
                .extracting("code").isEqualTo(Code.INVALID_EMAIL);
    }
}