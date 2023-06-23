package kw.soft.gourmet.presentation.auth;

import jakarta.validation.Valid;
import kw.soft.gourmet.application.auth.LoginService;
import kw.soft.gourmet.application.dto.auth.request.LoginRequest;
import kw.soft.gourmet.domain.auth.AuthTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("")
    public ResponseEntity<AuthTokens> login(final @RequestBody @Valid LoginRequest loginRequest) {
        AuthTokens authTokens = loginService.login(loginRequest);
        return new ResponseEntity<>(authTokens, HttpStatus.OK);
    }
}
