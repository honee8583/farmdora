package com.farmdora.farmdora.search.sale.repository;

import com.farmdora.farmdora.entity.*;
import com.farmdora.farmdora.sale.dto.CategorySearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSortType;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomSaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("카테고리에 해당하는 상품 조회 QueryDsl 테스트")
    void testSearchSalesByCategories() {
        // given
        User user1 = new User();
        User user2 = new User();
        em.persist(user1);
        em.persist(user2);

        SaleTypeBig bigType = SaleTypeBig.builder()
                .id((short) 1)
                .build();
        em.persist(bigType);

        SaleType type = SaleType.builder()
                .id((short) 1)
                .saleTypeBig(bigType)
                .build();
        em.persist(type);

        for (int i = 1; i <= 10; i++) {
            Sale sale = Sale.builder()
                    .title("Sale" + i)
                    .type(type)
                    .build();
            em.persist(sale);

            if (i % 2 != 0) {
                Review review = Review.builder()
                        .sale(sale)
                        .build();
                em.persist(review);
            }

            SaleFile saleFile1 = SaleFile.builder()
                    .saveFile("saleFile1")
                    .sale(sale)
                    .isMain(false)
                    .build();
            SaleFile saleFile2 = SaleFile.builder()
                    .saveFile("saleFile2")
                    .sale(sale)
                    .isMain(true)
                    .build();
            em.persist(saleFile1);
            em.persist(saleFile2);

            Order order1 = new Order();
            Order order2 = new Order();
            em.persist(order1);
            em.persist(order2);

            Option option1 = Option.builder()
                    .sale(sale)
                    .price(1000 * i)
                    .build();
            Option option2 = Option.builder()
                    .sale(sale)
                    .price(2000 * i)
                    .build();
            em.persist(option1);
            em.persist(option2);

            OrderOption orderOption1 = OrderOption.builder()
                    .order(order1)
                    .option(option1)
                    .build();
            em.persist(orderOption1);

            if (i % 2 == 0) {
                OrderOption orderOption2 = OrderOption.builder()
                        .order(order2)
                        .option(option2)
                        .build();
                em.persist(orderOption2);
            }

            if (i % 2 == 0) {
                em.persist(Like.builder()
                        .sale(sale)
                        .user(user1)
                        .build());
            }
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        CategorySearchRequestDto searchCondition1 = CategorySearchRequestDto.builder()
                .keyword(null)
                .typeId(type.getId())
                .bigTypeId(bigType.getId())
                .sort(SaleSortType.PRICE_DESC)
                .build();
        Page<SaleSummaryDto> salesByType = saleRepository.searchSalesByCategories(user1.getUserId(), searchCondition1, pageable);

        CategorySearchRequestDto searchCondition2 = CategorySearchRequestDto.builder()
                .keyword(null)
                .typeId(null)
                .bigTypeId(bigType.getId())
                .sort(SaleSortType.ORDER_DESC)
                .build();
        Page<SaleSummaryDto> salesByBigType = saleRepository.searchSalesByCategories(user1.getUserId(), searchCondition2, pageable);

        CategorySearchRequestDto searchCondition3 = CategorySearchRequestDto.builder()
                .keyword(null)
                .typeId(null)
                .bigTypeId(null)
                .sort(SaleSortType.REVIEW_DESC)
                .build();
        Page<SaleSummaryDto> salesByNoType = saleRepository.searchSalesByCategories(user1.getUserId(), searchCondition3, pageable);

        CategorySearchRequestDto searchCondition4 = CategorySearchRequestDto.builder()
                .keyword(null)
                .typeId(null)
                .bigTypeId(null)
                .sort(SaleSortType.RECOMMEND)
                .build();
        Page<SaleSummaryDto> salesByLikeCount = saleRepository.searchSalesByCategories(user1.getUserId(), searchCondition4, pageable);

        // then
        assertThat(salesByType.getContent().size()).isEqualTo(10);
        assertThat(salesByBigType.getContent().size()).isEqualTo(10);
        assertThat(salesByNoType.getContent().size()).isEqualTo(10);
        assertThat(salesByLikeCount.getContent().size()).isEqualTo(10);
    }
}