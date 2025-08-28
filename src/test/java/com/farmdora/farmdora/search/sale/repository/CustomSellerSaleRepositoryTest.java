package com.farmdora.farmdora.search.sale.repository;

import com.farmdora.farmdora.config.AuditConfig;
import com.farmdora.farmdora.entity.*;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(AuditConfig.class)
class CustomSellerSaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager em;

    private Seller seller;
    private SaleTypeBig bigType;
    private SaleType smallType;

    @BeforeEach
    void setUp() {
        User user = new User();
        em.persist(user);

        seller = Seller.builder()
                .user(user)
                .build();
        em.persist(seller);

        bigType = SaleTypeBig.builder()
                .id((short) 1)
                .name("과일")
                .build();
        em.persist(bigType);

        smallType = SaleType.builder()
                .id((short) 1)
                .name("사과")
                .saleTypeBig(bigType)
                .build();
        em.persist(smallType);

        for (int i = 0; i < 10; i++) {
            Sale sale = Sale.builder()
                    .title("상추" + (i + 1))
                    .isBlind(false)
                    .seller(seller)
                    .type(smallType)
                    .build();
            em.persist(sale);

            Option option1 = Option.builder()
                    .price(100 * (i + 1))
                    .quantity(100)
                    .sale(sale)
                    .build();
            Option option2 = Option.builder()
                    .price(100 * (i + 1))
                    .quantity(100)
                    .sale(sale)
                    .build();
            em.persist(option1);
            em.persist(option2);

            OrderOption orderOption1 = OrderOption.builder()
                    .option(option1)
                    .build();
            OrderOption orderOption2 = OrderOption.builder()
                    .option(option2)
                    .build();
            em.persist(orderOption1);
            em.persist(orderOption2);
        }
    }

    @Test
    @DisplayName("상품 목록 검색 및 조회 테스트")
    void testSearchSales() {
        // given
        Integer sellerId = seller.getUser().getUserId();
        System.out.println(sellerId);
        SaleSearchRequestDto searchCondition = SaleSearchRequestDto.builder()
                .keyword("상추")
                .sort(Sort.LATEST)
                .filters(Set.of(SaleStatus.INSTOCK))
                .typeBigId(bigType.getId())
                .typeId(smallType.getId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<SaleDto> result = saleRepository.searchSales(sellerId, searchCondition, pageable);

        // then
        assertThat(result.getContent().size()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품 목록 주문 수 조회 테스트")
    void testSearchOrderCounts() {
        // given
        List<Sale> sales = saleRepository.findAll();
        List<Integer> saleIds = sales.stream().map(s -> s.getId()).collect(Collectors.toList());

        // when
        List<SaleOrderCountDto> orderCounts = saleRepository.searchSaleOrderCount(saleIds);

        // then
        assertThat(orderCounts.size()).isEqualTo(10);
    }
}