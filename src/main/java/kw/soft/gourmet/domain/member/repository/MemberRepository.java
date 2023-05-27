package kw.soft.gourmet.domain.member.repository;

import kw.soft.gourmet.domain.member.Email;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.exception.Code;
import kw.soft.gourmet.domain.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends CrudRepository<Member,Long> {
    public boolean existsByEmail(Email email);

    default void validateExistByEmail(Email email) {
        if (this.existsByEmail(email)) {
            throw new MemberException(Code.ALREADY_EXIST_EMAIL);
        }
    }
}
