package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.GET , path = "/userprofile/{userId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserProfile (@PathVariable("userId") final String userId ,
                                                               @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        User user = userBusinessService.getUserByUuid(userId);
        if (user == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid does not exist");
        }

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();

        userDetailsResponse.setFirstName(user.getFirstName());
        userDetailsResponse.setAboutMe(user.getAboutme());
        userDetailsResponse.setLastName(user.getLastName());
        userDetailsResponse.setEmailAddress(user.getEmail());
        userDetailsResponse.setCountry(user.getCountry());
        userDetailsResponse.setDob(user.getDob());
        userDetailsResponse.setUserName(user.getUsername());
        userDetailsResponse.setContactNumber(user.getContactnumber());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse , HttpStatus.OK);
    }

}
