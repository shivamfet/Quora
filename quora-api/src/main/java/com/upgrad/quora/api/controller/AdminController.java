package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
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
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.DELETE , path = "/admin/user/{userId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") String userId , @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuth userAuth = authenticationService.authenticate(authorization);
        User user = userAuth.getUser();
        if (!user.getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003" , "Unauthorized Access, Entered user is not an admin");
        }
        userBusinessService.deleteUser(userId);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(user.getUuid());
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse , HttpStatus.OK);
    }
}
