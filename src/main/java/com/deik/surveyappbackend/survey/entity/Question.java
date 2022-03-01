package com.deik.surveyappbackend.survey.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "survey"})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long question_id;
    private String question;
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;
    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();
}
