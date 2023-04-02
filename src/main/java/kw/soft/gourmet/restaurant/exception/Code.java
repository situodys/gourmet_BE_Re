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

    INVALID_BUSINESS_HOUR(1005, "시간이 설정되지 않았습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RUNTIME(1006, "운영 시간이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_SCHEDULE(1007, "휴게 시간이 운영 시간을 벗어납니다.", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_BUSINESS_SCHEDULES(1008, "운영 시간이 없는 요일이 존재합니다.", HttpStatus.BAD_REQUEST),
    INVALID_START_OF_RUNTIME(1009, "운영 시간 시작은 익일이 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_SCHEDULE_INTERVAL(1010,"다음날 오픈 시간을 넘겨 마감할 수 없습니다." ,HttpStatus.BAD_REQUEST );

    private final int code;
    private final String message;
    private final HttpStatus status;
}
