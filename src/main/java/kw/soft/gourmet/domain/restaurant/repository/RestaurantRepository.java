package kw.soft.gourmet.domain.restaurant.repository;

import kw.soft.gourmet.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
