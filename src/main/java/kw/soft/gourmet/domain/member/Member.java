package kw.soft.gourmet.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @Builder
    private Member(final Long id, final String email, final String password, final PasswordPolicy passwordPolicy,
                   final Authority authority) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password, passwordPolicy);
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }
}
