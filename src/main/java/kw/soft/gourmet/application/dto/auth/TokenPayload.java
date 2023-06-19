package kw.soft.gourmet.application.dto.auth;

import kw.soft.gourmet.domain.member.Authority;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;

public record TokenPayload(long id, Email email, Authority authority) {
    public static TokenPayload from(Member member) {
        return new TokenPayload(member.getId(), member.getEmail(), member.getAuthority());
    }
}
