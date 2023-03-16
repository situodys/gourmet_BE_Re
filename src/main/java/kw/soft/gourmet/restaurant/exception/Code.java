package kw.soft.gourmet.restaurant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code {

    INVALID_NAME_LENGTH(1001, "음식점 이름의 길이는 50자 이내 입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_NAME(1002, "음식점 이름이 비어있습니다.", HttpStatus.BAD_REQUEST),

    INVALID_DESCRIPTION_LENGTH(1003, "음식점 설명은 500자 이내 입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_DESCRIPTION(1004, "음식점 설명이 비어있습니다.", HttpStatus.BAD_REQUEST),

    NOT_ENOUGH_BUSINESSHOURS(1005, "운영 시간이 없는 요일이 존재합니다. ", HttpStatus.BAD_REQUEST), INVALID_BUSINESS_SCHEDULE(1006,
            "휴게 시간이 운영 시간을 벗어납니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
