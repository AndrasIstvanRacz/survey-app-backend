package com.deik.surveyappbackend.appuser.repository;

import com.deik.surveyappbackend.appuser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findByUsername(String username);
}
