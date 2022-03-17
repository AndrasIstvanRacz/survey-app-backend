package com.deik.surveyappbackend.survey.projections;

import java.util.UUID;

public interface SurveyProjection {
    String getId();
    String getTitle();
    String getDescription();
    String getUsername();
}
