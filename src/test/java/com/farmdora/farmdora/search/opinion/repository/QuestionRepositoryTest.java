package com.farmdora.farmdora.search.opinion.repository;

import com.farmdora.farmdora.entity.Question;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.sale.dto.QuestionResponseDto;
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
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("findQuestionsBySaleId() 쿼리메서드 테스트")
    void testFindQuestionsBySaleId() {
        // given
        Sale sale = new Sale();
        em.persist(sale);

        User user = new User();
        em.persist(user);

        for (int i = 1; i <= 10; i++) {
            Question question = Question.builder()
                    .title("question" + i)
                    .content("content" + i)
                    .isBlind(false)
                    .isProcess(true)
                    .answer("answer")
                    .sale(sale)
                    .user(user)
                    .build();
            em.persist(question);
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<QuestionResponseDto> questions = questionRepository.findQuestionsBySaleId(sale, pageable);

        // then
        assertThat(questions.getContent().size()).isEqualTo(10);
    }
}