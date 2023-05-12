package kw.soft.gourmet.domain.proposal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kw.soft.gourmet.domain.member.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "proposal_category")
public abstract class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="proposal_id")
    private Long id;

    @Embedded
    private Title title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Proposal(final Long id, final String title, final State state, final Member member) {
        this.id = id;
        this.title = new Title(title);
        this.state = state;
        this.member = member;
    }

    public Long getId() {
        return id;
    }
}
