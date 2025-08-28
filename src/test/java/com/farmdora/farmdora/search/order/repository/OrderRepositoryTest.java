package com.farmdora.farmdora.search.order.repository;

import com.farmdora.farmdora.config.AuditConfig;
import com.farmdora.farmdora.entity.*;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(AuditConfig.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager em;

    private Seller seller;
    private List<Integer> orderIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("홍길동")
                .isExpire(true)
                .isBlind(true)
                .build();
        em.persist(user);

        OrderStatus status1 = OrderStatus.builder()
                .id((short) 1)
                .name("배송중")
                .build();
        OrderStatus status2 = OrderStatus.builder()
                .id((short) 2)
                .name("판매중")
                .build();
        em.persist(status1);
        em.persist(status2);

        Address address = Address.builder()
                .addr("addr")
                .detailAddr("detailAddr")
                .postNum("05240")
                .build();

        seller = Seller.builder()
                .name("홍길동")
                .user(user)
                .build();
        em.persist(seller);

        Sale sale1 = Sale.builder()
                .title("고구마")
                .seller(seller)
                .build();
        Sale sale2 = Sale.builder()
                .title("감자")
                .seller(seller)
                .build();
        em.persist(sale1);
        em.persist(sale2);

        Option option1 = Option.builder()
                .sale(sale1)
                .price(1000)
                .name("옵션1")
                .build();
        Option option2 = Option.builder()
                .sale(sale2)
                .price(4000)
                .name("옵션2")
                .build();
        Option option3 = Option.builder()
                .sale(sale2)
                .price(9000)
                .name("옵션3")
                .build();
        em.persist(option1);
        em.persist(option2);
        em.persist(option3);

        for (int i = 0; i < 10; i++) {
            Order order;
            if (i % 2 == 0) {
                order = Order.builder()
                        .user(user)
                        .status(status1)
                        .address(address)
                        .build();
            } else {
                order = Order.builder()
                        .user(user)
                        .status(status2)
                        .address(address)
                        .build();
            }
            em.persist(order);
            orderIds.add(order.getId());

            OrderOption orderOption1 = OrderOption.builder()
                    .order(order)
                    .option(option1)
                    .price(1000)
                    .quantity(1)
                    .build();
            OrderOption orderOption2 = OrderOption.builder()
                    .order(order)
                    .option(option2)
                    .price(4000)
                    .quantity(2)
                    .build();
            OrderOption orderOption3 = OrderOption.builder()
                    .order(order)
                    .option(option3)
                    .price(9000)
                    .quantity(3)
                    .build();
            em.persist(orderOption1);
            em.persist(orderOption2);
            em.persist(orderOption3);
        }
    }

    @Test
    @DisplayName("상품명으로 주문목록 검색")
    void testSearchOrdersBySaleTitle() {
        // given
        OrderSearchRequestDto searchCondition = OrderSearchRequestDto.builder()
                .searchType(SearchType.PRODUCT)
                .keyword("고구마")
                .searchPeriod(SearchPeriod.TODAY)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now())
                .statusIds(List.of((short) 1))
                .sort(Sort.LATEST)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<OrderDto> orders = orderRepository.searchOrders(seller.getId(), searchCondition, pageable);

        // then
        assertThat(orders.getContent().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("주문자명으로 주문목록 검색")
    void testSearchOrdersByBuyerName() {
        // given
        OrderSearchRequestDto searchCondition = OrderSearchRequestDto.builder()
                .searchType(SearchType.BUYER)
                .keyword("홍길")
                .searchPeriod(SearchPeriod.TODAY)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now())
                .statusIds(List.of((short) 2))
                .sort(Sort.PRICE_ASC)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<OrderDto> orders = orderRepository.searchOrders(seller.getId(), searchCondition, pageable);

        // then
        assertThat(orders.getContent().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("주문PK를 통해 주문 상세 조회")
    void testSearchOrderDetailsByIds() {
        // given

        // when
        List<OrderDetailDto> orderDetails = orderRepository.findOrderDetailsByIds(orderIds, Sort.LATEST);

        // then
        assertThat(orderDetails.size()).isEqualTo(30);
    }
}