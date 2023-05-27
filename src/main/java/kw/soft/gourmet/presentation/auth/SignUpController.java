package kw.soft.gourmet.presentation.auth;

import jakarta.validation.Valid;
import kw.soft.gourmet.application.auth.SignUpService;
import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/signup")
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(final SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("")
    public ResponseEntity<Void> signUp(final @RequestBody @Valid SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
