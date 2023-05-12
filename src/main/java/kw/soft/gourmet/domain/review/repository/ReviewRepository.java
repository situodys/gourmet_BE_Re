package kw.soft.gourmet.domain.review.repository;

import kw.soft.gourmet.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
