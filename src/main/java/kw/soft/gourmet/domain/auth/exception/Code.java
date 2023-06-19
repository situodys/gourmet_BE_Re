package kw.soft.gourmet.domain.auth.exception;

import kw.soft.gourmet.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code implements ErrorCode {
    INCORRECT_PASSWORD(6000, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
