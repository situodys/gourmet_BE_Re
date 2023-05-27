package kw.soft.gourmet.presentation.exception;

import kw.soft.gourmet.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code implements ErrorCode {
    INVALID_INPUT(6000, "유효하지 않은 입력값입니다.", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatus status;
}
