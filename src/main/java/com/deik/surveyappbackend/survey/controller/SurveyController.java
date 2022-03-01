package com.deik.surveyappbackend.survey.controller;


import com.deik.surveyappbackend.appuser.entity.AppUser;
import com.deik.surveyappbackend.appuser.repository.AppUserRepository;
import com.deik.surveyappbackend.appuser.service.AppUserServiceImplementation;
import com.deik.surveyappbackend.survey.entity.Answer;
import com.deik.surveyappbackend.survey.entity.Question;
import com.deik.surveyappbackend.survey.entity.Survey;
import com.deik.surveyappbackend.survey.projections.SurveyProjection;
import com.deik.surveyappbackend.survey.repository.SurveyRepository;
import com.deik.surveyappbackend.survey.request.NewSurveyRequest;
import com.deik.surveyappbackend.survey.request.QuestionRequest;
import com.deik.surveyappbackend.survey.service.SurveyService;
import com.deik.surveyappbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final JwtUtil jwtUtil;
    private final AppUserServiceImplementation userDetailsService;
    private final AppUserRepository appUserRepository;
    private final SurveyRepository surveyRepository;

    @GetMapping("/findAllVisible")
    public ResponseEntity<List<SurveyProjection>> findAllVisible() {
        return ResponseEntity.status(HttpStatus.OK).body(surveyRepository.findAllByVisibility());
    }

    @GetMapping("/getByIdWithoutAuth")
    public ResponseEntity<Survey> getByIdWithoutAuth(@RequestBody Long surveyId) {

        Survey surveyById = surveyRepository.getById(surveyId);

        if(surveyById.getVisibility())
            return ResponseEntity.status(HttpStatus.OK).body(surveyById);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/getByIdWithAuth")
    public ResponseEntity<Survey> getByIdWithAuth(@RequestParam String token,
                                                  @RequestBody Long surveyId) {
        AppUser surveyCreator;
        String surveyCreatorUsername;
        UserDetails userDetails;
        Survey surveyById;

        surveyCreatorUsername = jwtUtil.extractUsername(token);
        surveyCreator = appUserRepository.findByUsername(surveyCreatorUsername);

        try {
            userDetails = userDetailsService.loadUserByUsername(surveyCreator.getUsername());
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(!jwtUtil.validateToken(token, userDetails))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        surveyById = surveyRepository.getById(surveyId);

        if(surveyCreatorUsername.equals(surveyById.getAppUser().getUsername()))
            return ResponseEntity.status(HttpStatus.OK).body(surveyById);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/findAllByUsername")
    public ResponseEntity<List<SurveyProjection>> findAllByUsername(@RequestParam String token) {

        UserDetails userDetails;
        String surveyCreatorUsername = jwtUtil.extractUsername(token);

        AppUser surveyCreator = appUserRepository.findByUsername(surveyCreatorUsername);

        try {
            userDetails = userDetailsService.loadUserByUsername(surveyCreator.getUsername());
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (!jwtUtil.validateToken(token, userDetails))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(surveyRepository.findAllByAppUser(surveyCreator));

    }

    @PostMapping("/addNew")
    public ResponseEntity<String> addNewSurvey(@RequestParam String token,
                                               @RequestBody NewSurveyRequest surveyRequest) {
        String surveyCreatorUsername = jwtUtil.extractUsername(token);
        AppUser surveyCreator = appUserRepository.findByUsername(surveyCreatorUsername);
        UserDetails userDetails = userDetailsService.loadUserByUsername(surveyCreator.getUsername());

        if (!jwtUtil.validateToken(token, userDetails))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session");

        Survey newSurvey = new Survey();
        List<Question> newQuestions = new ArrayList<>();

        List<QuestionRequest> questions = surveyRequest.getQuestions();
        for (QuestionRequest question : questions) {
            Question newQuestion = new Question();
            List<Answer> newAnswers = new ArrayList<>();
            newQuestion.setQuestion(question.getNewQuestion());
            newQuestion.setSurvey(newSurvey);

            for (String answer : question.getNewAnswers()) {
                Answer newAnswer = new Answer();
                newAnswer.setAnswer(answer);
                newAnswer.setQuestion(newQuestion);
                newAnswer.setPicked(0);
                newAnswers.add(newAnswer);
            }

            newQuestion.setAnswers(newAnswers);
            newQuestions.add(newQuestion);
        }

        newSurvey.setAppUser(surveyCreator);
        newSurvey.setTitle(surveyRequest.getNewTitle());
        newSurvey.setDescription(surveyRequest.getNewDescription());
        newSurvey.setVisibility(surveyRequest.getNewVisibility());
        newSurvey.setQuestions(newQuestions);

        try {
            surveyService.saveSurvey(newSurvey);
            return ResponseEntity.status(HttpStatus.OK).body("Survey saved into the database");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database not working");
        }
    }
}
