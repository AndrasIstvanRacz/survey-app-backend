package com.deik.surveyappbackend.survey.projections;

import java.util.UUID;

public interface SurveyProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getUsername();
}
