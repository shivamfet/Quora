package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public User signUp(User user) throws SignUpRestrictedException {
        User fetchedUser = userDao.getUserByUsername(user.getUsername());
        if (fetchedUser != null) {
            throw new SignUpRestrictedException("SGR-001" , "Try any other Username, this Username has already been taken");
        }
        fetchedUser = userDao.getUserByEmail(user.getEmail());
        if (fetchedUser != null) {
            throw new SignUpRestrictedException("SGR-002" , "This user has already been registered, try with any other emailId");
        }
        String[] encryptedText = passwordCryptographyProvider.encrypt(user.getPassword());
        user.setSalt(encryptedText[0]);
        user.setPassword(encryptedText[1]);
        return userDao.createUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuth signOut (final String accessToken) throws SignOutRestrictedException {
        UserAuth userAuth = userDao.getUserAuthByAccessToken(accessToken);
        if (userAuth == null) {
            throw new SignOutRestrictedException("SGR-001" , "User is not signed in");
        }
        userAuth.setLogout_at(ZonedDateTime.now());
        userDao.updateAuthToken(userAuth);
        return userAuth;
    }

    public User getUserByUuid(final String uuid) {
        try {
            return userDao.getUserByUuid(uuid);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(String uuid) throws UserNotFoundException {
        User user = userDao.getUserByUuid(uuid);
        if (user == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid to be deleted does not exist");
        }
        userDao.deleteUser(uuid);
    }

}
