package com.deik.surveyappbackend.survey.request;

import lombok.Data;

import java.util.List;

@Data
public class SaveAnswerRequest {

    private List<Long> pickedAnswers;
}
