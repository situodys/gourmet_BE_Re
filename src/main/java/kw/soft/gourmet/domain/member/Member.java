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
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Builder(access = AccessLevel.PACKAGE)
    private Member(final Long id, final Email email, final Password password,
                   final Authority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String another) {
        return passwordEncoder.matches(another, this.password.getValue());
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Authority getAuthority() {
        return authority;
    }
}
