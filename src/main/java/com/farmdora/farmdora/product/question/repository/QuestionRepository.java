package com.farmdora.farmdora.product.question.repository;

import com.farmdora.farmdorabuyer.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
