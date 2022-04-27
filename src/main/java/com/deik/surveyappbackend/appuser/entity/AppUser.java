package com.deik.surveyappbackend.appuser.entity;

import com.deik.surveyappbackend.survey.entity.Survey;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class AppUser {

    @Id
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    @OneToMany(
            mappedBy = "appUser",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Survey> surveys = new ArrayList<>();
}
