package com.farmdora.farmdora.order.search.repository.impl;

import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import com.farmdora.farmdora.order.dto.querydsl.QOrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.QOrderDto;
import com.farmdora.farmdora.order.repository.CustomOrderRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.farmdora.farmdora.entity.QOption.option;
import static com.farmdora.farmdora.entity.QOrder.order;
import static com.farmdora.farmdora.entity.QOrderOption.orderOption;
import static com.farmdora.farmdora.entity.QOrderStatus.orderStatus;
import static com.farmdora.farmdora.entity.QSale.sale;
import static com.farmdora.farmdora.entity.QSeller.seller;
import static com.farmdora.farmdora.entity.QUser.user;

@Slf4j
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    private final JPAQueryFactory queryFactory;

    public CustomOrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OrderDto> searchOrders(Integer userId, OrderSearchRequestDto searchCondition, Pageable pageable) {
        List<OrderDto> orders = queryFactory
                .select(
                        new QOrderDto(order.id, user.name, orderStatus.name, orderOption.price.sum(), order.createdDate)
                )
                .from(order)
                .join(user).on(order.user.eq(user))
                .join(orderStatus).on(order.status.eq(orderStatus))
                .join(orderOption).on(orderOption.order.eq(order))
                .join(option).on(orderOption.option.eq(option))
                .join(sale).on(option.sale.eq(sale))
                .join(seller).on(sale.seller.eq(seller))
                .where(
                        seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        statusIn(searchCondition.getStatusIds()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(), searchCondition.getSearchPeriod())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .groupBy(order.id)
                .orderBy(ordersOrderBy(searchCondition.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.id.countDistinct())
                .from(order)
                .join(user).on(order.user.eq(user))
                .join(orderStatus).on(order.status.eq(orderStatus))
                .join(orderOption).on(orderOption.order.eq(order))
                .join(option).on(orderOption.option.eq(option))
                .join(sale).on(option.sale.eq(sale))
                .where(
                        seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        statusIn(searchCondition.getStatusIds()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(), searchCondition.getSearchPeriod())
                );

        log.info("전체 데이터: {}", orders);

        return PageableExecutionUtils.getPage(orders, pageable, countQuery::fetchOne);
    }

    @Override
    public List<OrderDetailDto> findOrderDetailsByIds(List<Integer> orderIds, Sort sort) {
        return queryFactory
                .select(
                        new QOrderDetailDto(
                                order.id,
                                sale.id,
                                sale.title,
                                option.id,
                                option.name,
                                orderOption.quantity,
                                orderOption.price)
                )
                .from(order)
                .join(orderOption).on(orderOption.order.eq(order))
                .join(option).on(orderOption.option.eq(option))
                .join(sale).on(option.sale.eq(sale))
                .where(order.id.in(orderIds))
                .orderBy(ordersOrderBy(sort))
                .groupBy(order.id, sale.id, sale.title, option.id, option.name, orderOption.quantity, orderOption.price)
                .fetch();
    }

    private BooleanExpression statusIn(List<Short> statusIds) {
        if (statusIds == null || statusIds.isEmpty()) {
            return null;
        }
        return order.status.id.in(statusIds);
    }

    private BooleanExpression keywordContains(SearchType searchType, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        if (searchType.equals(SearchType.PRODUCT)) {
            return sale.title.contains(keyword);
        } else if (searchType.equals(SearchType.BUYER)){
            return order.user.name.contains(keyword);
        }
        return null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate, SearchPeriod searchPeriod) {
        if (startDate != null && endDate != null) {
            return order.createdDate.between(startDate, endDate);
        } else if (searchPeriod != null) {
            return periodBetween(searchPeriod);
        }
        return null;
    }

    private BooleanExpression periodBetween(SearchPeriod searchPeriod) {
        return switch (searchPeriod) {
            case TODAY -> order.createdDate.between(LocalDateTime.now().minusDays(1), LocalDateTime.now());
            case WEEK -> order.createdDate.between(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
            case ONE_MONTH -> order.createdDate.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
            case THREE_MONTHS -> order.createdDate.between(LocalDateTime.now().minusMonths(3), LocalDateTime.now());
            default -> null;
        };
    }

    private OrderSpecifier<?> ordersOrderBy(Sort sort) {
        if (sort != null) {
            return switch (sort) {
                case LATEST -> order.createdDate.desc();
                case OLDEST -> order.createdDate.asc();
                case PRICE_DESC -> orderOption.price.sum().desc();
                case PRICE_ASC -> orderOption.price.sum().asc();
                default -> null;
            };
        }
        return order.createdDate.desc();
    }
}
