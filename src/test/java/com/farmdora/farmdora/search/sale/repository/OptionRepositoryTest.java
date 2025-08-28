package com.farmdora.farmdora.search.sale.repository;

import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Sale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("findAllBySale 쿼리메서드 테스트")
    void testFindAllBySale() {
        // given
        Sale sale = new Sale();
        em.persist(sale);

        Option option1 = Option.builder()
                .sale(sale)
                .build();
        em.persist(option1);

        Option option2 = Option.builder()
                .sale(sale)
                .build();
        em.persist(option2);

        em.flush();
        em.clear();

        // when
        List<Option> options = optionRepository.findAllBySale(sale);

        // then
        assertThat(options.size()).isEqualTo(2);
    }
}