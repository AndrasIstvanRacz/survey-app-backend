package com.deik.surveyappbackend.survey.service;

import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SurveyServiceImplementation implements SurveyService {

    private final SurveyRepository surveyRepository;

    @Override
    public Survey saveSurvey(Survey survey) {
        log.info("Saving new survey {} to the database", survey.getTitle());
        return surveyRepository.save(survey);
    }

}
