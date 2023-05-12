package kw.soft.gourmet.domain.proposal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code {
    EMPTY_TITLE(5001,"요청사항 제목이 비어 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TITLE_LENGTH(5002, "요청사항 제목은 100자 이내 입니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
