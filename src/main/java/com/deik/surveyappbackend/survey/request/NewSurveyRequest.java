package com.deik.surveyappbackend.survey.request;

import lombok.Data;

import java.util.List;

@Data
public class NewSurveyRequest {
    private String id;
    private String newTitle;
    private String newDescription;
    private Boolean newVisibility;
    private List<QuestionRequest> questions;
}
