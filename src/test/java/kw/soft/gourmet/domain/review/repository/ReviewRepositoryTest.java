package kw.soft.gourmet.domain.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kw.soft.gourmet.domain.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰를 저장한다.")
    public void saveReview() throws Exception {
        //given
        Review review = createReview();

        //when
        Review saved = reviewRepository.save(review);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    private Review createReview() {
        return Review.builder()
                .title("title")
                .content("content")
                .rating(3)
                .build();
    }
}
