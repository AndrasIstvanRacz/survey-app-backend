package com.deik.surveyappbackend.survey.request;

import lombok.Data;

import java.util.List;

@Data
public class AnswersIdRequest {
    List<Long> pickedAnswers;
}
