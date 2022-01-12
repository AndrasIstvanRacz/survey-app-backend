package com.deik.surveyappbackend.appuser.service;

import com.deik.surveyappbackend.appuser.repository.AppUserRepository;
import com.deik.surveyappbackend.appuser.entity.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImplementation implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("Username found in database {}", username);
        }

        return new User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving new user {} {} to the database",
                appUser.getFirstname(),
                appUser.getLastname());
        return appUserRepository.save(appUser);
    }

}
