package com.deik.surveyappbackend.survey.entity;


import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "appUser"})
public class Survey{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long survey_id;
    private String title;
    private String description;
    private Boolean visibility;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private AppUser appUser;
    @OneToMany(
            mappedBy = "survey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Question> questions = new ArrayList<>();

}