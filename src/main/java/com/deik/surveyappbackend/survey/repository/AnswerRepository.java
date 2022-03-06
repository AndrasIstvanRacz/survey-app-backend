package com.deik.surveyappbackend.survey.repository;

import com.deik.surveyappbackend.survey.entity.Answer;
import com.deik.surveyappbackend.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
