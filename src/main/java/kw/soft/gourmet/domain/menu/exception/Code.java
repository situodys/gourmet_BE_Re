package kw.soft.gourmet.domain.menu.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Code {
    INVALID_MENU_PRICE(2001, "가격은 음수 일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_MARKET_PRICE(2002, "싯가의 가격은 설정할 수 없습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_DESCRIPTION(2003, "메뉴 설명이 비어있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DESCRIPTION_LENGTH(2004, "메뉴 설명은 500자 이내 입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_NAME(2005, "메뉴명이 비어있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_NAME_LENGTH(2006, "메뉴명은 100자 이내 입니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
