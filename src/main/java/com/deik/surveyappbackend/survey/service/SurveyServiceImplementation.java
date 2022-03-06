package com.deik.surveyappbackend.survey.service;

import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.survey.entity.Answer;
import com.deik.surveyappbackend.survey.entity.Question;
import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.repository.SurveyRepository;
import com.deik.surveyappbackend.survey.request.NewSurveyRequest;
import com.deik.surveyappbackend.survey.request.QuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public Survey surveyBuilder(NewSurveyRequest surveyRequest, AppUser surveyCreator){
        Survey newSurvey = new Survey();
        List<Question> newQuestions = new ArrayList<>();

        List<QuestionRequest> questions = surveyRequest.getQuestions();
        for (QuestionRequest question : questions) {
            Question newQuestion = new Question();
            List<Answer> newAnswers = new ArrayList<>();
            newQuestion.setQuestion(question.getNewQuestion());
            newQuestion.setSurvey(newSurvey);

            for (String answer : question.getNewAnswers()) {
                Answer newAnswer = new Answer();
                newAnswer.setAnswer(answer);
                newAnswer.setQuestion(newQuestion);
                newAnswer.setPicked(0);
                newAnswers.add(newAnswer);
            }

            newQuestion.setAnswers(newAnswers);
            newQuestions.add(newQuestion);
        }
        newSurvey.setAppUser(surveyCreator);
        newSurvey.setTitle(surveyRequest.getNewTitle());
        newSurvey.setDescription(surveyRequest.getNewDescription());
        newSurvey.setVisibility(surveyRequest.getNewVisibility());
        newSurvey.setQuestions(newQuestions);

        return newSurvey;
    }

}
