package kw.soft.gourmet.domain.member.exception;

import kw.soft.gourmet.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code implements ErrorCode {

    INVALID_EMAIL(3001, "유효하지 않은 이메일 형식입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(3002, "유효하지 않은 형식의 비밀번호입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_EMAIL(3003, "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
