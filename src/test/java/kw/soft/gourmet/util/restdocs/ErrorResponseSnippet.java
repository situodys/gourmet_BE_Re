package kw.soft.gourmet.util.restdocs;

import java.util.Map;
import kw.soft.gourmet.exception.ErrorCode;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

public class ErrorResponseSnippet extends TemplatedSnippet {
    public ErrorResponseSnippet(ErrorCode... errorCodes) {
        super("error-code-types", Map.of("error-code", errorCodes));
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        return operation.getAttributes();
    }
}
