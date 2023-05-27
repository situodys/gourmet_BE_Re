package kw.soft.gourmet.application.auth;

import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.PasswordPolicy;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final MemberRepository memberRepository;
    private final PasswordPolicy passwordPolicy;
    private final MemberFactory memberFactory;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(final MemberRepository memberRepository,
                         @Qualifier("high") final PasswordPolicy passwordPolicy,
                         final MemberFactory memberFactory,
                         final PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordPolicy = passwordPolicy;
        this.memberFactory = memberFactory;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(final SignUpRequest signUpRequest) {
        memberRepository.validateExistByEmail(memberFactory.createEmail(signUpRequest.email()));

        Member member = signUpRequest.toMemberWithRoleUser(memberFactory, passwordPolicy, passwordEncoder);
        memberRepository.save(member);
    }
}
