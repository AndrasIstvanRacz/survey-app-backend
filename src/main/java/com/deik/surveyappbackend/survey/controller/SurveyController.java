package com.deik.surveyappbackend.survey.controller;

import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.appuser.repository.AppUserRepository;
import com.deik.surveyappbackend.jwt.util.JwtUtil;
import com.deik.surveyappbackend.survey.entity.Answer;
import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.projections.SurveyProjection;
import com.deik.surveyappbackend.survey.repository.AnswerRepository;
import com.deik.surveyappbackend.survey.repository.SurveyRepository;
import com.deik.surveyappbackend.survey.request.NewSurveyRequest;
import com.deik.surveyappbackend.survey.request.SaveAnswerRequest;
import com.deik.surveyappbackend.survey.service.SurveyService;

import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;

    @GetMapping("/findAllVisible")
    public ResponseEntity<List<SurveyProjection>> findAllVisible() {
        return ResponseEntity.status(HttpStatus.OK).body(surveyRepository.findAllByVisibility());
    }


    @GetMapping("/getByIdWithoutAuth")
    public ResponseEntity<Survey> getByIdWithoutAuth(@RequestParam String surveyId) {
        Survey surveyById = surveyRepository.getById(surveyId);

        if(surveyById.getVisibility())
            return ResponseEntity.status(HttpStatus.OK).body(surveyById);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @GetMapping("/getByIdWithAuth")
    public ResponseEntity<Survey> getByIdWithAuth(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestParam String surveyId) {
        String surveyCreatorUsername;
        String token = authorizationHeader.substring(7);

        try {
            surveyCreatorUsername = jwtUtil.extractUsername(token);
        }catch(SignatureException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Survey surveyById = surveyRepository.getById(surveyId);

        if(surveyCreatorUsername.equals(surveyById.getAppUser().getUsername()))
            return ResponseEntity.status(HttpStatus.OK).body(surveyById);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @GetMapping("/findAllByUsername")
    public ResponseEntity<List<SurveyProjection>> findAllByUsername(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String surveyCreatorUsername;

        try {
            surveyCreatorUsername = jwtUtil.extractUsername(token);
        }catch(SignatureException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        AppUser surveyCreator = appUserRepository.findByUsername(surveyCreatorUsername);

        return ResponseEntity.status(HttpStatus.OK).body(surveyRepository.findAllByAppUser(surveyCreator));
    }


    @PostMapping("/addNew")
    public ResponseEntity<String> addNew(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestBody NewSurveyRequest surveyRequest) {
        String surveyCreatorUsername;
        String token = authorizationHeader.substring(7);
        try {
            surveyCreatorUsername = jwtUtil.extractUsername(token);
        }catch(SignatureException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        AppUser surveyCreator = appUserRepository.findByUsername(surveyCreatorUsername);

        Survey newSurvey = surveyService.surveyBuilder(surveyRequest, surveyCreator);

        try {
            surveyService.saveSurvey(newSurvey);
            return ResponseEntity.status(HttpStatus.OK).body("Survey saved into the database");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database not working");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestBody NewSurveyRequest surveyRequest) {
        String usernameFromToken;
        String token = authorizationHeader.substring(7);
        try {
            usernameFromToken = jwtUtil.extractUsername(token);
        }catch(SignatureException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Survey oldSurvey = surveyRepository.getById(surveyRequest.getId());

        String surveyOwner = oldSurvey.getAppUser().getUsername();
        if(!usernameFromToken.equals(surveyOwner))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        surveyRepository.deleteById(surveyRequest.getId());
        AppUser surveyCreator = appUserRepository.findByUsername(usernameFromToken);
        Survey newSurvey = surveyService.surveyBuilder(surveyRequest, surveyCreator);

        try {
            surveyService.saveSurvey(newSurvey);
            return ResponseEntity.status(HttpStatus.OK).body("Survey saved into the database");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database not working");
        }
    }

    @PostMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestParam String surveyId) {
        String usernameFromToken;
        String token = authorizationHeader.substring(7);
        try {
            usernameFromToken = jwtUtil.extractUsername(token);
        }catch(SignatureException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String surveyOwner = surveyRepository.getById(surveyId).getAppUser().getUsername();
        System.out.println(surveyOwner);
        System.out.println(usernameFromToken);
        if(!usernameFromToken.equals(surveyOwner))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        surveyRepository.deleteById(surveyId);
        return ResponseEntity.status(HttpStatus.OK).body("Survey deleted from the database");
    }

    @PostMapping("/saveAnswers")
    public ResponseEntity<String> saveAnswers(@RequestBody SaveAnswerRequest pickedAnswers) {

        for (Long i: pickedAnswers.getPickedAnswers()) {
            Answer answer = answerRepository.getById(i);
            answer.setPicked(answer.getPicked() + 1);
            answerRepository.save(answer);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Answers saved");

    }
}
