package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Column(name = "image_url")
    private String imageUrl;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "restaurant_type")
    private RestaurantType restaurantType;

    @Embedded
    private BusinessSchedules businessSchedules = new BusinessSchedules();

    @Builder
    public Restaurant(final Long id, final Name name, final Description description, final String imageUrl,
                      final PhoneNumber phoneNumber, final Address address,
                      final RestaurantType restaurantType, final BusinessSchedules businessSchedules) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.restaurantType = restaurantType;
        this.businessSchedules = businessSchedules;
        updateBusinessSchedules(businessSchedules);
    }

    private void updateBusinessSchedules(final BusinessSchedules businessSchedules) {
        businessSchedules.updateRestaurant(this);
    }

    public Long getId() {
        return id;
    }
}
