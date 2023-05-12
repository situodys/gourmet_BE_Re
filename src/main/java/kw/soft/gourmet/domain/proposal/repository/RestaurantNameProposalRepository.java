package kw.soft.gourmet.domain.proposal.repository;

import kw.soft.gourmet.domain.proposal.RestaurantNameProposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantNameProposalRepository extends JpaRepository<RestaurantNameProposal,Long> {
}
