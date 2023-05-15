package kw.soft.gourmet.domain.review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewedMenus {
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewedMenu> reviewedMenus = new ArrayList<>();

    public ReviewedMenus(final List<ReviewedMenu> reviewedMenus) {
        this.reviewedMenus = new ArrayList<>(reviewedMenus);
    }

    public void updateReview(Review review) {
        for (ReviewedMenu reviewedMenu : reviewedMenus) {
            reviewedMenu.updateReview(review);
        }
    }
}
