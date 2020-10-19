package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public User updateUser(User user) {
        entityManager.merge(user);
        return user;
    }

    public UserAuth updateAuthToken(UserAuth userAuth) {
        entityManager.merge(userAuth);
        return userAuth;
    }

    public UserAuth createAuthToken (UserAuth userAuth) {
        entityManager.persist(userAuth);
        return userAuth;
    }

    public User getUserByUuid(final String uuid) {
        try {
         return  entityManager.createNamedQuery("userByUuid" , User.class ).setParameter("uuid" , uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail" , User.class).setParameter("email" , email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername" , User.class).setParameter("username" , username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserAuth getUserAuthByAccessToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken" , UserAuth.class).setParameter("accessToken" , accessToken).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public int deleteUser(final String uuid) {
        return entityManager.createQuery("delete from User u where u.uuid = :uuid").setParameter("uuid" , uuid).executeUpdate();
    }
}
