package com.deik.surveyappbackend.survey.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "question"})
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long answer_id;
    private String answer;
    private Integer picked;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
