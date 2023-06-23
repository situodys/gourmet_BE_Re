package kw.soft.gourmet.presentation.auth;

import static kw.soft.gourmet.domain.member.exception.Code.ALREADY_EXIST_EMAIL;
import static kw.soft.gourmet.presentation.exception.Code.INVALID_INPUT;
import static kw.soft.gourmet.util.restdocs.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import kw.soft.gourmet.common.fixtures.AuthFixtures;
import kw.soft.gourmet.domain.member.exception.MemberException;
import kw.soft.gourmet.presentation.ControllerTest;
import kw.soft.gourmet.util.restdocs.ErrorResponseSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

public class SignUpControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원 가입 성공 시 201 상태코드를 반환한다.")
    public void signUp() throws Exception {
        //given
        SignUpRequest signUpRequest = AuthFixtures.SIGN_UP_REQUEST;
        willDoNothing().given(signUpService).signUp(any(SignUpRequest.class));

        //when,then
        mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isCreated())
                .andDo(restDocs)
                .andDo(restDocs.document(
                                requestFields(
                                        fieldWithPath("email").description("이메일")
                                                .attributes(field("제한사항", "kw.ac.kr or gmail.com")),
                                        fieldWithPath("password").description("비밀번호")
                                                .attributes(field("제한사항", "8자이상,소문자,특수문자 한개 이상"))
                                ),
                                new ErrorResponseSnippet(INVALID_INPUT,ALREADY_EXIST_EMAIL)
                        )
                );
    }

    @Test
    @DisplayName("이미 가입된 이메일인 경우 예외를 발생시킨다")
    public void signUpFailByAlreadyExistEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = AuthFixtures.SIGN_UP_REQUEST;
        willThrow(new MemberException(ALREADY_EXIST_EMAIL)).given(signUpService).signUp(any(SignUpRequest.class));

        //when,then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isConflict())
                .andDo(restDocs.document());
    }

    @ParameterizedTest
    @CsvSource({"test12@kw.ac.kr, ", "'      ',test12!@","'', test12!@"})
    @DisplayName("유효하지 않은 요청값인 경우 예외를 발생시킨다.")
    public void signUpFailByEmptyField(String email, String password) throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(email, password);

        //when,then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                ).andExpect(status().isBadRequest())
                .andDo(restDocs.document());
    }
}
