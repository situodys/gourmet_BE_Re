package kw.soft.gourmet.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    public int getCode();
    public String getMessage();
    public HttpStatus getStatus();
}
