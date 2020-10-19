package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuth authenticate(final String username , final  String password) throws AuthenticationFailedException {
        User user = userDao.getUserByUsername(username);
        UserAuth userAuth = new UserAuth();
        if (user == null) {
            throw new AuthenticationFailedException("ATH-001" , "This username does not exist ");
        }
        String encryptedPassword = PasswordCryptographyProvider.encrypt(password , user.getSalt());
        if (encryptedPassword.equals(user.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuth.setUser(user);
            String token = jwtTokenProvider.generateToken(user.getUuid() , now , expiresAt);
            userAuth.setAccess_token(token);
            userAuth.setUuid(user.getUuid());
            userAuth.setLogin_at(now);
            userAuth.setExpires_at(expiresAt);
            userDao.createAuthToken(userAuth);
        } else {
            throw new AuthenticationFailedException("ATH-002" , "Password failed");
        }
        return userAuth;
    }

    public UserAuth authenticate (final String accessToken) throws AuthorizationFailedException {
        UserAuth userAuth = userDao.getUserAuthByAccessToken(accessToken);
        if (userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001" , "User has not signed in");
        }
        if (userAuth.getExpires_at().isBefore(ZonedDateTime.now())) {
          //  throw new AuthorizationFailedException("ATHR-001" , "User has not signed in");
        }
        if (userAuth.getLogout_at() != null) {
            throw new AuthorizationFailedException("ATHR-002" , "User is signed out.Sign in first");
        }

        return userAuth;
    }


}
