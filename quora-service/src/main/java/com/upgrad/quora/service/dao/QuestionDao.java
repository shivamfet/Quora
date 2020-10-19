package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Question createQuestion(Question question) {
        entityManager.persist(question);
        return question;
    }

    public List<Question> getAllQuestions() {
        return entityManager.createNamedQuery("getAllQuestions" , Question.class).getResultList();
    }

    public List<Question> getAllQuestionsByUser(final String uuid) {
        return entityManager.createNamedQuery("getAllQuestionsByUserId" , Question.class).setParameter("uuid" , uuid).getResultList();
    }

    public Question getQuestionByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("getQuestionByUuid" , Question.class).setParameter("uuid" , uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Question editQuestion(Question question) {
        entityManager.persist(question);
        return question;
    }

    public void deleteQuestion(Question question) {
        entityManager.remove(question);
    }

}
