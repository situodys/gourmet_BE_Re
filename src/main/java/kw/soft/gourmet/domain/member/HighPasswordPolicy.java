package kw.soft.gourmet.domain.member;

import java.util.regex.Pattern;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;

public class HighPasswordPolicy implements PasswordPolicy {
    private static final String AT_LEAST_ONE_SMALL_ALPHABET_ONE_NUMBER_ONE_SPECIAL_WORD_OVER_8_WORDS_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(
            AT_LEAST_ONE_SMALL_ALPHABET_ONE_NUMBER_ONE_SPECIAL_WORD_OVER_8_WORDS_REGEX
    );

    @Override
    public void validate(String password) {
        if (password == null || !pattern.matcher(password).matches()) {
            throw new MemberException(Code.INVALID_PASSWORD);
        }
    }
}
