package com.farmdora.farmdora.search.sale.repository;

import com.farmdora.farmdora.entity.Like;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("existsByUserIdAndSaleId() 쿼리 메서드 테스트")
    void testExistsByUserIdAndSaleId() {
        // given
        User user = new User();
        em.persist(user);

        Sale sale = new Sale();
        em.persist(sale);

        Like like1 = Like.builder()
                .user(user)
                .sale(sale)
                .build();
        Like like2 = Like.builder()
                .user(user)
                .sale(sale)
                .build();
        em.persist(like1);
        em.persist(like2);

        em.flush();
        em.clear();

        // when
        boolean like = likeRepository.existsByUserUserIdAndSaleId(user.getUserId(), sale.getId());

        // then
        assertThat(like).isEqualTo(true);
    }
}