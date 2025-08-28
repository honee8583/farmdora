package com.farmdora.farmdora.product.opinion.repository.impl;

import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ProcessType;
import com.farmdora.farmdora.opinion.dto.QQuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.repository.CustomQuestionRepository;
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

import static com.farmdora.farmdora.entity.QQuestion.question;
import static com.farmdora.farmdora.entity.QSale.sale;
import static com.farmdora.farmdora.entity.QSeller.seller;

@Slf4j
public class CustomQuestionRepositoryImpl implements CustomQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public CustomQuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<QuestionResponseDto> searchQuestions(Integer userId, OpinionSearchRequestDto searchCondition, Pageable pageable) {
        List<QuestionResponseDto> questions = queryFactory
                .select(new QQuestionResponseDto(
                        question.id,
                        question.user.name,
                        question.sale.title,
                        question.title,
                        question.createdDate,
                        question.isProcess
                ))
                .from(question)
                .join(question.sale, sale)
                .join(sale.seller, seller)
                .where(
                        sale.seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(),
                                searchCondition.getSearchPeriod()),
                        isProcessEq(searchCondition.getProcessTypes())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(ordersOrderBy(searchCondition.getSort()), question.id.desc())
                .fetch();

        log.info("문의 목록: {}", questions);

        JPAQuery<Long> countQuery = queryFactory
                .select(question.count())
                .from(question)
                .join(question.sale, sale)
                .join(sale.seller, seller)
                .where(
                        seller.user.userId.eq(userId),
                        keywordContains(searchCondition.getSearchType(), searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate(),
                                searchCondition.getSearchPeriod()),
                        isProcessEq(searchCondition.getProcessTypes())
                );

        return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
    }

    private BooleanExpression keywordContains(SearchType searchType, String keyword) {
        if (searchType != null && StringUtils.hasText(keyword)) {
            if (searchType.equals(SearchType.PRODUCT)) {
                return sale.title.contains(keyword);
            } else if (searchType.equals(SearchType.BUYER)){
                return question.user.name.contains(keyword);
            }
        }
        return null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate, SearchPeriod searchPeriod) {
        if (startDate != null && endDate != null) {
            return question.createdDate.between(startDate, endDate);
        } else if (searchPeriod != null) {
            return periodBetween(searchPeriod);
        }
        return null;
    }

    private BooleanExpression periodBetween(SearchPeriod searchPeriod) {
        return switch (searchPeriod) {
            case TODAY -> question.createdDate.between(LocalDateTime.now().minusDays(1), LocalDateTime.now());
            case WEEK -> question.createdDate.between(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
            case ONE_MONTH -> question.createdDate.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
            case THREE_MONTHS -> question.createdDate.between(LocalDateTime.now().minusMonths(3), LocalDateTime.now());
            default -> null;
        };
    }

    private BooleanExpression isProcessEq(List<ProcessType> processTypes) {
        if (!processTypes.isEmpty()) {
            if (processTypes.contains(ProcessType.WAIT)  && processTypes.contains(ProcessType.DONE)) {
                return null;
            } else if (processTypes.contains(ProcessType.WAIT)) {
                return question.isProcess.isFalse();
            } else if (processTypes.contains(ProcessType.DONE)) {
                return question.isProcess.isTrue();
            }
        }
        return null;
    }

    private OrderSpecifier<?> ordersOrderBy(Sort sort) {
        if (sort != null) {
            return switch (sort) {
                case LATEST -> question.createdDate.desc();
                case OLDEST -> question.createdDate.asc();
                default -> null;
            };
        }
        return question.createdDate.desc();
    }
}
