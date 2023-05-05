package kw.soft.gourmet.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code {

    INVALID_EMAIL(3001, "유효하지 않은 이메일 형식입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(3002, "유효하지 않은 형식의 비밀번호입니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
