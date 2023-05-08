package kw.soft.gourmet.domain.menu.repository;

import kw.soft.gourmet.domain.menu.ReviewedMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewedMenuRepository extends JpaRepository<ReviewedMenu, Long> {
}
