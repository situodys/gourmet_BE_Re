package kw.soft.gourmet.common.factory;

import kw.soft.gourmet.domain.member.Authority;
import kw.soft.gourmet.domain.member.HighPasswordPolicy;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.PasswordPolicy;

public class MemberFactory {
    private static PasswordPolicy highPasswordPolicy = new HighPasswordPolicy();

    public static Member createMemberWithHighPasswordPolicy() {
        return Member.builder()
                .email("test@kw.ac.kr")
                .passwordPolicy(highPasswordPolicy)
                .password("test12@#")
                .authority(Authority.USER)
                .build();
    }

    private MemberFactory() {
    }
}
