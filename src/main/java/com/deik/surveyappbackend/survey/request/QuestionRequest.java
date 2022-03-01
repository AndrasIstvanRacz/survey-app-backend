package com.deik.surveyappbackend.survey.request;

import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    private String newQuestion;
    private List<String> newAnswers;
}
