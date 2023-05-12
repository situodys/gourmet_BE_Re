package kw.soft.gourmet.domain.proposal.repository;

import kw.soft.gourmet.domain.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal,Long> {
}
