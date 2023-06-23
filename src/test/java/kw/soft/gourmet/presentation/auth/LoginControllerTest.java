package kw.soft.gourmet.presentation.auth;

import static kw.soft.gourmet.domain.auth.exception.Code.INCORRECT_PASSWORD;
import static kw.soft.gourmet.presentation.exception.Code.INVALID_INPUT;
import static kw.soft.gourmet.util.restdocs.RestDocsConfig.field;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kw.soft.gourmet.application.dto.auth.request.LoginRequest;
import kw.soft.gourmet.common.fixtures.AuthFixtures;
import kw.soft.gourmet.domain.auth.exception.AuthException;
import kw.soft.gourmet.presentation.ControllerTest;
import kw.soft.gourmet.util.restdocs.ErrorResponseSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.http.HttpRequestSnippet;

public class LoginControllerTest extends ControllerTest {

    @Test
    @DisplayName("로그인 성공 시 200상태 코드와 accessToken,refreshToken을 반환한다.")
    public void login() throws Exception {
        //given
        LoginRequest loginRequest = AuthFixtures.LOGIN_REQUEST;
        given(loginService.login(any(LoginRequest.class))).willReturn(AuthFixtures.AUTH_TOKENS);

        //when,then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andDo(restDocs)
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").description("이메일")
                                        .attributes(field("제한사항", "kw.ac.kr or gmail.com")),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(field("제한사항", "8자이상,소문자,특수문자 한개 이상"))
                        ),
                        responseFields(
                                fieldWithPath("atk.value").description("액세스 토큰"),
                                fieldWithPath("rtk.value").description("리프레쉬 토큰")
                        ),
                        new ErrorResponseSnippet(INVALID_INPUT, INCORRECT_PASSWORD)
                ));
    }

    @ParameterizedTest
    @CsvSource({"test12@kw.ac.kr, ", "'      ',test12!@","'', test12!@"})
    @DisplayName("입력값이 비어있거나 null인 경우 예외를 발생시킨다")
    public void returnExceptionWhenInvalidInput(String email, String password) throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest(email, password);

        //when,then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는 경우 예외를 발생시킨다")
    public void returnExceptionWhenPasswordIsNotMatch() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest(
                AuthFixtures.MEMBER_EMAIL_STRING,
                AuthFixtures.WRONG_PASSWORD_STRING);
        given(loginService.login(any(LoginRequest.class))).willThrow(new AuthException(INCORRECT_PASSWORD));

        //when,then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isBadRequest());
    }
}
