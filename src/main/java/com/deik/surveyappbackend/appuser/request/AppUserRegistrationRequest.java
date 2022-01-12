package com.deik.surveyappbackend.appuser.request;

import lombok.Data;

@Data
public class AppUserRegistrationRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
}
