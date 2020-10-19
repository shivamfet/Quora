package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserBusinessService signUpBusinessService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST , path = "/user/signup" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final User user = new User();
        user.setFirstName(signupUserRequest.getFirstName());
        user.setLastName(signupUserRequest.getLastName());
        user.setUsername(signupUserRequest.getUserName());
        user.setEmail(signupUserRequest.getEmailAddress());
        user.setPassword(signupUserRequest.getPassword());
        user.setAboutme(signupUserRequest.getAboutMe());
        user.setCountry(signupUserRequest.getCountry());
        user.setContactnumber(signupUserRequest.getContactNumber());
        user.setRole("nonadmin");
        user.setDob(signupUserRequest.getDob());
        user.setUuid(UUID.randomUUID().toString());

        final User createdUser = signUpBusinessService.signUp(user);
        SignupUserResponse userResponse = new SignupUserResponse().id(user.getUuid()).status("REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuth userAuth = authenticationService.authenticate(decodedArray[0] , decodedArray[1]);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token" , userAuth.getAccess_token());
        SigninResponse signinResponse = new SigninResponse().id(userAuth.getUuid()).message("SIGNED IN SUCCESSFULLY");
        return new ResponseEntity<SigninResponse>(signinResponse , httpHeaders , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> logout(@RequestHeader("authorization") final  String accessToken) throws SignOutRestrictedException, AuthorizationFailedException {
        UserAuth userAuth = signUpBusinessService.signOut(accessToken);
        SignoutResponse signOutResponse = new SignoutResponse().id(userAuth.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return  new ResponseEntity<SignoutResponse>(signOutResponse , HttpStatus.OK);
    }
}
