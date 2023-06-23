package kw.soft.gourmet.util.restdocs;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.http.HttpRequestSnippet;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.TemplatedSnippet;

@TestConfiguration
public class RestDocsConfig {
    @Bean
    public RestDocumentationResultHandler restDocumentationResultHandler() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
    }

    public static final Attribute field(final String key, final String value) {
        return new Attribute(key, value);
    }
}
