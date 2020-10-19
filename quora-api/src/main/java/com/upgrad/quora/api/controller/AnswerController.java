package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
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
public class AnswerController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST , path = "/question/{questionId}/answer/create" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable String questionId ,
                                                       @RequestHeader("authorization") String authorization,
                                                       final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        Question question = questionService.getQuestionByUuid(questionId);

        Answer answer = new Answer();
        answer.setUuid(UUID.randomUUID().toString());
        answer.setQuestion(question);
        answer.setDate(ZonedDateTime.now());
        answer.setAns(answerRequest.getAnswer());
        answerService.createAnswer(answer);

        AnswerResponse answerResponse = new AnswerResponse().id(answer.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse , HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/answer/edit/{answerId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@PathVariable String answerId ,
                                                         @RequestHeader("authorization") String authorization,
                                                         final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        Answer answer = answerService.getAnswerByUuid(answerId);

        if (!answer.getUser().getUuid().equals(userAuth.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003" , "Only the answer owner can edit the answer");
        }
        answer.setAns(answerEditRequest.getContent());
        answerService.editAnswer(answer);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answer.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE , path = "/answer/delete/{answerId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable String answerId,
                                                             @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        Answer answer = answerService.getAnswerByUuid(answerId);

        if (userAuth.getUser().getRole().equals("nonadmin") && !answer.getUser().getUuid().equals(userAuth.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003" , "Only the answer owner or admin can delete the answer");
        }

        answerService.deleteAnswer(answer);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answer.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET , path = "answer/all/{questionId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAnswersToQuestion(@PathVariable("questionId") String questionId,
                                                                            @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        Question question = questionService.getQuestionByUuid(questionId);

        answerService.getAnswersToQuestion(questionId);

        List<AnswerDetailsResponse> answersDetailsResponse = new ArrayList<AnswerDetailsResponse>();
        for (AnswerDetailsResponse answerDetailsResponse : answersDetailsResponse) {
            answersDetailsResponse.add(answerDetailsResponse);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answersDetailsResponse , HttpStatus.OK);
    }

}
