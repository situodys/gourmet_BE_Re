package kw.soft.gourmet.domain.restaurant.exception;

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
    INVALID_BREAK_TIME(1007, "운영 시간 없이 휴게 시간이 존재할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_SCHEDULE(1008, "휴게 시간이 운영 시간을 벗어납니다.", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_BUSINESS_SCHEDULES(1009, "운영 시간이 없는 요일이 존재합니다.", HttpStatus.BAD_REQUEST),
    INVALID_START_OF_RUNTIME(1010, "운영 시간 시작은 익일이 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BUSINESS_SCHEDULE_INTERVAL(1011, "다음날 오픈 시간을 넘겨 마감할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(1012, "잘못된 전화번호 형식입니다", HttpStatus.BAD_REQUEST),
    INVALID_GEOPOINT(1013, "유효하지 않은 위도,경도 값입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS(1014, "주소값은 존재하지 없습니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}