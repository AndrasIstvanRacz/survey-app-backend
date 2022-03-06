package com.deik.surveyappbackend.survey.repository;


import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.projections.SurveyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>{

    @Query("select s.survey_id as id, " +
            "s.title as title, " +
            "s.description as description " +
            "from Survey as s " +
            "where s.visibility = true")
    List<SurveyProjection> findAllByVisibility();

    @Query("select s.survey_id as id, " +
            "s.title as title, " +
            "s.description as description " +
            "from Survey as s " +
            "where s.appUser = ?1")
    List<SurveyProjection> findAllByAppUser(AppUser appUser);
}
