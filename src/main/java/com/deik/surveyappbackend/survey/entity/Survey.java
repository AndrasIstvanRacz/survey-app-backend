package com.deik.surveyappbackend.survey.entity;


import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "appUser"})
public class Survey{

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    private String survey_id;
    private String title;
    private String description;
    @Column(nullable = false)
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