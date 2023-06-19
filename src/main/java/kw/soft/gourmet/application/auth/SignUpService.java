package kw.soft.gourmet.application.auth;

import kw.soft.gourmet.application.dto.auth.request.SignUpRequest;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.MemberFactory;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;

    public SignUpService(final MemberRepository memberRepository,
                         final MemberFactory memberFactory) {
        this.memberRepository = memberRepository;
        this.memberFactory = memberFactory;
    }

    public void signUp(final SignUpRequest signUpRequest) {
        checkExistenceOfMemberByEmail(signUpRequest);

        Member member = convertToMember(signUpRequest);
        saveMember(member);
    }

    private void checkExistenceOfMemberByEmail(final SignUpRequest signUpRequest) {
        memberRepository.validateExistByEmail(convertToEmail(signUpRequest));
    }

    private Email convertToEmail(SignUpRequest signUpRequest) {
        return memberFactory.createEmail(signUpRequest.email());
    }

    private Member convertToMember(SignUpRequest signUpRequest) {
        return signUpRequest.toMemberWithRoleUser(memberFactory);
    }

    private void saveMember(final Member member) {
        memberRepository.save(member);
    }
}
