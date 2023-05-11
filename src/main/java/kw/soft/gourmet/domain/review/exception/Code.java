package kw.soft.gourmet.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code {
    EMPTY_TITLE(4001,"리뷰 제목이 비어 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TITLE_LENGTH(4002,"리뷰 제목은 100자 이내 입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_CONTENT(4003,"리뷰 내용이 비어 있습니다.",HttpStatus.BAD_REQUEST),
    INVALID_CONTENT_LENGTH(4004,"리뷰는 1000자 이내 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_RATING_RANGE(4005,"평점은 1이상 5이하의 정수입니다.",HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
