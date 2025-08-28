package com.farmdora.farmdora.product.opinion.repository.impl;

import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.QReviewResponseDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.opinion.repository.CustomReviewRepository;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
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

import static com.farmdora.farmdora.entity.QOrder.order;
import static com.farmdora.farmdora.entity.QReview.review;
import static com.farmdora.farmdora.entity.QSale.sale;
import static com.farmdora.farmdora.entity.QUser.user;

@Slf4j
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    public CustomReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ReviewResponseDto> searchReviews(Integer userId, OpinionSearchRequestDto searchCondition, Pageable pageable) {
        List<ReviewResponseDto> reviews = queryFactory
                .select(
                        new QReviewResponseDto(review.id, sale.title, review.content, user.name, review.reply, review.createdDate, review.score)
                )
                .from(sale)
                .join(review).on(review.sale.eq(sale))
                .join(order).on(review.order.eq(order))
                .join(user).on(order.user.eq(user))
                .where(
                        sale.seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(),
                                searchCondition.getSearchPeriod())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(reviewsOrderBy(searchCondition.getSort()), reviewIdOrderBy(searchCondition.getSort()))
                .fetch();

        log.info("리뷰 : {}", reviews);

        JPAQuery<Long> countQuery = queryFactory
                .select(review.id.countDistinct())
                .from(sale)
                .join(review).on(review.sale.eq(sale))
                .join(order).on(review.order.eq(order))
                .join(user).on(order.user.eq(user))
                .where(
                        sale.seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(), searchCondition.getSearchPeriod())
                );

        log.info("리뷰 개수: {}", countQuery.fetchOne());

        return PageableExecutionUtils.getPage(reviews, pageable, countQuery::fetchOne);
    }

    private BooleanExpression keywordContains(SearchType searchType, String keyword) {
        if (searchType != null && StringUtils.hasText(keyword)) {
            if (searchType.equals(SearchType.PRODUCT)) {
                return sale.title.contains(keyword);
            } else if (searchType.equals(SearchType.BUYER)){
                return review.order.user.name.contains(keyword);
            }
        }
        return null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate, SearchPeriod searchPeriod) {
        if (startDate != null && endDate != null) {
            return review.createdDate.between(startDate, endDate);
        } else if (searchPeriod != null) {
            return periodBetween(searchPeriod);
        }
        return null;
    }

    private BooleanExpression periodBetween(SearchPeriod searchPeriod) {
        return switch (searchPeriod) {
            case TODAY -> review.createdDate.between(LocalDateTime.now().minusDays(1), LocalDateTime.now());
            case WEEK -> review.createdDate.between(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
            case ONE_MONTH -> review.createdDate.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
            case THREE_MONTHS -> review.createdDate.between(LocalDateTime.now().minusMonths(3), LocalDateTime.now());
            default -> null;
        };
    }

    private OrderSpecifier<?> reviewsOrderBy(Sort sort) {
        if (sort != null) {
            return switch (sort) {
                case LATEST -> review.createdDate.desc();
                case OLDEST -> review.createdDate.asc();
                default -> null;
            };
        }
        return review.createdDate.desc();
    }

    private OrderSpecifier<?> reviewIdOrderBy(Sort sort) {
        if (sort != null) {
            return switch (sort) {
                case LATEST -> review.id.desc();
                case OLDEST -> review.id.asc();
                default -> null;
            };
        }
        return review.id.desc();
    }
}