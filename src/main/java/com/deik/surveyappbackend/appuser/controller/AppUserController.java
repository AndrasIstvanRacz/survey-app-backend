package com.deik.surveyappbackend.appuser.controller;

import com.deik.surveyappbackend.appuser.repository.AppUserRepository;
import com.deik.surveyappbackend.appuser.request.AppUserAuthRequest;
import com.deik.surveyappbackend.appuser.request.AppUserRegistrationRequest;
import com.deik.surveyappbackend.appuser.service.AppUserService;
import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.appuser.service.AppUserServiceImplementation;
import com.deik.surveyappbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins="http://localhost:3000")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final AppUserServiceImplementation userDetailsService;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/registration")
    public ResponseEntity<String>saveUser(@RequestBody AppUserRegistrationRequest newUser){
        if(appUserRepository.findByUsername(newUser.getUsername()) != null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username already exists");

        AppUser newAppUser = new AppUser();

        newAppUser.setUsername(newUser.getUsername());
        newAppUser.setFirstname(newUser.getFirstname());
        newAppUser.setLastname(newUser.getLastname());
        newAppUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

        appUserService.saveAppUser(newAppUser);

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(newAppUser.getUsername());

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/user/registration")
                        .toUriString());

        return ResponseEntity.created(uri).body(jwtUtil.generateToken(userDetails));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AppUserAuthRequest authenticationRequest)  {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(jwtUtil.generateToken(userDetails));
    }
}
