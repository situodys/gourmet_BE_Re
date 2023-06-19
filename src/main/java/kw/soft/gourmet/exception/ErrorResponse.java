package kw.soft.gourmet.exception;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public record ErrorResponse(int code, String message, int status, List<FieldError> errors) {
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage(), code.getStatus().value(), Collections.emptyList());
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code.getCode(), code.getMessage(), code.getStatus().value(),
                FieldError.createFieldErrors(bindingResult));
    }

    public record FieldError(String field, String value, String reason) {
        public static List<FieldError> createFieldErrors(final BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .collect(Collectors.toUnmodifiableList());
        }
    }
}
