package com.deik.surveyappbackend.survey.service;


import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.request.NewSurveyRequest;


public interface SurveyService {
    Survey saveSurvey(Survey survey);
    Survey surveyBuilder(NewSurveyRequest surveyRequest, AppUser surveyCreator);

}
