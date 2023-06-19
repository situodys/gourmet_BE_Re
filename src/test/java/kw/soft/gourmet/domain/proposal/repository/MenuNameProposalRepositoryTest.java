package kw.soft.gourmet.domain.proposal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kw.soft.gourmet.common.annotation.RepositoryTest;
import kw.soft.gourmet.common.fixtures.MemberFixtures;
import kw.soft.gourmet.common.fixtures.MenuFixtures;
import kw.soft.gourmet.common.fixtures.ProposalFixtures;
import kw.soft.gourmet.common.fixtures.RestaurantFixtures;
import kw.soft.gourmet.domain.member.Member;
import kw.soft.gourmet.domain.member.repository.MemberRepository;
import kw.soft.gourmet.domain.menu.Menu;
import kw.soft.gourmet.domain.menu.repository.MenuRepository;
import kw.soft.gourmet.domain.proposal.MenuNameProposal;
import kw.soft.gourmet.domain.restaurant.Restaurant;
import kw.soft.gourmet.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class MenuNameProposalRepositoryTest {

    @Autowired
    private MenuNameProposalRepository menuNameProposalRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Restaurant restaurant = RestaurantFixtures.createRestaurant();

    private Menu menu = MenuFixtures.createMenu(restaurant);

    private Member member = MemberFixtures.createMemberWithHighPasswordPolicyBcryptEncoded();

    @BeforeEach
    public void setUp() {
        restaurantRepository.save(restaurant);
        memberRepository.save(member);
        menuRepository.save(menu);
    }

    @Test
    public void saveMenuNameProposal() throws Exception {
        //given
        MenuNameProposal proposal = ProposalFixtures.createMenuNameProposal(menu, member);

        //when
        MenuNameProposal saved = menuNameProposalRepository.save(proposal);

        //then
        assertThat(saved.getId()).isNotNull();
    }
}
