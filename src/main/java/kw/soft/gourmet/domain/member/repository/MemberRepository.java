package kw.soft.gourmet.domain.member.repository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member,Long> {
    public boolean existsByEmail(final Email email);

    public Optional<Member> findByEmail(final Email email);

    default void validateExistByEmail(final Email email) {
        if (this.existsByEmail(email)) {
            throw new MemberException(Code.ALREADY_EXIST_EMAIL);
        }
    }

    default Member findByEmailHandleException(final Email email) {
        Optional<Member> member = this.findByEmail(email);
        return member.orElseThrow(EntityNotFoundException::new);
    }
}
