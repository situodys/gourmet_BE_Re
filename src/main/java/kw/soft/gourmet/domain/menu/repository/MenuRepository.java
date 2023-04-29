package kw.soft.gourmet.domain.menu.repository;

import kw.soft.gourmet.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
