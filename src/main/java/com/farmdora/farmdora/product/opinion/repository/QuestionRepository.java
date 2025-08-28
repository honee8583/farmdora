package com.farmdora.farmdora.product.opinion.repository;

import com.farmdora.farmdora.entity.Question;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.sale.dto.QuestionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer>, CustomQuestionRepository {

    @Query("SELECT new com.farmdora.farmdora.sale.dto.QuestionResponseDto(q.id, q.title, u.name, q.content, q.answer, q.isProcess, q.createdDate) "
            + "FROM Question q JOIN q.user u "
            + "WHERE q.sale = :sale")
    Page<QuestionResponseDto> findQuestionsBySaleId(@Param("sale")Sale sale, Pageable pageable);
}
