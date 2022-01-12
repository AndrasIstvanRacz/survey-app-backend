package com.deik.surveyappbackend.appuser.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AppUser {

    @Id
    private String username;
    private String firstname;
    private String lastname;
    private String password;
}
