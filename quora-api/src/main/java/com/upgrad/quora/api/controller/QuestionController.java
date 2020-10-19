package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST , path = "/question/create" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") String accessToken , final QuestionRequest questionRequest) throws AuthorizationFailedException {
        UserAuth userAuth = authenticationService.authenticate(accessToken);
        final Question question = new Question();
        question.setUuid(UUID.randomUUID().toString());
        question.setContent(questionRequest.getContent());
        question.setUser(userAuth.getUser());
        question.setDate(ZonedDateTime.now());
        questionService.createQuestion(question);

        QuestionResponse questionResponse = new QuestionResponse().id(question.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse , HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET , path = "/question/all" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {
        UserAuth userAuth = authenticationService.authenticate(accessToken);
        List<Question> questions = questionService.getAllQuestions();
        List<QuestionDetailsResponse> questionsDetailsResponse = new ArrayList<QuestionDetailsResponse>();

        for (Question question : questions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.getUuid()).content(question.getContent());
            questionsDetailsResponse.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionsDetailsResponse , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/question/edit/{questionId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable("questionId") String questionId , @RequestHeader("authorization") String accessToken , QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = authenticationService.authenticate(accessToken);
        Question question = questionService.getQuestionByUuid(questionId);

        if (!userAuth.getUser().getUuid().equals(question.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003" , "Only the Question owner can edit the question");
        }
        question.setContent(questionEditRequest.getContent());
        questionService.editQuestion(question);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionId).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE , path = "/question/delete/{questionId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") String questionid ,
                                                                 @RequestHeader("authorization") String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
           UserAuth userAuth= authenticationService.authenticate(accessToken);
           Question question = questionService.getQuestionByUuid(questionid);

           if (userAuth.getUser().getRole().equals("nonadmin") && !question.getUser().getUuid().equals(userAuth.getUser().getUuid())) {
               throw new AuthorizationFailedException("ATHR-003" , "Only the question owner or admin can delete the question");
           }

           questionService.deleteQuestion(question);
           QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(question.getUuid()).status("QUESTION DELETED");
           return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET , path = "/question/all/{userId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") String userId ,
                                                                               @RequestHeader("authorization") String accessToken)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuth userAuth = authenticationService.authenticate(accessToken);
        List<Question> questions = questionService.getAllQuestionsByUser(userId);
        List<QuestionDetailsResponse> questionsDetailsResponse = new ArrayList<QuestionDetailsResponse>();

        for (Question question : questions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.getUuid()).content(question.getContent());
            questionsDetailsResponse.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionsDetailsResponse , HttpStatus.OK);
    }
}
