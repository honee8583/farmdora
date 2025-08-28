package com.farmdora.farmdora.search.opinion.repository;

import com.farmdora.farmdora.config.AuditConfig;
import com.farmdora.farmdora.entity.Question;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.Seller;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ProcessType;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditConfig.class)
class CustomQuestionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    private Seller seller;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .phoneNum("010-1234-5678")
                .name("user")
                .build();
        em.persist(user);

        seller = Seller.builder()
                .name("seller")
                .user(user)
                .build();
        em.persist(seller);

        for (int i = 1; i <= 10; i++) {
            Sale sale = Sale.builder()
                    .title("상추" + i)
                    .seller(seller)
                    .build();
            em.persist(sale);

            Question question = Question.builder()
                    .user(user)
                    .sale(sale)
                    .title("title" + i)
                    .content("content" + i)
                    .isProcess(false)
                    .isBlind(false)
                    .build();
            em.persist(question);
        }
    }

    @Test
    @DisplayName("상품명으로 판매자의 문의 목록 조회 QueryDsl 실행 테스트")
    void testSearchQuestions() {
        // given
        Integer sellerId = user.getUserId();
        System.out.println("ID: " + sellerId);
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .searchType(SearchType.PRODUCT)
                .keyword("상추")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .processTypes(List.of(ProcessType.WAIT))
                .sort(Sort.OLDEST)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(sellerId, searchCondition, pageable);

        // then
        System.out.println(questions.getContent());
        List<QuestionResponseDto> questionResult = questions.getContent();
        assertThat(questionResult.size()).isEqualTo(10);
        assertThat(questions.getNumber()).isEqualTo(0);
        assertThat(questions.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("작성자명으로 판매자의 문의 목록 조회 QueryDsl 실행 테스트")
    void testSearchQuestionsByWriter() {
        // given
        Integer sellerId = user.getUserId();
        System.out.println("ID: " + sellerId);
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .searchType(SearchType.BUYER)
                .keyword("user")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .processTypes(List.of(ProcessType.WAIT))
                .sort(Sort.OLDEST)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(sellerId, searchCondition, pageable);

        // then
        System.out.println(questions.getContent());
        List<QuestionResponseDto> questionResult = questions.getContent();
        assertThat(questionResult.size()).isEqualTo(10);
        assertThat(questions.getNumber()).isEqualTo(0);
        assertThat(questions.getTotalElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("SearchPeriod로 판매자의 문의 목록 조회 QueryDsl 실행 테스트")
    void testSearchQuestionsBySearchPeriod() {
        // given
        Integer sellerId = user.getUserId();
        System.out.println("ID: " + sellerId);
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .searchType(SearchType.BUYER)
                .keyword("user")
                .searchPeriod(SearchPeriod.TODAY)
                .processTypes(List.of(ProcessType.WAIT))
                .sort(Sort.OLDEST)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(sellerId, searchCondition, pageable);

        // then
        System.out.println(questions.getContent());
        List<QuestionResponseDto> questionResult = questions.getContent();
        assertThat(questionResult.size()).isEqualTo(10);
        assertThat(questions.getNumber()).isEqualTo(0);
        assertThat(questions.getTotalElements()).isEqualTo(10);
    }
}