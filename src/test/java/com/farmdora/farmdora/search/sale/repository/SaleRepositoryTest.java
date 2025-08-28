package com.farmdora.farmdora.search.sale.repository;

import com.farmdora.farmdora.entity.*;
import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("findTop10ByTypeAndIdNotOrderByIdDesc 쿼리메서드 테스트")
    void testFindTop10ByTypeAndIdNotOrderByIdDesc() {
        // given
        User user = new User();
        em.persist(user);

        SaleType type = SaleType.builder()
                .id((short) 1)
                .name("소분류1")
                .build();
        em.persist(type);

        Sale excludeSale = Sale.builder()
                .type(type)
                .build();
        em.persist(excludeSale);

        for (int i = 1; i <= 9; i++) {
            Sale sale = Sale.builder()
                    .title("title" + i)
                    .type(type)
                    .build();
            em.persist(sale);

            if (i % 2 == 0) {
                Review review1 = Review.builder()
                        .sale(sale)
                        .score((byte) 3)
                        .build();
                Review review2 = Review.builder()
                        .sale(sale)
                        .score((byte) 4)
                        .build();
                em.persist(review1);
                em.persist(review2);
            } else {
                Like like = Like.builder()
                        .sale(sale)
                        .user(user)
                        .build();
                em.persist(like);

                Option option1 = Option.builder()
                        .sale(sale)
                        .price(1000)
                        .build();
                Option option2 = Option.builder()
                        .sale(sale)
                        .price(2000)
                        .build();
                em.persist(option1);
                em.persist(option2);
            }
        }
        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<SaleRelatedInfoDto> sales = saleRepository.findTop10SalesWithReviewCountByTypeAndExcludedId(type, excludeSale.getId(), pageable);

        // then
        assertThat(sales.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("상위 50개 상품 목록 조회")
    void testFindTop50ByOrderCount() {
        // given
        for (int i = 0; i < 50; i++) {
            Sale sale = Sale.builder()
                    .title("title" + i)
                    .build();
            em.persist(sale);

            Option option1 = Option.builder()
                    .sale(sale)
                    .price(10000)
                    .build();
            Option option2 = Option.builder()
                    .sale(sale)
                    .price(20000)
                    .build();
            em.persist(option1);
            em.persist(option2);

            Order order1 = new Order();
            Order order2 = new Order();
            em.persist(order1);
            em.persist(order2);

            OrderOption orderOption1 = OrderOption.builder()
                    .order(order1)
                    .option(option1)
                    .build();
            OrderOption orderOption2 = OrderOption.builder()
                    .order(order2)
                    .option(option2)
                    .build();
            em.persist(orderOption1);
            em.persist(orderOption2);
        }

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<SaleRankingDto> sales = saleRepository.findTop50ByOrderCount(pageable);
        System.out.println(sales.getContent());

        // then
        assertThat(sales.getContent().size()).isEqualTo(10);
    }
}