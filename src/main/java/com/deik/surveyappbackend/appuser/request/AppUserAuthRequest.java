package com.deik.surveyappbackend.appuser.request;

import lombok.Data;

@Data
public class AppUserAuthRequest {
    private String username;
    private String password;
}
