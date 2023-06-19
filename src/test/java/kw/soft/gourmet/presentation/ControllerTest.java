package kw.soft.gourmet.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kw.soft.gourmet.application.auth.LoginService;
import kw.soft.gourmet.application.auth.SignUpService;
import kw.soft.gourmet.presentation.auth.LoginController;
import kw.soft.gourmet.presentation.auth.SignUpController;
import kw.soft.gourmet.util.restdocs.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest({
        SignUpController.class,
        LoginController.class
})
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
public class ControllerTest {

    @MockBean
    protected SignUpService signUpService;

    @MockBean
    protected LoginService loginService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    protected final ObjectMapper objectMapper;

    public ControllerTest() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp(final WebApplicationContext context,
               final RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
